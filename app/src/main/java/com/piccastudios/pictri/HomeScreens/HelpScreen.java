package com.piccastudios.pictri.HomeScreens;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import com.piccastudios.pictri.R;

public class HelpScreen extends AppCompatActivity {

    private TextView FAQ_text;
    private FAQText faqText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_screen);

        faqText = new FAQText();

        FAQ_text = findViewById(R.id.hlp_FAQText);

        FAQ_text.setText(faqText.faq());
        FAQ_text.setMovementMethod(new ScrollingMovementMethod());

    }
}