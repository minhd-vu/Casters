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

	public CastSiphon(String name, String description)
	{
		super(name, description);

		warmup.setDuration(0);
		warmup.setAmplifier(0);
		cooldown.setCooldown(40);
		manacost = 3;

		info.add(ChatColor.DARK_AQUA + "WarmUp: " + ChatColor.GRAY + warmup.getDuration() / 20.0 + " Seconds.");
		info.add(ChatColor.DARK_AQUA + "Cooldown: " + ChatColor.GRAY + cooldown.getCooldown() / 20.0 + " Seconds.");
		info.add(ChatColor.DARK_AQUA + "Cost: " + ChatColor.GRAY + manacost + " MP.");

		siphon.setDuration(100);
		siphon.setPeriod(20);
		siphon.setDamage(1);
		siphon.setPercentage(100);
		damage = 3;
		percentage = 50;
		range = 8;

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

			if (args.length == 2 && args[1].equalsIgnoreCase("info"))
			{
				pages.display(player, args, 2);

				return true;
			}

			else if (args.length == 1 && caster.canCast(name, cooldown, manacost))
			{
				LivingEntity target = getTarget(player, range, false);

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
								siphon.start(caster, Main.getCasters().get(target.getUniqueId()), name);
							}

							else
							{
								siphon.start(caster, target, name);
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
