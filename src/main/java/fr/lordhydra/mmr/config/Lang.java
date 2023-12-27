package fr.lordhydra.mmr.config;

import fr.lordhydra.mmr.services.FileService;
import org.bukkit.configuration.file.FileConfiguration;

public class Lang {

    //================= Commands ================= //

    //Default
    public static String unknownCommand, cantBeExecuteByTerminal, playerNotFound;

    //Admin commands
    public static String invalidMmrFormat;
    //Add
    public static String invalidAddCommand, addNegativeMmrParamError, addSuccessMessage, addErrorMessage;

    //Del
    public static String invalidDelCommand, delNegativeMmrParamError, delSuccessMessage, delErrorMessage;

    //================= Commands ================= //

    public static void load() {
        FileConfiguration configuration = FileService.getInstance()
                .getFile("lang").getConfig();
        unknownCommand = configuration.getString("commands.default.unknownCommand");
        cantBeExecuteByTerminal = configuration.getString("commands.default.cantBeExecuteByTerminal");
        playerNotFound = configuration.getString("commands.default.playerNotFound");

        invalidMmrFormat = configuration.getString("commands.admin.invalidMmrFormat");

        invalidAddCommand = configuration.getString("commands.admin.add.invalidCommand");
        addNegativeMmrParamError = configuration.getString("commands.admin.add.negativeMmrParamError");
        addSuccessMessage = configuration.getString("commands.admin.add.successMessage");
        addErrorMessage = configuration.getString("commands.admin.add.errorMessage");

        delNegativeMmrParamError = configuration.getString("commands.admin.del.negativeMmrParamError");
        invalidDelCommand = configuration.getString("commands.admin.del.invalidCommand");
        delSuccessMessage = configuration.getString("commands.admin.del.successMessage");
        delErrorMessage = configuration.getString("commands.admin.del.errorMessage");
    }
}
