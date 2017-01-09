package Cast.Casts;

import java.util.HashMap;

import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;

import Cast.CommandInterface;
import Cast.Main;
import Cast.Casts.Types.ActiveCast;
import Cast.Essentials.Caster;
import net.md_5.bungee.api.ChatColor;

public class CastReflect extends ActiveCast implements CommandInterface, Listener
{
	private HashMap<String, Long> reflects = new HashMap<String, Long>();
	private int duration;
	private int percentage;

	public CastReflect(String name)
	{
		super(name);

		duration = Main.getConfigCasts().getInt("Reflect.Duration");
		percentage = Main.getConfigCasts().getInt("Reflect.Percentage");

		info.add(ChatColor.DARK_AQUA + "Duration: " + ChatColor.GRAY + duration / 20.0 + " Seconds");
		info.add(ChatColor.DARK_AQUA + "Percentage: " + ChatColor.GRAY + percentage + " %");

		pages.setPage(info);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if (sender instanceof Player)
		{
			Player player = (Player) sender;
			Caster caster = Main.getCasters().get(player.getUniqueId());

			if (args.length > 1)
			{
				pages.display(player, args, 2);

				return true;
			}

			if (caster.hasCast(name) && !caster.isCasting(name) && !caster.isWarmingUp() && !caster.isSilenced(name)
					&& !caster.isStunned(name) && !cooldown.hasCooldown(player, name) && caster.hasMana(manacost, name))
			{
				if (warmup.getDuration() > 0)
				{
					warmup.start(Main.getInstance(), caster, name);
				}

				new BukkitRunnable()
				{
					@Override
					public void run()
					{
						caster.setCasting(name, true);
						caster.setEffect("Reflecting", duration);
						caster.setMana(manacost);

						reflects.put(player.getName(), System.currentTimeMillis());

						player.getWorld().playEffect(player.getLocation(), Effect.MOBSPAWNER_FLAMES, 3);
						player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ZOMBIE_ATTACK_IRON_DOOR, 0.8F,
								1.0F);

						cast(player);

						new BukkitRunnable()
						{
							@Override
							public void run()
							{
								decast(player);
								caster.setCasting(name, false);
							}

						}.runTaskLater(Main.getInstance(), duration);

						cooldown.start(player.getName());
					}

				}.runTaskLater(Main.getInstance(), warmup.getDuration());
			}
		}

		return true;
	}

	@EventHandler
	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event)
	{
		Entity defender = event.getEntity();
		Entity attacker = event.getDamager();

		Caster caster = Main.getCasters().get(defender.getUniqueId());

		if (attacker instanceof LivingEntity && defender instanceof Player && caster.isCasting(name))
		{
			if ((System.currentTimeMillis() / 1000.0) - (reflects.get(defender.getName()) / 1000.0) < duration / 20)
			{
				((Damageable) attacker).damage(event.getDamage() * (percentage / 100));

				event.setCancelled(true);
				return;
			}
		}
	}
}
