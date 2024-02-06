import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ProgressScreen extends JFrame{
    static JWindow window;
    static JPanel backgroundPanel;
    static Font font = new Font("Berlin Sans FB",Font.ITALIC,25);
    static JLabel displayText;
    static Timer timer;
    JLabel displayIcon;
    JProgressBar progressBar;

    public ProgressScreen(){
        window = new JWindow();
        window.setSize(350,250);
        window.setLocationRelativeTo(null);

        progressBar = new JProgressBar(0,100);
        window.add(progressBar,BorderLayout.PAGE_END);
    }

    public void displayWelcomeScreen(String user) {
        backgroundPanel = Design.createPanel(Color.WHITE,new EtchedBorder(),null);

        displayText = Design.createLabel("Welcome "+user,new Color(0x07A1B4),font);
        displayText.setBounds(50,190,250,20);
        displayText.setHorizontalAlignment(SwingConstants.CENTER);
        backgroundPanel.add(displayText);
        ImageIcon UserIcon = new ImageIcon("UserIcon.png");
        UserIcon = new ImageIcon(UserIcon.getImage().getScaledInstance(150,155,Image.SCALE_SMOOTH));
        displayIcon = new JLabel(UserIcon);
        displayIcon.setHorizontalAlignment(SwingConstants.CENTER);
        displayIcon.setBounds(90,15,170,170);
        backgroundPanel.add(displayIcon);
        window.add(backgroundPanel);

        progressBar.setForeground(new Color(0x07A1B4));
        window.revalidate();
        timer = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int x = progressBar.getValue();
                if(x==100){
                    window.dispose();
                    timer.stop();
                }
                else {
                    progressBar.setValue(x+4);
                }
            }
        });
        timer.start();
        window.setVisible(true);
    }

    public void adminWelcomeScreen() {
        backgroundPanel = Design.createPanel(Color.WHITE,new EtchedBorder(),null);

        displayText = Design.createLabel("Welcome Admin",Color.BLACK,font);
        displayText.setBounds(50,190,250,20);
        displayText.setHorizontalAlignment(SwingConstants.CENTER);
        backgroundPanel.add(displayText);
        ImageIcon UserIcon = new ImageIcon("admin.png");
        UserIcon = new ImageIcon(UserIcon.getImage().getScaledInstance(150,155,Image.SCALE_SMOOTH));
        displayIcon = new JLabel(UserIcon);
        displayIcon.setHorizontalAlignment(SwingConstants.CENTER);
        displayIcon.setBounds(90,15,170,170);
        backgroundPanel.add(displayIcon);
        window.add(backgroundPanel);

        progressBar.setForeground(Color.RED);
        window.revalidate();
        timer = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int x = progressBar.getValue();
                if(x==100){
                    window.dispose();
                    timer.stop();
                }
                else {
                    progressBar.setValue(x+4);
                }
            }
        });
        timer.start();
        window.setVisible(true);
    }

    public void displayPaymentScreen() {

        backgroundPanel = Design.createPanel(new Color(0x44C5FE), new EtchedBorder(), null);

        displayText = Design.createLabel("Redirecting to bank page ... ", new Color(0x030f5b), font);
        displayText.setBackground(new Color(0x44C5FE));
        displayText.setBounds(27, 175, 300, 25);
        backgroundPanel.add(displayText);
        ImageIcon imageIcon = new ImageIcon("Bank.png");
        imageIcon = new ImageIcon(imageIcon.getImage().getScaledInstance(150, 155, Image.SCALE_SMOOTH));
        displayIcon = new JLabel(imageIcon);
        displayIcon.setHorizontalAlignment(SwingConstants.CENTER);
        displayIcon.setBounds(90, 15, 170, 170);
        backgroundPanel.add(displayIcon);
        window.add(backgroundPanel);

        progressBar.setForeground(new Color(0x030f5b));
        window.revalidate();
        timer = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int x = progressBar.getValue();
                if (x == 100) {
                    window.dispose();
                    timer.stop();
                } else {
                    progressBar.setValue(x + 4);
                }
            }
        });
        timer.start();

        window.setVisible(true);

    }

    public void displayLogoutProgress() {

        backgroundPanel = Design.createPanel(Color.WHITE, new EtchedBorder(), null);

        displayText = Design.createLabel("Logging out . . .", new Color(0x42A5F5), font);
        displayText.setBounds(90, 175, 300, 25);
        backgroundPanel.add(displayText);
        ImageIcon imageIcon = new ImageIcon("logout.png");
        imageIcon = new ImageIcon(imageIcon.getImage().getScaledInstance(150, 125, Image.SCALE_SMOOTH));
        displayIcon = new JLabel(imageIcon);
        displayIcon.setHorizontalAlignment(SwingConstants.CENTER);
        displayIcon.setBounds(120, 15, 170, 170);
        backgroundPanel.add(displayIcon);
        window.add(backgroundPanel);

        progressBar.setForeground(new Color(0xEF5350));
        window.revalidate();
        timer = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int x = progressBar.getValue();
                if (x == 100) {
                    window.dispose();
                    timer.stop();
                } else {
                    progressBar.setValue(x + 4);
                }
            }
        });
        timer.start();

        window.setVisible(true);

    }
}
