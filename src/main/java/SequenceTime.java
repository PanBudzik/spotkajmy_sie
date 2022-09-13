import java.io.IOException;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SequenceTime {
    public static void main(String[] args) throws IOException {
        String[] ArrayHoursCalendar1 = ReadJSON.getCalendar("src/main/json/calendar1.json");
        String[] ArrayHoursCalendar2 = ReadJSON.getCalendar("src/main/json/calendar2.json");

        List<String[]> output = new ArrayList<String[]>();

        ArrayList<LocalTime> calendarTime1 =  new ArrayList<>();
        for(String time : ArrayHoursCalendar1){
            String[] separatedTime = time.split(":");
            calendarTime1.add(LocalTime.of(Integer.parseInt(separatedTime[0]),Integer.parseInt(separatedTime[1])));
        }

        ArrayList<LocalTime> calendarTime2 =  new ArrayList<>();
        for(String time : ArrayHoursCalendar2){
            String[] separatedTime = time.split(":");
            calendarTime2.add(LocalTime.of(Integer.parseInt(separatedTime[0]),Integer.parseInt(separatedTime[1])));
        }

        boolean threadHasEndFlag1 = true;
        boolean threadHasEndFlag2 = true;

        int timeBeingCheckedThread1=0;
        int timeBeingCheckedThread2=0;

        LocalTime lastChecked = null;
        int currentThread = 1;
        String timeNeeded = "00:30";

        LocalTime ref = LocalTime.parse("00:00");
        LocalTime meetingTime = LocalTime.parse(timeNeeded);
        Duration meetingDuration = Duration.between(ref,meetingTime);

        int checkTime = (calendarTime1.get(timeBeingCheckedThread1).compareTo(calendarTime2.get(timeBeingCheckedThread2)));
        if(checkTime < 0){
            lastChecked = calendarTime1.get(timeBeingCheckedThread1);
            currentThread = 1;
            if (calendarTime2.get(0)==calendarTime2.get(1))
            {
                threadHasEndFlag2 = false;
            }
        } else if (checkTime==0) {
            lastChecked = calendarTime1.get(timeBeingCheckedThread1);
            currentThread = 1;
            if (calendarTime2.get(0)==calendarTime2.get(1))
            {
                threadHasEndFlag2 = false;
            }
        } else{
            lastChecked = calendarTime2.get(timeBeingCheckedThread2);
            currentThread = 2;
            if(calendarTime1.get(0)==calendarTime1.get(1))
            {
                threadHasEndFlag1 = false;
            }
        }

        do{
            if(threadHasEndFlag1 && threadHasEndFlag2)
            {

                LocalTime startOfFreeTimeGap = lastChecked;
                LocalTime endOfFreeTimeGap = null;

                if(currentThread==1 &&
                        calendarTime2.get(timeBeingCheckedThread2).compareTo(calendarTime1.get(timeBeingCheckedThread1+1))<=0)
                {
                    endOfFreeTimeGap = calendarTime2.get(timeBeingCheckedThread2);
                }
                    else if(currentThread==2 &&
                        calendarTime1.get(timeBeingCheckedThread1).compareTo(calendarTime2.get(timeBeingCheckedThread2+1))<=0)
                {
                    endOfFreeTimeGap = calendarTime1.get(timeBeingCheckedThread1);

                }

                    if(endOfFreeTimeGap!=null) {
                        Duration gapDuration = Duration.between(startOfFreeTimeGap, endOfFreeTimeGap);

                        int isEnough = gapDuration.compareTo(meetingDuration);

                        if (isEnough >= 0) {

                            output.add(new String[] {startOfFreeTimeGap.toString(), endOfFreeTimeGap.toString()});
                            endOfFreeTimeGap = null;
                        }
                    }
            }

           if (currentThread==1){
               timeBeingCheckedThread1+=1;
               if(calendarTime1.size()>(timeBeingCheckedThread1)) {
                   checkTime = (calendarTime1.get(timeBeingCheckedThread1).compareTo(calendarTime2.get(timeBeingCheckedThread2)));
                   if (checkTime < 0) {
                       lastChecked = calendarTime1.get(timeBeingCheckedThread1);
                       currentThread = 1;
                       threadHasEndFlag1 = !threadHasEndFlag1;
                   } else if (checkTime == 0) {
                       lastChecked = calendarTime1.get(timeBeingCheckedThread1);
                       currentThread = 1;
                       threadHasEndFlag1 = !threadHasEndFlag1;
                   } else {
                       lastChecked = calendarTime2.get(timeBeingCheckedThread2);
                       currentThread = 2;
                       threadHasEndFlag2 = !threadHasEndFlag2;
                   }
               }
           } else if (currentThread==2) {
               timeBeingCheckedThread2+=1;
               if(calendarTime2.size()>(timeBeingCheckedThread2))
               {
                   checkTime = (calendarTime2.get(timeBeingCheckedThread2).compareTo(calendarTime1.get(timeBeingCheckedThread1)));
                   if(checkTime < 0){
                       lastChecked = calendarTime2.get(timeBeingCheckedThread2);
                       currentThread = 2;
                       threadHasEndFlag2 = !threadHasEndFlag2;
                   } else if (checkTime==0) {
                       lastChecked = calendarTime2.get(timeBeingCheckedThread2);
                       currentThread = 2;
                       threadHasEndFlag2 = !threadHasEndFlag2;
                   } else{
                       lastChecked = calendarTime1.get(timeBeingCheckedThread1);
                       currentThread = 1;
                       threadHasEndFlag1 = !threadHasEndFlag1;
                   }
               }
           }

        }while(calendarTime1.size()!=(timeBeingCheckedThread1) && calendarTime2.size()!=(timeBeingCheckedThread2));


        for (String[] row : output) {
            System.out.println(Arrays.toString(row));
        }
    }
}
