package dev.jensderuiter.minecraft_whiteboard.command;

import dev.jensderuiter.minecraft_whiteboard.MinecraftWhiteboard;
import dev.jensderuiter.minecraft_whiteboard.Whiteboard;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if (!(sender instanceof Player)) return true;
        Player player = (Player) sender;

        MinecraftWhiteboard.whiteboards.add(new Whiteboard(player.getLocation()));

        player.sendMessage("Spawned a new whiteboard.");

        return true;
    }
}
