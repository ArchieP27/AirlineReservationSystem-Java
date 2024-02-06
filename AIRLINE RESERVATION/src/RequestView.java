import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RequestView extends JPanel {

    DefaultTableModel defaultTableModel;
    JTable requestView;
    JPanel tableDisplay;
    ResultSet resultSet;
        public RequestView(String user){
            setLayout(null);
            setOpaque(false);

            tableDisplay = new JPanel();
            tableDisplay.setOpaque(false);
            resultSet = LoginDB.getRequests(user);

            defaultTableModel = new DefaultTableModel();
            defaultTableModel.addColumn("Flight Number");
            defaultTableModel.addColumn("Passenger ID");
            defaultTableModel.addColumn("Request Type");
            defaultTableModel.addColumn("Field Modified");
            defaultTableModel.addColumn("Updated Value");
            defaultTableModel.addColumn("Request Status");
            defaultTableModel.addColumn("Admin Remarks");

            try{
                while (resultSet.next()){
                    defaultTableModel.addRow(new String[]{resultSet.getString(1),resultSet.getString(2),resultSet.getString(4),resultSet.getString(3),
                    resultSet.getString(5),resultSet.getString(7),resultSet.getString(6)});
                }
            }catch (SQLException se){}

            tableDisplay.setLayout(null);
            requestView = new JTable(defaultTableModel){
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            JScrollPane reqDisplay = new JScrollPane(requestView);
            reqDisplay.setBounds(30,220,940,260);
            tableDisplay.add(reqDisplay);
            tableDisplay.setBounds(100,100,1000,500);
            add(tableDisplay);

        }

}
