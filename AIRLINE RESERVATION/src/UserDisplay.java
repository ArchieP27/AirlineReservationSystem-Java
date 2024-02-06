import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@SuppressWarnings("CallToPrintStackTrace")
public class UserDisplay extends JFrame implements AirConnect,ActionListener {
    int width,height;
    String user;
    CardLayout cl = new CardLayout();
    JPanel center = new JPanel(cl);
    JButton more, home;
    JPopupMenu popupMenu = new JPopupMenu();
    JMenu Flights = new JMenu("Flights");
    JMenu Profile = new JMenu("Profile");
    JMenuItem checkFlights = new JMenuItem("Check Flight");
    JMenuItem searchFlights = new JMenuItem("Search Flight");
    JMenuItem cancelFlights = new JMenuItem("Cancel Flight");
    JMenuItem updateFlights = new JMenuItem("Update Flight");
    JMenuItem viewProfile = new JMenuItem("View Profile");
    JMenuItem changePassword = new JMenuItem("Change Password");
    JMenuItem viewTickets = new JMenuItem("View Tickets");
    JMenuItem checkReq = new JMenuItem("See Requests");
    JMenuItem viewMessages = new JMenuItem("View Messages");
    JMenuItem logout = new JMenuItem("Logout");
    public UserDisplay(String user, int width, int height){
        this.user = user;
        this.width=width;
        this.height=height;
        setTitle(title);
        setIconImage(icon.getImage());
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(width,height+80);
        setLayout(null);
        setLocationRelativeTo(null);
        setBackground(Color.WHITE);
        setContentPane(Design.createImageBackground("UserPageBG.png"));

        home = Design.createButton("homeScreen.png",40,35);
        home.setBounds(20,20,60,45);
        home.addActionListener(this);
        add(home);

        popupMenu.setBorderPainted(true);
        Flights.add(searchFlights);
        searchFlights.addActionListener(this);
        Flights.add(new JSeparator());
        Flights.add(checkFlights);
        checkFlights.addActionListener(this);
        Flights.add(new JSeparator());
        Flights.add(cancelFlights);
        cancelFlights.addActionListener(this);
        Flights.add(new JSeparator());
        Flights.add(updateFlights);
        updateFlights.addActionListener(this);
        popupMenu.add(Flights);
        popupMenu.add(new JSeparator());
        popupMenu.add(viewTickets);
        viewTickets.addActionListener(this);
        popupMenu.add(new JSeparator());
        Profile.add(viewProfile);
        viewProfile.addActionListener(this);
        Profile.add(new JSeparator());
        Profile.add(changePassword);
        changePassword.addActionListener(this);
        popupMenu.add(Profile);
        popupMenu.add(new JSeparator());
        popupMenu.add(checkReq);
        popupMenu.add(new JSeparator());
        popupMenu.add(viewMessages);
        viewMessages.addActionListener(this);
        popupMenu.add(new JSeparator());
        checkReq.addActionListener(this);
        popupMenu.add(logout);
        logout.addActionListener(this);

        more = Design.createButton("moreButton.png",40,35);
        more.setBounds(width-105,20,60,45);
        more.addActionListener(this);
        add(more);

        center.setOpaque(false);
        center.setBounds(0,70,width,height);
        JPanel display = new JPanel();
        display.setOpaque(false);
        center.add(new SearchFlights("Archita"),"1");
        center.add(display,"0");
        center.add(new UserFunctionalities(user).showFlights(),"2");
        center.add(new UserFunctionalities("Archita").updateFlights(),"3");
        center.add(new RequestView(user),"4");
        add(center);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==more){
            popupMenu.show(this,more.getX()-60,more.getY()+30);
        }
        if(e.getSource()==searchFlights){
            cl.show(center,"1");
            revalidate();
        }
        if(e.getSource()==checkFlights){
            cl.show(center,"2");
            revalidate();
        }
        if(e.getSource()==updateFlights){
            cl.show(center,"3");
            revalidate();
        }
        if(e.getSource()==cancelFlights){
            cl.show(center,"0");
            new cancelWindow(user);
        }
        if(e.getSource()==viewProfile){
            cl.show(center,"0");
            new UserProfile(user);
        }
        if(e.getSource()==changePassword){
            cl.show(center,"0");
            new changePass(user);
        }
        if(e.getSource()==home){
            cl.show(center,"0");
        }
        if(e.getSource()==logout){
            dispose();
            Thread logoutThread = new Thread(()->{
                new ProgressScreen().displayLogoutProgress();
                Runnable callback = () -> new HOMEPAGE(width,height);
                try {
                    Thread.sleep(2500);
                }catch (InterruptedException ie){
                    ie.printStackTrace();
                }
                SwingUtilities.invokeLater(callback);
            });
            logoutThread.start();
        }
        if(e.getSource()==checkReq){
            cl.show(center,"4");
        }
        if(e.getSource()==viewMessages){
            cl.show(center,"0");
            revalidate();
            new AdminMessage(user);
        }
        if(e.getSource()==viewTickets){
            cl.show(center,"0");
            revalidate();
            new UserFunctionalities(user).searchTicktets();
        }
    }
}
