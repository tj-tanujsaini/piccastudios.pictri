package com.piccastudios.pictri.Genre;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.piccastudios.pictri.QuestionBank.QuestionSet;
import com.piccastudios.pictri.QuestionFrames.QuestionLoad;
import com.piccastudios.pictri.R;

import java.util.ArrayList;
import java.util.List;

public class SelectGenre extends AppCompatActivity implements RecyclerViewClickListener, View.OnClickListener {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference crGenre = db.collection("genre");
    private DocumentReference drGenre = crGenre.document("genreList");

    private static final String TAG = "hs_logCheck";
    private RecyclerView rvGenre;
    private LinearLayoutManager linearLayoutManager;
    private RVAdapterGenre rvAdapterGenre;
    private List<String> list;
    private Button beginButton;
    private Animation fadeInAnim;
    private ProgressBar progressBar;
    private CardView rvCard;

    private GenreList genreList;
    private GenreListModel glModel;
    private QuestionSet questionSet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_genre);

        fadeInAnim = AnimationUtils.loadAnimation(this, R.anim.fade_in);

        questionSet = QuestionSet.getInstance(this);
        genreList = GenreList.getInstance(this);

//        if(questionSet.getCategory() != null) {
        switch (questionSet.getCategory()) {
            case "Multiple Choice":
                list = genreList.getTextGenre();
                break;
            case "True / False":
                list = genreList.getTrueFalseGenre();
                break;
            case "Select Odd One":
                list = genreList.getOddOneGenre();
                break;
            case "Pictographic":
                list = genreList.getPictureGenre();
                break;
        }
//        }

        beginButton = findViewById(R.id.asg_continueButton);
        progressBar = findViewById(R.id.asg_progressBar);
        rvGenre = findViewById(R.id.asg_rv_categories);
        rvCard = findViewById(R.id.asg_rvCard);

        linearLayoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        rvGenre.setHasFixedSize(true);

        rvCard.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        if (list != null) {
            rvCard.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);

            rvAdapterGenre = new RVAdapterGenre(SelectGenre.this, list, SelectGenre.this);
            rvGenre.setAdapter(rvAdapterGenre);

            rvGenre.setLayoutManager(new GridLayoutManager(SelectGenre.this, 2));
            rvAdapterGenre.notifyDataSetChanged();
        } else {
            drGenre.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot snapshot) {
                    if (snapshot.exists()) {

                        rvCard.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.INVISIBLE);

                        glModel = snapshot.toObject(GenreListModel.class);
                        assert glModel != null;
                        genreList.setTextGenre(glModel.getTextGenre());
                        genreList.setPictureGenre(glModel.getPictureGenre());
                        genreList.setTrueFalseGenre(glModel.getTrueFalseGenre());
                        genreList.setOddOneGenre(glModel.getOddOneGenre());

                        switch (questionSet.getCategory()) {
                            case "Multiple Choice":
                                list = genreList.getTextGenre();
                                break;
                            case "True / False":
                                list = genreList.getTrueFalseGenre();
                                break;
                            case "Select Odd One":
                                list = genreList.getOddOneGenre();
                                break;
                            case "Pictographic":
                                list = genreList.getPictureGenre();
                                break;
                        }

                        rvAdapterGenre = new RVAdapterGenre(SelectGenre.this, list, SelectGenre.this);
                        rvGenre.setAdapter(rvAdapterGenre);

                        rvGenre.setLayoutManager(new GridLayoutManager(SelectGenre.this, 2));
                        rvAdapterGenre.notifyDataSetChanged();
                    }
                }
            });
        }
        beginButton.setOnClickListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        beginButton.setClickable(false);
    }

    @Override
    public void recyclerViewListClicked(int position) {
        beginButton.setClickable(true);
        beginButton.setTextColor(getColor(R.color.textYellow));
        beginButton.startAnimation(fadeInAnim);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.asg_continueButton:
                startActivity(new Intent(SelectGenre.this, QuestionLoad.class));
                break;
        }
    }
}