package model;

import java.util.Date;

public class Nhanvien {
    private int id_nhanvien;
    private String ten;
    private String sdt;
    private String diachi;
    private int id_taikhoan;
    private Date ngaysinh;
    private String vitri;

    public int getId_nhanvien() {
        return id_nhanvien;
    }

    public void setId_nhanvien(int id_nhanvien) {
        this.id_nhanvien = id_nhanvien;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getDiachi() {
        return diachi;
    }

    public void setDiachi(String diachi) {
        this.diachi = diachi;
    }

    public int getId_taikhoan() {
        return id_taikhoan;
    }

    public void setId_taikhoan(int id_taikhoan) {
        this.id_taikhoan = id_taikhoan;
    }

    public Date getNgaysinh() {
        return ngaysinh;
    }

    public void setNgaysinh(Date ngaysinh) {
        this.ngaysinh = ngaysinh;
    }

    public String getVitri() {
        return vitri;
    }

    public void setVitri(String vitri) {
        this.vitri = vitri;
    }
}
