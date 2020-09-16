package com.piccastudios.pictri.UserSection;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.piccastudios.pictri.Genre.SelectGenre;
import com.piccastudios.pictri.HomeScreens.HomeScreen;
import com.piccastudios.pictri.QuestionBank.QuestionSet;
import com.piccastudios.pictri.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RVAdapterAchieve extends RecyclerView.Adapter<RVAdapterAchieve.ViewHolder> implements View.OnClickListener {

    //firebase
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference crUserDetails = db.collection("userDetails");
    private DocumentReference drUserDetails;

    private static final String TAG = "rva_logCheck";
    private Context context;
    private List<BadgeModel> list;
    private UserDetails userDetails;

    public RVAdapterAchieve(Context context, List<BadgeModel> list) {
        this.context = context;
        this.list = list;

        userDetails = UserDetails.getInstance(context);
        drUserDetails = crUserDetails.document(userDetails.getUserId());
    }

    @NonNull
    @Override
    public RVAdapterAchieve.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.achieve_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RVAdapterAchieve.ViewHolder viewHolder, final int position) {

        final BadgeModel badgeModel = list.get(position);

        viewHolder.badgeIcon.setImageResource(badgeModel.getBadgeIcon());
        viewHolder.badgeName.setText(badgeModel.getBadgeName());
        viewHolder.badgeSubText.setText(badgeModel.getBadgeSubText());
        if (position < userDetails.getSignInBadgeNumber()) {
            viewHolder.levelComplete.setText(String.valueOf(userDetails.getMaxSignInStreak()));
            viewHolder.totalLevel.setText("/" + String.valueOf(badgeModel.getTotalSignIn()));

            if (badgeModel.getTotalSignIn() <= userDetails.getMaxSignInStreak()) {
                viewHolder.levelComplete.setVisibility(View.INVISIBLE);
                viewHolder.totalLevel.setVisibility(View.INVISIBLE);

                if (userDetails.getAppliedBadge() == badgeModel.getBadgeIcon()) {
                    viewHolder.appliedBadge.setVisibility(View.VISIBLE);
                    viewHolder.useBadge.setVisibility(View.INVISIBLE);
                } else {
                    viewHolder.useBadge.setVisibility(View.VISIBLE);
                    viewHolder.appliedBadge.setVisibility(View.INVISIBLE);
                }

            } else {
                viewHolder.levelComplete.setVisibility(View.VISIBLE);
                viewHolder.totalLevel.setVisibility(View.VISIBLE);
                viewHolder.appliedBadge.setVisibility(View.INVISIBLE);
                viewHolder.useBadge.setVisibility(View.INVISIBLE);
                viewHolder.appliedBadge.setVisibility(View.INVISIBLE);
            }

        } else {
            viewHolder.levelComplete.setText(String.valueOf(userDetails.getLevelsCompleted()));
            viewHolder.totalLevel.setText("/" + String.valueOf(badgeModel.getTotalLevels()));

            if (badgeModel.getTotalLevels() <= userDetails.getLevelsCompleted()) {
                viewHolder.levelComplete.setVisibility(View.INVISIBLE);
                viewHolder.totalLevel.setVisibility(View.INVISIBLE);

                if (userDetails.getAppliedBadge() == badgeModel.getBadgeIcon()) {
                    viewHolder.appliedBadge.setVisibility(View.VISIBLE);
                    viewHolder.useBadge.setVisibility(View.INVISIBLE);
                } else {
                    viewHolder.useBadge.setVisibility(View.VISIBLE);
                    viewHolder.appliedBadge.setVisibility(View.INVISIBLE);
                }

            } else {
                viewHolder.levelComplete.setVisibility(View.VISIBLE);
                viewHolder.totalLevel.setVisibility(View.VISIBLE);
                viewHolder.appliedBadge.setVisibility(View.INVISIBLE);
                viewHolder.useBadge.setVisibility(View.INVISIBLE);
                viewHolder.appliedBadge.setVisibility(View.INVISIBLE);
            }

        }

        viewHolder.useBadge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                viewHolder.useBadge.setVisibility(View.INVISIBLE);
                viewHolder.progressBar.setVisibility(View.VISIBLE);
                userDetails.setAppliedBadge(badgeModel.getBadgeIcon());
                userDetails.setBadgeName(badgeModel.getBadgeName());

                Map<String, Object> updateData = new HashMap<>();
                updateData.put("appliedBadge", userDetails.getAppliedBadge());
                updateData.put("badgeName", userDetails.getBadgeName());

                drUserDetails.update(updateData)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                viewHolder.progressBar.setVisibility(View.INVISIBLE);
                                viewHolder.appliedBadge.setVisibility(View.VISIBLE);
                                notifyDataSetChanged();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("logCheck", "onFailure: Failed to update");
                            }
                        });

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView badgeIcon;
        TextView badgeName, badgeSubText;
        TextView levelComplete, totalLevel;
        Button useBadge, appliedBadge;
        ProgressBar progressBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            badgeIcon = itemView.findViewById(R.id.al_badgeIcon);
            badgeName = itemView.findViewById(R.id.al_badgeText);
            badgeSubText = itemView.findViewById(R.id.al_badgeSubtext);
            levelComplete = itemView.findViewById(R.id.al_levelComplete);
            totalLevel = itemView.findViewById(R.id.al_levelTotal);
            useBadge = itemView.findViewById(R.id.al_useBadge);
            appliedBadge = itemView.findViewById(R.id.al_appliedBadge);
            progressBar = itemView.findViewById(R.id.al_progressbar);

        }
    }

    @Override
    public void onClick(View v) {
    }
}
