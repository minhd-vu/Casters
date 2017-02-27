package Cast.Casts.Types;

import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class ActiveCast extends Cast
{
	public ActiveCast(String name, String description)
	{
		super(name, description);
	}

	public void decast(Player player)
	{
		player.sendMessage(header + ChatColor.WHITE + name + ChatColor.GRAY + " Is No Longer Active!");
	}
}
