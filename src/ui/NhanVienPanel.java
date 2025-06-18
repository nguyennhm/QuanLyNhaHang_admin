package ui;

import dao.NhanVienDAO;
import dao.TaiKhoanDAO;
import model.Nhanvien;
import model.TaiKhoan;
import utils.JDBCUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class NhanVienPanel extends JPanel {
    private JTable table;
    private NhanVienDAO nhanVienDAO;
    private TaiKhoanDAO taiKhoanDAO;

    public NhanVienPanel() {
        nhanVienDAO = new NhanVienDAO(JDBCUtil.getConnection());
        taiKhoanDAO = new TaiKhoanDAO(JDBCUtil.getConnection());

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Danh sách nhân viên"));

        table = createNonEditableTable();
        reloadTable();

        add(new JScrollPane(table), BorderLayout.CENTER);

        JButton btnThem = new JButton("➕ Thêm nhân viên");
        btnThem.addActionListener(e -> showAddForm());

        JButton btnQuenMatKhau = new JButton("Quên mật khẩu");
        btnQuenMatKhau.addActionListener(e -> new ForgotPasswordUI());

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.add(btnThem);
        btnPanel.add(btnQuenMatKhau);
        add(btnPanel, BorderLayout.SOUTH);
    }

    public void reloadTable() {
        List<Nhanvien> list = nhanVienDAO.getAllNhanVien();
        String[] cols = {"ID", "Tên", "SĐT", "Vị trí", "Trạng thái tài khoản"};
        Object[][] data = new Object[list.size()][cols.length];

        for (int i = 0; i < list.size(); i++) {
            Nhanvien nv = list.get(i);
            TaiKhoan tk = taiKhoanDAO.getTaiKhoanById(nv.getId_taikhoan());
            data[i][0] = nv.getId_nhanvien();
            data[i][1] = nv.getTen();
            data[i][2] = nv.getSdt();
            data[i][3] = nv.getVitri();
            data[i][4] = tk != null ? tk.getTrangThai() : "Không có tài khoản";
        }

        DefaultTableModel model = new DefaultTableModel(data, cols) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table.setModel(model);
    }

    public void showAddForm() {
        JFrame f = new JFrame("Thêm nhân viên");
        f.setSize(500, 400);
        f.setLocationRelativeTo(this);
        f.setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(7, 2, 10, 10));
        JTextField tfTen = new JTextField();
        JTextField tfSdt = new JTextField();
        JTextField tfDiaChi = new JTextField();
        JTextField tfNgaySinh = new JTextField("dd/MM/yyyy");
        JTextField tfViTri = new JTextField();
        JTextField tfEmail = new JTextField();
        JPasswordField tfMatKhau = new JPasswordField();
        JComboBox<String> cbVaiTro = new JComboBox<>(new String[]{"Nhân viên", "Quản lý"});

        formPanel.add(new JLabel("Tên nhân viên:")); formPanel.add(tfTen);
        formPanel.add(new JLabel("Số điện thoại:")); formPanel.add(tfSdt);
        formPanel.add(new JLabel("Địa chỉ:")); formPanel.add(tfDiaChi);
        formPanel.add(new JLabel("Ngày sinh:")); formPanel.add(tfNgaySinh);
        formPanel.add(new JLabel("Vị trí:")); formPanel.add(tfViTri);
        formPanel.add(new JLabel("Email:")); formPanel.add(tfEmail);
        formPanel.add(new JLabel("Mật khẩu:")); formPanel.add(tfMatKhau);
        formPanel.add(new JLabel("Vai trò:")); formPanel.add(cbVaiTro);

        JButton btnLuu = new JButton("Lưu");
        btnLuu.addActionListener(e -> {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                Nhanvien nv = new Nhanvien();
                nv.setTen(tfTen.getText());
                nv.setSdt(tfSdt.getText());
                nv.setDiachi(tfDiaChi.getText());
                nv.setNgaysinh(sdf.parse(tfNgaySinh.getText()));
                nv.setVitri(tfViTri.getText());

                TaiKhoan tk = new TaiKhoan(
                        tfEmail.getText(),
                        new String(tfMatKhau.getPassword()),
                        (String) cbVaiTro.getSelectedItem(),
                        "Hoạt động"
                );

                boolean taiKhoanOk = taiKhoanDAO.themTaiKhoan(tk);
                if (taiKhoanOk) {
                    nv.setId_taikhoan(tk.getId_taikhoan());
                    boolean nhanVienOk = nhanVienDAO.themNhanVien(nv);
                    if (nhanVienOk) {
                        reloadTable();
                        f.dispose();
                        JOptionPane.showMessageDialog(this, "Thêm nhân viên thành công!");
                    } else {
                        taiKhoanDAO.xoaTaiKhoan(tk.getId_taikhoan());
                        JOptionPane.showMessageDialog(f, "Thêm nhân viên thất bại!");
                    }
                } else {
                    JOptionPane.showMessageDialog(f, "Thêm tài khoản thất bại!");
                }
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

    public void showDetailForm(int nhanVienId) {
        Nhanvien nv = nhanVienDAO.getNhanVienById(nhanVienId);
        TaiKhoan tk = taiKhoanDAO.getTaiKhoanById(nv.getId_taikhoan());

        JFrame f = new JFrame("Chi tiết nhân viên");
        f.setSize(500, 400);
        f.setLocationRelativeTo(this);
        f.setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(8, 2, 10, 10));
        JTextField tfTen = new JTextField(nv.getTen());
        JTextField tfSdt = new JTextField(nv.getSdt());
        JTextField tfDiaChi = new JTextField(nv.getDiachi());
        JTextField tfNgaySinh = new JTextField(
                nv.getNgaysinh() != null ? new SimpleDateFormat("dd/MM/yyyy").format(nv.getNgaysinh()) : ""
        );
        JTextField tfViTri = new JTextField(nv.getVitri());
        JTextField tfEmail = new JTextField(tk != null ? tk.getEmail() : "");
        JPasswordField tfMatKhau = new JPasswordField(tk != null ? tk.getMatKhau() : "");
        JComboBox<String> cbVaiTro = new JComboBox<>(new String[]{"Nhân viên", "Quản lý"});
        if (tk != null) cbVaiTro.setSelectedItem(tk.getVaiTro());

        formPanel.add(new JLabel("Tên nhân viên:")); formPanel.add(tfTen);
        formPanel.add(new JLabel("Số điện thoại:")); formPanel.add(tfSdt);
        formPanel.add(new JLabel("Địa chỉ:")); formPanel.add(tfDiaChi);
        formPanel.add(new JLabel("Ngày sinh:")); formPanel.add(tfNgaySinh);
        formPanel.add(new JLabel("Vị trí:")); formPanel.add(tfViTri);
        formPanel.add(new JLabel("Email:")); formPanel.add(tfEmail);
        formPanel.add(new JLabel("Mật khẩu:")); formPanel.add(tfMatKhau);
        formPanel.add(new JLabel("Vai trò:")); formPanel.add(cbVaiTro);

        JButton btnLuu = new JButton("Cập nhật");
        btnLuu.addActionListener(e -> {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                nv.setTen(tfTen.getText());
                nv.setSdt(tfSdt.getText());
                nv.setDiachi(tfDiaChi.getText());
                nv.setNgaysinh(sdf.parse(tfNgaySinh.getText()));
                nv.setVitri(tfViTri.getText());

                if (tk != null) {
                    tk.setEmail(tfEmail.getText());
                    tk.setMatKhau(new String(tfMatKhau.getPassword()));
                    tk.setVaiTro((String) cbVaiTro.getSelectedItem());
                    boolean taiKhoanOk = taiKhoanDAO.capNhatTaiKhoan(tk);
                    boolean nhanVienOk = nhanVienDAO.capNhatNhanVien(nv);
                    if (taiKhoanOk && nhanVienOk) {
                        reloadTable();
                        f.dispose();
                        JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                    } else {
                        JOptionPane.showMessageDialog(f, "Cập nhật thất bại!");
                    }
                }
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