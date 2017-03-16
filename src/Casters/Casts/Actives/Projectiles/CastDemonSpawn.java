package Casters.Casts.Actives.Projectiles;

import Casters.Casters;
import Casters.CommandInterface;
import Casters.Essentials.Caster;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class CastDemonSpawn extends Projectile implements CommandInterface
{
	private int timer;
	private double damage;
	private double velocity;
	private boolean gravity;
	private int areaofeffect;
	private boolean singletarget;

	public CastDemonSpawn(String name, String description)
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
		velocity = 1.0;

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

							WitherSkull demonspawn = (WitherSkull) player.getWorld().spawnEntity(player.getEyeLocation(), EntityType.WITHER_SKULL);
							demonspawn.setShooter(player);
							demonspawn.setVelocity(caster.getPlayer().getEyeLocation().getDirection().normalize().multiply(velocity)); // TODO: Test With Kuro.
							demonspawn.setGravity(gravity);
							demonspawn.setShooter(player);

							projectiles.add(demonspawn.getUniqueId());

							player.getWorld().spigot().playEffect(player.getLocation(), Effect.WITHER_SHOOT);

							new BukkitRunnable()
							{
								@SuppressWarnings("deprecation")
								@Override
								public void run()
								{
									if (!demonspawn.isDead())
									{
										player.getWorld().spigot().playEffect(demonspawn.getLocation().add(0, 0.2, 0), Effect.WITCH_MAGIC, 0, 0, 0.2F, 0.2F, 0.2F, 0, 4, 128);
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
									if (!demonspawn.isDead())
									{
										projectiles.remove(demonspawn.getUniqueId());
										demonspawn.remove();
									}
								}

							}.runTaskLater(Casters.getInstance(), timer);

							player.getWorld().playSound(player.getLocation(), Sound.ENTITY_SNOWBALL_THROW, 8.0F, 1.0F);

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
		if (event.getDamager() instanceof WitherSkull)
		{
			WitherSkull demonspawn = (WitherSkull) event.getDamager();

			if (projectiles.contains(demonspawn.getUniqueId()) && demonspawn.getShooter() instanceof Player)
			{
				Caster caster = Casters.getCasters().get(((Player) demonspawn.getShooter()).getUniqueId());

				event.setCancelled(true);

				List<Entity> entities = demonspawn.getNearbyEntities(areaofeffect, areaofeffect, areaofeffect);

				for (Entity target : entities)
				{
					if (target instanceof LivingEntity && !target.equals(demonspawn.getShooter()))
					{
						if (!caster.sameParty(target))
						{
							((LivingEntity) target).damage(damage);
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
