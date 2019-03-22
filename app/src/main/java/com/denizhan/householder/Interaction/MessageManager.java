import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.denizhan.householder.ExternalTools.InstanceHolder;
import com.denizhan.householder.Prompter.AudioPrompter;
import com.denizhan.householder.Prompter.TextPrompter;
import com.denizhan.householder.Prompter.VideoPrompter;
import com.denizhan.householder.R;

public class MessageManager {

    private InstanceHolder ih;

    int lastPlayedViewID = 0; // hold this number to add view hierarchy element from top down
    private TextPrompter textPrompter;
    private AudioPrompter audioPrompter;
    private VideoPrompter videoPrompter;

    public MessageManager(InstanceHolder ih){
        this.ih = ih;
        textPrompter = new TextPrompter(ih);
        audioPrompter = new AudioPrompter(ih);
        videoPrompter = new VideoPrompter(ih);
    }

    public void addTextMessage(final String date, final String message){
        ih.activityInstance.runOnUiThread(new Runnable() {
            public void run() {
                ConstraintLayout constraintLayout = addView();

                Button openButton = constraintLayout.findViewById(R.id.open_button);
                openButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        textPrompter.show(message);
                    }
                });

                TextView date_text = constraintLayout.findViewById(R.id.date_text);
                date_text.setText(date);

                TextView description_text = constraintLayout.findViewById(R.id.description_text);
                description_text.setText(message);
            }
        });

    }

    public void addAudioMessage(final String date, final String path){
        ih.activityInstance.runOnUiThread(new Runnable() {
            public void run() {
                ConstraintLayout constraintLayout = addView();

                Button openButton = constraintLayout.findViewById(R.id.open_button);
                openButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        audioPrompter.show(date, path);
                    }
                });

                TextView date_text = constraintLayout.findViewById(R.id.date_text);
                date_text.setText(date);

                TextView description_text = constraintLayout.findViewById(R.id.description_text);
                description_text.setText(path);
            }
        });

    }

    public void addVideoMessage(final String date, final String path){
        ih.activityInstance.runOnUiThread(new Runnable() {
            public void run() {
                ConstraintLayout constraintLayout = addView();

                Button openButton = constraintLayout.findViewById(R.id.open_button);
                openButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        videoPrompter.show(date, path);
                    }
                });

                TextView date_text = constraintLayout.findViewById(R.id.date_text);
                date_text.setText(date);

                TextView description_text = constraintLayout.findViewById(R.id.description_text);
                description_text.setText(path);
            }
        });

    }

    private ConstraintLayout addView(){
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

        return constraintLayout;
    }
}