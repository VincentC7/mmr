package fr.lordhydra.mmr.error;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StatusUpdateCouldown extends Throwable {

    private long timeLeft;

    public String formatTimerToString() {
        long hours = timeLeft/3600;
        long minutes = (timeLeft%3600)/60;
        long secondes = timeLeft%60;
        return hours + " heure(s) " + minutes + " minute(s) et " + secondes + " secondes";
    }
}
