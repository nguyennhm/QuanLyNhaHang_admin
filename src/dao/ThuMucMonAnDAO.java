package dao;

import model.Thumucmonan;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ThuMucMonAnDAO {
    private Connection conn;

    public ThuMucMonAnDAO(Connection conn) {
        this.conn = conn;
    }

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

    // Lấy tên thư mục theo ID
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

    // Lấy thư mục
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

    // Lấy tên thư mục theo id
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



    // Lấy ID thư mục theo tên
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
