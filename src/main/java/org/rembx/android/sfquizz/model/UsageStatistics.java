package org.rembx.android.sfquizz.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Usage Statistics.
 *
 * @author remibantos
 */
public class UsageStatistics implements Serializable {

    static final long serialVersionUID;

    static {
        serialVersionUID = 5926487372717783265L;
    }

    /**
     * The list of all user answered QuizzItem Ids
     */
    private Set<Integer> answered;

    /**
     * The list of all user good answered QuizzItem Ids
     */
    private Set<Integer> goodAnswered
            ;


    public UsageStatistics(Set<Integer> answered, Set<Integer> goodAnsweredItemIds) {
        this.answered = answered;
        this.goodAnswered = goodAnsweredItemIds;
    }


    public Set<Integer> getAnswered() {
        if (answered==null)
            return new HashSet<Integer>();
        return answered;
    }


    public Set<Integer> getGoodAnswered() {
        if (goodAnswered == null)
            return new HashSet<Integer>();
        return goodAnswered;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UsageStatistics usageStatistics = (UsageStatistics) o;

        return !(answered != null ? !answered.equals(usageStatistics.answered) : usageStatistics.answered != null)
                && !(goodAnswered != null ? !goodAnswered.equals(usageStatistics.goodAnswered) : usageStatistics.goodAnswered != null);

    }

    @Override
    public int hashCode() {
        int result = answered != null ? answered.hashCode() : 0;
        result = 31 * result + (goodAnswered != null ? goodAnswered.hashCode() : 0);
        return result;
    }
}
