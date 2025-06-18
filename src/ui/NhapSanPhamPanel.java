package ui;

import dao.MonAnDAO;
import dao.NguyenLieuDAO;
import model.MonAn;
import model.NguyenLieu;
import utils.JDBCUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class NhapSanPhamPanel extends JPanel {
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private NguyenLieuPanel nguyenLieuPanel;
    private MonAnPanel monAnPanel;

    public NhapSanPhamPanel() {
        setLayout(new BorderLayout());

        // ==== Nút chuyển panel ====
        JPanel switchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton btnNguyenLieu = new JButton("Nguyên liệu");
        JButton btnMonAn = new JButton("Món ăn");
        switchPanel.add(btnNguyenLieu);
        switchPanel.add(btnMonAn);
        add(switchPanel, BorderLayout.NORTH);

        // ==== Tạo các panel ====
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        nguyenLieuPanel = new NguyenLieuPanel();
        monAnPanel = new MonAnPanel();

        cardPanel.add(nguyenLieuPanel, "nguyenlieu");
        cardPanel.add(monAnPanel, "monan");
        add(cardPanel, BorderLayout.CENTER);

        // ==== Xử lý nút chuyển panel ====
        btnNguyenLieu.addActionListener(e -> {
            nguyenLieuPanel.reloadTable();
            cardLayout.show(cardPanel, "nguyenlieu");
        });

        btnMonAn.addActionListener(e -> {
            monAnPanel.reloadTable();
            cardLayout.show(cardPanel, "monan");
        });

        cardLayout.show(cardPanel, "nguyenlieu");

        // ==== Sự kiện click bảng món ăn ====
        monAnPanel.getTable().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = monAnPanel.getTable().getSelectedRow();
                    if (row != -1) {
                        Object value = monAnPanel.getTable().getValueAt(row, 0);
                        if (value instanceof Number) {
                            int monAnId = ((Number) value).intValue();
                            MonAnDAO dao = new MonAnDAO(JDBCUtil.getConnection());
                            MonAn monAn = dao.getMonAnById(monAnId);
                            if (monAn != null) {
                                new MonAnUpdateForm(monAn, () -> monAnPanel.reloadTable());
                            }
                        }
                    }
                }
            }
        });

        // ==== Sự kiện click bảng nguyên liệu ====
        nguyenLieuPanel.getTable().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = nguyenLieuPanel.getTable().getSelectedRow();
                    if (row != -1) {
                        Object value = nguyenLieuPanel.getTable().getValueAt(row, 0);
                        if (value instanceof Number) {
                            int nguyenLieuId = ((Number) value).intValue();
                            NguyenLieuDAO dao = new NguyenLieuDAO(JDBCUtil.getConnection());
                            NguyenLieu nl = dao.getNguyenLieuById(nguyenLieuId);
                            if (nl != null) {
                                new NguyenLieuUpdateForm(nl, () -> nguyenLieuPanel.reloadTable());
                            }
                        }
                    }
                }
            }
        });
    }

    // ==== Tạo bảng không chỉnh sửa và không di chuyển cột ====
    public static JTable createNonEditableTable(Object[][] data, Object[] columnNames) {
        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable table = new JTable(model);
        JTableHeader header = table.getTableHeader();
        header.setReorderingAllowed(false);
        return table;
    }
}
