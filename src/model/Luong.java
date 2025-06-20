package model;

public class Luong {
    private int id;
    private float heSoLuong;
    private int diemThuong;
    private int idNhanVien;
    private String tenNhanVien; // Transient field for display

    public Luong() {}

    public Luong(int id, float heSoLuong, int diemThuong, int idNhanVien, String tenNhanVien) {
        this.id = id;
        this.heSoLuong = heSoLuong;
        this.diemThuong = diemThuong;
        this.idNhanVien = idNhanVien;
        this.tenNhanVien = tenNhanVien;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getHeSoLuong() {
        return heSoLuong;
    }

    public void setHeSoLuong(float heSoLuong) {
        this.heSoLuong = heSoLuong;
    }

    public int getDiemThuong() {
        return diemThuong;
    }

    public void setDiemThuong(int diemThuong) {
        this.diemThuong = diemThuong;
    }

    public int getIdNhanVien() {
        return idNhanVien;
    }

    public void setIdNhanVien(int idNhanVien) {
        this.idNhanVien = idNhanVien;
    }

    public String getTenNhanVien() {
        return tenNhanVien;
    }

    public void setTenNhanVien(String tenNhanVien) {
        this.tenNhanVien = tenNhanVien;
    }
}