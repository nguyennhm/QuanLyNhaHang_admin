package dao;

import java.sql.*;
import java.util.*;
import utils.JDBCUtil;

public class ThongKeDAO {
    private Connection conn;

    public ThongKeDAO() {
        conn = JDBCUtil.getConnection();
    }

    public double getTongThu() {
        String sql = "SELECT SUM(tongTien) FROM hoadon";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getDouble(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public double getTongChi() {
        return getTongLuong() + getTongNguyenLieu();
    }

    public double getTongLuong() {
        String sql = "SELECT SUM(hesoluong * 8 * 30 + diemthuong * 10000) FROM luong";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getDouble(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public double getTongNguyenLieu() {
        String sql = "SELECT SUM(giaNhap) FROM nguyenlieu";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getDouble(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getSoLuongDonHang() {
        String sql = "SELECT COUNT(*) FROM hoadon";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public double getTongDoanhThuSanPham() {
        String sql = "SELECT SUM(ct.soLuong * sp.gia) FROM chitietorder ct JOIN monan sp ON ct.monAnId = sp.id";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getDouble(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }


    public int getTongSanPhamBanDuoc() {
        String sql = "SELECT SUM(soLuong) FROM chitietorder";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Không chia theo tháng nữa, chỉ trả về 1 giá trị tổng
    public Map<String, Double> getMonthlyRevenue() {
        Map<String, Double> revenueData = new LinkedHashMap<>();
        double totalRevenue = getTongThu();
        revenueData.put("Tổng", totalRevenue);
        return revenueData;
    }

    public List<String[]> getTopProducts() {
        List<String[]> topProducts = new ArrayList<>();
        String sql = "SELECT sp.tenMon, SUM(ct.soLuong) AS soLuong, SUM(sp.gia)*SUM(ct.soLuong) AS doanhThu " +
                "FROM chitietorder ct JOIN monan sp ON ct.monAnId = sp.id " +
                "GROUP BY ct.monAnId, sp.tenMon ORDER BY soLuong DESC LIMIT 5";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                topProducts.add(new String[]{
                        rs.getString("tenMon"),
                        String.valueOf(rs.getInt("soLuong")),
                        String.valueOf(rs.getDouble("doanhThu"))
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return topProducts;
    }

    public ResultSet rsLuong() throws SQLException {
        String sql = "SELECT * FROM luong";
        return conn.prepareStatement(sql).executeQuery();
    }
    public ResultSet rsHoaDon() throws SQLException {
        String sql = "SELECT * FROM hoadon";
        return conn.prepareStatement(sql).executeQuery();
    }
    public ResultSet rsChiTietOrder() throws SQLException {
        String sql = "SELECT * FROM chitietorder";
        return conn.prepareStatement(sql).executeQuery();
    }
    public ResultSet rsNguyenLieu() throws SQLException {
        String sql = "SELECT * FROM nguyenlieu";
        return conn.prepareStatement(sql).executeQuery();
    }

}
