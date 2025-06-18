package dao;

import model.KhachHang;
import utils.JDBCUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class KhachHangDAO {
    private Connection connection;
    private XepHangKhachHangDAO xepHangDAO;

    public KhachHangDAO(Connection connection) {
        this.connection = connection;
        this.xepHangDAO = new XepHangKhachHangDAO(connection);
    }

    public List<KhachHang> getAllKhachHang() {
        List<KhachHang> list = new ArrayList<>();
        String sql = "SELECT * FROM khachhang";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                KhachHang kh = new KhachHang();
                kh.setId(rs.getInt("id"));
                kh.setHoTen(rs.getString("hoTen"));
                kh.setSoDienThoai(rs.getString("soDienThoai"));
                kh.setDiemTichLuy(rs.getInt("diemTichLuy"));
                kh.setCapBac(rs.getString("capBac"));
                java.sql.Date sinhNhatSql = rs.getDate("sinhNhat");
                if (sinhNhatSql == null) {
//                    System.out.println("Warning: sinhNhat is null for ID " + rs.getInt("id"));
                    kh.setSinhNhat(null);
                } else {
//                    System.out.println("sinhNhat for ID " + rs.getInt("id") + ": " + sinhNhatSql);
                    kh.setSinhNhat(new java.util.Date(sinhNhatSql.getTime()));
                }
                list.add(kh);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public KhachHang getKhachHangById(int id) {
        String sql = "SELECT * FROM khachhang WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                KhachHang kh = new KhachHang();
                kh.setId(rs.getInt("id"));
                kh.setHoTen(rs.getString("hoTen"));
                kh.setSoDienThoai(rs.getString("soDienThoai"));
                kh.setDiemTichLuy(rs.getInt("diemTichLuy"));
                kh.setCapBac(rs.getString("capBac"));
                java.sql.Date sinhNhatSql = rs.getDate("sinhNhat");
                if (sinhNhatSql == null) {
                    System.out.println("Warning: sinhNhat is null for ID " + id);
                    kh.setSinhNhat(null);
                } else {
                    System.out.println("sinhNhat for ID " + id + ": " + sinhNhatSql);
                    kh.setSinhNhat(new java.util.Date(sinhNhatSql.getTime()));
                }
                return kh;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean themKhachHang(KhachHang kh) {
        String sql = "INSERT INTO khachhang (hoTen, soDienThoai, diemTichLuy, capBac, sinhNhat) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, kh.getHoTen());
            stmt.setString(2, kh.getSoDienThoai());
            stmt.setInt(3, kh.getDiemTichLuy());
            int xepHangId = xepHangDAO.getXepHangIdByDiem(kh.getDiemTichLuy());
            kh.setCapBac(xepHangDAO.getCapBacById(xepHangId));
            stmt.setString(4, kh.getCapBac());
            if (kh.getSinhNhat() != null) {
                stmt.setDate(5, new java.sql.Date(kh.getSinhNhat().getTime()));
            } else {
                stmt.setNull(5, java.sql.Types.DATE);
            }
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    kh.setId(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean capNhatKhachHang(KhachHang kh) {
        String sql = "UPDATE khachhang SET hoTen = ?, soDienThoai = ?, diemTichLuy = ?, capBac = ?, sinhNhat = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, kh.getHoTen());
            stmt.setString(2, kh.getSoDienThoai());
            stmt.setInt(3, kh.getDiemTichLuy());
            int xepHangId = xepHangDAO.getXepHangIdByDiem(kh.getDiemTichLuy());
            kh.setCapBac(xepHangDAO.getCapBacById(xepHangId));
            stmt.setString(4, kh.getCapBac());
            if (kh.getSinhNhat() != null) {
                stmt.setDate(5, new java.sql.Date(kh.getSinhNhat().getTime()));
            } else {
                stmt.setNull(5, java.sql.Types.DATE);
            }
            stmt.setInt(6, kh.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean xoaKhachHang(int id) {
        String sql = "DELETE FROM khachhang WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}