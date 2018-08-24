package in.org.ilugbom.attendance;


/**
 * Created by student on 7/14/18.
 */

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Milind on 24/6/18.
 */

public class PickMonthAdapter extends BaseAdapter
{

    String[] numbers = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
    List<String> list = new ArrayList<String>();
    ArrayList<String> Divisions=new ArrayList<String>();
    Context context;


    void PickMonthAdapter()
    {
        list = new ArrayList<String>(Arrays.asList(numbers));
    }


    //TextAdapter.java


    public PickMonthAdapter(Context context)
    {
        this.context = context;
    }


    @Override
    public int getCount() {

        return numbers.length;
    }

    @Override
    public Object getItem(int position)
    {
        // TODO Auto-generated method stub
        return numbers[position];
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        TextView text = new TextView(this.context);
        text.setText(numbers[position]);
        text.setGravity(Gravity.CENTER);
      //  Integer tt=new Integer(position);
         // text.setBackgroundColor(context.getResources().getColor(R.color.colorTuch));
        text.setBackgroundColor(Color.parseColor("#fbdcbb"));

        text.setTextColor(Color.parseColor("#040404"));
        text.setTextSize(25);

        return text;
    }
}
