package model;

public class MonAnNguyenLieu {
    private int id;
    private int monAnId;
    private int nguyenLieuId;
    private double soLuongCan;

    public MonAnNguyenLieu() {}

    public MonAnNguyenLieu(int id, int monAnId, int nguyenLieuId, double soLuongCan) {
        this.id = id;
        this.monAnId = monAnId;
        this.nguyenLieuId = nguyenLieuId;
        this.soLuongCan = soLuongCan;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMonAnId() {
        return monAnId;
    }

    public void setMonAnId(int monAnId) {
        this.monAnId = monAnId;
    }

    public int getNguyenLieuId() {
        return nguyenLieuId;
    }

    public void setNguyenLieuId(int nguyenLieuId) {
        this.nguyenLieuId = nguyenLieuId;
    }

    public double getSoLuongCan() {
        return soLuongCan;
    }

    public void setSoLuongCan(double soLuongCan) {
        this.soLuongCan = soLuongCan;
    }
// Getter, Setter
}
