package com.denizhan.householder.Network;

/*
    Yazacak Olan: Nehir
    Açıklama: Ev sahibine kaydedilen mesajlarÄ± ve gerÃ§ek zamanlÄ± okunan veriyi gÃ¶ndermeye ve karÅŸÄ±dan gÃ¶nderilen veriyi
    almaya yaracak class.
*/

import android.util.Log;
import com.denizhan.householder.ExternalTools.InstanceHolder;
import com.denizhan.householder.Network.Tools.COMMANDS;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class NetworkConnector {

    private InstanceHolder ih;

    private UDPSender udpSender;
    private UDPReceiver udpReceiver;

    private Thread sendingThread, receivingThread;
    private Runnable sendingRunnable, receivingRunnable;
    private boolean sending,  receiving;

    public NetworkConnector(String ipAdress, InstanceHolder ih){
        this.udpSender = new UDPSender(ipAdress);
        this.udpReceiver = new UDPReceiver();
        this.ih = ih;
        initialize();
    }

    public void initialize(){
        sendingRunnable = new Runnable() {
            @Override
            public void run() {
                try {
                    while(sending){
                        send();
                        Thread.sleep(75);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        sendingThread = new Thread(sendingRunnable);

        receivingRunnable = new Runnable() {
            @Override
            public void run()  {
                try {
                    while(receiving){
                        receive();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        receivingThread = new Thread(receivingRunnable);
    }

    public void start(){
        sending = true;
        sendingThread.start();

        receiving = true;
        receivingThread.start();
    }

    public void stop(){
        sending = false;
        receiving = false;
    }

    private void destroy(){
        sending = false;
        sendingThread = null;

        receiving = false;
        receivingThread = null;
    }

    private void send() {

    }

    private void receive() throws IOException {
        Log.e("****", "data rec waiting");
        byte [] data = udpReceiver.receive();
        String data_as_str = new String(data);
        Log.e("****", "data rec");
        if(COMMANDS.TEXT_BEGIN.eqauls(data_as_str)){
            data = udpReceiver.receive();
            data_as_str = new String(data);
            while(!COMMANDS.TEXT_END.eqauls(data_as_str)){
                ih.activityInstance.messageManager.addTextMessage("~Xsn önce", data_as_str);
                data = udpReceiver.receive();
                data_as_str = new String(data);
            }
        } else if(COMMANDS.AUDIO_BEGIN.eqauls(data_as_str)){
            data = udpReceiver.receive();
            data_as_str = new String(data);
            while(!COMMANDS.AUDIO_END.eqauls(data_as_str)){
                writeDataToFile("/storage/emulated/0/sample" + "0" + ".3gp", data);
                ih.activityInstance.messageManager.addAudioMessage("~Xsn önce", "/storage/emulated/0/sample" + "0" + ".3gp");
                data = udpReceiver.receive();
                data_as_str = new String(data);
            }
        } else if(COMMANDS.VIDEO_BEGIN.eqauls(data_as_str)){
            data = udpReceiver.receive();
            data_as_str = new String(data);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            while(!COMMANDS.VIDEO_END.eqauls(data_as_str)){
                byteArrayOutputStream.write(data);
                data = udpReceiver.receive();
                data_as_str = new String(data);
            }
            Log.e("***", byteArrayOutputStream.toByteArray().length + " TOTAL");
            writeDataToFile("/storage/emulated/0/video" + "0" + ".mp4", byteArrayOutputStream.toByteArray());
            ih.activityInstance.messageManager.addVideoMessage("~Xsn önce", "/storage/emulated/0/video" + "0" + ".mp4");
        }
    }

    private void writeDataToFile(String path, byte[] data){
        try {
            FileOutputStream fos = new FileOutputStream(path);
            fos.write(data);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
