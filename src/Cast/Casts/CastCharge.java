package Cast.Casts;

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

import Cast.CommandInterface;
import Cast.Main;
import Cast.Casts.Types.TargettedCast;
import Cast.Essentials.Caster;
import Cast.Essentials.Effects.Stun;
import net.md_5.bungee.api.ChatColor;

public class CastCharge extends TargettedCast implements CommandInterface, Listener
{
	private Stun stun = new Stun();
	private double damage;
	private int range;

	public CastCharge(String name)
	{
		super(name);

		stun.setDuration(Main.getConfigCasts(), "Charge.Stun");
		damage = Main.getConfigCasts().getDouble("Charge.Damage");
		range = Main.getConfigCasts().getInt("Charge.Range");

		info.add(ChatColor.DARK_AQUA + "Stun: " + ChatColor.GRAY + stun.getDuration() / 20.0 + " Seconds");
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

			if (args.length > 1)
			{
				pages.display(player, args, 2);

				return true;
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

							double x = target.getLocation().getX() - player.getLocation().getX();
							double z = target.getLocation().getZ() - player.getLocation().getZ();

							Vector v = new Vector(x / (range / 2), 0.5, z / (range / 2));
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

											stun.start(Main.getInstance(), target);

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
