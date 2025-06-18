package dao;

import model.MonAnNguyenLieu;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MonAnNguyenLieuDAO {
    private Connection conn;

    public MonAnNguyenLieuDAO(Connection conn) {
        this.conn = conn;
    }

    // Lấy map nguyên liệu và số lượng (dùng trong form update)
    public Map<Integer, Double> getNguyenLieuMapByMonAnId(int monAnId) {
        Map<Integer, Double> map = new HashMap<>();
        String sql = "SELECT nguyenLieuid, soLuongCan FROM monan_nguyenlieu WHERE monAnid = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, monAnId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("nguyenLieuid");
                double soLuong = rs.getDouble("soLuongCan");
                map.put(id, soLuong);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return map;
    }

    public List<MonAnNguyenLieu> getByMonAnId(int monAnId) {
        List<MonAnNguyenLieu> list = new ArrayList<>();
        String sql = "SELECT * FROM monan_nguyenlieu WHERE monAnid = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, monAnId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                MonAnNguyenLieu mal = new MonAnNguyenLieu();
                mal.setId(rs.getInt("id"));
                mal.setMonAnId(rs.getInt("monAnid"));
                mal.setNguyenLieuId(rs.getInt("nguyenLieuid"));
                mal.setSoLuongCan(rs.getDouble("soLuongCan")); // sửa từ getInt → getDouble
                list.add(mal);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean xoaNguyenLieuTheoMonAn(int monAnId) {
        String sql = "DELETE FROM monan_nguyenlieu WHERE monAnid = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, monAnId);
            return stmt.executeUpdate() >= 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean themMonAnNguyenLieu(MonAnNguyenLieu mal) {
        return themMonAnNguyenLieu(mal.getMonAnId(), mal.getNguyenLieuId(), mal.getSoLuongCan());
    }

    public boolean themMonAnNguyenLieu(int monAnId, int nguyenLieuId, double soLuongCan) {
        String sql = "INSERT INTO monan_nguyenlieu (monAnid, nguyenLieuid, soLuongCan) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, monAnId);
            stmt.setInt(2, nguyenLieuId);
            stmt.setDouble(3, soLuongCan);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean capNhatSoLuong(int monAnId, int nguyenLieuId, double soLuongMoi) {
        String sql = "UPDATE monan_nguyenlieu SET soLuongCan = ? WHERE monAnid = ? AND nguyenLieuid = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, soLuongMoi);
            stmt.setInt(2, monAnId);
            stmt.setInt(3, nguyenLieuId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
