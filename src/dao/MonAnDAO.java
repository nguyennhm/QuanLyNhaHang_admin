package dao;

import model.MonAn;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MonAnDAO {
    private Connection conn;

    public MonAnDAO(Connection conn) {
        this.conn = conn;
    }

    public List<MonAn> getAllMonAn() {
        List<MonAn> list = new ArrayList<>();
        String sql = "SELECT * FROM monan";

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                MonAn mon = new MonAn();
                mon.setId(rs.getInt("id"));
                mon.setTenMon(rs.getString("tenMon"));
                mon.setGia(rs.getDouble("gia"));
                mon.setTrangThai(rs.getString("trangThai"));
                mon.setMoTa(rs.getString("moTa"));
                mon.setHinhAnh(rs.getString("hinhAnh"));
                mon.setId_thumuc(rs.getInt("id_thumuc"));
                mon.setTonKhaDung(rs.getInt("tonKhaDung"));
                list.add(mon);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean themMonAn(MonAn mon) {
        String sql = "INSERT INTO monan (tenMon, gia, trangThai, moTa, hinhAnh) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, mon.getTenMon());
            stmt.setDouble(2, mon.getGia());
            stmt.setString(3, mon.getTrangThai());
            stmt.setString(4, mon.getMoTa());
            stmt.setString(5, mon.getHinhAnh());
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    mon.setId(generatedKeys.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean capNhatMonAn(MonAn mon) {
        String sql = "UPDATE monan SET tenMon = ?, gia = ?, trangThai = ?, moTa = ?, hinhAnh = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, mon.getTenMon());
            stmt.setDouble(2, mon.getGia());
            stmt.setString(3, mon.getTrangThai());
            stmt.setString(4, mon.getMoTa());
            stmt.setString(5, mon.getHinhAnh());
            stmt.setInt(6, mon.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public MonAn getMonAnById(int id) {
        String sql = "SELECT * FROM monan WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                MonAn mon = new MonAn();
                mon.setId(rs.getInt("id"));
                mon.setTenMon(rs.getString("tenMon"));
                mon.setGia(rs.getDouble("gia"));
                mon.setTrangThai(rs.getString("trangThai"));
                mon.setMoTa(rs.getString("moTa"));
                mon.setHinhAnh(rs.getString("hinhAnh"));
                return mon;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
