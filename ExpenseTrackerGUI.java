import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ExpenseTrackerGUI {
    private double budget = 0.0;
    private double totalExpenses = 0.0;

    // Store expenses as objects
    private final ArrayList<Expense> expenseList = new ArrayList<>();

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ExpenseTrackerGUI::new);
    }

    public ExpenseTrackerGUI() {
        // Create the main frame
        JFrame frame = new JFrame("Expense Tracker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        frame.setLayout(new GridLayout(8, 1));

        // Budget Label and Field
        JLabel budgetLabel = new JLabel("Budget: $0.0");
        frame.add(budgetLabel);

        // Expense Label
        JLabel expenseLabel = new JLabel("Total Expenses: $0.0");
        frame.add(expenseLabel);

        // Add Expense Panel
        JPanel addExpensePanel = new JPanel(new GridLayout(1, 2));
        JButton setBudgetButton = new JButton("Set Budget");
        addExpensePanel.add(setBudgetButton);
        JButton addExpenseButton = new JButton("Add Expense");
        addExpensePanel.add(addExpenseButton);
        frame.add(addExpensePanel);

        // Category Selector
        JLabel categoryLabel = new JLabel("Select Expense Category:");
        frame.add(categoryLabel);
        String[] categories = {"Food", "Transportation", "Entertainment", "Bills", "Others"};
        JComboBox<String> categoryComboBox = new JComboBox<>(categories);
        frame.add(categoryComboBox);

        // Date Selector
        JLabel dateLabel = new JLabel("Select Date:");
        frame.add(dateLabel);
        JSpinner dateSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd");
        dateSpinner.setEditor(dateEditor);
        frame.add(dateSpinner);

        // Check Budget Status Button
        JButton checkStatusButton = new JButton("Check Budget Status");
        frame.add(checkStatusButton);

        // View Expenses by Category Button
        JButton viewCategoryButton = new JButton("View Expenses by Category");
        frame.add(viewCategoryButton);

        // Exit Button
        JButton exitButton = new JButton("Exit");
        frame.add(exitButton);

        // Action Listeners
        setBudgetButton.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(frame, "Enter budget amount:");
            try {
                budget = Double.parseDouble(input);
                if (budget < 0) {
                    JOptionPane.showMessageDialog(frame, "Budget cannot be negative.", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    budgetLabel.setText("Budget: $" + budget);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid input. Please enter a number.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        addExpenseButton.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(frame, "Enter expense amount:");
            try {
                double expense = Double.parseDouble(input);
                if (expense < 0) {
                    JOptionPane.showMessageDialog(frame, "Expense cannot be negative.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String category = (String) categoryComboBox.getSelectedItem();
                Date date = (Date) dateSpinner.getValue();

                totalExpenses += expense;
                expenseList.add(new Expense(expense, category, date));
                expenseLabel.setText("Total Expenses: $" + totalExpenses);

                // Check if the user has exceeded the budget
                if (totalExpenses > budget) {
                    JOptionPane.showMessageDialog(frame, "Warning: You have exceeded your budget!", "Alert", JOptionPane.WARNING_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid input. Please enter a number.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        checkStatusButton.addActionListener(e -> {
            if (totalExpenses > budget) {
                JOptionPane.showMessageDialog(frame, "You are over budget by: $" + (totalExpenses - budget), "Status", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(frame, "You are within budget. Remaining: $" + (budget - totalExpenses), "Status", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        viewCategoryButton.addActionListener(e -> {
            // Group expenses by category
            Map<String, Double> categoryTotals = new HashMap<>();
            for (Expense expense : expenseList) {
                categoryTotals.put(expense.getCategory(),
                        categoryTotals.getOrDefault(expense.getCategory(), 0.0) + expense.getAmount());
            }

            // Build a string to display the expenses by category
            StringBuilder message = new StringBuilder("Expenses by Category:\n");
            for (Map.Entry<String, Double> entry : categoryTotals.entrySet()) {
                message.append(entry.getKey()).append(": $").append(entry.getValue()).append("\n");
            }

            // Show the message in a dialog
            JOptionPane.showMessageDialog(frame, message.toString(), "Expenses by Category", JOptionPane.INFORMATION_MESSAGE);
        });

        exitButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(frame, "Are you sure you want to exit?", "Confirm Exit", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        // Display the frame
        frame.setVisible(true);
    }

    // Expense class to store details
    static class Expense {
        private final double amount;
        private final String category;
        private final Date date;

        public Expense(double amount, String category, Date date) {
            this.amount = amount;
            this.category = category;
            this.date = date;
        }

        public double getAmount() {
            return amount;
        }

        public String getCategory() {
            return category;
        }

        public Date getDate() {
            return date;
        }

        @Override
        public String toString() {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            return "Amount: $" + amount + ", Category: " + category + ", Date: " + sdf.format(date);
        }
    }
}
