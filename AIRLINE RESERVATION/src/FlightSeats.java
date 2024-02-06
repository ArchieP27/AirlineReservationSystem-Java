import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.sql.ResultSet;

public class FlightSeats {

    public JTable seatInfo;
    ResultSet resultSet;

    public JTable displaySeats(String Flight_Number) {
        String[] Heading = {"A", "B", "C", "", "D", "E", "F"};
        String[][] data = new String[33][7];
        seatInfo = new JTable(data, Heading) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        seatInfo.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component cellComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (isSelected) {
                    cellComponent.setBackground(Color.WHITE); // Set the color for the selected cell
                } else {
                    cellComponent.setBackground(Color.WHITE); // Set the default color for non-selected cells
                }
                return cellComponent;
            }
        });

        for (int i = 1; i <= 33; i++) {
            for (int j = 1; j <= 7; j++) {
                if (j > 4) {
                    data[i - 1][j - 1] = String.valueOf(i) + (char) ('A' + (j - 2));
                } else if (j < 4) {
                    data[i - 1][j - 1] = String.valueOf(i) + (char) ('A' + (j - 1));
                }
            }
        }

        resultSet = LoginDB.getReservedSeats(Flight_Number);
        if (resultSet != null) {
            try {
                while (resultSet.next()) {
                    String cell = resultSet.getString("Seat_Number");
                    int[] cellIndex = getCellIndexForValue(cell);
                    assert cellIndex != null;
                    seatInfo.setValueAt("NA", cellIndex[0], cellIndex[1]);
                }
            } catch (Exception ignored) {
            }
        }
        return seatInfo;
    }
    private int[] getCellIndexForValue(String value) {
        for (int row = 0; row < seatInfo.getRowCount(); row++) {
            for (int col = 0; col < seatInfo.getColumnCount(); col++) {
                Object cellValue = seatInfo.getValueAt(row, col);
                if (cellValue != null && cellValue.toString().equals(value)) {
                    return new int[]{row, col};
                }
            }
        }
        return null;
    }
}