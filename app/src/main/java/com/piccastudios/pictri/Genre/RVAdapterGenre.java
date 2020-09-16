package com.piccastudios.pictri.Genre;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.piccastudios.pictri.HomeScreens.SoundClass;
import com.piccastudios.pictri.QuestionBank.QuestionSet;
import com.piccastudios.pictri.QuestionFrames.QFSelectOddOne;
import com.piccastudios.pictri.QuestionFrames.QuestionFrameImage;
import com.piccastudios.pictri.QuestionFrames.QuestionFrameText;
import com.piccastudios.pictri.QuestionFrames.QuestionFrameTrueFalse;
import com.piccastudios.pictri.R;
import com.piccastudios.pictri.UserSection.UserDetails;

import java.util.List;

public class RVAdapterGenre extends RecyclerView.Adapter<RVAdapterGenre.ViewHolder> {

    private static final String TAG = "rvag_logCheck";
    private Context context;
    private List<String> list;
    private QuestionSet questionSet;
    private static RecyclerViewClickListener clickListener;
    private SoundClass soundClass;

    public RVAdapterGenre(Context context, List<String> list, RecyclerViewClickListener clickListener) {
        this.context = context;
        this.list = list;
        this.clickListener = clickListener;
        questionSet = QuestionSet.getInstance(context);
        soundClass = SoundClass.getInstance(this.context);
    }

    @NonNull
    @Override
    public RVAdapterGenre.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.hs_genre_orientation, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RVAdapterGenre.ViewHolder viewHolder, final int position) {

        String name = list.get(position);
        viewHolder.nameItem.setText(name);

        final UserDetails userDetails = UserDetails.getInstance(context);

        if (!questionSet.getGenre().equals(name)) {
            viewHolder.genreView.setBackground(context.getDrawable(R.drawable.rec_round_white));
        } else {
            viewHolder.genreView.setBackgroundColor(context.getColor(R.color.textYellow));
        }

        viewHolder.genreView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (userDetails.isAudioStatus()) {
                    soundClass.playSound(0);
                }

                questionSet.setGenre(list.get(position));
                viewHolder.genreView.setBackgroundColor(context.getColor(R.color.textYellow));

                clickListener.recyclerViewListClicked(position);
                notifyDataSetChanged();

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView nameItem;
        ImageView genreView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameItem = itemView.findViewById(R.id.hco_genreName);
            genreView = itemView.findViewById(R.id.hco_genreView);

        }

    }

}
