package Casters.Casts.Actives.Projectiles;

import Casters.Casters;
import Casters.CommandInterface;
import Casters.Essentials.Caster;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class CastArcaneShot extends Projectile implements CommandInterface
{
	private int timer;
	private double damage;
	private double velocity;
	private boolean gravity;
	private int areaofeffect;
	private boolean singletarget;

	public CastArcaneShot(String name, String description)
	{
		super(name, description);

		warmup.setDuration(0);
		warmup.setAmplifier(0);
		cooldown.setCooldown(40);
		manacost = 3;

		info.add(ChatColor.DARK_AQUA + "WarmUp: " + ChatColor.GRAY + warmup.getDuration() / 20.0 + " Seconds");
		info.add(ChatColor.DARK_AQUA + "Cooldown: " + ChatColor.GRAY + cooldown.getCooldown() / 20.0 + " Seconds");
		info.add(ChatColor.DARK_AQUA + "Cost: " + ChatColor.GRAY + manacost + " MP");

		timer = 100;
		damage = 2;
		manacost = 1;
		gravity = false;
		areaofeffect = 1;
		singletarget = true;
		velocity = 1;

		info.add(ChatColor.DARK_AQUA + "Damage: " + ChatColor.GRAY + damage + " HP");

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

							EnderPearl arcaneshot = (EnderPearl) player.getWorld().spawnEntity(player.getEyeLocation(), EntityType.ENDER_PEARL);
							arcaneshot.setShooter(player);
							arcaneshot.setVelocity(caster.getPlayer().getEyeLocation().getDirection().normalize().multiply(velocity)); // TODO: Test With Kuro.
							arcaneshot.setGravity(gravity);
							arcaneshot.setShooter(player);

							projectiles.add(arcaneshot.getUniqueId()); // TODO: Test If This Hits & Teleports.

							new BukkitRunnable()
							{
								@SuppressWarnings("deprecation")
								@Override
								public void run()
								{
									if (!arcaneshot.isDead())
									{
										player.getWorld().spigot().playEffect(arcaneshot.getLocation().add(0, 0.1, 0), Effect.MAGIC_CRIT, 0, 0, 0.2F, 0.2F, 0.2F, 0, 4, 128);
									}

									else
									{
										this.cancel();
										return;
									}
								}

							}.runTaskTimer(Casters.getInstance(), 2, 1);

							new BukkitRunnable()
							{
								@Override
								public void run()
								{
									if (!arcaneshot.isDead())
									{
										projectiles.remove(arcaneshot.getUniqueId());
										arcaneshot.remove();
									}
								}

							}.runTaskLater(Casters.getInstance(), timer);

							player.getWorld().spigot().playEffect(player.getLocation(), Effect.ENDEREYE_LAUNCH);

							caster.setCasting(name, false);
						}

						cooldown.start(player.getName());
					}

				}.runTaskLater(Casters.getInstance(), warmup.getDuration());
			}
		}

		return true;
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event)
	{
		if (event.getDamager() instanceof EnderPearl)
		{
			EnderPearl arcaneshot = (EnderPearl) event.getDamager();

			if (projectiles.contains(arcaneshot.getUniqueId()) && arcaneshot.getShooter() instanceof Player)
			{
				Caster caster = Casters.getCasters().get(((Player) arcaneshot.getShooter()).getUniqueId());

				event.setCancelled(true);

				List<Entity> entities = arcaneshot.getNearbyEntities(areaofeffect, areaofeffect, areaofeffect);

				for (Entity target : entities)
				{
					if (target instanceof LivingEntity && !target.equals(arcaneshot.getShooter()))
					{
						if (!caster.sameParty(target))
						{
							((LivingEntity) target).damage(damage);

							target.getWorld().spawnParticle(Particle.END_ROD, target.getLocation(), 50, 1.0, 1.0, 1.0);
							target.getWorld().playSound(target.getLocation(), Sound.ENTITY_ENDERDRAGON_FIREBALL_EXPLODE, 8.0F, 1.0F);
						}

						if (singletarget)
						{
							return;
						}
					}
				}
			}
		}
	}
}
