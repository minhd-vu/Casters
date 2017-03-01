package Cast.Casts;

import java.util.List;

import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import Cast.CommandInterface;
import Cast.Main;
import Cast.Casts.Types.ActiveCast;
import Cast.Essentials.Caster;
import net.md_5.bungee.api.ChatColor;

public class CastRevive extends ActiveCast implements CommandInterface, Listener
{
	private int range;
	private int percentage;

	public CastRevive(String name, String description)
	{
		super(name, description);

		warmup.setDuration(0);
		warmup.setAmplifier(0);
		cooldown.setCooldown(40);
		manacost = 3;

		info.add(ChatColor.DARK_AQUA + "WarmUp: " + ChatColor.GRAY + warmup.getDuration() / 20.0 + " Seconds");
		info.add(ChatColor.DARK_AQUA + "Cooldown: " + ChatColor.GRAY + cooldown.getCooldown() / 20.0 + " Seconds");
		info.add(ChatColor.DARK_AQUA + "Cost: " + ChatColor.GRAY + manacost + " MP");

		range = 32;
		percentage = 100;

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

				List<Entity> e = player.getNearbyEntities(range, range, range);

				for (Entity t : e)
				{
					if (t instanceof Player && t.getName().equalsIgnoreCase(args[1]))
					{
						Player target = (Player) t;

						if (target.isDead())
						{
							if (caster.hasCast(name) && !caster.isCasting(name) && !caster.isWarmingUp()
									&& !caster.isSilenced(name) && !caster.isStunned(name)
									&& !cooldown.hasCooldown(player, name) && caster.hasMana(manacost, name))
							{
								if (warmup.getDuration() > 0)
								{
									warmup.start(caster, target, name);
								}

								new BukkitRunnable()
								{
									@SuppressWarnings("deprecation")
									@Override
									public void run()
									{
										caster.setCasting(name, true);
										caster.setMana(manacost);

										target.setHealth(target.getMaxHealth() * (percentage / 100));
										target.getWorld().spigot().playEffect(target.getLocation(), Effect.HEART, 0, 0,
												0.2F, 0.2F, 0.2F, 0.1F, 50, 16);
										target.getWorld().playSound(target.getLocation(), Sound.BLOCK_PORTAL_AMBIENT,
												8.0F, 1.0F);

										cast(player, target);

										cooldown.start(player.getName());

										caster.setCasting(name, false);
									}

								}.runTaskLater(Main.getInstance(), warmup.getDuration());
							}
						}

						else
						{
							player.sendMessage(target + " Is Not Dead!");
						}

						break;
					}
				}

				return true;
			}

			player.sendMessage("Correct Usage: /Cast Revive <Player>");
		}

		return false;
	}
}
