package com.stillwildman.lazyrunner.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.stillwildman.lazyrunner.R;
import com.stillwildman.lazyrunner.utilities.Utility;

/**
 * Created by vincent.chang on 2017/4/12.
 */

public abstract class BaseGoogleApiActivity extends BaseFireActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    protected abstract GoogleAPIs[] getGoogleAPIs();
    protected abstract void onApiReady();

    protected static final int GOOGLE_SIGN_IN = 9001;

    protected GoogleApiClient apiClient;

    enum GoogleAPIs {
        SIGN_IN
    }

    @Override
    protected void init() {
        GoogleApiClient.Builder apiBuilder = new GoogleApiClient.Builder(this);
        apiBuilder.addConnectionCallbacks(this).enableAutoManage(this, this);

        for (int i = 0; i < getGoogleAPIs().length; i++)
        {
            switch (getGoogleAPIs()[i]) {
                case SIGN_IN:
                    GoogleSignInOptions gso = new GoogleSignInOptions
                            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                            .requestIdToken(getString(R.string.project_backend_client_id))
                            .requestEmail()
                            .build();

                    apiBuilder.addApi(Auth.GOOGLE_SIGN_IN_API, gso);
                    break;
            }
        }

        apiClient = apiBuilder.build();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(TAG, "GoogleApi: onConnected!");
        onApiReady();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "GoogleApi: onConnectionSuspended!");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (connectionResult.getErrorCode() == ConnectionResult.API_UNAVAILABLE) {
            Utility.toastLong(getString(R.string.google_api_places_unavailable));
            Log.i(TAG, "onConnectionFailed: API UNAVAILABLE!");
        }
        Log.i(TAG, "GoogleApi: onConnectionFailed!");
    }
}
