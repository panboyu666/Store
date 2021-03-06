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

        //訪客按鈕
        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getApplication(), "訪客不能發布，聊天，等相關功能,完整功能請登入GOOGLE或FB帳戶", Toast.LENGTH_SHORT).show();

                MainActivity.editor.putString("姓名key", "訪客");
                MainActivity.editor.putString("頭貼key", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR6T46zuj0rKxNFyKVSu0b1pnfXAETl83CRxw&usqp=CAU");

                MainActivity.editor.commit();
                MainActivity.userID = "訪客";

                Intent intent = new Intent(SingUp.this, MainActivity.class);
                startActivity(intent);
                finish();

            }
        });

        //google登入前置
        createGoogle();
        //FB登入前置
        createFB();


    }


    private void createGoogle() {


        mAuth = FirebaseAuth.getInstance();
        // Configure Google Sign In 這裡可以說是登入介面選擇信箱
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                //新增值後去values string.xml 把值刪除 並免名字重複 抱錯
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        //注意這裡GoogleSignIn.getClient 需要ADD依賴項
    }


    private void createFB() {
        //fb
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("email");

        //FB登入成功後動作
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                    //備註:因為一開始登入 profile還沒準備好所以是空  索性用追蹤器直接得到資料就好
                if(Profile.getCurrentProfile() == null) {
                    mProfileTracker = new ProfileTracker() {
                        @Override
                        protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {



                        MainActivity.userID=currentProfile.getId();
                        MainActivity.editor.putString("姓名key",currentProfile.getName());

//                        //得到完整FB頭貼網址
                        String str = "https://graph.facebook.com/" + currentProfile.getId() + "/picture?type=large";
                        MainActivity.editor.putString("頭貼key",str);
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
                    MainActivity.editor.putString("姓名key",profile.getName());

//                        //得到完整FB頭貼網址
                    String str = "https://graph.facebook.com/" + profile.getId() + "/picture?type=large";
                    MainActivity.editor.putString("頭貼key",str);
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
                Toast.makeText(SingUp.this, "抱歉 登入出錯了", Toast.LENGTH_SHORT).show();
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

        //以下GOOGLE
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
                Toast.makeText(this, "登入失敗" + e, Toast.LENGTH_SHORT).show();

            }
        }


    }

    //google 登入選擇帳號 成功登入後的後續動作
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
                            MainActivity.editor.putString("姓名key", fireuser.getDisplayName());
                            MainActivity.editor.putString("頭貼key", "" + fireuser.getPhotoUrl());
                            MainActivity.editor.commit();


                            Intent intent = new Intent(SingUp.this, MainActivity.class);
                            intent.putExtra("key1", MainActivity.userID);
                            startActivity(intent);
                            finish();

                            // updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("result", "signInWithCredential:failure", task.getException());
                            Toast.makeText(SingUp.this, "登入失敗Error", Toast.LENGTH_SHORT).show();
                            // updateUI(null);
                        }
                    }
                });
    }


    // 按下鍵盤上返回按鈕
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            new AlertDialog.Builder(this)
                    .setMessage("確定退出APP嗎？")
                    .setNegativeButton("取消",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                }
                            })
                    .setPositiveButton("確定",
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



