package com.sap.employee_management.employee;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import java.util.List;

public class App {

    static SessionFactory factory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();

    public static void main(String[] args) {
        // Create the main frame
        JFrame frame = new JFrame("Employee Management System");
        frame.setSize(900, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        // Set the overall layout for the frame
        frame.setLayout(new BorderLayout(10, 10));

        // Add a panel with buttons for operations
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.setBackground(Color.LIGHT_GRAY);

        JButton addButton = createButton("Add Employee");
        JButton readButton = createButton("View Employees");
        JButton updateButton = createButton("Update Employee");
        JButton deleteButton = createButton("Delete Employee");

        buttonPanel.add(addButton);
        buttonPanel.add(readButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);

        // Add the panel to the frame
        frame.add(buttonPanel, BorderLayout.NORTH);

        // Create JTable to display employee data
        JTable table = new JTable();
        JScrollPane scrollPane = new JScrollPane(table);
        table.setFillsViewportHeight(true);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Button Actions
        addButton.addActionListener(e -> openAddEmployeeDialog());
        readButton.addActionListener(e -> loadEmployeesIntoTable(table));
        updateButton.addActionListener(e -> openUpdateEmployeeDialog(table));
        deleteButton.addActionListener(e -> deleteSelectedEmployee(table));

        // Show the frame
        frame.setVisible(true);
    }

    // Helper method to create buttons with styling
    private static JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(new Color(33, 150, 243));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(150, 40));
        return button;
    }

    // Add Employee Dialog
    private static void openAddEmployeeDialog() {
        JDialog dialog = new JDialog();
        dialog.setTitle("Add Employee");
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(7, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panel.add(new JLabel("Name:"));
        JTextField nameField = new JTextField();
        panel.add(nameField);

        panel.add(new JLabel("Salary:"));
        JTextField salaryField = new JTextField();
        panel.add(salaryField);

        panel.add(new JLabel("Phone:"));
        JTextField phoneField = new JTextField();
        panel.add(phoneField);

        panel.add(new JLabel("Email:"));
        JTextField emailField = new JTextField();
        panel.add(emailField);

        panel.add(new JLabel("Type:"));
        JTextField typeField = new JTextField();
        panel.add(typeField);

        JButton saveButton = new JButton("Save");
        saveButton.setBackground(new Color(76, 175, 80));
        saveButton.setForeground(Color.WHITE);
        panel.add(saveButton);
        panel.add(new JLabel());

        dialog.add(panel);
        dialog.setVisible(true);

        saveButton.addActionListener(e -> {
            // Hibernate code to save the employee
            Session session = factory.openSession();
            Transaction transaction = session.beginTransaction();
            Employee employee = new Employee();
            employee.setEmp_name(nameField.getText());
            employee.setSalary(Double.parseDouble(salaryField.getText()));
            employee.setPhone(Long.parseLong(phoneField.getText()));
            employee.setEmail(emailField.getText());
            employee.setType(typeField.getText());
            session.persist(employee);
            transaction.commit();
            session.close();
            dialog.dispose();
            JOptionPane.showMessageDialog(null, "Employee Added Successfully!");
        });
    }

    // Load Employees into JTable
    private static void loadEmployeesIntoTable(JTable table) {
        Session session = factory.openSession();
        List<Employee> employees = session.createQuery("FROM Employee", Employee.class).list();
        session.close();

        String[] columnNames = {"ID", "Name", "Salary", "Phone", "Email", "Type"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        for (Employee emp : employees) {
            model.addRow(new Object[]{emp.getEmp_id(), emp.getEmp_name(), emp.getSalary(), emp.getPhone(), emp.getEmail(), emp.getType()});
        }

        table.setModel(model);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setRowHeight(30);
        table.setGridColor(Color.GRAY);
        table.setSelectionBackground(new Color(33, 150, 243));
    }

    // Update Employee Dialog
    private static void openUpdateEmployeeDialog(JTable table) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Select an Employee to Update!");
            return;
        }

        int id = (int) table.getValueAt(selectedRow, 0);

        JDialog dialog = new JDialog();
        dialog.setTitle("Update Employee");
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(7, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panel.add(new JLabel("Name:"));
        JTextField nameField = new JTextField((String) table.getValueAt(selectedRow, 1));
        panel.add(nameField);

        panel.add(new JLabel("Salary:"));
        JTextField salaryField = new JTextField(String.valueOf(table.getValueAt(selectedRow, 2)));
        panel.add(salaryField);

        panel.add(new JLabel("Phone:"));
        JTextField phoneField = new JTextField(String.valueOf(table.getValueAt(selectedRow, 3)));
        panel.add(phoneField);

        panel.add(new JLabel("Email:"));
        JTextField emailField = new JTextField((String) table.getValueAt(selectedRow, 4));
        panel.add(emailField);

        panel.add(new JLabel("Type:"));
        JTextField typeField = new JTextField((String) table.getValueAt(selectedRow, 5));
        panel.add(typeField);

        JButton updateButton = new JButton("Update");
        updateButton.setBackground(new Color(76, 175, 80));
        updateButton.setForeground(Color.WHITE);
        panel.add(updateButton);
        panel.add(new JLabel());

        dialog.add(panel);
        dialog.setVisible(true);

        updateButton.addActionListener(e -> {
            Session session = factory.openSession();
            Transaction transaction = session.beginTransaction();
            Employee employee = session.get(Employee.class, id);
            employee.setEmp_name(nameField.getText());
            employee.setSalary(Double.parseDouble(salaryField.getText()));
            employee.setPhone(Long.parseLong(phoneField.getText()));
            employee.setEmail(emailField.getText());
            employee.setType(typeField.getText());
            session.update(employee);
            transaction.commit();
            session.close();
            dialog.dispose();
            JOptionPane.showMessageDialog(null, "Employee Updated Successfully!");
        });
    }

    // Delete Employee
    private static void deleteSelectedEmployee(JTable table) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Select an Employee to Delete!");
            return;
        }

        int id = (int) table.getValueAt(selectedRow, 0);

        Session session = factory.openSession();
        Transaction transaction = session.beginTransaction();
        Employee employee = session.get(Employee.class, id);
        if (employee != null) {
            session.remove(employee);
            transaction.commit();
            JOptionPane.showMessageDialog(null, "Employee Deleted Successfully!");
        }
        session.close();
    }
}
