package Casters.Casts.Targetted;

import Casters.Casters;
import Casters.CommandInterface;
import Casters.Essentials.Caster;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CastBeasts extends Targetted implements CommandInterface, Listener
{
	private List<UUID> beasts;

	private double duration;
	private int amount;
	private int range;
	private double wolfdamage;

	public CastBeasts(String name, String description)
	{
		super(name, description);

		beasts = new ArrayList<UUID>();

		warmup.setDuration(20);
		warmup.setAmplifier(5);
		cooldown.setCooldown(100);
		manacost = 3;

		info.add(ChatColor.DARK_AQUA + "WarmUp: " + ChatColor.GRAY + warmup.getDuration() / 20.0 + " Seconds");
		info.add(ChatColor.DARK_AQUA + "Cooldown: " + ChatColor.GRAY + cooldown.getCooldown() / 20.0 + " Seconds");
		info.add(ChatColor.DARK_AQUA + "Cost: " + ChatColor.GRAY + manacost + " MP");

		duration = 100;
		amount = 3;
		range = 10;
		wolfdamage = 3;

		info.add(duration == 0 ? ChatColor.DARK_AQUA + "Duration: " + ChatColor.GRAY + "Forever"
				: ChatColor.DARK_AQUA + "Duration: " + ChatColor.GRAY + duration / 20 + " Seconds");
		info.add(ChatColor.DARK_AQUA + "Amount: " + ChatColor.GRAY + amount + " Wolves");
		info.add(ChatColor.DARK_AQUA + "Range: " + ChatColor.GRAY + range + " Blocks");
		info.add(ChatColor.DARK_AQUA + "Wolf Damage: " + ChatColor.GRAY + wolfdamage + " HP");

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
				LivingEntity target = getTarget(player, range, false, false);

				if (target != null && !(target instanceof Wolf))
				{
					warmup.start(caster, target, name);

					new BukkitRunnable()
					{
						@Override
						public void run()
						{
							if (!caster.isInterrupted())
							{
								caster.setCasting(name, true);
								caster.setMana(manacost);

								List<UUID> wolves = new ArrayList<UUID>();

								for (int i = 0; i < amount; ++i)
								{
									Wolf wolf = (Wolf) player.getWorld().spawnEntity(player.getLocation(), EntityType.WOLF);
									wolf.setOwner(player);
									wolf.setTarget(target);

									wolves.add(wolf.getUniqueId());
								}

								beasts.addAll(wolves);

								cast(player, target);

								caster.setCasting(name, false);

								if (duration > 0)
								{
									new BukkitRunnable()
									{
										@Override
										public void run()
										{
											for (UUID wolf : wolves)
											{
												Entity beast = Bukkit.getEntity(wolf);

												if (beast != null)
												{
													beast.remove();
												}
											}

											beasts.remove(wolves);
											wolves.clear();
										}

									}.runTaskLater(Casters.getInstance(), (long) duration);
								}
							}

							cooldown.start(player.getName());
						}

					}.runTaskLater(Casters.getInstance(), warmup.getDuration());
				}
			}
		}

		return true;
	}

	@EventHandler
	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event)
	{
		if (event.getDamager() instanceof Wolf)
		{
			Wolf beast = (Wolf) event.getDamager();

			if (beast.getOwner() instanceof Player)
			{
				Caster caster = Casters.getCasters().get(beast.getOwner().getUniqueId());

				if (event.getEntity() instanceof LivingEntity)
				{
					if (caster.sameParty(event.getEntity()))
					{
						event.setCancelled(true);
						return;
					}

					event.setDamage(wolfdamage);
					caster.setBossBarEntity((LivingEntity) event.getEntity());
				}
			}
		}
	}
}
