package me.windrof123.blockregen;

import java.util.List;

import net.coreprotect.CoreProtect;
import net.coreprotect.CoreProtectAPI;
import net.coreprotect.CoreProtectAPI.ParseResult;

import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.Location;

public class Main extends JavaPlugin implements Listener {
	
	private CoreProtectAPI getCoreProtect() {
		Plugin plugin = getServer().getPluginManager().getPlugin("CoreProtect");
		     
		// Check that CoreProtect is loaded
		if (plugin == null || !(plugin instanceof CoreProtect)) {
		    return null;
		}
		        
		// Check that the API is enabled
		CoreProtectAPI CoreProtect = ((CoreProtect)plugin).getAPI();
		if (CoreProtect.isEnabled()==false){
		    return null;
		}

		// Check that a compatible version of the API is loaded
		if (CoreProtect.APIVersion() < 2){
		    return null;
		}
		         
		return CoreProtect;
	}

	@Override
	public void onEnable(){
		getServer().getPluginManager().registerEvents(this, this);
		CoreProtectAPI CoreProtect = getCoreProtect();
		if(CoreProtect.isEnabled() == true){
		CoreProtect.testAPI();
		getLogger().info("BlockRegen has been successfully enabled!");
	}else{
		getLogger().warning("CoreProtect API doesn't seem to be enabled! Please enable it in the config.");
	}
}
	
	int ActionId;
	long TaskTime = 100;
	Location bLocation;
	
	@EventHandler
	public void BlockBreakEvent(BlockBreakEvent e){
		Block Block = e.getBlock();
		e.getPlayer().toString();
		bLocation = e.getBlock().getLocation();
		CoreProtectAPI CoreProtect = getCoreProtect();
		List<String[]> lookup = CoreProtect.blockLookup(Block, 0);
		for(String[] value : lookup){
			ParseResult result = CoreProtect.parseResult(value);
			ActionId = result.getActionId();
		}
		
		//boolean hasPlaced = CoreProtect.hasPlaced(ePlayerString, Block, 500, 1); //This will return true if a user has already removed a block at the location within the specified time limit. 
		//getLogger().info(String.valueOf(hasPlaced));
		getLogger().info(String.valueOf(ActionId));
		if(ActionId == 1){
			new BukkitRunnable(){
				@Override
				public void run(){
					performRestore();
					}	
				}.runTaskLater(this, TaskTime);
		}else{
			new BukkitRunnable(){
				@Override
				public void run(){
					performRestore();
					}	
				}.runTaskLater(this, TaskTime);
		}
	}
	public void performRestore(){
		CoreProtectAPI CoreProtect = getCoreProtect();
		CoreProtect.performRestore(null, 0, 1, bLocation, null, null); //This will perform a restore.
	}
}
