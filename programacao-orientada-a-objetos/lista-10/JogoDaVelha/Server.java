import java.io.Closeable;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

class PlayerConnection implements Closeable {
    private final char symbol;
    private final Socket socket;
    private final Scanner input;
    private final PrintWriter output;

    PlayerConnection(char symbol, Socket socket) throws IOException {
        this.symbol = symbol;
        this.socket = socket;
        input = new Scanner(socket.getInputStream(), StandardCharsets.UTF_8);
        output = new PrintWriter(socket.getOutputStream(), true, StandardCharsets.UTF_8);
    }

    char getSymbol() {
        return symbol;
    }

    String readMessage() {
        if (!input.hasNextLine()) {
            return null;
        }

        return input.nextLine();
    }

    void sendMessage(String message) {
        output.println(message);
    }

    @Override
    public void close() throws IOException {
        socket.close();
    }
}

public class Server {
    private static final int DEFAULT_PORT = 5000;

    private static int[] parseMove(String message) {
        String[] parts = message.trim().split("\\s+");

        if (parts.length != 3 || !Protocol.MOVE.equals(parts[0])) {
            return null;
        }

        try {
            return new int[]{Integer.parseInt(parts[1]), Integer.parseInt(parts[2])};
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static void sendBoard(BoardSystem board, PlayerConnection playerX, PlayerConnection playerO) {
        String message = Protocol.BOARD + " " + board.serialize();
        playerX.sendMessage(message);
        playerO.sendMessage(message);
    }

    private static boolean playTurn(BoardSystem board, PlayerConnection currentPlayer, PlayerConnection otherPlayer) {
        currentPlayer.sendMessage(Protocol.YOUR_TURN);
        otherPlayer.sendMessage(Protocol.OPPONENT_TURN);

        while (true) {
            String message = currentPlayer.readMessage();

            if (message == null) {
                otherPlayer.sendMessage(Protocol.OPPONENT_DISCONNECTED);
                return false;
            }

            int[] move = parseMove(message);

            if (move == null || !board.makeMove(move[0], move[1], currentPlayer.getSymbol())) {
                currentPlayer.sendMessage(Protocol.INVALID_MOVE);
                currentPlayer.sendMessage(Protocol.YOUR_TURN);
                continue;
            }

            sendBoard(board, currentPlayer, otherPlayer);

            if (board.hasWon(currentPlayer.getSymbol())) {
                currentPlayer.sendMessage(Protocol.WON);
                otherPlayer.sendMessage(Protocol.LOST);
                return false;
            }

            if (board.isBoardFull()) {
                currentPlayer.sendMessage(Protocol.DRAW);
                otherPlayer.sendMessage(Protocol.DRAW);
                return false;
            }

            return true;
        }
    }

    static void play(ServerSocket serverSocket) throws IOException {
        System.out.println("Aguardando dois jogadores...");

        try (PlayerConnection playerX = new PlayerConnection('X', serverSocket.accept());
             PlayerConnection playerO = new PlayerConnection('O', serverSocket.accept())) {
            System.out.println("Jogadores X e O conectados.");

            playerX.sendMessage(Protocol.PLAYER + " X");
            playerO.sendMessage(Protocol.PLAYER + " O");

            BoardSystem board = new BoardSystem();
            sendBoard(board, playerX, playerO);

            PlayerConnection currentPlayer = playerX;
            PlayerConnection otherPlayer = playerO;

            while (playTurn(board, currentPlayer, otherPlayer)) {
                PlayerConnection previousPlayer = currentPlayer;
                currentPlayer = otherPlayer;
                otherPlayer = previousPlayer;
            }
        }

        System.out.println("Partida encerrada.");
    }

    private static int getPort(String[] args) {
        if (args.length == 0) {
            return DEFAULT_PORT;
        }

        try {
            return Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            System.out.println("Porta inválida. Usando a porta " + DEFAULT_PORT + ".");
            return DEFAULT_PORT;
        }
    }

    public static void main(String[] args) {
        int port = getPort(args);

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Servidor iniciado na porta " + port + ".");
            play(serverSocket);
        } catch (IOException e) {
            System.out.println("Falha no servidor: " + e.getMessage());
        }
    }
}
