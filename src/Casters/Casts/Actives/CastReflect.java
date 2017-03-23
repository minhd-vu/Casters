package Casters.Casts.Actives;

import Casters.Casters;
import Casters.CommandInterface;
import Casters.Essentials.Caster;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

public class CastReflect extends Active implements CommandInterface, Listener
{
	private HashMap<UUID, Long> reflects;
	private int duration;
	private double percentage;

	public CastReflect(String name, String description)
	{
		super(name, description);

		reflects = new HashMap<UUID, Long>();

		warmup.setDuration(0);
		warmup.setAmplifier(0);
		cooldown.setCooldown(40);
		manacost = 3;

		info.add(ChatColor.DARK_AQUA + "WarmUp: " + ChatColor.GRAY + warmup.getDuration() / 20.0 + " Seconds");
		info.add(ChatColor.DARK_AQUA + "Cooldown: " + ChatColor.GRAY + cooldown.getCooldown() / 20.0 + " Seconds");
		info.add(ChatColor.DARK_AQUA + "Cost: " + ChatColor.GRAY + manacost + " MP");

		duration = 100;
		percentage = 100.0;

		info.add(ChatColor.DARK_AQUA + "Duration: " + ChatColor.GRAY + duration / 20.0 + " Seconds");
		info.add(ChatColor.DARK_AQUA + "Percentage: " + ChatColor.GRAY + percentage + "%");

		pages.setPage(info);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if (sender instanceof Player)
		{
			Player player = (Player) sender;
			Caster caster = Casters.getCasters().get(player.getUniqueId());

			if (args.length == 2 && args[1].equalsIgnoreCase("info"))
			{
				pages.display(player, args, 2);

				return true;
			}

			else if (args.length == 1 && caster.canCast(name, cooldown, manacost))
			{
				warmup.start(caster, name);

				new BukkitRunnable()
				{
					@Override
					public void run()
					{
						if (!caster.isInterrupted())
						{
							caster.setCasting(name, true);
							caster.setEffect("Reflecting", duration);
							caster.setMana(manacost);

							reflects.put(player.getUniqueId(), System.currentTimeMillis());

							player.getWorld().playEffect(player.getLocation(), Effect.MOBSPAWNER_FLAMES, 3);
							player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ZOMBIE_ATTACK_IRON_DOOR, 0.8F, 1.0F);

							cast(player);

							new BukkitRunnable()
							{
								private int count = 0;
								private boolean interrupted = false;

								@Override
								public void run()
								{
									if (caster.isInterrupted())
									{
										caster.setEffect("Reflecting", 0.0);
										reflects.remove(player.getUniqueId());

										interrupted = true;
									}

									if (++count > duration || interrupted)
									{
										this.cancel();
										return;
									}
								}
								
							}.runTaskTimer(Casters.getInstance(), 1, 1);

							new BukkitRunnable()
							{
								@Override
								public void run()
								{
									if (caster.isCasting(name))
									{
										decast(player);
										caster.setCasting(name, false);
									}
								}

							}.runTaskLater(Casters.getInstance(), duration);
						}

						cooldown.start(player.getName());
					}

				}.runTaskLater(Casters.getInstance(), warmup.getDuration());
			}
		}

		return true;
	}

	@EventHandler
	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event)
	{
		if (event.getEntity() instanceof Player && event.getDamager() instanceof LivingEntity)
		{
			Caster caster = Casters.getCasters().get(event.getEntity().getUniqueId());
			LivingEntity target = (LivingEntity) event.getDamager();

			if (reflects.containsKey(caster.getPlayer().getUniqueId()) && caster.isCasting(name) &&
					(System.currentTimeMillis() / 1000.0) - (reflects.get(caster.getPlayer().getUniqueId()) / 1000.0) < duration / 20.0)
			{
				// TODO: Removed The Event Cancelling; Retest.

				if (caster.sameParty(target))
				{
					return;
				}

				((LivingEntity) target).damage(event.getDamage() * (percentage / 100.0));
				caster.setBossBarEntity((LivingEntity) target);
			}
		}
	}
}
