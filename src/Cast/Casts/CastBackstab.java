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

public class CastBackstab extends ActiveCast implements CommandInterface, Listener
{
	private HashMap<String, Long> backstabs = new HashMap<String, Long>();
	private int duration;
	private int percentage;
	private int sneaking;

	public CastBackstab(String name, String description)
	{
		super(name, description);

		warmup.setDuration(20);
		warmup.setAmplifier(5);
		cooldown.setCooldown(100);
		manacost = 5;

		info.add(ChatColor.DARK_AQUA + "WarmUp: " + ChatColor.GRAY + warmup.getDuration() / 20.0 + " Seconds.");
		info.add(ChatColor.DARK_AQUA + "Cooldown: " + ChatColor.GRAY + cooldown.getCooldown() / 20.0 + " Seconds.");
		info.add(ChatColor.DARK_AQUA + "Cost: " + ChatColor.GRAY + manacost + " MP.");

		duration = 200;
		percentage = 150;
		sneaking = 200;

		info.add(ChatColor.DARK_AQUA + "Duration: " + ChatColor.GRAY + duration / 20 + " Seconds");
		info.add(ChatColor.DARK_AQUA + "Bonus: " + percentage + "%");
		info.add(ChatColor.DARK_AQUA + "Speaking: " + sneaking + "%");

		pages.setPage(info);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if (sender instanceof Player)
		{
			Player player = (Player) sender;
			Caster caster = Main.getCasters().get(player.getUniqueId());

			if (args.length == 2 && args[1].equalsIgnoreCase("info"))
			{
				pages.display(player, args, 2);

				return true;
			}

			else if (args.length == 1 && caster.hasCast(name) && !caster.isCasting(name) && !caster.isWarmingUp()
					&& !caster.isSilenced(name) && !caster.isStunned(name) && !cooldown.hasCooldown(player, name)
					&& caster.hasMana(manacost, name))
			{
				if (warmup.getDuration() > 0)
				{
					warmup.start(Main.getProvidingPlugin(this.getClass()), caster, name);
				}

				new BukkitRunnable()
				{
					@Override
					public void run()
					{
						caster.setCasting(name, true);
						caster.setEffect("Backstabbing", duration);
						caster.setMana(manacost);

						backstabs.put(player.getName(), System.currentTimeMillis());

						cast(player);

						new BukkitRunnable()
						{
							@Override
							public void run()
							{
								decast(player);

								caster.setCasting(name, false);
							}

						}.runTaskLater(Main.getProvidingPlugin(this.getClass()), duration);

						cooldown.start(player.getName());
					}

				}.runTaskLater(Main.getProvidingPlugin(this.getClass()), warmup.getDuration());
			}
		}

		return true;
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event)
	{
		Entity player = event.getDamager();
		Entity target = event.getEntity();

		Caster caster = Main.getCasters().get(player.getUniqueId());

		if (player instanceof Player && target instanceof LivingEntity && caster.isCasting(name))
		{
			if (event.getEntity().getLocation().getDirection().dot(player.getLocation().getDirection()) > 0.0D)
			{
				if ((System.currentTimeMillis() / 1000.0) - (backstabs.get(player.getName()) / 1000.0) < duration / 20)
				{
					if (((Player) player).isSneaking())
					{
						((Damageable) target).damage(event.getDamage() * (sneaking / 100));
					}

					else
					{
						((Damageable) target).damage(event.getDamage() * (percentage / 100));
					}

					target.getWorld().spigot().playEffect(target.getLocation(), Effect.COLOURED_DUST, 0, 0, 0.2F, 1.0F,
							0.2F, 0.0F, 30, 16);
					target.getWorld().playSound(target.getLocation(), Sound.ENTITY_ENDERDRAGON_HURT, 1.0F, 0.6F);

					event.setCancelled(true);
				}
			}
		}
	}
}
