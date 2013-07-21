package org.rembx.android.sfquizz.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Stores game state
 *
 * @author remibantos
 */
public class GameState implements Serializable {

    static final long serialVersionUID;

    static {
        serialVersionUID = 7777487372717783265L;
    }

    /**
     * The list of all user answered QuizzItem Ids
     */
    private List<Integer> remainingItemIds;

    private int totalAnswered;

    private int totalAnswers;

    private int goodAnswers;

    public GameState() {
        remainingItemIds = new ArrayList<Integer>();
    }

    public GameState(List<Integer> remainingItemIds) {
        this.remainingItemIds = remainingItemIds;
    }

    public List<Integer> getRemainingItemIds() {
        return remainingItemIds;
    }

    public void setRemainingItemIds(List<Integer> remainingItemIds) {
        this.remainingItemIds = remainingItemIds;
    }

    public int getTotalAnswered() {
        return totalAnswered;
    }

    public void setTotalAnswered(int totalAnswered) {
        this.totalAnswered = totalAnswered;
    }

    public int getGoodAnswers() {
        return goodAnswers;
    }

    public void setGoodAnswers(int goodAnswers) {
        this.goodAnswers = goodAnswers;
    }

    public int getTotalAnswers() {
        return totalAnswers;
    }

    public void setTotalAnswers(int totalAnswers) {
        this.totalAnswers = totalAnswers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GameState gameState = (GameState) o;

        if (goodAnswers != gameState.goodAnswers) return false;
        if (totalAnswered != gameState.totalAnswered) return false;
        if (totalAnswers != gameState.totalAnswers) return false;
        if (remainingItemIds != null ? !remainingItemIds.equals(gameState.remainingItemIds) : gameState.remainingItemIds != null)
            return false;

        return true;
    }

    @Override
    public String toString() {
        return "GameState{" +
                "remainingItemIds=" + remainingItemIds +
                ", totalAnswered=" + totalAnswered +
                ", totalAnswers=" + totalAnswers +
                ", goodAnswers=" + goodAnswers +
                '}';
    }

    @Override
    public int hashCode() {
        int result = remainingItemIds != null ? remainingItemIds.hashCode() : 0;
        result = 31 * result + totalAnswered;
        result = 31 * result + totalAnswers;
        result = 31 * result + goodAnswers;
        return result;
    }
}
