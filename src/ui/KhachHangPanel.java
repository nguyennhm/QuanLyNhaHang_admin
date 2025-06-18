package ui;

import dao.KhachHangDAO;
import dao.XepHangKhachHangDAO;
import model.KhachHang;
import utils.JDBCUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class KhachHangPanel extends JPanel {
    private JTable table;
    private KhachHangDAO khachHangDAO;

    public KhachHangPanel() {
        khachHangDAO = new KhachHangDAO(JDBCUtil.getConnection());

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Danh sách khách hàng"));

        table = createNonEditableTable();
        reloadTable();
        add(new JScrollPane(table), BorderLayout.CENTER);

        JButton btnThem = new JButton("➕ Thêm khách hàng");
        btnThem.addActionListener(e -> showAddForm());

        JButton btnXoa = new JButton("🗑 Xóa khách hàng");
        btnXoa.addActionListener(e -> xoaKhachHang());

        JButton btnCongDiem = new JButton("🎯 Cộng điểm");
        btnCongDiem.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                int id = (int) table.getValueAt(selectedRow, 0);
                showAddPointForm(id);
            } else {
                JOptionPane.showMessageDialog(this, "⚠ Vui lòng chọn khách hàng để cộng điểm!");
            }
        });

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.add(btnThem);
        btnPanel.add(btnXoa);
        btnPanel.add(btnCongDiem);
        add(btnPanel, BorderLayout.SOUTH);

        // Add double-click listener only if not already added
        if (table.getMouseListeners().length == 1) { // chỉ có default JTableHeader
            table.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent evt) {
                    if (evt.getClickCount() == 2) {
                        int row = table.rowAtPoint(evt.getPoint());
                        if (row >= 0) {
                            int id = (int) table.getValueAt(row, 0);
                            showDetailForm(id);
                        }
                    }
                }
            });
        }
    }

    public void reloadTable() {
        List<KhachHang> list = khachHangDAO.getAllKhachHang();
        String[] cols = {"ID", "Họ tên", "SĐT", "Điểm tích lũy", "Cấp bậc", "Sinh nhật"};
        Object[][] data = new Object[list.size()][cols.length];
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        for (int i = 0; i < list.size(); i++) {
            KhachHang kh = list.get(i);
            data[i][0] = kh.getId();
            data[i][1] = kh.getHoTen();
            data[i][2] = kh.getSoDienThoai();
            data[i][3] = kh.getDiemTichLuy();
            data[i][4] = kh.getCapBac();
            data[i][5] = kh.getSinhNhat() != null ? sdf.format(kh.getSinhNhat()) : "";
        }

        DefaultTableModel model = new DefaultTableModel(data, cols) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table.setModel(model);
    }

    private void showAddForm() {
        JDialog f = new JDialog(SwingUtilities.getWindowAncestor(this), "Thêm khách hàng", Dialog.ModalityType.APPLICATION_MODAL);
        f.setSize(400, 350);
        f.setLocationRelativeTo(this);
        f.setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        JTextField tfHoTen = new JTextField();
        JTextField tfSdt = new JTextField();
        JTextField tfDiemTichLuy = new JTextField("0");
        JLabel lblCapBac = new JLabel("Thường"); // Mặc định cấp bậc là "Thường"
        JTextField tfSinhNhat = new JTextField("dd/MM/yyyy");

        formPanel.add(new JLabel("Họ tên:")); formPanel.add(tfHoTen);
        formPanel.add(new JLabel("Số điện thoại:")); formPanel.add(tfSdt);
        formPanel.add(new JLabel("Điểm tích lũy:")); formPanel.add(tfDiemTichLuy);
        formPanel.add(new JLabel("Cấp bậc:")); formPanel.add(lblCapBac);
        formPanel.add(new JLabel("Sinh nhật:")); formPanel.add(tfSinhNhat);

        JButton btnLuu = new JButton("Lưu");
        btnLuu.addActionListener(e -> {
            try {
                KhachHang kh = new KhachHang();
                kh.setHoTen(tfHoTen.getText().trim());
                kh.setSoDienThoai(tfSdt.getText().trim());
                kh.setDiemTichLuy(Integer.parseInt(tfDiemTichLuy.getText().trim()));
                kh.setCapBac("Thường"); // Không cho chọn, luôn mặc định
                String sinhNhatStr = tfSinhNhat.getText().trim();
                if (!sinhNhatStr.isEmpty()) {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    sdf.setLenient(false);
                    kh.setSinhNhat(sdf.parse(sinhNhatStr));
                }

                boolean ok = khachHangDAO.themKhachHang(kh);
                if (ok) {
                    reloadTable();
                    f.dispose();
                    JOptionPane.showMessageDialog(this, "Thêm khách hàng thành công!");
                } else {
                    JOptionPane.showMessageDialog(f, "Thêm khách hàng thất bại!");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(f, "Điểm tích lũy phải là số nguyên!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(f, "Lỗi dữ liệu: " + ex.getMessage());
            }
        });

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.add(btnLuu);

        f.add(formPanel, BorderLayout.CENTER);
        f.add(bottom, BorderLayout.SOUTH);
        f.setVisible(true);
    }

    public void showDetailForm(int khachHangId) {
        KhachHang kh = khachHangDAO.getKhachHangById(khachHangId);

        JDialog f = new JDialog(SwingUtilities.getWindowAncestor(this), "Chi tiết khách hàng", Dialog.ModalityType.APPLICATION_MODAL);
        f.setSize(400, 350);
        f.setLocationRelativeTo(this);
        f.setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        JTextField tfHoTen = new JTextField(kh.getHoTen());
        JTextField tfSdt = new JTextField(kh.getSoDienThoai());
        JTextField tfDiemTichLuy = new JTextField(String.valueOf(kh.getDiemTichLuy()));
        JLabel lblCapBac = new JLabel(kh.getCapBac()); // chỉ hiển thị cấp bậc
        JTextField tfSinhNhat = new JTextField(kh.getSinhNhat() != null ? new SimpleDateFormat("dd/MM/yyyy").format(kh.getSinhNhat()) : "");

        lblCapBac.setFont(lblCapBac.getFont().deriveFont(Font.BOLD));
        lblCapBac.setForeground(Color.BLUE);

        formPanel.add(new JLabel("Họ tên:")); formPanel.add(tfHoTen);
        formPanel.add(new JLabel("Số điện thoại:")); formPanel.add(tfSdt);
        formPanel.add(new JLabel("Điểm tích lũy:")); formPanel.add(tfDiemTichLuy);
        formPanel.add(new JLabel("Cấp bậc:")); formPanel.add(lblCapBac); // Không cho chỉnh sửa
        formPanel.add(new JLabel("Sinh nhật:")); formPanel.add(tfSinhNhat);

        JButton btnLuu = new JButton("Cập nhật");
        btnLuu.addActionListener(e -> {
            try {
                kh.setHoTen(tfHoTen.getText().trim());
                kh.setSoDienThoai(tfSdt.getText().trim());
                kh.setDiemTichLuy(Integer.parseInt(tfDiemTichLuy.getText().trim()));
                // Không set lại cấp bậc thủ công!
                String sinhNhatStr = tfSinhNhat.getText().trim();
                if (!sinhNhatStr.isEmpty()) {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    sdf.setLenient(false);
                    kh.setSinhNhat(sdf.parse(sinhNhatStr));
                } else {
                    kh.setSinhNhat(null);
                }

                boolean ok = khachHangDAO.capNhatKhachHang(kh);
                if (ok) {
                    reloadTable();
                    f.dispose();
                    JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                } else {
                    JOptionPane.showMessageDialog(f, "Cập nhật thất bại!");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(f, "Điểm tích lũy phải là số nguyên!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(f, "Lỗi dữ liệu: " + ex.getMessage());
            }
        });

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.add(btnLuu);

        f.add(formPanel, BorderLayout.CENTER);
        f.add(bottom, BorderLayout.SOUTH);
        f.setVisible(true);
    }

    private void showAddPointForm(int khachHangId) {
        KhachHang kh = khachHangDAO.getKhachHangById(khachHangId);

        JDialog f = new JDialog(SwingUtilities.getWindowAncestor(this), "Cộng điểm cho khách hàng: " + kh.getHoTen(), Dialog.ModalityType.APPLICATION_MODAL);
        f.setSize(400, 200);
        f.setLocationRelativeTo(this);
        f.setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        JTextField tfSoTien = new JTextField("0");
        JLabel lblDiemHienTai = new JLabel("Điểm hiện tại: " + kh.getDiemTichLuy());
        JComboBox<String> cbPhanTram = new JComboBox<>(new String[]{"10%", "20%", "50%", "100%"});

        formPanel.add(new JLabel("Số tiền chi tiêu:")); formPanel.add(tfSoTien);
        formPanel.add(new JLabel("Tỷ lệ quy đổi:")); formPanel.add(cbPhanTram);
        formPanel.add(lblDiemHienTai);

        JButton btnCong = new JButton("Cộng điểm");
        btnCong.addActionListener(e -> {
            try {
                double soTien = Double.parseDouble(tfSoTien.getText().trim());
                String phanTram = (String) cbPhanTram.getSelectedItem();
                double tyLe = Double.parseDouble(phanTram.replace("%", "")) / 100;
                int diemCong = (int) (soTien * tyLe / 1000);
                int diemMoi = kh.getDiemTichLuy() + diemCong;

                kh.setDiemTichLuy(diemMoi);
                int xepHangId = new XepHangKhachHangDAO(JDBCUtil.getConnection()).getXepHangIdByDiem(diemMoi);
                kh.setCapBac(new XepHangKhachHangDAO(JDBCUtil.getConnection()).getCapBacById(xepHangId));

                boolean ok = khachHangDAO.capNhatKhachHang(kh);
                if (ok) {
                    reloadTable();
                    f.dispose();
                    JOptionPane.showMessageDialog(this, "Cộng " + diemCong + " điểm thành công! Điểm mới: " + diemMoi);
                } else {
                    JOptionPane.showMessageDialog(f, "Cộng điểm thất bại!");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(f, "Số tiền phải là số hợp lệ!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(f, "Lỗi: " + ex.getMessage());
            }
        });

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.add(btnCong);

        f.add(formPanel, BorderLayout.CENTER);
        f.add(bottom, BorderLayout.SOUTH);
        f.setVisible(true);
    }

    private void xoaKhachHang() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "⚠ Vui lòng chọn khách hàng cần xóa.");
            return;
        }

        int id = (int) table.getValueAt(selectedRow, 0);
        String ten = table.getValueAt(selectedRow, 1).toString();

        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc chắn muốn xóa khách hàng \"" + ten + "\"?",
                "Xác nhận xóa", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean ok = khachHangDAO.xoaKhachHang(id);
            if (ok) {
                JOptionPane.showMessageDialog(this, "✅ Đã xóa khách hàng.");
                reloadTable();
            } else {
                JOptionPane.showMessageDialog(this, "❌ Không thể xóa. Khách hàng có thể đang được sử dụng.");
            }
        }
    }

    private JTable createNonEditableTable() {
        JTable t = new JTable();
        JTableHeader header = t.getTableHeader();
        header.setReorderingAllowed(false);
        return t;
    }

    public JTable getTable() {
        return table;
    }
}
