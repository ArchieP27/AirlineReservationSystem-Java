import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class AdminSignIn extends JFrame implements ActionListener {
    JLabel username, password ,welcome;
    JTextField Un, pswd1;
    JPasswordField pswd;
    JCheckBox showpswd ;
    JButton button;
    JFrame parent;
    int width, height;
    ImageIcon imageIcon = new ImageIcon("profile.png");
    public AdminSignIn(){}
    public AdminSignIn(JFrame parentFrame, int width, int height){

        setTitle("ADMIN SIGN-IN");
        setSize(400,450);
        setIconImage(imageIcon.getImage());
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(new Color(0x030f5b));
        setLayout(null);
        parent = parentFrame;
        this.width = width;
        this.height = height;

        welcome = Design.createLabel("ADMIN LOG-IN",Color.CYAN,30,Font.BOLD,"Berlin Sans FB");
        welcome.setBounds(67,30,300,30);

        Font font = new Font("Candara",Font.BOLD,25);
        username = Design.createLabel("Username : ",Color.WHITE,font);
        username.setBounds(20,100,150,30);
        Un = Design.createTextField(Color.BLACK,font);
        Un.setBounds(20,140,350,40);

        password = Design.createLabel("Password : ",Color.WHITE,font);
        password.setBounds(20,200,150,30);
        pswd = Design.createPasswordField(Color.BLACK,font);
        pswd.setBounds(20,240,350,40);

        showpswd = Design.createCheckBox("Show Password",Color.WHITE,15,Font.ITALIC,"Arial",false,new Color(0x030f5b));
        showpswd.setBounds(240,290,200,20);
        showpswd.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                pswd1 = Design.createTextField(Color.BLACK,font,false);
                String password = pswd.getText().toString();
                if(showpswd.isSelected()) {
                    pswd1.setText(password);
                    pswd1.setBounds(20,240,350,40);
                    remove(pswd);
                    add(pswd1);
                    revalidate();
                }
                else{
                    remove(pswd1);
                    pswd.setText(password);
                    add(pswd);
                    revalidate();
                }
            }
        });

        button = Design.createButton("SIGN-UP",Color.BLACK,font,false,new EtchedBorder());
        button.setBounds(115,340,150,40);
        button.addActionListener(this);

        add(welcome);
        add(username);
        add(Un);
        add(password);
        add(pswd);
        add(showpswd);
        add(button);
        setVisible(true);
    }

    public void createAdmin(JFrame parentFrame){

        setTitle("NEW ADMIN");
        setSize(400,450);
        setIconImage(imageIcon.getImage());
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(new Color(0xA2432F));
        setLayout(null);

        JLabel welcome = Design.createLabel("ADD ADMIN",new Color(0x543F48),30,Font.BOLD,"Berlin Sans FB");
        welcome.setBounds(97,30,300,30);

        Font font = new Font("Candara",Font.BOLD,25);
        username = Design.createLabel("Username : ",Color.WHITE,font);
        username.setBounds(20,100,150,30);
        Un = Design.createTextField(Color.BLACK,font);
        Un.setBounds(20,140,350,40);

        password = Design.createLabel("Password : ",Color.WHITE,font);
        password.setBounds(20,200,150,30);
        JTextField pswd1 = Design.createTextField(Color.BLACK,font);
        pswd1.setBounds(20,240,350,40);

        button = Design.createButton("CREATE",Color.BLACK,font,false,new EtchedBorder());
        button.setBounds(115,320,150,50);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String user = Un.getText().toString();
                String pass = pswd1.getText().toString();

                if(user.isEmpty() || pass.isEmpty())
                    JOptionPane.showMessageDialog(parentFrame,"Please fill all fields!","Insufficient Information",JOptionPane.ERROR_MESSAGE);
                else{
                    if(LoginDB.addAdmin(user,pass)){
                        dispose();
                        JOptionPane.showMessageDialog(parentFrame, "Admin Successfully Created", "Creation Successful", JOptionPane.INFORMATION_MESSAGE);
                    }
                    else{
                        JOptionPane.showMessageDialog(parentFrame,"Admin already exists!","Creation Unsuccessful",JOptionPane.ERROR_MESSAGE);
                    }
                }

            }
        });

        add(welcome);
        add(username);
        add(Un);
        add(password);
        add(pswd1);
        add(button);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if(e.getSource()==button) {
            String user = Un.getText().toString();
            String pass = pswd.getText().toString();

                if(LoginDB.checkAdmin(user)) {
                    parent.dispose();
                    dispose();
                    Thread adminWelcomeThread = new Thread(()->{
                        new ProgressScreen().adminWelcomeScreen();
                        Runnable callback = () -> new AdminWindow(user,width,height);
                        try {
                            Thread.sleep(2500);
                        }catch (InterruptedException ie){
                            ie.printStackTrace();
                        }
                        SwingUtilities.invokeLater(callback);
                    });
                    adminWelcomeThread.start();
                }
                else
                    JOptionPane.showMessageDialog(this, "Invalid Password", "Password Mismatch", JOptionPane.ERROR_MESSAGE);
            }
            else{
                JOptionPane.showMessageDialog(this, "User doesn't exist", "Login Unsuccessful", JOptionPane.ERROR_MESSAGE);
            }
        }
}
