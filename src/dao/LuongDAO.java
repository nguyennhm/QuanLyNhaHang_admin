package dao;

import model.Luong;
import utils.JDBCUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LuongDAO {
    // Select all salary records
    public List<Luong> selectAll() {
        List<Luong> luongList = new ArrayList<>();
        String sql = """
        SELECT l.id, l.heSoLuong, l.diemThuong, l.id_nhanvien, nv.ten AS tenNhanVien
        FROM Luong l
        JOIN nhanvien nv ON l.id_nhanvien = nv.id_nhanvien
    """;

        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Luong luong = new Luong();
                luong.setId(rs.getInt("id"));
                luong.setHeSoLuong(rs.getFloat("heSoLuong"));
                luong.setDiemThuong(rs.getInt("diemThuong"));
                luong.setIdNhanVien(rs.getInt("id_nhanvien"));
                luong.setTenNhanVien(rs.getString("tenNhanVien"));
                luongList.add(luong);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return luongList;
    }

    // Insert a new salary record
    public boolean insert(Luong luong) {
        String sql = "INSERT INTO Luong (heSoLuong, diemThuong, id_nhanvien) VALUES (?, ?, ?)";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setFloat(1, luong.getHeSoLuong());
            pstmt.setInt(2, luong.getDiemThuong());
            pstmt.setInt(3, luong.getIdNhanVien());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Update an existing salary record
    public boolean update(Luong luong) {
        String sql = "UPDATE Luong SET heSoLuong = ?, diemThuong = ?, id_nhanvien = ? WHERE id = ?";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setFloat(1, luong.getHeSoLuong());
            pstmt.setInt(2, luong.getDiemThuong());
            pstmt.setInt(3, luong.getIdNhanVien());
            pstmt.setInt(4, luong.getId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Delete a salary record by ID
    public boolean delete(int id) {
        String sql = "DELETE FROM Luong WHERE id = ?";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}