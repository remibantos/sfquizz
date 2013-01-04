package org.rembx.android.sfquizz.model;

import java.io.Serializable;
import java.util.List;

/**
 * Game state
 *
 * @author rembx
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


    public GameState(List<Integer> remainingItemIds) {
        this.remainingItemIds = remainingItemIds;
    }

    public List<Integer> getRemainingItemIds() {
        return remainingItemIds;
    }

    public void setRemainingItemIds(List<Integer> remainingItemIds) {
        this.remainingItemIds = remainingItemIds;
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
