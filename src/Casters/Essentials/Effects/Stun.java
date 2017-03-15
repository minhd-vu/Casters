package Casters.Essentials.Effects;

import Casters.Essentials.Caster;
import Casters.Main;
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

public class Stun
{
	private HashMap<String, Long> stuns = new HashMap<String, Long>();

	private String header = ChatColor.DARK_GRAY + "[" + ChatColor.DARK_AQUA + "Casters" + ChatColor.DARK_GRAY + "]"
			+ ChatColor.WHITE + " ";

	private int duration;

	public Stun(int duration)
	{
		this.duration = duration;
	}

	public void start(LivingEntity target)
	{
		if (target instanceof Player)
		{
			Caster caster = Main.getCasters().get(target.getUniqueId());
			caster.setEffect("Stunned", duration);

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

			}.runTaskTimer(Main.getInstance(), 0, 1);
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

			}.runTaskLater(Main.getInstance(), duration);
		}
	}
}
