import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class UserFunctionalities implements ItemListener,ActionListener{
JFrame frame = new JFrame("Search Tickets");
    private final String user;
    JPanel userWindow;
    JTable flightInfo;
    ResultSet resultSet;
    DefaultTableModel defaultTableModel;
    JComboBox options,options2,options3, cID = new JComboBox();
    JButton confirm;

    public UserFunctionalities(String user){
        this.user = user;
    }
    public JPanel updateFlights(){
        resultSet = LoginDB.searchFlights(user);
        userWindow = Design.createPanel(new Color(0x8DD2FF),new EtchedBorder(),null);
        options = new JComboBox();
        while (true) {
            try {
                if (!resultSet.next()) break;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                options.addItem(resultSet.getString("Flight_Number"));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
            JLabel flightNumber = Design.createLabel("Flight Number : ",Color.BLUE,30,Font.BOLD,"Candara");
            flightNumber.setBounds(140,90,260,35);
            userWindow.add(flightNumber);

            options.setBounds(370,85,200,35);
            userWindow.add(options);

        JLabel passengerID = Design.createLabel("Passenger ID: ",Color.BLUE,30,Font.BOLD,"Candara");
        passengerID.setBounds(650,90,260,35);
        userWindow.add(passengerID);

        cID.setBounds(850,85,200,40);
        userWindow.add(cID);

        JLabel fieldsModified = Design.createLabel("Fields : ",Color.BLUE,30,Font.BOLD,"Candara");
        fieldsModified.setBounds(290,170,260,35);
        userWindow.add(fieldsModified);

            JComboBox fields = new JComboBox(new String[]{"Passenger Name","Contact","E-mail","Age","Gender"});
            fields.setBounds(230,200,200,35);
            userWindow.add(fields);

        JLabel newVal = Design.createLabel("Updated Value : ",Color.BLUE,30,Font.BOLD,"Candara");
        newVal.setBounds(500,170,260,35);
        userWindow.add(newVal);
            JTextField value = Design.createTextField(Color.BLUE,20,Font.BOLD,"Candara");
            value.setBounds(500,200,200,35);
            userWindow.add(value);

            JButton change = Design.createButton("SUBMIT",Color.BLACK,20,Font.BOLD,"Candara",false,new EtchedBorder());
            change.setBounds(800,200,150,35);
            userWindow.add(change);

        DefaultTableModel changeModel = new DefaultTableModel();
        changeModel.addColumn("Flight Number");
        changeModel.addColumn("Passenger ID");
        changeModel.addColumn("Changed Field");
        changeModel.addColumn("Updated Value");
        JTable changesInfo = new JTable(changeModel);

        JScrollPane changeDisplay = new JScrollPane(changesInfo);
        changeDisplay.setBounds(110,300,1000,350);
        userWindow.add(changeDisplay);

        options.addItemListener(this);

            change.addActionListener(e -> {
                try {
                    if(value.getText().isEmpty() || cID.getSelectedItem()==null){
                        JOptionPane.showMessageDialog(userWindow, "Please fill all fields", "Insufficient Information", JOptionPane.ERROR_MESSAGE);
                    }
                    else {
                        LoginDB.createRequest((String) options.getSelectedItem(), (String) cID.getSelectedItem(), (String) Objects.requireNonNull(fields.getSelectedItem()), value.getText(), "Update");
                        changeModel.addRow(new Object[]{options.getSelectedItem(), cID.getSelectedItem(), fields.getSelectedItem(), value.getText()});
                        options.setSelectedIndex(0);
                        cID.removeAllItems();
                        fields.setSelectedIndex(0);
                        value.setText("");
                        JOptionPane.showMessageDialog(userWindow, "Update request generated !", "Update Requested", JOptionPane.INFORMATION_MESSAGE);
                        userWindow.revalidate();
                    }
                }catch (SQLException se){
                    JOptionPane.showMessageDialog(userWindow, "Request Failed !", "Update Unsuccessful", JOptionPane.ERROR_MESSAGE);
                }
            });

        return userWindow;
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if(e.getSource()==options) {
            resultSet = LoginDB.getPID((String) options.getSelectedItem(), user);
            userWindow.remove(cID);
            userWindow.revalidate();
            cID = new JComboBox();
            while (true) {
                try {
                    if (!resultSet.next()) break;
                    cID.addItem(resultSet.getString("Passenger_ID"));
                } catch (SQLException se) {
                    throw new RuntimeException(se);
                }
            }
            cID.setBounds(850, 85, 250, 40);
            userWindow.add(cID);
            userWindow.revalidate();
        }
        if(e.getSource()==options3){
            resultSet = LoginDB.getPID((String) options3.getSelectedItem(), user);
            frame.remove(cID);
            frame.revalidate();
            cID = new JComboBox();
            while (true) {
                try {
                    if (!resultSet.next()) break;
                    cID.addItem(resultSet.getString("Passenger_ID"));
                } catch (SQLException se) {
                    throw new RuntimeException(se);
                }
            }
            cID.setBounds(20, 70, 250, 30);
            frame.add(cID);
            frame.revalidate();
        }
        if (e.getSource()==options2){
            int choice = options2.getSelectedIndex();
            String date;

            defaultTableModel.setRowCount(0);
            resultSet = LoginDB.searchFlights(user);
            switch (choice) {
                case 0 -> {
                    try {
                        while (resultSet.next()) {
                            addRow(resultSet.getString("Passenger_ID"), resultSet.getString("Passenger_Name"), resultSet.getString("Contact")
                                    , resultSet.getString("EMail"), resultSet.getString("FormattedDate"), resultSet.getString("Departure_Airport.AirportName")
                                    , resultSet.getString("Arrival_Airport.AirportName"), resultSet.getString("Departure_time"), resultSet.getString("Arrival_time")
                                    , resultSet.getString("Seat_Number"), resultSet.getString("Price"));
                        }
                    } catch (SQLException se) {
                        throw new RuntimeException(se);
                    }
                }
                case 1 -> {
                    try {
                        while (resultSet.next()) {
                            date = resultSet.getString("FormattedDate");
                            if (HelperClass.isCurrentDate(date)) {
                                addRow(resultSet.getString("Passenger_ID"), resultSet.getString("Passenger_Name"), resultSet.getString("Contact")
                                        , resultSet.getString("EMail"), resultSet.getString("FormattedDate"), resultSet.getString("Departure_Airport.AirportName")
                                        , resultSet.getString("Arrival_Airport.AirportName"), resultSet.getString("Departure_time"), resultSet.getString("Arrival_time")
                                        , resultSet.getString("Seat_Number"), resultSet.getString("Price"));
                            }
                        }
                    } catch (SQLException se) {
                        throw new RuntimeException(se);
                    }
                }
                case 2 -> {
                    try {
                        while (resultSet.next()) {
                            date = resultSet.getString("FormattedDate");
                            if (HelperClass.isDateInPast(date)) {
                                addRow(resultSet.getString("Passenger_ID"), resultSet.getString("Passenger_Name"), resultSet.getString("Contact")
                                        , resultSet.getString("EMail"), resultSet.getString("FormattedDate"), resultSet.getString("Departure_Airport.AirportName")
                                        , resultSet.getString("Arrival_Airport.AirportName"), resultSet.getString("Departure_time"), resultSet.getString("Arrival_time")
                                        , resultSet.getString("Seat_Number"), resultSet.getString("Price"));
                            }
                        }
                    } catch (SQLException se) {
                        throw new RuntimeException(se);
                    }
                }
                case 3 -> {
                    try {
                        while (resultSet.next()) {
                            date = resultSet.getString("FormattedDate");
                            if (HelperClass.isDateInFuture(date)) {
                                addRow(resultSet.getString("Passenger_ID"), resultSet.getString("Passenger_Name"), resultSet.getString("Contact")
                                        , resultSet.getString("EMail"), resultSet.getString("FormattedDate"), resultSet.getString("Departure_Airport.AirportName")
                                        , resultSet.getString("Arrival_Airport.AirportName"), resultSet.getString("Departure_time"), resultSet.getString("Arrival_time")
                                        , resultSet.getString("Seat_Number"), resultSet.getString("Price"));
                            }
                        }
                    } catch (SQLException se) {
                        throw new RuntimeException(se);
                    }
                }
            }
            flightInfo.setModel(defaultTableModel);
            userWindow.revalidate();
            userWindow.repaint();
        }
    }


    public JPanel showFlights(){
        userWindow = new JPanel();
        userWindow.setLayout(null);
        userWindow.setOpaque(false);

        options2 = new JComboBox(new String[]{"All Flights", "Current Flights", "Past Flights","Upcoming Flights"});
        options2.setBounds(110,250,150,30);
        userWindow.add(options2);

        resultSet = LoginDB.searchFlights(user);
        defaultTableModel = new DefaultTableModel();
        addColumns("Passenger ID","Passenger Name","Contact","E-Mail","Date","Departure Airport","Arrival Airport","Departure Time","Arrival Time","Seat Number","Price");
        try{
            while(resultSet.next()){
                addRow(resultSet.getString("Passenger_ID"),resultSet.getString("Passenger_Name"),resultSet.getString("Contact")
                        ,resultSet.getString("EMail"),resultSet.getString("FormattedDate"),resultSet.getString("Departure_Airport.AirportName")
                        ,resultSet.getString("Arrival_Airport.AirportName"),resultSet.getString("Departure_time"),resultSet.getString("Arrival_time")
                        ,resultSet.getString("Seat_Number"),resultSet.getString("Price"));
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        flightInfo = new JTable(defaultTableModel){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JScrollPane tableDisplay = new JScrollPane(flightInfo);
        tableDisplay.setBounds(110,300,1000,200);
        userWindow.add(tableDisplay);

        options2.addItemListener(this);

        return userWindow;
    }

    private void addRow(String pID, String pName, String contact, String email, String date, String depAirport,String arrivalAirport, String departureTime, String arrivalTime, String sNo, String price) {
        defaultTableModel.addRow(new Object[]{pID,pName,contact,email,date,depAirport,arrivalAirport,departureTime,arrivalTime,sNo,price});
    }

    private void addColumns(String... columnNames) {
        for (String columnName : columnNames) {
            defaultTableModel.addColumn(columnName);
        }
    }

    public void searchTicktets(){
        ImageIcon imageIcon = new ImageIcon("ticketIcon.png");
        frame.setSize(300,200);
        frame.setResizable(false);
        frame.setIconImage(imageIcon.getImage());
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        resultSet = LoginDB.searchFlights(user);
        userWindow = Design.createPanel(new Color(0x8DD2FF),new EtchedBorder(),null);
        options3 = new JComboBox();
        while (true) {
            try {
                if (!resultSet.next()) break;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                options3.addItem(resultSet.getString("Flight_Number"));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        confirm = new JButton("CONFIRM");
        confirm.setBounds(100,120,100,30);
        frame.add(confirm);
        confirm.addActionListener(this);
        options3.addItemListener(this);
        options3.setBounds(20,30,250,30);
        cID = new JComboBox();
        cID.setBounds(20, 70, 250, 30);
        frame.add(cID);
        frame.add(options3);
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==confirm){
            frame.dispose();
            new ViewTicket((String) cID.getSelectedItem());
        }
    }

    public static void main(String[] args) {

        new UserFunctionalities("Archita").searchTicktets();
    }

}
