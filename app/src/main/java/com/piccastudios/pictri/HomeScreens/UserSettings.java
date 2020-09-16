package com.piccastudios.pictri.HomeScreens;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.auth.User;
import com.piccastudios.pictri.Account.SignInAccount;
import com.piccastudios.pictri.QuestionFrames.LevelOver;
import com.piccastudios.pictri.QuestionFrames.QuestionFrameText;
import com.piccastudios.pictri.R;
import com.piccastudios.pictri.UserSection.SaveData;
import com.piccastudios.pictri.UserSection.UserDetails;

import java.util.Objects;

public class UserSettings extends AppCompatActivity implements View.OnClickListener {

    private Switch audioSwitch;
    private Button saveButton, logoutButton;
    private TextView privacyText, termsText, aboutUs;
    private WebView webview;
    private Button closeTap;
    private AlertDialog.Builder builder, aboutUsBuilder;
    private AlertDialog dialog, aboutUsDialog;

    private UserDetails userDetails;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;
    private GoogleSignInAccount googleSignInAccount;
    private GoogleSignInClient googleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);

        userDetails = UserDetails.getInstance(this);

        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();

        GoogleSignInOptions gso = new GoogleSignInOptions.
                Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
                build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);
//        FacebookSdk.sdkInitialize(getApplicationContext());

        audioSwitch = findViewById(R.id.us_audioSwitch);
        saveButton = findViewById(R.id.us_saveSettings);
        logoutButton = findViewById(R.id.us_logOut);
        privacyText = findViewById(R.id.us_privacyText);
        termsText = findViewById(R.id.us_termsText);
        aboutUs = findViewById(R.id.us_aboutUs);

        if (userDetails.isAudioStatus()) {
            audioSwitch.setChecked(true);
        } else {
            audioSwitch.setChecked(false);
        }

        saveButton.setOnClickListener(this);
        logoutButton.setOnClickListener(this);

        //Privacy policy and terms of use//

        final View tap = LayoutInflater.from(this).inflate(R.layout.terms_and_policy, null);
        webview = tap.findViewById(R.id.tap_webView);
        closeTap = tap.findViewById(R.id.tap_close);
        builder = new AlertDialog.Builder(UserSettings.this);
        builder.setView(tap);
        dialog = builder.create();

        aboutUsBuilder = new AlertDialog.Builder(this);
        final View aboutView = LayoutInflater.from(this).inflate(R.layout.about_us, null);
        aboutUsBuilder.setView(aboutView);
        aboutUsDialog = aboutUsBuilder.create();
        Objects.requireNonNull(aboutUsDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        privacyText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webview.setWebViewClient(new WebViewClient());
                webview.getSettings().setJavaScriptEnabled(true);
                webview.getSettings().setDomStorageEnabled(true);
                webview.setOverScrollMode(WebView.OVER_SCROLL_NEVER);
                webview.loadUrl("https://piccawall.blogspot.com/2020/08/pictri-privacy-policies.html");

                dialog.show();
                closeTap.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });
        termsText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webview.setWebViewClient(new WebViewClient());
                webview.getSettings().setJavaScriptEnabled(true);
                webview.getSettings().setDomStorageEnabled(true);
                webview.setOverScrollMode(WebView.OVER_SCROLL_NEVER);
                webview.loadUrl("https://piccawall.blogspot.com/2020/08/terms-and-conditions-for-pictri.html");

                dialog.show();
                closeTap.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });

        aboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aboutUsDialog.show();
            }
        });
        //*****//

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.us_saveSettings:
                if (audioSwitch.isChecked()) {
                    userDetails.setAudioStatus(true);
                } else {
                    userDetails.setAudioStatus(false);
                }
                new SaveData().execute(UserSettings.this);
                Intent intent = new Intent(UserSettings.this, HomeScreen.class);
                startActivity(intent);
                finish();
                break;
            case R.id.us_logOut:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                AlertDialog dialog;

                builder.setIcon(getDrawable(R.drawable.icon_info))
                        .setTitle("Sign Out")
                        .setMessage("Are you Sure?")
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("Sign Out", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                googleSignInAccount = GoogleSignIn.getLastSignedInAccount(UserSettings.this);
                                if (googleSignInAccount != null) {
                                    firebaseAuth.signOut();
                                    googleSignInClient.signOut().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Intent intent = new Intent(UserSettings.this, SignInAccount.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    });
                                } else {
                                    firebaseAuth.signOut();
                                    LoginManager.getInstance().logOut();
                                    Intent intent = new Intent(UserSettings.this, SignInAccount.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });

                dialog = builder.create();
                dialog.show();

                dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(getColor(R.color.buttonRed));
                dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(getColor(R.color.buttonGreen));

                break;
        }
    }
}