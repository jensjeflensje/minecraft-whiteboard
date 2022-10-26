package dev.jensderuiter.minecraft_whiteboard;

import dev.jensderuiter.minecraft_whiteboard.json_model.DrawMessage;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.bukkit.scheduler.BukkitRunnable;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Whiteboard {

    private int FRAMES_WIDTH = 8;
    private int WIDTH = 128 * FRAMES_WIDTH;
    private int FRAMES_HEIGHT = 5;
    private int HEIGHT = 128 * FRAMES_HEIGHT;

    public Location location;
    BufferedImage image;
    Graphics2D g2d;
    long lastRefresh;
    boolean refreshPlanned = false;
    List<WhiteboardPart> parts = new ArrayList<>();

    public Whiteboard(Location location) {
        this.location = location;

        // initalize the map

        for (int y = 0; y < FRAMES_HEIGHT; y++) {
            for (int x = 0; x < FRAMES_WIDTH; x++) {
                this.parts.add(this.addPart(location.clone().add(x, y, 0)));
            }
        }

        Collections.reverse(this.parts);

        this.image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        g2d = image.createGraphics();
        g2d.setPaint(Color.decode("#FFFFFF"));
        g2d.fillRect(0, 0, image.getWidth(), image.getHeight());
        this.refresh();
    }

    public WhiteboardPart addPart(Location location) {
        ItemFrame itemFrame = null;
        for (Entity entity : location.getChunk().getEntities()) {
            if (entity instanceof ItemFrame && locBlockSame(entity.getLocation(),location)) itemFrame = (ItemFrame) entity;
        }
        if (itemFrame == null) {
            itemFrame = location.getWorld().spawn(location, ItemFrame.class);
        }
        MapView view = Bukkit.createMap(location.getWorld());
        view.getRenderers().clear();
        return new WhiteboardPart(view, itemFrame);
    }

    public void refresh() {
        this.lastRefresh = System.currentTimeMillis();
        int widthCount = 0;
        int heightCount = 0;
        for (WhiteboardPart part : this.parts) {
            if (widthCount == FRAMES_WIDTH) {
                heightCount++;
                widthCount = 0;
            }
            BufferedImage croppedImage = this.image.getSubimage(widthCount * 128, heightCount * 128,
                    128, 128);
            WhiteboardMapRenderer renderer = new WhiteboardMapRenderer();
            renderer.load(croppedImage);
            part.getView().addRenderer(renderer);
            ItemStack map = new ItemStack(Material.FILLED_MAP);
            MapMeta meta = (MapMeta) map.getItemMeta();
            meta.setMapView(part.getView());
            map.setItemMeta(meta);
            part.getItemFrame().setItem(map);
            widthCount++;
        }
        System.out.println("Map refreshed");
    }

    public void draw(DrawMessage drawMessage) {
        BasicStroke bs = new BasicStroke(drawMessage.strokeWidth);
        g2d.setStroke(bs);
        g2d.setPaint(Color.decode(drawMessage.color));
        g2d.drawLine(drawMessage.x, drawMessage.y, drawMessage.px, drawMessage.py);
        if (this.lastRefresh + 1000 < System.currentTimeMillis()) {
            this.refresh();
        } else if (!this.refreshPlanned) {
            this.refreshPlanned = true;
            new BukkitRunnable() {
                @Override
                public void run() {
                    refresh();
                    refreshPlanned = false;
                }
            }.runTaskLater(MinecraftWhiteboard.getPlugin(MinecraftWhiteboard.class),
                    (long) Math.floor((this.lastRefresh + 1000 - System.currentTimeMillis()) / 50));
        }
    }

    private boolean locBlockSame (Location l1, Location l2) {
        return l1.getBlockX() == l2.getBlockX() &&
                l1.getBlockY() == l2.getBlockY() &&
                l1.getBlockZ() == l2.getBlockZ();
    }
}
