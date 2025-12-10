import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteConnection {


    private static final String URL = "jdbc:sqlite:parking_data.db";


    public static Connection getConnection() {
        Connection conn = null;
        try {

            conn = DriverManager.getConnection(URL);
            return conn;
        } catch (SQLException e) {
            System.err.println("خطا در اتصال به دیتابیس: " + e.getMessage());
            return null;
        }
    }


    public static void createTable() {


        String sql = """
    CREATE TABLE IF NOT EXISTS Cars (
        CarID INTEGER NOT NULL,  
        StackIndex INTEGER NOT NULL,
        PositionInStack INTEGER NOT NULL,
        PRIMARY KEY (StackIndex, PositionInStack)
    );
""";
        String sqlIndex = """
        CREATE INDEX IF NOT EXISTS idx_stack_position
        ON Cars (StackIndex, PositionInStack);
    """;
        try (Connection conn = getConnection();
             java.sql.Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            stmt.execute(sqlIndex);
            System.out.println("جدول Cars با موفقیت ایجاد شد.");
        } catch (SQLException e) {
            System.err.println("خطا در ایجاد جدول: " + e.getMessage());
        }
    }
}