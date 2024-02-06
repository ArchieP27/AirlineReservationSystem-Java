import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

public class changeFlight extends JFrame implements ActionListener {

    JLabel fno, updatedfno;
    JTextField updatedValue;
    JComboBox Un;
    JButton button;
    ResultSet resultSet;
    ImageIcon imageIcon = new ImageIcon("flight.png");

    public changeFlight(){

        setTitle("CHANGE FLIGHT");
        setSize(350,290);
        setLocationRelativeTo(null);
        setIconImage(imageIcon.getImage());
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(new Color(0xA2432F));
        setLayout(null);

        Font font = new Font("Candara",Font.BOLD,18);
        fno = Design.createLabel("Flight Number : ",new Color(0x543F48),font);
        fno.setBounds(20,30,150,20);
        Un = new JComboBox();
        Un.setBounds(20,60,300,30);
        resultSet = LoginDB.getFlightsId();
        while (true) {
            try {
                if (!resultSet.next()) break;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                Un.addItem(resultSet.getString("Flight_Number"));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        updatedfno = Design.createLabel("Updated Flight Number : ",new Color(0x543F48),font);
        updatedfno.setBounds(20,110,200,20);
        updatedValue = Design.createTextField(Color.BLACK,font);
        updatedValue.setBounds(20,140,300,30);

        button = Design.createButton("UPDATE",new Color(0x543F48),font,false,new EtchedBorder());
        button.setBounds(120,200,100,30);
        button.addActionListener(this);

        add(fno);
        add(Un);
        add(updatedfno);
        add(updatedValue);
        add(button);
        setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            String selectedFlight = (String) Un.getSelectedItem();
            LoginDB.changeFlight(selectedFlight, updatedValue.getText());
            JOptionPane.showMessageDialog(this, "Successfully changed flights !", "Update Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException se) {}
    }

}
