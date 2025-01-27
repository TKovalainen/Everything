import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;


public class Historia extends JFrame{
    JTextArea textarea;
    Font myFont = new Font("Arial", Font.BOLD, 20);
    JButton close;
    JButton deleteButton;

    public Historia(ArrayList<String> calculation) { //creates the history GUI
        JFrame frame = new JFrame("Calculation History");
        frame.setSize(400, 700);
        frame.setVisible(true);
        frame.setLayout(new BorderLayout());

        textarea = new JTextArea();
        textarea.setFont(myFont);
        textarea.setCaretColor(Color.WHITE);
        textarea.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        JScrollPane scroll = new JScrollPane(textarea);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        textarea.setEditable(false);
        frame.add(scroll, BorderLayout.CENTER);

        JPanel closePanel = new JPanel();
        close = new JButton("Close");
        close.addActionListener(new ActionListener() { //closes the history
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });
        closePanel.add(close);
        frame.add(closePanel, BorderLayout.SOUTH);


        JPanel buttonPanel = new JPanel();
        deleteButton = new JButton("Delete History");
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { //deletes the history with confirmation/Informs the user about empty history
                if(calculation.isEmpty()){
                    JOptionPane.showMessageDialog(null, "History is empty.", "Error", JOptionPane.INFORMATION_MESSAGE);
                }else{
                    int dialogResult = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete the history?", "Confirmation", JOptionPane.YES_NO_OPTION);
                    if (dialogResult == JOptionPane.YES_OPTION) {
                        calculation.clear();
                        textarea.setText("");
                    }                    
                }
            }
        });
        buttonPanel.add(deleteButton);
        frame.add(closePanel, BorderLayout.NORTH);
        frame.add(buttonPanel, BorderLayout.SOUTH);
        if(calculation.isEmpty()){
            textarea.setText("\t\t\t\t\t\t\t\t\t\t.History is empty");
        }else{
            for (String calc : calculation) {
                textarea.append(""+calc+"\n_________________________________\n");
            }            
        }

    }

}
