// File: dao/MonAnDAO.java
package dao;

import model.MonAn;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MonAnDAO {
    private Connection conn;
    private MonAnNguyenLieuDAO monAnNguyenLieuDAO;

    public MonAnDAO(Connection conn) {
        this.conn = conn;
        this.monAnNguyenLieuDAO = new MonAnNguyenLieuDAO(conn);
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
                list.add(mon);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean themMonAn(MonAn mon) {
        String sql = "INSERT INTO monan (tenMon, gia, trangThai, moTa, hinhAnh, id_thumuc) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, mon.getTenMon());
            stmt.setDouble(2, mon.getGia());
            stmt.setString(3, mon.getTrangThai());
            stmt.setString(4, mon.getMoTa());
            stmt.setString(5, mon.getHinhAnh());
            stmt.setInt(6, mon.getId_thumuc());
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
        String sql = "UPDATE monan SET tenMon = ?, gia = ?, trangThai = ?, moTa = ?, hinhAnh = ?, id_thumuc = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, mon.getTenMon());
            stmt.setDouble(2, mon.getGia());
            stmt.setString(3, mon.getTrangThai());
            stmt.setString(4, mon.getMoTa());
            stmt.setString(5, mon.getHinhAnh());
            stmt.setInt(6, mon.getId_thumuc());
            stmt.setInt(7, mon.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean xoaMonAn(int id) {
        try {
            conn.setAutoCommit(false);

            monAnNguyenLieuDAO.xoaNguyenLieuTheoMonAn(id);

            String sql = "DELETE FROM monan WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, id);
                boolean result = stmt.executeUpdate() > 0;

                conn.commit();
                return result;
            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public void capNhatTrangThaiMonAnTheoNguyenLieu() {
        String sql = """
        UPDATE monan m
        SET m.trangThai = CASE
            WHEN EXISTS (
                SELECT 1
                FROM monan_nguyenlieu ml
                JOIN nguyenlieu nl ON ml.nguyenLieuid = nl.id
                WHERE ml.monAnid = m.id
                AND nl.soLuongTon < ml.soLuongCan
            ) THEN 'Hết'
            ELSE 'Còn'
        END
        """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
                mon.setId_thumuc(rs.getInt("id_thumuc"));
                return mon;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}