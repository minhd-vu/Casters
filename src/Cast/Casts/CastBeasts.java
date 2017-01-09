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

	public CastBeasts(String name)
	{
		super(name);

		duration = Main.getConfigCasts().getDouble("Beasts.Duration");
		amount = Main.getConfigCasts().getInt("Beasts.Amount");
		range = Main.getConfigCasts().getInt("Beasts.Range");

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

			if (args.length > 1)
			{
				pages.display(player, args, 2);

				return true;
			}

			if (caster.hasCast(name) && !caster.isCasting(name) && !caster.isWarmingUp() && !caster.isSilenced(name)
					&& !caster.isStunned(name) && !cooldown.hasCooldown(player, name) && caster.hasMana(manacost, name))
			{
				LivingEntity target = getTarget(player, range);

				if (target != null && !target.equals(player) && !(target instanceof Wolf))
				{
					if (warmup.getDuration() > 0)
					{
						warmup.start(Main.getInstance(), caster, target, name);
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
