package org.rembx.android.sfquizz.model;

import java.io.Serializable;
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

    private int totalAnswers;

    private int goodAnswers;


    public GameState(List<Integer> remainingItemIds) {
        this.remainingItemIds = remainingItemIds;
    }

    public List<Integer> getRemainingItemIds() {
        return remainingItemIds;
    }

    public void setRemainingItemIds(List<Integer> remainingItemIds) {
        this.remainingItemIds = remainingItemIds;
    }

    public int getTotalAnswers() {
        return totalAnswers;
    }

    public void setTotalAnswers(int totalAnswers) {
        this.totalAnswers = totalAnswers;
    }

    public int getGoodAnswers() {
        return goodAnswers;
    }

    public void setGoodAnswers(int goodAnswers) {
        this.goodAnswers = goodAnswers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GameState gameState = (GameState) o;

        return !(remainingItemIds != null ? !remainingItemIds.equals(gameState.remainingItemIds) : gameState.remainingItemIds != null);

    }

    @Override
    public int hashCode() {
        return remainingItemIds != null ? remainingItemIds.hashCode() : 0;
    }
}
