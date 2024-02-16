package fr.lordhydra.mmr.lib;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
@EqualsAndHashCode
public class Rank {
    private String name, chatPrefix;
    private int mmrMin;

    public static Rank UNIDENTIFED_RANK = new Rank("&0Inconnu","",-1);
}
