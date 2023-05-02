package io.github.kuroka3.playermanager;

import io.github.kuroka3.playermanager.Util.LoadManager;
import io.github.kuroka3.playermanager.Util.SaveManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.UUID;


public final class PlayerManager extends JavaPlugin implements Listener, CommandExecutor {

    public static String playersjson;

    private HashMap<UUID, String> nicknames;
    private HashMap<UUID, Boolean> bans;
    private HashMap<UUID, String> banreasons;
    private HashMap<UUID, Integer> warns;

    @Override
    public void onEnable() {
        System.out.println("PlayerManager is enabled");
        getServer().getPluginManager().registerEvents(this, this);
        playersjson = PlayerManager.getPlugin(PlayerManager.class).getDataFolder() + "/players.json";
        nicknames = LoadManager.loadplayers();
        bans = LoadManager.loadbans();
        banreasons = LoadManager.loadbanreasons();
        warns = LoadManager.loadwarns();
    }



    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(command.getName().equalsIgnoreCase("save-data")) {
            SaveManager.savedata(nicknames, bans, banreasons, warns);
            sender.sendMessage("Save data completed");
            System.out.println("Players' data was saved");
        } else if (command.getName().equalsIgnoreCase("reload-data")) {
            nicknames = LoadManager.loadplayers();
            bans = LoadManager.loadbans();
            banreasons = LoadManager.loadbanreasons();
            warns = LoadManager.loadwarns();
            sender.sendMessage("reload data completed");
            System.out.println("Players' data was reloaded");
        } else if(command.getName().equalsIgnoreCase("moderate")) {
            sender.sendMessage("asdfasdfdasfasfs");
            if(args.length >= 3) {
                Boolean isoffline = false;
                Player target = Bukkit.getServer().getPlayerExact(args[0]);
                OfflinePlayer targetof = null;
                if(target==null) {
                    targetof = Bukkit.getServer().getOfflinePlayer(args[0]);
                    if(targetof!=null) {
                        isoffline = true;
                    } else {
                        sender.sendMessage(ChatColor.RED + "Couldn't get player");
                        return false;
                    }
                }
                if(args[1].equals("ban")) {
                    StringBuilder builder = new StringBuilder();
                    for (int i = 2; i < args.length; i++) {
                        builder.append(args[i]);
                        if(args.length-i != 1) {
                            builder.append(" ");
                        }
                    }

                    String banre = builder.toString();
                    if (!isoffline) banplayer(target, banre); else offlinebanplayer(targetof, banre);
                }

            } else {
                sender.sendMessage(ChatColor.RED + "\uac12\uc774\u0020\ubd80\uc871\ud569\ub2c8\ub2e4\u002e");
                sender.sendMessage(ChatColor.GOLD + "USAGE: /moderate [player] [ban/unban/warn/unwarn] [reason]");
            }
        } else if(command.getName().equalsIgnoreCase("nowtime")) {
            String banid = LocalDateTime.now().toString();
            banid = banid.replaceAll("-", "");
            banid = banid.replaceAll("T", "");
            banid = banid.replaceAll(":", "");
            banid = banid.replaceAll("\\.", "");
            sender.sendMessage(banid);
        }
        return true;
    }

    @EventHandler
    public void onJoin(PlayerLoginEvent e) {
        Player p = e.getPlayer();
        if(!nicknames.containsKey(p.getUniqueId())) {
            nicknames.put(p.getUniqueId(), p.getName());
            bans.put(p.getUniqueId(), false);
            banreasons.put(p.getUniqueId(), "");
            warns.put(p.getUniqueId(), 0);
        }
        if(bans.get(p.getUniqueId())) {
            e.disallow(PlayerLoginEvent.Result.KICK_OTHER,
                    ChatColor.RED + "\ub2f9\uc2e0\uc740\u0020\uc774\u0020\uc11c\ubc84\uc5d0\uc11c\u0020\ucc28\ub2e8\ub418\uc5c8\uc2b5\ub2c8\ub2e4"
                    + "\n\n"
                    + ChatColor.GRAY + "Reason: " + ChatColor.WHITE + banreasons.get(p.getUniqueId()));
        }
    }

    private void banplayer(Player p, String reason) {
        p.kickPlayer(ChatColor.RED + "\ub2f9\uc2e0\uc740\u0020\uc774\u0020\uc11c\ubc84\uc5d0\uc11c\u0020\ucc28\ub2e8\ub418\uc5c8\uc2b5\ub2c8\ub2e4"
                + "\n\n"
                + ChatColor.GRAY + "Reason: " + ChatColor.WHITE + reason);
        bans.put(p.getUniqueId(), true);
        banreasons.put(p.getUniqueId(), reason);
        System.out.println(p.getName() + " is banned: " + reason);
    }

    private void offlinebanplayer(OfflinePlayer p, String reason) {
        bans.put(p.getUniqueId(), true);
        banreasons.put(p.getUniqueId(), reason);
        System.out.println(p.getName() + " is banned: " + reason);
    }

    @Override
    public void onDisable() {
        SaveManager.savedata(nicknames, bans, banreasons, warns);
    }
}
