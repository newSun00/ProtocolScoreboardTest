package dev.zombie.survival.nano.scoreboard.api;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.Objects;

public class ScoreboardUtil {
    Scoreboard scoreboard;
    Objective objective;
    ProtocolManager protocolManager;

    public void sendScoreboard(Player player, int count, ProtocolManager protocolManager) {
        this.protocolManager = protocolManager;
        // Scoreboard, Objective 생성 및 DisplaySlot 설정
        scoreboard = Objects.requireNonNull(Bukkit.getScoreboardManager()).getNewScoreboard();
        objective = scoreboard.registerNewObjective("sidebar", "dummy");
        objective.setDisplayName("sidebar");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        // Scoreboard Objective Packet 전송
        PacketContainer objectivePacket = protocolManager.createPacket(PacketType.Play.Server.SCOREBOARD_OBJECTIVE);
        objectivePacket.getIntegers().write(0, 0); // 0: create, 1: remove, 2: update
        objectivePacket.getModifier().write(0, "sidebar"); // Objective Name: unique name for the objective to be displayed
        objectivePacket.getStrings().write(0, "dummy"); // Objective Criteria: criteria of the objective, always "dummy" for sidebar objectives
        protocolManager.sendServerPacket(player, objectivePacket);


        // Scoreboard Display Objective Packet 전송
        PacketContainer displayPacket = protocolManager.createPacket(PacketType.Play.Server.SCOREBOARD_DISPLAY_OBJECTIVE);
        displayPacket.getIntegers().write(0, 1); // Position: 0: list, 1: sidebar, 2: below name, 3 - 18: team specific sidebar, indexed as 3 + team color
        displayPacket.getStrings().write(0, "sidebar"); // Score Name: unique name for the scoreboard to be displayed
        protocolManager.sendServerPacket(player, displayPacket);

        // Score 추가 PacketContainer 생성
        PacketContainer scorePacket = protocolManager.createPacket(PacketType.Play.Server.SCOREBOARD_SCORE);
        // Score 1: 타이머 값
        scorePacket.getScoreboardActions().write(0, EnumWrappers.ScoreboardAction.CHANGE); // Scoreboard action
        scorePacket.getStrings().write(0, "타이머 : " + count); // Score display name
        scorePacket.getIntegers().write(0, 1); // Score value
        protocolManager.sendServerPacket(player, scorePacket);

        // Score 2: 현재 날짜
        scorePacket.getScoreboardActions().write(0, EnumWrappers.ScoreboardAction.CHANGE); // Scoreboard action
        scorePacket.getStrings().write(0, "닉네임 : " + player.getName()); // Score display name
        scorePacket.getIntegers().write(0, 2); // Score value
        protocolManager.sendServerPacket(player, scorePacket);
    }



    public void removeScoreboard(Player player) {
        PacketContainer packet = protocolManager.createPacket(PacketType.Play.Server.SCOREBOARD_OBJECTIVE);
        packet.getStrings().write(0, objective.getName());
        packet.getModifier().write(0, DisplaySlot.SIDEBAR);
        packet.getModifier().write(0, EnumWrappers.ScoreboardAction.REMOVE); // 수정된 코드
        protocolManager.sendServerPacket(player, packet);
    }
}
