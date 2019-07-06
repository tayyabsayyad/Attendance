package in.org.ilugbom.attendance;

import android.content.Context;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Milind on 7/3/18.
 */


public class Model
{
    ArrayList<String> Divisions = new ArrayList<String>();
    ArrayList<String> DateArray = new ArrayList<String>();
    int currentindex=0;


    String GetDivisionTitle(int index)
    {
        String temp[];
        temp = Divisions.get(index).split("#");
        return temp[0];

    }

    String GetRollStartFinish(int index)
    {
        String temp[];
        temp = Divisions.get(index).split("#");
        return temp[1];
    }



    // default added here
    // Add devision here with the missing numbers

    void LoadDivisions() {
        Divisions.add("IX-A#5001-5120#P");
        Divisions.add("XI-C#321-400#P");
        Divisions.add("TY-C#721-800#P");
    }


    void SaveList(String AttendanceLine)
    {
        int i;
        String txtData = "";
        //  modified=false;
        //  String tmpStr;
        boolean newfile=false;
        String FileNameWithPath = "/sdcard/AttendanceData.atd";
        try {
            File myFile = new File(FileNameWithPath);
            if (!myFile.exists()) { newfile=true; myFile.createNewFile();}
            else newfile=false;
            FileOutputStream fOut = new FileOutputStream(myFile,true);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);

            if(newfile)
            {  String RString = "======== Reserved Line 1 =======\n";
                RString+= "======== Reserved Line 2 =======\n";
                myOutWriter.append(RString);
            }
            txtData += GetDateTimeString();
            txtData +="#";
            txtData += GetDivisionTitle(MainActivity.currentDivision);
            txtData += "#";
            txtData += GetRollStartFinish(MainActivity.currentDivision);
            txtData += "#";
            txtData += AttendanceLine;
            txtData += "\n";

            myOutWriter.append(txtData);
            myOutWriter.close();
            fOut.close();
            Msg.Show("Attendance Saved");

        } catch (Exception e)
        {
            Msg.show(e.getMessage());
        }
    }

    String GetDateTimeString()
    {   String date, dow, time;
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df1 = new SimpleDateFormat("dd/MM/yy");
        SimpleDateFormat df2 = new SimpleDateFormat("E");
        SimpleDateFormat df3 = new SimpleDateFormat("HH:mm");
        date = df1.format(c.getTime());
        dow = df2.format(c.getTime());
        time = df3.format(c.getTime());
        return (date + " - " + dow + " - " + time);
    }




    void LoadHistory()   //// Load History = Load All Records
    {
        String FileNameWithPath = "/sdcard/AttendanceData.atd";
        try {

            File myFile = new File(FileNameWithPath);
            FileInputStream fIn = new FileInputStream(myFile);
            BufferedReader myReader = new BufferedReader(new InputStreamReader(fIn));
            String aDataRow = "";

            Divisions.clear();
            DateArray.clear();
            String temp[];
            while ((aDataRow = myReader.readLine()) != null)

            {
                if (!aDataRow.contains("#")) continue;
                temp = aDataRow.split("#");
                if (temp.length < 4) continue;
                DateArray.add(temp[0]);
                Divisions.add(temp[1] + "#" + temp[2] + "#" + temp[3]);
            }
            myReader.close();

            Msg.Show("History Mode On");
        }
        catch (Exception e)

        {
            Msg.show(e.getMessage());
            //    Toast.makeText(getBaseContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }


    void SaveHistory()
    {
        int i;
        String txtData = "";
        //  modified=false;
        //  String tmpStr;
        String FileNameWithPath = "/sdcard/AttendanceData.atd";
        try {
            File myFile = new File(FileNameWithPath);
            myFile.createNewFile();
            FileOutputStream fOut = new FileOutputStream(myFile);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);

            String RString = "======== Reserved Line 1 =======\n";
                   RString+= "======== Reserved Line 2 =======\n";
                   myOutWriter.append(RString);

                   for(i=0;i<DateArray.size();i++)
                   {txtData+=DateArray.get(i);
                    txtData+="#";
                    txtData+=Divisions.get(i);
                    txtData+="\n";
                   }

            myOutWriter.append(txtData);
            myOutWriter.close();
            fOut.close();
            Msg.Show("History Saved");


        } catch (Exception e)
        {
            Msg.show(e.getMessage());
        }
    }



}  // end Model class
