package model;

public class XepHangKhachHang {
    private String capBac;
    private int minTongChiTieu;
    private String uuDai;

    public XepHangKhachHang() {}

    public XepHangKhachHang(String capBac, int minTongChiTieu, String uuDai) {
        this.capBac = capBac;
        this.minTongChiTieu = minTongChiTieu;
        this.uuDai = uuDai;
    }

    // Getter, Setter

    public String getCapBac() {
        return capBac;
    }

    public void setCapBac(String capBac) {
        this.capBac = capBac;
    }

    public int getMinTongChiTieu() {
        return minTongChiTieu;
    }

    public void setMinTongChiTieu(int minTongChiTieu) {
        this.minTongChiTieu = minTongChiTieu;
    }

    public String getUuDai() {
        return uuDai;
    }

    public void setUuDai(String uuDai) {
        this.uuDai = uuDai;
    }
}
