package in.org.ilugbom.attendance;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.GestureDetector;
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
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
{
    TextAdapter TA;
    Msg msg=new Msg();
    Model model;

    FloatingActionButton fab;
    boolean fabVisible=true;
    boolean StartAttendance=false;

    private ImageButton  buttonLeft,  buttonRight;
    private Button buttonDivTitle;

    static  int currentDivision = 0;

    LinearLayout LL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        LL = (LinearLayout) findViewById(R.id.ClassBar);


        setSupportActionBar(toolbar);
        msg.SetMA(this);


        TA = new TextAdapter(this);
        final GridView gridView = (GridView) findViewById(R.id.gridview);
        gridView.setAdapter(TA);
        for (int i = 0; i < 120; i++) {
            TA.numbers[i] = String.format("%d", 5000 + i + 1);
        }


        model  = new Model();
        model.LoadDivisions();


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
             Msg.show("Div Title");
            }
        });

        buttonLeft = (ImageButton) findViewById(R.id.buttonLeft);
        buttonLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                currentDivision--;
                if (currentDivision < 0) currentDivision = model.Divisions.size() - 1;
                DisplayDivision();
            }
        });


        buttonRight = (ImageButton) findViewById(R.id.buttonRight);
        buttonRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                currentDivision++;
                if (currentDivision > model.Divisions.size() - 1) currentDivision = 0;
                DisplayDivision();
            }
        });

        DisplayDivision();   //assert currentDivision=0;

    }  ////////////////////////////////////////// END OF ONCREATE



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
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
        if (id == R.id.action_settings)
        {
            Msg.show("Settings");

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }


    void OnFloatingButton()
    {
        if(!StartAttendance)
            fab.setBackgroundTintList(getResources().getColorStateList(R.color.colorPink));
        else
            fab.setBackgroundTintList(getResources().getColorStateList(R.color.colorGreen));

        StartAttendance=!StartAttendance;

    }

    void DisplayDivision()   //// Display division with index currentdivision
    {   TA.Divisions.clear();
        TA.DisplayDivision(model.Divisions.get(currentDivision));
        buttonDivTitle.setText(model.GetDivisionTitle(currentDivision));
    }

}   /////CLASS END



///////////////////////// <<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>///////////////////////////////
//////////////////////////<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>/////////////////////////////
/////////////////////////// Extra Code  Snippets


/*
            if(!StartAttendance) LL.setBackgroundColor(Color.RED);
            else LL.setBackgroundColor( getResources().getColor(R.color.SecondBar));

            StartAttendance=!StartAttendance;
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
