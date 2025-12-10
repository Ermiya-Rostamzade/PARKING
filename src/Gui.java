import javax.swing.*;
import java.awt.*;

public class Gui extends JFrame {

    private Parking parking;

    private JTextField numStacksField;
    private JTextField capacityField;
    private JTextField carIdField;
    private JTextField stackIndexField;
    private JTextField fromStackField;
    private JTextField toStackField;

    private JTextArea outputArea;

    private boolean initialized = false;

    public Gui() {
        setTitle("Parking Management");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 500);
        setLocationRelativeTo(null);

        initComponents();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel configPanel = new JPanel();
        configPanel.setBorder(BorderFactory.createTitledBorder("Parking config"));
        configPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        numStacksField = new JTextField("3", 5);
        capacityField = new JTextField("3", 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        configPanel.add(new JLabel("Number of stacks:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        configPanel.add(numStacksField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        configPanel.add(new JLabel("Capacity per stack:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        configPanel.add(capacityField, gbc);

        JButton initButton = new JButton("Initialize parking");
        initButton.addActionListener(e -> initParking());

        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridheight = 2;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.VERTICAL;
        configPanel.add(initButton, gbc);

        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JPanel actionsPanel = new JPanel();
        actionsPanel.setLayout(new BoxLayout(actionsPanel, BoxLayout.Y_AXIS));
        actionsPanel.setBorder(BorderFactory.createTitledBorder("Actions"));
        actionsPanel.setBorder(BorderFactory.createCompoundBorder(
                actionsPanel.getBorder(),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        carIdField = new JTextField(8);
        stackIndexField = new JTextField(4);
        fromStackField = new JTextField(4);
        toStackField = new JTextField(4);

        JPanel queuePanel = new JPanel();
        queuePanel.setLayout(new BoxLayout(queuePanel, BoxLayout.Y_AXIS));
        queuePanel.setBorder(BorderFactory.createTitledBorder("Queue & parking"));

        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row1.add(new JLabel("Car ID:"));
        row1.add(carIdField);
        JButton addCarButton = new JButton("Add car to queue");
        addCarButton.addActionListener(e -> addCarToQueue());
        row1.add(addCarButton);
        queuePanel.add(row1);

        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row2.add(new JLabel("Stack index (1..n):"));
        row2.add(stackIndexField);
        JButton parkSpecificButton = new JButton("Park in specific stack");
        parkSpecificButton.addActionListener(e -> parkInSpecificStack());
        row2.add(parkSpecificButton);
        queuePanel.add(row2);

        JPanel row3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton parkFirstButton = new JButton("Park first available");
        parkFirstButton.addActionListener(e -> parkFirstAvailable());
        row3.add(parkFirstButton);
        queuePanel.add(row3);

        JPanel carPanel = new JPanel();
        carPanel.setLayout(new BoxLayout(carPanel, BoxLayout.Y_AXIS));
        carPanel.setBorder(BorderFactory.createTitledBorder("Find / exit"));

        JPanel row4 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton findCarButton = new JButton("Find car");
        findCarButton.addActionListener(e -> findCar());
        row4.add(findCarButton);
        carPanel.add(row4);

        JPanel row5 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton exitCarButton = new JButton("Exit car");
        exitCarButton.addActionListener(e -> exitCar());
        row5.add(exitCarButton);
        carPanel.add(row5);

        JPanel transferPanel = new JPanel();
        transferPanel.setLayout(new BoxLayout(transferPanel, BoxLayout.Y_AXIS));
        transferPanel.setBorder(BorderFactory.createTitledBorder("Transfer / sort"));

        JPanel row6 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row6.add(new JLabel("From stack:"));
        row6.add(fromStackField);
        row6.add(new JLabel("To stack:"));
        row6.add(toStackField);
        JButton transferButton = new JButton("Transfer stacks");
        transferButton.addActionListener(e -> transferStacks());
        row6.add(transferButton);
        transferPanel.add(row6);

        JPanel row7 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row7.add(new JLabel("Stack index (for sort):"));
        row7.add(stackIndexField);
        JButton sortButton = new JButton("Sort stack");
        sortButton.addActionListener(e -> sortStack());
        row7.add(sortButton);
        transferPanel.add(row7);

        actionsPanel.add(queuePanel);
        actionsPanel.add(Box.createVerticalStrut(8));
        actionsPanel.add(carPanel);
        actionsPanel.add(Box.createVerticalStrut(8));
        actionsPanel.add(transferPanel);

        actionsPanel.setPreferredSize(new Dimension(380, 0));

        outputArea = new JTextArea(20, 40);
        outputArea.setEditable(false);
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);
        outputArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));

        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Log / status"));

        JButton showStatusButton = new JButton("Show parking status (console)");
        showStatusButton.addActionListener(e -> showStatus());

        JButton clearLogButton = new JButton("Clear log");
        clearLogButton.addActionListener(e -> outputArea.setText(""));

        JPanel rightTopPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightTopPanel.add(showStatusButton);
        rightTopPanel.add(clearLogButton);

        JPanel rightPanel = new JPanel(new BorderLayout(5, 5));
        rightPanel.add(rightTopPanel, BorderLayout.NORTH);
        rightPanel.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(configPanel, BorderLayout.NORTH);
        mainPanel.add(actionsPanel, BorderLayout.WEST);
        mainPanel.add(rightPanel, BorderLayout.CENTER);

        setContentPane(mainPanel);
    }


    private void initParking() {
        try {
            int n = Integer.parseInt(numStacksField.getText().trim());
            int c = Integer.parseInt(capacityField.getText().trim());
            parking = new Parking(n, c);
            initialized = true;
            log("Parking initialized with " + n + " stacks, capacity " + c);
        } catch (NumberFormatException ex) {
            showError("Please enter valid numbers for stacks and capacity.");
        }
    }

    private void ensureInitialized() {
        if (!initialized) {
            showError("Please initialize parking first.");
            throw new IllegalStateException("Parking not initialized");
        }
    }

    private void addCarToQueue() {
        try {
            ensureInitialized();
            int id = Integer.parseInt(carIdField.getText().trim());
            parking.addToQueue(new Car(id));
            log("Car " + id + " added to entry queue.");
        } catch (NumberFormatException ex) {
            showError("Please enter a valid Car ID.");
        } catch (IllegalStateException ignored) {}
    }

    private void parkFirstAvailable() {
        try {
            ensureInitialized();
            parking.ParkFirstAvaliable();
            log("Tried to park first available car from queue.");
        } catch (IllegalStateException ignored) {}
    }

    private void parkInSpecificStack() {
        try {
            ensureInitialized();
            int stack = Integer.parseInt(stackIndexField.getText().trim()) - 1;
            parking.parkSpecificStack(stack);
            log("Tried to park in stack " + (stack + 1));
        } catch (NumberFormatException ex) {
            showError("Please enter a valid stack index.");
        } catch (IllegalStateException ignored) {}
    }

    private void findCar() {
        try {
            ensureInitialized();
            int id = Integer.parseInt(carIdField.getText().trim());
            int[] loc = parking.findCar(id);
            if (loc != null && loc[0] != -1) {
                log("Car " + id + " is in Stack " + (loc[0]+1) + ", position " + (loc[1] ));
            } else {
                log("Car " + id + " not found.");
            }
        } catch (NumberFormatException ex) {
            showError("Please enter a valid Car ID.");
        } catch (IllegalStateException ignored) {}
    }

    private void exitCar() {
        try {
            ensureInitialized();
            int id = Integer.parseInt(carIdField.getText().trim());
            Car car = parking.exitCar(id);
            if (car != null) {
                log("Car " + id + " exited.");
            } else {
                log("Car " + id + " not found or could not exit.");
            }
        } catch (NumberFormatException ex) {
            showError("Please enter a valid Car ID.");
        } catch (IllegalStateException ignored) {}
    }

    private void transferStacks() {
        try {
            ensureInitialized();
            int from = Integer.parseInt(fromStackField.getText().trim()) - 1;
            int to = Integer.parseInt(toStackField.getText().trim()) - 1;
            parking.transferStacks(from, to);
            log("Transferred cars from stack " + (from + 1) + " to stack " + (to + 1));
        } catch (NumberFormatException ex) {
            showError("Please enter valid stack indexes.");
        } catch (IllegalStateException ignored) {}
    }

    private void sortStack() {
        try {
            ensureInitialized();
            int s = Integer.parseInt(stackIndexField.getText().trim()) - 1;
            parking.sortStack(s);
            log("Sorted stack " + (s + 1));
        } catch (NumberFormatException ex) {
            showError("Please enter a valid stack index.");
        } catch (IllegalStateException ignored) {}
    }

    private void showStatus() {
        try {
            ensureInitialized();
            parking.showStacksStatus();
            log("Parking status printed to console.");
        } catch (IllegalStateException ignored) {}
    }

    private void log(String msg) {
        outputArea.append(msg + "\n");
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error",
                JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Gui gui = new Gui();
            gui.setVisible(true);
        });
    }
}
