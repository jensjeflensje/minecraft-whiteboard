package dev.jensderuiter.minecraft_whiteboard;

import dev.jensderuiter.minecraft_whiteboard.command.SpawnCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.util.ArrayList;
import java.util.List;

public final class MinecraftWhiteboard extends JavaPlugin {

    public static List<Whiteboard> whiteboards = new ArrayList<>();

    public static WebSocket websocket;
    @Override
    public void onEnable() {
        getCommand("spawnwhiteboard").setExecutor(new SpawnCommand());
        try {
            createWebsocket();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        try {
            websocket.abort();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void createWebsocket() {
        websocket = HttpClient
                .newHttpClient()
                .newWebSocketBuilder()
                .buildAsync(URI.create("ws://localhost:3000"), new WebsocketClient())
                .join();
    }
}
