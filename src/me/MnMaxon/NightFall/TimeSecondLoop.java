package me.MnMaxon.NightFall;

import org.bukkit.Bukkit;

public class TimeSecondLoop implements Runnable {

	@Override
	public void run() {
		Bukkit.getPluginManager().callEvent(new TimeSecondEvent());
	}

}