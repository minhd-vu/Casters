package Cast.Casts;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import Cast.CommandInterface;
import Cast.Main;
import Cast.Casts.Types.ActiveCast;
import Cast.Essentials.Caster;
import net.md_5.bungee.api.ChatColor;

public class CastTaunt extends ActiveCast implements CommandInterface, Listener
{
	private int range;
	private int duration;
	private int amplifier;

	public CastTaunt(String name)
	{
		super(name);

		warmup.setDuration(0);
		warmup.setAmplifier(0);
		cooldown.setCooldown(40);
		manacost = 3;

		info.add(ChatColor.DARK_AQUA + name + " Cast:");
		info.add(ChatColor.DARK_AQUA + "WarmUp: " + ChatColor.GRAY + warmup.getDuration() / 20.0 + " Seconds.");
		info.add(ChatColor.DARK_AQUA + "Cooldown: " + ChatColor.GRAY + cooldown.getCooldown() / 20.0 + " Seconds.");
		info.add(ChatColor.DARK_AQUA + "Cost: " + ChatColor.GRAY + manacost + " MP.");

		range = 10;
		duration = 100;
		amplifier = 2;

		info.add(ChatColor.DARK_AQUA + "Duration: " + ChatColor.GRAY + duration / 20.0 + " Seconds");
		info.add(ChatColor.DARK_AQUA + "Weakness: " + ChatColor.GRAY + amplifier);
		info.add(ChatColor.DARK_AQUA + "Range: " + ChatColor.GRAY + range + " Blocks");

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
						List<Entity> targets = player.getNearbyEntities(range, range, range);

						caster.setCasting(name, true);
						caster.setEffect("Taunting", duration);
						caster.setMana(manacost);

						cast(player);

						for (Entity target : targets)
						{
							if (target instanceof LivingEntity)
							{
								((LivingEntity) target).addPotionEffect(
										new PotionEffect(PotionEffectType.WEAKNESS, duration, amplifier));

								if (target instanceof Creature)
								{
									((Creature) target).setTarget(player);
								}

								if (target instanceof Player)
								{
									Caster tcaster = Main.getCasters().get(target.getUniqueId());
									tcaster.setEffect("Taunted", duration);
								}
							}
						}

						new BukkitRunnable()
						{
							@Override
							public void run()
							{
								decast(player);
								caster.setCasting(name, false);
							}
						}.runTaskLater(Main.getInstance(), duration);
					}

				}.runTaskLater(Main.getInstance(), warmup.getDuration());
			}
		}

		return true;
	}

	@EventHandler
	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event)
	{
		if (event.getDamager() instanceof Player && event.getEntity() instanceof Player)
		{
			Caster attacker = Main.getCasters().get(event.getDamager().getUniqueId());
			Caster defender = Main.getCasters().get(event.getEntity().getUniqueId());

			if (attacker.getEffect("Taunted") && !defender.getEffect("Taunting"))
			{
				attacker.getPlayer().sendMessage(header + ChatColor.WHITE + "You" + ChatColor.GRAY
						+ " Cannot Attack That Player While " + ChatColor.WHITE + "Taunted" + ChatColor.GRAY + "!");

				event.setCancelled(true);
			}

			return;
		}
	}
}
