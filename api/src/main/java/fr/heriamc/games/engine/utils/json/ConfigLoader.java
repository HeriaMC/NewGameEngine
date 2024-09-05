package fr.heriamc.games.engine.utils.json;

import com.google.gson.JsonParser;
import fr.heriamc.games.engine.utils.Utils;
import fr.heriamc.games.engine.utils.concurrent.MultiThreading;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;

public class ConfigLoader {

    public static <T> T loadConfig(File file, Class<T> clazz) {
        try {
            final var gson = Utils.gson;
            final var parser = new JsonParser();

            if (!file.getParentFile().exists())
                file.getParentFile().mkdirs();

            if (file.createNewFile())
                try (PrintWriter writer = new PrintWriter(file)) {
                    writer.println(gson.toJson(parser.parse(gson.toJson(clazz))));
                    writer.flush();
                }
            else
                return gson.fromJson(new String(Files.readAllBytes(file.toPath())), clazz);
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        return null;
    }

    public static <T> T loadConfig(JavaPlugin plugin, String fileName, Class<T> clazz) {
        return loadConfig(new File(plugin.getDataFolder().getAbsolutePath(), fileName + ".json"), clazz);
    }

    public static <T> T loadConfig(String path, String fileName, Class<T> clazz) {
        return loadConfig(new File(path, fileName + ".json"), clazz);
    }

    public static void saveConfig(File file, Object object) {
        MultiThreading.execute(() -> {
            try {
                final var gson = Utils.gson;
                final var parser = new JsonParser();

                file.createNewFile();

                try (PrintWriter writer = new PrintWriter(file)) {
                    writer.println(gson.toJson(parser.parse(gson.toJson(object))));
                    writer.flush();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static void saveConfig(String path, String fileName, Object object) {
        saveConfig(new File(path, fileName + ".json"), object);
    }

}
 