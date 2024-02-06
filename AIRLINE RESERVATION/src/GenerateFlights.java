import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class GenerateFlights {
    Random random = new Random();
    int hours, minutes, price;

    public GenerateFlights(String ArrCode, String DeptCode, String Date){
        if(!LoginDB.checkFlight(ArrCode,DeptCode,Date)){
            // Generate a random time difference between 1 and 3 hours
            int hoursDifference = random.nextInt(3) + 1;
            int minutesDifference = random.nextInt(60);
            for(int i = 1; i<=random.nextInt(3)+1;i++){
                hours = random.nextInt(24);
                minutes = random.nextInt(60);
                LocalTime departureTime = LocalTime.of(random.nextInt(24), random.nextInt(60));

                // Create arrival time based on the time difference
                LocalTime arrivalTime = departureTime.plusHours(hoursDifference).plusMinutes(minutesDifference);

                // Convert LocalTime to string format (HH:mm:ss)
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                String departureTimeString = departureTime.format(formatter);
                String arrivalTimeString = arrivalTime.format(formatter);

                price = random.nextInt(11000)+3000;

                LoginDB.insertFlight(DeptCode,ArrCode,Date,departureTimeString,arrivalTimeString,price);

            }
        }
    }

}
