package com.stillwildman.lazyrunner.ui;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.stillwildman.lazyrunner.utilities.DialogHelper;
import com.stillwildman.lazyrunner.utilities.Utility;

/**
 * Created by vincent.chang on 2017/4/14.
 */

public abstract class BaseFireActivity extends BaseActivity implements FirebaseAuth.AuthStateListener {

    protected abstract void onUserSignedIn(FirebaseUser user);
    protected abstract void onUserSignedOut();

    protected static final String DATABASE_USERS = "users";
    protected static final String DATA_USERS_UID = "uid";
    protected static final String DATA_USERS_NAME = "name";
    protected static final String DATA_USERS_EMAIL = "email";
    protected static final String DATA_USERS_PIC_URL = "pic_url";

    private FirebaseAuth fireAuth;
    private DatabaseReference userReference;

    @Override
    protected void init() {
        initFireAuth();
    }

    private void initFireAuth() {
        fireAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (fireAuth == null)
            initFireAuth();

        fireAuth.addAuthStateListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (fireAuth != null)
            fireAuth.removeAuthStateListener(this);
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        FirebaseUser user = firebaseAuth.getCurrentUser();

        if (user != null) {
            onUserSignedIn(user);
            Log.i(TAG, "onAuthStateChanged: Signed In: " + user.getUid());
        }
        else {
            onUserSignedOut();
            Log.i(TAG, "onAuthStateChanged: Signed Out!");
        }
    }

    protected void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.i(TAG, "firebaseAuthWithGoogle\nAccount ID (Google UID): " + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);

        fireAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // If sign in succeeds, it triggers onAuthStateChanged callback.
                        Log.i(TAG, "signInWithCredential:onComplete: " + task.isSuccessful());

                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Utility.toastShort("Authentication failed.");
                        }
                    }
                });
    }

    protected void signFireOut() {
        DialogHelper.showLoadingDialog(this);
        FirebaseAuth.getInstance().signOut();
    }

    protected DatabaseReference getUserReference() {
        if (userReference == null)
            userReference = FirebaseDatabase.getInstance().getReference(DATABASE_USERS);

        return userReference;
    }
}
