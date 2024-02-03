package fr.lordhydra.mmr.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class playerHasAlreadyTimerStarted extends Throwable {

    private long timeLeft;

    public String formatTimerToString() {
        long minutes = timeLeft/60;
        return minutes + " minute(s)";
    }
}
