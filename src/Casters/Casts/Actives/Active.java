package Casters.Casts.Actives;

import Casters.Casts.Cast;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;

public abstract class Active extends Cast
{
	public Active(String name, String description)
	{
		super(name, description);
	}

	public void decast(Player player)
	{
		player.sendMessage(header + " " + ChatColor.WHITE + name + ChatColor.GRAY + " Is No Longer Active!");
	}
}
