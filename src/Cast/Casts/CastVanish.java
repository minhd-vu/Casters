package Cast.Casts;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
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

public class CastVanish extends ActiveCast implements CommandInterface, Listener
{
	private int duration;
	private int range;

	public CastVanish(String name, String description)
	{
		super(name, description);

		warmup.setDuration(0);
		warmup.setAmplifier(0);
		cooldown.setCooldown(40);
		manacost = 3;

		info.add(ChatColor.DARK_AQUA + "WarmUp: " + ChatColor.GRAY + warmup.getDuration() / 20.0 + " Seconds.");
		info.add(ChatColor.DARK_AQUA + "Cooldown: " + ChatColor.GRAY + cooldown.getCooldown() / 20.0 + " Seconds.");
		info.add(ChatColor.DARK_AQUA + "Cost: " + ChatColor.GRAY + manacost + " MP.");

		duration = 100;
		range = 16;

		info.add(ChatColor.DARK_AQUA + "Duration: " + ChatColor.GRAY + duration / 20.0 + " Seconds");
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
					warmup.start(Main.getInstance(), caster, name);
				}

				new BukkitRunnable()
				{
					@Override
					public void run()
					{
						caster.setCasting(name, true);
						caster.setEffect("Invisible", duration);
						caster.setMana(manacost);

						for (Player p : player.getServer().getOnlinePlayers())
						{
							p.hidePlayer(player);

							if (player.getNearbyEntities(range, range, range).contains(p))
							{
								p.sendMessage(header + ChatColor.WHITE + " Someone" + ChatColor.GRAY + " Has "
										+ ChatColor.WHITE + "Vanished" + ChatColor.GRAY + "!");
							}
						}

						player.getWorld().playEffect(player.getLocation(), org.bukkit.Effect.SMOKE, 4);

						player.sendMessage(header + ChatColor.WHITE + " You" + ChatColor.GRAY + " Have "
								+ ChatColor.WHITE + "Vanished" + ChatColor.GRAY + "!");

						cooldown.start(player.getName());

						new BukkitRunnable()
						{
							@Override
							public void run()
							{
								for (Player p : player.getServer().getOnlinePlayers())
								{
									p.showPlayer(player);

									if (player.getNearbyEntities(range, range, range).contains(p))
									{
										p.sendMessage(header + ChatColor.WHITE + " " + player.getName() + ChatColor.GRAY
												+ " Has " + ChatColor.WHITE + "Reappeared" + ChatColor.GRAY + "!");
									}
								}

								player.sendMessage(header + ChatColor.WHITE + " You" + ChatColor.GRAY + " Are Now "
										+ ChatColor.WHITE + "Visible" + ChatColor.GRAY + "!");

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
		if (event.getEntity() instanceof Player)
		{
			Caster caster = Main.getCasters().get(event.getEntity().getUniqueId());

			if (caster.hasEffect("Invisible"))
			{
				for (Player p : event.getEntity().getServer().getOnlinePlayers())
				{
					caster.setEffect("Invisible", 0);

					p.showPlayer((Player) event.getEntity());

					if (event.getEntity().getNearbyEntities(range, range, range).contains(p))
					{
						p.sendMessage(header + ChatColor.WHITE + " " + event.getEntity().getName() + ChatColor.GRAY
								+ " Has " + ChatColor.WHITE + "Reappeared" + ChatColor.GRAY + "!");
					}
				}

				caster.getPlayer().sendMessage(header + ChatColor.WHITE + " You" + ChatColor.GRAY + " Are Now "
						+ ChatColor.WHITE + "Visible" + ChatColor.GRAY + "!");

				caster.setCasting(name, false);
			}
		}
	}
}
