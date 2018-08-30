package in.org.ilugbom.attendance;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;

/**
 * Created by student on 8/28/18.
 */

public class DayMonthPickerDlg extends AppCompatActivity
{   //PickDayAndMonthAdapter PDMA;
    MainActivity MA;
    GridView g31;

    void SetMA(MainActivity MA){this.MA=MA;}

    void ShowDayMonthDailog() {

        final Dialog dialog = new Dialog(MA);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.day_month_picker);


        Button monthTitle =  dialog.findViewById(R.id.monthTitle);
        monthTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {  Msg.Show("Mid");
            }
        });

        ImageButton monthRight = (ImageButton) dialog.findViewById(R.id.monthRight);
        monthRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {  Msg.Show("Right");
            }
        });



/*
        Button monthRight =  dialog.findViewById(R.id.monthRight);
        monthRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {  Msg.Show("Right");
            }
        });
*/

        GridView gView= (GridView)dialog.findViewById(R.id.grid31);
        PickDayAndMonthAdapter PDMA = new PickDayAndMonthAdapter(MA);
        // gridView.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, mList));
        PDMA.SetDays(31);
        gView.setAdapter(PDMA);
        gView.setNumColumns(5);
        gView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialog.dismiss();
            }
        });


        dialog.show();



    }

}