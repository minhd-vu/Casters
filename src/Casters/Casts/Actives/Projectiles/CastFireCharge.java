package Casters.Casts.Actives.Projectiles;

import Casters.Casters;
import Casters.CommandInterface;
import Casters.Essentials.Caster;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class CastFireCharge extends Projectile implements CommandInterface, Listener
{
	private int timer;
	private double damage;
	private boolean gravity;
	private int firechargefireticks;
	private int targetfireticks;
	private int areaofeffect;
	private boolean singletarget;
	private double velocity;

	public CastFireCharge(String name, String description)
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
		gravity = false;
		firechargefireticks = 100;
		targetfireticks = 50;
		areaofeffect = 3;
		singletarget = false;
		velocity = 1.0;

		info.add(ChatColor.DARK_AQUA + "Damage: " + ChatColor.GRAY + damage + " HP");
		info.add(ChatColor.DARK_AQUA + "FireTicks: " + ChatColor.GRAY + targetfireticks / 20.0 + " Seconds");

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

							SmallFireball firecharge = (SmallFireball) player.getWorld().spawnEntity(player.getEyeLocation(), EntityType.SMALL_FIREBALL);
							firecharge.setShooter(player);
							firecharge.setVelocity(caster.getPlayer().getEyeLocation().getDirection().normalize().multiply(velocity));
							firecharge.setFireTicks(firechargefireticks);
							firecharge.setGravity(gravity);

							projectiles.add(firecharge.getUniqueId());

							cast(player);

							player.getWorld().spigot().playEffect(player.getLocation(), Effect.BLAZE_SHOOT);

							caster.setCasting(name, false);

							new BukkitRunnable()
							{
								@Override
								public void run()
								{
									if (!firecharge.isDead())
									{
										projectiles.remove(firecharge.getUniqueId());
										firecharge.remove();
									}
								}

							}.runTaskLater(Casters.getInstance(), timer);
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
			SmallFireball firecharge = (SmallFireball) event.getDamager();

			if (projectiles.contains(firecharge.getUniqueId()) && firecharge.getShooter() instanceof Player)
			{
				Caster caster = Casters.getCasters().get(((Player) firecharge.getShooter()).getUniqueId());

				List<Entity> entities = firecharge.getNearbyEntities(areaofeffect, areaofeffect, areaofeffect);

				event.setCancelled(true);

				for (Entity target : entities)
				{
					if (target instanceof LivingEntity && !target.equals(firecharge.getShooter()))
					{
						if (!caster.sameParty(target))
						{
							((LivingEntity) target).damage(damage);
							target.setFireTicks(targetfireticks);

							target.getWorld().spigot().playEffect(target.getLocation().add(0.0D, 0.5D, 0.0D),
									Effect.FLAME, 0, 0, 0.2F, 0.2F, 0.2F, 0.1F, 50, 16);
							target.getWorld().playSound(target.getLocation(), Sound.BLOCK_FIRE_AMBIENT, 8.0F, 1.0F);
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
