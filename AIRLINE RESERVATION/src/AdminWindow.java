import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminWindow extends JFrame implements AirConnect,ActionListener {
    String admin;
    int width,height;
    JButton home, logout;
    JMenuBar menuBar = new JMenuBar();
    JMenu flights, customerReq, Admin;
    JMenuItem seeFlights, updateFlights, addAdmin, seeReq, changeFlights;
    JPanel center, flightDetails;
    CardLayout cl = new CardLayout();
    public AdminWindow(String admin, int width, int height){
        this.width = width;
        this.height = height;
        setTitle(title);
        setIconImage(icon.getImage());
        setSize(width,height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setContentPane(Design.createImageBackground("adminScreen.jpg"));
        setLayout(new BorderLayout());

        menuBar.setBackground(new Color(0xA2432F));
        menuBar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        menuBar.setPreferredSize(new Dimension(1200,30));
        flights = new JMenu("Flights");
        seeFlights = new JMenuItem("See Flights");
        updateFlights = new JMenuItem("Update Flights");
        changeFlights = new JMenuItem("Change Flights");
        seeFlights.addActionListener(this);
        changeFlights.addActionListener(this);
        updateFlights.addActionListener(this);
        flights.add(seeFlights);
        flights.add(changeFlights);
        flights.add(updateFlights);
        customerReq = new JMenu("Customer Requests");
        seeReq = new JMenuItem("See Requests");
        customerReq.add(seeReq);
        seeReq.addActionListener(this);
        menuBar.add(flights);
        menuBar.add(customerReq);
        Admin = new JMenu("Admin");
        if(admin.equals("Admin")) {
            addAdmin = new JMenuItem("Add Admin");
            Admin.add(addAdmin);
            addAdmin.addActionListener(this);
        }
        menuBar.add(Admin);
        add(menuBar,BorderLayout.PAGE_START);

        home = Design.createButton("homeScreen.png",40,35);
        home.setBounds(20,40,60,45);
        home.setBackground(new Color(0xFF8A65));
        home.addActionListener(this);
        add(home);

        logout = Design.createButton("logout.png",40,35);
        logout.setBounds(20,height-100,60,45);
        logout.setBackground(new Color(0xFF8A65));
        logout.addActionListener(this);
        add(logout);

        JPanel display = new JPanel();
        display.setOpaque(false);
        center = new JPanel();
        center.setLayout(cl);
        center.add(display,"0");
        JScrollPane flightDisplay = new JScrollPane(new AdminFunctionalities().showFlights());
        flightDisplay.setBounds(50,70,1090,600);
        center.add(flightDisplay,"1");
        center.add(new caterRequests(),"2");
        center.add(new AdminFunctionalities().updateFlights(),"3");
        center.setOpaque(false);

        this.admin = admin;
        add(center);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==seeFlights){
            cl.show(center,"1");
            revalidate();
        }
        if(e.getSource()==addAdmin){
            cl.show(center,"0");
            revalidate();
            new AdminSignIn().createAdmin(this);
        }
        if(e.getSource()==seeReq){
            cl.show(center,"2");
            revalidate();
        }
        if(e.getSource()==home){
            cl.show(center,"0");
        }
        if(e.getSource()==logout){
            dispose();
            Thread logoutThread = new Thread(()->{
                new ProgressScreen().displayLogoutProgress();
                Runnable callback = () -> {new HOMEPAGE(width,height);};
                try {
                    Thread.sleep(2500);
                }catch (InterruptedException ie){
                    ie.printStackTrace();
                }
                SwingUtilities.invokeLater(callback);
            });
            logoutThread.start();
        }
        if(e.getSource()==updateFlights){
            cl.show(center,"3");
            revalidate();
        }
        if(e.getSource()==changeFlights){
            cl.show(center,"0");
            center.revalidate();
            new changeFlight();
        }
    }
}
