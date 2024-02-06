import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class changePass extends JFrame implements ActionListener {

    JLabel username, password , newPassword, confirmPassword;
    JTextField Un, passwordField, newPasswordField, confirmPasswordField;
    JButton button;
    String passwordText,user;
    ImageIcon imageIcon = new ImageIcon("pswd.png");
    public changePass(String user){
        setTitle("CHANGE PASSWORD");
        setIconImage(imageIcon.getImage());
        setSize(350,450);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(new Color(0x030f5b));
        setLayout(null);

        this.user = user;
        passwordText = LoginDB.getPassword(user);

        Font font = new Font("Candara",Font.BOLD,18);
        username = Design.createLabel("Username : ",Color.WHITE,font);
        username.setBounds(20,30,150,20);
        Un = Design.createTextField(Color.BLACK,font);
        Un.setText(user);
        Un.setEditable(false);
        Un.setBounds(20,60,300,30);

        password = Design.createLabel("Enter Password : ",Color.WHITE,font);
        password.setBounds(20,110,150,20);
        passwordField = Design.createTextField(Color.BLACK,font);
        passwordField.setBounds(20,140,300,30);

        newPassword = Design.createLabel("Enter New Password : ",Color.WHITE,font);
        newPassword.setBounds(20,200,200,20);
        newPasswordField = Design.createTextField(Color.BLACK,font);
        newPasswordField.setBounds(20,220,300,30);

        confirmPassword = Design.createLabel("Confirm Password : ",Color.WHITE,font);
        confirmPassword.setBounds(20,270,200,20);
        confirmPasswordField = Design.createTextField(Color.BLACK,font);
        confirmPasswordField.setBounds(20,300,300,30);

        button = Design.createButton("CHANGE",Color.BLACK,font,false,new EtchedBorder());
        button.setBounds(120,350,100,30);
        button.addActionListener(this);

        add(confirmPassword);
        add(confirmPasswordField);
        add(newPassword);
        add(newPasswordField);
        add(username);
        add(Un);
        add(password);
        add(passwordField);
        add(button);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if(e.getSource()==button) {
            String pass = passwordField.getText();
            if(passwordField.getText().isEmpty() || newPasswordField.getText().isEmpty() || confirmPasswordField.getText().isEmpty())
                JOptionPane.showMessageDialog(this,"Please fill all fields!","Insufficient Information",JOptionPane.ERROR_MESSAGE);
            else if(!passwordText.equals(pass)){
                JOptionPane.showMessageDialog(this,"Old password does not match with current password!","Password Mismatch",JOptionPane.ERROR_MESSAGE);
            }
            else if (!newPasswordField.getText().equals(confirmPasswordField.getText())) {
                JOptionPane.showMessageDialog(this,"The password does not match","Password Mismatch",JOptionPane.ERROR_MESSAGE);
            }
            else{
                try{
                    LoginDB.changeUserPassword(user, newPasswordField.getText());
                    JOptionPane.showMessageDialog(this,"Password Successfully changed !","Password Change Successful",JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this,"Password change failed, please try again after some time !","Change Failed",JOptionPane.ERROR_MESSAGE);
                }
            }

        }

    }
}
