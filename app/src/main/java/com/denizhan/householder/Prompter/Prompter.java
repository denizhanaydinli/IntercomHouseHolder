package com.denizhan.householder.Prompter;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;

import com.denizhan.householder.ExternalTools.InstanceHolder;
import com.denizhan.householder.R;

public class Prompter extends Dialog {

    private Button exitButton;

    public Prompter(InstanceHolder ih){
        super((Context) ih.activityInstance);
        super.setContentView(R.layout.prompter_view);
        super.setCanceledOnTouchOutside(true);
        exitButton = findViewById(R.id.exit_button);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public void show(){
        Prompter.super.show();
    }

    public void dismiss(){
        Prompter.super.dismiss();
    }


}
