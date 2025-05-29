package model;

import java.time.LocalDateTime;

public class HoaDon {
    private int id;
    private int orderId;
    private int tongTien;
    private LocalDateTime thoiGianThanhToan;

    public HoaDon() {}

    public HoaDon(int id, int orderId, int tongTien, LocalDateTime thoiGianThanhToan) {
        this.id = id;
        this.orderId = orderId;
        this.tongTien = tongTien;
        this.thoiGianThanhToan = thoiGianThanhToan;
    }

    // Getter, Setter

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getTongTien() {
        return tongTien;
    }

    public void setTongTien(int tongTien) {
        this.tongTien = tongTien;
    }

    public LocalDateTime getThoiGianThanhToan() {
        return thoiGianThanhToan;
    }

    public void setThoiGianThanhToan(LocalDateTime thoiGianThanhToan) {
        this.thoiGianThanhToan = thoiGianThanhToan;
    }
}
