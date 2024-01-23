package fr.lordhydra.mmr.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.UUID;

@Getter
@Builder
@ToString
@Accessors(fluent = true)
public class MmrBalanceEntity {

    private UUID firstPlayerUUID, secondPlayerUUID;

    private Date created, updated;

    @Setter
    private int balance;

}
