package com.piccastudios.pictri.HomeScreens;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.piccastudios.pictri.R;
import com.piccastudios.pictri.UserSection.UserDetails;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class LeaderBoard extends AppCompatActivity {

    //firebase
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference crUserDetails = db.collection("userDetails");

    private List<UserModel> list;

    private RVAdapterLB rvAdapterLB;
    private RecyclerView recyclerView;

    private ImageView backButton;
    private ProgressBar progressBar;

    private UserDetails userDetails;

    private ImageView userImage, userBadge, userTheme;
    private TextView userName, starCounts, badgeName, rank;
    private RelativeLayout userRank;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard_screen);

        backButton = findViewById(R.id.als_back);
        progressBar = findViewById(R.id.lb_progressBar);

        progressBar.setVisibility(View.VISIBLE);
        userImage = findViewById(R.id.lb_userImage);
        userName = findViewById(R.id.lb_userName);
        userBadge = findViewById(R.id.lb_badgeIcon);
        starCounts = findViewById(R.id.lb_starsCount);
        rank = findViewById(R.id.lb_rank);
        badgeName = findViewById(R.id.lb_badgeName);
        userTheme = findViewById(R.id.lb_backGround);
        userRank = findViewById(R.id.lb_userRank);

        userDetails = UserDetails.getInstance(this);
        userName.setText(userDetails.getUserName());
        userBadge.setImageResource(userDetails.getAppliedBadge());
        badgeName.setText(userDetails.getBadgeName());
        starCounts.setText(String.valueOf(userDetails.getTotalStars()));

        if (userDetails.getUserPhoto() != null) {
            Picasso.get()
                    .load(userDetails.getUserPhoto())
                    .fit()
                    .centerCrop()
                    .into(userImage);
        }

        recyclerView = findViewById(R.id.als_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();

        crUserDetails.orderBy("totalStars", Query.Direction.ASCENDING)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots != null) {
                            for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {

                                UserModel userModel;
                                userModel = snapshot.toObject(UserModel.class);

                                Log.d("LogCheck", "onSuccess: list addition " +
                                        userModel.getUserId());
                                list.add(0, userModel);
                            }

                            rvAdapterLB = new RVAdapterLB(LeaderBoard.this, list);
                            recyclerView.setAdapter(rvAdapterLB);
                            rvAdapterLB.notifyDataSetChanged();

                            for (int position = 0; position < list.size(); position++) {

                                if (userDetails.getUserId().equals(list.get(position).getUserId())) {
                                    progressBar.setVisibility(View.INVISIBLE);
                                    userRank.setVisibility(View.VISIBLE);
                                    userTheme.setBackground(getDrawable(R.drawable.grad_mist));
                                    rank.setText("#" + String.valueOf(position + 1));
                                }

                            }
                        }
                    }
                });


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}