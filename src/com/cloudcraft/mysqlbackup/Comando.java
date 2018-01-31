package com.cloudcraft.mysqlbackup;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Comando implements CommandExecutor{

	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] arg3) {
		if (cmd.getName().equalsIgnoreCase("dobackupmysql")){
			if (sender.hasPermission("cloudcraft.mysqlbackup")) {
				
				SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH.mm.ss");
				String s = df.format(new Date(System.currentTimeMillis()));
				File a = new File(Main.getPlugin(Main.class).getDataFolder() + "/backup/"+s);
				File b = new File(Main.getPlugin(Main.class).getDataFolder() + "/backup/"+s+".zip");
				a.mkdirs();
				for(String dbs : Main.tabelas){
						File f = new File(Main.getPlugin(Main.class).getDataFolder() + "/backup/"+s, dbs+".csv");
						try {
							Methods.outfile(sender,f.getAbsolutePath(), dbs);
							sender.sendMessage("§aTabela " +dbs+ " salva com sucesso");
						} catch (Exception e) {
							sender.sendMessage("§cTabela " +dbs+ " não foi salva com sucesso");
							e.printStackTrace();
						}
				}
				try {
					zipFolder(Paths.get(a.getPath()), Paths.get(b.getPath()));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					deleteDirectory(a.getPath());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return false;
	}

	public static void deleteDirectory(String directoryFilePath) throws IOException
	{
	    Path directory = Paths.get(directoryFilePath);

	    if (Files.exists(directory))
	    {
	        Files.walkFileTree(directory, new SimpleFileVisitor<Path>()
	        {
	            @Override
	            public FileVisitResult visitFile(Path path, BasicFileAttributes basicFileAttributes) throws IOException
	            {
	                Files.delete(path);
	                return FileVisitResult.CONTINUE;
	            }

	            @Override
	            public FileVisitResult postVisitDirectory(Path directory, IOException ioException) throws IOException
	            {
	                Files.delete(directory);
	                return FileVisitResult.CONTINUE;
	            }
	        });
	    }
	}
	
	private void zipFolder(Path sourceFolderPath, Path zipPath) throws Exception {
		        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipPath.toFile()));
		        Files.walkFileTree(sourceFolderPath, new SimpleFileVisitor<Path>() {
		            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
		                zos.putNextEntry(new ZipEntry(sourceFolderPath.relativize(file).toString()));
		                Files.copy(file, zos);
		                zos.closeEntry();
		                return FileVisitResult.CONTINUE;
		            }
		        });
		        zos.close();
		    }

	
}
