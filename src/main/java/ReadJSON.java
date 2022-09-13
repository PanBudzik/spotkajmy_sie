import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.*;
import java.util.ArrayList;

public class ReadJSON {

    public static String[] getCalendar(String filePath) throws IOException{
        BufferedReader br = null;
        String json = "";
        try{
            br = new BufferedReader(new FileReader(filePath));
            String str=null;
            while ((str=br.readLine())!=null){
                json+=str;
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if(br!=null)br.close();
        }

        JSONObject obj = new JSONObject(json);
        String beginning = obj.getJSONObject("working_hours").getString("start");
        String ending = obj.getJSONObject("working_hours").getString("end");

        ArrayList<String> calendar =  new ArrayList<String>();
        calendar.add(beginning);

        JSONArray arr = obj.getJSONArray("planned_meeting");
        for (int i = 0; i < arr.length(); i++) {
            String start = arr.getJSONObject(i).getString("start");
            calendar.add(start);
            String end = arr.getJSONObject(i).getString("end");
            calendar.add(end);
        }

        calendar.add(ending);

        String[] ArrayHoursCalendar1 = calendar.toArray(new String[0]);
        return ArrayHoursCalendar1;
    }
}
