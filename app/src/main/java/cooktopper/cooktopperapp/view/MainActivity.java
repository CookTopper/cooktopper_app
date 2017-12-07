package cooktopper.cooktopperapp.view;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import cooktopper.cooktopperapp.R;
import cooktopper.cooktopperapp.presenter.SmokeSensorPresenter;

public class MainActivity extends AppCompatActivity{

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        Thread smokeSensorListener = new Thread() {
            @Override
            public void run() {

                Bitmap largeIcon = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                        R.drawable.fire_icon);
                final NotificationCompat.Builder builder =
                        new NotificationCompat.Builder(getApplicationContext(), null)
                                .setSmallIcon(R.mipmap.ic_fire_white_24dp)
                                .setLargeIcon(largeIcon)
                                .setContentTitle("Atenção")
                                .setContentText("Há mais fumaça que o normal no seu fogão");

                while(true) {

                    SmokeSensorPresenter smokeSensorPresenter = new SmokeSensorPresenter
                            (getApplicationContext());
                    final int smokeSensorLevel = smokeSensorPresenter.getSmokeSensor();
                    final int MAXIMUM_LEVEL = 200;

                    runOnUiThread(new Runnable(){
                        public void run(){
                            if(smokeSensorLevel >= MAXIMUM_LEVEL){
                                NotificationManager notificationManager =
                                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                notificationManager.notify(1, builder.build());
                            }
                        }
                    });

                    try{
                        sleep(1000);
                    } catch(InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        };

        smokeSensorListener.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int options_id = item.getItemId();

        switch (options_id){
            case R.id.qrcode:
                Intent launchchQrCodeReaderActivity = new Intent(MainActivity.this,
                        QrCodeActivity.class);
                startActivityForResult(launchchQrCodeReaderActivity, 0);
                return true;
            case R.id.new_shortcut:
                Intent intent = new Intent(this, ShortcutActivity.class);
                startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}