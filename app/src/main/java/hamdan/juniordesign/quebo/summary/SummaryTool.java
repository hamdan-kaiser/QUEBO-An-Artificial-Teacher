package hamdan.juniordesign.quebo.summary;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class SummaryTool {
    private String TAG = "SummaryTool";
    private ArrayList<Sentence> sentences, contentSummary;
    private ArrayList<Paragraph> paragraphs;
    private int noOfSentences, noOfParagraphs;
    private Context ctx;
    private String topic;
    private double[][] intersectionMatrix;
    private LinkedHashMap<Sentence, Double> dictionary;

    public SummaryTool(Context context, String text) {
        ctx = context;
        topic = text;
    }

    public void init() {
        sentences = new ArrayList<Sentence>();
        paragraphs = new ArrayList<Paragraph>();
        contentSummary = new ArrayList<Sentence>();
        dictionary = new LinkedHashMap<Sentence, Double>();

        makeParagraph(topic);
    }

    private void makeParagraph(String text) {

        String temp = null;
        int j = 0;

        for (int i = 0; i < text.length() - 1; i++) {

            temp = temp + text.charAt(i);

            if (text.charAt(i) == '.') {
                sentences.add(new Sentence(noOfSentences, temp.trim(), temp.trim().length(), noOfParagraphs));
                noOfSentences++;
            } else if (text.charAt(i) == '\n' && text.charAt(i + 1) == '\n') {
                noOfParagraphs++;
            }
        }

        groupIntoParagraph();
    }

    private void groupIntoParagraph() {
        int paraNum = 0;
        Paragraph paragraph = new Paragraph(0);

        for (int i = 0; i < noOfSentences; i++) {
            if (sentences.get(i).paragraphNumber == paraNum) {
                //continue
            } else {
                paragraphs.add(paragraph);
                paraNum++;
                paragraph = new Paragraph(paraNum);
            }
            Log.e(TAG, "############# " + sentences.get(i).value);
            paragraph.sentences.add(sentences.get(i));
        }
        paragraphs.add(paragraph);

        printSentences();
    }

    private void printSentences() {
        for (Sentence sentence : sentences) {
            System.out.println(sentence.number + " => " + sentence.value + " => " + sentence.stringLength + " => " + sentence.noOfWords + " => " + sentence.paragraphNumber);
        }

        createIntersectionMatrix();
    }

    private double noOfCommonWords(Sentence str1, Sentence str2) {
        double commonCount = 0;

        for (String str1Word : str1.value.split("\\s+")) {
            for (String str2Word : str2.value.split("\\s+")) {
                if (str1Word.compareToIgnoreCase(str2Word) == 0) {
                    commonCount++;
                }
            }
        }

        return commonCount;
    }

    private void createIntersectionMatrix() {
        intersectionMatrix = new double[noOfSentences][noOfSentences];
        for (int i = 0; i < noOfSentences; i++) {
            for (int j = 0; j < noOfSentences; j++) {

                if (i <= j) {
                    Sentence str1 = sentences.get(i);
                    Sentence str2 = sentences.get(j);
                    intersectionMatrix[i][j] = noOfCommonWords(str1, str2) / ((double) (str1.noOfWords + str2.noOfWords) / 2);
                } else {
                    intersectionMatrix[i][j] = intersectionMatrix[j][i];
                }

            }
        }

        Log.e(TAG, "------------------INTERSECTION MATRIX---------------------");
        printIntersectionMatrix();

        createDictionary();
    }

    private void printIntersectionMatrix() {
        for (int i = 0; i < noOfSentences; i++) {
            for (int j = 0; j < noOfSentences; j++) {
                System.out.print(intersectionMatrix[i][j] + "    ");
            }
            System.out.print("\n");
        }
    }

    private void createDictionary() {
        for (int i = 0; i < noOfSentences; i++) {
            double score = 0;
            for (int j = 0; j < noOfSentences; j++) {
                score += intersectionMatrix[i][j];
            }
            dictionary.put(sentences.get(i), score);
            sentences.get(i).score = score;
        }

        Log.e(TAG, "------------------PRINT DICTIONARY---------------------");
        printDicationary();

        createSummary();

    }

    private void printDicationary() {
        // Get a set of the entries
        Set set = dictionary.entrySet();
        // Get an iterator
        Iterator i = set.iterator();
        // Display elements
        while (i.hasNext()) {
            Map.Entry me = (Map.Entry) i.next();
            System.out.print(((Sentence) me.getKey()).value + ": ");
            System.out.println(me.getValue());
        }
    }

    private void createSummary() {

        for (int j = 0; j <= noOfParagraphs; j++) {
            int primary_set = paragraphs.get(j).sentences.size() / 5;

            //Sort based on score (importance)
            Collections.sort(paragraphs.get(j).sentences, new SentenceComparator());
            for (int i = 0; i <= primary_set; i++) {
                contentSummary.add(paragraphs.get(j).sentences.get(i));

                Log.e(TAG, "" + paragraphs.get(j).sentences.get(i).value);
            }
        }

        //To ensure proper ordering
        Collections.sort(contentSummary, new SentenceComparatorForSummary());


        Log.e(TAG, "------------------SUMMARY---------------------");
        //printSummary();

    }

    public StringBuilder getSummary() {
        System.out.println("no of paragraphs = " + noOfParagraphs);
        StringBuilder summary = new StringBuilder();
        for (int i = 0; i < contentSummary.size(); i++)
            summary.append(contentSummary.get(i).value);
        Log.e(TAG, ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        return summary;
    }

    private double getWordCount(ArrayList<Sentence> sentenceList) {
        double wordCount = 0.0;
        for (Sentence sentence : sentenceList) {
            wordCount += (sentence.value.split(" ")).length;
        }
        return wordCount;
    }

    public String getStats() {
        String calculation = "";
        calculation += ("Number of words in Context : " + getWordCount(sentences));
        calculation += ("\nNumber of words in Summary : " + getWordCount(contentSummary));
        calculation += ("\nCompression Ratio : " + getWordCount(contentSummary) / getWordCount(sentences));
        /*System.out.println("number of words in Context : " + getWordCount(sentences));
        System.out.println("number of words in Summary : " + getWordCount(contentSummary));
        System.out.println("Commpression : " + getWordCount(contentSummary) / getWordCount(sentences));*/
        return calculation;
    }
}
