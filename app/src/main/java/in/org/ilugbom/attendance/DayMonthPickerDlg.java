package in.org.ilugbom.attendance;

import android.app.Dialog;
import android.content.Context;
import android.widget.GridView;

/**
 * Created by student on 8/28/18.
 */

public class DayMonthPickerDlg
{   PickDayAndMonthAdapter PDMA;
    MainActivity MA;
    GridView g31;

    void SetMA(MainActivity MA){this.MA=MA;}
    void Prepare()
    {
        PDMA = new PickDayAndMonthAdapter(MA);

        g31 = (GridView) MA.findViewById(R.id.grid31);
        //PDMA.SetDays(31);
        g31.setAdapter(PDMA);


    }

    public void showDialog(final Context context) {
        final Dialog dialog = new Dialog(context);
        //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.day_month_picker);
        dialog.show();

    }
}