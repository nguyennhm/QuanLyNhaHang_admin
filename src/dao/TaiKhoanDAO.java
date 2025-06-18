package dao;

import model.TaiKhoan;
import utils.JDBCUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TaiKhoanDAO {
    private Connection connection;

    public TaiKhoanDAO(Connection connection) {
        this.connection = connection;
    }

    public static boolean kiemTraEmailTonTai(String email) {
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT email FROM taikhoan WHERE email = ?")) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static TaiKhoan kiemTraDangNhap(String email, String matKhau) {
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM taikhoan WHERE email = ? AND matKhau = ?")) {
            stmt.setString(1, email);
            stmt.setString(2, matKhau);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                TaiKhoan tk = new TaiKhoan(
                        rs.getString("email"),
                        rs.getString("matKhau"),
                        rs.getString("vaiTro"),
                        rs.getString("trangThai")
                );
                tk.setId_taikhoan(rs.getInt("id_taikhoan"));
                return tk;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean dangKyTaiKhoan(TaiKhoan tk) {
        if (kiemTraEmailTonTai(tk.getEmail())) {
            return false; // Email đã tồn tại
        }
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO taikhoan (email, matKhau, vaiTro, trangThai) VALUES (?, ?, ?, ?)",
                     PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, tk.getEmail());
            stmt.setString(2, tk.getMatKhau());
            stmt.setString(3, tk.getVaiTro());
            stmt.setString(4, tk.getTrangThai());
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    tk.setId_taikhoan(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean capNhatMatKhau(String email, String matKhau) {
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement("UPDATE taikhoan SET matKhau = ? WHERE email = ?")) {
            stmt.setString(1, matKhau);
            stmt.setString(2, email);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean themTaiKhoan(TaiKhoan tk) {
        if (kiemTraEmailTonTai(tk.getEmail())) {
            return false; // Email đã tồn tại
        }
        String sql = "INSERT INTO taikhoan (email, matKhau, vaiTro, trangThai) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, tk.getEmail());
            stmt.setString(2, tk.getMatKhau());
            stmt.setString(3, tk.getVaiTro());
            stmt.setString(4, tk.getTrangThai());
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    tk.setId_taikhoan(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean capNhatTaiKhoan(TaiKhoan tk) {
        String sql = "UPDATE taikhoan SET email = ?, matKhau = ?, vaiTro = ?, trangThai = ? WHERE id_taikhoan = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, tk.getEmail());
            stmt.setString(2, tk.getMatKhau());
            stmt.setString(3, tk.getVaiTro());
            stmt.setString(4, tk.getTrangThai());
            stmt.setInt(5, tk.getId_taikhoan());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean xoaTaiKhoan(int id) {
        String sql = "DELETE FROM taikhoan WHERE id_taikhoan = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public TaiKhoan getTaiKhoanById(int id) {
        String sql = "SELECT * FROM taikhoan WHERE id_taikhoan = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                TaiKhoan tk = new TaiKhoan(
                        rs.getString("email"),
                        rs.getString("matKhau"),
                        rs.getString("vaiTro"),
                        rs.getString("trangThai")
                );
                tk.setId_taikhoan(rs.getInt("id_taikhoan"));
                return tk;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}