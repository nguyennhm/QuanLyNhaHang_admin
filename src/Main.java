import utils.JDBCUtil;

import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        Connection conn = JDBCUtil.getConnection();

        if (conn != null) {
            System.out.println("✅ Kết nối thành công đến CSDL MySQL!");
        } else {
            System.out.println("❌ Kết nối thất bại.");
        }

        JDBCUtil.closeConnection(conn);
    }
}