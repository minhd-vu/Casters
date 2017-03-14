package Cast.Casts.Actives;

import Cast.CommandInterface;
import Cast.Essentials.Caster;
import Cast.Main;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class CastLightningStorm extends Active implements CommandInterface, Listener
{
	private double damage;
	private int range;
	private int explosion;
	private boolean explode;
	private boolean incendiary;

	public CastLightningStorm(String name, String description)
	{
		super(name, description);

		warmup.setDuration(0);
		warmup.setAmplifier(0);
		cooldown.setCooldown(40);
		manacost = 3;

		info.add(ChatColor.DARK_AQUA + "WarmUp: " + ChatColor.GRAY + warmup.getDuration() / 20.0 + " Seconds");
		info.add(ChatColor.DARK_AQUA + "Cooldown: " + ChatColor.GRAY + cooldown.getCooldown() / 20.0 + " Seconds");
		info.add(ChatColor.DARK_AQUA + "Cost: " + ChatColor.GRAY + manacost + " MP");

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
			else if (args.length == 1 && caster.canCast(name, cooldown, manacost))
			{
				List<Entity> targets = player.getNearbyEntities(range, range, range);

				warmup.start(caster, name);

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
									((LivingEntity) target).damage(damage);
									caster.setBossBarEntity((LivingEntity) target);

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
