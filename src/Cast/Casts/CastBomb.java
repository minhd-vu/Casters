package Cast.Casts;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import Cast.CommandInterface;
import Cast.Main;
import Cast.Casts.Types.TargettedCast;
import Cast.Essentials.Caster;
import net.md_5.bungee.api.ChatColor;

public class CastBomb extends TargettedCast implements CommandInterface, Listener
{
	private int range;
	private int fuse;
	private int velocity;
	private boolean gravity;
	private boolean incendiary;

	public CastBomb(String name)
	{
		super(name);

		gravity = Main.getConfigCasts().getBoolean("Bomb.Gravity");
		range = Main.getConfigCasts().getInt("Bomb.Range");
		fuse = Main.getConfigCasts().getInt("Bomb.Fuse");
		velocity = Main.getConfigCasts().getInt("Bomb.Velocity");
		incendiary = Main.getConfigCasts().getBoolean("Bomb.Incendiary");

		info.add(ChatColor.DARK_AQUA + "Range: " + ChatColor.GRAY + range + " Blocks");
		info.add(ChatColor.DARK_AQUA + "Fuse: " + ChatColor.GRAY + fuse / 20.0 + " Seconds");

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
			}

			if (caster.hasCast(name) && !caster.isCasting(name) && !caster.isWarmingUp() && !caster.isSilenced(name)
					&& !caster.isStunned(name) && !cooldown.hasCooldown(player, name) && caster.hasMana(manacost, name))
			{
				LivingEntity target = getTarget(player, range);

				if (target != null && !target.equals(player))
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

							TNTPrimed tnt = null;

							if (velocity > 0)
							{
								tnt = (TNTPrimed) player.getWorld().spawn(player.getLocation(), TNTPrimed.class);
								tnt.setVelocity(player.getLocation().getDirection().normalize().multiply(velocity));
							}

							else
							{
								tnt = (TNTPrimed) player.getWorld().spawn(target.getLocation(), TNTPrimed.class);
							}

							tnt.setGravity(gravity);
							tnt.setFuseTicks(fuse);
							tnt.setIsIncendiary(incendiary);

							cast(player, target);

							cooldown.start(player.getName());

							caster.setCasting(name, false);
						}

					}.runTaskLater(Main.getInstance(), warmup.getDuration());
				}
			}
		}

		return true;
	}
}
