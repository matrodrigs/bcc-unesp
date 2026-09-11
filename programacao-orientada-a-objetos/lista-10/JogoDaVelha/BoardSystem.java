class BoardSystem {
    private static final int SIZE = 3;
    private static final char EMPTY = ' ';

    private final char[][] board = {
            {EMPTY, EMPTY, EMPTY},
            {EMPTY, EMPTY, EMPTY},
            {EMPTY, EMPTY, EMPTY},
    };

    boolean isValidMove(int row, int col) {
        if (row < 0 || row >= SIZE || col < 0 || col >= SIZE) {
            return false;
        }

        return board[row][col] == EMPTY;
    }

    boolean makeMove(int row, int col, char player) {
        if ((player != 'X' && player != 'O') || !isValidMove(row, col)) {
            return false;
        }

        board[row][col] = player;
        return true;
    }

    boolean isBoardFull() {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (board[row][col] == EMPTY) {
                    return false;
                }
            }
        }

        return true;
    }

    boolean hasWon(char player) {
        if (player != 'X' && player != 'O') {
            return false;
        }

        for (int i = 0; i < SIZE; i++) {
            if (board[i][0] == player && board[i][1] == player && board[i][2] == player) {
                return true;
            }

            if (board[0][i] == player && board[1][i] == player && board[2][i] == player) {
                return true;
            }
        }

        if (board[0][0] == player && board[1][1] == player && board[2][2] == player) {
            return true;
        }

        return board[0][2] == player && board[1][1] == player && board[2][0] == player;
    }

    String serialize() {
        StringBuilder state = new StringBuilder(SIZE * SIZE);

        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                char value = board[row][col];
                state.append(value == EMPTY ? Protocol.EMPTY_CELL : value);
            }
        }

        return state.toString();
    }
}
