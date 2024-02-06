import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Payment extends JFrame implements ActionListener {
    JPanel p1 = new JPanel();
    JPanel p2 = new JPanel();
    CardLayout cl = new CardLayout();
    Container c;
    ImageIcon icon = new ImageIcon("payment.png");
    public Payment(String user,String passengerName, String pid, String cno, String fno, String email, String seatNumber, int age, String g, int price) {
        setTitle("PAYMENT");
        setIconImage(icon.getImage());
        setResizable(false);
        setSize(410, 278);
        setLayout(cl);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(cl);

        p1.setLayout(new GridLayout(0,2));
        JButton netBanking = Design.createButton("NetBanking.png","Net Banking",150,170);
        JButton upi = Design.createButton("UPI.png","UPI",150,170);
        netBanking.addActionListener(this);
        upi.addActionListener(this);

        p1.add(netBanking);
        p1.add(upi);

        p2.setLayout(null);
        JTextField accountNumber = Design.createTextField(new Color(0x030f5b),new Font("Agency FB",Font.PLAIN,20));
        JTextField passwordDisplay = Design.createPasswordField(new Color(0x030f5b),new Font("Agency FB",Font.PLAIN,20));
        JLabel amount = Design.createLabel("Amount : INR "+price,Color.BLACK,20,Font.BOLD,"Agency FB");
        JLabel accID = Design.createLabel("Account ID : ",new Color(0x030f5b),20,Font.BOLD,"Agency FB");
        JLabel accPass = Design.createLabel("Account Password : ",new Color(0x030f5b),20,Font.BOLD,"Agency FB");

        amount.setBounds(140,20,150,30);
        accID.setBounds(40,70,150,30);
        accountNumber.setBounds(200,70,150,30);
        accPass.setBounds(40,120,150,30);
        passwordDisplay.setBounds(200,120,150,30);
        p2.add(amount);
        p2.add(accID);
        p2.add(accountNumber);
        p2.add(passwordDisplay);
        p2.add(accPass);

        JButton pay = Design.createButton("PAY",new Color(0x030f5b),20,Font.BOLD,"Candara",false,new EtchedBorder());
        pay.setBounds(140,170,150,40);
        pay.addActionListener(e -> {
            if(accountNumber.getText().isEmpty() || passwordDisplay.getText().isEmpty()){
                JOptionPane.showMessageDialog(p2,"Fill All Fields !","Insufficient Data",JOptionPane.ERROR_MESSAGE);
            }
            else{
                JOptionPane.showMessageDialog(p2,"Payment Successful !","Success",JOptionPane.INFORMATION_MESSAGE);
                dispose();
                LoginDB.addCustomer(user,passengerName,pid,cno,fno,email,seatNumber,age,g);
                new ViewTicket(pid);
            }
        });
        p2.add(pay);

        c = getContentPane();
        c.add(p1,"1");
        c.add(p2,"2");

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        setVisible(false);
        revalidate();
        Thread bankRedirectThread = new Thread(()->{
            new ProgressScreen().displayPaymentScreen();
            Runnable callback = () -> {cl.show(c,"2"); setVisible(true);};
            try {
                Thread.sleep(2500);
            }catch (InterruptedException ie){
                ie.printStackTrace();
            }
            SwingUtilities.invokeLater(callback);
        });
        bankRedirectThread.start();
    }

}
