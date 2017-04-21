package com.stillwildman.lazyrunner.ui;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.stillwildman.lazyrunner.R;
import com.stillwildman.lazyrunner.utilities.DialogHelper;
import com.stillwildman.lazyrunner.utilities.Utility;


public class UiMainActivity extends BaseWidgetActivity  {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected String getToolbarTitle() {
        return getString(R.string.app_launch_slogan);
    }

    @Override
    protected boolean enableDrawer() {
        return true;
    }

    @Override
    protected void onWidgetsReady() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });
    }

    @Override
    protected void findViews() {

    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void onUserSignedIn(FirebaseUser user) {
        Log.i(TAG, "UserInfo\nName: " + user.getDisplayName() + "\nEmail: " + user.getEmail() + "\nPhotoURL: " + user.getPhotoUrl());

        navName.setText(user.getDisplayName());
        navSnippet.setText(user.getEmail());

        Glide.with(this)
                .load(user.getPhotoUrl())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(navPhoto);

        for (UserInfo userInfo : user.getProviderData()) {
            Log.i(TAG, "ProviderID: " + userInfo.getProviderId() + "\nUID: " + userInfo.getUid() + "\nName: "
                    + userInfo.getDisplayName() + "\nEmail: " + userInfo.getEmail() + "\nPhotoURL: " + userInfo.getPhotoUrl());
        }
    }

    @Override
    protected void onUserSignedOut() {
        DialogHelper.dismissDialog();

        Utility.toastShort("Signed Out");
        Intent intent = new Intent(this, UiStartupActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_logout:
                signFireOut();
                break;
        }

        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_camera:
                // Handle the camera action
                break;
            case R.id.nav_gallery:

                break;
            case R.id.nav_slideshow:

                break;
            case R.id.nav_manage:

                break;
            case R.id.nav_share:

                break;
            case R.id.nav_send:

                break;
        }

        closeDrawer();
        return true;
    }

    @Override
    public void onBackPressed() {
        if (isDrawerOpened())
            closeDrawer();
        else
            super.onBackPressed();
    }
}
