package fr.lordhydra.mmr.lib;

import java.util.Comparator;

public class RankComparator implements Comparator<Rank> {
    @Override
    public int compare(Rank rank1, Rank rank2) {
        return rank1.getMmrMin() - rank2.getMmrMin();
    }
}
