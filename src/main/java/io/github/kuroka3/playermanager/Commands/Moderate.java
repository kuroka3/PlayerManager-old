package io.github.kuroka3.playermanager.Commands;

import io.github.kuroka3.playermanager.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Moderate implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
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
                if(sender.hasPermission("playermanager.moderate.ban")) {
                    StringBuilder builder = new StringBuilder();
                    for (int i = 2; i < args.length; i++) {
                        builder.append(args[i]);
                        if (args.length - i != 1) {
                            builder.append(" ");
                        }
                    }

                    String banre = builder.toString();
                    if (!isoffline) banplayer(target, banre, sender);
                    else offlinebanplayer(targetof, banre, sender);
                } else {
                    sender.sendMessage(ChatColor.RED + "You don't have permission");
                }
            } else if(args[1].equals("warn")) {
                if(sender.hasPermission("playermanager.moderate.warn")) {
                    StringBuilder builder = new StringBuilder();
                    for (int i = 2; i < args.length; i++) {
                        builder.append(args[i]);
                        if (args.length - i != 1) {
                            builder.append(" ");
                        }
                    }

                    String warnre = builder.toString();
                    warnplayer(target, warnre, sender);
                } else {
                    sender.sendMessage(ChatColor.RED + "You don't have permission");
                }
            } else if(args[1].equals("kick")) {
                if(sender.hasPermission("playermanager.moderate.kick")) {
                    StringBuilder builder = new StringBuilder();
                    for (int i = 2; i < args.length; i++) {
                        builder.append(args[i]);
                        if (args.length - i != 1) {
                            builder.append(" ");
                        }
                    }

                    String kickre = builder.toString();
                    kickplayer(target, kickre, sender);
                } else {
                    sender.sendMessage(ChatColor.RED + "You don't have permission");
                }
            }

        } else {
            sender.sendMessage(ChatColor.RED + "\uac12\uc774\u0020\ubd80\uc871\ud569\ub2c8\ub2e4\u002e");
            sender.sendMessage(ChatColor.GOLD + "USAGE: /moderate [player] [ban/unban/warn/unwarn] [reason]");
        }
        return true;
    }

    // TabComplete

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        ArrayList<String> list = new ArrayList<>();
        if (args.length == 0) return list;
        if (args.length == 1) {
            for(int i = 0; i < Bukkit.getOfflinePlayers().length; i++) {
                OfflinePlayer[] ofps = Bukkit.getOfflinePlayers();
                list.add(ofps[i].getName());
            }
        } else if (args.length == 2) {
            list.add("ban");
            list.add("warn");
            list.add("kick");
            list.add("unban");
            list.add("unwarn");
        } else if (args.length == 3) {
            list.add("No");
        } else if (args.length == 4) {
            list.add("reason");
        } else if (args.length == 5) {
            list.add("given");
        } else if (args.length > 5) {
            list.clear();
        }

        ArrayList<String> completerList = new ArrayList<>();
        String currentarg = args[args.length-1];
        for (String s : list) {
            String sl = s.toLowerCase();
            if (sl.startsWith(currentarg)) {
                completerList.add(s);
            }
        }
        return completerList;
    }

    // private methods

    private void banplayer(Player p, String reason, CommandSender sender) {
        p.kickPlayer(ChatColor.RED + "\ub2f9\uc2e0\uc740\u0020\uc774\u0020\uc11c\ubc84\uc5d0\uc11c\u0020\ucc28\ub2e8\ub418\uc5c8\uc2b5\ub2c8\ub2e4"
                + "\n\n"
                + ChatColor.GRAY + "Reason: " + ChatColor.WHITE + reason);
        sender.sendMessage(ChatColor.GRAY + "[" + ChatColor.LIGHT_PURPLE + "!" + ChatColor.GRAY + "] "
                + ChatColor.YELLOW + p.getName() + ChatColor.RED + "\ub2d8\uc744\u0020\uc11c\ubc84\uc5d0\uc11c\u0020\ucc28\ub2e8\ud588\uc2b5\ub2c8\ub2e4\u003a\u0020" + ChatColor.GOLD + reason);
        PlayerManager.bans.put(p.getUniqueId(), true);
        PlayerManager.banreasons.put(p.getUniqueId(), reason);
        System.out.println(p.getName() + " is banned: " + reason);
    }

    private void offlinebanplayer(OfflinePlayer p, String reason, CommandSender sender) {
        sender.sendMessage(ChatColor.GRAY + "[" + ChatColor.LIGHT_PURPLE + "!" + ChatColor.GRAY + "] "
                + ChatColor.YELLOW + p.getName() + ChatColor.RED + "\ub2d8\uc744\u0020\uc11c\ubc84\uc5d0\uc11c\u0020\ucc28\ub2e8\ud588\uc2b5\ub2c8\ub2e4\u003a\u0020" + ChatColor.GOLD + reason);
        PlayerManager.bans.put(p.getUniqueId(), true);
        PlayerManager.banreasons.put(p.getUniqueId(), reason);
        System.out.println(p.getName() + " is banned: " + reason);
    }

    private void warnplayer(Player p, String reason, CommandSender sender) {
        int warn = PlayerManager.warns.get(p.getUniqueId());
        warn++;
        PlayerManager.warns.put(p.getUniqueId(), warn);
        p.sendMessage(ChatColor.GRAY + "[" + ChatColor.LIGHT_PURPLE + "!" + ChatColor.GRAY + "] "
                + ChatColor.RED + "\uad00\ub9ac\uc790\uc5d0\uac8c\u0020\uacbd\uace0\ub97c\u0020\ubc1b\uc558\uc2b5\ub2c8\ub2e4\u003a\u0020" + ChatColor.GOLD + reason + " "
                + ChatColor.YELLOW + "(\ucd1d " + warn + "\ud68c)");
        sender.sendMessage(ChatColor.GRAY + "[" + ChatColor.LIGHT_PURPLE + "!" + ChatColor.GRAY + "] "
                + ChatColor.YELLOW + p.getName() + ChatColor.RED + "\ub2d8\uc5d0\uac8c\u0020\uacbd\uace0\u0020\u0031\ud68c\ub97c\u0020\ubd80\uc5ec\ud588\uc2b5\ub2c8\ub2e4\u003a\u0020" + ChatColor.GOLD + reason + " "
                + ChatColor.YELLOW + "(\ucd1d " + warn + "\ud68c)");
    }

    private void kickplayer(Player p, String reason, CommandSender sender) {
        p.kickPlayer(ChatColor.RED + "\ub2f9\uc2e0\uc740\u0020\uc774\u0020\uc11c\ubc84\uc5d0\uc11c\u0020\ucd94\ubc29\u0020\ub2f9\ud588\uc2b5\ub2c8\ub2e4"
                + "\n\n"
                + ChatColor.GRAY + "Reason: " + ChatColor.WHITE + reason);
        sender.sendMessage(ChatColor.GRAY + "[" + ChatColor.LIGHT_PURPLE + "!" + ChatColor.GRAY + "] "
                + ChatColor.YELLOW + p.getName() + ChatColor.RED + "\ub2d8\uc744\u0020\uc11c\ubc84\uc5d0\uc11c\u0020\ucd94\ubc29\ud588\uc2b5\ub2c8\ub2e4\u003a\u0020" + ChatColor.GOLD + reason);
    }
}
