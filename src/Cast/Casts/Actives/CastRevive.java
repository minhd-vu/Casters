package Cast.Casts.Actives;

import Cast.Casts.Types.ActiveCast;
import Cast.CommandInterface;
import Cast.Essentials.Caster;
import Cast.Main;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class CastRevive extends ActiveCast implements CommandInterface, Listener
{
	private List<Death> deaths;
	private int range;
	private int percentage;
	private int timer;
	public CastRevive(String name, String description)
	{
		super(name, description);

		deaths = new ArrayList<Death>();

		warmup.setDuration(0);
		warmup.setAmplifier(0);
		cooldown.setCooldown(40);
		manacost = 3;

		info.add(ChatColor.DARK_AQUA + "WarmUp: " + ChatColor.GRAY + warmup.getDuration() / 20.0 + " Seconds");
		info.add(ChatColor.DARK_AQUA + "Cooldown: " + ChatColor.GRAY + cooldown.getCooldown() / 20.0 + " Seconds");
		info.add(ChatColor.DARK_AQUA + "Cost: " + ChatColor.GRAY + manacost + " MP");

		range = 32;
		percentage = 100;
		timer = 30000;

		info.add(ChatColor.DARK_AQUA + "Range: " + ChatColor.GRAY + range + " Blocks");
		info.add(ChatColor.DARK_AQUA + "Percentage: " + ChatColor.GRAY + percentage + "%");

		pages.setPage(info);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if (sender instanceof Player)
		{
			Player player = (Player) sender;
			Caster caster = Main.getCasters().get(player.getUniqueId());

			if (args.length == 2)
			{
				if (args[1].equalsIgnoreCase("info"))
				{
					pages.display(player, args, 2);

					return true;
				}

				if (player.getServer().getPlayer(args[1]) != null)
				{
					Player target = player.getServer().getPlayer(args[1]);

					if (caster.canCast(name, cooldown, manacost))
					{
						warmup.start(caster, target, name);

						new BukkitRunnable()
						{
							@SuppressWarnings("deprecation")
							@Override
							public void run()
							{
								for (Death death : deaths)
								{
									if (death.getPlayer().equals(target))
									{
										if (System.currentTimeMillis() - death.getTime() <= timer)
										{
											caster.setCasting(name, true);
											caster.setMana(manacost);

											target.setHealth(target.getMaxHealth() * (percentage / 100));
											target.getWorld().spigot().playEffect(target.getLocation(), Effect.HEART, 0, 0, 0.2F, 0.2F, 0.2F, 0.1F, 50, 16);
											target.getWorld().playSound(target.getLocation(), Sound.BLOCK_PORTAL_AMBIENT, 8.0F, 1.0F);

											cast(player, target);

											cooldown.start(player.getName());

											caster.setCasting(name, false);
										}

										else
										{
											caster.getPlayer().sendMessage(header + ChatColor.GRAY + " You Were Too Late. " +
													ChatColor.WHITE + target.getName() + ChatColor.GRAY + " Could Not Be" + ChatColor.WHITE + " Revived.");
										}

										break;
									}
								}
							}

						}.runTaskLater(Main.getInstance(), warmup.getDuration());
					}

					else
					{
						player.sendMessage(header + ChatColor.WHITE + " " + args[1] + ChatColor.GRAY
								+ " Must Be Dead In Order To Cast " + ChatColor.WHITE + name + ChatColor.GRAY + "!");
					}
				}

				else
				{
					player.sendMessage(header + ChatColor.WHITE + " " + args[1] + ChatColor.GRAY + " Is Not Online!");
				}

				return true;
			}

			player.sendMessage(header + ChatColor.GRAY + " Correct Usage: " + ChatColor.DARK_AQUA + "/cast"
					+ ChatColor.AQUA + " revive <player>");
		}

		return false;
	}

	@EventHandler
	public void onPlayerDeathEvent(PlayerDeathEvent event)
	{
		deaths.add(new Death(event.getEntity(), event.getEntity().getLocation(), System.currentTimeMillis()));
	}

	private class Death
	{
		private Player player;
		private Location location;
		private Long time;

		public Death(Player player, Location location, Long time)
		{
			this.player = player;
			this.location = location;
			this.time = time;
		}

		public Player getPlayer()
		{
			return player;
		}

		public Location getLocation()
		{
			return location;
		}

		public Long getTime()
		{
			return time;
		}
	}
}
