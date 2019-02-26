package com.denizhan.householder.Interaction;

import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import com.denizhan.householder.ExternalTools.InstanceHolder;
import com.denizhan.householder.Prompter.Prompter;
import com.denizhan.householder.R;

public class MessageManager {

    int lastPlayedViewID = 0;
    Prompter prompter;

    public MessageManager(InstanceHolder ih){
        prompter = new Prompter(ih);
        for (int i = 0; i < 10; i++) {
            int view_id = View.generateViewId();
            ConstraintLayout constraintLayout = (ConstraintLayout) View.inflate(ih.activityInstance.getApplicationContext(), R.layout.message_view, null);
            constraintLayout.setId(view_id);

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.ALIGN_PARENT_START, 1);
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 1);
            if(lastPlayedViewID == 0){
                params.addRule(RelativeLayout.ALIGN_PARENT_TOP, 1);
            }else{
                params.addRule(RelativeLayout.BELOW, lastPlayedViewID);
            }
            lastPlayedViewID = view_id;

            constraintLayout.setLayoutParams(params);

            RelativeLayout relativeLayout = ih.activityInstance.findViewById(R.id.messages_layout);
            relativeLayout.addView(constraintLayout);

            Button openButton = constraintLayout.findViewById(R.id.open_button);
            openButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    prompter.show();
                }
            });
        }
    }
}
