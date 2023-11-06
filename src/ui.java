package src;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ui  {
    
    public static void main(String[] args) {
        
        JFrame frame = new JFrame("FASAL Fusion");
        frame.setSize(1500, 250); 
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        JLabel label = new JLabel("FASAL Fusion");
        label.setFont(new Font("Arial", Font.BOLD, 40));
        panel.add(label, BorderLayout.NORTH);
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout());
        String[] fieldLabels = {"Nitrogen", "Phosphorous", "Potassium", "Temperature", "humidity", "pH", "Rainfall"};
        JLabel[] labels = new JLabel[fieldLabels.length];
        JTextField[] textFields = new JTextField[fieldLabels.length];
        
        for (int i = 0; i < fieldLabels.length; i++) {
            labels[i] = new JLabel(fieldLabels[i]);
            textFields[i] = new JTextField(10);
            inputPanel.add(labels[i]);
            inputPanel.add(textFields[i]);

            // Add an empty border to the text fields to create space
            textFields[i].setBorder(BorderFactory.createCompoundBorder(
                    textFields[i].getBorder(),
                    BorderFactory.createEmptyBorder(0, 0, 10, 0)  // Adjust the spacing as needed
            ));
        }
        //Test_Final.initalize();

        JButton analyzeButton = new JButton("Analyse");
        inputPanel.add(analyzeButton);
        panel.add(inputPanel, BorderLayout.CENTER);
        frame.getContentPane().add(panel);
        frame.setVisible(true);

        analyzeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    double[] values = new double[fieldLabels.length];
                    for (int i = 0; i < fieldLabels.length; i++) {
                        values[i] = Double.parseDouble(textFields[i].getText());
                    }
                    String result = loadModel.middleware(values[0], values[1], values[2], values[3], values[4], values[5], values[6]);
                    System.out.println(result); 
                    // Show result in a popup dialog
                    JOptionPane.showMessageDialog(frame, "Recommended Crop: \n" + result);
                } catch (NumberFormatException ex) {
                    System.out.println("Please enter valid numeric values in all fields.");
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        });
    }
}
