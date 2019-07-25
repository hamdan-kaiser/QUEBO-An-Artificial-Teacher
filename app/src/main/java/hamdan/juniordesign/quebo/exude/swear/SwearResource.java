package hamdan.juniordesign.quebo.exude.swear;

import android.content.Context;

import java.io.InputStream;
import java.util.Properties;

import hamdan.juniordesign.quebo.exude.commonclass.BaseResource;

public class SwearResource implements BaseResource {

    private static Properties properties = null;

    private void loadResource(Context context) {
        try {
            if (properties == null) {
                properties = new Properties();
                InputStream input = context.getAssets().open("swear_en.properties");
                if (input != null) {
                    properties.load(input);
                } else {
                    System.err.println("Swear word resource file not found");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getProperties(String property, Context context) {
        if (properties == null) {
            loadResource(context);
        }
        return properties.getProperty(property);
    }

}
