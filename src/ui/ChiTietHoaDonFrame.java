package ui;

import dao.ChiTietHoaDonDAO;
import model.ChiTietHoaDon;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.print.PrinterException;
import java.text.MessageFormat;
import java.util.List;

public class ChiTietHoaDonFrame extends JFrame {
    private JTable table;
    private int hoaDonId;

    public ChiTietHoaDonFrame(int hoaDonId) {
        this.hoaDonId = hoaDonId;
        setTitle("Chi tiết hóa đơn #" + hoaDonId);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        table = new JTable();
        loadData();

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // 👉 Nút In hóa đơn
        JButton btnPrint = new JButton("🖨 In hóa đơn");
        btnPrint.addActionListener(e -> printInvoice());

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(btnPrint);
        add(bottomPanel, BorderLayout.SOUTH);

        // 👉 Tự động vừa kích thước bảng
//        pack();
//        setLocationRelativeTo(null); // căn giữa
        adjustFrameSize();
    }

    private void loadData() {
        ChiTietHoaDonDAO dao = new ChiTietHoaDonDAO();
        List<ChiTietHoaDon> list = dao.getByHoaDonId(hoaDonId);

        String[] columnNames = {"Tên món ăn", "Số lượng", "Thành tiền (VNĐ)"};
        Object[][] data = new Object[list.size()][columnNames.length];

        for (int i = 0; i < list.size(); i++) {
            ChiTietHoaDon cthd = list.get(i);
            data[i][0] = cthd.getTenMonAn();
            data[i][1] = cthd.getSoLuong();
            data[i][2] = String.format("%,d", cthd.getTongTien()); // định dạng tiền có dấu phẩy
        }

        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table.setModel(model);
        table.getTableHeader().setReorderingAllowed(false);
    }

    // 👉 Hàm in hóa đơn
    private void printInvoice() {
        MessageFormat header = new MessageFormat("HÓA ĐƠN #" + hoaDonId);
        MessageFormat footer = new MessageFormat("Trang {0}");

        try {
            boolean printed = table.print(JTable.PrintMode.FIT_WIDTH, header, footer);
            if (!printed) {
                JOptionPane.showMessageDialog(this, "In bị hủy.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (PrinterException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi in: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // resize lại khung hóa đơn
    private void adjustFrameSize() {
        int rowHeight = table.getRowHeight();
        int rowCount = table.getRowCount();
        int tableHeight = rowHeight * rowCount + table.getTableHeader().getPreferredSize().height;

        int tableWidth = table.getPreferredSize().width;

        int maxWidth = 600;
        int maxHeight = 500;

        // Giới hạn tối đa để không tràn màn hình
        int finalWidth = Math.min(tableWidth + 50, maxWidth);
        int finalHeight = Math.min(tableHeight + 100, maxHeight);

        setSize(finalWidth, finalHeight);
        setLocationRelativeTo(null);
    }

}
