package com.cloudcraft.mysqlbackup;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Main extends JavaPlugin implements Listener{

	public static List<String> tabelas;
	public boolean todas;
	public String horario;
	
	public void onEnable() {
		saveDefaultConfig();
		getCommand("dobackupmysql").setExecutor(new Comando());
		MySQL.open();
		todas = getConfig().getBoolean("TodasTabelas");
		if (!todas) tabelas = getConfig().getStringList("Lista");
		else {
			tabelas = Methods.allTables();
		}
		System.out.println("Tabelas a serem salvas "+tabelas);
		   File userfiles;
	        try {
	            userfiles = new File(getDataFolder() + File.separator + "backup");
	            if(!userfiles.exists()){
	                userfiles.mkdirs();
	            }
	        } catch(SecurityException e) {
	            userfiles = null;
	        }
	        if (getConfig().getBoolean("SalvarAutomatico.Ativar")) {
	        	horario = getConfig().getString("SalvarAutomatico.Horario");
	        	SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
	        	new BukkitRunnable() {
					
					@Override
					public void run() {
						Date now = new Date();
						if (sdf.format(now).equalsIgnoreCase(horario)) {
							Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "dobackupmysql");
						}
					}
				}.runTaskTimerAsynchronously(this, 20l, 20);
	        }
		}
	
	public void onDisable() {
	MySQL.close();
	}	
	
}
