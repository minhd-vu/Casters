package Cast.Casts.Targetted;

import Cast.Casts.Types.TargettedCast;
import Cast.CommandInterface;
import Cast.Essentials.Caster;
import Cast.Main;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

public class CastBomb extends TargettedCast implements CommandInterface, Listener
{
	private int range;
	private int fuse;
	private int velocity;
	private boolean gravity;
	private boolean incendiary;

	public CastBomb(String name, String description)
	{
		super(name, description);

		warmup.setDuration(20);
		warmup.setAmplifier(5);
		cooldown.setCooldown(100);
		manacost = 5;

		info.add(ChatColor.DARK_AQUA + "WarmUp: " + ChatColor.GRAY + warmup.getDuration() / 20.0 + " Seconds");
		info.add(ChatColor.DARK_AQUA + "Cooldown: " + ChatColor.GRAY + cooldown.getCooldown() / 20.0 + " Seconds");
		info.add(ChatColor.DARK_AQUA + "Cost: " + ChatColor.GRAY + manacost + " MP");

		gravity = true;
		range = 8;
		fuse = 40;
		velocity = 0;
		incendiary = true;

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

			if (args.length == 2 && args[1].equalsIgnoreCase("info"))
			{
				pages.display(player, args, 2);

				return true;
			}

			else if (args.length == 1 && caster.canCast(name, cooldown, manacost))
			{
				LivingEntity target = getTarget(player, range, false, false);

				if (target != null && !target.equals(player))
				{
					warmup.start(caster, target, name);

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
								tnt = player.getWorld().spawn(player.getLocation(), TNTPrimed.class);
								tnt.setVelocity(player.getLocation().getDirection().normalize().multiply(velocity));
							}
							else
							{
								tnt = player.getWorld().spawn(target.getLocation(), TNTPrimed.class);
							}

							tnt.setGravity(gravity);
							tnt.setFuseTicks(fuse);
							tnt.setIsIncendiary(incendiary);

							new BukkitRunnable()
							{
								@Override
								public void run()
								{
									caster.setBossBarEntity(target);
								}

							}.runTaskLater(Main.getInstance(), fuse);

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
