package in.org.ilugbom.attendance;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
{

    private static final String TAG = "MainActivity";  //for rutime storage permission code
    private static final int REQUEST_CODE = 1;
    private boolean modified=false;
    private TextView FC;
    int counter=0;
    TextAdapter TA;
    Msg msg=new Msg();
    SendBackupByEmail sbbe=new SendBackupByEmail();

    Model model;
    CreateDivDialog CDD=new CreateDivDialog();
    HelpDialog HD=new HelpDialog();
    MonthlyReport MR = new MonthlyReport();

    FloatingActionButton fab;
    boolean fabVisible=true;
    boolean AttendanceInProgress=false;
    boolean HistoryMode=false;

    private ImageButton  buttonLeft,  buttonRight;
    private Button buttonDivTitle;

    static  int currentDivision = 0;

   // LinearLayout LL;



    //////////////////ON CREATE METHOD //////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
      //  LL = (LinearLayout) findViewById(R.id.ClassBar);

        if(!StoragePermissionGranted()) ;

        if(!StoragePermissionGranted()) finish();


        setSupportActionBar(toolbar);
        msg.SetMA(this);
        sbbe.SetMA(this);

        FC=(TextView) findViewById(R.id.FabCounter);

        TA = new TextAdapter(this);
        final GridView gridView = (GridView) findViewById(R.id.gridview);
        gridView.setAdapter(TA);
        for (int i = 0; i < 120; i++) {
            TA.numbers[i] = String.format("%d", 5000 + i + 1);
        }


        model  = new Model();
        model.LoadDivisions();


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id)
            {
               if(!AttendanceInProgress) { Msg.ImageMessage("Tap Attendance Button",R.drawable.fab_blue_green72); return;}
               // Attendance in  Progress
                int firstPosition = gridView.getFirstVisiblePosition();
                int childPosition = position - firstPosition;
                TextView txtView = (TextView) gridView.getChildAt(childPosition);
                Integer tt = new Integer(position);
                if (TA.selectedPositions.contains(tt)) {
                    txtView.setBackgroundColor(Color.parseColor("#fbdcbb"));
                    TA.selectedPositions.remove(tt);
                    FC.setText(String.format("%d",TA.selectedPositions.size()));
                } else {
                    txtView.setBackgroundColor(getResources().getColor(R.color.colorTuch));
                    TA.selectedPositions.add((Integer) position);
                    FC.setText(String.format("%d",TA.selectedPositions.size()));

                }
                modified=true;
            }
        });



        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int position, long arg3) {
                int firstPosition = gridView.getFirstVisiblePosition();
                int childPosition = position - firstPosition;
                TextView txtView = (TextView) gridView.getChildAt(childPosition);
                Integer tt = new Integer(position);
                int index=tt.intValue();
                CreateReport CR=new CreateReport();
                CR.LoadHistory(model.GetDivisionTitle(currentDivision));
                CR.ShowReport(index);

                return true;
            }
        });



        fab = (FloatingActionButton) findViewById(R.id.fab);

        /*
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                        */


        fab.setOnTouchListener(new View.OnTouchListener() {


            float x, y;
            float x1,y1;
            float x2,y2;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction())
                {   case MotionEvent.ACTION_UP :
                    if(Math.abs(x2-x1)<10 && Math.abs(y2-y1)<10)

                    {
                        OnFloatingButton();
                    }

                    return true;
                    case MotionEvent.ACTION_MOVE:

                        x2=fab.getX()+event.getX()-x; y2=fab.getY()+event.getY()-y;

                        fab.setX(x2);
                        fab.setY(y2);
                        return true;
                    case MotionEvent.ACTION_DOWN:
                        x = event.getX();
                        y = event.getY();
                        x1=fab.getX()+event.getX()-x; y1=fab.getY()+event.getY()-y;
                        x2=fab.getX();y2=fab.getY();
                     //   x1=x;
                      //  y1=y;
                     //   Msg.show(String.format("%d",event.getX()));
                        return true;
                }

                return false;
            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

//////////////////// Button Listeners ////////////////////////////////////

        buttonDivTitle=findViewById(R.id.buttonDivTitle);
        buttonDivTitle.setOnClickListener(new Button.OnClickListener() {

            public void onClick(View view)
            {

            }
        });

        buttonLeft = (ImageButton) findViewById(R.id.buttonLeft);
        buttonLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {   if(AttendanceInProgress) { Msg.ImageMessage("Attendance in Progress",R.drawable.blue_red_60); return; }

                currentDivision--;
                if (currentDivision < 0) currentDivision = model.Divisions.size() - 1;
                DisplayDivision();
            }
        });


        buttonRight = (ImageButton) findViewById(R.id.buttonRight);
        buttonRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            { if(AttendanceInProgress) { Msg.ImageMessage("Attendance in Progress",R.drawable.blue_red_60); return; }
                currentDivision++;
                if (currentDivision > model.Divisions.size() - 1) currentDivision = 0;
                DisplayDivision();
            }
        });

        CDD.SetRef(model);
        CDD.SetMA(this);
