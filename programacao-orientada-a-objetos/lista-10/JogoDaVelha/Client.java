import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Scanner;

public class Client {
    private static final String DEFAULT_HOST = "127.0.0.1";
    private static final int DEFAULT_PORT = 5000;

    private final String host;
    private final int port;
    private final JFrame window = new JFrame("Jogo da Velha");
    private final JLabel playerInfo = new JLabel("Jogador: aguardando", SwingConstants.CENTER);
    private final JLabel status = new JLabel("Conectando ao servidor...", SwingConstants.CENTER);
    private final JButton[] cells = new JButton[9];
    private final char[] board = new char[9];

    private volatile Socket socket;
    private volatile PrintWriter output;
    private boolean myTurn;

    Client(String host, int port) {
        this.host = host;
        this.port = port;
        Arrays.fill(board, Protocol.EMPTY_CELL);
        createInterface();
    }

    private void createInterface() {
        JPanel boardPanel = new JPanel(new GridLayout(3, 3, 6, 6));
        boardPanel.setBorder(BorderFactory.createEmptyBorder(12, 12, 8, 12));

        for (int i = 0; i < cells.length; i++) {
            int position = i;
            JButton button = new JButton();
            button.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 42));
            button.setFocusPainted(false);
            button.addActionListener(e -> sendMove(position));
            cells[i] = button;
            boardPanel.add(button);
        }

        playerInfo.setBorder(BorderFactory.createEmptyBorder(12, 12, 0, 12));
        status.setBorder(BorderFactory.createEmptyBorder(6, 12, 12, 12));

        window.add(playerInfo, BorderLayout.NORTH);
        window.add(boardPanel, BorderLayout.CENTER);
        window.add(status, BorderLayout.SOUTH);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(340, 390);
        window.setResizable(false);
        window.setLocationRelativeTo(null);
        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                closeConnection();
            }
        });

        updateButtons();
    }

    void show() {
        window.setVisible(true);

        Thread connectionThread = new Thread(this::connectAndListen, "game-connection");
        connectionThread.setDaemon(true);
        connectionThread.start();
    }

    private void connectAndListen() {
        try (Socket activeSocket = new Socket()) {
            activeSocket.connect(new InetSocketAddress(host, port), 5000);
            socket = activeSocket;

            boolean finalMessageReceived;

            try (Scanner input = new Scanner(activeSocket.getInputStream(), StandardCharsets.UTF_8);
                 PrintWriter serverOutput = new PrintWriter(activeSocket.getOutputStream(), true, StandardCharsets.UTF_8)) {
                output = serverOutput;
                setStatus("Conectado. Aguardando outro jogador...");
                finalMessageReceived = listenToServer(input);
            }

            if (!finalMessageReceived) {
                finishGame("Conexão encerrada.");
            }
        } catch (IOException e) {
            finishGame("Não foi possível conectar ao servidor.");
        } finally {
            output = null;
            socket = null;
        }
    }

    private boolean listenToServer(Scanner input) {
        while (input.hasNextLine()) {
            String message = input.nextLine();
            SwingUtilities.invokeLater(() -> handleMessage(message));

            if (isFinalMessage(message)) {
                return true;
            }
        }

        return false;
    }

    private boolean isFinalMessage(String message) {
        return Protocol.WON.equals(message)
                || Protocol.LOST.equals(message)
                || Protocol.DRAW.equals(message)
                || Protocol.OPPONENT_DISCONNECTED.equals(message);
    }

    private void handleMessage(String message) {
        if (message.startsWith(Protocol.PLAYER + " ")) {
            char symbol = message.charAt(message.length() - 1);
            playerInfo.setText("Jogador: " + symbol);
            status.setText("Você joga com " + symbol + ".");
        } else if (message.startsWith(Protocol.BOARD + " ")) {
            updateBoard(message.substring(Protocol.BOARD.length() + 1));
        } else if (Protocol.YOUR_TURN.equals(message)) {
            myTurn = true;
            status.setText("Sua vez.");
            updateButtons();
        } else if (Protocol.OPPONENT_TURN.equals(message)) {
            myTurn = false;
            status.setText("Aguarde a jogada do adversário.");
            updateButtons();
        } else if (Protocol.INVALID_MOVE.equals(message)) {
            myTurn = false;
            status.setText("Movimento inválido. Escolha outra casa.");
            updateButtons();
        } else if (Protocol.WON.equals(message)) {
            finishGame("Você venceu!");
        } else if (Protocol.LOST.equals(message)) {
            finishGame("Você perdeu.");
        } else if (Protocol.DRAW.equals(message)) {
            finishGame("Empate.");
        } else if (Protocol.OPPONENT_DISCONNECTED.equals(message)) {
            finishGame("O adversário desconectou.");
        }
    }

    private void updateBoard(String state) {
        if (state.length() != board.length) {
            finishGame("O servidor enviou um tabuleiro inválido.");
            return;
        }

        for (int i = 0; i < board.length; i++) {
            board[i] = state.charAt(i);
        }

        updateButtons();
    }

    private void sendMove(int position) {
        PrintWriter serverOutput = output;

        if (!myTurn || board[position] != Protocol.EMPTY_CELL || serverOutput == null) {
            return;
        }

        myTurn = false;
        status.setText("Jogada enviada...");
        updateButtons();

        int row = position / 3;
        int column = position % 3;
        serverOutput.println(Protocol.MOVE + " " + row + " " + column);
    }

    private void updateButtons() {
        for (int i = 0; i < cells.length; i++) {
            char value = board[i];
            cells[i].setText(value == Protocol.EMPTY_CELL ? "" : String.valueOf(value));
            cells[i].setEnabled(myTurn && value == Protocol.EMPTY_CELL);
        }
    }

    private void setStatus(String message) {
        SwingUtilities.invokeLater(() -> status.setText(message));
    }

    private void finishGame(String message) {
        SwingUtilities.invokeLater(() -> {
            myTurn = false;
            status.setText(message);
            updateButtons();
        });
    }

    private void closeConnection() {
        Socket activeSocket = socket;

        if (activeSocket == null) {
            return;
        }

        try {
            activeSocket.close();
        } catch (IOException ignored) {
        }
    }

    private static int getPort(String[] args) {
        if (args.length < 2) {
            return DEFAULT_PORT;
        }

        try {
            return Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            return DEFAULT_PORT;
        }
    }

    public static void main(String[] args) {
        String host = args.length > 0 ? args[0] : DEFAULT_HOST;
        int port = getPort(args);

        SwingUtilities.invokeLater(() -> new Client(host, port).show());
    }
}
