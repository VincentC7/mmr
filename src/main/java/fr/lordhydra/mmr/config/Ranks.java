package fr.lordhydra.mmr.config;

import fr.lordhydra.mmr.lib.rank.Rank;
import fr.lordhydra.mmr.lib.rank.RankComparator;
import fr.lordhydra.mmr.services.FileService;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.TreeSet;

public class Ranks {

    private final TreeSet<Rank> ranks;

    private static Ranks instance;

    private Ranks() {
        ranks = new TreeSet<>(new RankComparator());
        parseFile();
    }

    public static Ranks getInstance() {
        if (instance == null) instance = new Ranks();
        return instance;
    }

    private void parseFile() {
        FileConfiguration configuration = FileService.getInstance()
                .getFile("ranks").getConfig();
        ConfigurationSection section = configuration.getConfigurationSection("mmrRanks");
        if (section == null) return;
        for (String rankId :  section.getKeys(false)) {
            ConfigurationSection rankConfig = section.getConfigurationSection(rankId);
            if (rankConfig == null) continue;
            Rank rank = Rank.builder()
                    .name(rankConfig.getString("name"))
                    .chatPrefix(rankConfig.getString("chatPrefix"))
                    .mmrMin(rankConfig.getInt("mmrMin"))
                    .build();
            ranks.add(rank);
        }
    }

    public Rank getRank(int mmr) {
        if (mmr < 0 || ranks.isEmpty()) return Rank.UNIDENTIFED_RANK;
        Rank previous = ranks.first();
        for (Rank rank : ranks) {
            if (previous.getMmrMin() <= mmr && mmr < rank.getMmrMin())
                return previous;
            previous = rank;
        }
        //Highest rank
        return previous;
    }
}
