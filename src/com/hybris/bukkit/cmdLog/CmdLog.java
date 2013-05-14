/*
    CmdLog - The Command Logger plugin for CraftBukkit
    Copyright (C) 2013  Hybris95
    hybris_95@hotmail.com

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

import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.event.server.ServerCommandEvent;

public class CmdLog extends JavaPlugin{
	
	FileWriter fW = null;
	private Listener pL = null;
	
	public void onLoad(){}
	
	public void onEnable(){
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
					this.getServer().getLogger().severe("[" + this.getDescription().getName() + "] Critical error : #4");
					this.getPluginLoader().disablePlugin(this);
					return;
				}
				this.getServer().getLogger().info("[" + this.getDescription().getName() + "] Enabled!");
			}
			else{
				this.getServer().getLogger().severe("[" + this.getDescription().getName() + "] Cannot write on CmdLog.log !");
				this.getPluginLoader().disablePlugin(this);
				return;
			}
		}
		catch(FileNotFoundException e){
			this.getServer().getLogger().severe("[" + this.getDescription().getName() + "] Critical error : #3");
			this.getPluginLoader().disablePlugin(this);
			return;
		}
		catch(NullPointerException e){
			this.getServer().getLogger().severe("[" + this.getDescription().getName() + "] Critical error : #1");
			this.getPluginLoader().disablePlugin(this);
			return;
		}
		catch(IOException e){
			this.getServer().getLogger().severe("[" + this.getDescription().getName() + "] Critical error : #2" + System.getProperty("line.separator") + "[" + this.getDescription().getName() + "] Check if CmdLog.log is accessible !");
			this.getPluginLoader().disablePlugin(this);
			return;
		}
		catch(SecurityException e){
			this.getServer().getLogger().severe("[" + this.getDescription().getName() + "] Critical error : check your security settings");
			this.getPluginLoader().disablePlugin(this);
			return;
		}
	}
	
	public void onDisable(){
		try{
			this.fW.close();
		}
		catch(IOException e){
			this.getServer().getLogger().warning("[" + this.getDescription().getName() + "] Could not close CmdLog.log!");
		}
		this.fW = null;
		this.getServer().getLogger().info("[" + this.getDescription().getName() + "] Disabled !");
	}
	
	private class CmdLogListener implements Listener{
						
		private CmdLog plugin = null;
		
		public CmdLogListener(CmdLog plugin){
			super();
			this.plugin = plugin;
		}
		
        @EventHandler(priority = EventPriority.MONITOR)
		public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event){
            Player sender = event.getPlayer();
            this.logIntoFile(sender.getDisplayName(), sender.getAddress().toString(), sender.isOp(), event.getMessage());
		}
        
        @EventHandler(priority = EventPriority.MONITOR)
        public void onCommandProcess(ServerCommandEvent event)
        {
            CommandSender sender = event.getSender();
            this.logIntoFile(sender.getName(), "127.0.0.1", sender.isOp(), event.getCommand());
        }
        
        private void logIntoFile(String senderName, String ipAddress, boolean operator, String commandMessage)
        {
    		if(this.plugin.fW != null)
            {
    			Date currentDate = new Date();
                
                StringBuilder toWrite = new StringBuilder();
                toWrite.append(currentDate.toString());
                
                toWrite.append("[");
                toWrite.append(senderName);
                toWrite.append("]");
                
                toWrite.append("(");
                toWrite.append(ipAddress);
                toWrite.append(")");
                
    			if(operator){
					toWrite.append("Operator:");
				}
				else{
					toWrite.append(":");
				}
    			
                toWrite.append(commandMessage);
				toWrite.append(System.getProperty("line.separator"));
				
				try{
					this.plugin.fW.write(toWrite.toString());
					this.plugin.fW.flush();
				}
				catch(IOException e){
					this.plugin.getServer().getLogger().warning("[" + this.plugin.getDescription().getName() + "] Could not write in CmdLog.log");
				}
    		}
        }
	}
	
}