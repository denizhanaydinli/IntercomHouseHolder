package com.denizhan.householder.Prompter;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.denizhan.householder.ExternalTools.InstanceHolder;
import com.denizhan.householder.ExternalTools.T2S;
import com.denizhan.householder.Media.CustomAudioPlayer;
import com.denizhan.householder.R;

public class AudioPrompter extends Prompter {

    private Button exitButton;
    private ProgressBar progressBar;
    private TextView details_text;

    private CustomAudioPlayer customAudioPlayer;

    public AudioPrompter(InstanceHolder ih){
        super(ih, R.layout.prompter_audio_view);

        details_text = findViewById(R.id.details_text);

        exitButton = findViewById(R.id.exit_button);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customAudioPlayer.stop();
                dismiss();
            }
        });

        progressBar = findViewById(R.id.audio_bar);

        customAudioPlayer = new CustomAudioPlayer(progressBar);
    }

    public void show(String date, String path) {
        details_text.setText(date);
        customAudioPlayer.reset();
        customAudioPlayer.prepare(path, true);
        customAudioPlayer.start();
        super.show();
    }
}