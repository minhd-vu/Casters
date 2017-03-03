package Cast.Essentials.Effects;

import Cast.Essentials.Caster;
import Cast.Main;
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

	protected String header = ChatColor.DARK_GRAY + "[" + ChatColor.DARK_AQUA + "Cast" + ChatColor.DARK_GRAY + "]"
			+ ChatColor.WHITE + " ";

	protected double damage;
	protected int duration;
	protected int period;

	public Bleed()
	{
		damage = 0;
		duration = 0;
		period = 0;
	}

	public void start(Caster caster, LivingEntity target, String name)
	{
		bleeds.put(target.getUniqueId(), System.currentTimeMillis());

		if (target instanceof Player)
		{
			target.sendMessage(
					header + "You" + ChatColor.GRAY + " Are " + ChatColor.WHITE + "Bleeding" + ChatColor.GRAY + "!");
		}

		List<Entity> e = target.getNearbyEntities(16, 16, 16);

		for (Entity player : e)
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
				if (System.currentTimeMillis() / 1000.0 - bleeds.get(target.getUniqueId()) / 1000.0 > duration / 20
						|| target.isDead())
				{
					this.cancel();
				}
				else
				{
					target.damage(damage);
				}
			}

		}.runTaskTimer(Main.getInstance(), period, period);

		new BukkitRunnable()
		{
			@Override
			public void run()
			{
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

				caster.setCasting(name, false);
			}
		}.runTaskLater(Main.getInstance(), duration);
	}

	public void start(Caster caster, Caster tcaster, String name)
	{
		Player target = tcaster.getPlayer();

		bleeds.put(target.getUniqueId(), System.currentTimeMillis());

		tcaster.setEffect("Bleeding", duration);

		if (target instanceof Player)
		{
			target.sendMessage("You Are Bleeding!");
		}

		List<Entity> e = target.getNearbyEntities(16, 16, 16);

		for (Entity player : e)
		{
			if (player instanceof Player)
			{
				player.sendMessage(header + target.getName() + ChatColor.WHITE + " Is " + ChatColor.WHITE + "Bleeding"
						+ ChatColor.GRAY + "!");
			}
		}

		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				if (System.currentTimeMillis() / 1000.0 - bleeds.get(target.getUniqueId()) / 1000.0 > duration / 20
						|| target.isDead())
				{
					this.cancel();
				}
				else
				{
					target.damage(damage);
				}
			}

		}.runTaskTimer(Main.getInstance(), period, period);

		new BukkitRunnable()
		{
			@Override
			public void run()
			{
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

				caster.setCasting(name, false);
			}
		}.runTaskLater(Main.getInstance(), duration);
	}

	public void setPeriod(int period)
	{
		this.period = period;
	}

	public double getDamage()
	{
		return damage;
	}

	public void setDamage(double damage)
	{
		this.damage = damage;
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
