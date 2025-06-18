package model;

import java.time.LocalDateTime;

public class Order {
    private int id;
    private int banAnId;
    private int id_nhanvien;
    private LocalDateTime thoiGianTao;
    private String trangThai;
    private String ghiChu;

    public Order() {}

    public Order(int id, int banAnId, int id_nhanvien, LocalDateTime thoiGianTao, String trangThai, String ghiChu) {
        this.id = id;
        this.banAnId = banAnId;
        this.id_nhanvien = id_nhanvien;
        this.thoiGianTao = thoiGianTao;
        this.trangThai = trangThai;
        this.ghiChu= ghiChu;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBanAnId() {
        return banAnId;
    }

    public void setBanAnId(int banAnId) {
        this.banAnId = banAnId;
    }


    public int getId_nhanvien() {
        return id_nhanvien;
    }

    public void setId_nhanvien(int id_nhanvien) {
        this.id_nhanvien = id_nhanvien;
    }

    public LocalDateTime getThoiGianTao() {
        return thoiGianTao;
    }

    public void setThoiGianTao(LocalDateTime thoiGianTao) {
        this.thoiGianTao = thoiGianTao;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }
// Getter, Setter
}