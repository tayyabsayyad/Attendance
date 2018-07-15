package in.org.ilugbom.attendance;


/**
 * Created by Milind on 7/15/18.
 */

import android.widget.Toast;

/**
 * Created by student on 7/11/18.
 */

public class Msg
{private static  MainActivity MA;
    void SetMA(MainActivity MA){this.MA=MA;}
    static void show(String msg)
    {                  Toast.makeText(MA.getApplicationContext(), msg,Toast.LENGTH_SHORT).show();

    }

}
