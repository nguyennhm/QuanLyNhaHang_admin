package dao;

import model.Order;
import utils.JDBCUtil;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {
    public List<Order> getAllOrders() {
        List<Order> list = new ArrayList<>();
        String sql = "SELECT * FROM `order`"; // ← Bọc tên bảng trong ``
        try (Connection conn = JDBCUtil.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Order o = new Order();
                o.setId(rs.getInt("id"));
                o.setBanAnId(rs.getInt("banAnId"));
                o.setId_nhanvien(rs.getInt("id_nhanvien"));

                Timestamp timestamp = rs.getTimestamp("thoiGianTao");
                if (timestamp != null) {
                    o.setThoiGianTao(timestamp.toLocalDateTime());
                }

                o.setTrangThai(rs.getString("trangThai"));
                list.add(o);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
