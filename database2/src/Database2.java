import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Database2 extends JFrame {
    private JTextField txtID;
    private JTextField txtName;
    private JTextField txtMobile;
    private JRadioButton rbFemale;
    private JRadioButton rbMale;
    private JTextField txtAddress;
    private JTextField txtContact;
    private JCheckBox chkTerms;
    private JTable dataTable;
    private DefaultTableModel tableModel;
    private ButtonGroup genderGroup;

    public static final String DB_URL = "jdbc:mysql://localhost:3306/reg_form";
    public static final String USER = "admin";
    public static final String PASS = "1234";

    public Database2() {
        setTitle("Registration Form");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Create Form Panel with GridBagLayout
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtID = new JTextField(20);
        txtName = new JTextField(20);
        txtMobile = new JTextField(20);
        rbFemale = new JRadioButton("Female");
        rbMale = new JRadioButton("Male");
        genderGroup = new ButtonGroup();
        genderGroup.add(rbFemale);
        genderGroup.add(rbMale);

        txtAddress = new JTextField(20); // Address Text Field
        txtContact = new JTextField(20); // Contact Text Field

        // Adding components to the formPanel
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("ID"), gbc);
        gbc.gridx = 1; gbc.gridy = 0;
        formPanel.add(txtID, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Name"), gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        formPanel.add(txtName, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Gender"), gbc);
        gbc.gridx = 1; gbc.gridy = 2;
        JPanel genderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        genderPanel.add(rbFemale);
        genderPanel.add(rbMale);
        formPanel.add(genderPanel, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Address"), gbc);
        gbc.gridx = 1; gbc.gridy = 3;
        formPanel.add(txtAddress, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Contact"), gbc);
        gbc.gridx = 1; gbc.gridy = 4;
        formPanel.add(txtContact, gbc);

        // Create Buttons Panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton btnRegister = new JButton("Register");
        JButton btnExit = new JButton("Exit");
        buttonsPanel.add(btnRegister);
        buttonsPanel.add(btnExit);

        // Create Table for displaying data
        String[] columnNames = {"ID", "Name", "Gender", "Address", "Contact"};
        tableModel = new DefaultTableModel(columnNames, 0);
        dataTable = new JTable(tableModel);
        JScrollPane scrollTable = new JScrollPane(dataTable);

        // Add panels to the frame
        add(formPanel, BorderLayout.WEST);
        add(buttonsPanel, BorderLayout.SOUTH);
        add(scrollTable, BorderLayout.CENTER);

        // Load Data
        loadData();

        // Button Actions
        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                submitForm();
            }
        });

        btnExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }

    private void loadData() {
        try (Connection con = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM Employees2")) {

            // Clear existing data
            tableModel.setRowCount(0);

            while (rs.next()) {
                Object[] row = {
                    rs.getInt("ID"),
                    rs.getString("Name"),
                    rs.getString("Gender"),
                    rs.getString("Address"),
                    rs.getString("Contact")
                };
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void submitForm() {
        try (Connection con = DriverManager.getConnection(DB_URL, USER, PASS)) {
            String gender = rbFemale.isSelected() ? "Female" : "Male";

            String query = "INSERT INTO Employees2 (ID, Name, Gender, Address, Contact) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setInt(1, Integer.parseInt(txtID.getText()));
            pst.setString(2, txtName.getText());
            pst.setString(3, gender);
            pst.setString(4, txtAddress.getText());
            pst.setString(5, txtContact.getText());

            pst.executeUpdate();
            loadData();
            JOptionPane.showMessageDialog(this, "Record added successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void resetForm() {
        txtID.setText("");
        txtName.setText("");
        genderGroup.clearSelection();
        txtAddress.setText("");
        txtContact.setText("");
        chkTerms.setSelected(false);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Database2().setVisible(true);
            }
        });
    }
}
