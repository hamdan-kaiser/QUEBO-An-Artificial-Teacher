package hamdan.juniordesign.quebo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Objects;

public class ShowPOSActivity extends AppCompatActivity {

    TextView mainTextPOS, posText;
    StringBuilder message;
    String string;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_pos);

        mainTextPOS = findViewById(R.id.mainTopicPOS);
        posText = findViewById(R.id.posText);

        String topic = Objects.requireNonNull(getIntent().getExtras()).getString("topic");

        mainTextPOS.setText(topic);

        startPOSDetection(topic);
    }

    private void startPOSDetection(String topic) {
        PartsOfSpeech partsOfSpeech;
        ArrayList<POSModelClass> posValues;
        String[] sentences = topic.split("(?<=[.!?;])\\s* ");
        message = new StringBuilder("");

        // For removing questions from paragraph
        //StringBuilder finalSentences = new StringBuilder();
        for (String sen : sentences) {

            message.append(sen).append(" ");
        }

        partsOfSpeech = new PartsOfSpeech(ShowPOSActivity.this);

        posValues = partsOfSpeech.detectParts(String.valueOf(message));
        //Log.e(TAG, "$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
        StringBuilder pos = new StringBuilder();

        //For Collecting all POS together


        for (int i = 0; i < posValues.size(); i++) {
            if (posValues.get(i).getToken().equals(".") || posValues.get(i).getToken().equals(";")
                    || posValues.get(i).getToken().equals("!") || posValues.get(i).getToken().equals("?")
                    || posValues.get(i).getToken().equals("\n")) {

                pos.append(posValues.get(i).getToken()).append("\n\n");

            } else {
                if (posValues.get(i).getTag().equals("''")) {
                    pos.append("\n").append(posValues).append("\n").append("NNP");

                    //countPos = (countPos * 10) + 3;

                } else {
                   // pos.append("\n").append(posValues.get(i).getToken()).append("\n").append(posValues.get(i).getTag());


                    if (posValues.get(i).getTag().equals("NNP"))
                    {

                        pos.append("\n").append(posValues.get(i).getToken()).append("\n").append("Proper noun, singular");

                    }

                   else if (posValues.get(i).getTag().equals("CD"))
                    {
                        pos.append("\n").append(posValues.get(i).getToken()).append("\n").append("Cardinal Number");

                    }
                    else if (posValues.get(i).getTag().equals("RB"))
                    {
                        pos.append("\n").append(posValues.get(i).getToken()).append("\n").append("Adverb");

                    }

                    else if (posValues.get(i).getTag().equals("VB"))
                    {
                        pos.append("\n").append(posValues.get(i).getToken()).append("\n").append("Verb, base form");

                    }

                    else if (posValues.get(i).getTag().equals("PRP"))
                    {
                        pos.append("\n").append(posValues.get(i).getToken()).append("\n").append("Personal pronoun");

                    }

                    else if (posValues.get(i).getTag().equals("NN"))
                    {
                        pos.append("\n").append(posValues.get(i).getToken()).append("\n").append("Noun (Singular or mass)");

                    } else if (posValues.get(i).getTag().equals("PRP"))
                    {
                        pos.append("\n").append(posValues.get(i).getToken()).append("\n").append("Personal Pronoun");

                    } else if (posValues.get(i).getTag().equals("IN"))
                    {
                        pos.append("\n").append(posValues.get(i).getToken()).append("\n").append("Preposition/subordinating Conjunction");

                    } else if (posValues.get(i).getTag().equals("VBD"))
                    {
                        pos.append("\n").append(posValues.get(i).getToken()).append("\n").append("Verb, Past Tense");

                    } else if (posValues.get(i).getTag().equals("VBZ"))
                    {
                        pos.append("\n").append(posValues.get(i).getToken()).append("\n").append("Verb, Third Person Singular Present");

                    } else if (posValues.get(i).getTag().equals("VBN"))
                    {
                        pos.append("\n").append(posValues.get(i).getToken()).append("\n").append("Verb, past participle");

                    } else if (posValues.get(i).getTag().equals("VBG"))
                    {
                        pos.append("\n").append(posValues.get(i).getToken()).append("\n").append("Verb, gerund, present participle");

                    } else if (posValues.get(i).getTag().equals("JJ"))
                    {
                        pos.append("\n").append(posValues.get(i).getToken()).append("\n").append("Adjective");

                    } else if (posValues.get(i).getTag().equals("NNS"))
                    {
                        pos.append("\n").append(posValues.get(i).getToken()).append("\n").append("Noun, plural");

                    }



                }
            }
            //Log.e(TAG, "" + posValues.get(i).getToken() + "\t" + posValues.get(i).getTag());
        }

        posText.setText(pos);
    }
}
