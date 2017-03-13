package Cast.Essentials.Effects;

import Cast.Essentials.Caster;
import Cast.Main;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class Poison
{
	protected HashMap<UUID, Long> poisons = new HashMap<UUID, Long>();

	protected String header = ChatColor.DARK_GRAY + "[" + ChatColor.DARK_AQUA + "Cast" + ChatColor.DARK_GRAY + "]";

	protected double damage;
	protected int duration;
	protected int amplifier;

	public Poison(double damage, int duration, int amplfier)
	{
		this.damage = damage;
		this.duration = duration;
		this.amplifier = amplfier;
	}

	public void start(Caster caster, LivingEntity target, String name)
	{
		poisons.put(target.getUniqueId(), System.currentTimeMillis());

		target.addPotionEffect(new PotionEffect(PotionEffectType.POISON, duration, amplifier));

		if (target instanceof Player)
		{
			Caster targetcaster = Main.getCasters().get(target.getUniqueId());
			targetcaster.setEffect("Poisoned", duration);
			target.sendMessage(header + ChatColor.WHITE + " You" + ChatColor.GRAY + " Are "
					+ ChatColor.WHITE + "Poisoned" + ChatColor.GRAY + "!");
		}

		List<Entity> entities = target.getNearbyEntities(16, 16, 16);

		for (Entity player : entities)
		{
			if (player instanceof Player)
			{
				player.sendMessage(
						header + ChatColor.WHITE + " " + target.getName() + ChatColor.GRAY + " Is "
								+ ChatColor.WHITE + "Poisoned" + ChatColor.GRAY + "!");
			}
		}

		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				poisons.remove(target.getUniqueId());

				if (target instanceof Player)
				{
					target.sendMessage(header + ChatColor.WHITE + " You" + ChatColor.GRAY
							+ " Have Stopped Being " + ChatColor.WHITE + "Poisoned" + ChatColor.GRAY
							+ "!");
				}

				List<Entity> e = target.getNearbyEntities(16, 16, 16);

				for (Entity player : e)
				{
					if (player instanceof Player)
					{
						player.sendMessage(header + ChatColor.WHITE + " " + target.getName()
								+ ChatColor.GRAY + " Has Stopped Being " + ChatColor.WHITE
								+ "Poisoned" + ChatColor.GRAY + "!");
					}
				}
			}

		}.runTaskLater(Main.getInstance(), duration);
	}

	public Set<UUID> getAffectedPlayers()
	{
		return poisons.keySet();
	}
}
