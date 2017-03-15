package Casters.Wands;

import Casters.CommandInterface;
import Casters.Essentials.Caster;
import Casters.Casters;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class WandInferno extends Wand implements CommandInterface, Listener
{
	private List<SmallFireball> fireballs = new ArrayList<SmallFireball>();

	private int targetfireticks;
	private int explosion;

	public WandInferno(String name)
	{
		super(name);

		warmup.setDuration(0);
		warmup.setAmplifier(0);
		cooldown.setCooldown(20);
		timer = 100;
		damage = 2;
		manacost = 1;
		gravity = false;
		areaofeffect = 1;
		singletarget = true;
		incendiary = false;
		targetfireticks = 1;
		explosion = 0;

		info.add(ChatColor.DARK_AQUA + name + " Wand:");
		info.add(ChatColor.DARK_AQUA + "Cost: " + ChatColor.GRAY + manacost + " MP.");
		info.add(ChatColor.DARK_AQUA + "Damage: " + ChatColor.GRAY + damage + " HP.");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if (sender instanceof Player)
		{
			Player player = (Player) sender;

			pages.display(player, args, 3);
		}

		return true;
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event)
	{
		if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
		{
			Player player = event.getPlayer();
			Caster caster = Casters.getCasters().get(player.getUniqueId());

			material = player.getInventory().getItemInMainHand().getType();

			if (caster.getType().getName().equalsIgnoreCase(name) && !cooldown.hasCooldown(player.getName())
					&& !caster.isCasting(name) && !caster.isWarmingUp() && !caster.isSilenced(name)
					&& !caster.isStunned(name) && caster.hasMana(manacost, name))
			{
				if (player.getInventory().getItemInMainHand().getType().equals(Material.BLAZE_ROD))
				{
					caster.setCasting(name, true);
					caster.setMana(manacost);

					SmallFireball fireball = player.launchProjectile(SmallFireball.class);
					fireball.setShooter(player);
					fireball.setGravity(gravity);
					fireball.setIsIncendiary(incendiary);

					fireballs.add(fireball);

					player.getWorld().spigot().playEffect(player.getLocation(), Effect.BLAZE_SHOOT);

					cooldown.start(player.getName());

					caster.setCasting(name, false);

					new BukkitRunnable()
					{
						@Override
						public void run()
						{
							if (!fireball.isDead())
							{
								player.getWorld().spigot().playEffect(fireball.getLocation().add(0, 0.2, 0),
										Effect.COLOURED_DUST, 0, 0, 0.2F, 0.2F, 0.2F, 0, 4, 128);
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
							if (!fireball.isDead())
							{
								fireballs.remove(fireball);
								fireball.remove();
							}
						}

					}.runTaskLater(Casters.getInstance(), (long) timer);
				}
			}
		}
	}

	/*-@SuppressWarnings("deprecation")
	@EventHandler
	public void onProjectileHit(ProjectileHitEvent event)
	{
		Projectile projectile = event.getEntity();
	
		if (projectile instanceof SmallFireball && fireballs.contains(projectile))
		{
			SmallFireball fireball = (SmallFireball) projectile;
	
			List<Entity> e = fireball.getNearbyEntities(areaofeffect, areaofeffect, areaofeffect);
	
			for (Entity target : e)
			{
				if (fireball.getShooter() instanceof Player && !target.equals(fireball.getShooter()))
				{
					if (target instanceof LivingEntity)
					{
						((Damageable) target).damage(damage);
	
						target.getWorld().spigot().playEffect(target.getLocation().add(0.0D, 1.0D, 0.0D), Effect.FLAME,
								0, 0, 0.2F, 0.2F, 0.2F, 0.1F, 50, 16);
						target.getWorld().playSound(target.getLocation(), Sound.ENTITY_BLAZE_HURT, 8.0F, 1.0F);
	
						fireballs.remove(fireball);
						fireball.remove();
	
						if (singletarget)
						{
							return;
						}
					}
				}
			}
		}
	}*/

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event)
	{
		if (event.getDamager() instanceof Fireball)
		{
			Fireball fireball = (Fireball) event.getDamager();

			if (fireballs.contains(fireball))
			{
				List<Entity> entities = fireball.getNearbyEntities(areaofeffect, areaofeffect, areaofeffect);

				for (Entity target : entities)
				{
					if (fireball.getShooter() instanceof Player && !target.equals(fireball.getShooter()))
					{
						if (target instanceof LivingEntity)
						{
							event.setDamage(damage);
							target.setFireTicks(targetfireticks);

							target.getWorld().spigot().playEffect(target.getLocation().add(0.0D, 0.5D, 0.0D),
									Effect.FLAME, 0, 0, 0.2F, 0.2F, 0.2F, 0.1F, 50, 16);
							target.getWorld().playSound(target.getLocation(), Sound.BLOCK_FIRE_AMBIENT, 7.0F, 1.0F);

							fireballs.remove(fireball);
							fireball.remove();

							if (singletarget)
							{
								return;
							}
						}
					}
				}

				if (explosion > 0)
				{
					fireball.getWorld().createExplosion(fireball.getLocation(), explosion, incendiary);

					return;
				}
			}
		}
	}
}
