package dao;

import model.NguyenLieu;
import utils.JDBCUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NguyenLieuDAO {
    private Connection conn;

    // Constructor tự động tạo connection nếu không truyền
    public NguyenLieuDAO() {
        this.conn = JDBCUtil.getConnection();
    }

    // Constructor có truyền connection từ ngoài
    public NguyenLieuDAO(Connection conn) {
        this.conn = conn;
    }


    // Lấy danh sách tất cả nguyên liệu
    public List<NguyenLieu> getAllNguyenLieu() {
        List<NguyenLieu> list = new ArrayList<>();
        String sql = "SELECT * FROM nguyenlieu";

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                NguyenLieu nl = new NguyenLieu();
                nl.setId(rs.getInt("id"));
                nl.setTenNguyenLieu(rs.getString("tenNguyenLieu"));
                nl.setDonViTinh(rs.getString("donViTinh"));
                nl.setSoLuongTon(rs.getDouble("soLuongTon"));
                nl.setGiaNhap(rs.getInt("giaNhap"));
                list.add(nl);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }


    // Xóa nguyên liệu theo ID
    public boolean xoaNguyenLieu(int id) {
        String sql = "DELETE FROM nguyenlieu WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Tìm nguyên liệu theo ID
    public NguyenLieu getNguyenLieuById(int id) {
        String sql = "SELECT * FROM nguyenlieu WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                NguyenLieu nl = new NguyenLieu();
                nl.setId(rs.getInt("id"));
                nl.setTenNguyenLieu(rs.getString("tenNguyenLieu"));
                nl.setDonViTinh(rs.getString("donViTinh"));
                nl.setSoLuongTon(rs.getInt("soLuongTon"));
                nl.setGiaNhap(rs.getInt("giaNhap"));
                return nl;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Thêm nguyên liệu mới (nếu trùng tên thì cộng thêm số lượng)
    public boolean themNguyenLieu(NguyenLieu nl) {
        String selectSql = "SELECT * FROM nguyenlieu WHERE tenNguyenLieu = ?";
        String insertSql = "INSERT INTO nguyenlieu (tenNguyenLieu, donViTinh, soLuongTon, giaNhap) VALUES (?, ?, ?, ?)";
        String updateSql = "UPDATE nguyenlieu SET soLuongTon = soLuongTon + ?, giaNhap = giaNhap + ? WHERE tenNguyenLieu = ?";

        try (PreparedStatement selectStmt = conn.prepareStatement(selectSql)) {
            selectStmt.setString(1, nl.getTenNguyenLieu());
            ResultSet rs = selectStmt.executeQuery();

            if (rs.next()) {
                // Nếu đã có → cộng thêm số lượng và cập nhật giá nhập
                try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                    updateStmt.setDouble(1, nl.getSoLuongTon());
                    updateStmt.setInt(2, nl.getGiaNhap());
                    updateStmt.setString(3, nl.getTenNguyenLieu());
                    return updateStmt.executeUpdate() > 0;
                }
            } else {
                // Nếu chưa có → thêm mới
                try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                    insertStmt.setString(1, nl.getTenNguyenLieu());
                    insertStmt.setString(2, nl.getDonViTinh());
                    insertStmt.setDouble(3, nl.getSoLuongTon());
                    insertStmt.setInt(4, nl.getGiaNhap());
                    return insertStmt.executeUpdate() > 0;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Cộng thêm số lượng và giá nhập cho nguyên liệu
    public boolean congThemSoLuongVaGia(int id, double themSoLuong, int themGiaNhap) {
        String sql = "UPDATE nguyenlieu SET soLuongTon = soLuongTon + ?, giaNhap = giaNhap + ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, themSoLuong);
            stmt.setInt(2, themGiaNhap);
            stmt.setInt(3, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    // Cập nhật số lượng tồn theo ID
    public boolean capNhatSoLuongTon(int nguyenLieuId, int soLuongMoi) {
        String sql = "UPDATE nguyenlieu SET soLuongTon = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, soLuongMoi);
            stmt.setInt(2, nguyenLieuId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Cập nhật toàn bộ nguyên liệu (dùng trong form cập nhật)
    public boolean capNhatNguyenLieu(NguyenLieu nl) {
        String sql = "UPDATE nguyenlieu SET tenNguyenLieu = ?, donViTinh = ?, soLuongTon = ?, giaNhap = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nl.getTenNguyenLieu());
            stmt.setString(2, nl.getDonViTinh());
            stmt.setDouble(3, nl.getSoLuongTon());
            stmt.setDouble(4, nl.getGiaNhap());
            stmt.setInt(5, nl.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}