package Cast.Essentials.Effects;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.List;

public class Stun
{
	private HashMap<String, Long> stuns = new HashMap<String, Long>();

	private String header = ChatColor.DARK_GRAY + "[" + ChatColor.DARK_AQUA + "Cast" + ChatColor.DARK_GRAY + "]"
			+ ChatColor.WHITE + " ";

	private int duration;

	public Stun()
	{
		duration = 0;
	}

	public void start(Plugin plugin, LivingEntity target)
	{
		if (target instanceof Player)
		{
			stuns.put(target.getName(), System.currentTimeMillis());

			Location location = target.getLocation();

			target.sendMessage(header + "You" + ChatColor.GRAY + " Have Been " + ChatColor.WHITE + "Stunned"
					+ ChatColor.GRAY + "!");

			List<Entity> e = target.getNearbyEntities(16, 16, 16);

			for (Entity player : e)
			{
				if (player instanceof Player)
				{
					player.sendMessage(header + target.getName() + ChatColor.GRAY + " Has Been " + ChatColor.WHITE
							+ "Stunned" + ChatColor.GRAY + "!");
				}
			}

			new BukkitRunnable()
			{
				@Override
				public void run()
				{
					if (System.currentTimeMillis() / 1000.0 - stuns.get(target.getName()) / 1000.0 < duration / 20.0)
					{
						target.teleport(location);
					}
					else
					{
						target.sendMessage(header + "You" + ChatColor.GRAY + " Are No Longer " + ChatColor.WHITE
								+ "Stunned" + ChatColor.GRAY + "!");

						List<Entity> e = target.getNearbyEntities(16, 16, 16);

						for (Entity player : e)
						{
							if (player instanceof Player)
							{
								player.sendMessage(header + target.getName() + ChatColor.GRAY + " Is No Longer "
										+ ChatColor.WHITE + "Stunned" + ChatColor.GRAY + "!");
							}
						}

						this.cancel();
						return;
					}
				}

			}.runTaskTimer(plugin, 0L, 100L);
		}
		else if (target instanceof LivingEntity)
		{
			target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, duration, 7));

			List<Entity> e = target.getNearbyEntities(16, 16, 16);

			for (Entity player : e)
			{
				if (player instanceof Player)
				{
					player.sendMessage(header + target.getName() + ChatColor.GRAY + " Has Been " + ChatColor.WHITE
							+ "Stunned" + ChatColor.GRAY + "!");
				}
			}

			new BukkitRunnable()
			{
				@Override
				public void run()
				{
					List<Entity> e = target.getNearbyEntities(16, 16, 16);

					for (Entity player : e)
					{
						if (player instanceof Player)
						{
							player.sendMessage(header + target.getName() + ChatColor.GRAY + " Is No Longer "
									+ ChatColor.WHITE + "Stunned" + ChatColor.GRAY + "!");
						}
					}

					return;
				}

			}.runTaskLater(plugin, duration);
		}
	}

	public int getDuration()
	{
		return duration;
	}

	public void setDuration(int duration)
	{
		this.duration = duration;
	}
}
