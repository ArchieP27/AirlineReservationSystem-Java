import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class UserProfile extends JFrame implements ItemListener {
    JLabel username, password ;
    JTextField Un, passwordDisplay;
    JCheckBox showPassword;
    String passwordText;
    ImageIcon imageIcon = new ImageIcon("UserIcon.png");
    public UserProfile(String user){

            setTitle("USER PROFILE");
            setSize(350,250);
            setIconImage(imageIcon.getImage());
            setLocationRelativeTo(null);
            setResizable(false);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            getContentPane().setBackground(new Color(0x030f5b));
            setLayout(null);

            Font font = new Font("Candara",Font.BOLD,18);
            username = Design.createLabel("Username : ",Color.WHITE,font);
            username.setBounds(20,30,150,20);
            Un = Design.createTextField(Color.BLACK,font);
            Un.setText(user);
            Un.setEditable(false);
            Un.setBounds(20,60,300,30);

            password = Design.createLabel("Password : ",Color.WHITE,font);
            password.setBounds(20,110,150,20);
            passwordDisplay = Design.createTextField(Color.BLACK,font);
            passwordText = LoginDB.getPassword(user);
            passwordDisplay.setText("*".repeat(passwordText.length()));
            passwordDisplay.setEditable(false);
            passwordDisplay.setBounds(20,140,300,30);

            showPassword = Design.createCheckBox("Show Password",Color.WHITE,18,Font.ITALIC,"Berlin Sans FB");
            showPassword.setBounds(170,170,150,30);
            showPassword.setBackground(new Color(0x030f5b));
            showPassword.setFocusable(false);
            showPassword.addItemListener(this);

            add(showPassword);
            add(username);
            add(Un);
            add(password);
            add(passwordDisplay);
            setVisible(true);
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if(e.getSource()==showPassword){
            if(showPassword.isSelected()) {
                passwordDisplay.setText(passwordText);
            }
            else{
                passwordDisplay.setText("*".repeat(passwordText.length()));
            }
        }
    }
}
