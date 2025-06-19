package dao;

import model.Thumucmonan;
import utils.JDBCUtil; // ✅ cần thêm dòng này nếu chưa có

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ThuMucMonAnDAO {
    private Connection conn;

    // ✅ Constructor cũ: vẫn giữ để linh hoạt
    public ThuMucMonAnDAO(Connection conn) {
        this.conn = conn;
    }

    // ✅ Constructor mặc định thêm vào
    public ThuMucMonAnDAO() {
        this.conn = JDBCUtil.getConnection(); // Tự động lấy connection
    }

    // ==== Các hàm bên dưới giữ nguyên ====

    public List<Thumucmonan> getAllThuMuc() {
        List<Thumucmonan> list = new ArrayList<>();
        String sql = "SELECT * FROM thumucmonan";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Thumucmonan tm = new Thumucmonan();
                tm.setId(rs.getInt("id_thumuc"));
                tm.setTenthumuc(rs.getString("ten_thu_muc"));
                list.add(tm);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public String getTenThuMucById(int id) {
        String sql = "SELECT ten_thu_muc FROM thumucmonan WHERE id_thumuc = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("ten_thu_muc");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Không rõ";
    }

    public Map<Integer, String> getMapDanhMuc() {
        Map<Integer, String> map = new HashMap<>();
        String sql = "SELECT id_thumuc, ten_thu_muc FROM thumucmonan";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                map.put(rs.getInt("id_thumuc"), rs.getString("ten_thu_muc"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return map;
    }

    public String getTenById(int id) {
        try {
            String sql = "SELECT tenthumuc FROM thumucmonan WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("tenthumuc");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Thêm mới thư mục
    public boolean insert(Thumucmonan thuMuc) {
        String sql = "INSERT INTO thumucmonan (ten_thu_muc) VALUES (?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, thuMuc.getTenthumuc());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Cập nhật thư mục
    public boolean update(Thumucmonan thuMuc) {
        String sql = "UPDATE thumucmonan SET ten_thu_muc = ? WHERE id_thumuc = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, thuMuc.getTenthumuc());
            stmt.setInt(2, thuMuc.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Xóa thư mục
    public boolean delete(int id) {
        String sql = "DELETE FROM thumucmonan WHERE id_thumuc = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public int getIdByTenThuMuc(String tenThuMuc) {
        String sql = "SELECT id_thumuc FROM thumucmonan WHERE ten_thu_muc = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, tenThuMuc);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id_thumuc");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
