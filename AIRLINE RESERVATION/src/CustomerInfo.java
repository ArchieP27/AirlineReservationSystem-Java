import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerInfo extends JFrame implements AirConnect,ActionListener, ItemListener {
    JTextField name;
    JTextField contact;
    JTextField FlightNumberDisplay;
    JTextField customerID;
    JTextField emailDisplay;
    JTextField seatNumber = new JTextField(5);
    JRadioButton mr, mrs, ms,m,f;
    ResultSet resultSet;
    JButton payment, showSeat;
    SpinnerModel ageValue = new SpinnerNumberModel(1,1,120,1);
    JSpinner getAge = new JSpinner(ageValue);
    String flight_number, passengerName;
    JPanel setSeat = new JPanel();
    JTable seatNumberDisplay;
    JWindow seats;
    int flightPrice;
    String user;
    public CustomerInfo(String user, JPanel c, String Fno){
        this.user = user;
        flight_number = Fno;
        setTitle(title);
        setIconImage(icon.getImage());
        setSize(1100,800);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);
        JPanel top = Design.createPanel(new Color(0x030f5b),new LineBorder(Color.BLACK),null);
        top.setBounds(0,0,1100,150);
        c.setBorder(null);
        c.setBounds(250,0,600,150);
        top.add(c);
        add(top);


        // PASSENGER DETAILS

        Font font = (new Font("Candara",Font.BOLD,25));

        JLabel cid = Design.createLabel("Passenger ID : ",font);
        cid.setBounds(20,200,200,30);
        add(cid);
        String s = LoginDB.getCustomerID();
        int n = Integer.parseInt(s.substring(1));
        n+=1;
        customerID = Design.createTextField(font,false);
        customerID.setBounds(180,190,200,40);
        customerID.setText(String.format("C%04d",n));
        add(customerID);
        JLabel cname = Design.createLabel("Passenger Name : ",font);
        cname.setBounds(20,300,200,30);
        add(cname);

        Font font1 = (new Font("Candara",Font.BOLD,20));
        mr = Design.createRadioButton("Mr.",font1,true);
        ms = Design.createRadioButton("Ms.",font1,false);
        mrs = Design.createRadioButton("Mrs.",font1,false);
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(mr);
        buttonGroup.add(ms);
        buttonGroup.add(mrs);
        mr.setBounds(270,260,60,20);
        ms.setBounds(340,260,60,20);
        mrs.setBounds(410,260,70,20);
        mr.addItemListener(this);
        ms.addItemListener(this);
        mrs.addItemListener(this);
        add(mr);
        add(ms);
        add(mrs);

        JLabel cno = Design.createLabel("Passenger Contact : ",font);
        cno.setBounds(20,440,250,30);
        add(cno);

        name = Design.createTextField(font,true);
        name.setBounds(250,290,250,40);
        add(name);
        contact = Design.createTextField(font,true);
        contact.setBounds(250,430,250,40);
        add(contact);

        JLabel email = Design.createLabel("Passenger E-Mail : ",font);
        email.setBounds(20,510,250,30);
        add(email);

        emailDisplay = Design.createTextField(font,true);
        emailDisplay.setBounds(250,500,250,40);
        emailDisplay.setText("( Optional )");
        add(emailDisplay);

        JLabel age= Design.createLabel("Age : ",font);
        age.setBounds(20,370,80,30);
        add(age);
        getAge.setFont(font);
        getAge.setBounds(90,360,60,40);
        add(getAge);

        JLabel gender = Design.createLabel("Gender : ",font);
        gender.setBounds(180,370,100,30);
        add(gender);
        Font font2 = (new Font("Candara",Font.BOLD,22));
        m= Design.createRadioButton("Male",font2,true);
        f= Design.createRadioButton("Female",font2,false);
        m.setBounds(290,370,100,30);
        f.setBounds(390,370,150,30);
        ButtonGroup buttonGroup1 = new ButtonGroup();
        buttonGroup1.add(m);
        buttonGroup1.add(f);
        add(m);
        add(f);
        m.addItemListener(this);
        f.addItemListener(this);

        payment = Design.createButton("PROCEED FOR PAYMENT",new Color(0x030f5b),25,Font.BOLD,"Agency FB");
        payment.setBounds(140,670,300,50);
        add(payment);
        payment.addActionListener(this);

        JLabel seat = Design.createLabel("Seat Number : ",font);
        seat.setBounds(20, 580,200,30);
        add(seat);

        setSeat.setLayout(new GridLayout(0,2));
        setSeat.setBounds(250,570,150,40);
        seatNumber.setFont(font);
        seatNumber.setEditable(false);
        setSeat.add(seatNumber);

        showSeat = Design.createButton("seat.png",40,30);
        showSeat.setMaximumSize(new Dimension(50,40));
        showSeat.addActionListener(this);
        setSeat.add(showSeat);
        add(setSeat);

        seatNumberDisplay = new FlightSeats().displaySeats(flight_number);
        seatNumberDisplay.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount()==2){
                    String seatNumber = (String) seatNumberDisplay.getValueAt(seatNumberDisplay.getSelectedRow(),seatNumberDisplay.getSelectedColumn());
                    if(seatNumber.equals("NA"))
                        JOptionPane.showMessageDialog(setSeat,"This seat is already occupied!","Seat Full",JOptionPane.ERROR_MESSAGE);
                    else
                        CustomerInfo.this.seatNumber.setText(seatNumber);
                    seats.dispose();
                }
            }
        });

        // FLIGHT DETAILS
        JPanel Flight_info = Design.createPanel(new Color(0x030f5b),Design.createTitledBorder(Color.WHITE,4," FLIGHT DETAILS ",Color.CYAN,"Berlin Sans FB",Font.ITALIC,30),null);
        Flight_info.setBounds(570,170,500,575);

        JLabel fno = Design.createLabel("Flight Number : ",Color.WHITE,font);
        fno.setBounds(20,65,200,30);
        Flight_info.add(fno);
        FlightNumberDisplay = Design.createTextField(Color.BLACK,font,false);
        FlightNumberDisplay.setBounds(200,55,70,40);
        FlightNumberDisplay.setText(Fno);
        Flight_info.add(FlightNumberDisplay);

        resultSet = LoginDB.getFlights(Fno);
        JLabel flightDate = Design.createLabel("Flight Date : ",Color.WHITE,font);
        flightDate.setBounds(20,125,200,30);
        Flight_info.add(flightDate);
        JTextField dateDisplay = Design.createTextField(font,false);
        dateDisplay.setBounds(200,115,115,40);

        JLabel departureAirport = Design.createLabel("Departure Airport : ",Color.WHITE,font);
        JLabel arrivalAirport = Design.createLabel("Arrival Airport : ",Color.WHITE,font);
        departureAirport.setBounds(20,195,300,30);
        Flight_info.add(departureAirport);
        JTextField departureDisplay =  Design.createTextField(font,false);
        departureDisplay.setHorizontalAlignment(SwingConstants.CENTER);
        departureDisplay.setBounds(40,230,430,40);
        arrivalAirport.setBounds(20,290,300,30);
        Flight_info.add(arrivalAirport);
        JTextField arrivalDisplay =  Design.createTextField(font,false);
        arrivalDisplay.setHorizontalAlignment(SwingConstants.CENTER);
        arrivalDisplay.setBounds(40,325,430,40);

        JLabel departureTime = Design.createLabel("Departure time : ",Color.WHITE,25,Font.BOLD,"Candara");
        departureTime.setBounds(50,390,300,30);
        Flight_info.add(departureTime);
        JTextField departureTimeDisplay =  Design.createTextField(font,false);
        departureTimeDisplay.setHorizontalAlignment(SwingConstants.CENTER);
        departureTimeDisplay.setBounds(80,420,100,45);

        JLabel arrivalTime = Design.createLabel("Arrival time : ",Color.WHITE,25,Font.BOLD,"Candara");
        arrivalTime.setBounds(290,390,300,30);
        Flight_info.add(arrivalTime);
        JTextField arrivalTimeDisplay = Design.createTextField(font,false);
        arrivalTimeDisplay.setHorizontalAlignment(SwingConstants.CENTER);
        arrivalTimeDisplay.setBounds(310,420,100,45);

        JLabel price = Design.createLabel("Price : ",Color.WHITE,25,Font.BOLD,"Candara");
        price.setBounds(130,510,150,30);
        Flight_info.add(price);
        JTextField priceDisplay= Design.createTextField(font,false);
        priceDisplay.setHorizontalAlignment(0);
        priceDisplay.setBounds(220,495,140,45);

        try {
            if(resultSet.next()) {
                departureDisplay.setText(resultSet.getString("DepartureAirportName"));
                arrivalDisplay.setText(resultSet.getString("ArrivalAirportName"));
                departureTimeDisplay.setText(resultSet.getString("Departure_time").substring(0,5));
                arrivalTimeDisplay.setText(resultSet.getString("Arrival_time").substring(0,5));
                dateDisplay.setText(resultSet.getString("Flight_date"));
                flightPrice = resultSet.getInt("Price");
                priceDisplay.setText("INR "+ flightPrice);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        Flight_info.add(departureDisplay);
        Flight_info.add(arrivalDisplay);
        Flight_info.add(departureTimeDisplay);
        Flight_info.add(arrivalTimeDisplay);
        Flight_info.add(dateDisplay);
        Flight_info.add(priceDisplay);

        add(Flight_info);

        setVisible(true);

    }

    public static void main(String[] args) {
        JPanel panel = new JPanel();
        panel.setBackground(Color.BLUE);
        new CustomerInfo("Archita",panel,"AI191");
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if(e.getSource()== showSeat){

            seats = new JWindow();
            seats.setSize(220,196);
            seats.setLocationRelativeTo(showSeat);

            JScrollPane table = new JScrollPane(seatNumberDisplay);
            table.setBounds(10,35,200,150);
            seats.add(table);
            seats.setVisible(true);

            revalidate();

        }

        if(e.getSource()==payment) {
            if(mr.isSelected())
                passengerName = "Mr. "+name.getText();
            if(ms.isSelected())
                passengerName = "Ms. "+name.getText();
            if(mrs.isSelected())
                passengerName = "Mrs. "+name.getText();

            if (name.getText().isEmpty() || contact.getText().isEmpty() || seatNumber.getText().isEmpty())
                JOptionPane.showMessageDialog(this, "Fill all fields!", "Invalid Submit", JOptionPane.ERROR_MESSAGE);
            else {
                JFrame frame = new JFrame("Customer Details");
                frame.setIconImage(icon.getImage());
                frame.setSize(300, 300);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setLocationRelativeTo(null);
                JEditorPane details = getjEditorPane();

                frame.add(details);
                frame.setVisible(true);

                JButton proceed = new JButton("PROCEED");
                frame.add(proceed, BorderLayout.PAGE_END);

                proceed.addActionListener(e1 -> {
                    dispose();
                    frame.dispose();
                    new Payment(user, passengerName, customerID.getText(), contact.getText(), FlightNumberDisplay.getText(), emailDisplay.getText(), seatNumber.getText(), (Integer) getAge.getValue(), "F", flightPrice);

                    revalidate();
                });
            }
        }
    }

    private JEditorPane getjEditorPane() {
        JEditorPane details = new JEditorPane();
        details.setFont(new Font("Agency FB", Font.ITALIC, 20));
        details.setForeground(new Color(0x030f5b));
        details.setContentType("text/html");
        details.setEditable(false);
        details.setText("<html><h1 color='blue'> Confirm Details ! </h1> <hr>" +
                "<b>Passenger ID : </b>" + customerID.getText() + "<br>"
                + "<b>Flight Number : </b>" + FlightNumberDisplay.getText() + "<br>"
                + "<b>Passenger Name : </b>" + passengerName + "<br>"
                + "<b>Passenger Contact : </b>" + contact.getText() + "<br>"
                + "<b>Passenger E-mail : </b>" + emailDisplay.getText() + "<br>"
                + "<b>Age : </b>" + getAge.getValue() + "<br>"
                + "<b>Gender : </b>" + "M" + "<br>"
                + "<b>Seat Number : </b>" + seatNumber.getText() + "</html>");
        return details;
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            if (e.getSource() == ms || e.getSource() == mrs) {
                f.setSelected(true);
            } else if (e.getSource() == m) {
                mr.setSelected(true);
            } else if (e.getSource() == mr) {
                m.setSelected(true);
            }
        }
    }

}
