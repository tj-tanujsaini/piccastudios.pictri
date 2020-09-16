package com.piccastudios.pictri.Category;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.auth.User;
import com.piccastudios.pictri.Genre.SelectGenre;
import com.piccastudios.pictri.HomeScreens.SoundClass;
import com.piccastudios.pictri.QuestionBank.QuestionSet;
import com.piccastudios.pictri.R;
import com.piccastudios.pictri.UserSection.UserDetails;

import java.util.List;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.ViewHolder> implements View.OnClickListener {

    private static final String TAG = "rva_logCheck";
    private Context context;
    private List<String> list;
    private String category;
    private QuestionSet questionSet;
    private SoundClass soundClass;

    public RVAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
        questionSet = QuestionSet.getInstance(this.context);
        soundClass = SoundClass.getInstance(this.context);
    }

    @NonNull
    @Override
    public RVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.category_orientation, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RVAdapter.ViewHolder viewHolder, final int position) {

        category = list.get(position);
        viewHolder.nameItem.setText(category);

        final UserDetails userDetails = UserDetails.getInstance(context);

        viewHolder.categoryView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (userDetails.isAudioStatus()) {
                    soundClass.playSound(0);
                }

                questionSet.setCategory(list.get(position));
                context.startActivity(new Intent(context, SelectGenre.class));
            }
        });


        switch (category) {
            case "Multiple Choice":
                viewHolder.imageCategory.setImageResource(R.drawable.png_mcq);
                break;
            case "True / False":
                viewHolder.imageCategory.setImageResource(R.drawable.png_truefalse);
                break;
            case "Select Odd One":
                viewHolder.imageCategory.setImageResource(R.drawable.png_oddone);
                break;
            case "Pictographic":
                viewHolder.imageCategory.setImageResource(R.drawable.png_picto);
                break;
        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        TextView nameItem;
        ImageView categoryView, imageCategory;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameItem = itemView.findViewById(R.id.co_categoryName);
            categoryView = itemView.findViewById(R.id.co_categoryView);
            imageCategory = itemView.findViewById(R.id.co_imageCategory);

        }
    }

    @Override
    public void onClick(View v) {
    }

}
