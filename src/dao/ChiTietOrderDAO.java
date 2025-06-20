package dao;

import model.ChiTietOrder;
import utils.JDBCUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChiTietOrderDAO {
    public List<ChiTietOrder> getChiTietByOrderId(int orderId) {
        List<ChiTietOrder> list = new ArrayList<>();
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT cto.*, ma.tenMon FROM chitietorder cto " +
                             "JOIN monan ma ON cto.monAnId = ma.id WHERE cto.orderId = ?")) {
            stmt.setInt(1, orderId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ChiTietOrder ct = new ChiTietOrder();
                ct.setId(rs.getInt("id"));
                ct.setOrderId(rs.getInt("orderId"));
                ct.setMonAnId(rs.getInt("monAnId"));
                ct.setSoLuong(rs.getInt("soLuong"));
                ct.setTrangThai(rs.getString("trangThai"));
                ct.setTenMon(rs.getString("tenMon")); // thêm trường này vào class ChiTietOrder
                list.add(ct);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<ChiTietOrder> getAll() {
        List<ChiTietOrder> list = new ArrayList<>();
        String sql = "SELECT cto.*, ma.tenMon FROM chitietorder cto JOIN monan ma ON cto.monAnId = ma.id";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                ChiTietOrder ct = new ChiTietOrder();
                ct.setId(rs.getInt("id"));
                ct.setOrderId(rs.getInt("orderId"));
                ct.setMonAnId(rs.getInt("monAnId"));
                ct.setSoLuong(rs.getInt("soLuong"));
                ct.setTrangThai(rs.getString("trangThai"));
                ct.setTenMon(rs.getString("tenMon"));
                list.add(ct);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

}

