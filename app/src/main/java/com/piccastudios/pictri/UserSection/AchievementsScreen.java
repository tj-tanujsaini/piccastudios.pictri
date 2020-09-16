package com.piccastudios.pictri.UserSection;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.firestore.auth.User;
import com.piccastudios.pictri.R;

import java.util.ArrayList;
import java.util.List;

public class AchievementsScreen extends AppCompatActivity {

    private List<BadgeModel> list;

    private RVAdapterAchieve rvAdapterAchieve;
    private RecyclerView recyclerView;

    private UserDetails userDetails;
    private ImageView backButton;

    public AchievementsScreen() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievements_screen);

        userDetails = UserDetails.getInstance(this);
        backButton = findViewById(R.id.aas_back);
        list = new ArrayList<>();

        badgeDefinition();
        signInBadgeDefinition();

        rvAdapterAchieve = new RVAdapterAchieve(this, list);
        recyclerView = findViewById(R.id.aas_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(rvAdapterAchieve);

        rvAdapterAchieve.notifyDataSetChanged();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void badgeDefinition() {

        BadgeModel badgeModel1 = new BadgeModel();
        BadgeModel badgeModel2 = new BadgeModel();
        BadgeModel badgeModel3 = new BadgeModel();
        BadgeModel badgeModel4 = new BadgeModel();
        BadgeModel badgeModel5 = new BadgeModel();
        BadgeModel badgeModel6 = new BadgeModel();
        BadgeModel badgeModel7 = new BadgeModel();
        BadgeModel badgeModel8 = new BadgeModel();
        BadgeModel badgeModel9 = new BadgeModel();
        BadgeModel badgeModel10 = new BadgeModel();
        BadgeModel badgeModel11 = new BadgeModel();
        BadgeModel badgeModel12 = new BadgeModel();
        BadgeModel badgeModel13 = new BadgeModel();
        BadgeModel badgeModel14 = new BadgeModel();
        BadgeModel badgeModel15 = new BadgeModel();
        BadgeModel badgeModel16 = new BadgeModel();

        badgeModel1.setBadgeIcon(R.drawable.ach_scout);
        badgeModel2.setBadgeIcon(R.drawable.ach_bear);
        badgeModel3.setBadgeIcon(R.drawable.ach_bandit);
        badgeModel4.setBadgeIcon(R.drawable.ach_cowgirl);
        badgeModel5.setBadgeIcon(R.drawable.ach_cowboy);
        badgeModel6.setBadgeIcon(R.drawable.ach_killer);
        badgeModel7.setBadgeIcon(R.drawable.ach_viking);
        badgeModel8.setBadgeIcon(R.drawable.ach_jester);
        badgeModel9.setBadgeIcon(R.drawable.ach_pirate);
        badgeModel10.setBadgeIcon(R.drawable.ach_ninja);
        badgeModel11.setBadgeIcon(R.drawable.ach_mummy);
        badgeModel12.setBadgeIcon(R.drawable.ach_wizard);
        badgeModel13.setBadgeIcon(R.drawable.ach_death);
        badgeModel14.setBadgeIcon(R.drawable.ach_knight);
        badgeModel15.setBadgeIcon(R.drawable.ach_alien);
        badgeModel16.setBadgeIcon(R.drawable.ach_vampire);

        badgeModel1.setBadgeName("Scout");
        badgeModel2.setBadgeName("Bear");
        badgeModel3.setBadgeName("Bandit");
        badgeModel4.setBadgeName("Cowgirl");
        badgeModel5.setBadgeName("Cowboy");
        badgeModel6.setBadgeName("Killer");
        badgeModel7.setBadgeName("Viking");
        badgeModel8.setBadgeName("Jester");
        badgeModel9.setBadgeName("Pirate");
        badgeModel10.setBadgeName("Ninja");
        badgeModel11.setBadgeName("Mummy");
        badgeModel12.setBadgeName("Wizard");
        badgeModel13.setBadgeName("Death");
        badgeModel14.setBadgeName("Knight");
        badgeModel15.setBadgeName("Alien");
        badgeModel16.setBadgeName("Vampire");

        badgeModel1.setBadgeSubText("Sign in for the First Time");
        badgeModel2.setBadgeSubText("Complete 5 levels");
        badgeModel3.setBadgeSubText("Complete 10 levels");
        badgeModel4.setBadgeSubText("Complete 15 levels");
        badgeModel5.setBadgeSubText("Complete 20 levels");
        badgeModel6.setBadgeSubText("Complete 30 levels");
        badgeModel7.setBadgeSubText("Complete 40 levels");
        badgeModel8.setBadgeSubText("Complete 50 levels");
        badgeModel9.setBadgeSubText("Complete 75 levels");
        badgeModel10.setBadgeSubText("Complete 100 levels");
        badgeModel11.setBadgeSubText("Complete 130 levels");
        badgeModel12.setBadgeSubText("Complete 160 levels");
        badgeModel13.setBadgeSubText("Complete 190 levels");
        badgeModel14.setBadgeSubText("Complete 220 levels");
        badgeModel15.setBadgeSubText("Complete 250 levels");
        badgeModel16.setBadgeSubText("Complete 300 levels");

        badgeModel1.setTotalLevels(0);
        badgeModel2.setTotalLevels(5);
        badgeModel3.setTotalLevels(10);
        badgeModel4.setTotalLevels(15);
        badgeModel5.setTotalLevels(20);
        badgeModel6.setTotalLevels(30);
        badgeModel7.setTotalLevels(40);
        badgeModel8.setTotalLevels(50);
        badgeModel9.setTotalLevels(75);
        badgeModel10.setTotalLevels(100);
        badgeModel11.setTotalLevels(130);
        badgeModel12.setTotalLevels(160);
        badgeModel13.setTotalLevels(190);
        badgeModel14.setTotalLevels(220);
        badgeModel15.setTotalLevels(250);
        badgeModel16.setTotalLevels(300);

        list.add(0, badgeModel16);
        list.add(0, badgeModel15);
        list.add(0, badgeModel14);
        list.add(0, badgeModel13);
        list.add(0, badgeModel12);
        list.add(0, badgeModel11);
        list.add(0, badgeModel10);
        list.add(0, badgeModel9);
        list.add(0, badgeModel8);
        list.add(0, badgeModel7);
        list.add(0, badgeModel6);
        list.add(0, badgeModel5);
        list.add(0, badgeModel4);
        list.add(0, badgeModel3);
        list.add(0, badgeModel2);
        list.add(0, badgeModel1);
    }

    private void signInBadgeDefinition() {

        userDetails.setSignInBadgeNumber(8);

        BadgeModel badgeModel1 = new BadgeModel();
        BadgeModel badgeModel2 = new BadgeModel();
        BadgeModel badgeModel3 = new BadgeModel();
        BadgeModel badgeModel4 = new BadgeModel();
        BadgeModel badgeModel5 = new BadgeModel();
        BadgeModel badgeModel6 = new BadgeModel();
        BadgeModel badgeModel7 = new BadgeModel();
        BadgeModel badgeModel8 = new BadgeModel();

        badgeModel1.setBadgeIcon(R.drawable.sign_balloon);
        badgeModel2.setBadgeIcon(R.drawable.sign_headband);
        badgeModel3.setBadgeIcon(R.drawable.sign_helmet);
        badgeModel4.setBadgeIcon(R.drawable.sign_glasses);
        badgeModel5.setBadgeIcon(R.drawable.sign_hat);
        badgeModel6.setBadgeIcon(R.drawable.sign_mask);
        badgeModel7.setBadgeIcon(R.drawable.sign_theatre_mask);
        badgeModel8.setBadgeIcon(R.drawable.sign_carnival_mask);

        badgeModel1.setBadgeName("Balloon");
        badgeModel2.setBadgeName("Headband");
        badgeModel3.setBadgeName("Helmet");
        badgeModel4.setBadgeName("Glasses");
        badgeModel5.setBadgeName("Hat");
        badgeModel6.setBadgeName("Mask");
        badgeModel7.setBadgeName("Theatre");
        badgeModel8.setBadgeName("Carnival");

        badgeModel1.setBadgeSubText("Sign in for the First Time");
        badgeModel2.setBadgeSubText("Sign in 2 days in a row");
        badgeModel3.setBadgeSubText("Sign in 3 days in a row");
        badgeModel4.setBadgeSubText("Sign in 4 days in a row");
        badgeModel5.setBadgeSubText("Sign in 5 days in a row");
        badgeModel6.setBadgeSubText("Sign in 10 days in a row");
        badgeModel7.setBadgeSubText("Sign in 30 days in a row");
        badgeModel8.setBadgeSubText("Sign in 60 days in a row");

        badgeModel1.setTotalSignIn(1);
        badgeModel2.setTotalSignIn(2);
        badgeModel3.setTotalSignIn(3);
        badgeModel4.setTotalSignIn(4);
        badgeModel5.setTotalSignIn(5);
        badgeModel6.setTotalSignIn(10);
        badgeModel7.setTotalSignIn(30);
        badgeModel8.setTotalSignIn(60);

        list.add(0, badgeModel8);
        list.add(0, badgeModel7);
        list.add(0, badgeModel6);
        list.add(0, badgeModel5);
        list.add(0, badgeModel4);
        list.add(0, badgeModel3);
        list.add(0, badgeModel2);
        list.add(0, badgeModel1);
    }

}