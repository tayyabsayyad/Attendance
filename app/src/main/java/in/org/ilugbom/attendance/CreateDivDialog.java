package in.org.ilugbom.attendance;

/**
 * Created by Milind on 7/8/18.
 *
 * This class is used to create Division
 * It useses Shared preferences
 *
 * Following methods are implemented
 *
 * 1. showDialog()
 * 2. showPreferenceDialog
 * 3. SaveDivisionsInPrefs
 * 4. LoadDivisionsFromPrefs
 *
 * */

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CreateDivDialog
{
    public String tag = "CreateDivDialog";
    public String message = "";


    Model model;
    private MainActivity MA;

    public String tempDivTitle,tempFroll,tempLroll,tempMissingRoll;

    String college="School/College";
    String teacher="Name";
    String subject="Subject";
    String email="Email";

    boolean editmode=false;  /// if true Record is refreshed, if false it added to division array

    void SetRef(Model model){
        this.model=model;
    }

    void SetMA(MainActivity MA){
        this.MA=MA;
    }


    public void showDialog(final Context context){


        // Create Dialog box for creating division

        final Dialog dialog = new Dialog(context);
        if(editmode) dialog.setTitle("Edit Division");
        else dialog.setTitle("Add New Divisoin");
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.create_div_dlg);
        dialog.show();


        final EditText ClassDiv = dialog.findViewById(R.id.division);
        final EditText FirstRoll = dialog.findViewById(R.id.firstroll);
        final EditText LastRoll = dialog.findViewById(R.id.lastroll);
        final EditText MissingRoll = dialog.findViewById(R.id.missingroll);

        if(editmode) { // if editmode =true offer current values
            ClassDiv.setText(tempDivTitle);
            FirstRoll.setText(tempFroll);
            LastRoll.setText(tempLroll);
            MissingRoll.setText(tempMissingRoll);
        }

        Button mBtn_cancel = dialog.findViewById(R.id.btn_cancel);
        Button mBtn_ok = dialog.findViewById(R.id.btn_ok);

        mBtn_cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        mBtn_ok.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                String frollstring=FirstRoll.getText().toString();
                String lrollstring=LastRoll.getText().toString();
                String mrollstring=MissingRoll.getText().toString();
                String classdiv=ClassDiv.getText().toString();

                if(frollstring.length()==0 || lrollstring.length()==0)
                {
                    Msg.Show("Invlid Roll"); return;
                }

                int  in1 = new Integer(frollstring);
                int  in2 = new Integer(lrollstring);

                int strength=in2-in1+1;

                if(strength>500) Msg.show("Class Strength > 500");

                if(classdiv.length()==0) Msg.show("Class-Division Empty");

                Msg.Show(String.format("Strength - %d",strength));

               message = mrollstring;
               Log.d(tag, message);

                if(editmode) ///if true add new division to Divisions Array
                {
                    // model.Divisions.set(MainActivity.currentDivision, classdiv + "#" + frollstring + "-" + lrollstring + "#" + "PP");
                    model.Divisions.set(MainActivity.currentDivision, classdiv + "#" + frollstring + "-" + lrollstring + "#" + mrollstring +"#"+ "PP");
                }
                else
                { /// else replace new division with current division

                    // model.Divisions.add(classdiv + "#" + frollstring + "-" + lrollstring + "#" + "PP");
                    model.Divisions.add(classdiv + "#" + frollstring + "-" + lrollstring + "#" + mrollstring + "#" + "PP");

                    MainActivity.currentDivision = model.Divisions.size() - 1;

                    //Log.d(tag, classdiv + "#" + frollstring + "-" + lrollstring + "#" + "PP");
                    Log.d(tag, classdiv + "#" + frollstring + "-" + lrollstring + "#" + mrollstring + "#" + "PP");
                }
                MA.DisplayDivision();
                SaveDivisionsInPrefs();
                dialog.dismiss();
            }
        });
    }

    public void showPreferenceDialog(final Context context){
        final Dialog dialog = new Dialog(context);
        dialog.setTitle("Set Preferences");
        //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.preferences_dialog);
        dialog.show();

        final EditText etCollege = dialog.findViewById(R.id.college);
        final EditText etTeacher = dialog.findViewById(R.id.teacher);
        final EditText etSubject = dialog.findViewById(R.id.subject);
        final EditText etEmail = dialog.findViewById(R.id.email);

        /// set current values
        etCollege.setText(college);
        etTeacher.setText(teacher);
        etSubject.setText(subject);
        etEmail.setText(email);
        ////////
        Button mBtn_cancel = dialog.findViewById(R.id.btn_cancel);
        Button mBtn_ok = dialog.findViewById(R.id.btn_ok);

        mBtn_cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        mBtn_ok.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                college=etCollege.getText().toString();
                teacher=etTeacher.getText().toString();
                subject=etSubject.getText().toString();
                email=etEmail.getText().toString();

                // Need to add code for the Email Validation !!!!!!!!!!

                 SaveDivisionsInPrefs();
                dialog.dismiss();
            }
        });
    }


    void SaveDivisionsInPrefs()
    {
        String alldivisions=model.Divisions.get(0);

        if(model.Divisions.size()>1)
        {
            for(int i=1;i< model.Divisions.size();i++) {
                alldivisions += "│";
                alldivisions += model.Divisions.get(i);
            }

        }

        SharedPreferences settings = MA.getSharedPreferences("DIVS", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("key1", alldivisions);
        editor.putString("key2", college);
        editor.putString("key3", teacher);
        editor.putString("key4",subject);
        editor.putString("key5", email);
        editor.commit();
        Msg.Show("Saved In Preferences");

    }


    void LoadDivisionsFromPrefs()
    {
        SharedPreferences settings = MA.getSharedPreferences("DIVS", 0);
        String alldivisions = settings.getString("key1", "XI-Z");
        college = settings.getString("key2", "School/College");
        teacher = settings.getString("key3", "Name");
        subject = settings.getString("key4","Subject");
        email = settings.getString("key5", "Email");

        if(alldivisions.contains("│"))
        {
            model.Divisions.clear();
            String temp[];
            temp=alldivisions.split("│");
            for(int i=0;i<temp.length;i++)
            {
                model.Divisions.add(temp[i]);

            }

        }
        else
        {
            if (alldivisions.contains("#")) {
                model.Divisions.clear();
                model.Divisions.add(alldivisions);
            }

        }

    }

}
