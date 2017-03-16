package Casters.Casts.Actives;

import Casters.Casters;
import Casters.CommandInterface;
import Casters.Essentials.Caster;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class CastTrample extends Active implements CommandInterface, Listener
{
	private double damage;
	private int range;

	public CastTrample(String name, String description)
	{
		super(name, description);

		warmup.setDuration(40);
		warmup.setAmplifier(5);
		cooldown.setCooldown(100);
		manacost = 3;

		info.add(ChatColor.DARK_AQUA + "WarmUp: " + ChatColor.GRAY + warmup.getDuration() / 20.0 + " Seconds");
		info.add(ChatColor.DARK_AQUA + "Cooldown: " + ChatColor.GRAY + cooldown.getCooldown() / 20.0 + " Seconds");
		info.add(ChatColor.DARK_AQUA + "Cost: " + ChatColor.GRAY + manacost + " MP");

		damage = 5;
		range = 4;

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
			Caster caster = Casters.getCasters().get(player.getUniqueId());

			if (args.length == 2 && args[1].equalsIgnoreCase("info"))
			{
				pages.display(player, args, 2);

				return true;
			}

			else if (args.length == 1 && caster.canCast(name, cooldown, manacost))
			{
				warmup.start(caster, name);

				new BukkitRunnable()
				{
					@SuppressWarnings("deprecation")
					@Override
					public void run()
					{
						if (!caster.isInterrupted())
						{
							if (!player.isInsideVehicle() && !(player.getVehicle() instanceof Horse))
							{
								player.sendMessage(header + ChatColor.GRAY + " You Must Be On A " + ChatColor.WHITE + "Horse" + ChatColor.GRAY + " To Cast This!");
								return;
							}

							caster.setCasting(name, true);
							caster.setMana(manacost);

							List<Entity> entities = player.getNearbyEntities(range, range, range);

							entities.remove(player.getVehicle());

							for (Entity entity : entities)
							{
								if(entity instanceof LivingEntity && !caster.sameParty(entity))
								{
									((LivingEntity) entity).damage(damage);
									caster.setBossBarEntity((Damageable) entity);
									player.getWorld().spawnParticle(Particle.FALLING_DUST, entity.getLocation(), 100, 0.5, 1.0, 0.5);
								}
							}

							player.getWorld().playSound(player.getLocation(), Sound.ENTITY_HORSE_GALLOP, 8.0F, 1.0F);
							player.getWorld().playSound(player.getLocation(), Sound.ENTITY_HORSE_BREATHE, 8.0F, 1.0F);

							cast(player);

							caster.setCasting(name, false);
						}

						cooldown.start(player.getName());
					}

				}.runTaskLater(Casters.getInstance(), warmup.getDuration());
			}
		}

		return true;
	}
}
