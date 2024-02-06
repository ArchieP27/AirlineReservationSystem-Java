import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class caterRequests extends JPanel implements ItemListener {

    JPanel reqPanel, changesPanel = new JPanel();
    DefaultTableModel defaultTableModel = new DefaultTableModel();
    JTable reqDisplay;
    JComboBox searchOptions, reqStatus;
    JLabel heading,flightNo,passengerID, remarks, reqStats, field, reqID;
    JTextField flightNoDisplay,passengerIDDisplay, adminRemarks, updatedValue;
    JButton confirm;
    ResultSet resultSet;
    String fno,pid, updatedField, rid;
    public caterRequests(){
        setLayout(null);
        setOpaque(false);

        reqPanel = Design.createPanel(new Color(0xA2432F),Design.createTitledBorder(new Color(0xEE9054),4," MANAGE REQUESTS ",new Color(0xEE9054),"Berlin Sans FB",Font.BOLD,30),null);
        reqPanel.setBounds(600,20,570,650);

        searchOptions = new JComboBox(new String[]{"All requests","Cancel Requests","Update Request"});
        searchOptions.setBounds(370,50,150,40);
        reqPanel.add(searchOptions);
        searchOptions.addItemListener(this);

        defaultTableModel.addColumn("Request ID");
        defaultTableModel.addColumn("Flight Number");
        defaultTableModel.addColumn("Passenger ID");
        defaultTableModel.addColumn("Request Type");
        defaultTableModel.addColumn("Request Status");
        defaultTableModel.addColumn("Field Updated");
        defaultTableModel.addColumn("Updated Value");

        resultSet = LoginDB.getRequests();
        try {
            while (resultSet.next()) {
                defaultTableModel.addRow(new Object[]{String.format("R%04d",resultSet.getInt("Request_ID"))
                        ,resultSet.getString("Flight_Number"), resultSet.getString("Passenger_ID")
                        ,resultSet.getString("RequestType"),resultSet.getString("Request_Status")});
            }
        }catch (SQLException se){
            se.printStackTrace();
        }

        defaultTableModel.setColumnCount(5);
        reqDisplay = new JTable(defaultTableModel){
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JScrollPane reqtabledisplay = new JScrollPane(reqDisplay);
        reqtabledisplay.setBounds(40,100,500,500);
        reqPanel.add(reqtabledisplay);
        reqDisplay.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount()==2){
                    Object[] row = new Object[7];
                    for (int i = 0; i < 5; i++) {
                        row[i] = reqDisplay.getValueAt(reqDisplay.getSelectedRow(), i);
                    }
                    try{
                        fno = (String)row[1];
                        pid = (String)row[2];
                        flightNoDisplay.setText(fno);
                        passengerIDDisplay.setText(pid);
                        rid = String.valueOf(row[0].toString().charAt(4));
                        reqID.setText("RID : "+row[0]);

                        if(searchOptions.getSelectedIndex()==2){
                            for (int i = 0; i < 7; i++) {
                                row[i] = reqDisplay.getValueAt(reqDisplay.getSelectedRow(), i);
                            }
                            updatedField =(String) row[5];
                            field.setText(updatedField+" : ");
                            updatedValue.setText((String) row[6]);
                        }
                    }catch (Exception ex){
                        JOptionPane.showMessageDialog(reqDisplay,"Please select some operation first!","Invalid Selection",JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        add(reqPanel);

    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if(e.getSource()==searchOptions){
            JPanel cover = new JPanel();
            cover.setOpaque(false);
            cover.setBounds(0,100,570,650);
            if(searchOptions.getSelectedIndex()==0){
                defaultTableModel.setColumnCount(5);
                defaultTableModel.setRowCount(0);
                resultSet = LoginDB.getRequests();
                try {
                    while (resultSet.next()) {
                        defaultTableModel.addRow(new Object[]{String.format("R%04d", resultSet.getInt("Request_ID"))
                                , resultSet.getString("Flight_Number"), resultSet.getString("Passenger_ID")
                                , resultSet.getString("RequestType"),resultSet.getString("Request_Status")});
                    }
                }catch (SQLException se){
                    se.printStackTrace();
                }
                reqDisplay.setModel(defaultTableModel);
                remove(changesPanel);
                revalidate();
                repaint();

                add(cover);
                revalidate();
            }
            if(searchOptions.getSelectedIndex()==1){

                defaultTableModel.setColumnCount(5);
                defaultTableModel.setRowCount(0);
                resultSet = LoginDB.getRequests();
                try {
                    while (resultSet.next()) {
                        String type = resultSet.getString("RequestType");
                        if(type.equals("Cancel")) {
                            defaultTableModel.addRow(new Object[]{String.format("R%04d", resultSet.getInt("Request_ID"))
                                    , resultSet.getString("Flight_Number"), resultSet.getString("Passenger_ID")
                                    ,type, resultSet.getString("Request_Status")});
                        }
                    }
                }catch (SQLException se){
                    se.printStackTrace();
                }
                reqDisplay.setModel(defaultTableModel);
                remove(changesPanel);
                revalidate();
                repaint();

                add(cover);
                revalidate();
                changesPanel = Design.createPanel(new Color(0xFF8A65),new LineBorder(new Color(0x5D4037),4),null);
                Font font = new Font("Candara",Font.BOLD,20);

                reqID = Design.createLabel("RID : ",new Color(0x5D4037),15,Font.BOLD,"Agency FB");
                reqID.setBounds(370,10,200,30);
                changesPanel.add(reqID);

                heading = Design.createLabel("CANCEL FLIGHT",new Color(0x5D4037),30,Font.BOLD,"Berlin Sans FB");
                heading.setBounds(110,50,250,30);
                changesPanel.add(heading);
                flightNo = Design.createLabel("Flight Number :",new Color(0x5D4037),font);
                flightNo.setBounds(80,120,150,30);
                flightNoDisplay = Design.createTextField(new Color(0xA2432F),font,false);
                flightNoDisplay.setBounds(230,120,150,30);
                changesPanel.add(flightNo);
                changesPanel.add(flightNoDisplay);

                passengerID = Design.createLabel("Passenger ID :",new Color(0x5D4037),font);
                passengerID.setBounds(80,180,150,30);
                passengerIDDisplay = Design.createTextField(new Color(0xA2432F),font,false);
                passengerIDDisplay.setBounds(230,180,150,30);
                changesPanel.add(passengerID);
                changesPanel.add(passengerIDDisplay);

                reqStats = Design.createLabel("Request Status :",new Color(0x5D4037),font);
                reqStats.setBounds(80,240,150,30);
                reqStatus = new JComboBox(new String[]{"Pending","Denied","Approved"});
                reqStatus.setBounds(230,240,150,30);
                changesPanel.add(reqStats);
                changesPanel.add(reqStatus);

                remarks = Design.createLabel("Admin Remarks : ",new Color(0x5D4037),font);
                remarks.setBounds(80,300,200,30);
                adminRemarks = Design.createTextField(new Color(0xA2432F),font);
                adminRemarks.setBounds(80,340,300,40);
                changesPanel.add(remarks);
                changesPanel.add(adminRemarks);

                confirm = Design.createButton("CONFIRM",new Color(0x5D4037),font,false,new EtchedBorder());
                confirm.setBounds(180,420,100,40);
                confirm.addActionListener(e1 -> {
                    LoginDB.alterRequest(Integer.parseInt(rid), (String) reqStatus.getSelectedItem(),adminRemarks.getText());
                    LoginDB.cancelFlight(passengerIDDisplay.getText());
                    reqID.setText("RID : ");
                    flightNoDisplay.setText("");
                    passengerIDDisplay.setText("");
                    reqStatus.setSelectedIndex(0);
                    adminRemarks.setText("");
                    revalidate();
                    JOptionPane.showMessageDialog(changesPanel,"Flight Cancelled Successfully !","CANCEL SUCCESS",JOptionPane.INFORMATION_MESSAGE);
                });
                changesPanel.add(confirm);

                changesPanel.setBounds(80,100,450,500);
                this.add(changesPanel);
                repaint();
                revalidate();
            }

            if(searchOptions.getSelectedIndex()==2){

                defaultTableModel.setColumnCount(7);
                defaultTableModel.setRowCount(0);
                resultSet = LoginDB.getRequests();
                try {
                    while (resultSet.next()) {
                        String type = resultSet.getString("RequestType");
                        if(type.equals("Update")) {
                            defaultTableModel.addRow(new Object[]{String.format("R%04d", resultSet.getInt("Request_ID"))
                                    , resultSet.getString("Flight_Number"), resultSet.getString("Passenger_ID")
                                    ,type,resultSet.getString("Request_Status"),resultSet.getString("FieldModified"),resultSet.getString("UpdatedValue")});
                        }
                    }
                }catch (SQLException se){
                    se.printStackTrace();
                }
                reqDisplay.setModel(defaultTableModel);
                remove(changesPanel);
                revalidate();
                repaint();

                add(cover);
                revalidate();

                changesPanel = Design.createPanel(new Color(0xFF8A65),new LineBorder(new Color(0x5D4037),4),null);
                Font font = new Font("Candara",Font.BOLD,20);

                reqID = Design.createLabel("RID : ",new Color(0x5D4037),15,Font.BOLD,"Agency FB");
                reqID.setBounds(370,10,200,30);
                changesPanel.add(reqID);

                heading = Design.createLabel("UPDATE FLIGHT",new Color(0x5D4037),30,Font.BOLD,"Berlin Sans FB");
                heading.setBounds(110,50,250,30);
                changesPanel.add(heading);
                flightNo = Design.createLabel("Flight Number :",new Color(0x5D4037),font);
                flightNo.setBounds(80,120,150,30);
                flightNoDisplay = Design.createTextField(new Color(0xA2432F),font,false);
                flightNoDisplay.setBounds(230,120,150,30);
                changesPanel.add(flightNo);
                changesPanel.add(flightNoDisplay);

                passengerID = Design.createLabel("Passenger ID :",new Color(0x5D4037),font);
                passengerID.setBounds(80,180,150,30);
                passengerIDDisplay = Design.createTextField(new Color(0xA2432F),font,false);
                passengerIDDisplay.setBounds(230,180,150,30);
                changesPanel.add(passengerID);
                changesPanel.add(passengerIDDisplay);

                reqStats = Design.createLabel("Request Status :",new Color(0x5D4037),font);
                reqStats.setBounds(80,240,150,30);
                reqStatus = new JComboBox(new String[]{"Pending","Denied","Approved"});
                reqStatus.setBounds(230,240,150,30);
                changesPanel.add(reqStats);
                changesPanel.add(reqStatus);

                field = Design.createLabel("Field : ",new Color(0x5D4037),font);
                updatedValue = Design.createTextField(new Color(0xA2432F),font,false);
                updatedValue.setBounds(230,300,150,30);
                field.setBounds(70,300,200,30);
                changesPanel.add(field);
                changesPanel.add(updatedValue);

                remarks = Design.createLabel("Admin Remarks : ",new Color(0x5D4037),font);
                remarks.setBounds(80,360,200,30);
                adminRemarks = Design.createTextField(new Color(0xA2432F),font);
                adminRemarks.setBounds(80,400,300,40);
                changesPanel.add(remarks);
                changesPanel.add(adminRemarks);

                confirm = Design.createButton("CONFIRM",new Color(0x5D4037),font,false,new EtchedBorder());
                confirm.setBounds(180,470,100,40);
                changesPanel.add(confirm);

                confirm.addActionListener(e12 -> {
                    LoginDB.alterRequest(Integer.parseInt(rid), (String) reqStatus.getSelectedItem(),adminRemarks.getText());
                    LoginDB.updateFlight(updatedField,updatedValue.getText(),passengerIDDisplay.getText());
                    reqID.setText("RID : ");
                    flightNoDisplay.setText("");
                    passengerIDDisplay.setText("");
                    reqStatus.setSelectedIndex(0);
                    adminRemarks.setText("");
                    revalidate();
                    JOptionPane.showMessageDialog(changesPanel,"Flight Updated Successfully !","UPDATE SUCCESS",JOptionPane.INFORMATION_MESSAGE);
                });

                changesPanel.setBounds(80,70,450,550);
                this.add(changesPanel);
                repaint();
                revalidate();
            }

        }

    }

}
