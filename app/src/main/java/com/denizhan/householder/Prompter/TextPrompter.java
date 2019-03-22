package com.denizhan.householder.Prompter;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.denizhan.householder.ExternalTools.InstanceHolder;
import com.denizhan.householder.R;

public class TextPrompter extends Prompter {

    private Button exitButton;
    private TextView textView;

    public TextPrompter(InstanceHolder ih){
        super(ih, R.layout.prompter_text_view);

        exitButton = findViewById(R.id.exit_button);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        textView = findViewById(R.id.details_text);

    }

    public void show(String str) {
        textView.setText(str);
        super.show();
    }
}
