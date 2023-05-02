package io.github.kuroka3.playermanager.Util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.github.kuroka3.playermanager.Class.Users;
import io.github.kuroka3.playermanager.PlayerManager;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.UUID;

public class LoadManager {
    public static @Nullable HashMap<UUID, String> loadplayers() {
        try {
            File file = new File(PlayerManager.playersjson);
            if(!file.isFile()) {
                if (!file.createNewFile()) {
                    System.out.println("File create failed");
                }
            }

            FileReader fileReader = new FileReader(file);


            Gson gson = new Gson();
            Type type = new TypeToken<HashMap<String, Users>>(){}.getType();
            HashMap<String, Users> players = gson.fromJson(fileReader, type);

            if (players == null) {
                players = new HashMap<String, Users>();
            }

            HashMap<UUID, String> playerNames = new HashMap<>();
            for (String uuidString : players.keySet()) {
                Users player = players.get(uuidString);
                UUID uuid = UUID.fromString(uuidString);
                String nickname = player.getNickname();
                playerNames.put(uuid, nickname);
            }
            System.out.println("File load completed");
            return playerNames;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static @Nullable HashMap<UUID, Boolean> loadbans() {
        try {
            File file = new File(PlayerManager.getPlugin(PlayerManager.class).getDataFolder() + "/players.json");
            FileReader fileReader = null;

            fileReader = new FileReader(file);


            Gson gson = new Gson();
            Type type = new TypeToken<HashMap<String, Users>>(){}.getType();
            HashMap<String, Users> players = gson.fromJson(fileReader, type);

            if (players == null) {
                players = new HashMap<String, Users>();
            }

            HashMap<UUID, Boolean> bans = new HashMap<>();
            for (String uuidString : players.keySet()) {
                Users player = players.get(uuidString);
                UUID uuid = UUID.fromString(uuidString);
                Boolean banable = player.isBan();
                bans.put(uuid, banable);
            }
            return bans;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static @Nullable HashMap<UUID, String> loadbanreasons() {
        try {
            File file = new File(PlayerManager.getPlugin(PlayerManager.class).getDataFolder() + "/players.json");
            FileReader fileReader = null;

            fileReader = new FileReader(file);


            Gson gson = new Gson();
            Type type = new TypeToken<HashMap<String, Users>>(){}.getType();
            HashMap<String, Users> players = gson.fromJson(fileReader, type);

            if (players == null) {
                players = new HashMap<String, Users>();
            }

            HashMap<UUID, String> banReason = new HashMap<>();
            for (String uuidString : players.keySet()) {
                Users player = players.get(uuidString);
                UUID uuid = UUID.fromString(uuidString);
                String reason = player.getBanreason();
                banReason.put(uuid, reason);
            }
            return banReason;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static @Nullable HashMap<UUID, Integer> loadwarns() {
        try {
            File file = new File(PlayerManager.getPlugin(PlayerManager.class).getDataFolder() + "/players.json");
            FileReader fileReader = null;

            fileReader = new FileReader(file);


            Gson gson = new Gson();
            Type type = new TypeToken<HashMap<String, Users>>(){}.getType();
            HashMap<String, Users> players = gson.fromJson(fileReader, type);

            if (players == null) {
                players = new HashMap<String, Users>();
            }

            HashMap<UUID, Integer> playerwarns = new HashMap<>();
            for (String uuidString : players.keySet()) {
                Users player = players.get(uuidString);
                UUID uuid = UUID.fromString(uuidString);
                Integer warns = player.getWarns();
                playerwarns.put(uuid, warns);
            }
            return playerwarns;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
