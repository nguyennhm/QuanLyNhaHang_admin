package model;

public class XepHangKhachHang {
    private int id;
    private String capBac;
    private int dieuKienDiem;
    private float uuDai;

    public XepHangKhachHang() {}

    public XepHangKhachHang(int id, String capBac, int dieuKienDiem, float uuDai) {
        this.id = id;
        this.capBac = capBac;
        this.dieuKienDiem = dieuKienDiem;
        this.uuDai = uuDai;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCapBac() {
        return capBac;
    }

    public void setCapBac(String capBac) {
        this.capBac = capBac;
    }

    public int getdieuKienDiem() {
        return dieuKienDiem;
    }

    public void setdieuKienDiem(int dieuKienDiem) {
        this.dieuKienDiem = dieuKienDiem;
    }

    public float getUuDai() {
        return uuDai;
    }

    public void setUuDai(float uuDai) {
        this.uuDai = uuDai;
    }
}