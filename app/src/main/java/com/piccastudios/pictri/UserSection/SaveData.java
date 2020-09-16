package com.piccastudios.pictri.UserSection;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.piccastudios.pictri.QuestionBank.QuestionSet;

import java.util.HashMap;
import java.util.Map;

public class SaveData extends AsyncTask<Context, Void, Void> {

    //firebase
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference crUserDetails = db.collection("userDetails");
    private DocumentReference drUserDetails;

    UserDetails userDetails;
    QuestionSet questionSet;

    @Override
    protected Void doInBackground(Context... contexts) {

        userDetails = UserDetails.getInstance(contexts[0]);
        questionSet = QuestionSet.getInstance(contexts[0]);

        drUserDetails = crUserDetails.document(userDetails.getUserId());

        Map<String, Object> updateData = new HashMap<>();

        updateData.put("totalQuestions", userDetails.getTotalQuestions());
        updateData.put("totalCorrectAnswer", userDetails.getTotalCorrectAnswer());
        updateData.put("levelsCompleted", userDetails.getLevelsCompleted());
        updateData.put("lifeLine1", userDetails.isLifeLine1());
        updateData.put("lifeLine2", userDetails.isLifeLine2());
        updateData.put("lifeLine3", userDetails.isLifeLine3());
        updateData.put("lifeLine4", userDetails.isLifeLine4());
        updateData.put("totalStars", userDetails.getTotalStars());
        updateData.put("quesDone", userDetails.getQuesDone());
        updateData.put("maxSignInStreak", userDetails.getMaxSignInStreak());
        updateData.put("totalSignIn", userDetails.getTotalSignIn());
        updateData.put("lastSignIn", userDetails.getLastSignIn());
        updateData.put("audioStatus", userDetails.isAudioStatus());

        if (questionSet.isLlUpdate1()) {
            questionSet.setLlUpdate1(false);
            updateData.put("llts1", userDetails.getLLTS1());
        }

        if (questionSet.isLlUpdate2()) {
            questionSet.setLlUpdate2(false);
            updateData.put("llts2", userDetails.getLLTS2());
        }

        if (questionSet.isLlUpdate3()) {
            questionSet.setLlUpdate3(false);
            updateData.put("llts3", userDetails.getLLTS3());
        }

        if (questionSet.isLlUpdate4()) {
            questionSet.setLlUpdate4(false);
            updateData.put("llts4", userDetails.getLLTS4());
        }

        drUserDetails.update(updateData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("logCheck", "update success");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("logCheck", "onFailure: Failed to update");
                    }
                });

        return null;
    }
}
