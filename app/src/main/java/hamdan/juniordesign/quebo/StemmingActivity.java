package hamdan.juniordesign.quebo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.util.Objects;

import hamdan.juniordesign.quebo.exude.ExudeData;
import hamdan.juniordesign.quebo.exude.exception.InvalidDataException;

public class StemmingActivity extends AppCompatActivity {

    String TAG = "StemmingActivity";

    TextView topicText, stemmedText;
    String topic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stemming);

        // 1. Multiple choice
        // 2. True/False
        // 3.

        topicText = findViewById(R.id.topicText);
        stemmedText = findViewById(R.id.stemmedText);

        topic = Objects.requireNonNull(getIntent().getExtras()).getString("ocrtext");
        topicText.setText(topic);
        Log.e(TAG, "" + topic);

        startStemming(topic);
    }

    private void startStemming(String topic) {
        String output = "";
        try {
            output = ExudeData.getInstance(StemmingActivity.this).filterStoppings(topic);
            output = output + "\n\nSwear Words: " + ExudeData.getInstance(StemmingActivity.this).getSwearWords(topic + " ");
            //output = output + "\n\nDuplicate Words: " + ExudeData.getInstance().filterStoppingsKeepDuplicates(topic);
            stemmedText.setText(output);
        } catch (InvalidDataException e) {
            e.printStackTrace();
        }
    }
}
