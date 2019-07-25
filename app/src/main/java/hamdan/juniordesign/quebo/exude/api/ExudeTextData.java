package hamdan.juniordesign.quebo.exude.api;

import android.content.Context;

import java.util.logging.Level;
import java.util.logging.Logger;

import hamdan.juniordesign.quebo.exude.commonclass.ExudeRequest;
import hamdan.juniordesign.quebo.exude.commonclass.ExudeResponse;
import hamdan.juniordesign.quebo.exude.exception.InvalidDataException;
import hamdan.juniordesign.quebo.exude.swear.SwearParser;

public class ExudeTextData implements ExudeAPI {

    Logger logger = Logger.getLogger("ExudeTextData");

    @Override
    public ExudeResponse filterStoppings(ExudeRequest exudeRequest, Context context) throws InvalidDataException {
        try {
            return ExudeAPIImpl.getInstance(context).filterStoppings(exudeRequest);
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
            throw new InvalidDataException("Invalid Data");
        }
    }

    @Override
    public ExudeResponse filterStoppingKeepDuplicate(ExudeRequest exudeRequest, Context context) throws InvalidDataException {
        try {
            return ExudeAPIImpl.getInstance(context).filterStoppingWithDuplicate(exudeRequest);
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
            throw new InvalidDataException("Invalid Data");
        }
    }

    @Override
    public ExudeResponse getSwearWords(ExudeRequest exudeRequest, Context context) throws InvalidDataException {
        StringBuilder finalFilteredData = new StringBuilder();
        try {
            if (exudeRequest.getData().isEmpty()) {
                throw new InvalidDataException("Invalid Data");
            }
            SwearParser swearParser = SwearParser.getInstance(context);
            finalFilteredData.append(swearParser.getSwearWords(exudeRequest.getData()));
            swearParser.resetSwearWords();
            ExudeResponse response = new ExudeResponse();
            response.setResultData(finalFilteredData.toString());
            return response;
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
            throw new InvalidDataException("Invalid Data");
        }
    }

}
