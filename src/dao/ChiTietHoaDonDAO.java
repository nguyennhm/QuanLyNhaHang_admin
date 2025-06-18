package dao;

import model.ChiTietHoaDon;
import utils.JDBCUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChiTietHoaDonDAO {
    public List<ChiTietHoaDon> getByHoaDonId(int hoaDonId) {
        List<ChiTietHoaDon> list = new ArrayList<>();
        String sql = """
        SELECT cthd.*, ma.tenmon
        FROM chitiethoadon cthd
        JOIN monan ma ON cthd.idmonan = ma.id
        WHERE cthd.idhoadon = ?
    """;

        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, hoaDonId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ChiTietHoaDon ct = new ChiTietHoaDon();
                ct.setId(rs.getInt("id"));
                ct.setIdhoadon(rs.getInt("idhoadon"));
                ct.setIdmonan(rs.getInt("idmonan"));
                ct.setSoLuong(rs.getInt("soluong"));
                ct.setTongTien(rs.getLong("tongtien"));
                ct.setTenMonAn(rs.getString("tenmon"));
                list.add(ct);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

}
