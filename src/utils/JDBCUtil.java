package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCUtil {
    private static final String URL = "jdbc:mysql://localhost:3306/quan_li_nha_hang"; // Tên database là quan_li_nha_hang
    private static final String USER = "root"; // Thay bằng username của bạn
    private static final String PASSWORD = "22032005"; // Thay bằng mật khẩu của bạn

    // Phương thức lấy kết nối
    public static Connection getConnection() {
        Connection conn = null;
        try {
            // Đăng ký MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Tạo kết nối
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            System.out.println("Không tìm thấy JDBC Driver.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Kết nối CSDL thất bại.");
            e.printStackTrace();
        }

        return conn;
    }

    // Đóng kết nối
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
