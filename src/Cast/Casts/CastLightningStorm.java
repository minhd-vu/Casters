package Cast.Casts;

import java.util.List;

import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import Cast.CommandInterface;
import Cast.Main;
import Cast.Casts.Types.ActiveCast;
import Cast.Essentials.Caster;
import net.md_5.bungee.api.ChatColor;

public class CastLightningStorm extends ActiveCast implements CommandInterface, Listener
{
	private double damage;
	private int range;
	private int explosion;
	private boolean explode;
	private boolean incendiary;

	public CastLightningStorm(String name)
	{
		super(name);

		warmup.setDuration(0);
		warmup.setAmplifier(0);
		cooldown.setCooldown(40);
		manacost = 3;

		info.add(ChatColor.DARK_AQUA + name + " Cast:");
		info.add(ChatColor.DARK_AQUA + "WarmUp: " + ChatColor.GRAY + warmup.getDuration() / 20.0 + " Seconds.");
		info.add(ChatColor.DARK_AQUA + "Cooldown: " + ChatColor.GRAY + cooldown.getCooldown() / 20.0 + " Seconds.");
		info.add(ChatColor.DARK_AQUA + "Cost: " + ChatColor.GRAY + manacost + " MP.");

		damage = 10;
		range = 12;
		explosion = 0;
		explode = false;
		incendiary = false;

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

			else if (args.length == 1 && caster.hasCast(name) && !caster.isCasting(name) && !caster.isWarmingUp()
					&& !caster.isSilenced(name) && !caster.isStunned(name) && !cooldown.hasCooldown(player, name)
					&& caster.hasMana(manacost, name))
			{
				List<Entity> targets = player.getNearbyEntities(range, range, range);

				if (warmup.getDuration() > 0)
				{
					warmup.start(Main.getInstance(), caster, name);
				}

				if (targets.size() > 0)
				{
					for (Entity target : targets)
					{
						if (target instanceof LivingEntity && !target.equals(player))
						{
							new BukkitRunnable()
							{
								@Override
								public void run()
								{
									caster.setCasting(name, true);
									caster.setMana(manacost);

									target.getWorld().spigot().strikeLightningEffect(target.getLocation(), true);
									target.getWorld().playSound(target.getLocation(), Sound.ENTITY_LIGHTNING_THUNDER,
											1.0F, 1.0F);
									((Damageable) target).damage(damage);

									if (explode)
									{
										target.getWorld().createExplosion(target.getLocation(), explosion, incendiary);
									}

									cooldown.start(player.getName());

									caster.setCasting(name, false);
								}

							}.runTaskLater(Main.getInstance(), warmup.getDuration());
						}
					}
				}

				else
				{
					new BukkitRunnable()
					{
						@Override
						public void run()
						{
							player.sendMessage(
									header + ChatColor.WHITE + name + ChatColor.GRAY + ": No Targets In Range!");
						}

					}.runTaskLater(Main.getInstance(), warmup.getDuration());
				}

				new BukkitRunnable()
				{
					@Override
					public void run()
					{
						cast(player);
					}

				}.runTaskLater(Main.getInstance(), warmup.getDuration());
			}
		}

		return true;
	}
}
