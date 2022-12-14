import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Stonks implements Runnable {
    private JFrame frame = new JFrame("STONKS");
    private DefaultListModel<String> model = new DefaultListModel<>();
    private JList list = new JList(model);
    private JScrollPane scrollPane = new JScrollPane(list);
    private JPanel inputPanel = new JPanel();
    private JButton calculate = new JButton("Calculate");
    private JPanel oiPanel = new JPanel();
    private JLabel orgInvestmentLabel = new JLabel("Original Investment");
    private JTextField orgInvestmentText = new JTextField(16);
    private JPanel spPanel = new JPanel();
    private JLabel sharePriceLabel = new JLabel("Share Price");
    private JTextField sharePriceText = new JTextField(16);
    private JPanel ayPanel = new JPanel();
    private JLabel annualYieldLabel = new JLabel("Annual Yield (as %)");
    private JTextField annualYieldText = new JTextField(16);
    private JPanel yearsPanel = new JPanel();
    private JLabel yearsLabel = new JLabel("Years");
    private JTextField yearsText = new JTextField(16);
    private double orgInvestment;
    private double sharePrice;
    private double annualYield;
    private int years;
    private int[] stockNum;
    private double[] monthlyDiv;
    private double[] annualDiv;
    private double[] total;

    // action listener for buttons
    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == calculate) {
                try {
                    orgInvestment = Double.parseDouble(orgInvestmentText.getText());
                    sharePrice = Double.parseDouble(sharePriceText.getText());
                    annualYield = Double.parseDouble(annualYieldText.getText()) / 100;
                    years = Integer.parseInt(yearsText.getText());

                    // calculate and populate arrays
                    stockNum = new int[years];
                    monthlyDiv = new double[years];
                    annualDiv = new double[years];
                    total = new double[years];

                    for (int i = 0; i < years; i++) {
                        if (i == 0) {
                            stockNum[i] = (int) Math.floor(orgInvestment / sharePrice);
                        } else {
                            stockNum[i] = (int) Math.floor(annualDiv[i-1] / sharePrice) + stockNum[i-1];
                        }
                        total[i] = round(stockNum[i] * sharePrice, 2);
                        annualDiv[i] = round(total[i] * annualYield, 2);
                        monthlyDiv[i] = round(annualDiv[i] / 12, 2);

                    }

                    editModel();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null,
                        "Make sure all fields are formatted correctly!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    };

    public void editModel() {
        model.clear();
        for (int i = 0; i < years; i++) {
            model.addElement(String.format(
                "Year: %d\t|\t" +
                "Stocks: %d\t|\t" +
                "Monthly Dividend: %.2f\t|\t" +
                "Annual Dividend: %.2f\t|\t" +
                "Total: %.2f",
                (i + 1), stockNum[i], monthlyDiv[i],annualDiv[i], total[i]));
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Stonks());
    }

    public void run() {
        frame.setLayout(new BorderLayout());

        // input panel at top
        oiPanel.setLayout(new BoxLayout(oiPanel, BoxLayout.PAGE_AXIS));
        oiPanel.add(orgInvestmentLabel);
        oiPanel.add(orgInvestmentText);

        spPanel.setLayout(new BoxLayout(spPanel, BoxLayout.PAGE_AXIS));
        spPanel.add(sharePriceLabel);
        spPanel.add(sharePriceText);

        ayPanel.setLayout(new BoxLayout(ayPanel, BoxLayout.PAGE_AXIS));
        ayPanel.add(annualYieldLabel);
        ayPanel.add(annualYieldText);

        yearsPanel.setLayout(new BoxLayout(yearsPanel, BoxLayout.PAGE_AXIS));
        yearsPanel.add(yearsLabel);
        yearsPanel.add(yearsText);

        calculate.addActionListener(actionListener);

        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.LINE_AXIS));
        inputPanel.add(oiPanel);
        inputPanel.add(spPanel);
        inputPanel.add(ayPanel);
        inputPanel.add(yearsPanel);
        inputPanel.add(calculate);

        // add elements and configure frame
        frame.add(inputPanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.setSize(700, 500);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
}