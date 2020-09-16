package com.piccastudios.pictri.QuestionFrames;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdSize;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.RewardedVideoAd;
import com.facebook.ads.RewardedVideoAdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.piccastudios.pictri.HomeScreens.HomeScreen;
import com.piccastudios.pictri.HomeScreens.SoundClass;
import com.piccastudios.pictri.QuestionBank.QuesModelPic;
import com.piccastudios.pictri.QuestionBank.QuestionSet;
import com.piccastudios.pictri.R;
import com.piccastudios.pictri.UserSection.SaveData;
import com.piccastudios.pictri.UserSection.UserDetails;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class QuestionFrameImage extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "qfi_logCheck";
    private TextView quesTimer;
    private long timeRemain = 70000;
    private CountDownTimer cT;
    private ProgressBar progressBar;

    //***** LifeLines *****//

    private CardView lifeLine1, lifeLine2, lifeLine3, lifeLine4;
    private RelativeLayout adView1, adView2, adView3, adView4;
    private TextView quesNo;
    //*** lifeLine Over ***//

    private Button optionA, optionB, optionC, optionD;
    private TextView quesText;
    private static String correctOption;
    private static String quesID;
    private ImageView quesImage;
    private ImageView starIcon, corIcon, wrIcon, lifeIcon;
    private TextView starCounts, lifeCounts;

    private QuestionSet questionSet;
    private int questionIndex = 0;
    private long points = 0;
    private Animation fadeInAnim;

    private List<QuesModelPic> quesList;

    //
    private UserDetails userDetails;
    private List<String> quesDone;
    Handler handler;

    SoundClass soundClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_frame_image);

        quesTimer = findViewById(R.id.qf_timer);
        progressBar = findViewById(R.id.qf_pb);
        corIcon = findViewById(R.id.qf_correctIcon);
        wrIcon = findViewById(R.id.qf_wrongIcon);
        starIcon = findViewById(R.id.qf_starIcon);
        starCounts = findViewById(R.id.qf_starText);

        // ****
        lifeIcon = findViewById(R.id.qf_lifeIcon);
        lifeCounts = findViewById(R.id.qf_lifeText);
        quesNo = findViewById(R.id.qf_quesNo);
        adView1 = findViewById(R.id.qf_adView1);
        adView2 = findViewById(R.id.qf_adView2);
        adView3 = findViewById(R.id.qf_adView3);
        adView4 = findViewById(R.id.qf_adView4);

        lifeLine1 = findViewById(R.id.qf_lifeLine1);
        lifeLine2 = findViewById(R.id.qf_lifeLine2);
        lifeLine3 = findViewById(R.id.qf_lifeLine3);
        lifeLine4 = findViewById(R.id.qf_lifeLine4);
        //**


        optionA = findViewById(R.id.qfi_optionA);
        optionB = findViewById(R.id.qfi_optionB);
        optionC = findViewById(R.id.qfi_optionC);
        optionD = findViewById(R.id.qfi_optionD);
        quesImage = findViewById(R.id.qfi_queImage);
        quesText = findViewById(R.id.qfi_queText);

        userDetails = UserDetails.getInstance(this);
        fadeInAnim = AnimationUtils.loadAnimation(this, R.anim.fade_in);

        //*****

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        AdView mAdView = findViewById(R.id.qf_adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        //*****

        timerStart(timeRemain);
        timerPause();

        questionSet = QuestionSet.getInstance(this);

        quesList = new ArrayList<>();
        quesList = questionSet.getQuesModelPicList();

        quesDone = new ArrayList<>();
        quesDone = userDetails.getQuesDone();

        soundClass = SoundClass.getInstance(this);

        lifeCounts.setText(String.valueOf(questionSet.getLife()));
        setLifelines();

        setQuestion();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timerPause();
        Log.d(TAG, "onStop: Activity Stop and timer pause");
    }

    public void timerStart(long timeLength) {
        cT = new CountDownTimer(timeLength, 1000) {

            public void onTick(long millisUntilFinished) {
                timeRemain = millisUntilFinished;
                progressBar.setProgress((int) ((timeRemain / 1000) * (142)));
                if (timeRemain < 67000) {
                    quesTimer.setTextColor(getColor(R.color.textYellow));
                }
                if (timeRemain < 21000) {
                    quesTimer.setTextColor(getColor(R.color.textRed));
                }
//                String min = String.format("%02d", millisUntilFinished/60000);
                int sec = (int) ((millisUntilFinished) / 1000);
                quesTimer.setText(String.format("%02d", sec));
            }

            public void onFinish() {
                if (userDetails.isAudioStatus()) {
                    soundClass.playSound(3);
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(QuestionFrameImage.this);
                AlertDialog dialog;
                builder.setTitle(" Level Over")
                        .setMessage("Times Up!")
                        .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                questionSet.setPoints(points);
                                startActivity(new Intent(QuestionFrameImage.this, LevelOver.class));
                                finish();
                            }
                        })
                        .setIcon(getDrawable(R.drawable.icon_addtime));
                dialog = builder.create();
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();

                dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(getColor(R.color.buttonGreen));
            }
        };
        cT.start();
    }

    public void timerPause() {
        cT.cancel();
    }

    private void timerResume() {
        timerStart(timeRemain);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.qfi_optionA:
                if (!checkAnswer("optionA")) {
                    optionA.setBackground(getDrawable(R.drawable.rec_button_red));
                } else {
                    optionA.setBackground(getDrawable(R.drawable.rec_button_green));
                }
                break;
            case R.id.qfi_optionB:
                if (!checkAnswer("optionB")) {
                    optionB.setBackground(getDrawable(R.drawable.rec_button_red));
                } else {
                    optionB.setBackground(getDrawable(R.drawable.rec_button_green));
                }
                break;
            case R.id.qfi_optionC:
                if (!checkAnswer("optionC")) {
                    optionC.setBackground(getDrawable(R.drawable.rec_button_red));
                } else {
                    optionC.setBackground(getDrawable(R.drawable.rec_button_green));
                }
                break;
            case R.id.qfi_optionD:
                if (!checkAnswer("optionD")) {
                    optionD.setBackground(getDrawable(R.drawable.rec_button_red));
                } else {
                    optionD.setBackground(getDrawable(R.drawable.rec_button_green));
                }
                break;
            //**
            case R.id.qf_lifeLine1:
                if (userDetails.isAudioStatus()) {
                    soundClass.playSound(3);
                }
                questionSet.setLlUpdate1(true);
                userDetails.setLifeLine1(false);
                userDetails.setLLTS1(System.currentTimeMillis());
                deselectWrongOptions();
                adView1.setVisibility(View.VISIBLE);
                break;
            case R.id.qf_lifeLine2:
                if (userDetails.isAudioStatus()) {
                    soundClass.playSound(3);
                }
                questionSet.setLlUpdate2(true);
                userDetails.setLifeLine2(false);
                userDetails.setLLTS2(System.currentTimeMillis());
                removeTwoOptions();
                adView2.setVisibility(View.VISIBLE);
                break;
            case R.id.qf_lifeLine3:
                if (userDetails.isAudioStatus()) {
                    soundClass.playSound(3);
                }
                questionSet.setLlUpdate3(true);
                userDetails.setLifeLine3(false);
                userDetails.setLLTS3(System.currentTimeMillis());
                adView3.setVisibility(View.VISIBLE);
                questionIndex += 1;
                timerPause();
                timeRemain -= 1;
                setQuestion();
                break;
            case R.id.qf_lifeLine4:
                if (userDetails.isAudioStatus()) {
                    soundClass.playSound(3);
                }
                questionSet.setLlUpdate4(true);
                userDetails.setLifeLine4(false);
                userDetails.setLLTS3(System.currentTimeMillis());
                timerPause();
                timeRemain -= 1;
                timeRemain += 15000;
                if (timeRemain >= 100000) {
                    timeRemain = 100000;
                }
                timerResume();
                adView4.setVisibility(View.VISIBLE);
                break;
            //*
        }
    }

    private void removeTwoOptions() {
        if (correctOption.equals("optionA")) {
            optionC.setBackground(getDrawable(R.drawable.rec_button_deselect));
            optionD.setBackground(getDrawable(R.drawable.rec_button_deselect));
        } else {
            if (correctOption.equals("optionB")) {
                optionC.setBackground(getDrawable(R.drawable.rec_button_deselect));
                optionD.setBackground(getDrawable(R.drawable.rec_button_deselect));
            } else {
                if (correctOption.equals("optionC")) {
                    optionA.setBackground(getDrawable(R.drawable.rec_button_deselect));
                    optionB.setBackground(getDrawable(R.drawable.rec_button_deselect));
                } else {
                    if (correctOption.equals("optionD")) {
                        optionA.setBackground(getDrawable(R.drawable.rec_button_deselect));
                        optionB.setBackground(getDrawable(R.drawable.rec_button_deselect));
                    }
                }
            }
        }
    }

    private void deselectWrongOptions() {

        if (correctOption.equals("optionA")) {
            optionB.setBackground(getDrawable(R.drawable.rec_button_deselect));
            optionC.setBackground(getDrawable(R.drawable.rec_button_deselect));
            optionD.setBackground(getDrawable(R.drawable.rec_button_deselect));
        } else {
            if (correctOption.equals("optionB")) {
                optionA.setBackground(getDrawable(R.drawable.rec_button_deselect));
                optionC.setBackground(getDrawable(R.drawable.rec_button_deselect));
                optionD.setBackground(getDrawable(R.drawable.rec_button_deselect));
            } else {
                if (correctOption.equals("optionC")) {
                    optionA.setBackground(getDrawable(R.drawable.rec_button_deselect));
                    optionB.setBackground(getDrawable(R.drawable.rec_button_deselect));
                    optionD.setBackground(getDrawable(R.drawable.rec_button_deselect));
                } else {
                    if (correctOption.equals("optionD")) {
                        optionA.setBackground(getDrawable(R.drawable.rec_button_deselect));
                        optionB.setBackground(getDrawable(R.drawable.rec_button_deselect));
                        optionC.setBackground(getDrawable(R.drawable.rec_button_deselect));
                    }
                }
            }
        }
    }

    private boolean checkAnswer(String answer) {

        optionA.setClickable(false);
        optionB.setClickable(false);
        optionC.setClickable(false);
        optionD.setClickable(false);

        handler = new Handler();
        questionSet.setPoints(points);
        //
        userDetails.setTotalQuestions(userDetails.getTotalQuestions() + 1);

        if (correctOption.equals(answer)) {

            if (userDetails.isAudioStatus()) {
                soundClass.playSound(4);
            }
            corIcon.setVisibility(View.VISIBLE);
            corIcon.startAnimation(fadeInAnim);

            starIcon.startAnimation(fadeInAnim);
            points += 10;
            starCounts.setText(String.valueOf(points));

            //
            userDetails.setTotalCorrectAnswer(userDetails.getTotalCorrectAnswer() + 1);
            quesDone.add(quesID);
            userDetails.setQuesDone(quesDone);

            timerPause();
            timeRemain -= 1;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    questionIndex += 1;
                    if (questionIndex == 10) {
                        questionSet.setPoints(points);
                        //
                        userDetails.setLevelsCompleted(userDetails.getLevelsCompleted() + 1);
                        startActivity(new Intent(QuestionFrameImage.this, LevelOver.class));
                        finish();
                    } else {
                        setQuestion();
                    }
                }
            }, 1000);
            return true;
        } else {

            if (userDetails.isAudioStatus()) {
                soundClass.playSound(5);
            }
            wrIcon.setVisibility(View.VISIBLE);
            wrIcon.startAnimation(fadeInAnim);

            //***** Deduct life *****//
            lifeIcon.startAnimation(fadeInAnim);
            if (questionSet.getLife() > 1) {
                questionSet.setLife(questionSet.getLife() - 1);
                lifeCounts.setText(String.valueOf(questionSet.getLife()));

                timerPause();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        questionIndex += 1;
                        if (questionIndex == 10) {
                            questionSet.setPoints(points);
                            //
                            userDetails.setLevelsCompleted(userDetails.getLevelsCompleted() + 1);
                            startActivity(new Intent(QuestionFrameImage.this, LevelOver.class));
                            finish();
                        } else {
                            setQuestion();
                        }
                    }
                }, 1000);

            } else {

                lifeCounts.setText("0");
                questionSet.setLife(0);
                timerPause();
                timeRemain -= 1;
                if (questionIndex < 9) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    AlertDialog dialog;
                    builder.setTitle(" Level Over")
                            .setMessage("You have Used All of Your Lifes.")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    startActivity(new Intent(QuestionFrameImage.this, LevelOver.class));
                                    finish();
                                }
                            })
                            .setIcon(getDrawable(R.drawable.icon_life));
                    dialog = builder.create();
                    dialog.setCancelable(false);
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();

                    dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(getColor(R.color.buttonGreen));
                } else {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            questionSet.setPoints(points);
                            //
                            userDetails.setLevelsCompleted(userDetails.getLevelsCompleted() + 1);
                            startActivity(new Intent(QuestionFrameImage.this, LevelOver.class));
                            finish();
                        }
                    }, 1000);

                }
            }
            //***** Life Deduct Over *****//

            return false;
        }
    }

    private void setQuestion() {

        optionA.setClickable(true);
        optionB.setClickable(true);
        optionC.setClickable(true);
        optionD.setClickable(true);

        quesNo.setText(String.valueOf(questionIndex + 1));

        if (questionIndex == 10) {
            questionSet.setPoints(points);
            //
            userDetails.setLevelsCompleted(userDetails.getLevelsCompleted() + 1);
            startActivity(new Intent(QuestionFrameImage.this, LevelOver.class));
            finish();
        }
        if (questionIndex == 9) {
            quesNo.setTextColor(getColor(R.color.textYellow));
        }

        optionA.setOnClickListener(this);
        optionB.setOnClickListener(this);
        optionC.setOnClickListener(this);
        optionD.setOnClickListener(this);

        //
        lifeLine1.setOnClickListener(this);
        lifeLine2.setOnClickListener(this);
        lifeLine3.setOnClickListener(this);
        lifeLine4.setOnClickListener(this);

        adView1.setOnClickListener(this);
        adView2.setOnClickListener(this);
        adView3.setOnClickListener(this);
        adView4.setOnClickListener(this);

        //Set theme to default
        optionA.setBackground(getDrawable(R.drawable.rec_button_white));
        optionB.setBackground(getDrawable(R.drawable.rec_button_white));
        optionC.setBackground(getDrawable(R.drawable.rec_button_white));
        optionD.setBackground(getDrawable(R.drawable.rec_button_white));
        corIcon.setVisibility(View.INVISIBLE);
        wrIcon.setVisibility(View.INVISIBLE);
        //

        QuesModelPic quesModelPic;
        quesModelPic = quesList.get(questionIndex);

        Picasso.get()
                .load(quesModelPic.getQuesImageUrl())
                .resize(500, 300)
                .centerCrop()
                .into(quesImage, new com.squareup.picasso.Callback() {

                    @Override
                    public void onSuccess() {
                        timerResume();
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });

        quesText.setText(quesModelPic.getQuestion());
        optionA.setText(quesModelPic.getOptionA());
        optionB.setText(quesModelPic.getOptionB());
        optionC.setText(quesModelPic.getOptionC());
        optionD.setText(quesModelPic.getOptionD());
        quesID = quesModelPic.getQuesId();

        correctOption = quesModelPic.getCorrectOption();

    }

    private void setLifelines() {

        if (!userDetails.isLifeLine1()) {
            adView1.setVisibility(View.VISIBLE);
        }

        if (!userDetails.isLifeLine2()) {
            adView2.setVisibility(View.VISIBLE);
        }

        if (!userDetails.isLifeLine3()) {
            adView3.setVisibility(View.VISIBLE);
        }

        if (!userDetails.isLifeLine4()) {
            adView4.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog dialog;

        builder.setIcon(getDrawable(R.drawable.icon_info))
                .setTitle("Exit Level")
                .setMessage("Sure to leave the Level in between?")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        timerPause();
                        new SaveData().execute(QuestionFrameImage.this);
                        startActivity(new Intent(QuestionFrameImage.this, HomeScreen.class));
                        finish();
                    }
                });

        dialog = builder.create();
        dialog.show();

        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(getColor(R.color.buttonRed));
        dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(getColor(R.color.buttonGreen));
    }

}