package fr.lordhydra.mmr.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.UUID;

@Builder
@ToString
@Accessors(fluent = true)
public class MmrBalanceEntity {

    @Getter
    private UUID firstPlayerUUID, secondPlayerUUID;

    @Getter
    private Date created, updated;

    @Getter
    @Setter
    private int balancePlayer1, balancePlayer2;

}
