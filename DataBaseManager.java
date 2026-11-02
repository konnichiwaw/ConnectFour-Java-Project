import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DataBaseManager {
    // XAMPP Default MySQL Connection details
    private static final String URL = "jdbc:mysql://localhost:3306/connect4_db";
    private static final String USER = "root";
    private static final String PASSWORD = ""; // Default XAMPP password is blank

    public static Connection getConnection() throws SQLException {
    try {
        Class.forName("com.mysql.cj.jdbc.Driver");
    } catch (ClassNotFoundException e) {
        e.printStackTrace();
    }
    return DriverManager.getConnection(URL, USER, PASSWORD);
}

    
// Save match result to MySQL database
public void saveMatch(String winnerName, String winningColor, int totalMoves) {
    String query = "INSERT INTO match_history (winner_name, winning_color, total_moves, match_date) VALUES (?, ?, ?, NOW())";
    
    // Use your existing getConnection() method and separate resources with a semicolon (;)
    try (Connection conn = getConnection();
         PreparedStatement pstmt = conn.prepareStatement(query)) {
        
        pstmt.setString(1, winnerName);
        pstmt.setString(2, winningColor); // e.g., "Red" or "Yellow"
        pstmt.setInt(3, totalMoves);
        
        pstmt.executeUpdate();
        System.out.println("Match and winning color successfully logged to database!");
        
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

}