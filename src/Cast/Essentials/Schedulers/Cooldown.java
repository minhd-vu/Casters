package Cast.Essentials.Schedulers;

import java.text.DecimalFormat;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import Cast.Configs.Config;

public class Cooldown
{
	private HashMap<String, Long> cooldowns;

	private String header = ChatColor.DARK_GRAY + "[" + ChatColor.DARK_AQUA + "Cast" + ChatColor.DARK_GRAY + "]"
			+ ChatColor.WHITE + " ";

	private double seconds;

	public Cooldown()
	{
		cooldowns = new HashMap<String, Long>();
		seconds = 0;
	}

	public boolean hasCooldown(String name)
	{
		if (cooldowns.containsKey(name))
		{
			double secondsleft = getCooldown(name);

			if (secondsleft > 0)
			{
				return true;
			}
		}

		return false;
	}

	public boolean hasCooldown(Player player, String name)
	{
		if (cooldowns.containsKey(player.getName()))
		{
			double secondsleft = getCooldown(player.getName());

			if (secondsleft > 0)
			{
				player.sendMessage(header + name + ChatColor.GRAY + ": " + secondsleft + " Seconds Left!");
				return true;
			}
		}

		return false;
	}

	public double getCooldown(String name)
	{
		return Double.parseDouble(new DecimalFormat("##.#")
				.format(((cooldowns.get(name) / 1000.0) + seconds) - (System.currentTimeMillis() / 1000.0)));
	}

	public double getCooldown()
	{
		return seconds * 20;
	}

	public void start(String name)
	{
		cooldowns.put(name, System.currentTimeMillis());
	}

	public void setCooldown(double duration)
	{
		seconds = duration / 20;
	}

	public void setCooldown(Config config, String path)
	{
		seconds = config.getDouble(path) / 20;
	}
}
