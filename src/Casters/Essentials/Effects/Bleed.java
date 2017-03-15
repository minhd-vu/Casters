package Casters.Essentials.Effects;

import Casters.Essentials.Caster;
import Casters.Main;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Bleed
{
	protected HashMap<UUID, Long> bleeds = new HashMap<UUID, Long>();

	protected String header = ChatColor.DARK_GRAY + "[" + ChatColor.DARK_AQUA + "Casters" + ChatColor.DARK_GRAY + "]"
			+ ChatColor.WHITE + " ";

	protected double damage;
	protected int duration;
	protected int period;

	public Bleed(double damage, int duration, int period)
	{
		this.damage = damage;
		this.duration = duration;
		this.period = period;
	}

	public void start(Caster caster, LivingEntity target, String name)
	{
		bleeds.put(target.getUniqueId(), System.currentTimeMillis());

		if (target instanceof Player)
		{
			Main.getCasters().get(target.getUniqueId()).setEffect("Bleeding", duration);
			target.sendMessage(
					header + "You" + ChatColor.GRAY + " Are " + ChatColor.WHITE + "Bleeding" + ChatColor.GRAY + "!");
		}

		List<Entity> entities = target.getNearbyEntities(16, 16, 16);

		for (Entity player : entities)
		{
			if (player instanceof Player)
			{
				player.sendMessage(header + target.getName() + ChatColor.GRAY + " Is " + ChatColor.WHITE + "Bleeding"
						+ ChatColor.GRAY + "!");
			}
		}

		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				if (target == null || target.isDead())
				{
					cancel();
					return;
				}

				else if (System.currentTimeMillis() / 1000.0 - bleeds.get(target.getUniqueId()) / 1000.0 > duration / 20)
				{
					bleeds.remove(target.getUniqueId());

					if (target instanceof Player)
					{
						target.sendMessage(header + "You" + ChatColor.GRAY + " Have Stopped " + ChatColor.WHITE + "Bleeding"
								+ ChatColor.GRAY + "!");
					}

					List<Entity> e = target.getNearbyEntities(16, 16, 16);

					for (Entity player : e)
					{
						if (player instanceof Player)
						{
							player.sendMessage(header + target.getName() + ChatColor.GRAY + " Has Stopped "
									+ ChatColor.WHITE + "Bleeding" + ChatColor.GRAY + "!");
						}
					}

					cancel();
					return;
				}

				else
				{
					target.damage(damage);
				}
			}

		}.runTaskTimer(Main.getInstance(), period, period);
	}
}
