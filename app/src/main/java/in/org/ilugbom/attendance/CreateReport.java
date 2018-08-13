package in.org.ilugbom.attendance;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by student on 8/13/18.
 */

public class CreateReport
{
    ArrayList<String> Divisions = new ArrayList<String>();

    void LoadHistory(String divtitle)   //// Load All Records of one division
    {
        String FileNameWithPath = "/sdcard/AttendanceData.atd";
        try {

            File myFile = new File(FileNameWithPath);
            FileInputStream fIn = new FileInputStream(myFile);
            BufferedReader myReader = new BufferedReader(new InputStreamReader(fIn));
            String aDataRow = "";

            Divisions.clear();

            String temp[];
            while ((aDataRow = myReader.readLine()) != null)
            {
                if (!aDataRow.contains("#")) continue;
                temp = aDataRow.split("#");
                if (temp.length < 4) continue;
                if(!temp[1].contains(divtitle)) continue;
                Divisions.add(temp[3]);
             }
            myReader.close();

            Msg.show(String.format("%d",Divisions.size()));
        }
        catch (Exception e)

        {
            Msg.show(e.getMessage());
            //    Toast.makeText(getBaseContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    void ShowReport(int index)
    {
        for(int i=0;i<Divisions.size();i++)
            Msg.show(Divisions.get(i));

    }

}
