import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class teamGUI implements ActionListener{
    
    public teamGUI(){
        JFrame mainFrame = new JFrame();

        JPanel mainPanel = new JPanel();
        mainPanel.setBorder(BorderFactory.createEmptyBorder(300, 500, 300, 500));
        mainPanel.setLayout(new GridLayout(0, 1));
        
        JLabel tlabel = new JLabel("***Welcome to Night Owl Database***");

        //Configure the frame
        mainFrame.setTitle("Night Owl Database");
        mainFrame.setSize(700, 400);
        mainFrame.add(mainPanel, BorderLayout.CENTER);
        mainFrame.add(tlabel, BorderLayout.CENTER);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setVisible(true);
    }

    public static void main (String[] args){
        new teamGUI();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
    }
}
