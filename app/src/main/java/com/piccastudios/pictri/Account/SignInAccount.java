package com.piccastudios.pictri.Account;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;

import com.facebook.internal.CallbackManagerImpl;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.piccastudios.pictri.HomeScreens.HomeScreen;
import com.piccastudios.pictri.HomeScreens.UserModel;
import com.piccastudios.pictri.MainActivity;
import com.piccastudios.pictri.QuestionFrames.QFSelectOddOne;
import com.piccastudios.pictri.R;
import com.piccastudios.pictri.UserSection.SaveData;
import com.piccastudios.pictri.UserSection.UserDetails;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class SignInAccount extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "si_logCheck";
    private RelativeLayout googleSI, fbSI;
    private GoogleSignInClient mGoogleSignInClient;
    private final static int RC_SIGN_IN = 1;
    private FirebaseAuth mAuth;
    private CallbackManager callbackManager;

    private UserDetails userDetails;
    private UserModel userModel;

    //firebase Reference
    private FirebaseFirestore db;
    private CollectionReference crUserDetails;
    private DocumentReference drUserDetails;

    private CheckBox privacyCB, termsCB;
    private TextView privacyText, termsText, aboutUs;

    private RelativeLayout signInProgress;
    private WebView webview;
    private Button closeTap;
    private AlertDialog.Builder builder, aboutUsBuilder;
    private AlertDialog dialog, aboutUsDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_account);

        callbackManager = CallbackManager.Factory.create();

        db = FirebaseFirestore.getInstance();
        crUserDetails = db.collection("userDetails");

        googleSI = findViewById(R.id.ca_googleSI);
        fbSI = findViewById(R.id.ca_facebookSI);
        privacyCB = findViewById(R.id.ca_privacyCB);
        termsCB = findViewById(R.id.ca_termsCB);
        termsText = findViewById(R.id.ca_termsText);
        privacyText = findViewById(R.id.ca_privacyText);
        aboutUs = findViewById(R.id.ca_about);
        signInProgress = findViewById(R.id.ca_signInProgress);

        userDetails = UserDetails.getInstance(this);

        mAuth = FirebaseAuth.getInstance();
        createRequest();

        googleSI.setOnClickListener(this);
        //Facebook login
        fbSI.setOnClickListener(this);
        signInProgress.setOnClickListener(this);


        //Privacy policy and terms of use//

        final View tap = LayoutInflater.from(this).inflate(R.layout.terms_and_policy, null);
        webview = tap.findViewById(R.id.tap_webView);
        closeTap = tap.findViewById(R.id.tap_close);
        builder = new AlertDialog.Builder(SignInAccount.this);
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
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog dialog;

        builder.setIcon(getDrawable(R.drawable.icon_info))
                .setTitle("Exit")
                .setMessage("Sure to Exit PicTri!")
                .setNegativeButton("Play More", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SignInAccount.this.finishAffinity();
                    }
                });

        dialog = builder.create();
        dialog.show();

        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(getColor(R.color.buttonRed));
        dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(getColor(R.color.buttonGreen));
    }

    private void createRequest() {

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.ca_googleSI:
                if (privacyCB.isChecked() && termsCB.isChecked()) {
                    signIn();
                } else {
                    Toast.makeText(this, "Please Accept Privacy Policy \nand Terms of Use",
                            Toast.LENGTH_LONG)
                            .show();
                }
                break;
            case R.id.ca_facebookSI:
                if (privacyCB.isChecked() && termsCB.isChecked()) {
                    LoginManager.getInstance()
                            .logInWithReadPermissions(SignInAccount.this,
                                    Arrays.asList("email", "public_profile"));
                    LoginManager.getInstance().registerCallback(callbackManager,
                            new FacebookCallback<LoginResult>() {
                                @Override
                                public void onSuccess(LoginResult loginResult) {
                                    handleFacebookAccessToken(loginResult.getAccessToken());
                                }

                                @Override
                                public void onCancel() {
                                    // ...
                                }
                                @Override
                                public void onError(FacebookException error) {
                                    // ...
                                }
                            });
                } else {
                    Toast.makeText(this, "Please Accept Privacy Policy \nand Terms of Use",
                            Toast.LENGTH_LONG)
                            .show();
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                signInProgress.setVisibility(View.VISIBLE);
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
//                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
//                // Google Sign In failed, update UI appropriately
//                Log.w(TAG, "Google sign in failed", e);
            }
        }

        //facebook result
        if (requestCode == CallbackManagerImpl.RequestCodeOffset.Login.toRequestCode()) {
            signInProgress.setVisibility(View.VISIBLE);
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            FirebaseUser user = mAuth.getCurrentUser();
                            assert user != null;
                            createUserDatabase(user);
                        } else {
//                            // If sign in fails, display a message to the user.
//                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                        }

                    }
                });
    }

    private void handleFacebookAccessToken(AccessToken token) {


        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            FirebaseUser user = mAuth.getCurrentUser();
                            assert user != null;
                            createUserDatabase(user);
                        } else {
                            // If sign in fails, display a message to the user.

                            Toast.makeText(SignInAccount.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    private void createUserDatabase(final FirebaseUser user) {

        drUserDetails = crUserDetails.document(user.getUid());

        crUserDetails.whereEqualTo("userId", user.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().size() > 0) {

                                List<DocumentSnapshot> snapshotList;
                                snapshotList = task.getResult().getDocuments();

                                DocumentSnapshot snapshot = snapshotList.get(0);

                                if (snapshot.exists()) {

                                    startActivity(new Intent(SignInAccount.this, MainActivity.class));
                                    finish();
                                }
                            } else {
                                userModel = new UserModel();

                                List<String> newList = new ArrayList<>();
                                userModel.setQuesDone(newList);

                                userModel.setUserName(user.getDisplayName());
                                userModel.setUserId(user.getUid());
                                userModel.setLastSignIn(System.currentTimeMillis());
                                userModel.setLLTS1(System.currentTimeMillis());
                                userModel.setLLTS2(System.currentTimeMillis());
                                userModel.setLLTS3(System.currentTimeMillis());
                                userModel.setLLTS4(System.currentTimeMillis());
                                if (user.getPhotoUrl() != null) {
                                    userModel.setUserPhoto(user.getPhotoUrl().toString());
                                }

                                drUserDetails = crUserDetails.document(userModel.getUserId());
                                drUserDetails.set(userModel)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Intent intent = new Intent(SignInAccount.this, MainActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
//                                                Log.d(TAG, "onFailure: " + e.toString());
                                            }
                                        });
                            }
                        } else {
//                            Log.d(TAG, "onComplete: task not successful");
                        }
                    }
                });

    }


}