//        msg.SetMA(this);

        setTitle(model.GetDateTimeString());

        CDD.LoadDivisionsFromPrefs();currentDivision=0;


        DisplayDivision();   //assert currentDivision=0;


      //  navigationView.setBackgroundColor(getResources().getColor(R.color.skyBlue));

    }  ////////////////////////////////////////// END OF ONCREATE
       /////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////





    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else

        if(modified)  {  ShowPopupMenu(); }
        else
        {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();



        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add_division)
        {
            //Msg.show("Settings");
            CDD.editmode=false;
            // Toast.makeText(getBaseContext(), "create", Toast.LENGTH_SHORT).show();
            CDD.showDialog(MainActivity.this);
            return true;
        }

        if (id == R.id.action_invert_selection)
        {
            InvertAttendance();
            Msg.Show("Selection Inverted");
            return true;
        }


        if (id==R.id.action_edit_div)
        {
            EditDivision();
        }


        if (id == R.id.action_delete_div)
        {
            DeleteDivision();
            return true;
        }

        if (id == R.id.action_history_mode)
        {

          if(HistoryMode) //if historymode is true then switch it off and load opening screen
           { if(modified) {  Msg.Show("History modified, Save or Discard First !");
                             return true;
                          }
             HistoryMode=false;
             model.Divisions.clear();
             item.setChecked(HistoryMode);
             model.LoadDivisions();
             CDD.LoadDivisionsFromPrefs();
             currentDivision=0;
             DisplayDivision();
             setTitle(model.GetDateTimeString());
             FC.setText(String.format(""));
           }
          else
           { if(modified) {  Msg.Show("Attendance Modified, Save or Discard First !");
               return true;
           }
             HistoryMode=true;
             item.setChecked(HistoryMode);
             model.LoadHistory();
             currentDivision = model.Divisions.size()-1;
             DisplayDivision();
           }
            fab.setBackgroundTintList(getResources().getColorStateList(R.color.colorGreen));
            AttendanceInProgress=false;
            return true;
        }


        if (id == R.id.action_delete_record)
        {
//           model.Divisions.remove(currentDivision);
            if(!HistoryMode) { Msg.ImageMessage("History Mode Off",R.drawable.exclaimation60); return true; }

            DeleteHistoryRecord();
          //  Msg.show("History - Del Record");
            return true;
        }


        if (id == R.id.action_help)
        {

            HD.showDialog(MainActivity.this);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id)
        {
            case R.id.nav_printmonthlyreport :

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        PrintMonthlyReport();
                    }
                }, 300);


            break;

            case R.id.nav_reserved1 : showAlertDialog(); break;

            case R.id.nav_reserved2 : Msg.Show("Reserved Menu 2");; break;

            case R.id.nav_setpreferences : CDD.showPreferenceDialog(MainActivity.this); break;

            case R.id.nav_share : Msg.Show("Share-Module Pending..");break;

            case R.id.nav_send : sbbe.Send(CDD.email); break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    void OnFloatingButton()
    {
       // ShowPopupMenu();
        if(!AttendanceInProgress) {
            fab.setBackgroundTintList(getResources().getColorStateList(R.color.colorPink));
            Msg.Show("Mark Attendance");
            AttendanceInProgress=!AttendanceInProgress;
        }

        else {

            ShowPopupMenu();

             }
    }
