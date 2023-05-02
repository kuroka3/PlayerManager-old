package io.github.kuroka3.playermanager.Util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.kuroka3.playermanager.PlayerManager;
import org.jetbrains.annotations.NotNull;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SaveManager {
    public static void savedata(@NotNull HashMap<UUID, String> nicknames, HashMap<UUID, Boolean> bans, HashMap<UUID, String> banreasons, HashMap<UUID, Integer> warns) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Map<String, Map<String, Object>> jsonMap = new HashMap<>();
        for (UUID uuid : nicknames.keySet()) {
            Map<String, Object> innerMap = new HashMap<>();
            innerMap.put("nickname", nicknames.get(uuid));
            innerMap.put("ban", bans.get(uuid));
            innerMap.put("banreason", banreasons.get(uuid));
            innerMap.put("warn", warns.get(uuid));
            jsonMap.put(uuid.toString(), innerMap);
        }

        String jsonString = gson.toJson(jsonMap);

        try (FileWriter file = new FileWriter(PlayerManager.playersjson)) {
            file.write(jsonString);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("File save completed");
    }
}
