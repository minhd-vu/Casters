package Cast.Casts.Targetted;

import Cast.Casts.Types.TargettedCast;
import Cast.CommandInterface;
import Cast.Essentials.Caster;
import Cast.Essentials.Effects.Stun;
import Cast.Main;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class CastCharge extends TargettedCast implements CommandInterface, Listener
{
	private Stun stun;

	private int duration;
	private double damage;
	private int range;

	public CastCharge(String name, String description)
	{
		super(name, description);

		warmup.setDuration(40);
		warmup.setAmplifier(5);
		cooldown.setCooldown(100);
		manacost = 3;

		info.add(ChatColor.DARK_AQUA + "WarmUp: " + ChatColor.GRAY + warmup.getDuration() / 20.0 + " Seconds");
		info.add(ChatColor.DARK_AQUA + "Cooldown: " + ChatColor.GRAY + cooldown.getCooldown() / 20.0 + " Seconds");
		info.add(ChatColor.DARK_AQUA + "Cost: " + ChatColor.GRAY + manacost + " MP");

		duration = 60;
		damage = 4;
		range = 8;

		stun = new Stun(duration);

		info.add(ChatColor.DARK_AQUA + "Stun: " + ChatColor.GRAY + duration / 20.0 + " Seconds");
		info.add(ChatColor.DARK_AQUA + "Damage: " + ChatColor.GRAY + damage + " HP");
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
				LivingEntity target = getTarget(player, range, false, false);

				if (target != null && !target.equals(player))
				{
					warmup.start(caster, target, name);

					new BukkitRunnable()
					{
						@SuppressWarnings("deprecation")
						@Override
						public void run()
						{
							caster.setCasting(name, true);
							caster.setMana(manacost);

							double vertical;

							if (player.leaveVehicle())
							{
								vertical = 0.0;
							}
							else
							{
								vertical = 0.5;
							}

							double x = target.getLocation().getX() - player.getLocation().getX();
							double z = target.getLocation().getZ() - player.getLocation().getZ();

							Vector v = new Vector(x / (range / 2), vertical, z / (range / 2));

							player.setVelocity(v);

							player.getWorld().spigot().playEffect(player.getLocation(), Effect.CLOUD, 0, 0, 0.0F, 0.1F,
									0.0F, 0.5F, 25, 12);
							player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDERDRAGON_FLAP, 8.0F,
									1.0F);

							new BukkitRunnable()
							{
								@Override
								public void run()
								{
									for (Entity e : player.getNearbyEntities(3F, 3F, 3F))
									{
										if (e.equals(target))
										{
											target.damage(damage);
											caster.setBossBarEntity(target);

											stun.start(target);

											break;
										}
									}

									caster.setCasting(name, false);
								}

							}.runTaskLater(Main.getInstance(), (long) (range * 1.25));

							cast(player, target);

							cooldown.start(player.getName());
						}

					}.runTaskLater(Main.getInstance(), warmup.getDuration());
				}
			}
		}

		return true;
	}
}
