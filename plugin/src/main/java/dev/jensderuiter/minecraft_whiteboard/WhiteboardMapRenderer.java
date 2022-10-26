package dev.jensderuiter.minecraft_whiteboard;

import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import java.awt.image.BufferedImage;

public class WhiteboardMapRenderer extends MapRenderer {

    private boolean done = false;
    private BufferedImage image;


    public void load(BufferedImage image) {
        this.image = image;
    }


    @Override
    public void render(MapView view, MapCanvas canvas, Player player) {
        if(done) return;

        canvas.drawImage(0,0, image);

        view.setTrackingPosition(false);
        done = true;
    }
}
