package model;

public class MonAn {
    private int id;
    private String tenMon;
    private double gia;
    private String trangThai;
    private String moTa;
    private String hinhAnh;
    private Integer id_thumuc;
    private Integer tonKhaDung;


    public MonAn() {
    }

    public Integer getId_thumuc() {
        return id_thumuc;
    }

    public void setId_thumuc(Integer id_thumuc) {
        this.id_thumuc = id_thumuc;
    }

    public Integer getTonKhaDung() {
        return tonKhaDung;
    }

    public void setTonKhaDung(Integer tonKhaDung) {
        this.tonKhaDung = tonKhaDung;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTenMon() {
        return tenMon;
    }

    public void setTenMon(String tenMon) {
        this.tenMon = tenMon;
    }

    public double getGia() {
        return gia;
    }

    public void setGia(double gia) {
        this.gia = gia;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public String getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
    }

    public MonAn(int id, String tenMon, double gia, String trangThai, String moTa, String hinhAnh) {
        this.id = id;
        this.tenMon = tenMon;
        this.gia = gia;
        this.trangThai = trangThai;
        this.moTa = moTa;
        this.hinhAnh = hinhAnh;
    }

    // Getter, Setter
}
