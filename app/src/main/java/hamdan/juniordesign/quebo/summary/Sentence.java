package hamdan.juniordesign.quebo.summary;

public class Sentence {
    int paragraphNumber;
    int number;
    int stringLength; //Dont need this
    double score;
    int noOfWords;
    String value;

    Sentence(int number, String value, int stringLength, int paragraphNumber) {
        this.number = number;
        this.value = value;
        this.stringLength = stringLength;
        noOfWords = value.split("\\s+").length;
        score = 0.0;
        this.paragraphNumber = paragraphNumber;
    }
}
