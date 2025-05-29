package model;

public class ChiTietOrder {
    private int id;
    private int orderId;
    private int monAnId;
    private int soLuong;
    private String ghiChu;

    public ChiTietOrder() {}

    public ChiTietOrder(int id, int orderId, int monAnId, int soLuong, String ghiChu) {
        this.id = id;
        this.orderId = orderId;
        this.monAnId = monAnId;
        this.soLuong = soLuong;
        this.ghiChu = ghiChu;
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

    public int getMonAnId() {
        return monAnId;
    }

    public void setMonAnId(int monAnId) {
        this.monAnId = monAnId;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }
}
