package org.rembx.android.sfquizz.model;


/**
 * Stores question, associated proposed answers and correct answer index
 *
 * @author remibantos
 */

public class QuizzItem {

    private String question;

    private String[] proposedAnswers;

    private Integer answerIdx;

    private Integer id = 0;

    private boolean hasBeenCorrectlyAnswered;

    public QuizzItem(String question, String[] proposedAnswers, Integer answer, Integer id) {
        this.question = question;
        this.proposedAnswers = proposedAnswers;
        this.answerIdx = answer;
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public String[] getProposedAnswers() {
        return proposedAnswers;
    }

    public String getAnswer() {
        return proposedAnswers[answerIdx];
    }

    public String toString() {
        return question;
    }

    public Integer getId() {
        return id;
    }

    public boolean hasBeenCorrectlyAnswered() {
        return hasBeenCorrectlyAnswered;
    }

    public void setHasBeenCorrectlyAnswered(boolean hasBeenCorrectlyAnswered) {
        this.hasBeenCorrectlyAnswered = hasBeenCorrectlyAnswered;
    }
}
