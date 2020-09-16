package com.piccastudios.pictri.HomeScreens;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.auth.User;
import com.piccastudios.pictri.R;
import com.piccastudios.pictri.UserSection.BadgeModel;
import com.piccastudios.pictri.UserSection.UserDetails;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RVAdapterLB extends RecyclerView.Adapter<RVAdapterLB.ViewHolder> implements View.OnClickListener {

    private static final String TAG = "rva_logCheck";
    private Context context;
    private List<UserModel> list;
    private UserDetails userDetails;
    private UserModel userModel;

    public RVAdapterLB(Context context, List<UserModel> list) {
        this.context = context;
        this.list = list;
        userDetails = UserDetails.getInstance(context);
    }

    @NonNull
    @Override
    public RVAdapterLB.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.leaderboard_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RVAdapterLB.ViewHolder viewHolder, final int position) {

        userModel = new UserModel();
        userModel = list.get(position);

        viewHolder.userName.setText(userModel.getUserName());
        viewHolder.badgeName.setText(userModel.getBadgeName());
        viewHolder.userBadge.setImageResource(userModel.getAppliedBadge());
        viewHolder.starCounts.setText(String.valueOf(userModel.getTotalStars()));
        viewHolder.rank.setText("#" + (position + 1));

        if (userModel.getUserPhoto() != null) {
            Picasso.get()
                    .load(userModel.getUserPhoto())
                    .fit()
                    .centerCrop()
                    .into(viewHolder.userImage);
        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView userImage, userBadge;
        TextView userName, starCounts, badgeName, rank;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            userImage = itemView.findViewById(R.id.lb_userImage);
            userName = itemView.findViewById(R.id.lb_userName);
            userBadge = itemView.findViewById(R.id.lb_badgeIcon);
            starCounts = itemView.findViewById(R.id.lb_starsCount);
            rank = itemView.findViewById(R.id.lb_rank);
            badgeName = itemView.findViewById(R.id.lb_badgeName);

        }
    }

    @Override
    public void onClick(View v) {
    }
}
