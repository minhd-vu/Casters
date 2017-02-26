package Cast.Casts.Passives;

import org.bukkit.ChatColor;
import org.bukkit.entity.Horse;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import Cast.Main;
import Cast.Casts.Types.Passive;
import Cast.Essentials.Caster;

public class PassiveMount extends Passive implements Listener
{
	public PassiveMount(String name)
	{
		super(name);
	}

	private String header = ChatColor.DARK_GRAY + "[" + ChatColor.DARK_AQUA + "Casters" + ChatColor.DARK_GRAY + "]";

	@EventHandler
	public void onPlayerInteractEntityEvent(PlayerInteractEntityEvent event)
	{
		if (event.getRightClicked() instanceof Horse)
		{
			Caster caster = Main.getCasters().get(event.getPlayer().getUniqueId());

			if (caster.getCasts().containsKey(name))
			{
				caster.getPlayer().sendMessage(header + ChatColor.GRAY + " You Must Be Have The Cast " + ChatColor.WHITE
						+ name + ChatColor.GRAY + " To Ride A Horse!");

				event.setCancelled(true);
			}
		}
	}
}