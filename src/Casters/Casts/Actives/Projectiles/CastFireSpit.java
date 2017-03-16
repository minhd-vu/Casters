package Casters.Casts.Actives.Projectiles;

import Casters.Casters;
import Casters.CommandInterface;
import Casters.Essentials.Caster;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class CastFireSpit extends Projectile implements CommandInterface
{
	private int timer;
	private double damage;
	private double velocity;
	private boolean gravity;
	private int areaofeffect;
	private boolean singletarget;
	private int fireticks;

	public CastFireSpit(String name, String description)
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
		fireticks = 50;

		info.add(ChatColor.DARK_AQUA + "Damage: " + ChatColor.GRAY + damage + " HP");
		info.add(ChatColor.DARK_AQUA + "FireTicks: " + ChatColor.GRAY + fireticks / 20.0 + " Seconds");

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

							SmallFireball firespit = (SmallFireball) player.getWorld().spawnEntity(player.getEyeLocation(), EntityType.SMALL_FIREBALL);
							firespit.setShooter(player);
							firespit.setVelocity(caster.getPlayer().getEyeLocation().getDirection().normalize().multiply(velocity)); // TODO: Test With Kuro.
							firespit.setGravity(gravity);
							firespit.setShooter(player);

							projectiles.add(firespit.getUniqueId());

							new BukkitRunnable()
							{
								@SuppressWarnings("deprecation")
								@Override
								public void run()
								{
									if (!firespit.isDead())
									{
										player.getWorld().spigot().playEffect(firespit.getLocation().add(0, 0.2, 0), Effect.COLOURED_DUST, 0, 0, 0.2F, 0.2F, 0.2F, 0, 4, 128);
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
									if (!firespit.isDead())
									{
										projectiles.remove(firespit.getUniqueId());
										firespit.remove();
									}
								}

							}.runTaskLater(Casters.getInstance(),  timer);

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
		if (event.getDamager() instanceof SmallFireball)
		{
			SmallFireball firespit = (SmallFireball) event.getDamager();

			if (projectiles.contains(firespit.getUniqueId()) && firespit.getShooter() instanceof Player)
			{
				Caster caster = Casters.getCasters().get(((Player) firespit.getShooter()).getUniqueId());

				event.setCancelled(true);

				List<Entity> entities = firespit.getNearbyEntities(areaofeffect, areaofeffect, areaofeffect);

				for (Entity target : entities)
				{
					if (target instanceof LivingEntity && !target.equals(firespit.getShooter()))
					{
						if (!caster.sameParty(target))
						{
							((LivingEntity) target).damage(damage);
							target.setFireTicks(fireticks);

							target.getWorld().spigot().playEffect(target.getLocation().add(0.0D, 0.5D, 0.0D), Effect.FLAME, 0, 0, 0.2F, 0.2F, 0.2F, 0.1F, 50, 16);
							target.getWorld().playSound(target.getLocation(), Sound.BLOCK_FIRE_AMBIENT, 7.0F, 1.0F);
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
