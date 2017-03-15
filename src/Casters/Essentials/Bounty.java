package Casters.Essentials;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class Bounty implements Listener
{
	@EventHandler
	public void onPlayerDeathEvent(PlayerDeathEvent event)
	{
		if (event.getEntity().getKiller() instanceof Player)
		{
			ItemStack skull = new ItemStack(Material.SKULL_ITEM);
			SkullMeta skullmeta = (SkullMeta) skull.getItemMeta();
			skullmeta.setOwner(event.getEntity().getName());
			skull.setItemMeta(skullmeta);

			event.getEntity().getWorld().dropItemNaturally(event.getEntity().getLocation(), skull);
		}
	}
}
