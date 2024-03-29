package fr.lordhydra.mmr.config;

import fr.lordhydra.mmr.services.FileService;
import org.bukkit.configuration.file.FileConfiguration;

public class Lang {

    //================= Commands ================= //

    //Default
    public static String unknownCommand, cantBeExecuteByTerminal, playerNotFound, tooManyArgument;

    // ------- Player command -------
    //Rank
    public static String currentPlayerMmrMessage, otherPLayerMmrMessage, currentPlayerSampleCommand,
            currentPlayerCommandDescription, otherPlayerSampleCommand, otherPlayerCommandDescription;

    //Top
    public static String invalidTopCommand, topNextPageMessage, topSampleCommand, topCommandDescription;

    //On (enable mmr for players)
    public static String playerMmrAlreadyActive, playerMmrEnableSuccess, enableMmrSampleCommand, enableMmrCommandDescription;

    //Off (disable mmr for players)
    public static String playerMmrDisableSuccess, playerMmrAlreadyDisabled, disableMmrSampleCommand, disableMmrCommandDescription;

    //Status
    public static String playerChangeStatusOnCooldown, playerChangeStatusAlreadyStarted,
            playerMmrIsNowActive, playerMmrIsNowDisable, playerMmrAlreadyBan, playerMmrIsNotBan;

    // ------- Admin commands -------
    public static String invalidMmrFormat;
    //Add
    public static String invalidAddCommand, addNegativeMmrParamError, addSuccessMessage, addErrorMessage,
            addSampleCommand, addCommandDescription;

    //Del
    public static String invalidDelCommand, delNegativeMmrParamError, delSuccessMessage, delErrorMessage,
            delSampleCommand, delCommandDescription;

    //Reset
    public static String invalidResetCommand, resetSuccessMessage, resetSampleCommand, resetCommandDescription;

    //Ban
    public static String invalidBanCommand, banSampleCommand, banCommandDescription, banSuccessMessage,
            playerMmrIsBan;

    //unban
    public static String unbanSuccessMessage, invalidunbanCommand, unbanSampleCommand, unbanCommandDescription;

    //info
    public static String invalidPlayerInfoCommand;

    //banlist
    public static String banlistSampleCommand, banlistCommandDescription, bannedListEmpty;

    //================= Commands ================= //

    public static void load() {
        FileConfiguration configuration = FileService.getInstance()
                .getFile("lang").getConfig();

        unknownCommand = configuration.getString("commands.default.unknownCommand");
        cantBeExecuteByTerminal = configuration.getString("commands.default.cantBeExecuteByTerminal");
        playerNotFound = configuration.getString("commands.default.playerNotFound");
        tooManyArgument = configuration.getString("commands.default.tooManyArgument");

        invalidMmrFormat = configuration.getString("commands.admin.invalidMmrFormat");

        currentPlayerMmrMessage = configuration.getString("commands.player.rank.currentMmrMessage");
        currentPlayerSampleCommand = configuration.getString("commands.player.rank.sampleCommand");
        currentPlayerCommandDescription = configuration.getString("commands.player.rank.description");

        otherPLayerMmrMessage = configuration.getString("commands.player.rank.playerMmrMessage");
        otherPlayerSampleCommand = configuration.getString("commands.player.rank.samplePlayerCommand");
        otherPlayerCommandDescription = configuration.getString("commands.player.rank.playerDescription");

        invalidAddCommand = configuration.getString("commands.admin.add.invalidCommand");
        addNegativeMmrParamError = configuration.getString("commands.admin.add.negativeMmrParamError");
        addSuccessMessage = configuration.getString("commands.admin.add.successMessage");
        addErrorMessage = configuration.getString("commands.admin.add.errorMessage");
        addSampleCommand = configuration.getString("commands.admin.add.sampleCommand");
        addCommandDescription = configuration.getString("commands.admin.add.description");

        delNegativeMmrParamError = configuration.getString("commands.admin.del.negativeMmrParamError");
        invalidDelCommand = configuration.getString("commands.admin.del.invalidCommand");
        delSuccessMessage = configuration.getString("commands.admin.del.successMessage");
        delErrorMessage = configuration.getString("commands.admin.del.errorMessage");
        delSampleCommand = configuration.getString("commands.admin.del.sampleCommand");
        delCommandDescription = configuration.getString("commands.admin.del.description");

        invalidResetCommand = configuration.getString("commands.admin.reset.invalidCommand");
        resetSuccessMessage = configuration.getString("commands.admin.reset.successMessage");
        resetSampleCommand = configuration.getString("commands.admin.reset.sampleCommand");
        resetCommandDescription = configuration.getString("commands.admin.reset.description");

        invalidTopCommand = configuration.getString("commands.player.top.invalidCommand");
        topNextPageMessage = configuration.getString("commands.player.top.nextPageMessage");
        topSampleCommand = configuration.getString("commands.player.top.sampleCommand");
        topCommandDescription = configuration.getString("commands.player.top.description");

        playerMmrAlreadyActive = configuration.getString("commands.player.enable.playerMmrAlreadyActive");
        playerMmrEnableSuccess = configuration.getString("commands.player.enable.playerMmrEnableSuccess");
        enableMmrSampleCommand = configuration.getString("commands.player.enable.sampleCommand");
        enableMmrCommandDescription = configuration.getString("commands.player.enable.description");
        playerMmrIsNowActive = configuration.getString("commands.player.status.playerMmrIsNowActive");

        playerMmrDisableSuccess = configuration.getString("commands.player.disable.playerMmrDisableSuccess");
        playerMmrAlreadyDisabled = configuration.getString("commands.player.disable.playerMmrAlreadyDisabled");
        disableMmrSampleCommand = configuration.getString("commands.player.disable.sampleCommand");
        disableMmrCommandDescription = configuration.getString("commands.player.disable.description");
        playerMmrIsNowDisable = configuration.getString("commands.player.status.playerMmrIsNowDisable");

        playerChangeStatusOnCooldown = configuration.getString("commands.player.status.playerChangeStatusOnCooldown");
        playerChangeStatusAlreadyStarted = configuration.getString("commands.player.status.playerChangeStatusAlreadyStarted");

        invalidBanCommand = configuration.getString("commands.admin.ban.invalidCommand");
        banSuccessMessage = configuration.getString("commands.admin.ban.successMessage");
        banSampleCommand = configuration.getString("commands.admin.ban.sampleCommand");
        banCommandDescription = configuration.getString("commands.admin.ban.description");
        playerMmrIsBan = configuration.getString("commands.admin.status.ban");
        playerMmrAlreadyBan = configuration.getString("commands.admin.ban.alreadyBan");

        invalidunbanCommand = configuration.getString("commands.admin.unban.invalidCommand");
        unbanSuccessMessage = configuration.getString("commands.admin.unban.successMessage");
        unbanSampleCommand = configuration.getString("commands.admin.unban.sampleCommand");
        unbanCommandDescription = configuration.getString("commands.admin.unban.commandDescription");
        playerMmrIsNotBan = configuration.getString("commands.admin.unban.isNotBan");

        banlistSampleCommand = configuration.getString("commands.admin.banlist.sampleCommand");
        banlistCommandDescription = configuration.getString("commands.admin.banlist.description");
        bannedListEmpty = configuration.getString("commands.admin.banlist.bannedListEmpty");
    }
}
