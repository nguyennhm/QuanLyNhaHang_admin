package ui;

import dao.HoaDonDAO;
import dao.OrderDAO;
import model.HoaDon;
import model.Order;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class DonHangPanel extends JPanel {
    private CardLayout cardLayout;
    private JPanel contentPanel;
    private JTable tableOrder, tableHoaDon;
    private OrderDAO orderDAO;
    private HoaDonDAO hoaDonDAO;

    public DonHangPanel() {
        orderDAO = new OrderDAO();
        hoaDonDAO = new HoaDonDAO();

        setLayout(new BorderLayout());

        // 👉 Nút điều hướng căn giữa
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 10));
        JButton btnDatMon = new JButton("🍽 Đặt món");
        JButton btnHoaDon = new JButton("🧾 Hóa đơn");

        btnDatMon.setPreferredSize(new Dimension(140, 40));
        btnHoaDon.setPreferredSize(new Dimension(140, 40));
        btnDatMon.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnHoaDon.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        buttonPanel.add(btnDatMon);
        buttonPanel.add(btnHoaDon);
        add(buttonPanel, BorderLayout.NORTH);

        // 👉 Panel chứa nội dung đổi qua lại
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.add(createOrderPanel(), "Order");
        contentPanel.add(createHoaDonPanel(), "HoaDon");

        add(contentPanel, BorderLayout.CENTER);

        btnDatMon.addActionListener(e -> cardLayout.show(contentPanel, "Order"));
        btnHoaDon.addActionListener(e -> cardLayout.show(contentPanel, "HoaDon"));
    }

    private JPanel createOrderPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        tableOrder = new JTable();
        loadOrderData();

        JScrollPane scrollPane = new JScrollPane(tableOrder);
        panel.add(scrollPane, BorderLayout.CENTER);

        // 👉 Click row → mở JFrame hiển thị chi tiết order
        tableOrder.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = tableOrder.getSelectedRow();
                if (selectedRow != -1) {
                    int orderId = (int) tableOrder.getValueAt(selectedRow, 0);
                    new ChiTietOrderFrame(orderId).setVisible(true);
                }
            }
        });

        return panel;
    }

    private JScrollPane createHoaDonPanel() {
        tableHoaDon = new JTable();
        loadHoaDonData();
        tableHoaDon.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = tableHoaDon.getSelectedRow();
                if (selectedRow != -1) {
                    int hoaDonId = (int) tableHoaDon.getValueAt(selectedRow, 0);
                    new ChiTietHoaDonFrame(hoaDonId).setVisible(true);
                }
            }
        });

        return new JScrollPane(tableHoaDon);
    }

    private void loadOrderData() {
        List<Order> list = orderDAO.getAllOrders();
        String[] cols = {"ID", "ID Bàn", "ID KH", "Ngày tạo", "Trạng thái"};
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        Object[][] data = new Object[list.size()][cols.length];
        for (int i = 0; i < list.size(); i++) {
            Order o = list.get(i);
            data[i][0] = o.getId();
            data[i][1] = o.getBanAnId();
            data[i][2] = o.getId_nhanvien();
            data[i][3] = o.getThoiGianTao() != null ? o.getThoiGianTao().format(dtf) : "";
            data[i][4] = o.getTrangThai();
        }

        DefaultTableModel model = new DefaultTableModel(data, cols) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableOrder.setModel(model);
        tableOrder.getTableHeader().setReorderingAllowed(false);
    }

    private void loadHoaDonData() {
        List<HoaDon> list = hoaDonDAO.getAllHoaDon();
        String[] cols = {"ID", "ID Order", "Tổng tiền", "Thời gian thanh toán"};
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        Object[][] data = new Object[list.size()][cols.length];
        for (int i = 0; i < list.size(); i++) {
            HoaDon h = list.get(i);
            data[i][0] = h.getId();
            data[i][1] = h.getOrderId();
            data[i][2] = h.getTongTien();
            data[i][3] = h.getThoiGianThanhToan() != null ? h.getThoiGianThanhToan().format(dtf) : "";
        }

        DefaultTableModel model = new DefaultTableModel(data, cols) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableHoaDon.setModel(model);
        tableHoaDon.getTableHeader().setReorderingAllowed(false);
    }

}
