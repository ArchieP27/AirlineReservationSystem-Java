import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class DisplaySearches extends JPanel implements ActionListener {
    JLabel fno, dt, at, pr, ac, dc, flightDate, sep;
    JButton button = new JButton("BOOK");
    String user;
    public JPanel displayFlights(String user,String arrivalLoc, String deptLoc, String date,String Flight_number,String departureTime,String arrivalTime,int price){
        JPanel flightPanel = new JPanel();
        flightPanel.setLayout(new BoxLayout(flightPanel,BoxLayout.Y_AXIS));

        this.user = user;

        final JPanel flightDisplay = new JPanel();
        flightDisplay.setBackground(new Color(0x030f5b));
        flightDisplay.setSize(500,200);
        flightDisplay.setLayout(null);
        flightDisplay.setBorder(BorderFactory.createLineBorder(Color.WHITE));

        fno = setLabel(Flight_number,Color.CYAN,30);
        fno.setBounds(10,30,150,30);
        fno.setName("Flight_number");

        dt = setLabel(departureTime,Color.WHITE,20);
        dt.setBounds(150,120,150,20);

        at = setLabel(arrivalTime,Color.WHITE,20);
        at.setBounds(420,120,150,20);

        pr = setLabel("INR "+ price,Color.yellow,20);
        pr.setBounds(450,160,150,30);

        ac = setLabel(arrivalLoc ,Color.CYAN,25);
        dc = setLabel(deptLoc ,Color.CYAN,25);
        ac.setBounds(420,80,80,30);
        dc.setBounds(150,80,80,30);

        sep = setLabel("----------->",new Color(0xEF94F3),40);
        sep.setBounds(250,100,200,30);

        flightDate = setLabel(date,Color.PINK,30);
        flightDate.setBounds(230,30,200,30);

        button.setBounds(20,150,150,30);
        button.setFocusable(false);

        flightDisplay.add(fno);
        flightDisplay.add(dt);
        flightDisplay.add(at);
        flightDisplay.add(pr);
        flightDisplay.add(ac);
        flightDisplay.add(dc);
        flightDisplay.add(flightDate);
        flightDisplay.add(sep);
        flightDisplay.add(button);

        button.addActionListener(this);

        return flightDisplay;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String fno="";
        if(e.getSource()==button){
            Container c = button.getParent();
            for(Component component : c.getComponents()){
                if(component instanceof JLabel label){
                    if(Objects.equals(label.getName(), "Flight_number")){
                        fno=label.getText();
                    }
                }
            }
            new CustomerInfo(user, (JPanel) c,fno);
        }
    }
    public JLabel setLabel(String Text, Color color, int size){
        JLabel label = new JLabel(Text);
        Font font = new Font("Candara",Font.BOLD,size);
        label.setFont(font);
        label.setForeground(color);
        return label;
    }
}