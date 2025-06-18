package dao;

import model.TaiKhoan;
import utils.JDBCUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TaiKhoanDAO {
    public static TaiKhoan kiemTraDangNhap(String email, String matKhauDaMaHoa) {
        TaiKhoan tk = null;
        try {
            Connection conn = JDBCUtil.getConnection();
            String sql = "SELECT * FROM taikhoan WHERE email = ? AND matKhau = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            stmt.setString(2, matKhauDaMaHoa);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                tk = new TaiKhoan(
                        rs.getString("email"),
                        rs.getString("matKhau"),
                        rs.getString("vaiTro"),
                        rs.getString("trangThai")
                );
            }

            JDBCUtil.closeConnection(conn);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return tk;
    }

    // ✅ Hàm kiểm tra email đã tồn tại
    public static boolean kiemTraEmailTonTai(String email) {
        boolean tonTai = false;
        try {
            Connection conn = JDBCUtil.getConnection();
            String sql = "SELECT email FROM taikhoan WHERE email = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            tonTai = rs.next();
            JDBCUtil.closeConnection(conn);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tonTai;
    }

    // ✅ Hàm thêm tài khoản mới
    public static boolean dangKyTaiKhoan(TaiKhoan tk) {
        try {
            if (kiemTraEmailTonTai(tk.getEmail())) {
                return false; // Email đã tồn tại
            }

            Connection conn = JDBCUtil.getConnection();
            String sql = "INSERT INTO taikhoan (email, matKhau, vaiTro, trangThai) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, tk.getEmail());
            stmt.setString(2, tk.getMatKhau());
            stmt.setString(3, tk.getVaiTro());
            stmt.setString(4, tk.isTrangThai());

            int rows = stmt.executeUpdate();
            JDBCUtil.closeConnection(conn);
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean capNhatMatKhau(String email, String matKhauMaHoa) {
        try {
            Connection conn = JDBCUtil.getConnection();
            String sql = "UPDATE taikhoan SET matkhau = ? WHERE email = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, matKhauMaHoa);
            ps.setString(2, email);

            int rows = ps.executeUpdate();
            JDBCUtil.closeConnection(conn);
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
