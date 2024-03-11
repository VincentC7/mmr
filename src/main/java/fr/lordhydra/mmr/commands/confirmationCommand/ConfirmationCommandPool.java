package fr.lordhydra.mmr.commands.confirmationCommand;

import fr.lordhydra.mmr.error.playerHasAlreadyConfirmationCommand;

import java.util.HashMap;
import java.util.UUID;

public class ConfirmationCommandPool {

    private static final HashMap<UUID, AbstractConfirmCommand> confirmCommands = new HashMap<>();

    public static void addConfirmationRequest(UUID playerUuid, AbstractConfirmCommand confirmationCommand) throws playerHasAlreadyConfirmationCommand {
        if (confirmCommands.containsKey(playerUuid)) {
            throw new playerHasAlreadyConfirmationCommand();
        }
        confirmCommands.put(playerUuid, confirmationCommand);
    }

    public static AbstractConfirmCommand getConfirmationCommand(UUID playerUuid) {
        return confirmCommands.get(playerUuid);
    }

    public static void removeConfirmCommand(UUID playerUuid) {
        confirmCommands.remove(playerUuid);
    }

    public static boolean contains(UUID playerUuid) {
        return confirmCommands.containsKey(playerUuid);
    }
}
