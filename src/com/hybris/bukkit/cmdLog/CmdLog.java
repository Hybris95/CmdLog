/*
    CmdLog - The Command Logger plugin for CraftBukkit
    Copyright (C) 2013  Hybris95

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package com.hybris.bukkit.cmdLog;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.Listener;
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
	private Listener pL = null;
	
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
                    this.getServer().getPluginManager().registerEvents(this.pL, this);
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
	
	private class CmdLogListener implements Listener{
						
		private CmdLog plugin = null;
		
		public CmdLogListener(CmdLog plugin){
			super();
			this.plugin = plugin;
		}
		
        @EventHandler(priority = EventPriority.MONITOR)
		public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event){
				
			if(this.plugin.fW != null){
				
				Date currentDate = new Date();
                
                StringBuilder toWrite = new StringBuilder();
                toWrite.append(currentDate.toString());
				
				Player sender = event.getPlayer();
                toWrite.append("[");
                toWrite.append(sender.getDisplayName());
                toWrite.append("]");
                
                toWrite.append("(");
                toWrite.append(sender.getAddress().toString());
                toWrite.append(")");
                
				if(sender.isOp()){
					toWrite.append("Operator:");
				}
				else{
					toWrite.append(":");
				}
				
                toWrite.append(event.getMessage());			
				toWrite.append(System.getProperty("line.separator"));
				
				try{
					this.plugin.fW.write(toWrite.toString());
					this.plugin.fW.flush();
				}
				catch(IOException e){
					this.plugin.getServer().getLogger().warning("[CmdLog] Could not write in CmdLog.log");
				}
				
			}
		}
	}
	
}