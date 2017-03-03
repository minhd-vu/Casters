package Cast.Casts;

import org.bukkit.Effect;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import Cast.CommandInterface;
import Cast.Main;
import Cast.Casts.Types.ActiveCast;
import Cast.Essentials.Caster;
import net.md_5.bungee.api.ChatColor;

public class CastVanish extends ActiveCast implements CommandInterface, Listener
{
	private int duration;
	private int range;

	private BukkitTask reappeartask;

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
			Caster caster = Main.getCasters().get(player.getUniqueId());

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
						caster.setCasting(name, true);
						caster.setEffect("Invisible", duration);
						caster.setMana(manacost);

						player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, duration, 0));

						for (Player p : player.getServer().getOnlinePlayers())
						{
							// p.hidePlayer(player);

							if (player.getNearbyEntities(range, range, range).contains(p))
							{
								p.sendMessage(header + ChatColor.WHITE + " Someone" + ChatColor.GRAY + " Has "
										+ ChatColor.WHITE + "Vanished" + ChatColor.GRAY + "!");
							}
						}

						player.getWorld().playEffect(player.getLocation(), Effect.SMOKE, 4);

						player.sendMessage(header + ChatColor.WHITE + " You" + ChatColor.GRAY + " Have "
								+ ChatColor.WHITE + "Vanished" + ChatColor.GRAY + "!");

						cooldown.start(player.getName());

						reappeartask = new BukkitRunnable()
						{
							@Override
							public void run()
							{
								player.removePotionEffect(PotionEffectType.INVISIBILITY);

								for (Player p : player.getServer().getOnlinePlayers())
								{

									// p.showPlayer(player);

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

	private void cancelInvisibility(Player player)
	{
		Caster caster = Main.getCasters().get(player.getUniqueId());

		if (caster.hasEffect("Invisible"))
		{
			caster.getPlayer().removePotionEffect(PotionEffectType.INVISIBILITY);
			caster.setEffect("Invisible", 0);

			for (Player p : caster.getPlayer().getServer().getOnlinePlayers())
			{
				// p.showPlayer((Player) event.getEntity());

				if (caster.getPlayer().getNearbyEntities(range, range, range).contains(p))
				{
					p.sendMessage(header + ChatColor.WHITE + " " + caster.getPlayer().getName() + ChatColor.GRAY
							+ " Has " + ChatColor.WHITE + "Reappeared" + ChatColor.GRAY + "!");
				}
			}

			caster.getPlayer().sendMessage(header + ChatColor.WHITE + " You" + ChatColor.GRAY + " Are Now "
					+ ChatColor.WHITE + "Visible" + ChatColor.GRAY + "!");

			caster.setCasting(name, false);

			reappeartask.cancel();
		}
	}

	@EventHandler
	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event)
	{
		if (event.getEntity() instanceof Player)
		{
			cancelInvisibility((Player) event.getEntity());
		}

		if (event.getDamager() instanceof Player)
		{
			cancelInvisibility((Player) event.getDamager());
		}
	}
}
