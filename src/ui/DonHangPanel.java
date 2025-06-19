package ui;

import dao.HoaDonDAO;
import dao.OrderDAO;
import model.HoaDon;
import model.Order;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
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
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 👉 Nút điều hướng căn giữa
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 10)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, new Color(240, 240, 240), 0, getHeight(), new Color(200, 200, 200));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        JButton btnDatMon = createStyledButton("🍽 Đặt món");
        JButton btnHoaDon = createStyledButton("🧾 Hóa đơn");
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
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(150, 150, 150)), "Danh sách đặt món", TitledBorder.CENTER, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 14), Color.DARK_GRAY));

        tableOrder = createStyledTable();
        loadOrderData();
        JScrollPane scrollPane = new JScrollPane(tableOrder);
        panel.add(scrollPane, BorderLayout.CENTER);

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
        tableHoaDon = createStyledTable();
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

        JScrollPane scrollPane = new JScrollPane(tableHoaDon);
        scrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(150, 150, 150)), "Danh sách hóa đơn", TitledBorder.CENTER, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 14), Color.DARK_GRAY));
        return scrollPane;
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
        adjustColumnWidths(tableOrder);
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
        adjustColumnWidths(tableHoaDon);
    }

    private JTable createStyledTable() {
        JTable t = new JTable();
        JTableHeader header = t.getTableHeader();
        header.setReorderingAllowed(false);
        header.setBackground(new Color(240, 240, 240));
        header.setForeground(Color.DARK_GRAY);
        t.setRowHeight(25);
        t.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        t.setShowGrid(true);
        t.setGridColor(new Color(200, 200, 200));
        return t;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(new Color(0, 120, 215));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0, 100, 200), 1),
                BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        return button;
    }

    private void adjustColumnWidths(JTable table) {
        table.getColumnModel().getColumn(0).setPreferredWidth(50); // ID
        if (table == tableOrder) {
            table.getColumnModel().getColumn(3).setPreferredWidth(120); // Ngày tạo
        } else if (table == tableHoaDon) {
            table.getColumnModel().getColumn(2).setPreferredWidth(100); // Tổng tiền
            table.getColumnModel().getColumn(3).setPreferredWidth(120); // Thời gian thanh toán
        }
    }
}