package com.denizhan.householder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.denizhan.householder.ExternalTools.InstanceHolder;
import com.denizhan.householder.Interaction.RealtimeInterface;
import com.denizhan.householder.Interaction.MessageManager;

public class MainActivity extends AppCompatActivity {

    private InstanceHolder IH;

    private RealtimeInterface realtimeInterface;
    private MessageManager messageManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.IH = new InstanceHolder( MainActivity.this);

        realtimeInterface = new RealtimeInterface(this.IH);
        messageManager = new MessageManager(this.IH);
    }
}
