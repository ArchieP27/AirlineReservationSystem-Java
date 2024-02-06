import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminFunctionalities implements ItemListener, ActionListener {
    static JPanel userWindow;
    static JTable flightInfo;
    static ResultSet resultSet;
    static DefaultTableModel defaultTableModel;
    static JComboBox options,options1;
    static JButton update;
    String Fno;
    static JTextField priceDisplay,dateDisplay,departureDisplay,arrivalDisplay,departureTimeDisplay,arrivalTimeDisplay;
    public JPanel updateFlights(){

        userWindow = new JPanel();
        userWindow.setLayout(null);
        userWindow.setOpaque(false);

        options1 = new JComboBox();
        resultSet = LoginDB.getFlightsId();
        while (true) {
            try {
                if (!resultSet.next()) break;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                options1.addItem(resultSet.getString("Flight_Number"));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        JPanel Flight_info =  Design.createPanel(new Color(0xEE9054),Design.createTitledBorder(new Color(0x543F48),4," FLIGHT DETAILS ",new Color(0xA2432F),"Berlin Sans FB",Font.ITALIC,30),null);
        Flight_info.setBounds(570,170,500,575);

        Font font = (new Font("Candara",Font.BOLD,25));

        JLabel fno = Design.createLabel("Flight Number : ",Color.WHITE,font);
        fno.setBounds(20,65,200,30);
        Flight_info.add(fno);
        options1.setFont(font);
        options1.setBounds(200,55,120,40);
        Flight_info.add(options1);

        options1.addItemListener(this);

        resultSet = LoginDB.getFlights(Fno);
        JLabel flightDate = Design.createLabel("Flight Date : ",Color.WHITE,font);
        flightDate.setBounds(20,125,250,30);
        Flight_info.add(flightDate);
        dateDisplay = Design.createTextField(font,true);
        dateDisplay.setBounds(200,115,115,40);

        JLabel departureAirport = Design.createLabel("Departure : ",Color.WHITE,font);
        JLabel arrivalAirport = Design.createLabel("Arrival : ",Color.WHITE,font);
        departureAirport.setBounds(20,195,300,30);
        Flight_info.add(departureAirport);
        departureDisplay =  Design.createTextField(font,false);
        departureDisplay.setHorizontalAlignment(SwingConstants.CENTER);
        departureDisplay.setBounds(40,230,430,40);
        arrivalAirport.setBounds(20,290,300,30);
        Flight_info.add(arrivalAirport);
        arrivalDisplay =  Design.createTextField(font,false);
        arrivalDisplay.setHorizontalAlignment(SwingConstants.CENTER);
        arrivalDisplay.setBounds(40,325,430,40);

        JLabel departureTime = Design.createLabel("Departure time : ",Color.WHITE,25,Font.BOLD,"Candara");
        departureTime.setBounds(50,390,300,30);
        Flight_info.add(departureTime);
        departureTimeDisplay =  Design.createTextField(font,true);
        departureTimeDisplay.setHorizontalAlignment(SwingConstants.CENTER);
        departureTimeDisplay.setBounds(80,420,100,45);

        JLabel arrivalTime = Design.createLabel("Arrival time : ",Color.WHITE,25,Font.BOLD,"Candara");
        arrivalTime.setBounds(290,390,300,30);
        Flight_info.add(arrivalTime);
        arrivalTimeDisplay = Design.createTextField(font,true);
        arrivalTimeDisplay.setHorizontalAlignment(SwingConstants.CENTER);
        arrivalTimeDisplay.setBounds(310,420,100,45);

        JLabel price = Design.createLabel("Price : ",Color.WHITE,25,Font.BOLD,"Candara");
        price.setBounds(130,510,150,30);
        Flight_info.add(price);
        priceDisplay= Design.createTextField(font,true);
        priceDisplay.setHorizontalAlignment(0);
        priceDisplay.setBounds(220,495,140,45);

        Flight_info.add(departureDisplay);
        Flight_info.add(arrivalDisplay);
        Flight_info.add(departureTimeDisplay);
        Flight_info.add(arrivalTimeDisplay);
        Flight_info.add(dateDisplay);
        Flight_info.add(priceDisplay);

        update = Design.createButton("UPDATE",new Color(0x543F48),20,Font.BOLD,"Agency FB");
        update.setBounds(200,570,100,40);
        update.addActionListener(this);
        Flight_info.add(update);

        Flight_info.setBounds(370,30,500,650);
        userWindow.add(Flight_info);

        return userWindow;
    }

    public JPanel showFlights(){
        userWindow = Design.createPanel(new Color(0xEE9054),new LineBorder(new Color(0x543F48),4),null);

        options = new JComboBox(new String[]{"All Flights", "Current Flights", "Past Flights","Upcoming Flights"});
        options.setBounds(130,110,150,30);
        options.addItemListener(this);
        userWindow.add(options);

        defaultTableModel = new DefaultTableModel();
        addColumns("Flight_Number","Departure_Code","Arrival_Code","Flight_date","Departure Time","Arrival Time","Price","Total Booking","Total Sales");
        resultSet = LoginDB.displayAllFLight();
        try{
            while(resultSet.next()){
                addRow(resultSet.getString("Flight_number"),resultSet.getString("Departure_Airport_Code"),resultSet.getString("Arrival_Airport_Code")
                        ,resultSet.getString("FormattedDate"),resultSet.getString("Departure_time"),resultSet.getString("Arrival_time")
                        ,resultSet.getString("Price"),resultSet.getString("Bookings"),resultSet.getString("Total_Sales"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        flightInfo = new JTable(defaultTableModel);
        JScrollPane tableDisplay = new JScrollPane(flightInfo);
        tableDisplay.setBounds(130,160,950,400);
        userWindow.add(tableDisplay);

        return userWindow;
    }

    private static void addRow(String fno, String dcode, String acode, String date, String dtime, String atime, String price, String bookings, String totalCost) {
        defaultTableModel.addRow(new Object[]{fno,dcode,acode,date,dtime,atime,price,bookings,totalCost});
    }

    private static void addColumns(String... columnNames) {
        for (String columnName : columnNames) {
            defaultTableModel.addColumn(columnName);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==update){
            try {
                LoginDB.updateFlightInfo(departureTimeDisplay.getText(),arrivalTimeDisplay.getText(),dateDisplay.getText(),priceDisplay.getText(), (String) options1.getSelectedItem());
                departureDisplay.setText("");
                departureTimeDisplay.setText("");
                arrivalDisplay.setText("");
                arrivalTimeDisplay.setText("");
                dateDisplay.setText("");
                priceDisplay.setText("");
                options1.setSelectedIndex(0);
                userWindow.revalidate();
                JOptionPane.showMessageDialog(userWindow,"Successfully updated information !","Update Success",JOptionPane.INFORMATION_MESSAGE);
            }catch (SQLException se){
                JOptionPane.showMessageDialog(userWindow,"Please Check all entered fields correctly!","Update Failed",JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if(e.getSource()==options){
            int choice = options.getSelectedIndex();
            String date;

            defaultTableModel.setRowCount(0);
            resultSet = LoginDB.displayAllFLight();
            switch (choice) {
                case 0:
                    try {
                        while (resultSet.next()) {
                            addRow(resultSet.getString("Flight_number"), resultSet.getString("Departure_Airport_Code"), resultSet.getString("Arrival_Airport_Code")
                                    , resultSet.getString("FormattedDate"), resultSet.getString("Departure_time"), resultSet.getString("Arrival_time")
                                    , resultSet.getString("Price"), resultSet.getString("Bookings"), resultSet.getString("Total_Sales"));
                        }
                    } catch (SQLException se) {
                        throw new RuntimeException(se);
                    }
                    break;
                case 1:
                    try {
                        while (resultSet.next()) {
                            date = resultSet.getString("FormattedDate");
                            if (HelperClass.isCurrentDate(date)) {
                                addRow(resultSet.getString("Flight_number"), resultSet.getString("Departure_Airport_Code"), resultSet.getString("Arrival_Airport_Code")
                                        , date, resultSet.getString("Departure_time"), resultSet.getString("Arrival_time")
                                        , resultSet.getString("Price"), resultSet.getString("Bookings"), resultSet.getString("Total_Sales"));
                            }
                        }
                    } catch (SQLException se) {
                        throw new RuntimeException(se);
                    }
                    break;
                case 2:
                    try {
                        while (resultSet.next()) {
                            date = resultSet.getString("FormattedDate");
                            if (HelperClass.isDateInPast(date)) {
                                addRow(resultSet.getString("Flight_number"), resultSet.getString("Departure_Airport_Code"), resultSet.getString("Arrival_Airport_Code")
                                        , date, resultSet.getString("Departure_time"), resultSet.getString("Arrival_time")
                                        , resultSet.getString("Price"), resultSet.getString("Bookings"), resultSet.getString("Total_Sales"));
                            }
                        }
                    } catch (SQLException se) {
                        throw new RuntimeException(se);
                    }
                    break;
                case 3:
                    try {
                        while (resultSet.next()) {
                            date = resultSet.getString("FormattedDate");
                            if (HelperClass.isDateInFuture(date)) {
                                addRow(resultSet.getString("Flight_number"), resultSet.getString("Departure_Airport_Code"), resultSet.getString("Arrival_Airport_Code")
                                        , date, resultSet.getString("Departure_time"), resultSet.getString("Arrival_time")
                                        , resultSet.getString("Price"), resultSet.getString("Bookings"), resultSet.getString("Total_Sales"));
                            }
                        }
                    } catch (SQLException se) {
                        throw new RuntimeException(se);
                    }
                    break;
            }
            flightInfo.setModel(defaultTableModel);
            userWindow.revalidate();
            userWindow.repaint();
        }

        if(e.getSource()==options1){
            resultSet = LoginDB.getFlightDetails((String) options1.getSelectedItem());
            try {
                if(resultSet.next()) {
                    dateDisplay.setText(resultSet.getString("Flight_date"));
                    departureDisplay.setText(resultSet.getString(2));
                    arrivalDisplay.setText(resultSet.getString(3));
                    departureTimeDisplay.setText(resultSet.getString(5));
                    arrivalTimeDisplay.setText(resultSet.getString(6));
                    priceDisplay.setText(resultSet.getString(7));
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }

        }
    }

}
