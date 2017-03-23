package Casters.Essentials.Effects;

import Casters.Casters;
import Casters.Essentials.Caster;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.List;

public class Root
{
	private HashMap<String, Long> stuns = new HashMap<String, Long>();

	private String header = ChatColor.DARK_GRAY + "[" + ChatColor.DARK_AQUA + "Cast" + ChatColor.DARK_GRAY + "]"
			+ ChatColor.WHITE + " ";

	private int duration;

	public Root(int duration)
	{
		this.duration = duration;
	}

	public void start(LivingEntity target)
	{
		if (target instanceof Player)
		{
			Caster caster = Casters.getCasters().get(target.getUniqueId());
			caster.setEffect("Rooted", duration);

			stuns.put(target.getName(), System.currentTimeMillis());

			Location location = target.getLocation();

			target.sendMessage(header + "You" + ChatColor.GRAY + " Have Been " + ChatColor.WHITE + "Rooted" + ChatColor.GRAY + "!");

			List<Entity> e = target.getNearbyEntities(16, 16, 16);

			for (Entity player : e)
			{
				if (player instanceof Player)
				{
					player.sendMessage(header + target.getName() + ChatColor.GRAY + " Has Been " + ChatColor.WHITE + "Rooted" + ChatColor.GRAY + "!");
				}
			}

			new BukkitRunnable()
			{
				@Override
				public void run()
				{
					if (System.currentTimeMillis() / 1000.0 - stuns.get(target.getName()) / 1000.0 < duration / 20.0)
					{
						target.teleport(location.setDirection(target.getLocation().getDirection())); // TODO: Test If The DIrection TelePort Works.
					}

					else
					{
						target.sendMessage(header + "You" + ChatColor.GRAY + " Are No Longer " + ChatColor.WHITE + "Rooted" + ChatColor.GRAY + "!");

						List<Entity> e = target.getNearbyEntities(16, 16, 16);

						for (Entity player : e)
						{
							if (player instanceof Player)
							{
								player.sendMessage(header + target.getName() + ChatColor.GRAY + " Is No Longer " + ChatColor.WHITE + "Rooted" + ChatColor.GRAY + "!");
							}
						}

						this.cancel();
						return;
					}
				}

			}.runTaskTimer(Casters.getInstance(), 0, 1);
		}

		else if (target instanceof LivingEntity)
		{
			target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, duration, 7));

			List<Entity> e = target.getNearbyEntities(16, 16, 16);

			for (Entity player : e)
			{
				if (player instanceof Player)
				{
					player.sendMessage(header + target.getName() + ChatColor.GRAY + " Has Been " + ChatColor.WHITE + "Rooted" + ChatColor.GRAY + "!");
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
							player.sendMessage(header + target.getName() + ChatColor.GRAY + " Is No Longer " + ChatColor.WHITE + "Rooted" + ChatColor.GRAY + "!");
						}
					}

					return;
				}

			}.runTaskLater(Casters.getInstance(), duration);
		}
	}

	public void stop(LivingEntity target)
	{
		if (target instanceof Player)
		{
			Caster caster = Casters.getCasters().get(target.getUniqueId()); // TODO: Finish This.
			caster.setEffect("Rooted", 0);

			List<Entity> e = target.getNearbyEntities(16, 16, 16);

			for (Entity player : e)
			{
				if (player instanceof Player)
				{
					player.sendMessage(header + target.getName() + ChatColor.GRAY + " Is No Longer " + ChatColor.WHITE + "Rooted" + ChatColor.GRAY + "!");
				}
			}
		}

		else
		{
			target.removePotionEffect(PotionEffectType.SLOW);
		}
	}
}
