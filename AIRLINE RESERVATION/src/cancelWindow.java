import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.ResultSet;
import java.sql.SQLException;

public class cancelWindow extends JFrame implements ActionListener, ItemListener {

    JLabel flightNumber, PassengerID;
    JComboBox fNo, pID;
    JButton confirm;
    ResultSet resultSet;
    String user;
    ImageIcon imageIcon = new ImageIcon("flight.png");

    public cancelWindow(String user) {
        setTitle("CANCEL FLIGHT");
        setIconImage(imageIcon.getImage());
        setSize(400, 450);
        setLocationRelativeTo(null);
        setLayout(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(new Color(0x030f5b));

        this.user = user;
        resultSet = LoginDB.searchFlights(user);
        JLabel display = Design.createLabel("CANCEL FLIGHT", Color.WHITE, 30, Font.BOLD, "Berlin Sans FB");
        display.setBounds(67, 30, 300, 30);

        Font font = new Font("Candara", Font.BOLD, 25);
        flightNumber = Design.createLabel("Flight Number : ", Color.WHITE, font);
        flightNumber.setBounds(20, 100, 250, 30);
        fNo = new JComboBox();
        while (true) {
            try {
                if (!resultSet.next()) break;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                fNo.addItem(resultSet.getString("Flight_Number"));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        fNo.setBounds(20, 140, 350, 40);
        fNo.addItemListener(this);

        PassengerID = Design.createLabel("Passenger ID : ", Color.WHITE, font);
        PassengerID.setBounds(20, 200, 250, 30);
        pID = new JComboBox();
        pID.setBounds(20, 240, 350, 40);

        confirm = Design.createButton("CONFIRM", Color.BLACK, font, false, new EtchedBorder());
        confirm.setBounds(115, 320, 150, 50);
        confirm.addActionListener(this);

        add(display);
        add(flightNumber);
        add(fNo);
        add(PassengerID);
        add(pID);
        add(confirm);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == confirm) {
            int choice = JOptionPane.showConfirmDialog(this,"Confirm Cancel ?","CONFIRMATION",JOptionPane.YES_NO_OPTION);
            if(choice== JOptionPane.YES_OPTION){
                try {
                    LoginDB.createRequest((String) fNo.getSelectedItem(), (String) pID.getSelectedItem(),"","","Cancel");
                    JOptionPane.showMessageDialog(this, "Cancel request generated !", "Cancel Requested", JOptionPane.INFORMATION_MESSAGE);
                    this.dispose();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Request Failed !", "Cancel Unsuccessful", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        resultSet = LoginDB.getPID((String) fNo.getSelectedItem(),user);
        remove(pID);
        revalidate();
        pID = new JComboBox();
        while (true) {
            try {
                if (!resultSet.next()) break;
                pID.addItem(resultSet.getString("Passenger_ID"));
            } catch (SQLException se) {
                throw new RuntimeException(se);
            }
        }
        pID.setBounds(20, 240, 350, 40);
        add(pID);
        revalidate();
    }
}
