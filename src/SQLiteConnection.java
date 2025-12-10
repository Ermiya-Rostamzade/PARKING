import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteConnection {
    // مسیر دیتابیس SQLite
    // این فایل در پوشه اصلی پروژه (Project Root) ایجاد خواهد شد.
    private static final String URL = "jdbc:sqlite:parking_data.db";

    /**
     * یک اتصال به دیتابیس SQLite برقرار می کند.
     * @return شیء Connection یا null در صورت خطا
     */
    public static Connection getConnection() {
        Connection conn = null;
        try {
            // بارگذاری درایور (در JDBC مدرن معمولاً ضروری نیست اما ایمنی را تضمین می کند)
            // Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection(URL);
            return conn;
        } catch (SQLException e) {
            System.err.println("خطا در اتصال به دیتابیس: " + e.getMessage());
            return null;
        }
    }

    /**
     * متد برای ایجاد جدول اگر وجود نداشته باشد.
     */
    public static void createTable() {
        // ما فقط CarID و شماره Stack را ذخیره می کنیم.

        String sql = """
    CREATE TABLE IF NOT EXISTS Cars (
        CarID INTEGER NOT NULL,  
        StackIndex INTEGER NOT NULL,
        PositionInStack INTEGER NOT NULL,
        PRIMARY KEY (StackIndex, PositionInStack)
    );
""";
        try (Connection conn = getConnection();
             java.sql.Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("جدول Cars با موفقیت ایجاد یا تأیید شد.");
        } catch (SQLException e) {
            System.err.println("خطا در ایجاد جدول: " + e.getMessage());
        }
    }
}