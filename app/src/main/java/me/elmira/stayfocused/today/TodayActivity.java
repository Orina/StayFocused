package me.elmira.stayfocused.today;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import me.elmira.stayfocused.Injection;
import me.elmira.stayfocused.R;
import me.elmira.stayfocused.archive.ArchiveActivity;
import me.elmira.stayfocused.util.ActivityUtils;

public class TodayActivity extends AppCompatActivity {

    public static final String LOG_TAG = "TodayActivity";

    private DrawerLayout mDrawerLayout;
    private Toolbar mToolbar;
    private ActionBarDrawerToggle mDrawerToggle;
    private TodayFragment mTasksFragment;
    private NavigationView mNavigationView;

    private TodayPresenter mTodayPresenter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreate()");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.home_drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close);

        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
        setupDrawerContent();
        //setupNavigationView();

        mTasksFragment = (TodayFragment) getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (mTasksFragment == null) {
            mTasksFragment = TodayFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), mTasksFragment, R.id.content_frame);
        }

        mTodayPresenter = new TodayPresenter(Injection.provideTasksRepository(getApplicationContext()), mTasksFragment);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.d(LOG_TAG, "onConfigurationChanged()");
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(LOG_TAG, "onCreateOptionsMenu()");
        getMenuInflater().inflate(R.menu.menu_today, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // The action bar home/up action should open or close the drawer.
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return false;
    }


    @Override
    protected void onResume() {
        Log.d(LOG_TAG, "onResume()");
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.d(LOG_TAG, "onPause()");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.d(LOG_TAG, "onStop()");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d(LOG_TAG, "onDestroy()");
        super.onDestroy();
    }


    /*private void setupNavigationView() {
        Menu menu = mNavigationView.getMenu();
        for (int i = 0; i < 4; i++) {
            MenuItem item = menu.add(R.id.labelsGroupId, Menu.NONE, 1, "Label " + i);
            item.setIcon(R.drawable.ic_label_outline_white_24dp);
            item.getIcon().mutate().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN);
        }
    }*/

    private void setupDrawerContent() {
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                return onDrawerNavigationItemSelected(item);
            }
        });
    }

    private boolean onDrawerNavigationItemSelected(MenuItem item) {
        item.setChecked(true);
        mDrawerLayout.closeDrawers();

        switch (item.getItemId()) {
            case R.id.archiveMenuId:
                startArchiveActivity();
                return true;
            case R.id.todayId:
                Intent intent = new Intent(getApplicationContext(), TodayActivity.class);
                startActivity(intent);
                return true;
        }
        return false;
    }

    private void startArchiveActivity() {
        Intent intent = new Intent(getApplicationContext(), ArchiveActivity.class);
        startActivity(intent);
    }
}
