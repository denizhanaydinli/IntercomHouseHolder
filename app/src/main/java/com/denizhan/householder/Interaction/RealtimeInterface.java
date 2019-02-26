package com.denizhan.householder.Interaction;

import android.animation.ValueAnimator;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.Button;

import com.denizhan.householder.ExternalTools.InstanceHolder;
import com.denizhan.householder.R;

public class RealtimeInterface {

    private InstanceHolder ih;
    private View cameraDivider;
    private Button openCameraButton, closeCameraButton, openMicButton;

    public RealtimeInterface(InstanceHolder ih){
        this.ih = ih;
        this.cameraDivider = ih.activityInstance.findViewById(R.id.cam_divider);
        this.openCameraButton = ih.activityInstance.findViewById(R.id.open_cam_button);
        openCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openIntercomCamera();
            }
        });
        this.closeCameraButton = ih.activityInstance.findViewById(R.id.close_camera_button);
        closeCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeIntercomCamera();
            }
        });
        this.openMicButton = ih.activityInstance.findViewById(R.id.open_mic_button);
        openMicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public void openIntercomCamera(){
        ValueAnimator biasAnimator;

        ConstraintLayout.LayoutParams initLayoutParams = (ConstraintLayout.LayoutParams) cameraDivider.getLayoutParams();

        biasAnimator = ValueAnimator.ofFloat(initLayoutParams.horizontalBias, 0.0f);
        biasAnimator.setDuration(500);
        biasAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatedValue = (float) animation.getAnimatedValue();
                ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) cameraDivider.getLayoutParams();
                layoutParams.horizontalBias = animatedValue;
                cameraDivider.setLayoutParams(layoutParams);
            }
        });

        biasAnimator.start();

    }

    public void closeIntercomCamera(){
        ValueAnimator biasAnimator;

        ConstraintLayout.LayoutParams initLayoutParams = (ConstraintLayout.LayoutParams) cameraDivider.getLayoutParams();

        biasAnimator = ValueAnimator.ofFloat(initLayoutParams.horizontalBias, 1.0f);
        biasAnimator.setDuration(500);
        biasAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatedValue = (float) animation.getAnimatedValue();
                ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) cameraDivider.getLayoutParams();
                layoutParams.horizontalBias = animatedValue;
                cameraDivider.setLayoutParams(layoutParams);
            }
        });

        biasAnimator.start();

    }

}
