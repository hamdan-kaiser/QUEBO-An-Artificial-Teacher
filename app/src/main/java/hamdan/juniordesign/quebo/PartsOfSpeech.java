package hamdan.juniordesign.quebo;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

import static hamdan.juniordesign.quebo.splash.SplashScreen.posTaggerME;

public class PartsOfSpeech {

    private ArrayList<POSModelClass> posModelClassArray = new ArrayList<>();
    private Context context;
    private InputStream tokenModelStream = null;
    private POSModelClass posModelClass;

    PartsOfSpeech(Context ctx) {
        context = ctx;
    }

    /*
      To get probabilities of tags...
      double[] probs = posTaggerME.probs();
    */

    public ArrayList<POSModelClass> detectParts(String paragraph) {

        String[] tokens = tokenizeParagraph(paragraph);
        String[] tags = new String[0];

        tags = posTaggerME.tag(tokens); // Tagging the tokens

        for (int i = 0; i < tokens.length; i++) {
            //parts.put(tokens[i], tags[i]);
            posModelClass = new POSModelClass(context, tokens[i], tags[i]);
            posModelClassArray.add(posModelClass);
        }

        return posModelClassArray;
    }

    // Tokenize sentences
    private String[] tokenizeParagraph(String paragraph) {

        String[] tokens = null;

        try {
            tokenModelStream = context.getAssets().open("en_token.bin");

            if (tokenModelStream == null) {
                Log.e("#######", "$$$$$$$$$$");
            } else {
                TokenizerModel tokenizerModel = new TokenizerModel(tokenModelStream);
                Tokenizer tokenizer = new TokenizerME(tokenizerModel);
                tokens = tokenizer.tokenize(paragraph);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (tokenModelStream != null) {
                try {
                    tokenModelStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return tokens;
    }

}
