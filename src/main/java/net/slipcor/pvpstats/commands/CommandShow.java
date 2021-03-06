package net.slipcor.pvpstats.commands;

import net.slipcor.pvpstats.PVPStats;
import net.slipcor.pvpstats.api.DatabaseAPI;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandShow extends AbstractCommand {
    public CommandShow() {
        super(new String[]{"pvpstats.count"});
    }

    @Override
    public void commit(final CommandSender sender, final String[] args) {
        if (!hasPerms(sender)) {
            return;
        }

        if (args == null || args.length < 1 || (args.length == 1 && args[0].equals("show"))) {
            // /pvpstats - show your pvp stats

            class TellLater implements Runnable {

                @Override
                public void run() {
                    final String[] info = DatabaseAPI.info(sender.getName());
                    sender.sendMessage(info);
                }

            }
            Bukkit.getScheduler().runTaskAsynchronously(PVPStats.getInstance(), new TellLater());
            return;
        }
        if (sender.hasPermission("pvpstats.top")) {

            // /pvpstats [player] - show player's pvp stats

            class TellLater implements Runnable {

                @Override
                public void run() {
                    final String[] info = DatabaseAPI.info(args[1]);
                    sender.sendMessage(info);
                }

            }
            Bukkit.getScheduler().runTaskAsynchronously(PVPStats.getInstance(), new TellLater());
        }
    }

    public List<String> completeTab(String[] args) {
        List<String> results = new ArrayList<>();

        if (args.length < 2 || args[1].equals("")) {
            // list first argument possibilities
            for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                results.add(p.getName());
            }
            return results;
        }

        if (args.length > 2) {
            return results; // don't go too far!
        }

        // we started typing!
        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            addIfMatches(results, p.getName(), p.getName());
        }
        return results;
    }

    @Override
    public List<String> getMain() {
        return Collections.singletonList("show");
    }

    @Override
    public String getName() {
        return getClass().getName();
    }

    @Override
    public List<String> getShort() {
        return Collections.singletonList("!sh");
    }

    @Override
    public String getShortInfo() {
        return "/pvpstats - show your pvp stats";
    }
}
