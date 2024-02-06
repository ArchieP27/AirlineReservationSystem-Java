import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.YearMonth;
import java.util.Objects;

public class SearchFlights extends JPanel implements ActionListener {
    String[] airportCode = {"DEL", "BOM", "BLR", "CCU", "MAA", "HYD", "AMD", "PNQ", "GOI"};
    String[] airports = {"Delhi", "Bombay", "Bangalore", "Kolkata", "Chennai", "Hyderabad", "Ahmedabad", "Pune", "Goa"};
    JLabel from, to, date;
    JComboBox fromLocation = new JComboBox(airports), toLocation = new JComboBox(airports);
    JTextField extractDate;
    JPanel searchPanel, display = new JPanel();
    JButton search, ok;
    ResultSet resultSet;
    String user;
    Font font = new Font("Candara",Font.BOLD,25);
    private JTable calendarTable;
    private JComboBox<String> monthComboBox;
    private JComboBox<String> yearComboBox;
    private DateSelectionListener dateSelectionListener;

    public SearchFlights(String user){
        this.user = user;
        setOpaque(false);
        setLayout(null);

        searchPanel = Design.createPanel(new Color(0x030f5b),Design.createTitledBorder(Color.WHITE,4," SEARCH FLIGHTS ",Color.CYAN,"Berlin Sans FB",Font.BOLD,30));
        searchPanel.setBounds(20,20,570,690);
        searchPanel.setLayout(null);

        from = Design.createLabel("FROM : ",Color.WHITE,font);
        from.setBounds(24,375,120,30);
        searchPanel.add(from);

        fromLocation.setBounds(117,370,170,30);
        searchPanel.add(fromLocation);

        to = Design.createLabel("TO : ",Color.WHITE,font);
        to.setBounds(315,375,50,30);
        searchPanel.add(to);

        toLocation.setBounds(370,370,170,30);
        searchPanel.add(toLocation);

        date = Design.createLabel("DATE : ",Color.WHITE,font);
        date.setBounds(30,95,80,30);
        searchPanel.add(date);

        extractDate = Design.createTextField(new Color(0x030f5b),20,Font.BOLD,"Agency FB",false);
        extractDate.setBounds(105,92,200,30);
        extractDate.setHorizontalAlignment(JTextField.CENTER);
        searchPanel.add(extractDate);

        JPanel calendar;
        calendar = getCalendarPanel();
        calendar.setBounds(40,155,500,153);
        calendar.setBackground(new Color(0x030f5b));
        searchPanel.add(calendar);

        calendarTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount()==2){
                    extractDate.setText(getSelectedDate());
                }
            }
        });

        ok = Design.createButton("SELECT",new Color(0x030f5b),20,Font.BOLD,"Agency FB",false, new EtchedBorder());
        ok.setBounds(410,92,120,30);
        searchPanel.add(ok);
        ok.addActionListener(e -> extractDate.setText(getSelectedDate()));

        search = Design.createButton("SEARCH",new Color(0x030f5b),30,Font.BOLD,"Agency FB",false, new EtchedBorder());
        search.setBounds(220,600,150,50);
        search.addActionListener(this);
        searchPanel.add(search);

        display.setBounds(620,20,570,690);
        display.setLayout(new BoxLayout(display, BoxLayout.Y_AXIS));
        display.setOpaque(false);

        add(searchPanel);
        add(display);

    }

    @Override
    public void actionPerformed(ActionEvent e) {

            if (e.getSource() == search) {
                String date = extractDate.getText();
                int toIndex = toLocation.getSelectedIndex();
                int fromIndex = fromLocation.getSelectedIndex();
                String arrivalLoc = airportCode[toIndex];
                String deptLoc = airportCode[fromIndex];
                display.removeAll();

                if(date.isEmpty()){
                    JOptionPane.showMessageDialog(this,"Please Fill All Fields","Search Error",JOptionPane.ERROR_MESSAGE);
                }
                else{
                if(HelperClass.isDateInFuture(date) && !Objects.equals(arrivalLoc, deptLoc)) {

                    new GenerateFlights(arrivalLoc, deptLoc, date);
                    resultSet = LoginDB.getFlights(deptLoc, arrivalLoc, date);

                    try {
                        while (resultSet.next()) {
                            JPanel panel;

                            String Flight_number = resultSet.getString("Flight_number");
                            String departureTime = resultSet.getString("Departure_time");
                            String arrivalTime = resultSet.getString("Arrival_time");
                            int price = resultSet.getInt("Price");

                            panel = new DisplaySearches().displayFlights(user, arrivalLoc, deptLoc, date, Flight_number, departureTime, arrivalTime, price);
                            panel.setMaximumSize(new Dimension(600, 200));
                            panel.setMinimumSize(new Dimension(600, 200));
                            display.add(panel);
                            display.add(Box.createVerticalStrut(30));

                            display.revalidate();
                            display.repaint();

                            setVisible(true);
                        }
                    } catch (SQLException se) {
                        throw new RuntimeException(se);
                    }
                }
                else{
                    JPanel panel = Design.createImageBackground("flightUnavailable.jpg");
                    panel.setMinimumSize(display.getSize());
                    display.add(panel);
                    display.revalidate();
                    display.repaint();
                }

                extractDate.setText("");
                toLocation.setSelectedIndex(0);
                fromLocation.setSelectedIndex(0);

            }
        }
    }


    public JPanel getCalendarPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Month and Year selection
        JPanel selectionPanel = new JPanel();
        String[] months = new String[]{"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        monthComboBox = new JComboBox<>(months);
        monthComboBox.addActionListener(e -> updateCalendar());
        selectionPanel.add(monthComboBox);

        int currentYear = java.time.LocalDate.now().getYear();
        String[] years = generateYears(currentYear - 20, currentYear + 20);
        yearComboBox = new JComboBox<>(years);
        yearComboBox.setSelectedItem(String.valueOf(currentYear));
        yearComboBox.addItemListener(e -> updateCalendar());
        selectionPanel.add(yearComboBox);
        selectionPanel.setBackground(new Color(0x030f5b));
        panel.add(selectionPanel, BorderLayout.NORTH);

        // Calendar
        calendarTable = new JTable(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JScrollPane scrollPane = new JScrollPane(calendarTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        updateCalendar();

        calendarTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selectedDate = getSelectedDate();
                if (dateSelectionListener != null) {
                    dateSelectionListener.dateSelected(selectedDate);
                }
            }
        });

        return panel;
    }

    public String getSelectedDate() {
        int selectedRow = calendarTable.getSelectedRow();
        int selectedColumn = calendarTable.getSelectedColumn();

        if (selectedRow != -1 && selectedColumn != -1) {
            Object value = calendarTable.getValueAt(selectedRow, selectedColumn);
            if (value instanceof Integer) {
                int day = (int) value;
                String month = ((String) Objects.requireNonNull(monthComboBox.getSelectedItem())).substring(0,3);
                int year = Integer.parseInt((String) Objects.requireNonNull(yearComboBox.getSelectedItem()));
                return day+"-"+month+"-"+year;
            }
        }
        return "01-Jan-2023";
    }


    private String[] generateYears(int startYear, int endYear) {
        String[] years = new String[endYear - startYear + 1];
        for (int i = 0; i <= endYear - startYear; i++) {
            years[i] = String.valueOf(startYear + i);
        }
        return years;
    }

    private void updateCalendar() {
        int monthIndex = monthComboBox.getSelectedIndex();
        int currentYear = Integer.parseInt((String) Objects.requireNonNull(yearComboBox.getSelectedItem()));

        YearMonth yearMonthObject = YearMonth.of(currentYear, monthIndex + 1);
        int daysInMonth = yearMonthObject.lengthOfMonth();
        int startDay = yearMonthObject.atDay(1).getDayOfWeek().getValue();

        DefaultTableModel model = new DefaultTableModel(new String[]{"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"}, 0);

        int row = 0;
        int day = 1;
        model.addRow(new Object[7]);
        for (int i = 1; i <= 42; i++) {
            if (i >= startDay && day <= daysInMonth) {
                model.setValueAt(day, row, (i - 1) % 7);
                day++;
            } else {
                model.setValueAt("", row, (i - 1) % 7);
            }
            if (i % 7 == 0 && i < 42) {
                row++;
                model.addRow(new Object[7]);
            }
        }

        calendarTable.setModel(model);
    }

}

interface DateSelectionListener {
    void dateSelected(String date);
}