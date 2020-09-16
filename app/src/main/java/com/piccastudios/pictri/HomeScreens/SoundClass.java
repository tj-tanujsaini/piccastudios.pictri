package com.piccastudios.pictri.HomeScreens;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

import com.google.firebase.firestore.auth.User;
import com.piccastudios.pictri.R;
import com.piccastudios.pictri.UserSection.UserDetails;

public class SoundClass {

    private static SoundPool soundPool;
    private static int[] sm;

    private static SoundClass instance;

    private SoundClass() {
    }

    public static SoundClass getInstance(Context ctx) {
        if (instance == null) {
            instance = new SoundClass();
        }

        int maxStreams = 6;

        soundPool = new SoundPool.Builder()
                .setMaxStreams(maxStreams)
                .build();

        sm = new int[6];
        // fill your sounds
        sm[0] = soundPool.load(ctx, R.raw.click, 1);
        sm[1] = soundPool.load(ctx, R.raw.star_counting, 1);
        sm[2] = soundPool.load(ctx, R.raw.clock_ticking, 1);
        sm[3] = soundPool.load(ctx, R.raw.life_over, 1);
        sm[4] = soundPool.load(ctx, R.raw.correct_answer, 1);
        sm[5] = soundPool.load(ctx, R.raw.wrong_answer, 1);

        return instance;
    }

    public void playSound(int sound) {
        if (sound == 1) {
            soundPool.play(sm[sound], 1, 1, 1, -1, 1f);
        } else {
            soundPool.play(sm[sound], 1, 1, 1, 0, 1f);
        }

    }

    public void cleanUpIfEnd() {
        sm = null;
        soundPool.autoPause();
        soundPool = null;
    }

}
