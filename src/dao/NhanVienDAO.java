package dao;

import model.Nhanvien;
import utils.JDBCUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NhanVienDAO {
    private Connection connection;

    public NhanVienDAO(Connection connection) {
        this.connection = connection;
    }

    public List<Nhanvien> getAllNhanVien() {
        List<Nhanvien> list = new ArrayList<>();
        String sql = "SELECT * FROM nhanvien";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Nhanvien nv = new Nhanvien();
                nv.setId_nhanvien(rs.getInt("id_nhanvien"));
                nv.setTen(rs.getString("ten"));
                nv.setSdt(rs.getString("sdt"));
                nv.setDiachi(rs.getString("diachi"));
                nv.setId_taikhoan(rs.getInt("id_taikhoan"));
                nv.setNgaysinh(rs.getDate("ngaysinh"));
                nv.setVitri(rs.getString("vitri"));
                list.add(nv);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Nhanvien getNhanVienById(int id) {
        String sql = "SELECT * FROM nhanvien WHERE id_nhanvien = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Nhanvien nv = new Nhanvien();
                nv.setId_nhanvien(rs.getInt("id_nhanvien"));
                nv.setTen(rs.getString("ten"));
                nv.setSdt(rs.getString("sdt"));
                nv.setDiachi(rs.getString("diachi"));
                nv.setId_taikhoan(rs.getInt("id_taikhoan"));
                nv.setNgaysinh(rs.getDate("ngaysinh"));
                nv.setVitri(rs.getString("vitri"));
                return nv;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean themNhanVien(Nhanvien nv) {
        String sqlNhanVien = "INSERT INTO nhanvien (ten, sdt, diachi, id_taikhoan, ngaysinh, vitri) VALUES (?, ?, ?, ?, ?, ?)";
        String sqlLuong = "INSERT INTO luong (hesoluong, diemthuong, id_nhanvien) VALUES (?, ?, ?)";

        try {
            // Bắt đầu transaction
            connection.setAutoCommit(false);

            // Thêm vào bảng nhanvien
            try (PreparedStatement stmtNhanVien = connection.prepareStatement(sqlNhanVien, PreparedStatement.RETURN_GENERATED_KEYS)) {
                stmtNhanVien.setString(1, nv.getTen());
                stmtNhanVien.setString(2, nv.getSdt());
                stmtNhanVien.setString(3, nv.getDiachi());
                stmtNhanVien.setInt(4, nv.getId_taikhoan());
                stmtNhanVien.setDate(5, new java.sql.Date(nv.getNgaysinh().getTime()));
                stmtNhanVien.setString(6, nv.getVitri());
                int rows = stmtNhanVien.executeUpdate();
                if (rows > 0) {
                    ResultSet rs = stmtNhanVien.getGeneratedKeys();
                    if (rs.next()) {
                        nv.setId_nhanvien(rs.getInt(1));
                    }
                } else {
                    connection.rollback();
                    return false;
                }
            }

            // Thêm vào bảng luong với id_nhanvien vừa tạo
            try (PreparedStatement stmtLuong = connection.prepareStatement(sqlLuong)) {
                stmtLuong.setDouble(1, 0.0); // Giá trị mặc định cho hesoluong
                stmtLuong.setNull(2, java.sql.Types.INTEGER); // diemthuong mặc định NULL
                stmtLuong.setInt(3, nv.getId_nhanvien());
                int rows = stmtLuong.executeUpdate();
                if (rows == 0) {
                    connection.rollback();
                    return false;
                }
            }

            // Commit transaction
            connection.commit();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return false;
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean capNhatNhanVien(Nhanvien nv) {
        String sql = "UPDATE nhanvien SET ten = ?, sdt = ?, diachi = ?, id_taikhoan = ?, ngaysinh = ?, vitri = ? WHERE id_nhanvien = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nv.getTen());
            stmt.setString(2, nv.getSdt());
            stmt.setString(3, nv.getDiachi());
            stmt.setInt(4, nv.getId_taikhoan());
            stmt.setDate(5, new java.sql.Date(nv.getNgaysinh().getTime()));
            stmt.setString(6, nv.getVitri());
            stmt.setInt(7, nv.getId_nhanvien());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean xoaNhanVien(int id) {
        String sql = "DELETE FROM nhanvien WHERE id_nhanvien = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}