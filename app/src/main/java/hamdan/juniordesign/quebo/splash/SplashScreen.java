package hamdan.juniordesign.quebo.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.InputStream;

import hamdan.juniordesign.quebo.MainActivity;
import hamdan.juniordesign.quebo.R;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;

import static hamdan.juniordesign.quebo.MainActivity.initiatePOS;

public class SplashScreen extends AppCompatActivity {

    public static POSTaggerME posTaggerME;
    InputStream posModelStream = null;
    ImageView imageView;

    Animation moveAnimation, blink;
    RelativeLayout layout;
    TextView subTitle, initialize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        initPOS();

        imageView = findViewById(R.id.logoImage);
        layout = findViewById(R.id.relativeLayout);
        subTitle = findViewById(R.id.subTitle);
        initialize = findViewById(R.id.initialize);

        subTitle.setVisibility(View.GONE);
        initialize.setVisibility(View.GONE);

        moveAnimation = AnimationUtils.loadAnimation(SplashScreen.this, R.anim.move);
        blink = AnimationUtils.loadAnimation(SplashScreen.this, R.anim.blink);

        moveAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                subTitle.setVisibility(View.VISIBLE);

                new Handler().postDelayed(() -> {
                    initialize.setVisibility(View.VISIBLE);
                    initialize.setAnimation(blink);

                    new Handler().postDelayed(() -> {
                        startActivity(new Intent(SplashScreen.this, MainActivity.class));
                        finish();
                    }, 5000);

                }, 1000);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        layout.startAnimation(moveAnimation);

        blink.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                initialize.setAnimation(blink);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void initPOS() {
        new Thread(() -> {
            try {
                initiatePOS = false;
                System.setProperty("org.xml.sax.driver", "org.xmlpull.v1.sax2.Driver");
                posModelStream = getApplicationContext().getAssets().open("en_pos_maxent.bin"); // Reading POS model to stream
                POSModel posModel = new POSModel(posModelStream); // Loading POS model from stream
                posTaggerME = new POSTaggerME(posModel); // Init POS tagger with model
                Log.e("Splash", "POS Tagger ready");
                initiatePOS = true;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
