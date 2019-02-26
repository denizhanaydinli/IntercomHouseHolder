package com.denizhan.householder.ExternalTools;

import com.denizhan.householder.MainActivity;

//Denizhan
public class InstanceHolder {
    public MainActivity activityInstance;
    //bundle da ekleyebiliriz ilerde

    public InstanceHolder(MainActivity activity){
        this.activityInstance = activity;
    }
}