public class Board {
    private final int rows;
    private final int cols;
    private int[][] grid;

    public Board(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.grid = new int[rows][cols];
    }

    public void resetBoard() {
        this.grid = new int[rows][cols];
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public int getCell(int r, int c) {
        return grid[r][c];
    }

    // Drops a piece into a column and returns the row it landed on (-1 if full)
    public int dropPiece(int col, int playerId) {
        for (int r = rows - 1; r >= 0; r--) {
            if (grid[r][col] == 0) {
                grid[r][col] = playerId;
                return r;
            }
        }
        return -1; // Column full
    }

    public boolean isFull() {
        for (int c = 0; c < cols; c++) {
            if (grid[0][c] == 0) return false;
        }
        return true;
    }

    // Algorithmic check scanning 4 directions: Horizontal, Vertical, Diagonals
    public boolean checkWin(int row, int col, int player) {
        return countConsecutive(row, col, 0, 1, player) + countConsecutive(row, col, 0, -1, player) >= 3 || // Horizontal
               countConsecutive(row, col, 1, 0, player) >= 3 || // Vertical
               countConsecutive(row, col, 1, 1, player) + countConsecutive(row, col, -1, -1, player) >= 3 || // Diagonal \
               countConsecutive(row, col, 1, -1, player) + countConsecutive(row, col, -1, 1, player) >= 3;    // Diagonal /
    }

    private int countConsecutive(int row, int col, int rowDir, int colDir, int player) {
        int count = 0;
        int r = row + rowDir;
        int c = col + colDir;
        while (r >= 0 && r < rows && c >= 0 && c < cols && grid[r][c] == player) {
            count++;
            r += rowDir;
            c += colDir;
        }
        return count;
    }
}