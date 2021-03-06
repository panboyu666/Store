package pan.bo.yu.petadoption;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.os.Handler;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessTokenTracker;
import com.facebook.ProfileTracker;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;


public class SingUp extends AppCompatActivity {
    private static final int RC_SIGN_IN = 123;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    private CallbackManager callbackManager;
    private LoginButton loginButton;
    private FirebaseUser fireuser;

    private Button user, newFB, newGoogle;

    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker mProfileTracker;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Design_Light_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);

        user = findViewById(R.id.user);
        newFB = findViewById(R.id.newFB);
        newGoogle = findViewById(R.id.newGoogle);
        loginButton = findViewById(R.id.login_button);


        newFB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButton.performClick();
            }
        });


        newGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        //????????????
        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getApplication(), "?????????????????????????????????????????????,?????????????????????GOOGLE???FB??????", Toast.LENGTH_SHORT).show();

                MainActivity.editor.putString("??????key", "??????");
                MainActivity.editor.putString("??????key", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR6T46zuj0rKxNFyKVSu0b1pnfXAETl83CRxw&usqp=CAU");

                MainActivity.editor.commit();
                MainActivity.userID = "??????";

                Intent intent = new Intent(SingUp.this, MainActivity.class);
                startActivity(intent);
                finish();

            }
        });

        //google????????????
        createGoogle();
        //FB????????????
        createFB();


    }


    private void createGoogle() {


        mAuth = FirebaseAuth.getInstance();
        // Configure Google Sign In ??????????????????????????????????????????
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                //???????????????values string.xml ???????????? ?????????????????? ??????
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        //????????????GoogleSignIn.getClient ??????ADD?????????
    }


    private void createFB() {
        //fb
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("email");

        //FB?????????????????????
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                    //??????:????????????????????? profile???????????????????????????  ??????????????????????????????????????????
                if(Profile.getCurrentProfile() == null) {
                    mProfileTracker = new ProfileTracker() {
                        @Override
                        protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {



                        MainActivity.userID=currentProfile.getId();
                        MainActivity.editor.putString("??????key",currentProfile.getName());

//                        //????????????FB????????????
                        String str = "https://graph.facebook.com/" + currentProfile.getId() + "/picture?type=large";
                        MainActivity.editor.putString("??????key",str);
                        MainActivity.editor.commit();


                        Intent intent = new Intent(SingUp.this,MainActivity.class);
                        intent.putExtra("key1",MainActivity.userID);

                        startActivity(intent);

                        finish();

                            mProfileTracker.stopTracking();
                        }
                    };

                }
                else {
                    Profile profile = Profile.getCurrentProfile();


                    MainActivity.userID=profile.getId();
                    MainActivity.editor.putString("??????key",profile.getName());

//                        //????????????FB????????????
                    String str = "https://graph.facebook.com/" + profile.getId() + "/picture?type=large";
                    MainActivity.editor.putString("??????key",str);
                    MainActivity.editor.commit();


                    Intent intent = new Intent(SingUp.this,MainActivity.class);
                    intent.putExtra("key1",MainActivity.userID);

                    startActivity(intent);

                    finish();


                }


            }

            @Override
            public void onCancel() {
                Log.w("fragment", "onCancel");
            }

            @Override
            public void onError(FacebookException exception) {
                Log.w("fragment", "onError");
                Toast.makeText(SingUp.this, "?????? ???????????????", Toast.LENGTH_SHORT).show();
            }
        });



    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //fb

        if (callbackManager.onActivityResult(requestCode, resultCode, data)) {
            return;
        }

        //??????GOOGLE
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("boobs", "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());

            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("boobs", "Google sign in failed", e);
                Toast.makeText(this, "????????????" + e, Toast.LENGTH_SHORT).show();

            }
        }


    }

    //google ?????????????????? ??????????????????????????????
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            fireuser = mAuth.getCurrentUser();
                            MainActivity.userID = fireuser.getDisplayName();
                            MainActivity.userUri = "" + fireuser.getPhotoUrl();
                            MainActivity.editor.putString("??????key", fireuser.getDisplayName());
                            MainActivity.editor.putString("??????key", "" + fireuser.getPhotoUrl());
                            MainActivity.editor.commit();


                            Intent intent = new Intent(SingUp.this, MainActivity.class);
                            intent.putExtra("key1", MainActivity.userID);
                            startActivity(intent);
                            finish();

                            // updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("result", "signInWithCredential:failure", task.getException());
                            Toast.makeText(SingUp.this, "????????????Error", Toast.LENGTH_SHORT).show();
                            // updateUI(null);
                        }
                    }
                });
    }


    // ???????????????????????????
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            new AlertDialog.Builder(this)
                    .setMessage("????????????APP??????")
                    .setNegativeButton("??????",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                }
                            })
                    .setPositiveButton("??????",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int whichButton) {
                                    finish();
                                }
                            }).show();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }


}



