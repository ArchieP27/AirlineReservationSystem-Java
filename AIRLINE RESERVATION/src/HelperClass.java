import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HelperClass {
        public static boolean isDateInFuture(String givenDate) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
            try {
                Date parsedDate = dateFormat.parse(givenDate);
                Date currentDate = new Date();
                return parsedDate.after(currentDate);
            } catch (ParseException e) {
                e.printStackTrace();
                return false;
            }
        }

    public static boolean isCurrentDate(String dateStr) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
        Date date;
        try {
            date = formatter.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }

        Date currentDate = new Date();
        return date != null && date.equals(currentDate);
    }

    public static boolean isDateInPast(String dateStr) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
        Date date;
        try {
            date = formatter.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }

        Date currentDate = new Date();
        return date != null && date.before(currentDate);
    }

}