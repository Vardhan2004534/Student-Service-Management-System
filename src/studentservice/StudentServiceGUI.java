package studentservice;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

public class StudentServiceGUI extends JFrame {
    private static final long serialVersionUID = 1L;

    private final StudentServiceSystem system;

    private final JTextField studentIdField;
    private final JTextField studentNameField;
    private final JTextField emailField;
    private final JTextField contactNumberField;

    private final JComboBox<String> requestTypeComboBox;
    private final JTextArea descriptionArea;
    private final JTextField requestIdField;
    private final JComboBox<String> statusUpdateComboBox;

    private final JTextField searchStudentIdField;
    private final JTextField searchRequestIdField;

    private final DefaultTableModel tableModel;
    private final JTable requestTable;

    public StudentServiceGUI() {
        super("Student Service Management System");
        this.system = new StudentServiceSystem();

        studentIdField = new JTextField(20);
        studentNameField = new JTextField(20);
        emailField = new JTextField(20);
        contactNumberField = new JTextField(20);

        requestTypeComboBox = new JComboBox<>(StudentServiceSystem.getValidRequestTypes().toArray(new String[0]));
        descriptionArea = new JTextArea(5, 30);
        requestIdField = new JTextField(20);
        requestIdField.setEditable(false);
        statusUpdateComboBox = new JComboBox<>(StudentServiceSystem.getValidStatuses().toArray(new String[0]));

        searchStudentIdField = new JTextField(18);
        searchRequestIdField = new JTextField(18);

        String[] columnNames = {"Request ID", "Student ID", "Student Name", "Request Type", "Description", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        requestTable = new JTable(tableModel);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(12, 12));

        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createMainContentPanel(), BorderLayout.CENTER);
        add(createFooterPanel(), BorderLayout.SOUTH);

