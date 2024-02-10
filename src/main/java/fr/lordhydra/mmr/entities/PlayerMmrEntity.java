package fr.lordhydra.mmr.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
    @Setter
    private LocalDateTime created, mmrUpdated, statusUpdated;

    @Getter
    @Setter
    private BigDecimal mmr;

    @Setter
    @Getter
    private PlayerMmrStatus status;

    public void addMmr(BigDecimal mmrToAdd) {
        mmr = mmr.add(mmrToAdd);
    }
}
