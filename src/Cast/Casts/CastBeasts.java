package Cast.Casts;

import java.util.ArrayList;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import Cast.CommandInterface;
import Cast.Main;
import Cast.Casts.Types.TargettedCast;
import Cast.Essentials.Caster;
import net.md_5.bungee.api.ChatColor;

public class CastBeasts extends TargettedCast implements CommandInterface, Listener
{
	private double duration;
	private int amount;
	private int range;

	public CastBeasts(String name, String description)
	{
		super(name, description);

		warmup.setDuration(20);
		warmup.setAmplifier(5);
		cooldown.setCooldown(100);
		manacost = 3;

		info.add(ChatColor.DARK_AQUA + "WarmUp: " + ChatColor.GRAY + warmup.getDuration() / 20.0 + " Seconds");
		info.add(ChatColor.DARK_AQUA + "Cooldown: " + ChatColor.GRAY + cooldown.getCooldown() / 20.0 + " Seconds");
		info.add(ChatColor.DARK_AQUA + "Cost: " + ChatColor.GRAY + manacost + " MP");

		duration = 0;
		amount = 3;
		range = 10;

		info.add(duration == 0 ? ChatColor.DARK_AQUA + "Duration: " + ChatColor.GRAY + "Forever"
				: ChatColor.DARK_AQUA + "Duration: " + ChatColor.GRAY + duration / 20 + " Seconds");
		info.add(ChatColor.DARK_AQUA + "Amount: " + ChatColor.GRAY + amount + " Wolves");
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
				LivingEntity target = getTarget(player, range, false);

				if (target != null && !target.equals(player) && !(target instanceof Wolf))
				{
					if (warmup.getDuration() > 0)
					{
						warmup.start(caster, target, name);
					}

					new BukkitRunnable()
					{
						@Override
						public void run()
						{
							caster.setCasting(name, true);
							caster.setMana(manacost);

							ArrayList<Wolf> wolves = new ArrayList<Wolf>();

							for (int i = 0; i < amount; ++i)
							{
								wolves.add((Wolf) player.getWorld().spawnEntity(player.getLocation(), EntityType.WOLF));
							}

							for (Wolf wolf : wolves)
							{
								wolf.setOwner(player);
								wolf.setTarget(target);
							}

							cast(player, target);

							cooldown.start(player.getName());

							caster.setCasting(name, false);

							if (duration > 0)
							{
								new BukkitRunnable()
								{
									@Override
									public void run()
									{
										for (Wolf wolf : wolves)
										{
											wolf.remove();
										}
									}

								}.runTaskLater(Main.getInstance(), (long) duration);
							}
						}

					}.runTaskLater(Main.getInstance(), warmup.getDuration());
				}
			}
		}

		return true;
	}
}
