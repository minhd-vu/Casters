package Cast.Essentials.Schedulers;

import Cast.Configs.Config;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.HashMap;

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

	public double getCooldown(String name)
	{
		return Double.parseDouble(new DecimalFormat("##.#")
				.format(((cooldowns.get(name) / 1000.0) + seconds) - (System.currentTimeMillis() / 1000.0)));
	}

	public boolean hasCooldown(Player player, String name)
	{
		if (cooldowns.containsKey(player.getName()))
		{
			return getCooldown(player.getName()) > 0;
		}

		return false;
	}

	public double getCooldown()
	{
		return seconds * 20;
	}

	public void setCooldown(double duration)
	{
		seconds = duration / 20;
	}

	public void start(String name)
	{
		cooldowns.put(name, System.currentTimeMillis());
	}
}
