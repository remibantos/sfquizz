package org.rembx.android.sfquizz.model;

import java.io.Serializable;
import java.util.Set;

/**
 * User Statistics.
 * Store quizz games statistics
 *
 * @author rembx
 */
public class Stats implements Serializable {

    static final long serialVersionUID;

    static {
        serialVersionUID = 5926487372717783265L;
    }

    /**
     * The list of all user answered QuizzItem Ids
     */
    private Set<Integer> answeredItemIds;

    /**
     * The list of all user good answered QuizzItem Ids
     */
    private Set<Integer> goodAnsweredItemIds;


    public Stats(Set<Integer> answeredItemIds, Set<Integer> goodAnsweredItemIds) {
        this.answeredItemIds = answeredItemIds;
        this.goodAnsweredItemIds = goodAnsweredItemIds;
    }


    public Set<Integer> getAnsweredItemIds() {
        return answeredItemIds;
    }


    public Set<Integer> getGoodAnsweredItemIds() {
        return goodAnsweredItemIds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Stats stats = (Stats) o;

        return !(answeredItemIds != null ? !answeredItemIds.equals(stats.answeredItemIds) : stats.answeredItemIds != null)
                && !(goodAnsweredItemIds != null ? !goodAnsweredItemIds.equals(stats.goodAnsweredItemIds) : stats.goodAnsweredItemIds != null);

    }

    @Override
    public int hashCode() {
        int result = answeredItemIds != null ? answeredItemIds.hashCode() : 0;
        result = 31 * result + (goodAnsweredItemIds != null ? goodAnsweredItemIds.hashCode() : 0);
        return result;
    }
}
