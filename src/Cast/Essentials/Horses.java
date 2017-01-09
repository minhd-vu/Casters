package Cast.Essentials;

import org.bukkit.ChatColor;
import org.bukkit.entity.Horse;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import Cast.Main;

public class Horses implements Listener
{
	private String header = ChatColor.DARK_GRAY + "[" + ChatColor.DARK_AQUA + "Casters" + ChatColor.DARK_GRAY + "] ";

	@EventHandler
	public void onPlayerInteractEntityEvent(PlayerInteractEntityEvent event)
	{
		if (event.getRightClicked() instanceof Horse)
		{
			Caster caster = Main.getCasters().get(event.getPlayer().getUniqueId());

			if (caster.getType() != "Cavalier")
			{
				caster.getPlayer().sendMessage(header + ChatColor.GRAY + "You Must Be A " + ChatColor.WHITE + "Cavalier"
						+ ChatColor.GRAY + " To Ride A Horse!");

				event.setCancelled(true);
			}
		}
	}
}
