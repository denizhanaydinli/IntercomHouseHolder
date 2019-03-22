package com.denizhan.householder.Interaction;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.graphics.BitmapFactory;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.denizhan.householder.ExternalTools.InstanceHolder;
import com.denizhan.householder.MainActivity;
import com.denizhan.householder.Network.Tools.COMMANDS;
import com.denizhan.householder.R;

import java.util.Arrays;

public class RealtimeInterface {

    private InstanceHolder ih;
    private Button openCameraButton, closeCameraButton, openMicButton, openLockButton;
    private ImageView video_view;
    private ConstraintLayout message_layout, camera_layout;

    private boolean ringing = false;

    private ObjectAnimator ring_animation;


    @SuppressLint("ClickableViewAccessibility")
    public RealtimeInterface(InstanceHolder ih){
        this.ih = ih;
        this.message_layout = ih.activityInstance.findViewById(R.id.message_layout);
        this.camera_layout = ih.activityInstance.findViewById(R.id.camera_layout);
        this.openCameraButton = ih.activityInstance.findViewById(R.id.open_cam_button);
        openCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openIntercomCamera();
                //MainActivity.networkConnector.setCommand(COMMANDS.REAL_TIME_VIDEO_BEGIN);
            }
        });
        this.closeCameraButton = ih.activityInstance.findViewById(R.id.close_camera_button);
        closeCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeIntercomCamera();
                //MainActivity.networkConnector.setCommand(COMMANDS.IDLE);
            }
        });
        this.openMicButton = ih.activityInstance.findViewById(R.id.open_mic_button);
        openMicButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    ///MainActivity.networkConnector.setCommand(COMMANDS.REAL_TIME_AUDIO_BEGIN);
                } else if(event.getAction() == MotionEvent.ACTION_UP){
                    //MainActivity.networkConnector.setCommand(COMMANDS.IDLE);
                }
                return false;
            }
        });
        this.openLockButton = ih.activityInstance.findViewById(R.id.open_lock_button);
        openLockButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    //MainActivity.networkConnector.setCommand(COMMANDS.UNLOCK_BEGIN);
                } else if(event.getAction() == MotionEvent.ACTION_UP){
                    //MainActivity.networkConnector.setCommand(COMMANDS.IDLE);
                }
                return false;
            }
        });
        this.video_view = ih.activityInstance.findViewById(R.id.video_view);
    }

    public void openIntercomCamera(){
        this.camera_layout.setVisibility(View.VISIBLE);
        this.message_layout.setVisibility(View.INVISIBLE);
    }

    public void closeIntercomCamera(){
        this.camera_layout.setVisibility(View.INVISIBLE);
        this.message_layout.setVisibility(View.VISIBLE);
    }

    public void ring(){
        if(!ringing){
            ih.activityInstance.runOnUiThread(new Runnable() {
                public void run() {
                    ring_animation = ObjectAnimator.ofFloat(openLockButton, "translationY", 0, -5, 10, -15, -10, 5, 0);
                    ring_animation.setDuration(1000);
                    ring_animation.start();

                    ringing = true;

                    ring_animation.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            ringing = false;
                        }
                    });
                }
            });
        }
    }

    public void draw(final byte[] video_data){
        ih.activityInstance.runOnUiThread(new Runnable() {
            public void run() {
                video_view.setImageBitmap(BitmapFactory.decodeByteArray(video_data,0, video_data.length));
            }
        });
    }

}

