import javax.swing.*;

public class ViewTicket extends JFrame {

    ImageIcon imageIcon = new ImageIcon("ticketIcon.png");
    public ViewTicket(String pID){
        setTitle("TICKET");
        setIconImage(imageIcon.getImage());
        setSize(900,400);
        setResizable(false);
        setLocationRelativeTo(null);
        setContentPane(Design.createImageBackground("ticketTemplate.png"));
        setLayout(null);
        setVisible(true);
    }

}