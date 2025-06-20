package service;

import dao.LuongDAO;
import dao.NhanVienDAO;
import model.Luong;
import model.Nhanvien;
import utils.EmailUtil;
import utils.JDBCUtil;

import java.sql.Connection;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class BirthdayService {
    public void checkAndRewardBirthdays() {
        try (Connection conn = JDBCUtil.getConnection()) {
            NhanVienDAO nvDAO = new NhanVienDAO(conn);
            LuongDAO luongDAO = new LuongDAO();

            List<Nhanvien> nhanViens = nvDAO.getAllNhanVien();
            Calendar today = Calendar.getInstance();

            for (Nhanvien nv : nhanViens) {
                if (isTodayBirthday(nv.getNgaysinh(), today)) {
                    // Gửi email chúc mừng
                    EmailUtil.sendBirthdayEmail(nv.getSdt(), nv.getTen());

                    // Cộng điểm thưởng
                    List<Luong> allLuong = luongDAO.selectAll();
                    for (Luong luong : allLuong) {
                        if (luong.getIdNhanVien() == nv.getId_nhanvien()) {
                            luong.setDiemThuong(luong.getDiemThuong() + 50);
                            luongDAO.update(luong);
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isTodayBirthday(Date birthDate, Calendar today) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(birthDate);
        return cal.get(Calendar.MONTH) == today.get(Calendar.MONTH) &&
                cal.get(Calendar.DAY_OF_MONTH) == today.get(Calendar.DAY_OF_MONTH);
    }
}