////////////////////////////////////////////////


    public  boolean StoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted");
                return true;
            } else {

                Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted");
            return true;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            Log.v(TAG,"Permission: "+permissions[0]+ "was "+grantResults[0]);
            //resume tasks needing this permission
        }
    }



    void EditDivision()
    {
        CDD.editmode=true;
        CDD.tempDivTitle=model.GetDivisionTitle(currentDivision);
        String temp,temp2[];
        temp=model.GetRollStartFinish(currentDivision);
        temp2=temp.split("-");
        CDD.tempFroll=temp2[0];
        CDD.tempLroll=temp2[1];
        CDD.showDialog(MainActivity.this);


    }





    String GetAttendanceLine()
    {String Line="";
        for(int i=0;i<TA.numbers.length;i++)
        {Integer tt=new Integer(i);
            if(TA.selectedPositions.contains(tt))
                Line+="A";
            else
                Line+="P";
        }
        return Line;
    }



    void DeleteHistoryRecord()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Delete This Record ?");

        // Set the alert dialog yes button click listener
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {

                if(model.Divisions.size()>1)
                {
                    model.Divisions.remove(currentDivision);
                    model.DateArray.remove(currentDivision);
                    currentDivision--;
                    if(currentDivision<0) currentDivision=0;
                    DisplayDivision();
                    // Toast.makeText(getApplicationContext(),
                    //       "Division Deleted",Toast.LENGTH_LONG).show();
                    model.SaveHistory();
                    Msg.Show("History Record Deleted");
                    //CDD.SaveDivisionsInPrefs();

                }
            }
        });

        // Set the alert dialog no button click listener
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing on No button clicked
            }
        });

        AlertDialog dialog = builder.create();
        // Display the alert dialog on interface
        dialog.show();
    }



    void DeleteDivision()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Delete This Division ?");   // Set a title for alert dialog

        // Set the alert dialog yes button click listener
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {

                if(model.Divisions.size()>1)
                {
                    model.Divisions.remove(currentDivision);
                    currentDivision--;
                    if(currentDivision<0) currentDivision=0;
                    DisplayDivision();
                   // Toast.makeText(getApplicationContext(),
                     //       "Division Deleted",Toast.LENGTH_LONG).show();
                    Msg.Show("Division Deleted");
                    CDD.SaveDivisionsInPrefs();

                }
            }
        });

        // Set the alert dialog no button click listener
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing on No button clicked
            }
        });

        AlertDialog dialog = builder.create();
        // Display the alert dialog on interface
        dialog.show();
    }




    void ShowPopupMenu()
    {
        //Creating the instance of PopupMenu
        PopupMenu popup = new PopupMenu(MainActivity.this, fab);
        //Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item)
            {
 //               Toast.makeText(MainActivity.this,"You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
            //    Msg.show("Test");
               int option=item.getItemId();
               switch(option)
               { case R.id.one : CloseAndSaveAttendance(); break;
                 case R.id.two :
                   Msg.Show("Continue Attendance"); break;
                   case R.id.three :
                       fab.setBackgroundTintList(getResources().getColorStateList(R.color.colorGreen));
                       Msg.Show("Attendance Discarded");
                       TA.selectedPositions.clear();
                       modified=false;
                       AttendanceInProgress=!AttendanceInProgress;
                       DisplayDivision();
                       break;
               }
                return true;
            }
        });

        popup.show();//showing popup menu
    }
