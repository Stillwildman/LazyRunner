package com.stillwildman.lazyrunner.ui;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.stillwildman.lazyrunner.R;

/**
 * Created by vincent.chang on 2017/3/30.
 */

public abstract class BaseActivity extends AppCompatActivity {

    protected abstract int getLayoutId();
    protected abstract void init();
    protected abstract void findViews();
    protected abstract void setListener();

    protected final String TAG = getClass().getSimpleName();

    private Handler uiHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (setFullScreenStyle())
            this.setTheme(R.style.AppFullScreenTheme);

        setContentView(getLayoutId());

        findViews();
        setListener();
        init();
    }

    protected boolean setFullScreenStyle() {
        return false;
    }

    protected Handler getUiHandler() {
        if (uiHandler == null)
            uiHandler = new Handler();
        return uiHandler;
    }

    protected boolean notNull(Object anyObject) {
        return anyObject != null;
    }
}
