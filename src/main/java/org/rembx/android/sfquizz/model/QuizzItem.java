package org.rembx.android.sfquizz.model;


/**
 * Class which represents a question, associated choices and correct answer
 *
 * @author rembx
 */

public class QuizzItem {

    /**
     * The value
     */
    private String value;

    /**
     * List of choices
     */
    private String[] choices;

    private Integer answerIdx;

    private Integer idx = 0;

    public QuizzItem(String question, String[] choices, Integer answer, Integer idx) {
        this.value = question;
        this.choices = choices;
        this.answerIdx = answer;
        this.idx = idx;
    }

    public String getValue() {
        return value;
    }

    public String[] getChoices() {
        return choices;
    }

    public String getAnswer() {
        return choices[answerIdx];
    }

    public String toString() {
        return value;
    }

    public Integer getIdx() {
        return idx;
    }
}
