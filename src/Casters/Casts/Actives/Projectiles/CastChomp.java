package Casters.Casts.Actives.Projectiles;

import Casters.Casters;
import Casters.CommandInterface;
import Casters.Essentials.Caster;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BlockIterator;

public class CastChomp extends Projectile implements CommandInterface
{
	private double range;
	private double damage;

	public CastChomp(String name, String description)
	{
		super(name, description);

		warmup.setDuration(0);
		warmup.setAmplifier(0);
		cooldown.setCooldown(100);
		manacost = 5;

		info.add(ChatColor.DARK_AQUA + "WarmUp: " + ChatColor.GRAY + warmup.getDuration() / 20.0 + " Seconds");
		info.add(ChatColor.DARK_AQUA + "Cooldown: " + ChatColor.GRAY + cooldown.getCooldown() / 20.0 + " Seconds");
		info.add(ChatColor.DARK_AQUA + "Cost: " + ChatColor.GRAY + manacost + " MP");

		range = 10;
		damage = 10;

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
							caster.setCasting(name, true);
							caster.setMana(manacost);

							BlockIterator blockiterator = new BlockIterator((LivingEntity) player, (int) range);

							new BukkitRunnable()
							{
								@Override
								public void run()
								{
									if (blockiterator.hasNext())
									{
										Block block = blockiterator.next();

										EvokerFangs chomp = ((EvokerFangs) player.getWorld().spawnEntity(block.getLocation(), EntityType.EVOKER_FANGS));
										chomp.setOwner(player);

										projectiles.add(chomp.getUniqueId());

										if (block.getType().isSolid())
										{
											cancel();
											return;
										}
									}

									cancel();
								}
							}.runTaskTimer(Casters.getInstance(), 0, 1);

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

	@EventHandler
	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event)
	{
		if (event.getDamager() instanceof EvokerFangs)
		{
			EvokerFangs chomp = (EvokerFangs) event.getDamager();

			if (projectiles.contains(chomp.getUniqueId()) && chomp.getOwner() instanceof Player && event.getEntity() instanceof LivingEntity)
			{
				Caster caster = Casters.getCasters().get(chomp.getOwner().getUniqueId());
				LivingEntity target = (LivingEntity) event.getEntity();

				event.setCancelled(true);

				if (!caster.sameParty(target))
				{
					target.damage(damage);
					caster.setBossBarEntity((Damageable) event.getEntity());
				}
			}
		}
	}
}
