import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CarDAO {

    /**
     * ذخیره کردن یک Car در دیتابیس.
     */
    public void saveCar(Car car, int stackIndex, int positionInStack) {
        String sql = "INSERT INTO Cars(CarID, StackIndex, PositionInStack) VALUES(?, ?, ?)";

        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, car.getId());
            pstmt.setInt(2, stackIndex +1);
            pstmt.setInt(3, positionInStack);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            // اگر CarID تکراری باشد (چون Primary Key است) این خطا رخ می دهد.
            // در حالت Push نباید تکراری باشد.
            System.err.println("خطا در ذخیره کردن Car: " + e.getMessage());
        }
    }

    /**
     * حذف یک Car از دیتابیس بر اساس شناسه.
     */
    public void deleteCar(int carId) {
        String sql = "DELETE FROM Cars WHERE CarID = ?";

        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, carId);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("خطا در حذف Car: " + e.getMessage());
        }
    }

    /**
     * حذف تمام ماشین های یک Stack و ذخیره مجدد آن برای حفظ ترتیب Pop و Push (به روزرسانی).
     * در عمل، برای یک Stack، حذف و درج مجدد بهترین راه برای حفظ ترتیب استک است.
     */
// در کلاس CarDAO.java
    public void deleteStackCars(int stackIndex) {
        String sql = "DELETE FROM Cars WHERE StackIndex = ?";

        try (Connection conn = SQLiteConnection.getConnection();
             java.sql.PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // اندیس حافظه (0, 1, 2) را با 1 جمع می‌کنیم تا اندیس دیتابیس (1, 2, 3) را هدف قرار دهیم
            pstmt.setInt(1, stackIndex + 1);
            pstmt.executeUpdate();

            // --- نکته مهم: اگر حذف موفق بود، این خط اجرا می‌شود ---
            System.out.println("حذف موفقیت آمیز رکوردهای Stack " + (stackIndex + 1));

        } catch (java.sql.SQLException e) {
            System.err.println("خطا در حذف ماشین های Stack: " + e.getMessage());
        }
    }
    // متدهای دیگر برای Load کردن داده ها در زمان راه اندازی برنامه
}