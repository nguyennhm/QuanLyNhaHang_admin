package dao;

import model.HoaDon;
import utils.JDBCUtil;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class HoaDonDAO {

    public List<HoaDon> getAllHoaDon() {
        List<HoaDon> list = new ArrayList<>();
        String sql = "SELECT * FROM hoadon";

        try (Connection conn = JDBCUtil.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                HoaDon h = new HoaDon();
                h.setId(rs.getInt("id"));
                h.setOrderId(rs.getInt("orderId"));
                h.setTongTien(rs.getInt("tongTien"));

                Timestamp timestamp = rs.getTimestamp("thoiGianThanhToan");
                if (timestamp != null) {
                    h.setThoiGianThanhToan(timestamp.toLocalDateTime());
                }

                list.add(h);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}
