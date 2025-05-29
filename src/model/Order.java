package model;

import java.time.LocalDateTime;

public class Order {
    private int id;
    private int banAnId;
    private int khachHangId;
    private LocalDateTime thoiGianTao;
    private String trangThai;

    public Order() {}

    public Order(int id, int banAnId, int khachHangId, LocalDateTime thoiGianTao, String trangThai) {
        this.id = id;
        this.banAnId = banAnId;
        this.khachHangId = khachHangId;
        this.thoiGianTao = thoiGianTao;
        this.trangThai = trangThai;
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

    public int getKhachHangId() {
        return khachHangId;
    }

    public void setKhachHangId(int khachHangId) {
        this.khachHangId = khachHangId;
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
