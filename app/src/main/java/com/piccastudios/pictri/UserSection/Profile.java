package com.piccastudios.pictri.UserSection;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.ads.*;

import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.piccastudios.pictri.HomeScreens.SoundClass;
import com.piccastudios.pictri.QuestionFrames.QuestionLoad;
import com.piccastudios.pictri.R;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

public class Profile extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "ap_logCheck";
    private TextView achieve;
    private UserDetails userDetails;

    private static int adClicked = 0;

    private TextView userName, badgeName, levels;
    private TextView stars, questions, percentAns;
    private RelativeLayout adLL1, adLL2, adLL3, adLL4;
    private ImageView userImage, badgeIcon;

    private TextView lifeLineText;

    private SoundClass soundClass;

    private RewardedAd rewardedAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        userDetails = UserDetails.getInstance(this);
        soundClass = SoundClass.getInstance(this);

        achieve = findViewById(R.id.ap_showAchieve);
        userName = findViewById(R.id.ap_userName);
        badgeName = findViewById(R.id.ap_badgeText);
        levels = findViewById(R.id.ap_levelsCompleted);
        stars = findViewById(R.id.ap_stars);
        questions = findViewById(R.id.ap_questions);
        percentAns = findViewById(R.id.ap_percent);
        adLL1 = findViewById(R.id.ap_adView_ll1);
        adLL2 = findViewById(R.id.ap_adView_ll2);
        adLL3 = findViewById(R.id.ap_adView_ll3);
        adLL4 = findViewById(R.id.ap_adView_ll4);
        userImage = findViewById(R.id.ap_userImage);
        badgeIcon = findViewById(R.id.ap_badgeIcon);
        lifeLineText = findViewById(R.id.ap_lifelineText);

        //*****

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        AdView mAdView = findViewById(R.id.ap_adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        rewardedAd = createAndLoadRewardedAd();

        //*****

        userName.setText(userDetails.getUserName());
        badgeIcon.setImageResource(userDetails.getAppliedBadge());
        badgeName.setText(userDetails.getBadgeName());

        if (userDetails.getUserPhoto() != null) {
            Picasso.get()
                    .load(userDetails.getUserPhoto())
                    .fit()
                    .centerCrop()
                    .into(userImage);
        }
        //*****

        levels.setText("#" + String.valueOf(userDetails.getLevelsCompleted()));
        stars.setText("#" + String.valueOf(userDetails.getTotalStars()));
        questions.setText("#" + String.valueOf(userDetails.getTotalQuestions()));

        double percent = 0;
        if (userDetails.getTotalQuestions() != 0) {
            percent = ((double) userDetails.getTotalCorrectAnswer() / (double) userDetails.getTotalQuestions()) * 100;
            percentAns.setText(new DecimalFormat("##").format(percent) + " %");
        } else {
            percentAns.setText("0");
        }

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

        achieve.setOnClickListener(this);
        adLL1.setOnClickListener(this);
        adLL2.setOnClickListener(this);
        adLL3.setOnClickListener(this);
        adLL4.setOnClickListener(this);
        badgeIcon.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        badgeIcon.setImageResource(userDetails.getAppliedBadge());
        badgeName.setText(userDetails.getBadgeName());

        if (!userDetails.isLifeLine1() ||
                !userDetails.isLifeLine2() ||
                !userDetails.isLifeLine3() ||
                !userDetails.isLifeLine4()) {
            lifeLineText.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onClick(View v) {

        if (userDetails.isAudioStatus()) {
            soundClass.playSound(0);
        }

        switch (v.getId()) {
            case R.id.ap_showAchieve:
            case R.id.ap_badgeIcon:
                startActivity(new Intent(Profile.this, AchievementsScreen.class));
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
            Activity activityContext = Profile.this;
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
                    Toast.makeText(Profile.this,
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
                    new SaveData().execute(Profile.this);
                }

//                @Override
//                public void onRewardedAdFailedToShow(AdError adError) {
//                    // Ad failed to display.
//                }
            };
            rewardedAd.show(activityContext, adCallback);
        } else {
            Toast.makeText(Profile.this,
                    "The rewarded ad wasn't loaded yet", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

}