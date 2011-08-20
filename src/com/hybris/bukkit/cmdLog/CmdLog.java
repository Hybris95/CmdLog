package com.hybris.bukkit.cmdLog;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import java.io.*;
import org.bukkit.command.CommandSender;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import org.bukkit.event.player.*;
import org.bukkit.event.*;

import java.util.Date;

public class CmdLog extends JavaPlugin{
	
	FileWriter fW = null;
	private PlayerListener pL = null;
	
	public void onLoad(){
		this.getServer().getLogger().info("[CmdLog] Loading...");
	}
	
	public void onEnable(){
		this.getServer().getLogger().info("[CmdLog] Enabling...");
		try{
			File logFile = new File("CmdLog.log");
			logFile.createNewFile();
			if(logFile.canWrite()){
				this.fW = new FileWriter(logFile, true);
				if(this.fW != null){
					this.pL = new CmdLogListener(this);
					this.getServer().getPluginManager().registerEvent(Event.Type.PLAYER_COMMAND_PREPROCESS, this.pL, Event.Priority.Monitor, this);
				}
				else{
					this.getServer().getLogger().severe("[CmdLog] Critical error : #4");
					this.getPluginLoader().disablePlugin(this);
					return;
				}
				this.getServer().getLogger().info("[CmdLog] Enabled!");
			}
			else{
				this.getServer().getLogger().severe("[CmdLog] Cannot write on CmdLog.log !");
				this.getPluginLoader().disablePlugin(this);
				return;
			}
		}
		catch(FileNotFoundException e){
			this.getServer().getLogger().severe("[CmdLog] Critical error : #3");
			this.getPluginLoader().disablePlugin(this);
			return;
		}
		catch(NullPointerException e){
			this.getServer().getLogger().severe("[CmdLog] Critical error : #1");
			this.getPluginLoader().disablePlugin(this);
			return;
		}
		catch(IOException e){
			this.getServer().getLogger().severe("[CmdLog] Critical error : #2\n[CmdLog] Check if CmdLog.log is accessible !");
			this.getPluginLoader().disablePlugin(this);
			return;
		}
		catch(SecurityException e){
			this.getServer().getLogger().severe("[CmdLog] Critical error : check your security settings");
			this.getPluginLoader().disablePlugin(this);
			return;
		}
	}
	
	public void onDisable(){
		this.getServer().getLogger().info("[CmdLog] Disabling...");
		try{
			this.fW.close();
		}
		catch(IOException e){
			this.getServer().getLogger().warning("[CmdLog] Could not close CmdLog.log!");
		}
		this.fW = null;
		this.getServer().getLogger().info("[CmdLog] Disabled !");
	}
	
	/*public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
		if(this.fW != null){
			
			Date currentDate = new Date();
			String toWrite = currentDate.toString();
			
			if(sender instanceof Player){
				toWrite += "["+((Player)sender).getDisplayName()+"]";
				toWrite +="("+((Player)sender).getAddress().toString()+")";
			}
			
			if(sender.isOp()){
				toWrite += "Operator:";
			}
			else{
				toWrite += ":";
			}
			
			toWrite += command.getName();
			
			for(int i = 0; i < args.length; i++){
				String arg = args[i];
				toWrite += " "+arg;
			}
			
			toWrite += '\n';
			
			try{
				this.fW.write(toWrite);
			}
			catch(IOException e){
				this.getServer().getLogger().warning("[CmdLog] Could not write in CmdLog.log");
			}
			
		}
		return true;
	}*/
	
	private class CmdLogListener extends PlayerListener{
						
		private CmdLog plugin = null;
		
		public CmdLogListener(CmdLog plugin){
			super();
			this.plugin = plugin;
		}
		
		public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event){
				
			/*if(event.isCancelled()){
				return;
			}*/
				
			if(this.plugin.fW != null){
				
				Date currentDate = new Date();
				String toWrite = currentDate.toString();
				
				Player sender = event.getPlayer();
				//if(sender instanceof Player){
					toWrite += "["+/*((Player)*/sender/*)*/.getDisplayName()+"]";
					toWrite +="("+/*((Player)*/sender/*)*/.getAddress().toString()+")";
				//}
				if(sender.isOp()){
					toWrite += "Operator:";
				}
				else{
					toWrite += ":";
				}
				
				toWrite += event.getMessage();
				/*
				toWrite += command.getName();
				
				for(int i = 0; i < args.length; i++){
					String arg = args[i];
					toWrite += " "+arg;
				}
				*/
				
				toWrite += '\n';
				
				try{
					this.plugin.fW.write(toWrite);
					this.plugin.fW.flush();
				}
				catch(IOException e){
					this.plugin.getServer().getLogger().warning("[CmdLog] Could not write in CmdLog.log");
				}
				
			}
		}
	}
	
}