//});//closing the setOnClickListener method

void CloseAndSaveAttendance()
    {

        fab.setBackgroundTintList(getResources().getColorStateList(R.color.colorGreen));
        AttendanceInProgress=false;
        String AL=GetAttendanceLine(); //current line :  AAPAPAPP...etc

        if(HistoryMode)
        {
            String atdLine=model.Divisions.get(currentDivision);
            String temp[];
            temp=atdLine.split("#");
            model.Divisions.set(currentDivision,temp[0]+"#"+temp[1]+"#"+AL);
            model.SaveHistory();
            modified=false;
            FC.setText(String.format("%d",TA.selectedPositions.size()));
            return;

        }
        ///else normal mode so save on sd card
        if(StoragePermissionGranted())
        {
            model.SaveList(AL);
            TA.selectedPositions.clear();
            DisplayDivision();
            modified=false;
        }

    }

    void DisplayDivision()   //// Display division with index currentdivision
    {   TA.Divisions.clear();
        TA.DisplayDivision(model.Divisions.get(currentDivision));
        buttonDivTitle.setText(model.GetDivisionTitle(currentDivision));
        if(HistoryMode) {
            setTitle(model.DateArray.get(currentDivision));
            FC.setText(String.format("%d",TA.selectedPositions.size()));
        }
        else
            FC.setText("");

    }


    void InvertAttendance()
    {
        if(!AttendanceInProgress) {Msg.Show("Attendance Mode Off"); return;}
        String Line="";
            for(int i=0;i<TA.numbers.length;i++)
            {Integer tt=new Integer(i);
                if(TA.selectedPositions.contains(tt))
                    Line+="P";
                else
                    Line+="A";
            }

        TA.Fillpositions(Line);
            TA.notifyDataSetChanged();
        FC.setText(String.format("%d",TA.selectedPositions.size()));

    }


   void PrintMonthlyReport()
   {
       MR.callcdd(CDD);
       try {
           MR.AttendanceReportPdf();
       } catch (FileNotFoundException e) {
           e.printStackTrace();
       } catch (DocumentException e) {
           e.printStackTrace();
       }
       Msg.Show("Monthly Report.pdf Created");
   }


    private void showAlertDialog() {
        // Prepare grid view

        PickMonthAdapter PMA = new PickMonthAdapter(this);


        GridView monthgridView = new GridView(this);

        monthgridView.setAdapter(PMA);
        monthgridView.setNumColumns(3);
        monthgridView.setVerticalSpacing(4);
        monthgridView.setHorizontalSpacing(4);
        //setPadding(2,2,2,2);
        monthgridView.setBackgroundColor(Color.WHITE);

        // Set grid view to alertDialog
        final AlertDialog builder = new AlertDialog.Builder(this).create();
//        builder.setCancelable(true);
        builder.setView(monthgridView);
        builder.setTitle("( Div : "+ model.GetDivisionTitle(currentDivision)+" )    Choose Month");
        builder.show();
        monthgridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Msg.Show(String.format("%d",position));

             builder.dismiss();
            }
        });
    }



}   /////CLASS END



///////////////////////// <<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>///////////////////////////////
//////////////////////////<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>/////////////////////////////
/////////////////////////// Extra Code  Snippets


/*
            if(!AttendanceInProgress) LL.setBackgroundColor(Color.RED);
            else LL.setBackgroundColor( getResources().getColor(R.color.SecondBar));

            AttendanceInProgress=!AttendanceInProgress;
*/

// Change FAB mail icon
//fab.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_launcher_round2));

            /*
            hide and show FAB toggle

            CoordinatorLayout.LayoutParams p = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
            p.setAnchorId(View.NO_ID);
            fab.setLayoutParams(p);
            if(fabVisible) fab.setVisibility(View.GONE);
            else
                fab.setVisibility(View.VISIBLE);
            fabVisible=!fabVisible;

            */
