package Cast.Casts.Actives;

import Cast.Casts.Cast;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
