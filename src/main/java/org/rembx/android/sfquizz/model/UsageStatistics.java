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
    private Set<Integer> goodAnswered;

    public UsageStatistics(){
        answered =  new HashSet<Integer>();
        goodAnswered =  new HashSet<Integer>();
    }

    public Set<Integer> getAnswered() {
        return answered;
    }


    public Set<Integer> getGoodAnswered() {
        return goodAnswered;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UsageStatistics that = (UsageStatistics) o;

        if (answered != null ? !answered.equals(that.answered) : that.answered != null) return false;
        if (goodAnswered != null ? !goodAnswered.equals(that.goodAnswered) : that.goodAnswered != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = answered != null ? answered.hashCode() : 0;
        result = 31 * result + (goodAnswered != null ? goodAnswered.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UsageStatistics{" +
                "answered=" + answered +
                ", goodAnswered=" + goodAnswered +
                '}';
    }

}
