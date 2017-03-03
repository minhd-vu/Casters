package Cast.Essentials.Schedulers;

import java.util.HashMap;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import Cast.Main;
import Cast.Essentials.Caster;

public class WarmUp
{
	private HashMap<String, Long> warmups;

	private int duration;
	private int amplifier;

	private String header = ChatColor.DARK_GRAY + "[" + ChatColor.DARK_AQUA + "Cast" + ChatColor.DARK_GRAY + "]"
			+ ChatColor.WHITE + " ";

	public WarmUp()
	{
		warmups = new HashMap<String, Long>();
		duration = 0;
		amplifier = 0;
	}

	public boolean hasWarmUp(Player player)
	{
		if (warmups.containsKey(player.getName()))
		{
			if ((System.currentTimeMillis() / 1000.0) - (warmups.get(player.getName()) / 1000.0) < duration / 20.0)
			{
				return true;
			}
		}

		return false;
	}

	public void start(Caster caster, String name)
	{
		if (duration > 0)
		{
			caster.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW, duration, amplifier));
			caster.setWarmingUp(name, true);

			new BukkitRunnable()
			{
				@Override
				public void run()
				{
					caster.setWarmingUp(name, false);
				}

			}.runTaskLater(Main.getInstance(), duration);

			warmups.put(caster.getPlayer().getName(), System.currentTimeMillis());

			List<Entity> e = caster.getPlayer().getNearbyEntities(16, 16, 16);

			for (Entity reciever : e)
			{
				if (reciever instanceof Player)
				{
					reciever.sendMessage(header + ChatColor.WHITE + caster.getPlayer().getName() + ChatColor.GRAY
							+ " Begins To Casts " + ChatColor.WHITE + name + ChatColor.GRAY + "!");
				}
			}

			caster.getPlayer().sendMessage(header + ChatColor.WHITE + "You" + ChatColor.GRAY + " Begin To Cast "
					+ ChatColor.WHITE + name + ChatColor.GRAY + "!");
		}
	}

	public void start(Caster caster, LivingEntity target, String name)
	{
		caster.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW, duration, amplifier));
		caster.setWarmingUp(name, true);

		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				caster.setWarmingUp(name, false);
			}

		}.runTaskLater(Main.getInstance(), duration);

		warmups.put(caster.getPlayer().getName(), System.currentTimeMillis());

		List<Entity> e = caster.getPlayer().getNearbyEntities(16, 16, 16);

		for (Entity reciever : e)
		{
			if (reciever instanceof Player)
			{
				reciever.sendMessage(header + ChatColor.WHITE + caster.getPlayer().getName() + ChatColor.GRAY
						+ " Begins To Casts " + ChatColor.WHITE + name + ChatColor.GRAY + " On " + ChatColor.WHITE
						+ target.getName() + ChatColor.GRAY + "!");
			}
		}

		if (caster.getPlayer().equals(target))
		{
			caster.getPlayer()
					.sendMessage(header + ChatColor.WHITE + "You" + ChatColor.GRAY + " Begin To Cast " + ChatColor.WHITE
							+ name + ChatColor.GRAY + " On " + ChatColor.WHITE + "Yourself" + ChatColor.GRAY + "!");
		}

		else
		{
			caster.getPlayer()
					.sendMessage(header + ChatColor.WHITE + "You" + ChatColor.GRAY + " Begin To Cast " + ChatColor.WHITE
							+ name + ChatColor.GRAY + " On " + ChatColor.WHITE + target.getName() + ChatColor.GRAY
							+ "!");
		}
	}

	public int getDuration()
	{
		return duration;
	}

	public int getAmplifier()
	{
		return amplifier;
	}

	public void setDuration(int duration)
	{
		this.duration = duration;
	}

	public void setAmplifier(int amplifier)
	{
		this.amplifier = amplifier;
	}
}
