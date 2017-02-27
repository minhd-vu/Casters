package Cast.Casts.Types;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import Cast.Essentials.Chat.Pages;
import Cast.Essentials.Schedulers.Cooldown;
import Cast.Essentials.Schedulers.WarmUp;

public class Cast
{
	protected String name;
	protected String description;
	protected Pages pages;
	protected List<String> info;

	protected int level;

	protected WarmUp warmup;
	protected Cooldown cooldown;

	protected double manacost;

	protected String header = ChatColor.DARK_GRAY + "[" + ChatColor.DARK_AQUA + "Cast" + ChatColor.DARK_GRAY + "]";
	protected String fill = "------------------------";

	public Cast(String name, String description)
	{
		this.name = name;
		this.description = description;

		pages = new Pages();
		info = new ArrayList<String>();
		warmup = new WarmUp();
		cooldown = new Cooldown();

		info.add(ChatColor.DARK_AQUA + this.name + ChatColor.GRAY + " - " + this.description);

		pages.setHeader(ChatColor.DARK_GRAY + fill + header + fill);
		pages.setCommand("cast " + name + " info");
		pages.setError("Cast " + name);
	}

	public String getName()
	{
		return name;
	}

	public String getDescription()
	{
		return description;
	}

	public List<String> getInfo()
	{
		return info;
	}

	public Cooldown getCooldown()
	{
		return cooldown;
	}

	public void cast(Player player)
	{
		List<Entity> e = player.getNearbyEntities(16, 16, 16);

		for (Entity reciever : e)
		{
			if (reciever instanceof Player)
			{
				reciever.sendMessage(header + " " + ChatColor.WHITE + player.getName() + ChatColor.GRAY + " Casts "
						+ ChatColor.WHITE + name + ChatColor.GRAY + "!");
			}
		}

		player.sendMessage(header + " " + ChatColor.WHITE + "You" + ChatColor.GRAY + " Cast " + ChatColor.WHITE + name
				+ ChatColor.GRAY + "!");
	}

	public void cast(Player player, LivingEntity target)
	{
		List<Entity> e = player.getNearbyEntities(16, 16, 16);

		for (Entity reciever : e)
		{
			if (reciever instanceof Player)
			{
				reciever.sendMessage(header + " " + ChatColor.WHITE + player.getName() + ChatColor.GRAY + " Casts "
						+ ChatColor.WHITE + name + ChatColor.GRAY + " On " + ChatColor.WHITE + target.getName()
						+ ChatColor.GRAY + "!");
			}
		}

		if (player.equals(target))
		{
			player.sendMessage(header + " " + ChatColor.WHITE + "You" + ChatColor.GRAY + " Cast " + ChatColor.WHITE
					+ name + ChatColor.GRAY + " On " + ChatColor.WHITE + "Yourself!");
		}

		else
		{
			player.sendMessage(header + " " + ChatColor.WHITE + "You" + ChatColor.GRAY + " Cast " + ChatColor.WHITE
					+ name + ChatColor.GRAY + " On " + ChatColor.WHITE + target.getName() + ChatColor.GRAY + "!");
		}
	}
}
