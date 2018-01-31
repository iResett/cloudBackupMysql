package com.cloudcraft.mysqlbackup;


import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;


public class MySQL {

	protected static Connection con = null;
	public static ConsoleCommandSender sc = Bukkit.getConsoleSender();
	public static File f;
    public static String prefix = "§6[cloudBackup] ";
    public static String user, password, table, db;
	
	public static void open(){
		db = Main.getPlugin(Main.class).getConfig().getString("Mysql.DB");
			String url = "jdbc:mysql://"+Main.getPlugin(Main.class).getConfig().getString("Mysql.IP")+":"+Main.getPlugin(Main.class).getConfig().getString("Mysql.Porta")+"/"+db+"?autoreconnect=true";
			String user = ""+Main.getPlugin(Main.class).getConfig().getString("Mysql.Usuario");
			String password = ""+Main.getPlugin(Main.class).getConfig().getString("Mysql.Senha");
			MySQL.user = user;
			MySQL.password = password;
			table = Main.getPlugin(Main.class).getConfig().getString("Mysql.DB");
					sc.sendMessage(url);
					try {
						con = DriverManager.getConnection(url, user, password);
						sc.sendMessage(prefix + "§aConexao com MySQL aberta!");
					} catch (Exception e) {
						sc.sendMessage(prefix + "§cConexao com MySQL nao foi possivel!");
						Main.getPlugin(Main.class).getPluginLoader().disablePlugin(Main.getPlugin(Main.class));
						}
		
	}
	
	public static boolean isOpen() {
		return con != null;
	}
	
	public static void close(){
		if (con != null){
			try {
				con.close();
				sc.sendMessage(prefix + "§aConexao fechada com sucesso!");
			} catch (SQLException e) {
				sc.sendMessage(prefix + "§cNao foi possivel fechar a conexao!");
			}
		}
	}
	
}