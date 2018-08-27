package in.org.ilugbom.attendance;


/**
 * Created by Milind on 7/15/18.
 */

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by student on 7/11/18.
 */

public class Msg
{   private static  MainActivity MA;
    void SetMA(MainActivity MA){this.MA=MA;}
    static void show(String msg)
    {                  Toast.makeText(MA.getApplicationContext(), msg,Toast.LENGTH_SHORT).show();

    }

     static void Show(String msg)
    {// Get your custom_toast.xml ayout
        LayoutInflater inflater = MA.getLayoutInflater();

        View layout = inflater.inflate(R.layout.custom_toast,
                (ViewGroup) MA.findViewById(R.id.custom_toast_layout_id));
     // set image

     //   ImageView image = (ImageView) layout.findViewById(R.id.image);
     //   image.setImageResource(R.drawable.fab_blue_green72);
     // set a message
        TextView text = (TextView) layout.findViewById(R.id.text);
        text.setText(msg+" "); //give right padding one space

// Toast...
        Toast toast = new Toast(MA);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();


    }

    static void ImageMessage(String msg,int imageid)
    {// Get your custom_toast.xml ayout
        LayoutInflater inflater = MA.getLayoutInflater();

        View layout = inflater.inflate(R.layout.custom_toast,
                (ViewGroup) MA.findViewById(R.id.custom_toast_layout_id));
        // set image

        ImageView image = (ImageView) layout.findViewById(R.id.image);
        image.setImageResource(imageid);
        // set a message
        TextView text = (TextView) layout.findViewById(R.id.text);
        text.setText(msg+" "); //give padding right

// Toast...
        Toast toast = new Toast(MA);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();


    }




}
