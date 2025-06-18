package model;

public class TaiKhoan {


    private int id_taikhoan;
    private String email;
    private String matKhau;
    private String vaiTro;
    private String trangThai;

    public TaiKhoan(String email, String matKhau, String vaiTro, String trangThai) {
        this.email = email;
        this.matKhau = matKhau;
        this.vaiTro = vaiTro;
        this.trangThai = trangThai;
    }

    // Getters và Setters
    public int getId_taikhoan() {
        return id_taikhoan;
    }

    public void setId_taikhoan(int id_taikhoan) {
        this.id_taikhoan = id_taikhoan;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    public String getVaiTro() {
        return vaiTro;
    }

    public void setVaiTro(String vaiTro) {
        this.vaiTro = vaiTro;
    }

    public String isTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }
}
