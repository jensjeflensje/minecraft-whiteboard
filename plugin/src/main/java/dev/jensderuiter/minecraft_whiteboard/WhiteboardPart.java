package dev.jensderuiter.minecraft_whiteboard;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.entity.ItemFrame;
import org.bukkit.map.MapView;

@AllArgsConstructor
@Getter
public class WhiteboardPart {

    private MapView view;
    private ItemFrame itemFrame;

}
