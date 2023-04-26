package dev.zombie.survival.nano.scoreboard;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import dev.zombie.survival.nano.Nano;
import dev.zombie.survival.nano.game.controller.api.GameImpl;
import dev.zombie.survival.nano.scoreboard.api.ScoreboardUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ScoreboardScheduler {

    ScoreboardUtil util;
    GameImpl game;
    ProtocolManager protocolManager;
    public ScoreboardScheduler(GameImpl game, Nano plugin,ProtocolManager protocolManager){
        this.protocolManager = ProtocolLibrary.getProtocolManager();
        this.game = game;
        util = new ScoreboardUtil();
        plugin.getServer().getPluginManager().registerEvents(new ScoreboardEvent(util,plugin,protocolManager),plugin);

        onEnable();
    }

    private void onEnable() {
        new BukkitRunnable() {
            @Override
            public void run() {
                // Send scoreboard to all players
                for (Player p : Bukkit.getOnlinePlayers()) {
                    util.sendScoreboard(p, game.getTimer(),protocolManager);
                }
            }
        }.runTaskTimer(Nano.getProvidingPlugin(Nano.class), 20, 20L);
    }

}
