package Cast.Casts;

import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import Cast.CommandInterface;
import Cast.Main;
import Cast.Casts.Types.TargettedCast;
import Cast.Essentials.Caster;
import net.md_5.bungee.api.ChatColor;

public class CastBandage extends TargettedCast implements CommandInterface, Listener
{
	private double heal;
	private int range;

	public CastBandage(String name, String description)
	{
		super(name, description);

		warmup.setDuration(0);
		warmup.setAmplifier(0);
		cooldown.setCooldown(40);
		manacost = 3;

		info.add(ChatColor.DARK_AQUA + "WarmUp: " + ChatColor.GRAY + warmup.getDuration() / 20.0 + " Seconds.");
		info.add(ChatColor.DARK_AQUA + "Cooldown: " + ChatColor.GRAY + cooldown.getCooldown() / 20.0 + " Seconds.");
		info.add(ChatColor.DARK_AQUA + "Cost: " + ChatColor.GRAY + manacost + " MP.");

		heal = 1;
		range = 8;

		info.add(ChatColor.DARK_AQUA + "Heal:" + heal + " HP");
		info.add(ChatColor.DARK_AQUA + "Range:" + range + " Blocks");

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
				LivingEntity target = getTarget(player, range);

				if (warmup.getDuration() > 0)
				{
					warmup.start(Main.getProvidingPlugin(this.getClass()), caster, target, name);
				}

				new BukkitRunnable()
				{
					@SuppressWarnings("deprecation")
					@Override
					public void run()
					{
						caster.setCasting(name, true);
						caster.setMana(manacost);

						if (target.getHealth() + heal > target.getMaxHealth())
						{
							target.setHealth(target.getMaxHealth());
						}

						else
						{
							target.setHealth(target.getHealth() + heal);
						}

						target.getWorld().spigot().playEffect(target.getLocation().add(0, 1, 0), Effect.HEART, 0, 0,
								0.5F, 0.5F, 0.5F, 0.1F, 50, 16);
						target.getWorld().playSound(target.getLocation(), Sound.BLOCK_GRAVEL_PLACE, 4.0F, 1.0F);

						cast(player, target);

						cooldown.start(player.getName());

						caster.setCasting(name, false);
					}

				}.runTaskLater(Main.getProvidingPlugin(this.getClass()), warmup.getDuration());
			}
		}

		return true;
	}
}
