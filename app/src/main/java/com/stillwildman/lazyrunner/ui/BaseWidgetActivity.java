package com.stillwildman.lazyrunner.ui;

import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.stillwildman.lazyrunner.R;

/**
 * Created by vincent.chang on 2017/4/14.
 */

public abstract class BaseWidgetActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    protected abstract String getToolbarTitle();
    protected abstract boolean enableDrawer();
    protected abstract void onWidgetsReady();

    private Toolbar toolbar;

    protected DrawerLayout drawer;
    protected NavigationView navigationView;
    private ActionBarDrawerToggle drawerToggle;

    protected ImageView navPhoto;
    protected TextView navName;
    protected TextView navSnippet;

    @Override
    protected void init() {
        intToolbar();
        initDrawer();
    }

    private void intToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getToolbarTitle());
        setSupportActionBar(toolbar);
    }

    protected void setToolbarTitle(String title) {
        toolbar.setTitle(title);
    }

    protected void initDrawer() {
        if (enableDrawer()) {
            findDrawerViews();

            drawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(drawerToggle);
            drawerToggle.syncState();

            navigationView.setNavigationItemSelectedListener(this);
        }

        onWidgetsReady();
    }

    private void findDrawerViews() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        navPhoto = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.navHeader_photo);
        navName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.text_navHeaderName);
        navSnippet = (TextView) navigationView.getHeaderView(0).findViewById(R.id.text_navHeaderSnippet);
    }

    protected boolean isDrawerOpened() {
        return drawer.isDrawerOpen(GravityCompat.START);
    }

    protected void closeDrawer() {
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (notNull(drawer))
            drawer.removeDrawerListener(drawerToggle);
    }
}
