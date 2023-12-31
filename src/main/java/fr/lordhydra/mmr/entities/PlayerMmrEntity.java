package fr.lordhydra.mmr.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Builder
@ToString
@Accessors(fluent = true)
public class PlayerMmrEntity {

    private int id;

    @Getter
    private UUID playerUUID;

    @Getter
    private String playerName;

    @Getter
    private Date created, updated;

    @Getter
    @Setter
    private BigDecimal mmr;

    public void addMmr(BigDecimal mmrToAdd) {
        mmr = mmr.add(mmrToAdd);
    }
}
