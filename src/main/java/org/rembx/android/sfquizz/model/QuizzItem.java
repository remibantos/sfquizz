package org.rembx.android.sfquizz.model;


import java.util.Arrays;

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

    @Override
    public String toString() {
        return "QuizzItem{" +
                "question='" + question + '\'' +
                ", proposedAnswers=" + Arrays.toString(proposedAnswers) +
                ", answerIdx=" + answerIdx +
                ", id=" + id +
                ", hasBeenCorrectlyAnswered=" + hasBeenCorrectlyAnswered +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        QuizzItem quizzItem = (QuizzItem) o;

        if (hasBeenCorrectlyAnswered != quizzItem.hasBeenCorrectlyAnswered) return false;
        if (answerIdx != null ? !answerIdx.equals(quizzItem.answerIdx) : quizzItem.answerIdx != null) return false;
        if (id != null ? !id.equals(quizzItem.id) : quizzItem.id != null) return false;
        if (!Arrays.equals(proposedAnswers, quizzItem.proposedAnswers)) return false;
        if (question != null ? !question.equals(quizzItem.question) : quizzItem.question != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = question != null ? question.hashCode() : 0;
        result = 31 * result + (proposedAnswers != null ? Arrays.hashCode(proposedAnswers) : 0);
        result = 31 * result + (answerIdx != null ? answerIdx.hashCode() : 0);
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (hasBeenCorrectlyAnswered ? 1 : 0);
        return result;
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
