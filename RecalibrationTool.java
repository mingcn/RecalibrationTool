import com.rbnb.sapi.*;
import java.util.*;
import java.text.*;

public class RecalibrationTool {

    protected String dateAndTime, day, time;
    protected int duration;
    protected int i;
    protected int chosenTime;

    protected int [] potentialTimes;

  public static void main(String[] args) throws Exception
  {
    // scanner object for reading input
    Scanner s = new Scanner(System.in);

    RecalibrationTool rt = new RecalibrationTool();

    rt.printCurrentDateAndTime();

    rt.getExpectedCalibrationTimeRange(s);

    rt.findTimeRangeFromDT();

    rt.printPotentialCalibrationTimes();

    rt.sendCalibrationTimeToDT(s);

  }

  public RecalibrationTool()
  {

  }

  public void printCurrentDateAndTime()
  {
    // print the current date and time for user to see
    System.out.print("The current date and time is ");
    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    Calendar cal = Calendar.getInstance();
    System.out.println(dateFormat.format(cal.getTime()));

  }

  public void getExpectedCalibrationTimeRange(Scanner s)
  {
     // get the date and time of calibration
    System.out.print("Please enter the date of calibration (ex: 2014-02-25 10:12:11): ");
    day = s.next();
    time = s.next();

    //System.out.println(day + " " + time);

    dateAndTime = day + " " + time;

    SimpleDateFormat sdf  = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
    Date date = sdf.parse(dateAndTime);
    long epoch = date.getTime(); 

    // get the duration of calibration
    System.out.print("Please enter duration time for potential calibration times in minutes (ex: 60): ");
    duration = s.nextInt();

    //  convert duration to seconds
    duration = duration * 60;
  }

  public void findTimeRangeFromDT()
  {
    try 
    {
      Sink sink=new Sink();
      sink.OpenRBNBConnection();
 
       // Pull data from the server:
      ChannelMap rMap = new ChannelMap();
      rMap.Add("Something/TimeStamp");
      sink.Request(rMap, -10.0, 20.0, "absolute");
 
      //sink.CloseRBNBConnection();

      ChannelMap aMap;
        if ((aMap = sink.Fetch(-1)) == null) 
        {
            System.err.println("Data not received!");
            return;
        }
        System.out.println("Retrieved \""
                   +aMap.GetDataAsString(0)[0]
                   +"\" from server.");
        
        sink.CloseRBNBConnection();
       } 
     catch(SAPIException se) 
     { 
      se.printStackTrace(); 
     }
  }

  public void printPotentialCalibrationTimes()
  {
    System.out.println("Here are the times that were potentially calibrated at");

     potentialTimes = new int[10];
     for(i=0; i < potentialTimes.length; i++)
     {
        System.out.printf("#%d TimeStamp: 10:10:10 Vbatt: 5V Vtherm: 3V Vout: 3V Vrs: 1V \n", i);

     }
  }

  public void sendCalibrationTimeToDT(Scanner s)
  {
    System.out.print("Select one number: ");

    chosenTime = s.nextInt();


     // Send the chosen time to a new channel map
     try
     {
        Source source=new Source();
        source.OpenRBNBConnection("localhost:3333","HelloWorld");

        // Push data onto the server:
        System.out.println("Pushing data into server.");
        ChannelMap sMap = new ChannelMap();
        sMap.Add("HelloWorld");
        sMap.PutTimeAuto("timeofday");
        //sMap.PutDataAsString(0,date);
        source.Flush(sMap);
 
     }
     catch(SAPIException se) 
     {
        se.printStackTrace();
     }
  }

}
