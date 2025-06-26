package Client;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import Server.CabBookingInterface;
import java.rmi.Naming;

public class CabBookingGUI extends JFrame {
    private static final String SERVICE_URL = "rmi://localhost:2001/CabService";
    private CabBookingInterface booking;
    private JPanel mainPanel;
    private JLabel statusLabel;

    
    private JTextField nameField, sourceField, destField, locationField, bookingIdField, customerField;
    private JTextArea resultArea, statusArea;
    private JButton bookBtn, availableBtn, cancelBtn, historyBtn, addCabBtn, statusBtn, clearBtn;
    private JTabbedPane tabbedPane;
    
    public CabBookingGUI() {
        initializeGUI();
        connectToService();
    }
    
    private void initializeGUI() {
        setTitle("🚕 Enhanced Cab Booking System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null);
        
        
        tabbedPane = new JTabbedPane();
        
        
        tabbedPane.addTab("🚖 Book Cab", createBookingPanel());
        tabbedPane.addTab("📍 Available Cabs", createAvailablePanel());
        tabbedPane.addTab("❌ Cancel Booking", createCancelPanel());
        tabbedPane.addTab("📋 History", createHistoryPanel());
        tabbedPane.addTab("⚙️ Admin", createAdminPanel());
        
        add(tabbedPane, BorderLayout.CENTER);
        
        
        JPanel statusBar = createStatusBar();
        add(statusBar, BorderLayout.SOUTH);
        
        
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    private void styleButton(JButton button, Color backgroundColor) {
        button.setBackground(backgroundColor);
        button.setForeground(Color.BLACK);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setOpaque(true);
        button.setBorderPainted(true);
        button.setFocusPainted(false);
    }
    
    private JPanel createBookingPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(new TitledBorder("📝 Booking Details"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        
        gbc.gridx = 0; gbc.gridy = 0;
        inputPanel.add(new JLabel("👤 Customer Name:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        nameField = new JTextField(20);
        inputPanel.add(nameField, gbc);
        
        
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE;
        inputPanel.add(new JLabel("📍 Source Location:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        sourceField = new JTextField(20);
        inputPanel.add(sourceField, gbc);
        
        
        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE;
        inputPanel.add(new JLabel("🎯 Destination:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        destField = new JTextField(20);
        inputPanel.add(destField, gbc);
        
        
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.NONE;
        bookBtn = new JButton("🚖 Book Cab Now");
        bookBtn.setPreferredSize(new Dimension(200, 40));
        styleButton(bookBtn, new Color(76, 175, 80));
        bookBtn.addActionListener(new BookCabListener());
        inputPanel.add(bookBtn, gbc);
        
        panel.add(inputPanel, BorderLayout.NORTH);
        
        
        JPanel resultPanel = new JPanel(new BorderLayout());
        resultPanel.setBorder(new TitledBorder("📋 Booking Result"));
        resultArea = new JTextArea(10, 50);
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        resultArea.setBackground(new Color(248, 249, 250));
        JScrollPane scrollPane = new JScrollPane(resultArea);
        resultPanel.add(scrollPane, BorderLayout.CENTER);
        
        
        clearBtn = new JButton("🗑️ Clear Results");
        styleButton(clearBtn, Color.LIGHT_GRAY);
        clearBtn.addActionListener(e -> resultArea.setText(""));
        resultPanel.add(clearBtn, BorderLayout.SOUTH);
        
        panel.add(resultPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createAvailablePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        
        JPanel inputPanel = new JPanel(new FlowLayout());
        inputPanel.setBorder(new TitledBorder("🔍 Search Available Cabs"));
        
        inputPanel.add(new JLabel("📍 Location:"));
        locationField = new JTextField(20);
        inputPanel.add(locationField);
        
        availableBtn = new JButton("🔍 Search Cabs");
        styleButton(availableBtn, new Color(33, 150, 243));
        availableBtn.addActionListener(new AvailableCabsListener());
        inputPanel.add(availableBtn);
        
        panel.add(inputPanel, BorderLayout.NORTH);
        
        
        JTextArea availableArea = new JTextArea(15, 50);
        availableArea.setEditable(false);
        availableArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        availableArea.setBackground(new Color(248, 249, 250));
        JScrollPane scrollPane = new JScrollPane(availableArea);
        scrollPane.setBorder(new TitledBorder("🚕 Available Cabs"));
        panel.add(scrollPane, BorderLayout.CENTER);
        
        
        availableBtn.putClientProperty("resultArea", availableArea);
        
        return panel;
    }
    
    private JPanel createCancelPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        
        JPanel inputPanel = new JPanel(new FlowLayout());
        inputPanel.setBorder(new TitledBorder("❌ Cancel Booking"));
        
        inputPanel.add(new JLabel("🔢 Booking ID:"));
        bookingIdField = new JTextField(15);
        inputPanel.add(bookingIdField);
        
        cancelBtn = new JButton("❌ Cancel Booking");
        styleButton(cancelBtn, new Color(244, 67, 54));
        cancelBtn.addActionListener(new CancelBookingListener());
        inputPanel.add(cancelBtn);
        
        panel.add(inputPanel, BorderLayout.NORTH);
        
        
        JTextArea cancelArea = new JTextArea(15, 50);
        cancelArea.setEditable(false);
        cancelArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        cancelArea.setBackground(new Color(248, 249, 250));
        JScrollPane scrollPane = new JScrollPane(cancelArea);
        scrollPane.setBorder(new TitledBorder("📋 Cancellation Result"));
        panel.add(scrollPane, BorderLayout.CENTER);
        
        
        cancelBtn.putClientProperty("resultArea", cancelArea);
        
        return panel;
    }
    
    private JPanel createHistoryPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        
        JPanel inputPanel = new JPanel(new FlowLayout());
        inputPanel.setBorder(new TitledBorder("📋 Booking History"));
        
        inputPanel.add(new JLabel("👤 Customer Name:"));
        customerField = new JTextField(20);
        inputPanel.add(customerField);
        
        historyBtn = new JButton("📋 Get History");
        styleButton(historyBtn, new Color(156, 39, 176));
        historyBtn.addActionListener(new BookingHistoryListener());
        inputPanel.add(historyBtn);
        
        panel.add(inputPanel, BorderLayout.NORTH);
        
        
        JTextArea historyArea = new JTextArea(15, 50);
        historyArea.setEditable(false);
        historyArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        historyArea.setBackground(new Color(248, 249, 250));
        JScrollPane scrollPane = new JScrollPane(historyArea);
        scrollPane.setBorder(new TitledBorder("📋 Booking History"));
        panel.add(scrollPane, BorderLayout.CENTER);
        
        
        historyBtn.putClientProperty("resultArea", historyArea);
        
        return panel;
    }
    
    private JPanel createAdminPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        
        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        buttonPanel.setBorder(new TitledBorder("⚙️ Admin Operations"));
        
        
        addCabBtn = new JButton("➕ Add New Cab");
        styleButton(addCabBtn, new Color(76, 175, 80));
        addCabBtn.addActionListener(new AddCabListener());
        buttonPanel.add(addCabBtn);
        
        
        statusBtn = new JButton("📊 View Cab Status");
        styleButton(statusBtn, new Color(255, 152, 0));
        statusBtn.addActionListener(new CabStatusListener());
        buttonPanel.add(statusBtn);
        
        
        JButton refreshBtn = new JButton("🔄 Refresh Connection");
        styleButton(refreshBtn, new Color(96, 125, 139));
        refreshBtn.addActionListener(e -> connectToService());
        buttonPanel.add(refreshBtn);
        
        
        JButton clearAdminBtn = new JButton("🗑️ Clear Results");
        styleButton(clearAdminBtn, Color.LIGHT_GRAY);
        clearAdminBtn.addActionListener(e -> {
            if (statusArea != null) {
                statusArea.setText("");
            }
        });
        buttonPanel.add(clearAdminBtn);
        
        panel.add(buttonPanel, BorderLayout.NORTH);
        
        
        statusArea = new JTextArea(15, 50);
        statusArea.setEditable(false);
        statusArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        statusArea.setBackground(new Color(248, 249, 250));
        JScrollPane scrollPane = new JScrollPane(statusArea);
        scrollPane.setBorder(new TitledBorder("📊 System Status"));
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createStatusBar() {
        JPanel statusBar = new JPanel(new BorderLayout());
        statusBar.setBorder(BorderFactory.createLoweredBevelBorder());
        statusBar.setPreferredSize(new Dimension(-1, 25));
        
        statusLabel = new JLabel("🔄 Connecting to service...");
        statusLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        statusBar.add(statusLabel, BorderLayout.WEST);
        
        JLabel timeLabel = new JLabel(java.time.LocalDateTime.now().toString());
        timeLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
        statusBar.add(timeLabel, BorderLayout.EAST);
        
        
        Timer timer = new Timer(1000, e -> timeLabel.setText(java.time.LocalDateTime.now().toString()));
        timer.start();
        
        return statusBar;
    }
    
    private void connectToService() {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                booking = (CabBookingInterface) Naming.lookup(SERVICE_URL);
                return null;
            }
            
            @Override
            protected void done() {
                try {
                    get();
                    statusLabel.setText("✅ Connected to Cab Service");
                    statusLabel.setForeground(new Color(76, 175, 80));
                } catch (Exception e) {
                    statusLabel.setText("❌ Connection failed: " + e.getMessage());
                    statusLabel.setForeground(Color.RED);
                    JOptionPane.showMessageDialog(CabBookingGUI.this, 
                        "Failed to connect to cab service!\nMake sure the server is running on port 2001.", 
                        "Connection Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    }
    
    
    private class BookCabListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (booking == null) {
                JOptionPane.showMessageDialog(CabBookingGUI.this, "Not connected to service!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            String name = nameField.getText().trim();
            String source = sourceField.getText().trim();
            String dest = destField.getText().trim();
            
            if (name.isEmpty() || source.isEmpty() || dest.isEmpty()) {
                JOptionPane.showMessageDialog(CabBookingGUI.this, "Please fill all fields!", "Input Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {
                @Override
                protected String doInBackground() throws Exception {
                    return booking.bookCab(name, source, dest);
                }
                
                @Override
                protected void done() {
                    try {
                        String result = get();
                        resultArea.setText(result);
                        if (result.contains("✅")) {
                            
                            nameField.setText("");
                            sourceField.setText("");
                            destField.setText("");
                        }
                    } catch (Exception ex) {
                        resultArea.setText("❌ Error: " + ex.getMessage());
                    }
                }
            };
            worker.execute();
        }
    }
    
    private class AvailableCabsListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (booking == null) {
                JOptionPane.showMessageDialog(CabBookingGUI.this, "Not connected to service!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            String location = locationField.getText().trim();
            if (location.isEmpty()) {
                JOptionPane.showMessageDialog(CabBookingGUI.this, "Please enter a location!", "Input Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            JTextArea resultArea = (JTextArea) availableBtn.getClientProperty("resultArea");
            
            SwingWorker<List<String>, Void> worker = new SwingWorker<List<String>, Void>() {
                @Override
                protected List<String> doInBackground() throws Exception {
                    return booking.getAvailableCabs(location);
                }
                
                @Override
                protected void done() {
                    try {
                        List<String> cabs = get();
                        StringBuilder sb = new StringBuilder();
                        sb.append("🚕 Available Cabs at ").append(location).append(":\n\n");
                        
                        if (cabs.isEmpty()) {
                            sb.append("❌ No cabs available at this location.");
                        } else {
                            for (int i = 0; i < cabs.size(); i++) {
                                sb.append(String.format("%d. %s\n", i + 1, cabs.get(i)));
                            }
                        }
                        
                        resultArea.setText(sb.toString());
                    } catch (Exception ex) {
                        resultArea.setText("❌ Error: " + ex.getMessage());
                    }
                }
            };
            worker.execute();
        }
    }
    
    private class CancelBookingListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (booking == null) {
                JOptionPane.showMessageDialog(CabBookingGUI.this, "Not connected to service!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            String bookingIdStr = bookingIdField.getText().trim();
            if (bookingIdStr.isEmpty()) {
                JOptionPane.showMessageDialog(CabBookingGUI.this, "Please enter booking ID!", "Input Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            try {
                int bookingId = Integer.parseInt(bookingIdStr);
                JTextArea resultArea = (JTextArea) cancelBtn.getClientProperty("resultArea");
                
                SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {
                    @Override
                    protected String doInBackground() throws Exception {
                        return booking.cancelBooking(bookingId);
                    }
                    
                    @Override
                    protected void done() {
                        try {
                            String result = get();
                            resultArea.setText(result);
                            if (result.contains("✅")) {
                                bookingIdField.setText("");
                            }
                        } catch (Exception ex) {
                            resultArea.setText("❌ Error: " + ex.getMessage());
                        }
                    }
                };
                worker.execute();
                
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(CabBookingGUI.this, "Invalid booking ID format!", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private class BookingHistoryListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (booking == null) {
                JOptionPane.showMessageDialog(CabBookingGUI.this, "Not connected to service!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            String customer = customerField.getText().trim();
            if (customer.isEmpty()) {
                JOptionPane.showMessageDialog(CabBookingGUI.this, "Please enter customer name!", "Input Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            JTextArea resultArea = (JTextArea) historyBtn.getClientProperty("resultArea");
            
            SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {
                @Override
                protected String doInBackground() throws Exception {
                    return booking.getBookingHistory(customer);
                }
                
                @Override
                protected void done() {
                    try {
                        String result = get();
                        resultArea.setText(result);
                    } catch (Exception ex) {
                        resultArea.setText("❌ Error: " + ex.getMessage());
                    }
                }
            };
            worker.execute();
        }
    }
    
    private class AddCabListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (booking == null) {
                JOptionPane.showMessageDialog(CabBookingGUI.this, "Not connected to service!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            String location = JOptionPane.showInputDialog(CabBookingGUI.this, "Enter cab location:", "Add New Cab", JOptionPane.QUESTION_MESSAGE);
            if (location == null || location.trim().isEmpty()) {
                return;
            }
            
            SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {
                @Override
                protected String doInBackground() throws Exception {
                    return booking.addCab(location.trim());
                }
                
                @Override
                protected void done() {
                    try {
                        String result = get();
                        statusArea.append(result + "\n\n");
                        statusArea.setCaretPosition(statusArea.getDocument().getLength());
                    } catch (Exception ex) {
                        statusArea.append("❌ Error adding cab: " + ex.getMessage() + "\n\n");
                    }
                }
            };
            worker.execute();
        }
    }
    
    private class CabStatusListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (booking == null) {
                JOptionPane.showMessageDialog(CabBookingGUI.this, "Not connected to service!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {
                @Override
                protected String doInBackground() throws Exception {
                    return booking.getCabStatus();
                }
                
                @Override
                protected void done() {
                    try {
                        String result = get();
                        statusArea.setText(result);
                    } catch (Exception ex) {
                        statusArea.setText("❌ Error: " + ex.getMessage());
                    }
                }
            };
            worker.execute();
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new CabBookingGUI().setVisible(true);
        });
    }
}
