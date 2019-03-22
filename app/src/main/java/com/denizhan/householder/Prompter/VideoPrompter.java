package com.denizhan.householder.Prompter;

import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.denizhan.householder.ExternalTools.InstanceHolder;
import com.denizhan.householder.Media.CustomAudioPlayer;
import com.denizhan.householder.Media.CustomVideoPlayer;
import com.denizhan.householder.R;

public class VideoPrompter extends Prompter {

    private Button exitButton;
    private TextView details_text;

    private SurfaceView surfaceView;
    private CustomVideoPlayer customVideoPlayer;

    public VideoPrompter(InstanceHolder ih) {
        super(ih, R.layout.prompter_video_view);

        details_text = findViewById(R.id.details_text);

        exitButton = findViewById(R.id.exit_button);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customVideoPlayer.stop();
                dismiss();
            }
        });

        surfaceView = findViewById(R.id.surfaceView);
        customVideoPlayer = new CustomVideoPlayer(surfaceView);
    }

    public void show(String date, String path) {
        super.show();
        details_text.setText(date);
        customVideoPlayer.prepare( "/storage/emulated/0/video" + "0" + ".mp4");
        customVideoPlayer.start();
    }

}