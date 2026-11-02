import java.awt.*;
import javax.swing.*;

public class ConnectFourGUI extends JFrame {
    private static final int ROWS = 6;
    private static final int COLS = 7;

    private Board gameBoard;
    private Player player1;
    private Player player2;
    private Player currentPlayer;
    private boolean gameOver;

    private JButton[][] cellButtons = new JButton[ROWS][COLS];
    private JButton[] dropButtons = new JButton[COLS];
    private JLabel statusLabel = new JLabel("", JLabel.CENTER);

    public ConnectFourGUI() {
        gameBoard = new Board(ROWS, COLS);
        player1 = new Player("Player 1 (Red)", Color.RED, 1);
        player2 = new Player("Player 2 (Yellow)", Color.YELLOW, 2);
        currentPlayer = player1;
        gameOver = false;

        setTitle("Connect 4 - OOP & DSA Capstone");
        setSize(700, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Top Status Panel
        statusLabel.setText(currentPlayer.getName() + "'s Turn");
        statusLabel.setFont(new Font("Arial", Font.BOLD, 18));
        statusLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(statusLabel, BorderLayout.NORTH);

        // Center Panel: Grid and Drop Buttons
        JPanel mainBoardPanel = new JPanel(new GridLayout(ROWS + 1, COLS, 5, 5));
        mainBoardPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainBoardPanel.setBackground(new Color(20, 40, 100));

        // Initialize Drop Buttons
        for (int c = 0; c < COLS; c++) {
            final int col = c;
            dropButtons[c] = new JButton("↓");
            dropButtons[c].setFont(new Font("Arial", Font.BOLD, 16));
            dropButtons[c].setFocusPainted(false);
            dropButtons[c].addActionListener(e -> handleColumnClick(col));
            mainBoardPanel.add(dropButtons[c]);
        }

        // Initialize Game Board Cells
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                cellButtons[r][c] = new JButton();
                cellButtons[r][c].setEnabled(false);
                cellButtons[r][c].setBackground(Color.WHITE);
                cellButtons[r][c].setOpaque(true);
                mainBoardPanel.add(cellButtons[r][c]);
            }
        }

        add(mainBoardPanel, BorderLayout.CENTER);

        // Bottom Control Panel
        JPanel controlPanel = new JPanel();
        JButton resetButton = new JButton("Restart Game");
        resetButton.setFont(new Font("Arial", Font.BOLD, 14));
        resetButton.addActionListener(e -> resetGame());
        controlPanel.add(resetButton);
        add(controlPanel, BorderLayout.SOUTH);
    }

    private void handleColumnClick(int col) {
        if (gameOver) return;

        int targetRow = gameBoard.dropPiece(col, currentPlayer.getId());

       // Check Win Condition
     if (gameBoard.checkWin(targetRow, col, currentPlayer.getId())) {
       gameOver = true;
       statusLabel.setText(currentPlayer.getName() + " Wins!");
    
      // Save match to database
         try {
        DataBaseManager dbManager = new DataBaseManager();
        // Automatically grabs whether the current player is Red or Yellow
        String color = (currentPlayer.getId() == 1) ? "Red" : "Yellow";
        dbManager.saveMatch(currentPlayer.getName(), color, 0);
        System.out.println("SUCCESS: Match saved to database!");
    } catch (Exception e) {
        System.out.println("ERROR saving match: " + e.getMessage());
 }
    
    JOptionPane.showMessageDialog(this, currentPlayer.getName() + " has won the game!", "Game Over", JOptionPane.INFORMATION_MESSAGE);
    disableControls();
    return;
}
        
        if (targetRow == -1) {
            JOptionPane.showMessageDialog(this, "Column is full! Choose another column.", "Invalid Move", JOptionPane.WARNING_MESSAGE);
            return;
        }

        updateUIBoard();

        // Check Win Condition
if (gameBoard.checkWin(targetRow, col, currentPlayer.getId())) {
    gameOver = true;
    statusLabel.setText(currentPlayer.getName() + " Wins!");
    
    // --> PUT YOUR DATABASE CODE HERE <--
    DataBaseManager dbManager = new DataBaseManager();
    // Assuming your player has a method to get their color name (e.g., "Red" or "Yellow")
    // If your color is a Color object, you can pass its name or text string instead:
    dbManager.saveMatch(currentPlayer.getName(), "Red", /* total moves if tracked */ 0); 
    
    JOptionPane.showMessageDialog(this, currentPlayer.getName() + " has won the game!", "Game Over", JOptionPane.INFORMATION_MESSAGE);
    disableControls();
    return;
}

        // Check Draw Condition
        if (gameBoard.isFull()) {
            gameOver = true;
            statusLabel.setText("It's a Draw!");
            JOptionPane.showMessageDialog(this, "The board is full. It's a draw!", "Game Over", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Switch Turn
        currentPlayer = (currentPlayer == player1) ? player2 : player1;
        statusLabel.setText(currentPlayer.getName() + "'s Turn");
    }

    private void updateUIBoard() {
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                int val = gameBoard.getCell(r, c);
                if (val == 1) {
                    cellButtons[r][c].setBackground(player1.getColor());
                } else if (val == 2) {
                    cellButtons[r][c].setBackground(player2.getColor());
                } else {
                    cellButtons[r][c].setBackground(Color.WHITE);
                }
            }
        }
    }

    private void resetGame() {
        gameBoard.resetBoard();
        gameOver = false;
        currentPlayer = player1;
        statusLabel.setText(currentPlayer.getName() + "'s Turn");
        updateUIBoard();
        for (int c = 0; c < COLS; c++) {
            dropButtons[c].setEnabled(true);
        }
    }

    private void disableControls() {
        for (int c = 0; c < COLS; c++) {
            dropButtons[c].setEnabled(false);
        }
    }
}