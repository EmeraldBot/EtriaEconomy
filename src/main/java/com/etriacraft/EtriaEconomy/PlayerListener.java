package com.etriacraft.EtriaEconomy;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerListener implements Listener {
	
	EtriaEconomy plugin;
	public PlayerListener(EtriaEconomy plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		if (!plugin.getAPI().hasAccount(e.getPlayer().getName())) {
			plugin.getAPI().createPlayerAccount(e.getPlayer().getName());
			EtriaEconomy.log.info("Created an account for: " + e.getPlayer().getName());
		}
		if (plugin.getAPI().hasAccount(e.getPlayer().getName())) {
			ResultSet rs2 = DBConnection.sql.readQuery("SELECT * FROM econ_players WHERE player = '" + e.getPlayer().getName() + "';");
			try {
				if (rs2.next()) {
					if (rs2.getString("uuid") == null || rs2.getString("uuid") == "") {
						DBConnection.sql.modifyQuery("UPDATE econ_players SET uuid = '" + e.getPlayer().getUniqueId().toString() + "' WHERE player = '" + e.getPlayer().getName() + "';");
					}
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
		ResultSet rs2 = DBConnection.sql.readQuery("SELECT * FROM econ_players WHERE uuid = '" + e.getPlayer().getUniqueId().toString() + "';");
		try {
			if (rs2.next()) {
				// There was a UUID entry
				if (!rs2.getString("player").equals(e.getPlayer().getUniqueId().toString())) {
					// The player has changed their username ;o
					DBConnection.sql.modifyQuery("UPDATE econ_players SET player = '" + e.getPlayer().getName() + "' WHERE uuid = '" + rs2.getString("uuid") + "';");
				}
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

}
