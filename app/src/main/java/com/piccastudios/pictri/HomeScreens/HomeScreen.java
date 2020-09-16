package com.piccastudios.pictri.HomeScreens;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.common.util.concurrent.HandlerExecutor;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.piccastudios.pictri.Account.SignInAccount;
import com.piccastudios.pictri.Category.SelectCategory;
import com.piccastudios.pictri.Genre.GenreList;
import com.piccastudios.pictri.QuestionBank.QuestionSet;
import com.piccastudios.pictri.QuestionFrames.LevelOver;
import com.piccastudios.pictri.QuestionFrames.QuestionFrameText;
import com.piccastudios.pictri.QuestionFrames.QuestionLoad;
import com.piccastudios.pictri.R;
import com.piccastudios.pictri.UserSection.AchievementsScreen;
import com.piccastudios.pictri.UserSection.Profile;
import com.piccastudios.pictri.UserSection.UserDetails;
import com.squareup.picasso.Picasso;

public class HomeScreen extends AppCompatActivity implements View.OnClickListener{


    private LinearLayout hs_category, hs_settings, hs_weekly;
    private LinearLayout hs_profile, hs_help, hs_leaderBoard;

    private QuestionSet questionSet;
    private UserDetails userDetails;

    private ImageView badgeIcon;
    private TextView badgeText, totalStars;

    private ImageView weeklyPoster;
    private GenreList genreList;
    private SoundClass soundClass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        userDetails = UserDetails.getInstance(this);
        genreList = GenreList.getInstance(this);

        hs_category = findViewById(R.id.ahs_category);
        hs_weekly = findViewById(R.id.ahs_weekly);
        hs_leaderBoard = findViewById(R.id.ahs_leaderBoard);
        hs_profile = findViewById(R.id.ahs_profile);
        hs_help = findViewById(R.id.ahs_help);
        hs_settings = findViewById(R.id.ahs_settings);

        badgeIcon = findViewById(R.id.ahs_badgeIcon);
        badgeText = findViewById(R.id.ahs_badgeName);
        totalStars = findViewById(R.id.ahs_totalStars);

        weeklyPoster = findViewById(R.id.ahs_weekendPoster);

        questionSet = QuestionSet.getInstance(this);

        badgeIcon.setImageResource(userDetails.getAppliedBadge());
        badgeText.setText(userDetails.getBadgeName());
        totalStars.setText(String.valueOf(userDetails.getTotalStars()));

        soundClass = SoundClass.getInstance(this);

        hs_category.setOnClickListener(this);
        hs_weekly.setOnClickListener(this);
        hs_leaderBoard.setOnClickListener(this);
        hs_profile.setOnClickListener(this);
        hs_help.setOnClickListener(this);
        hs_settings.setOnClickListener(this);
        badgeIcon.setOnClickListener(this);

        if(genreList.getPosterUrl() != null){
            Picasso.get()
                    .load(genreList.getPosterUrl())
                    .fit()
                    .centerCrop()
                    .into(weeklyPoster);
        }

    }

    @Override
    public void onClick(View v) {
        if(userDetails.isAudioStatus()) {
            soundClass.playSound(0);
        }

        switch (v.getId()){
            case R.id.ahs_category:
                questionSet.setLife(2);
                    startActivity(new Intent(HomeScreen.this, SelectCategory.class));
                break;
            case R.id.ahs_weekly:
                questionSet.setLife(2);
                    questionSet.setGenre(genreList.getWbGenre());
                    questionSet.setCategory(genreList.getWbCategory());
                    startActivity(new Intent(HomeScreen.this, QuestionLoad.class));
                break;
            case R.id.ahs_leaderBoard:
                startActivity(new Intent(HomeScreen.this, LeaderBoard.class));
                break;
            case R.id.ahs_profile:
                startActivity(new Intent(HomeScreen.this, Profile.class));
                break;
            case R.id.ahs_help:
                startActivity(new Intent(HomeScreen.this, HelpScreen.class));
                break;
            case R.id.ahs_settings:
                soundClass.cleanUpIfEnd();
                startActivity(new Intent(HomeScreen.this, UserSettings.class));
                break;
            case R.id.ahs_badgeIcon:
                startActivity(new Intent(HomeScreen.this, AchievementsScreen.class));
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        badgeIcon.setImageResource(userDetails.getAppliedBadge());
        badgeText.setText(userDetails.getBadgeName());

        totalStars.setText(String.valueOf(userDetails.getTotalStars()));
        soundClass = SoundClass.getInstance(this);

    }

    @Override
    protected void onPause() {
        super.onPause();
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
                        HomeScreen.this.finishAffinity();
                    }
                });

        dialog = builder.create();
        dialog.show();

        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(getColor(R.color.buttonRed));
        dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(getColor(R.color.buttonGreen));
    }

}