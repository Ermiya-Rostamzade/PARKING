import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CarDAO {

    public void saveCar(Car car, int stackIndex, int positionInStack) {
        String sql = "INSERT INTO Cars(CarID, StackIndex, PositionInStack) VALUES(?, ?, ?)";

        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, car.getId());
            pstmt.setInt(2, stackIndex +1);
            pstmt.setInt(3, positionInStack);
            pstmt.executeUpdate();

        } catch (SQLException e) {

            System.err.println("خطا در ذخیره کردن Car: " + e.getMessage());
        }
    }


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

    public void deleteStackCars(int stackIndex) {
        String sql = "DELETE FROM Cars WHERE StackIndex = ?";

        try (Connection conn = SQLiteConnection.getConnection();
             java.sql.PreparedStatement pstmt = conn.prepareStatement(sql)) {


            pstmt.setInt(1, stackIndex + 1);
            pstmt.executeUpdate();


            System.out.println("حذف موفقیت آمیز رکوردهای Stack " + (stackIndex + 1));

        } catch (java.sql.SQLException e) {
            System.err.println("خطا در حذف ماشین های Stack: " + e.getMessage());
        }
    }

}