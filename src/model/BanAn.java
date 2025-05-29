package model;

public class BanAn {
    private int id;
    private String trangThai;

    public BanAn() {}

    public BanAn(int id, String trangThai) {
        this.id = id;
        this.trangThai = trangThai;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }
// Getter, Setter
}
