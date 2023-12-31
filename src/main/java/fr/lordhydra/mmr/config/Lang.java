package fr.lordhydra.mmr.config;

import fr.lordhydra.mmr.services.FileService;
import org.bukkit.configuration.file.FileConfiguration;

public class Lang {

    //================= Commands ================= //

    //Default
    public static String unknownCommand, cantBeExecuteByTerminal, playerNotFound;

    // ------- Player command -------
    //Rank
    public static String currentPlayerMmrMessage, otherPLayerMmrMessage;
    //Top
    public static String invalidTopCommand, topNextPageMessage;

    // ------- Admin commands -------
    public static String invalidMmrFormat;
    //Add
    public static String invalidAddCommand, addNegativeMmrParamError, addSuccessMessage, addErrorMessage;

    //Del
    public static String invalidDelCommand, delNegativeMmrParamError, delSuccessMessage, delErrorMessage;

    //Reset
    public static String invalidResetCommand, resetSuccessMessage;

    //================= Commands ================= //

    public static void load() {
        FileConfiguration configuration = FileService.getInstance()
                .getFile("lang").getConfig();
        unknownCommand = configuration.getString("commands.default.unknownCommand");
        cantBeExecuteByTerminal = configuration.getString("commands.default.cantBeExecuteByTerminal");
        playerNotFound = configuration.getString("commands.default.playerNotFound");

        invalidMmrFormat = configuration.getString("commands.admin.invalidMmrFormat");

        currentPlayerMmrMessage = configuration.getString("commands.player.rank.currentMmrMessage");
        otherPLayerMmrMessage = configuration.getString("commands.player.rank.playerMmrMessage");

        invalidAddCommand = configuration.getString("commands.admin.add.invalidCommand");
        addNegativeMmrParamError = configuration.getString("commands.admin.add.negativeMmrParamError");
        addSuccessMessage = configuration.getString("commands.admin.add.successMessage");
        addErrorMessage = configuration.getString("commands.admin.add.errorMessage");

        delNegativeMmrParamError = configuration.getString("commands.admin.del.negativeMmrParamError");
        invalidDelCommand = configuration.getString("commands.admin.del.invalidCommand");
        delSuccessMessage = configuration.getString("commands.admin.del.successMessage");
        delErrorMessage = configuration.getString("commands.admin.del.errorMessage");

        invalidResetCommand = configuration.getString("commands.admin.reset.invalidCommand");
        resetSuccessMessage = configuration.getString("commands.admin.reset.successMessage");

        invalidTopCommand = configuration.getString("commands.player.top.invalidCommand");
        topNextPageMessage = configuration.getString("commands.player.top.nextPageMessage");
    }
}
