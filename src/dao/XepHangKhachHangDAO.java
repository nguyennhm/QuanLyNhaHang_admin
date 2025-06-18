// XepHangKhachHangDAO.java
package dao;

import model.XepHangKhachHang;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class XepHangKhachHangDAO {
    private final Connection connection;

    public XepHangKhachHangDAO(Connection connection) {
        this.connection = connection;
    }

    public List<XepHangKhachHang> getAllXepHang() {
        List<XepHangKhachHang> list = new ArrayList<>();
        String sql = "SELECT * FROM xephangkhachhang ORDER BY dieuKienDiem ASC";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                XepHangKhachHang xh = new XepHangKhachHang(
                        rs.getInt("id"),
                        rs.getString("capBac"),
                        rs.getInt("dieuKienDiem"),
                        rs.getFloat("uuDai")
                );
                list.add(xh);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public XepHangKhachHang getXepHangById(int id) {
        String sql = "SELECT * FROM xephangkhachhang WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new XepHangKhachHang(
                        rs.getInt("id"),
                        rs.getString("capBac"),
                        rs.getInt("dieuKienDiem"),
                        rs.getFloat("uuDai")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean themXepHang(XepHangKhachHang xh) {
        String sql = "INSERT INTO xephangkhachhang (capBac, dieuKienDiem, uuDai) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, xh.getCapBac());
            stmt.setInt(2, xh.getdieuKienDiem());
            stmt.setFloat(3, xh.getUuDai());
            if (stmt.executeUpdate() > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    xh.setId(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean capNhatXepHang(XepHangKhachHang xh) {
        String sql = "UPDATE xephangkhachhang SET capBac = ?, dieuKienDiem = ?, uuDai = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, xh.getCapBac());
            stmt.setInt(2, xh.getdieuKienDiem());
            stmt.setFloat(3, xh.getUuDai());
            stmt.setInt(4, xh.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean xoaXepHang(int id) {
        String checkSql = "SELECT COUNT(*) FROM khachhang WHERE capBac = (SELECT capBac FROM xephangkhachhang WHERE id = ?)";
        try (PreparedStatement checkStmt = connection.prepareStatement(checkSql)) {
            checkStmt.setInt(1, id);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        String sql = "DELETE FROM xephangkhachhang WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public int getXepHangIdByDiem(int diemTichLuy) {
        String sql = "SELECT id FROM xephangkhachhang WHERE dieuKienDiem <= ? ORDER BY dieuKienDiem DESC LIMIT 1";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, diemTichLuy);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt("id");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        sql = "SELECT id FROM xephangkhachhang ORDER BY dieuKienDiem ASC LIMIT 1";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) return rs.getInt("id");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public String getCapBacById(int id) {
        String sql = "SELECT capBac FROM xephangkhachhang WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getString("capBac");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}