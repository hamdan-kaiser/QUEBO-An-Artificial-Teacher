package hamdan.juniordesign.quebo;

import android.content.Context;

public class POSModelClass {
    private Context context;
    private String token, tag;

    POSModelClass(Context ctx, String tokens, String tags) {
        context = ctx;
        token = tokens;
        tag = tags;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
