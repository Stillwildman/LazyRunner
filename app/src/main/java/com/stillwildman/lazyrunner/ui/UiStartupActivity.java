package com.stillwildman.lazyrunner.ui;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.stillwildman.lazyrunner.R;
import com.stillwildman.lazyrunner.utilities.DialogHelper;
import com.stillwildman.lazyrunner.utilities.Utility;

/**
 * Created by vincent.chang on 2017/4/7.
 */

public class UiStartupActivity extends BaseGoogleApiActivity {

    private LinearLayout appLogoLayout;
    private LinearLayout signInLayout;
    private FrameLayout googleSignInBtn;

    private boolean animated;

    @Override
    protected boolean setFullScreenStyle() {
        return true;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_startup_and_login;
    }

    @Override
    protected GoogleAPIs[] getGoogleAPIs() {
        return new GoogleAPIs[] {GoogleAPIs.SIGN_IN};
    }

    @Override
    protected void onApiReady() {

    }

    @Override
    protected void findViews() {
        appLogoLayout = (LinearLayout) findViewById(R.id.layout_appLogo);
        signInLayout = (LinearLayout) findViewById(R.id.layout_signIn);
        googleSignInBtn = (FrameLayout) findViewById(R.id.btn_signInWithGoogle);
    }

    @Override
    protected void setListener() {

    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        FirebaseUser user = firebaseAuth.getCurrentUser();

        if (user != null) {
            goToMain();
            Log.i(TAG, "onAuthStateChanged:signed_in: " + user.getUid());
        }
        else {
            prepareToSignIn();
            Log.i(TAG, "onAuthStateChanged:signed_out");
        }
    }

    private void goToMain() {
        getUiHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(UiStartupActivity.this, UiMainActivity.class));
                UiStartupActivity.this.finish();
            }
        }, 1500);
    }

    private void prepareToSignIn() {
        getUiHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                moveLogoLayout();
            }
        }, 1500);
    }

    private void moveLogoLayout() {
        if (!animated) {
            int[] originPos = new int[2];
            appLogoLayout.getLocationOnScreen(originPos);

            int animHeight = 0 - (int) (originPos[1] / 1.4);

            Log.i(TAG, "appLogoLayout Left: " + originPos[0] + " Top: " + originPos[1]);
            Log.i(TAG, "AnimHeight: " + animHeight);

            TranslateAnimation anim = new TranslateAnimation(0, 0, 0, animHeight);
            anim.setDuration(1000);
            anim.setFillAfter(true);

            anim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    signInLayout.setVisibility(View.VISIBLE);
                    googleSignInBtn.setOnClickListener(getGoogleSignInClick());
                    animated = true;
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

            appLogoLayout.startAnimation(anim);
        }
    }

    private View.OnClickListener getGoogleSignInClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        };
    }

    private void signIn() {
        DialogHelper.showLoadingDialog(this);
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(apiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "onActivityResult - requestCode: " + requestCode + " resultCode: " + resultCode);

        DialogHelper.dismissDialog();

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            }
            else {
                // Google Sign In failed, update UI appropriately
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.i(TAG, "firebaseAuthWithGoogle\nAccount ID (Google UID): " + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);

        fireAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.i(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Utility.toastShort("Authentication failed.");
                        }
                    }
                });
    }

}