/*package com.external.intercomhost.Activities;

        import android.animation.ObjectAnimator;
        import android.annotation.SuppressLint;
        import android.content.Context;
        import android.content.SharedPreferences;
        import android.graphics.BitmapFactory;
        import android.graphics.Color;
        import android.graphics.PorterDuff;
        import android.graphics.drawable.GradientDrawable;
        import android.os.Vibrator;
        import android.speech.tts.TextToSpeech;
        import android.speech.tts.UtteranceProgressListener;
        import android.support.annotation.NonNull;
        import android.support.constraint.ConstraintLayout;
        import android.support.v7.app.AppCompatActivity;
        import android.os.CountDownTimer;
        import android.os.Bundle;
        import android.view.MotionEvent;
        import android.view.SurfaceView;
        import android.view.View;
        import android.widget.ImageButton;
        import android.widget.ImageView;
        import android.widget.ProgressBar;
        import android.widget.TextView;
        import com.external.intercomhost.Media.RealTimeAudioPlayer;
        import com.external.intercomhost.Media.CustomAudioPlayer;
        import com.external.intercomhost.Media.CustomVideoPlayer;
        import com.external.intercomhost.Media.RealTimeAudioRecorder;
        import com.external.intercomhost.Network.NetworkConnector;
        import com.external.intercomhost.R;
        import java.io.BufferedReader;
        import java.io.FileOutputStream;
        import java.io.InputStream;
        import java.io.InputStreamReader;
        import java.io.OutputStreamWriter;
        import java.util.Arrays;
        import java.util.Locale;
        import java.util.Timer;
        import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    public static NetworkConnector networkConnector;
    private RealTimeAudioPlayer realTimeAudioPlayer;
    private RealTimeAudioRecorder realTimeAudioRecorder;
    private CustomVideoPlayer videoPlayer;
    private CustomAudioPlayer audioPlayer;

    private ConstraintLayout text_layout, audio_layout, video_layout;
    private ImageButton surveillance_video_button, surveillance_audio_button, unlock_button;
    private TextView text_count, audio_count, video_count, date_text, text_message;
    private ImageView video_image;
    private ProgressBar tts_progressbar, audio_progressbar;

    private ObjectAnimator ring_animator;

    private Vibrator vibrator;

    private TextToSpeech textToSpeech;
    private InstanceHolder IH;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.IH = new InstanceHolder(getApplicationContext(), MainActivity.this);
        prefs =  IH.contextInstance.getSharedPreferences("RECORDING_PREF", MODE_PRIVATE);
        initGUI();
        initTools();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(textToSpeech != null){
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        audioPlayer.destroy();
        videoPlayer.destroy();
        realTimeAudioPlayer.destroy();
        realTimeAudioRecorder.destroy();
        networkConnector.destroy();
    }

    private void initTools(){
        networkConnector = new NetworkConnector("192.168.43.165", this.IH);
        videoPlayer = new CustomVideoPlayer((SurfaceView)findViewById(R.id.video_surface));
        audioPlayer = new CustomAudioPlayer();
        audioPlayer.setProgressBar(audio_progressbar);
        realTimeAudioPlayer = new RealTimeAudioPlayer();
        realTimeAudioRecorder = new RealTimeAudioRecorder();
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                networkConnector.setCommand("check_updates");
            }
        }, 0, 500);
        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status == TextToSpeech.SUCCESS){
                    textToSpeech.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                        @Override
                        public void onStart(String utteranceId) {
                            tts_progressbar.setVisibility(View.GONE);
                        }
                        @Override
                        public void onDone(String utteranceId) {

                        }
                        @Override
                        public void onError(String utteranceId) { }
                    });
                    textToSpeech.setSpeechRate(0.8f);
                    textToSpeech.setLanguage(Locale.ENGLISH);
                }
            }
        });

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
    }
    @SuppressLint("ClickableViewAccessibility") //Silence "View.OnTouchListener" warnings.
    private void initGUI(){
        surveillance_video_button = findViewById(R.id.surveillance_video_button);
        setViewAppearance(surveillance_video_button, new float[] {8, 8, 8, 8, 8, 8, 8, 8}, "#DBDBDB");
        surveillance_video_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeTextLayout();
                closeAudioLayout();
                closeVideoLayout();

                networkConnector.setCommand("send_video");

                surveillance_video_button.setImageResource(R.drawable.open_cam_green_icon);
                video_image.setVisibility(View.VISIBLE);
                new CountDownTimer(5000, 16) {
                    public void onTick(long millisUntilFinished) {
                        byte[] video_data = Arrays.copyOfRange(networkConnector.receiving_bytes, 2, 2+((networkConnector.receiving_bytes[0]& 0xFF))+((networkConnector.receiving_bytes[1] & 0xFF)<<(8)));
                        video_image.setImageBitmap(BitmapFactory.decodeByteArray(video_data, 0, video_data.length));
                    }
                    public void onFinish() {
                        networkConnector.setCommand("end_video");
                        surveillance_video_button.setImageResource(R.drawable.open_cam_icon);
                        video_image.setVisibility(View.GONE);
                    }
                }.start();
            }
        });
        surveillance_audio_button = findViewById(R.id.surveillance_audio_button);
        setViewAppearance(surveillance_audio_button, new float[] {8, 8, 8, 8, 8, 8, 8, 8}, "#DBDBDB");
        surveillance_audio_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                realTimeAudioRecorder.start();
                networkConnector.setCommand("send_audio");
                surveillance_audio_button.setImageResource(R.drawable.open_speaker_icon_green);
                new CountDownTimer(5000, 100) {
                    public void onTick(long millisUntilFinished) {
                        byte[] audio_data = Arrays.copyOfRange(networkConnector.receiving_bytes, networkConnector.receiving_bytes.length-2000, networkConnector.receiving_bytes.length);
                        if(networkConnector.pushAudio){
                            realTimeAudioPlayer.pushAudio(audio_data);
                            networkConnector.pushAudio = false;
                        }
                    }
                    public void onFinish() {
                        networkConnector.setCommand("end_audio");
                        realTimeAudioRecorder.stop();
                        surveillance_audio_button.setImageResource(R.drawable.open_speaker_icon);
                    }
                }.start();
            }
        });

        unlock_button = findViewById(R.id.unlock_button);
        setViewAppearance(unlock_button, new float[] {8, 8, 8, 8, 8, 8, 8, 8}, "#DBDBDB");
        unlock_button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    networkConnector.setCommand("open_lock");
                    unlock_button.setImageResource(R.drawable.unlocked_green_icon);
                    ObjectAnimator.ofFloat(unlock_button, "translationY", 0, 6, -6, 0).setDuration(250).start();
                    return true;
                }else if(event.getAction() == MotionEvent.ACTION_UP){
                    unlock_button.setImageResource(R.drawable.locked_icon);
                    return true;
                }
                return false;
            }
        });

        ImageButton text_button = findViewById(R.id.text_button);
        text_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = getLastTextIndex();
                if(index > 0){
                    openTextLayout();
                    closeAudioLayout();
                    closeVideoLayout();
                    deleteTextIndex();

                    String message = getTextMessage(index-1);
                    text_message.setText(message);
                    readMessage(message);
                    tts_progressbar.setVisibility(View.VISIBLE);
                    text_count.setText(((index-1)+""));
                    date_text.setText(getTextDate(index-1));
                }else{
                    date_text.setText(R.string.NoText);
                }
            }
        });

        ImageButton audio_button = findViewById(R.id.audio_button);
        audio_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = getLastAudioIndex();
                if(index > 0){
                    closeTextLayout();
                    openAudioLayout();
                    closeVideoLayout();
                    deleteAudioIndex();

                    audioPlayer.prepare(index-1);
                    audio_count.setText(((index-1)+""));
                    date_text.setText(getAudioDate(index-1));
                }else{
                    date_text.setText(R.string.NoAudio);
                }
            }
        });

        ImageButton video_button = findViewById(R.id.video_button);
        video_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = getLastVideoIndex();
                if(index > 0){
                    openVideoLayout();
                    closeTextLayout();
                    closeAudioLayout();
                    deleteVideoIndex();

                    videoPlayer.stop();
                    videoPlayer.prepare(index - 1);
                    video_count.setText(((index-1)+""));
                    date_text.setText(getVideoDate(index-1));
                }else{
                    date_text.setText(R.string.NoVideo);
                }
            }
        });

        text_count = findViewById(R.id.text_count);
        text_count.setText((getLastTextIndex()+""));
        audio_count = findViewById(R.id.audio_count);
        audio_count.setText((getLastAudioIndex()+""));
        video_count = findViewById(R.id.video_count);
        video_count.setText((getLastVideoIndex()+""));

        ImageButton cancel_text = findViewById(R.id.cancel_text);
        cancel_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeTextLayout();
                date_text.setText("");
            }
        });

        ImageButton skip_text = findViewById(R.id.skip_text);
        skip_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = getLastTextIndex();
                if(index > 0){
                    deleteTextIndex();

                    String message = getTextMessage(index-1);
                    text_message.setText(message);
                    readMessage(message);
                    tts_progressbar.setVisibility(View.VISIBLE);
                    text_count.setText(((index-1)+""));
                    date_text.setText(getTextDate(index-1));
                }else{
                    text_message.setText("");
                    date_text.setText(R.string.NoTextLeft);
                }
            }
        });

        ImageButton cancel_audio = findViewById(R.id.cancel_audio);
        cancel_audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeAudioLayout();
                date_text.setText("");
            }
        });

        ImageButton skip_audio = findViewById(R.id.skip_audio);
        skip_audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = getLastAudioIndex();
                if(index > 0){
                    deleteAudioIndex();

                    audioPlayer.prepare(index-1);
                    audio_count.setText(((index-1)+""));
                    date_text.setText(getAudioDate(index-1));
                }else{
                    closeTextLayout();
                    date_text.setText(R.string.NoAudioLeft);
                    audioPlayer.stop();
                    audio_progressbar.setProgress(0);
                }
            }
        });

        ImageButton cancel_video = findViewById(R.id.cancel_video);
        cancel_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeVideoLayout();
                date_text.setText("");
            }
        });

        ImageButton skip_video = findViewById(R.id.skip_video);
        skip_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = getLastVideoIndex();
                if(index > 0){
                    deleteVideoIndex();

                    videoPlayer.stop();
                    videoPlayer.prepare(index - 1);
                    video_count.setText(((index-1)+""));
                    date_text.setText(getVideoDate(index-1));
                }else{
                    videoPlayer.stop();
                    date_text.setText(R.string.NoVideoLeft);
                }
            }
        });

        date_text = findViewById(R.id.date_text);

        text_layout = findViewById(R.id.text_layout);
        audio_layout = findViewById(R.id.audio_layout);
        video_layout = findViewById(R.id.video_layout);

        video_image = findViewById(R.id.video_image);
        text_message = findViewById(R.id.text_message);
        setViewAppearance(findViewById(R.id.display_layout), new float[] {8, 8, 8, 8, 8, 8, 8, 8}, "#008577");

        audio_progressbar = findViewById(R.id.audio_progressbar);
        audio_progressbar.getProgressDrawable().setColorFilter(Color.parseColor("#F45642"), PorterDuff.Mode.SRC_IN);

        tts_progressbar = findViewById(R.id.tts_progressbar);
    }

/*    private void writeDataToFile(String path, byte[] data){
        try {
            FileOutputStream fos = new FileOutputStream(path);
            fos.write(data);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void setViewAppearance(@NonNull View button, float[] corners, String color){
        GradientDrawable gd = new GradientDrawable();
        gd.setColor(Color.parseColor(color));
        gd.setCornerRadii(corners);
        button.setBackground(gd);
    }*/
   /* private void readMessage(String message){
        textToSpeech.speak(message, TextToSpeech.QUEUE_FLUSH, null, "1234567");
    }
    public void ring(){
        ring_animator = ObjectAnimator.ofFloat(unlock_button, "translationX", 0, 25, -25, 25, -25, 15, -15, 6, -6, 0);
        ring_animator.setDuration(1000);

        IH.activityInstance.runOnUiThread(new Runnable() {
            public void run() {
                ring_animator.start();
                vibrator.vibrate(100);
            }
        });
    }

    @NonNull
    private String getStringFromTxt(String path){
        try {
            InputStream inputStream = IH.contextInstance.openFileInput(path);
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString;
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                return stringBuilder.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "~";
    }
    private void setStringToTxt(String path, String str){
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(IH.contextInstance.openFileOutput(path, Context.MODE_PRIVATE));
            outputStreamWriter.write(str);
            outputStreamWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @NonNull
    private String getTextMessage(int index) {
        return getStringFromTxt("MES_" + index + ".txt");
    }

    public void addTextMessage(String message, String date){
        setTextDate(date, getLastTextIndex());
        setStringToTxt("MES_" + getLastTextIndex() + ".txt", message);
        updateTextIndex();
        text_count.post(new Runnable() {
            public void run() {
                text_count.setText((getLastTextIndex()+""));
            }
        });
    }
    public void addAudioMessage(byte[] data, String date){
        setAudioDate(date, getLastAudioIndex());
        writeDataToFile("/storage/emulated/0/" + "VID_" + getLastAudioIndex() + ".3gp", data);
        updateAudioIndex();
        audio_count.post(new Runnable() {
            public void run() {
                audio_count.setText((getLastAudioIndex()+""));
            }
        });
    }
    public void addVideoMessage(byte[] data, String date){
        setVideoDate(date, getLastVideoIndex());
        writeDataToFile("/storage/emulated/0/" + "VID_" + getLastVideoIndex() + ".mp4", data);
        updateVideoIndex();
        video_count.post(new Runnable() {
            public void run() {
                video_count.setText((getLastVideoIndex()+""));
            }
        });
    }

    @NonNull
    private String getTextDate(int index){
        return getStringFromTxt("TEXT_DATE_" + index + ".txt");
    }
    @NonNull
    private String getAudioDate(int index){
        return getStringFromTxt("AUDIO_DATE_" + index + ".txt");
    }
    @NonNull
    private String getVideoDate(int index){
        return getStringFromTxt("VIDEO_DATE_" + index + ".txt");
    }

    private void setTextDate(String date, int index){
        setStringToTxt("TEXT_DATE_" + index + ".txt", date);
    }
    private void setAudioDate(String date, int index){
        setStringToTxt("AUDIO_DATE_" + index + ".txt", date);
    }
    private void setVideoDate(String date, int index){
        setStringToTxt("VIDEO_DATE_" + index + ".txt", date);
    }

    private int getLastTextIndex(){
        return prefs.getInt("last_text_index", 0);
    }
    private int getLastAudioIndex(){
        return prefs.getInt("last_audio_index", 0);
    }
    private int getLastVideoIndex(){
        return prefs.getInt("last_video_index", 0);
    }
    private void updateTextIndex(){
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("last_text_index", getLastTextIndex()+1);
        editor.apply();
    }
    private void updateAudioIndex(){
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("last_audio_index", getLastAudioIndex()+1);
        editor.apply();
    }
    private void updateVideoIndex(){
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("last_video_index", getLastVideoIndex()+1);
        editor.apply();
    }
    private void deleteTextIndex(){
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("last_text_index", getLastTextIndex()-1);
        editor.apply();
    }
    private void deleteAudioIndex(){
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("last_audio_index", getLastAudioIndex()-1);
        editor.apply();
    }
    private void deleteVideoIndex(){
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("last_video_index", getLastVideoIndex()-1);
        editor.apply();
    }

    private void openTextLayout(){
        text_layout.setVisibility(View.VISIBLE);
    }
    private void openAudioLayout(){
        audio_layout.setVisibility(View.VISIBLE);
    }
    private void openVideoLayout(){
        video_layout.setVisibility(View.VISIBLE);
    }
    private void closeTextLayout(){
        text_layout.setVisibility(View.GONE);
        text_message.setText("");
    }
    private void closeAudioLayout(){
        audio_layout.setVisibility(View.GONE);
        audioPlayer.stop();
        audio_progressbar.setProgress(0);
    }
    private void closeVideoLayout(){
        video_layout.setVisibility(View.GONE);
        videoPlayer.stop();
    }

    public class InstanceHolder{
        Context contextInstance;
        public MainActivity activityInstance;

        InstanceHolder(Context context, MainActivity activity){
            this.contextInstance = context;
            this.activityInstance = activity;
        }
    }
}*/
