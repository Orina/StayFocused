package me.elmira.stayfocused.today;

import android.content.res.Configuration;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import me.elmira.stayfocused.Injection;
import me.elmira.stayfocused.util.ActivityUtils;
import me.elmira.stayfocused.R;

public class TodayActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private Toolbar mToolbar;
    private ActionBarDrawerToggle mDrawerToggle;
    private TodayFragment mTasksFragment;
    private NavigationView mNavigationView;

    private boolean changeLayoutMenu = false;
    private TodayPresenter todayPresenter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        /*mToolbar.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom,
                                       int oldLeft, int oldTop, int oldRight, int oldBottom) {
                View item = mToolbar.findViewById(R.id.menu_change_layout_id);
                if (item != null) {
                    mToolbar.removeOnLayoutChangeListener(this);
                    item.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ObjectAnimator animator = ObjectAnimator.ofFloat(v, "rotation", v.getRotation() + 180);
                            animator.start();
                        }
                    });
                }
            }
        });*/

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.home_drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close);

        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
        setupNavigationView();

        mTasksFragment = (TodayFragment) getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (mTasksFragment == null) {
            mTasksFragment = TodayFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), mTasksFragment, R.id.content_frame);
        }

        todayPresenter = new TodayPresenter(Injection.provideTasksRepository(getApplicationContext()), mTasksFragment);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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
            case R.id.menu_change_layout_id:
                changeLayoutMenu = true;
                int type = mTasksFragment.changeLayout();
                if (type == TodayFragment.TASK_LAYOUT_MANAGER_GRID) {
                    item.setIcon(R.drawable.ic_view_stream_white_24dp);
                } else if (type == TodayFragment.TASK_LAYOUT_MANAGER_LIST) {
                    item.setIcon(R.drawable.ic_dashboard_white_24dp);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupNavigationView() {
        Menu menu = mNavigationView.getMenu();
        for (int i = 0; i < 4; i++) {
            MenuItem item = menu.add(R.id.labelsGroupId, Menu.NONE, 1, "Label " + i);
            item.setIcon(R.drawable.ic_label_outline_white_24dp);
            item.getIcon().mutate().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN);
        }

    }
}
