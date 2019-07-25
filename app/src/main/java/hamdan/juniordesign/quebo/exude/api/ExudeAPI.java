package hamdan.juniordesign.quebo.exude.api;

import android.content.Context;

import hamdan.juniordesign.quebo.exude.commonclass.ExudeRequest;
import hamdan.juniordesign.quebo.exude.commonclass.ExudeResponse;
import hamdan.juniordesign.quebo.exude.exception.InvalidDataException;

public interface ExudeAPI {
    ExudeResponse filterStoppings(ExudeRequest exudeRequest, Context context) throws InvalidDataException;

    ExudeResponse filterStoppingKeepDuplicate(ExudeRequest exudeRequest, Context context) throws InvalidDataException;

    ExudeResponse getSwearWords(ExudeRequest exudeRequest, Context context) throws InvalidDataException;
}
