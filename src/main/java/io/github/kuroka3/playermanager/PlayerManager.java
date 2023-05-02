package io.github.kuroka3.playermanager;

import io.github.kuroka3.playermanager.Commands.Moderate;
import io.github.kuroka3.playermanager.Util.LoadManager;
import io.github.kuroka3.playermanager.Util.SaveManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.UUID;


public final class PlayerManager extends JavaPlugin implements Listener, CommandExecutor {

    public static String playersjson;

    public static HashMap<UUID, String> nicknames;
    public static HashMap<UUID, Boolean> bans;
    public static HashMap<UUID, String> banreasons;
    public static HashMap<UUID, Integer> warns;

    @Override
    public void onEnable() {
        System.out.println("PlayerManager is enabled");
        getServer().getPluginManager().registerEvents(this, this);
        playersjson = PlayerManager.getPlugin(PlayerManager.class).getDataFolder() + "/players.json";
        nicknames = LoadManager.loadplayers();
        bans = LoadManager.loadbans();
        banreasons = LoadManager.loadbanreasons();
        warns = LoadManager.loadwarns();
        getCommand("moderate").setExecutor(new Moderate());
    }



    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (command.getName().equalsIgnoreCase("save-data")) {
            if (sender instanceof Player || sender instanceof ConsoleCommandSender) {
                if (sender.hasPermission("playermanager.data.save")) {
                    SaveManager.savedata(nicknames, bans, banreasons, warns);
                    sender.sendMessage("Save data completed");
                    System.out.println("Players' data was saved");
                } else {
                    sender.sendMessage(ChatColor.RED + "You don't have permission");
                }
            }
        } else if (command.getName().equalsIgnoreCase("reload-data")) {
            if (sender instanceof Player || sender instanceof ConsoleCommandSender) {
                if (sender.hasPermission("playermanager.data.reload")) {
                    nicknames = LoadManager.loadplayers();
                    bans = LoadManager.loadbans();
                    banreasons = LoadManager.loadbanreasons();
                    warns = LoadManager.loadwarns();
                    sender.sendMessage("reload data completed");
                    System.out.println("Players' data was reloaded");
                } else {
                    sender.sendMessage(ChatColor.RED + "You don't have permission");
                }
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



    @Override
    public void onDisable() {
        SaveManager.savedata(nicknames, bans, banreasons, warns);
    }
}
