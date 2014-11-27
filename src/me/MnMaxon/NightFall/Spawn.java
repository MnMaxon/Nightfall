package me.MnMaxon.NightFall;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;

public class Spawn {
	public Block block;
	public String name;

	public Spawn(String name) {
		this.name = name;
		YamlConfiguration data = Config.Load(Main.dataFolder + "/Data");
		block = new Location(Bukkit.getWorld(data.getString(name + ".world")), data.getInt(name + ".x"),
				data.getInt(name + ".y"), data.getInt(name + ".z")).getBlock();
	}

	public Spawn(String name, Location loc) {
		this.name = name;
		block = loc.getBlock();
	}

	@SuppressWarnings("unchecked")
	public void save() {
		YamlConfiguration data = Config.Load(Main.dataFolder + "/Data");
		data.set(name + ".world", block.getWorld().getName());
		data.set(name + ".x", block.getX());
		data.set(name + ".y", block.getY());
		data.set(name + ".z", block.getZ());
		ArrayList<String> names;
		if (data.get("names") != null)
			names = (ArrayList<String>) data.getList("names");
		else
			names = new ArrayList<String>();
		if (!names.contains(name))
			names.add(name);
		data.set("names", names);
		Config.Save(data, Main.dataFolder + "/Data");
	}

	public void spawnMobs() {
		for (int i = 0; i < 5; i++)
			block.getWorld().spawnEntity(block.getLocation(), EntityType.PIG_ZOMBIE);
		for (int i = 0; i < 3; i++)
			block.getWorld().spawnEntity(block.getLocation(), EntityType.BLAZE);
		for (int i = 0; i < 2; i++)
			block.getWorld().spawnEntity(block.getLocation(), EntityType.GHAST);
	}

	public void delete() {
		YamlConfiguration data = Config.Load(Main.dataFolder + "/Data");
		if (data.get(name) == null)
			return;
		data.set(name, null);
		@SuppressWarnings("unchecked")
		ArrayList<String> names = (ArrayList<String>) data.getList("names");
		if (names.contains(name))
			names.remove(name);
		data.set("names", names);
		Config.Save(data, Main.dataFolder + "/Data");
	}

	@SuppressWarnings("unchecked")
	public static ArrayList<String> listSpawns() {
		ArrayList<String> nameList = new ArrayList<String>();
		YamlConfiguration data = Config.Load(Main.dataFolder + "/Data");
		if (data.get("names") != null)
			nameList = (ArrayList<String>) data.getList("names");
		return nameList;
	}

	public static ArrayList<Spawn> getSpawns() {
		ArrayList<Spawn> spawnList = new ArrayList<Spawn>();
		ArrayList<String> nameList = listSpawns();
		if (nameList.size() != 0)
			for (String name : nameList)
				spawnList.add(new Spawn(name));
		return spawnList;
	}
}
