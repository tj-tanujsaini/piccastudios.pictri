package com.piccastudios.pictri.QuestionFrames;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.RewardedVideoAd;
import com.facebook.ads.RewardedVideoAdListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;
import com.piccastudios.pictri.Category.SelectCategory;
import com.piccastudios.pictri.HomeScreens.HomeScreen;
import com.piccastudios.pictri.HomeScreens.SoundClass;
import com.piccastudios.pictri.HomeScreens.UserModel;
import com.piccastudios.pictri.QuestionBank.QuestionSet;
import com.piccastudios.pictri.R;
import com.piccastudios.pictri.UserSection.SaveData;
import com.piccastudios.pictri.UserSection.UserDetails;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;

public class LevelOver extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "logCheck";
    private FirebaseAuth auth;
    private FirebaseUser user;
    //firebase
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference crUserDetails = db.collection("userDetails");
    private DocumentReference drUserDetails;

    private QuestionSet questionSet;

    private ImageView starLeft, starCenter, starRight;
    private ImageView starLeft2, starCenter2, starRight2;
    private TextView points, totalPoints, multiplyThree;
    private LinearLayout homeIcon, nextIcon;
    private long levelPoints;
    private long setPoints = 0;

    private Runnable task;
    private Handler handler;

    private Animation fadeInAnim;
    private UserDetails userDetails;
    private SoundClass soundClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_over);

        starLeft = findViewById(R.id.alo_starLeft);
        starRight = findViewById(R.id.alo_starRight);
        starCenter = findViewById(R.id.alo_starCenter);
        starLeft2 = findViewById(R.id.alo_starLeft2);
        starRight2 = findViewById(R.id.alo_starRight2);
        starCenter2 = findViewById(R.id.alo_starCenter2);
        points = findViewById(R.id.alo_points);
        totalPoints = findViewById(R.id.alo_totalStars);
        multiplyThree = findViewById(R.id.alo_multiplyThree);
        homeIcon = findViewById(R.id.alo_home);
        nextIcon = findViewById(R.id.alo_next);

        userDetails = UserDetails.getInstance(this);
        soundClass = SoundClass.getInstance(this);
        fadeInAnim = AnimationUtils.loadAnimation(this, R.anim.fade_in);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        drUserDetails = crUserDetails.document(userDetails.getUserId());

        questionSet = QuestionSet.getInstance(this);
        levelPoints = questionSet.getPoints();

        handler = new Handler();
        task = new Runnable() {
            @Override
            public void run() {
                points.setText(String.valueOf(setPoints));
                setPoints += 1;

                if (userDetails.isAudioStatus()) {
                    soundClass.playSound(0);
                }

                if (setPoints == 30) {
                    starLeft2.setVisibility(View.INVISIBLE);
                    starLeft.startAnimation(fadeInAnim);
                }
                if (setPoints == 50) {
                    starRight2.setVisibility(View.INVISIBLE);
                    starRight.startAnimation(fadeInAnim);
                }
                if (setPoints == 80) {
                    levelPoints = levelPoints * 2;
                    starCenter2.setVisibility(View.INVISIBLE);
                    starCenter.startAnimation(fadeInAnim);
                    multiplyThree.setVisibility(View.VISIBLE);
                    multiplyThree.startAnimation(fadeInAnim);
                }
                if (setPoints >= levelPoints) {

                    points.setText(String.valueOf(setPoints));
                    if (setPoints == 1) {
                        points.setText("000");
                    }
                    setPoints = userDetails.getTotalStars() + levelPoints;
                    userDetails.setTotalStars(setPoints);

                    totalPoints.setText(String.valueOf(setPoints));
                    totalPoints.startAnimation(fadeInAnim);

                    userModelData();

                    handler.removeCallbacks(task);

                } else {
                    handler.postDelayed(this, 30);
                }
            }
        };
        handler.post(task);

        homeIcon.setOnClickListener(this);
        nextIcon.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.alo_home:
                startActivity(new Intent(LevelOver.this, HomeScreen.class));
                finish();
                break;
            case R.id.alo_next:
                startActivity(new Intent(LevelOver.this, QuestionLoad.class));
                finish();
                break;
        }
    }

    public void userModelData() {

        Map<String, Object> updateData = new HashMap<>();

        updateData.put("totalQuestions", userDetails.getTotalQuestions());
        updateData.put("totalCorrectAnswer", userDetails.getTotalCorrectAnswer());
        updateData.put("levelsCompleted", userDetails.getLevelsCompleted());
        updateData.put("lifeLine1", userDetails.isLifeLine1());
        updateData.put("lifeLine2", userDetails.isLifeLine2());
        updateData.put("lifeLine3", userDetails.isLifeLine3());
        updateData.put("lifeLine4", userDetails.isLifeLine4());
        updateData.put("totalStars", userDetails.getTotalStars());
        updateData.put("quesDone", userDetails.getQuesDone());

        if (questionSet.isLlUpdate1()) {
            questionSet.setLlUpdate1(false);
            updateData.put("llts1", userDetails.getLLTS1());
        }

        if (questionSet.isLlUpdate2()) {
            questionSet.setLlUpdate2(false);
            updateData.put("llts2", userDetails.getLLTS2());
        }

        if (questionSet.isLlUpdate3()) {
            questionSet.setLlUpdate3(false);
            updateData.put("llts3", userDetails.getLLTS3());
        }

        if (questionSet.isLlUpdate4()) {
            questionSet.setLlUpdate4(false);
            updateData.put("llts4", userDetails.getLLTS4());
        }

        drUserDetails.update(updateData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        if (questionSet.getLife() == 0) {
                            nextIcon.setVisibility(View.INVISIBLE);
                        } else {
                            nextIcon.setVisibility(View.VISIBLE);
                        }
                        homeIcon.setVisibility(View.VISIBLE);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("logCheck", "onFailure: Failed to update");
                    }
                });

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(LevelOver.this, HomeScreen.class));
        finish();
    }
}