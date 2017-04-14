package com.stillwildman.lazyrunner.ui;

import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by vincent.chang on 2017/4/14.
 */

public abstract class BaseFireActivity extends BaseActivity implements FirebaseAuth.AuthStateListener {

    protected FirebaseAuth fireAuth;

    @Override
    protected void init() {
        fireAuth = FirebaseAuth.getInstance();
    }


    @Override
    public void onStart() {
        super.onStart();
        fireAuth.addAuthStateListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (fireAuth != null)
            fireAuth.removeAuthStateListener(this);
    }
}
