package Casters.Casts.Actives;

import Casters.Casters;
import Casters.CommandInterface;
import Casters.Essentials.Caster;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class CastTaunt extends Active implements CommandInterface, Listener
{
	private int range;
	private int duration;
	private int amplifier;

	public CastTaunt(String name, String description)
	{
		super(name, description);

		warmup.setDuration(0);
		warmup.setAmplifier(0);
		cooldown.setCooldown(40);
		manacost = 3;

		info.add(ChatColor.DARK_AQUA + "WarmUp: " + ChatColor.GRAY + warmup.getDuration() / 20.0 + " Seconds");
		info.add(ChatColor.DARK_AQUA + "Cooldown: " + ChatColor.GRAY + cooldown.getCooldown() / 20.0 + " Seconds");
		info.add(ChatColor.DARK_AQUA + "Cost: " + ChatColor.GRAY + manacost + " MP");

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
			Caster caster = Casters.getCasters().get(player.getUniqueId());

			if (args.length == 2 && args[1].equalsIgnoreCase("info"))
			{
				pages.display(player, args, 2);

				return true;
			}

			else if (args.length == 1 && caster.canCast(name, cooldown, manacost))
			{
				List<Entity> targets = player.getNearbyEntities(range, range, range);
				List<LivingEntity> livingtargets = new ArrayList<LivingEntity>();

				for (Entity target : targets)
				{
					if (target instanceof LivingEntity && !caster.sameParty(target))
					{
						livingtargets.add((LivingEntity) target);
					}
				}

				warmup.start(caster, name);

				new BukkitRunnable()
				{
					@Override
					public void run()
					{
						if (!caster.isInterrupted())
						{
							caster.setCasting(name, true);
							caster.setEffect("Taunting", duration);
							caster.setMana(manacost);

							cast(player);

							new BukkitRunnable()
							{
								@Override
								public void run()
								{
									if (caster.isCasting(name)) // TODO: Check If This Works.
									{
										decast(player);
										caster.setCasting(name, false);
									}
								}

							}.runTaskLater(Casters.getInstance(), duration);
						}
					}

				}.runTaskLater(Casters.getInstance(), warmup.getDuration());
			}
		}

		return true;
	}

	@EventHandler
	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event)
	{
		if (event.getDamager() instanceof Player && event.getEntity() instanceof Player)
		{
			Caster attacker = Casters.getCasters().get(event.getDamager().getUniqueId());
			Caster defender = Casters.getCasters().get(event.getEntity().getUniqueId());

			if (attacker.hasEffect("Taunted") && !defender.hasEffect("Taunting"))
			{
				attacker.getPlayer()
						.sendMessage(header + ChatColor.WHITE + "You" + ChatColor.GRAY + " Cannot Attack That Player While " + ChatColor.WHITE + "Taunted" + ChatColor.GRAY + "!");

				event.setCancelled(true);
			}

			return;
		}
	}
}
