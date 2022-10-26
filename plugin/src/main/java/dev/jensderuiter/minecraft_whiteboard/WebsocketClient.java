package dev.jensderuiter.minecraft_whiteboard;

import com.google.gson.Gson;
import dev.jensderuiter.minecraft_whiteboard.json_model.DrawMessage;

import javax.websocket.*;
import java.net.URI;
import java.net.http.WebSocket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletionStage;

public class WebsocketClient implements WebSocket.Listener {
    Gson gson = new Gson();

    public void onOpen(WebSocket webSocket) {
        System.out.println("Websocket connected.");
        WebSocket.Listener.super.onOpen(webSocket);
    }

    @Override
    public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
        System.out.println("Received message: " + data);
        DrawMessage drawMessage = gson.fromJson(data.toString(), DrawMessage.class);
        MinecraftWhiteboard.whiteboards.forEach(whiteboard -> {
            whiteboard.draw(drawMessage);
        });
        return WebSocket.Listener.super.onText(webSocket, data, last);
    }

    @Override
    public CompletionStage<?> onBinary(WebSocket webSocket, ByteBuffer data, boolean last) {
        String stringData = StandardCharsets.UTF_8.decode(data).toString();;
        System.out.println("Received binary message: " + stringData);
        /*DrawMessage drawMessage = gson.fromJson(stringData, DrawMessage.class);
        MinecraftWhiteboard.whiteboards.forEach(whiteboard -> {
            whiteboard.draw(drawMessage);
        });*/
        return WebSocket.Listener.super.onBinary(webSocket, data, last);
    }

    public void onError(WebSocket webSocket, Throwable error) {
        error.printStackTrace();
        MinecraftWhiteboard.createWebsocket();
    }

}
