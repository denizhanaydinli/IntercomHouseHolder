package com.denizhan.householder.Prompter;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.denizhan.householder.ExternalTools.InstanceHolder;
import com.denizhan.householder.ExternalTools.T2S;
import com.denizhan.householder.R;

public class TextPrompter extends Prompter {

    private Button exitButton, listenButton;
    private TextView textView;

    private T2S t2s;
    private ProgressBar listenProgressbar;

    private String message = "";

    public TextPrompter(InstanceHolder ih){
        super(ih, R.layout.prompter_text_view);

        exitButton = findViewById(R.id.exit_button);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                t2s.stop();
            }
        });
        textView = findViewById(R.id.details_text);

        listenButton = findViewById(R.id.listen_button);
        listenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t2s.readMessage(message);
            }
        });

        listenProgressbar = findViewById(R.id.listen_progressbar);

        t2s = new T2S((Context) ih.activityInstance.getApplicationContext());
        t2s.setVisuals(listenProgressbar, listenButton);

    }

    public void show(String str) {
        textView.setText(str);
        message = str;
        listenButton.setVisibility(View.VISIBLE);
        listenProgressbar.setVisibility(View.INVISIBLE);
        super.show();
    }
}
