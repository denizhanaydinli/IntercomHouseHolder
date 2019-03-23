package com.denizhan.householder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.denizhan.householder.ExternalTools.InstanceHolder;
import com.denizhan.householder.Interaction.RealtimeInterface;
import com.denizhan.householder.Interaction.MessageManager;
import com.denizhan.householder.Network.NetworkConnector;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private InstanceHolder IH;

    public static NetworkConnector networkConnector;

    private RealtimeInterface realtimeInterface;
    public MessageManager messageManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.IH = new InstanceHolder( MainActivity.this);

        networkConnector = new NetworkConnector("192.168.43.165", this.IH);
        networkConnector.start();

        realtimeInterface = new RealtimeInterface(this.IH);
        messageManager = new MessageManager(this.IH);
    }
}

