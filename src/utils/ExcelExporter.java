package utils;

import dao.LuongDAO;
import dao.NguyenLieuDAO;
import dao.HoaDonDAO;
import dao.ChiTietOrderDAO;
import model.Luong;
import model.NguyenLieu;
import model.HoaDon;
import model.ChiTietOrder;

import jxl.Workbook;
import jxl.write.*;

import java.io.File;
import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

public class ExcelExporter {

    public static void exportToExcel(String filePath) {
        try {
            WritableWorkbook workbook = Workbook.createWorkbook(new File(filePath));

            // ===== Sheet 1: Lương =====
            WritableSheet sheet1 = workbook.createSheet("Luong", 0);
            LuongDAO luongDAO = new LuongDAO();
            List<Luong> luongs = luongDAO.selectAll();

            sheet1.addCell(new Label(0, 0, "ID"));
            sheet1.addCell(new Label(1, 0, "Tên nhân viên"));
            sheet1.addCell(new Label(2, 0, "Hệ số lương"));
            sheet1.addCell(new Label(3, 0, "Điểm thưởng"));
            sheet1.addCell(new Label(4, 0, "Mã nhân viên"));

            int row = 1;
            for (Luong l : luongs) {
                sheet1.addCell(new jxl.write.Number(0, row, l.getId()));
                sheet1.addCell(new Label(1, row, l.getTenNhanVien()));
                sheet1.addCell(new jxl.write.Number(2, row, l.getHeSoLuong()));
                sheet1.addCell(new jxl.write.Number(3, row, l.getDiemThuong()));
                sheet1.addCell(new jxl.write.Number(4, row, l.getIdNhanVien()));
                row++;
            }

            // ===== Sheet 2: Nguyên liệu =====
            WritableSheet sheet2 = workbook.createSheet("NguyenLieu", 1);
            NguyenLieuDAO nlDAO = new NguyenLieuDAO();
            List<NguyenLieu> nlList = nlDAO.getAllNguyenLieu();

            sheet2.addCell(new Label(0, 0, "ID"));
            sheet2.addCell(new Label(1, 0, "Tên nguyên liệu"));
            sheet2.addCell(new Label(2, 0, "Đơn vị tính"));
            sheet2.addCell(new Label(3, 0, "Số lượng tồn"));
            sheet2.addCell(new Label(4, 0, "Giá nhập"));

            row = 1;
            for (NguyenLieu n : nlList) {
                sheet2.addCell(new jxl.write.Number(0, row, n.getId()));
                sheet2.addCell(new Label(1, row, n.getTenNguyenLieu()));
                sheet2.addCell(new Label(2, row, n.getDonViTinh()));
                sheet2.addCell(new jxl.write.Number(3, row, n.getSoLuongTon()));
                sheet2.addCell(new jxl.write.Number(4, row, n.getGiaNhap()));
                row++;
            }

            // ===== Sheet 3: Hóa đơn =====
            WritableSheet sheet3 = workbook.createSheet("HoaDon", 2);
            HoaDonDAO hdDAO = new HoaDonDAO();
            List<HoaDon> hdList = hdDAO.getAllHoaDon();

            sheet3.addCell(new Label(0, 0, "ID"));
            sheet3.addCell(new Label(1, 0, "Order ID"));
            sheet3.addCell(new Label(2, 0, "Tổng tiền"));
            sheet3.addCell(new Label(3, 0, "Thời gian thanh toán"));

            row = 1;
            for (HoaDon h : hdList) {
                sheet3.addCell(new jxl.write.Number(0, row, h.getId()));
                sheet3.addCell(new jxl.write.Number(1, row, h.getOrderId()));
                sheet3.addCell(new jxl.write.Number(2, row, h.getTongTien()));
                sheet3.addCell(new Label(3, row, h.getThoiGianThanhToan().toString()));
                row++;
            }

            // ===== Sheet 4: Chi tiết Order =====
            WritableSheet sheet4 = workbook.createSheet("ChiTietOrder", 3);
            ChiTietOrderDAO ctDAO = new ChiTietOrderDAO();
            List<ChiTietOrder> ctList = ctDAO.getAll(); // Hoặc viết thêm method getAll()

            sheet4.addCell(new Label(0, 0, "ID"));
            sheet4.addCell(new Label(1, 0, "Order ID"));
            sheet4.addCell(new Label(2, 0, "Món ăn ID"));
            sheet4.addCell(new Label(3, 0, "Tên món"));
            sheet4.addCell(new Label(4, 0, "Số lượng"));
            sheet4.addCell(new Label(5, 0, "Trạng thái"));

            row = 1;
            for (ChiTietOrder c : ctList) {
                sheet4.addCell(new jxl.write.Number(0, row, c.getId()));
                sheet4.addCell(new jxl.write.Number(1, row, c.getOrderId()));
                sheet4.addCell(new jxl.write.Number(2, row, c.getMonAnId()));
                sheet4.addCell(new Label(3, row, c.getTenMon()));
                sheet4.addCell(new jxl.write.Number(4, row, c.getSoLuong()));
                sheet4.addCell(new Label(5, row, c.getTrangThai()));
                row++;
            }

            workbook.write();
            workbook.close();
            // Sau khi xuất Excel xong → reset dữ liệu
            resetDatabase();
            System.out.println("✅ Đã xuất Excel thành công tại: " + filePath);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void resetDatabase() {
        try (Connection conn = JDBCUtil.getConnection()) {
            Statement stmt = conn.createStatement();

            // Reset số lượng nguyên liệu
            stmt.executeUpdate("UPDATE nguyenlieu SET soLuongTon = 0, giaNhap = 0");

            // Xoá toàn bộ hóa đơn và chi tiết
            stmt.executeUpdate("DELETE FROM chitiethoadon");
            stmt.executeUpdate("DELETE FROM chitietorder");
            stmt.executeUpdate("DELETE FROM hoadon");
            stmt.executeUpdate("DELETE FROM `order`");


            // Xoá bảng lương (nếu cần reset)
//             stmt.executeUpdate("DELETE FROM luong");

            // Reset điểm thưởng nhân viên (nếu có)
             stmt.executeUpdate("UPDATE luong SET diemthuong = 0");

            System.out.println("✅ Đã reset dữ liệu trong CSDL");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        exportToExcel("ThongKeNhaHang.xls");
    }
}
