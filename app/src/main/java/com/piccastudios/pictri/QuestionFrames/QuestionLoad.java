package com.piccastudios.pictri.QuestionFrames;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.RewardedVideoAd;
import com.facebook.ads.RewardedVideoAdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.firebase.firestore.auth.User;
import com.piccastudios.pictri.HomeScreens.HomeScreen;
import com.piccastudios.pictri.QuestionBank.QuesModelMCQ;
import com.piccastudios.pictri.QuestionBank.QuesModelOddOne;
import com.piccastudios.pictri.QuestionBank.QuesModelPic;
import com.piccastudios.pictri.QuestionBank.QuesModelTrueFalse;
import com.piccastudios.pictri.QuestionBank.QuestionSet;
import com.piccastudios.pictri.R;
import com.piccastudios.pictri.UserSection.AchievementsScreen;
import com.piccastudios.pictri.UserSection.Profile;
import com.piccastudios.pictri.UserSection.SaveData;
import com.piccastudios.pictri.UserSection.UserDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class QuestionLoad extends AppCompatActivity implements View.OnClickListener {

    private ImageView imageClock, imageBell, noQuesIcon;
    private TextView readyText, clockText, noQuesText;
    private Button beginButton, homeButton;
    private Animation rotateAnim, fadeIn, rotateHalf;

    private QuestionSet questionSet;
    private int questionLength = 0;
    String urlAPI;

    private TextView lifeLineText;

    private RelativeLayout adLL1, adLL2, adLL3, adLL4;
    private RequestQueue requestQueue;
    private List<QuesModelPic> quesListPics;
    private List<QuesModelMCQ> quesListMCQ;
    private List<QuesModelOddOne> quesListOddOne;
    private List<QuesModelTrueFalse> quesListTrueFalse;

    private List<String> quesDone;
    private UserDetails userDetails;

    private AdView mAdView;
    private RewardedAd rewardedAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_load);

        rotateAnim = AnimationUtils.loadAnimation(this, R.anim.rotate_anim);
        rotateHalf = AnimationUtils.loadAnimation(this, R.anim.rotate_anim_half);
        fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);

        requestQueue = Volley.newRequestQueue(this);

        imageClock = findViewById(R.id.aql_clock);
        imageBell = findViewById(R.id.aql_bell);
        readyText = findViewById(R.id.aql_readyText);
        clockText = findViewById(R.id.aql_clockText);
        beginButton = findViewById(R.id.aql_beginButton);
        noQuesIcon = findViewById(R.id.aql_noQuesIcon);
        noQuesText = findViewById(R.id.aql_noQuesText);
        homeButton = findViewById(R.id.aql_homeButton);
        adLL1 = findViewById(R.id.ap_adView_ll1);
        adLL2 = findViewById(R.id.ap_adView_ll2);
        adLL3 = findViewById(R.id.ap_adView_ll3);
        adLL4 = findViewById(R.id.ap_adView_ll4);
        lifeLineText = findViewById(R.id.ap_lifelineText);

        //*****

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        rewardedAd = createAndLoadRewardedAd();

        //*****

        userDetails = UserDetails.getInstance(this);

        imageClock.startAnimation(rotateAnim);
        questionSet = QuestionSet.getInstance(this);

        quesDone = new ArrayList<>();
        quesDone = userDetails.getQuesDone();

        switch (questionSet.getCategory()) {
            case "Multiple Choice":
                urlAPI = "https://twvht2sim7.execute-api.ap-south-1.amazonaws.com/dev/getall/text/questions";
                break;
            case "True / False":
                urlAPI = "https://on9h4mm2re.execute-api.ap-south-1.amazonaws.com/dev/getall/truefalse/questions";
                break;
            case "Select Odd One":
                urlAPI = "https://l028rcsygh.execute-api.ap-south-1.amazonaws.com/dev/getall/oddone/questions";
                break;
            case "Pictographic":
                urlAPI = "https://rbcjvmh26g.execute-api.ap-south-1.amazonaws.com/dev/getall/questions";
                break;
        }

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(urlAPI,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        boolean listComplete = addQuesToList(response);

                        if (listComplete) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    imageClock.clearAnimation();
                                    clockText.setVisibility(View.INVISIBLE);
                                    imageClock.setVisibility(View.INVISIBLE);
                                    imageBell.setVisibility(View.VISIBLE);
                                    readyText.setVisibility(View.VISIBLE);
                                    beginButton.setVisibility(View.VISIBLE);
                                    beginButton.startAnimation(fadeIn);
                                    readyText.startAnimation(fadeIn);

                                    imageBell.startAnimation(rotateHalf);
                                }
                            }, 1000);
                        } else {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    imageClock.clearAnimation();
                                    clockText.setVisibility(View.INVISIBLE);
                                    imageClock.setVisibility(View.INVISIBLE);
                                    imageBell.setVisibility(View.INVISIBLE);
                                    readyText.setVisibility(View.INVISIBLE);
                                    beginButton.setVisibility(View.INVISIBLE);
                                    noQuesIcon.setVisibility(View.VISIBLE);
                                    noQuesText.setVisibility(View.VISIBLE);
                                    homeButton.setVisibility(View.VISIBLE);
                                    homeButton.startAnimation(fadeIn);
                                    noQuesText.startAnimation(fadeIn);
                                    noQuesIcon.startAnimation(fadeIn);

                                }
                            }, 1000);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        requestQueue.add(jsonArrayRequest);

        beginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startQuestionActivity();
            }
        });
        adLL1.setOnClickListener(this);
        adLL2.setOnClickListener(this);
        adLL3.setOnClickListener(this);
        adLL4.setOnClickListener(this);
        homeButton.setOnClickListener(this);


        //********* LifeLines //**********

        if (!userDetails.isLifeLine1()) {
            adLL1.setVisibility(View.VISIBLE);
        } else {
            adLL1.setVisibility(View.INVISIBLE);
        }
        if (!userDetails.isLifeLine2()) {
            adLL2.setVisibility(View.VISIBLE);
        } else {
            adLL2.setVisibility(View.INVISIBLE);
        }
        if (!userDetails.isLifeLine3()) {
            adLL3.setVisibility(View.VISIBLE);
        } else {
            adLL3.setVisibility(View.INVISIBLE);
        }
        if (!userDetails.isLifeLine4()) {
            adLL4.setVisibility(View.VISIBLE);
        } else {
            adLL4.setVisibility(View.INVISIBLE);
        }

        //********** lifeline over //**********

    }

    private boolean addQuesToList(JSONArray response) {
        switch (questionSet.getCategory()) {

            case "Multiple Choice":

                quesListMCQ = new ArrayList<>();
                for (int i = 0; i < response.length(); i++) {

                    try {
                        JSONObject question = response.getJSONObject(i);

                        String quesGenre = question.getString("genre");

                        if (questionSet.getGenre().equals(quesGenre)
                                && !quesDone.contains(question.getString("id"))) {

                            QuesModelMCQ quesModelMCQ = new QuesModelMCQ();
                            quesModelMCQ.setQuestion(question.getString("question"));
                            quesModelMCQ.setCorrectOption(question.getString("correctOption"));
                            quesModelMCQ.setQuesId(question.getString("id"));

                            JSONObject options = question.getJSONObject("options");
                            quesModelMCQ.setOptionA(options.getString("optionA"));
                            quesModelMCQ.setOptionB(options.getString("optionB"));
                            quesModelMCQ.setOptionC(options.getString("optionC"));
                            quesModelMCQ.setOptionD(options.getString("optionD"));

                            quesListMCQ.add(quesModelMCQ);
                            questionLength += 1;
                            if (questionLength == 10)
                                break;
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                if (questionLength != 10) {
                    return false;
                } else {
                    questionSet.setQuesModelMCQList(quesListMCQ);
                    return true;
                }

            case "True / False":
                quesListTrueFalse = new ArrayList<>();
                for (int i = 0; i < response.length(); i++) {

                    try {
                        JSONObject question = response.getJSONObject(i);

                        String quesGenre = question.getString("genre");

                        if (questionSet.getGenre().equals(quesGenre)
                                && !quesDone.contains(question.getString("id"))) {

                            QuesModelTrueFalse quesModelTrueFalse = new QuesModelTrueFalse();
                            quesModelTrueFalse.setQuestion(question.getString("question"));
                            quesModelTrueFalse.setAnswer(question.getString("answer"));
                            quesModelTrueFalse.setQuesId(question.getString("id"));

                            quesListTrueFalse.add(quesModelTrueFalse);
                            questionLength += 1;
                            if (questionLength == 10) {
                                break;
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if (questionLength != 10) {
                    return false;
                } else {
                    questionSet.setQuesModelTrueFalseList(quesListTrueFalse);
                    return true;
                }

            case "Select Odd One":

                quesListOddOne = new ArrayList<>();
                for (int i = 0; i < response.length(); i++) {

                    try {
                        JSONObject question = response.getJSONObject(i);

                        String quesGenre = question.getString("genre");

                        if (questionSet.getGenre().equals(quesGenre)
                                && !quesDone.contains(question.getString("id"))) {

                            QuesModelOddOne quesModelOddOne = new QuesModelOddOne();
                            quesModelOddOne.setAnswer(question.getString("answer"));
                            quesModelOddOne.setCorrectOption(question.getString("correctOption"));
                            quesModelOddOne.setQuesId(question.getString("id"));

                            JSONObject options = question.getJSONObject("options");
                            quesModelOddOne.setOptionA(options.getString("optionA"));
                            quesModelOddOne.setOptionB(options.getString("optionB"));
                            quesModelOddOne.setOptionC(options.getString("optionC"));
                            quesModelOddOne.setOptionD(options.getString("optionD"));

                            quesListOddOne.add(quesModelOddOne);
                            questionLength += 1;
                            if (questionLength == 10)
                                break;
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if (questionLength != 10) {
                    return false;
                } else {
                    questionSet.setQuesModelOddOneList(quesListOddOne);
                    return true;
                }

            case "Pictographic":

                quesListPics = new ArrayList<>();
                for (int i = 0; i < response.length(); i++) {

                    try {
                        JSONObject question = response.getJSONObject(i);

                        String quesGenre = question.getString("genre");

                        if (questionSet.getGenre().equals(quesGenre)
                                && !quesDone.contains(question.getString("id"))) {

                            QuesModelPic quesModelPic = new QuesModelPic();
                            quesModelPic.setQuestion(question.getString("question"));
                            quesModelPic.setQuesImageUrl(question.getString("questionImg"));
                            quesModelPic.setCorrectOption(question.getString("correctOption"));
                            quesModelPic.setQuesId(question.getString("id"));

                            JSONObject options = question.getJSONObject("options");
                            quesModelPic.setOptionA(options.getString("optionA"));
                            quesModelPic.setOptionB(options.getString("optionB"));
                            quesModelPic.setOptionC(options.getString("optionC"));
                            quesModelPic.setOptionD(options.getString("optionD"));

                            quesListPics.add(quesModelPic);
                            questionLength += 1;
                            if (questionLength == 10)
                                break;
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                if (questionLength != 10) {
                    return false;
                } else {
                    questionSet.setQuesModelPicList(quesListPics);
                    return true;
                }

        }
        return false;
    }

    private void startQuestionActivity() {
        switch (questionSet.getCategory()) {
            case "Multiple Choice":
                startActivity(new Intent(QuestionLoad.this, QuestionFrameText.class));
                finish();
                break;
            case "True / False":
                startActivity(new Intent(QuestionLoad.this, QuestionFrameTrueFalse.class));
                finish();
                break;
            case "Select Odd One":
                startActivity(new Intent(QuestionLoad.this, QFSelectOddOne.class));
                finish();
                break;
            case "Pictographic":
                startActivity(new Intent(QuestionLoad.this, QuestionFrameImage.class));
                finish();
                break;

        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(QuestionLoad.this, HomeScreen.class));
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!userDetails.isLifeLine1() ||
                !userDetails.isLifeLine2() ||
                !userDetails.isLifeLine3() ||
                !userDetails.isLifeLine4()) {
            lifeLineText.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.aql_homeButton:
                startActivity(new Intent(QuestionLoad.this, HomeScreen.class));
                finish();
                break;
            case R.id.ap_adView_ll1:
                showAd(1);
                break;
            case R.id.ap_adView_ll2:
                showAd(2);
                break;
            case R.id.ap_adView_ll3:
                showAd(3);
                break;
            case R.id.ap_adView_ll4:
                showAd(4);
                break;
        }
    }

    private RewardedAd createAndLoadRewardedAd() {
        RewardedAd rewardedAd = new RewardedAd(this,
                getString(R.string.rewarded_ad));
        RewardedAdLoadCallback adLoadCallback = new RewardedAdLoadCallback() {
            @Override
            public void onRewardedAdLoaded() {
                // Ad successfully loaded.
            }

            @Override
            public void onRewardedAdFailedToLoad(LoadAdError adError) {
                // Ad failed to load.
            }
        };
        rewardedAd.loadAd(new AdRequest.Builder().build(), adLoadCallback);
        return rewardedAd;
    }

    private void showAd(final int lifeLineId) {
        if (rewardedAd.isLoaded()) {
            Activity activityContext = QuestionLoad.this;
            RewardedAdCallback adCallback = new RewardedAdCallback() {
                @Override
                public void onRewardedAdOpened() {
                    // Ad opened.
                }

                @Override
                public void onRewardedAdClosed() {
                    // Ad closed.
                    rewardedAd = createAndLoadRewardedAd();
                }

                @Override
                public void onUserEarnedReward(@NonNull RewardItem reward) {
                    // User earned reward.
                    Toast.makeText(QuestionLoad.this,
                            "LifeLine Rewarded Successfully!", Toast.LENGTH_SHORT).show();
                    switch (lifeLineId) {
                        case 1:
                            adLL1.setVisibility(View.INVISIBLE);
                            userDetails.setLifeLine1(true);
                            break;
                        case 2:
                            adLL2.setVisibility(View.INVISIBLE);
                            userDetails.setLifeLine2(true);
                            break;
                        case 3:
                            adLL3.setVisibility(View.INVISIBLE);
                            userDetails.setLifeLine3(true);
                            break;
                        case 4:
                            adLL4.setVisibility(View.INVISIBLE);
                            userDetails.setLifeLine4(true);
                            break;
                    }
                    new SaveData().execute(QuestionLoad.this);
                }

//                @Override
//                public void onRewardedAdFailedToShow(AdError adError) {
//                    // Ad failed to display.
//                }
            };
            rewardedAd.show(activityContext, adCallback);
        } else {
            Toast.makeText(QuestionLoad.this,
                    "The rewarded ad wasn't loaded yet", Toast.LENGTH_SHORT).show();
        }
    }
}