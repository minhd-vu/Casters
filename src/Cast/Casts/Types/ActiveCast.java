package Cast.Casts.Types;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;

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
