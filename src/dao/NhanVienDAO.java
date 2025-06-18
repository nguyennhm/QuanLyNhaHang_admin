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
        String sql = "INSERT INTO nhanvien (ten, sdt, diachi, id_taikhoan, ngaysinh, vitri) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, nv.getTen());
            stmt.setString(2, nv.getSdt());
            stmt.setString(3, nv.getDiachi());
            stmt.setInt(4, nv.getId_taikhoan());
            stmt.setDate(5, new java.sql.Date(nv.getNgaysinh().getTime()));
            stmt.setString(6, nv.getVitri());
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    nv.setId_nhanvien(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
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