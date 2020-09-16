package com.piccastudios.pictri;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.piccastudios.pictri.Account.SignInAccount;
import com.piccastudios.pictri.Genre.GenreList;
import com.piccastudios.pictri.Genre.GenreListModel;
import com.piccastudios.pictri.Genre.RVAdapterGenre;
import com.piccastudios.pictri.Genre.SelectGenre;
import com.piccastudios.pictri.HomeScreens.HomeScreen;
import com.piccastudios.pictri.HomeScreens.UserModel;
import com.piccastudios.pictri.UserSection.UserDetails;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "logCheck";
    private CardView mainLogo;

    private UserDetails userDetails;

    //Firebase
    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference crUserDetails;
    private DocumentReference drUserDetails;
    private CollectionReference crGenre = db.collection("genre");
    private DocumentReference drGenre = crGenre.document("genreList");

    private boolean STATUS = false;
    private Handler handlerAnim, handler;
    private Runnable runnable;
    private Animation fadeIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainLogo = findViewById(R.id.main_cardView);

        fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);

        userDetails = UserDetails.getInstance(this);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        crUserDetails = db.collection("userDetails");

        handler = new Handler();
        handlerAnim = new Handler();

        runnable = new Runnable() {
            @Override
            public void run() {
                mainLogo.startAnimation(fadeIn);
                if (!STATUS) {
                    handlerAnim.postDelayed(this, 1500);
                } else {
                    handlerAnim.removeCallbacks(runnable);
                }
            }
        };

        handlerAnim.postDelayed(runnable, 1500);

        if (user != null) {
            drUserDetails = crUserDetails.document(user.getUid());

            drUserDetails.get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot snapshot) {
                            if (snapshot.exists()) {
                                UserModel userModel = snapshot.toObject(UserModel.class);

                                assert userModel != null;
                                userDetails.setUserName(userModel.getUserName());
                                userDetails.setUserId(userModel.getUserId());
                                if (userModel.getUserPhoto() != null) {
                                    userDetails.setUserPhoto(userModel.getUserPhoto());
                                }
                                userDetails.setBadgeName(userModel.getBadgeName());
                                userDetails.setAppliedBadge(userModel.getAppliedBadge());
                                userDetails.setQuesDone(userModel.getQuesDone());

                                userDetails.setLastSignIn(userModel.getLastSignIn());
                                userDetails.setMaxSignInStreak(userModel.getMaxSignInStreak());
                                userDetails.setTotalSignIn(userModel.getTotalSignIn());
                                userDetails.setLLTS1(userModel.getLLTS1());
                                userDetails.setLLTS2(userModel.getLLTS2());
                                userDetails.setLLTS3(userModel.getLLTS3());
                                userDetails.setLLTS4(userModel.getLLTS4());
                                userDetails.setLifeLine1(userModel.isLifeLine1());
                                userDetails.setLifeLine2(userModel.isLifeLine2());
                                userDetails.setLifeLine3(userModel.isLifeLine3());
                                userDetails.setLifeLine4(userModel.isLifeLine4());
                                userDetails.setAudioStatus(userModel.isAudioStatus());

                                long currentTime = System.currentTimeMillis();

                                if (currentTime - userDetails.getLLTS1() > 10800000) {
                                    userDetails.setLifeLine1(true);
                                } else
                                    userDetails.setLifeLine1(userModel.isLifeLine1());

                                if (currentTime - userDetails.getLLTS2() > 10800000) {
                                    userDetails.setLifeLine2(true);
                                } else
                                    userDetails.setLifeLine2(userModel.isLifeLine2());

                                if (currentTime - userDetails.getLLTS3() > 10800000) {
                                    userDetails.setLifeLine3(true);
                                } else
                                    userDetails.setLifeLine3(userModel.isLifeLine3());


                                if (Math.abs(currentTime - userDetails.getLLTS4()) > 10800000) {
                                    userDetails.setLifeLine4(true);
                                } else {
                                    userDetails.setLifeLine4(userModel.isLifeLine4());
                                }


                                userDetails.setTotalCorrectAnswer(userModel.getTotalCorrectAnswer());
                                userDetails.setTotalQuestions(userModel.getTotalQuestions());
                                userDetails.setTotalStars(userModel.getTotalStars());
                                userDetails.setLevelsCompleted(userModel.getLevelsCompleted());

                                boolean dateCompareResult = areEqual(userModel.getLastSignIn(), currentTime);
                                if (!dateCompareResult) {
                                    if (currentTime - userModel.getLastSignIn() < 172800000) {
                                        userDetails.setTotalSignIn(userModel.getTotalSignIn() + 1);
                                        userDetails.setLastSignIn(currentTime);
                                        if (userDetails.getTotalSignIn() > userDetails.getMaxSignInStreak()) {
                                            userDetails.setMaxSignInStreak(userDetails.getTotalSignIn());
                                        }
                                    } else {
                                        userDetails.setTotalSignIn(1);
                                        userDetails.setLastSignIn(currentTime);
                                    }
                                }

                                STATUS = true;
                                //Move to Next Activity
                                drGenre.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot snapshot) {
                                        if (snapshot.exists()) {

                                            GenreListModel glModel = snapshot.toObject(GenreListModel.class);
                                            GenreList genreList = GenreList.getInstance(MainActivity.this);
                                            assert glModel != null;
                                            genreList.setTextGenre(glModel.getTextGenre());
                                            genreList.setPictureGenre(glModel.getPictureGenre());
                                            genreList.setTrueFalseGenre(glModel.getTrueFalseGenre());
                                            genreList.setOddOneGenre(glModel.getOddOneGenre());
                                            genreList.setWbCategory(glModel.getWbCategory());
                                            genreList.setWbGenre(glModel.getWbGenre());
                                            genreList.setPosterUrl(glModel.getPosterUrl());

                                        }
                                        startActivity(new Intent(MainActivity.this, HomeScreen.class));
                                        finish();
                                    }
                                });
                                //

                            } else {
                                Log.d(TAG, "onSuccess: Snapshot notExist");
                            }


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, "onFailure: " + e.toString());
                }
            });
        } else {
            STATUS = true;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(MainActivity.this, SignInAccount.class));
                    finish();
                }
            }, 1500);
        }

    }

    private boolean areEqual(long oldTime, long currentTime) {
        Calendar c1 = Calendar.getInstance();
        c1.setTime(new Date(oldTime));

        Calendar c2 = Calendar.getInstance();
        c2.setTime(new Date(currentTime));

        return ((c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR))
                &&
                (c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)));

    }

}