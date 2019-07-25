package hamdan.juniordesign.quebo.exude.factory;

import android.content.Context;

import java.util.regex.Pattern;

import hamdan.juniordesign.quebo.exude.api.ExudeAPI;
import hamdan.juniordesign.quebo.exude.api.ExudeFileData;
import hamdan.juniordesign.quebo.exude.api.ExudeTextData;
import hamdan.juniordesign.quebo.exude.api.ExudeWebLinkData;
import hamdan.juniordesign.quebo.exude.commonclass.Constants;
import hamdan.juniordesign.quebo.exude.commonclass.ExudeRequest;
import hamdan.juniordesign.quebo.exude.commonclass.ExudeResponse;
import hamdan.juniordesign.quebo.exude.exception.InvalidDataException;

public class ExudeFactory {

    private ExudeAPI exudeAPI = null;
    private ExudeRequest exudeRequest;
    private Context ctx;

    public ExudeFactory(ExudeRequest exudeRequest, Context context) {
        ctx = context;
        boolean isFile = Pattern.matches(Constants.FILE_PATH_REGULAR_EXPRESSION, exudeRequest.getData());
        boolean isURL = Pattern.matches(Constants.URL_REGULAR_EXPRESSION, exudeRequest.getData());
        if (isFile || isURL) {
            exudeRequest.setType(isFile ? Constants.ExudeType.FILE.name()
                    : Constants.ExudeType.WEB_LINK.name());
        } else {
            exudeRequest.setType(Constants.ExudeType.TEXT_DATA.name());
        }
        this.exudeRequest = exudeRequest;
    }

    private ExudeAPI getExudeAPI() {
        switch (Constants.ExudeType.valueOf(getExudeRequest().getType())) {
            case TEXT_DATA:
                exudeAPI = new ExudeTextData();
                break;
            case FILE:
                exudeAPI = new ExudeFileData();
                break;
            case WEB_LINK:
                exudeAPI = new ExudeWebLinkData();
                break;
        }
        return exudeAPI;
    }

    public ExudeResponse filterStopppingData() throws InvalidDataException {
        try {
            exudeAPI = getExudeAPI();
            return exudeRequest.isKeepDuplicate() ? exudeAPI.filterStoppingKeepDuplicate(exudeRequest, ctx)
                    : exudeAPI.filterStoppings(exudeRequest, ctx);
        } catch (Exception e) {
            throw new InvalidDataException("Invalid Input Data ", e);
        }
    }

    public ExudeResponse getSwearWords() throws InvalidDataException {
        try {
            exudeAPI = getExudeAPI();
            return exudeAPI.getSwearWords(exudeRequest, ctx);
        } catch (Exception e) {
            throw new InvalidDataException("Invalid Input Data ", e);
        }
    }

    public ExudeRequest getExudeRequest() {
        return exudeRequest;
    }

    public void setExudeRequest(ExudeRequest exudeRequest) {
        this.exudeRequest = exudeRequest;
    }

}
