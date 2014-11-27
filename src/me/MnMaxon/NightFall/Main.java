package me.MnMaxon.NightFall;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin implements Listener {
	public static String dataFolder;
	public static Main plugin;
	public static int timeSecondLoop = 0;

	@Override
	public void onEnable() {
		plugin = this;
		dataFolder = this.getDataFolder().getAbsolutePath();

		YamlConfiguration cfg = Config.Load(dataFolder + "/Config.yml");
		Config.Load(dataFolder + "/Data");

		if (cfg.get("world") == null)
			cfg.set("world", Bukkit.getWorlds().get(0).getName());
		if (cfg.get("Time") == null)
			cfg.set("Time", 15000);

		getServer().getPluginManager().registerEvents(this, this);
		timeSecondLoop = getServer().getScheduler().scheduleSyncRepeatingTask(this, new TimeSecondLoop(), 20l, 20l);

		Config.Save(cfg, dataFolder + "/Config.yml");
	}

	@Override
	public void onDisable() {
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			return false;
		}
		Player p = (Player) sender;
		if (!p.isOp()) {
			p.sendMessage("You need to be an op to do this");
			return false;
		}
		if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
			displayHelp(p);
		} else if (args[0].equalsIgnoreCase("set")) {
			if (args.length != 2)
				displayHelp(p);
			else {
				new Spawn(args[1], p.getLocation()).save();
				p.sendMessage(ChatColor.DARK_GREEN + "Spawn point successfully set");
			}
		} else if (args[0].equalsIgnoreCase("del") || args[0].equalsIgnoreCase("delete")) {
			if (args.length != 2)
				displayHelp(p);
			else {
				new Spawn(args[1]).delete();
				p.sendMessage(ChatColor.DARK_GREEN + "Spawn point successfully deleted");
			}
		} else if (args.length == 1 && args[0].equals("list")) {
			ArrayList<String> names = Spawn.listSpawns();
			String message = ChatColor.DARK_PURPLE + "Spawns (" + names.size() + "): ";
			boolean first = true;
			for (String name : names) {
				if (!first)
					message = message + ", ";
				else
					first = false;
				message = message + name;
			}
			p.sendMessage(message);
		} else
			displayHelp(p);
		return false;
	}

	private void displayHelp(Player p) {
		p.sendMessage(ChatColor.AQUA + "/nightfall set <name> - Sets a mob spawn");
		p.sendMessage(ChatColor.AQUA + "/nightfall del <name> - Deletes a mob spawn");
		p.sendMessage(ChatColor.AQUA + "/nightfall list - Lists all mob spawns");

	}

	@EventHandler
	public void onRandomTick(TimeSecondEvent event) {
		YamlConfiguration config = Config.Load(dataFolder + "/Config.yml");
		if (config.get("Time") == null)
			config.set("Time", 15000);
		if (config.get("world") == null)
			config.set("world", Bukkit.getWorlds().get(0).getName());
		Config.Save(config, dataFolder + "/Config.yml");
		long time = Bukkit.getWorld(config.getString("world")).getTime();
		if (time > config.getInt("Time") - 10 && time <= config.getInt("Time") + 10) {
			Bukkit.broadcastMessage(ChatColor.DARK_RED + "Night has fallen, and the monsters come out...");
			for (Spawn spawn : Spawn.getSpawns())
				spawn.spawnMobs();
		}
	}
}