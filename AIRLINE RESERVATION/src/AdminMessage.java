import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminMessage extends JFrame {

    JTextArea display;
    ResultSet resultSet;
    ImageIcon imageIcon = new ImageIcon("profile.png");
    public AdminMessage(String user){

        setTitle("VIEW ADMIN MESSAGES");
        setIconImage(imageIcon.getImage());
        setSize(400,200);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        resultSet = LoginDB.viewMessages(user);
        display = new JTextArea();
        display.setFont(new Font("Candara",Font.BOLD,20));
        display.setEditable(false);
        display.setText("For changed flights, tickets will be mailed by the airline\n");
        try {
            while (resultSet.next()) {
                display.setText(display.getText() + "\nPassenger ID : " + resultSet.getString(3) +
                        "\nDate : " + resultSet.getString(5) + "\n"+
                        resultSet.getString(4) + "\n");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        add(new JScrollPane(display));
        setVisible(true);

    }
}
