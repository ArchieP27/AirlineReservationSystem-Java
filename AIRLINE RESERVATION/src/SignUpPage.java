import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SignUpPage extends JFrame implements ActionListener {
    JLabel username, password ;
    JTextField Un;
    JPasswordField passwordField;
    JButton button;
    ImageIcon imageIcon = new ImageIcon("profile.png");
    public SignUpPage(JFrame parentFrame){

        setTitle("SIGN-UP");
        setSize(350,300);
        setIconImage(imageIcon.getImage());
        setLocationRelativeTo(parentFrame);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(new Color(0x030f5b));
        setLayout(null);

        Font font = new Font("Candara",Font.BOLD,18);
        username = Design.createLabel("Username : ",Color.WHITE,font);
        username.setBounds(20,30,150,20);
        Un = Design.createTextField(Color.BLACK,font);
        Un.setBounds(20,60,300,30);
        Un.addActionListener(this);

        password = Design.createLabel("Password : ",Color.WHITE,font);
        password.setBounds(20,110,150,20);
        passwordField = Design.createPasswordField(Color.BLACK,font);
        passwordField.setBounds(20,140,300,30);
        passwordField.addActionListener(this);

        button = Design.createButton("SIGN-UP",Color.BLACK,font,false,new EtchedBorder());
        button.setBounds(120,200,100,30);
        button.addActionListener(this);

        add(username);
        add(Un);
        add(password);
        add(passwordField);
        add(button);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if(e.getSource()==button || e.getSource()==Un || e.getSource()== passwordField) {
            String user = Un.getText();
            String pass = passwordField.getText();

            if(user.isEmpty() || pass.isEmpty()){
                JOptionPane.showMessageDialog(this, "Please fill all fields", "Insufficient information", JOptionPane.ERROR_MESSAGE);
            }
            else {
                if (LoginDB.addUser(user, pass)) {
                    JOptionPane.showMessageDialog(this, "User Successfully Created", "Creation Successful", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "User can't be created", "Creation Unsuccessful", JOptionPane.ERROR_MESSAGE);
                }
            }
        }

    }
}
