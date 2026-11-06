package com.mycompany.cobamvc;

import com.mycompany.cobamvc.dao.MobilDAO;
import com.mycompany.cobamvc.model.Mobil;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class FormRentalMobil extends javax.swing.JFrame {

    private final MobilDAO mobilDAO = new MobilDAO();
    private DefaultTableModel tableModel;
    private int selectedId = -1;

    public FormRentalMobil() {
        initComponentsCustom();
        setTitle("Kelola Data Rental Mobil - Ali RPL (Hibernate ORM)");
        setLocationRelativeTo(null);
        loadTableData();
    }

    private void initComponentsCustom() {
        // Label Judul
        JLabel lblTitle = new JLabel("DATA RENTAL MOBIL");
        lblTitle.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 22));

        // Komponen Input
        JLabel lblKode = new JLabel("Kode Mobil:");
        txtKode = new JTextField();

        JLabel lblNama = new JLabel("Nama Mobil:");
        txtNama = new JTextField();

        JLabel lblMerk = new JLabel("Merk:");
        txtMerk = new JTextField();

        JLabel lblTarif = new JLabel("Tarif / Hari (Rp):");
        txtTarif = new JTextField();

        JLabel lblStatus = new JLabel("Status:");
        cbStatus = new JComboBox<>(new String[]{"Tersedia", "Disewa", "Perawatan"});

        // Tombol Aksi
        btnSimpan = new JButton("Simpan");
        btnUbah = new JButton("Ubah");
        btnHapus = new JButton("Hapus");
        btnReset = new JButton("Reset");
        btnLogout = new JButton("Logout");

        // Tabel Data
        tableModel = new DefaultTableModel(new String[]{"ID", "Kode", "Nama Mobil", "Merk", "Tarif (Rp)", "Status"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblMobil = new JTable(tableModel);
        JScrollPane scrollTable = new JScrollPane(tblMobil);

        // Event Klik Baris Tabel
        tblMobil.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = tblMobil.getSelectedRow();
                if (row >= 0) {
                    selectedId = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
                    txtKode.setText(tableModel.getValueAt(row, 1).toString());
                    txtNama.setText(tableModel.getValueAt(row, 2).toString());
                    txtMerk.setText(tableModel.getValueAt(row, 3).toString());
                    txtTarif.setText(tableModel.getValueAt(row, 4).toString());
                    cbStatus.setSelectedItem(tableModel.getValueAt(row, 5).toString());
                }
            }
        });

        // Event Tombol Simpan
        btnSimpan.addActionListener(e -> simpanData());

        // Event Tombol Ubah
        btnUbah.addActionListener(e -> ubahData());

        // Event Tombol Hapus
        btnHapus.addActionListener(e -> hapusData());

        // Event Tombol Reset
        btnReset.addActionListener(e -> resetForm());

        // Event Tombol Logout
        btnLogout.addActionListener(e -> {
            new NewJFrame().setVisible(true);
            this.dispose();
        });

        // Layout Manual Sederhana dan Rapi
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(750, 520);
        setLayout(null);

        lblTitle.setBounds(260, 15, 300, 30);
        add(lblTitle);

        // Baris Input Form
        lblKode.setBounds(30, 65, 120, 25);
        txtKode.setBounds(150, 65, 180, 25);
        add(lblKode);
        add(txtKode);

        lblNama.setBounds(30, 100, 120, 25);
        txtNama.setBounds(150, 100, 180, 25);
        add(lblNama);
        add(txtNama);

        lblMerk.setBounds(30, 135, 120, 25);
        txtMerk.setBounds(150, 135, 180, 25);
        add(lblMerk);
        add(txtMerk);

        lblTarif.setBounds(30, 170, 120, 25);
        txtTarif.setBounds(150, 170, 180, 25);
        add(lblTarif);
        add(txtTarif);

        lblStatus.setBounds(30, 205, 120, 25);
        cbStatus.setBounds(150, 205, 180, 25);
        add(lblStatus);
        add(cbStatus);

        // Baris Tombol
        btnSimpan.setBounds(30, 250, 85, 30);
        btnUbah.setBounds(125, 250, 85, 30);
        btnHapus.setBounds(220, 250, 85, 30);
        btnReset.setBounds(315, 250, 85, 30);
        btnLogout.setBounds(630, 20, 85, 30);
        add(btnSimpan);
        add(btnUbah);
        add(btnHapus);
        add(btnReset);
        add(btnLogout);

        // Posisi Tabel
        scrollTable.setBounds(30, 295, 680, 165);
        add(scrollTable);
    }

    private void loadTableData() {
        tableModel.setRowCount(0);
        List<Mobil> list = mobilDAO.getAll();
        if (list != null) {
            for (Mobil m : list) {
                tableModel.addRow(new Object[]{
                    m.getId(),
                    m.getKodeMobil(),
                    m.getNamaMobil(),
                    m.getMerk(),
                    m.getTarif(),
                    m.getStatus()
                });
            }
        }
    }

    private void simpanData() {
        String kode = txtKode.getText().trim();
        String nama = txtNama.getText().trim();
        String merk = txtMerk.getText().trim();
        String tarifStr = txtTarif.getText().trim();
        String status = cbStatus.getSelectedItem().toString();

        if (kode.isEmpty() || nama.isEmpty() || tarifStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Kode, Nama, dan Tarif wajib diisi!");
            return;
        }

        try {
            double tarif = Double.parseDouble(tarifStr);
            Mobil mobil = new Mobil(kode, nama, merk, tarif, status);
            if (mobilDAO.insert(mobil)) {
                JOptionPane.showMessageDialog(this, "Data Mobil berhasil disimpan via Hibernate ORM!");
                loadTableData();
                resetForm();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menyimpan data!");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Tarif harus berupa angka!");
        }
    }

    private void ubahData() {
        if (selectedId == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data mobil dari tabel terlebih dahulu!");
            return;
        }

        try {
            double tarif = Double.parseDouble(txtTarif.getText().trim());
            Mobil mobil = new Mobil(txtKode.getText().trim(), txtNama.getText().trim(), txtMerk.getText().trim(), tarif, cbStatus.getSelectedItem().toString());
            mobil.setId(selectedId);

            if (mobilDAO.update(mobil)) {
                JOptionPane.showMessageDialog(this, "Data Mobil berhasil diubah via Hibernate!");
                loadTableData();
                resetForm();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal mengubah data!");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Tarif harus berupa angka!");
        }
    }

    private void hapusData() {
        if (selectedId == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data yang akan dihapus dari tabel!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Yakin ingin menghapus mobil ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (mobilDAO.delete(selectedId)) {
                JOptionPane.showMessageDialog(this, "Data Mobil berhasil dihapus via Hibernate!");
                loadTableData();
                resetForm();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menghapus data!");
            }
        }
    }

    private void resetForm() {
        selectedId = -1;
        txtKode.setText("");
        txtNama.setText("");
        txtMerk.setText("");
        txtTarif.setText("");
        cbStatus.setSelectedIndex(0);
        tblMobil.clearSelection();
    }

    // Komponen GUI
    private JTextField txtKode, txtNama, txtMerk, txtTarif;
    private JComboBox<String> cbStatus;
    private JButton btnSimpan, btnUbah, btnHapus, btnReset, btnLogout;
    private JTable tblMobil;
}
