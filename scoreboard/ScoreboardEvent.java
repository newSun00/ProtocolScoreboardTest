package dev.zombie.survival.nano.scoreboard;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.*;
import dev.zombie.survival.nano.scoreboard.api.ScoreboardUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class ScoreboardEvent implements Listener {
    ScoreboardUtil util;
    JavaPlugin plugins;
    ProtocolManager protocolManager;
    PacketListener packetListener;
    public ScoreboardEvent(ScoreboardUtil util, JavaPlugin plugin, ProtocolManager protocolManager) {
        this.util = util;
        this.plugins =plugin;
        this.protocolManager = protocolManager;
        this.packetListener = new PacketListener() {
            @Override
            public void onPacketSending(PacketEvent packetEvent) {
            }
            @Override
            public void onPacketReceiving(PacketEvent packetEvent) {
                System.out.println(" 패킷 : "+packetEvent.getPacket());
            }

            @Override
            public ListeningWhitelist getSendingWhitelist() {
                return null;
            }

            @Override
            public ListeningWhitelist getReceivingWhitelist() {
                return null;
            }

            @Override
            public Plugin getPlugin() {
                return null;
            }
        };
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        Player p = e.getPlayer();
        util.removeScoreboard(p);
    }
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        System.out.println(" 이벤트 실행 ");
        protocolManager.addPacketListener(packetListener);
    }
}
