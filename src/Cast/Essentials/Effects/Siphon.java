package Cast.Essentials.Effects;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import Cast.Main;
import Cast.Essentials.Caster;

public class Siphon extends Bleed
{
	private double percentage;

	@Override
	public void start(Caster caster, LivingEntity target, String name)
	{
		bleeds.put(target.getUniqueId(), System.currentTimeMillis());

		caster.setEffect("Siphoning", duration);

		if (target instanceof Player)
		{
			target.sendMessage(header + "You" + ChatColor.GRAY + "Are Being " + ChatColor.WHITE + "Siphoned"
					+ ChatColor.GRAY + "!");
		}

		List<Entity> e = target.getNearbyEntities(16, 16, 16);

		for (Entity player : e)
		{
			if (player instanceof Player)
			{
				player.sendMessage(header + target.getName() + ChatColor.GRAY + " Is Being " + ChatColor.WHITE
						+ "Siphoned" + ChatColor.GRAY + "!");
			}
		}

		new BukkitRunnable()
		{
			@SuppressWarnings("deprecation")
			@Override
			public void run()
			{
				Player player = caster.getPlayer();

				if (System.currentTimeMillis() / 1000.0 - bleeds.get(target.getUniqueId()) / 1000.0 > duration / 20
						|| target.isDead())
				{
					this.cancel();
				}

				else
				{
					target.getWorld().spigot().playEffect(target.getLocation().add(0, 1, 0), Effect.WITCH_MAGIC, 0, 0,
							0.5F, 0.5F, 0.5F, 1.0F, 16, 16);
					target.getWorld().playSound(target.getLocation(), Sound.ENTITY_WITCH_DRINK, 8.0F, 1.0F);
					target.damage(damage);

					if (player.getHealth() + damage * (percentage / 100) > player.getMaxHealth())
					{
						player.setHealth(player.getMaxHealth());
					}

					else
					{
						player.setHealth(player.getHealth() + damage * (percentage / 100));
					}
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
					target.sendMessage(header + "You" + ChatColor.GRAY + " Are No Longer " + ChatColor.WHITE
							+ "Siphoned" + ChatColor.GRAY + "!");
				}

				List<Entity> e = target.getNearbyEntities(16, 16, 16);

				for (Entity player : e)
				{
					if (player instanceof Player)
					{
						player.sendMessage(header + target.getName() + ChatColor.GRAY + " Is No Longer "
								+ ChatColor.WHITE + "Siphoned" + ChatColor.GRAY + "!");
					}
				}

				caster.setCasting(name, false);
			}
		}.runTaskLater(Main.getInstance(), duration);
	}

	@Override
	public void start(Caster caster, Caster tcaster, String name)
	{
		Player target = tcaster.getPlayer();

		bleeds.put(target.getUniqueId(), System.currentTimeMillis());

		caster.setEffect("Siphoning", duration);
		;
		tcaster.setEffect("Siphoned", duration);
		;

		if (target instanceof Player)
		{
			target.sendMessage(header + "You" + ChatColor.GRAY + " Are Being " + ChatColor.WHITE + "Siphoned"
					+ ChatColor.GRAY + "!");
		}

		List<Entity> e = target.getNearbyEntities(16, 16, 16);

		for (Entity player : e)
		{
			if (player instanceof Player)
			{
				player.sendMessage(header + target.getName() + ChatColor.GRAY + " Is Being " + ChatColor.WHITE
						+ "Siphoned" + ChatColor.GRAY + "!");
			}
		}

		new BukkitRunnable()
		{
			@SuppressWarnings("deprecation")
			@Override
			public void run()
			{
				Player player = caster.getPlayer();

				if (System.currentTimeMillis() / 1000.0 - bleeds.get(player.getUniqueId()) / 1000.0 > duration / 20)
				{
					this.cancel();
				}

				else
				{
					target.getWorld().spigot().playEffect(target.getLocation().add(0, 1, 0), Effect.WITCH_MAGIC, 0, 0,
							0.5F, 0.5F, 0.5F, 1.0F, 16, 16);
					target.getWorld().playSound(target.getLocation(), Sound.ENTITY_WITCH_DRINK, 8.0F, 1.0F);
					target.damage(damage);

					if (player.getHealth() + damage * (percentage / 100) > player.getMaxHealth())
					{
						player.setHealth(player.getMaxHealth());
					}

					else
					{
						player.setHealth(player.getHealth() + damage * (percentage / 100));
					}
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
					target.sendMessage(header + "You" + ChatColor.GRAY + " Are No Longer " + ChatColor.WHITE
							+ "Siphoned" + ChatColor.GRAY + "!");
				}

				List<Entity> e = target.getNearbyEntities(16, 16, 16);

				for (Entity player : e)
				{
					if (player instanceof Player)
					{
						player.sendMessage(header + target.getName() + ChatColor.GRAY + " Is No Longer "
								+ ChatColor.WHITE + "Siphoned" + ChatColor.GRAY + "!");
					}
				}

				caster.setCasting(name, false);
			}
		}.runTaskLater(Main.getInstance(), duration);
	}

	public void setPercentage(double percentage)
	{
		this.percentage = percentage;
	}
}