        requestTable.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting() && requestTable.getSelectedRow() != -1) {
                int row = requestTable.getSelectedRow();
                String requestId = tableModel.getValueAt(row, 0).toString();
                ServiceRequest selected = system.searchByRequestId(requestId);
                if (selected != null) {
                    requestIdField.setText(selected.getRequestId());
                    statusUpdateComboBox.setSelectedItem(selected.getStatus());
                    requestTypeComboBox.setSelectedItem(selected.getRequestType());
                }
            }
        });

        refreshRequestTable();
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        panel.setBackground(new Color(230, 240, 255));

        JLabel titleLabel = new JLabel("Student Service Management System", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 28));

        JLabel subtitleLabel = new JLabel("Student Enquiry and Request Administration", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(new Color(230, 240, 255));
        content.add(titleLabel);
        content.add(subtitleLabel);
        panel.add(content, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createMainContentPanel() {
        JPanel panel = new JPanel(new BorderLayout(12, 12));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.add(createStudentPanel());
        formPanel.add(createRequestPanel());
        formPanel.add(createSearchPanel());
        formPanel.add(createActionPanel());

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("Submitted Requests"));
        JScrollPane scrollPane = new JScrollPane(requestTable);
        scrollPane.setPreferredSize(new Dimension(1100, 280));
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        panel.add(formPanel, BorderLayout.NORTH);
        panel.add(tablePanel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createStudentPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Student Details"));
        panel.setBackground(new Color(245, 248, 255));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        addLabeledField(panel, gbc, 0, 0, "Student ID:", studentIdField);
        addLabeledField(panel, gbc, 0, 1, "Student Name:", studentNameField);
        addLabeledField(panel, gbc, 0, 2, "Email Address:", emailField);
        addLabeledField(panel, gbc, 0, 3, "Contact Number:", contactNumberField);

        return panel;
    }

    private JPanel createRequestPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Request Details"));
        panel.setBackground(new Color(245, 248, 255));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel requestTypeLabel = new JLabel("Request Type:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(requestTypeLabel, gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        panel.add(requestTypeComboBox, gbc);

        JLabel descriptionLabel = new JLabel("Description:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(descriptionLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane descriptionScroll = new JScrollPane(descriptionArea);
        descriptionScroll.setPreferredSize(new Dimension(500, 90));
        panel.add(descriptionScroll, gbc);

        JLabel requestIdLabel = new JLabel("Generated Request ID:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        panel.add(requestIdLabel, gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        panel.add(requestIdField, gbc);

        JLabel statusLabel = new JLabel("Selected Status:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        panel.add(statusLabel, gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        panel.add(statusUpdateComboBox, gbc);

        return panel;
    }

    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Search Requests"));
        panel.setBackground(new Color(245, 248, 255));

        panel.add(new JLabel("Student ID:"));
        panel.add(searchStudentIdField);
        JButton searchStudentButton = new JButton("Search by Student ID");
        panel.add(searchStudentButton);

        panel.add(new JLabel("Request ID:"));
        panel.add(searchRequestIdField);
        JButton searchRequestButton = new JButton("Search by Request ID");
        panel.add(searchRequestButton);

        JButton showAllButton = new JButton("Display All Requests");
        panel.add(showAllButton);

        searchStudentButton.addActionListener(event -> searchByStudentId());
        searchRequestButton.addActionListener(event -> searchByRequestId());
        showAllButton.addActionListener(event -> refreshRequestTable());

        return panel;
    }

    private JPanel createActionPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 12));
        panel.setBorder(BorderFactory.createTitledBorder("Actions"));
        panel.setBackground(new Color(245, 248, 255));

        JButton submitButton = new JButton("Submit Request");
        JButton clearButton = new JButton("Clear Form");
        JButton updateStatusButton = new JButton("Update Status");

        panel.add(submitButton);
        panel.add(clearButton);
        panel.add(updateStatusButton);

        submitButton.addActionListener(event -> submitRequest());
        clearButton.addActionListener(event -> clearForm());
        updateStatusButton.addActionListener(event -> updateRequestStatus());

        return panel;
    }

    private JPanel createFooterPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
        JLabel footerLabel = new JLabel("In-memory records are kept while the application is running.");
        footerLabel.setForeground(new Color(80, 80, 80));
        panel.add(footerLabel);
        return panel;
    }

    private void addLabeledField(JPanel panel, GridBagConstraints gbc, int x, int y, String labelText, JTextField field) {
        JLabel label = new JLabel(labelText);
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = 1;
        panel.add(label, gbc);

        gbc.gridx = x + 1;
        gbc.gridwidth = 2;
        panel.add(field, gbc);
    }

    private void submitRequest() {
        try {
            String studentId = studentIdField.getText();
            String studentName = studentNameField.getText();
            String email = emailField.getText();
            String contactNumber = contactNumberField.getText();
            String requestType = (String) requestTypeComboBox.getSelectedItem();
            String description = descriptionArea.getText();

            ServiceRequest request = system.submitRequest(
                    studentId,
                    studentName,
                    email,
                    contactNumber,
                    requestType,
                    description);

            requestIdField.setText(request.getRequestId());
            statusUpdateComboBox.setSelectedItem(request.getStatus());
            JOptionPane.showMessageDialog(this,
                    "Request submitted successfully. Request ID: " + request.getRequestId(),
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);

            refreshRequestTable();
            clearForm(false);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validation Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void searchByStudentId() {
        try {
            List<ServiceRequest> results = system.searchByStudentId(searchStudentIdField.getText());
            if (results.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No matching request found for the supplied Student ID.",
                        "Search Result", JOptionPane.INFORMATION_MESSAGE);
                refreshRequestTable();
                return;
            }

            displayRequestList(results);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void searchByRequestId() {
        try {
            ServiceRequest request = system.searchByRequestId(searchRequestIdField.getText());
            if (request == null) {
                JOptionPane.showMessageDialog(this, "No matching request found for the supplied Request ID.",
                        "Search Result", JOptionPane.INFORMATION_MESSAGE);
                refreshRequestTable();
                return;
            }

            displayRequestList(List.of(request));
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void displayRequestList(List<ServiceRequest> requests) {
        tableModel.setRowCount(0);
        for (ServiceRequest request : requests) {
            tableModel.addRow(new Object[] {
                    request.getRequestId(),
                    request.getStudent().getStudentId(),
                    request.getStudent().getName(),
                    request.getRequestType(),
                    request.getDescription(),
                    request.getStatus()
            });
        }
    }

    private void refreshRequestTable() {
        tableModel.setRowCount(0);
        for (ServiceRequest request : system.getAllRequests()) {
            tableModel.addRow(new Object[] {
                    request.getRequestId(),
                    request.getStudent().getStudentId(),
                    request.getStudent().getName(),
                    request.getRequestType(),
                    request.getDescription(),
                    request.getStatus()
            });
        }
    }

    private void updateRequestStatus() {
        try {
            String requestId = requestIdField.getText().trim();
            if (requestId.isEmpty()) {
                int selectedRow = requestTable.getSelectedRow();
                if (selectedRow >= 0) {
                    requestId = tableModel.getValueAt(selectedRow, 0).toString();
                }
            }

            if (requestId.isEmpty()) {
                throw new IllegalArgumentException("Please select a request to update its status.");
            }

            String newStatus = (String) statusUpdateComboBox.getSelectedItem();
            system.updateRequestStatus(requestId, newStatus);
            requestIdField.setText(requestId);
            JOptionPane.showMessageDialog(this,
                    "Status updated successfully for request " + requestId + ".",
                    "Status Updated",
                    JOptionPane.INFORMATION_MESSAGE);
            refreshRequestTable();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Update Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearForm() {
        clearForm(true);
    }

    private void clearForm(boolean clearRequestId) {
        studentIdField.setText("");
        studentNameField.setText("");
        emailField.setText("");
        contactNumberField.setText("");
        requestTypeComboBox.setSelectedIndex(0);
        descriptionArea.setText("");
        if (clearRequestId) {
            requestIdField.setText("");
        }
        statusUpdateComboBox.setSelectedIndex(0);
        searchStudentIdField.setText("");
        searchRequestIdField.setText("");
    }
}
