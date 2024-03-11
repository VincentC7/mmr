package fr.lordhydra.mmr.commands.confirmationCommand;

import fr.lordhydra.mmr.error.Result;

public interface ConfirmationCommand {
    public Result prepare();
    public boolean check();
    public Result confirm();
}
