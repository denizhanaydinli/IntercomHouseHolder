package com.denizhan.householder.Prompter;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.denizhan.householder.ExternalTools.InstanceHolder;
import com.denizhan.householder.R;

public class Prompter extends Dialog {

    public Prompter(InstanceHolder ih, int layout_id){
        super((Context) ih.activityInstance);
        super.setContentView(layout_id);
        super.setCanceledOnTouchOutside(true);
    }

    public void show(){
        Prompter.super.show();
    }

    public void dismiss(){
        Prompter.super.dismiss();
    }

}