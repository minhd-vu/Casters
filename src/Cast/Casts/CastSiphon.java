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
import Cast.Essentials.Effects.Siphon;
import net.md_5.bungee.api.ChatColor;

public class CastSiphon extends TargettedCast implements CommandInterface, Listener
{
	private Siphon siphon = new Siphon();

	private double damage;
	private int percentage;
	private int range;

	public CastSiphon(String name)
	{
		super(name);

		siphon.setDuration(Main.getConfigCasts(), "Siphon.Duration");
		siphon.setPeriod(Main.getConfigCasts(), "Siphon.Period");
		siphon.setDamage(Main.getConfigCasts(), "Siphon.Damage.Tick");
		siphon.setPercentage(Main.getConfigCasts(), "Siphon.Percentage.Tick");
		damage = Main.getConfigCasts().getDouble("Siphon.Damage.Initial");
		percentage = Main.getConfigCasts().getInt("Siphon.Percentage.Initial");
		range = Main.getConfigCasts().getInt("Siphon.Range");

		info.add(ChatColor.DARK_AQUA + "Duration: " + ChatColor.GRAY + siphon.getDuration() / 20.0 + " Seconds");
		info.add(ChatColor.DARK_AQUA + "Siphon: " + ChatColor.GRAY + siphon.getDamage() + " HP");
		info.add(ChatColor.DARK_AQUA + "Damage: " + ChatColor.GRAY + damage + " HP");
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

							target.getWorld().spigot().playEffect(target.getLocation().add(0, 1, 0), Effect.WITCH_MAGIC,
									0, 0, 0.5F, 0.5F, 0.5F, 1.0F, 16, 16);
							target.getWorld().playSound(target.getLocation(), Sound.ENTITY_WITCH_DRINK, 8.0F, 1.0F);

							target.damage(damage);

							if (player.getHealth() + damage * (percentage / 100) > player.getMaxHealth())
							{
								player.setHealth(player.getMaxHealth());
							}

							else
							{
								player.setHealth(player.getHealth() + damage * (percentage / 100));
							}

							if (target instanceof Player)
							{
								siphon.start(Main.getInstance(), caster, Main.getCasters().get(target.getUniqueId()),
										name);
							}

							else
							{
								siphon.start(Main.getInstance(), caster, target, name);
							}

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
