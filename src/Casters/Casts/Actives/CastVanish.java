package Casters.Casts.Actives;

import Casters.Casters;
import Casters.CommandInterface;
import Casters.Essentials.Caster;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Effect;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class CastVanish extends Active implements CommandInterface, Listener
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

		info.add(ChatColor.DARK_AQUA + "WarmUp: " + ChatColor.GRAY + warmup.getDuration() / 20.0 + " Seconds");
		info.add(ChatColor.DARK_AQUA + "Cooldown: " + ChatColor.GRAY + cooldown.getCooldown() / 20.0 + " Seconds");
		info.add(ChatColor.DARK_AQUA + "Cost: " + ChatColor.GRAY + manacost + " MP");

		duration = 300;
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
							caster.setEffect("Invisible", duration);
							caster.setMana(manacost);

							for (Player p : player.getServer().getOnlinePlayers())
							{
								p.hidePlayer(player);

								if (player.getNearbyEntities(range, range, range).contains(p))
								{
									p.sendMessage(header + ChatColor.WHITE + " Someone" + ChatColor.GRAY + " Has " + ChatColor.WHITE + "Vanished" + ChatColor.GRAY + "!");
								}
							}

							player.getWorld().playEffect(player.getLocation(), Effect.SMOKE, 0, 4);
							player.sendMessage(header + ChatColor.WHITE + " You" + ChatColor.GRAY + " Have " + ChatColor.WHITE + "Vanished" + ChatColor.GRAY + "!");
						}

						cooldown.start(player.getName());

						new BukkitRunnable()
						{
							@Override
							public void run()
							{
								if (caster.isCasting(name))
								{
									cancelInvisibility(player);
								}
							}

						}.runTaskLater(Casters.getInstance(), duration);
					}

				}.runTaskLater(Casters.getInstance(), warmup.getDuration());
			}
		}

		return true;
	}

	private void cancelInvisibility(Player player)
	{
		Caster caster = Casters.getCasters().get(player.getUniqueId());

		if (caster.isCasting(name))
		{
			caster.getPlayer().removePotionEffect(PotionEffectType.INVISIBILITY);
			caster.setEffect("Invisible", 0);

			for (Player p : caster.getPlayer().getServer().getOnlinePlayers())
			{
				p.showPlayer(player);

				if (caster.getPlayer().getNearbyEntities(range, range, range).contains(p))
				{
					p.sendMessage(header + ChatColor.WHITE + " " + caster.getPlayer().getName() + ChatColor.GRAY + " Has " + ChatColor.WHITE + "Reappeared" + ChatColor.GRAY + "!");
				}
			}

			caster.getPlayer().sendMessage(header + ChatColor.WHITE + " You" + ChatColor.GRAY + " Are Now " + ChatColor.WHITE + "Visible" + ChatColor.GRAY + "!");
			caster.setCasting(name, false);
		}
	}

	@EventHandler
	public void onEntityDamageEvent(EntityDamageEvent event)
	{
		if (event.getEntity() instanceof Player)
		{
			cancelInvisibility((Player) event.getEntity()); // TODO: Check This.
		}
	}
}
