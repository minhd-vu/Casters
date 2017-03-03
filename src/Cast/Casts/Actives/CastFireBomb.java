package Cast.Casts.Actives;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LargeFireball;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;

import Cast.CommandInterface;
import Cast.Main;
import Cast.Casts.Types.ActiveCast;
import Cast.Essentials.Caster;
import net.md_5.bungee.api.ChatColor;

public class CastFireBomb extends ActiveCast implements CommandInterface, Listener
{
	private static List<LargeFireball> firebombs = new ArrayList<LargeFireball>();

	private static double seconds;
	private static double damage;
	private static boolean gravity;
	private static int firebombfireticks;
	private static int targetfireticks;
	private static int areaofeffect;
	private static int explosion;
	private static boolean incendiary;
	private static boolean singletarget;

	public CastFireBomb(String name, String description)
	{
		super(name, description);

		warmup.setDuration(0);
		warmup.setAmplifier(0);
		cooldown.setCooldown(40);
		manacost = 3;

		info.add(ChatColor.DARK_AQUA + "WarmUp: " + ChatColor.GRAY + warmup.getDuration() / 20.0 + " Seconds");
		info.add(ChatColor.DARK_AQUA + "Cooldown: " + ChatColor.GRAY + cooldown.getCooldown() / 20.0 + " Seconds");
		info.add(ChatColor.DARK_AQUA + "Cost: " + ChatColor.GRAY + manacost + " MP");

		seconds = 5;
		damage = 10;
		gravity = false;
		firebombfireticks = 100;
		targetfireticks = 50;
		areaofeffect = 5;
		explosion = 3;
		incendiary = true;
		singletarget = false;

		info.add(ChatColor.DARK_AQUA + "Damage: " + ChatColor.GRAY + damage + " HP");
		info.add(ChatColor.DARK_AQUA + "FireTicks: " + ChatColor.GRAY + targetfireticks / 20);

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
				warmup.start(caster, name);

				new BukkitRunnable()
				{
					@SuppressWarnings("deprecation")
					@Override
					public void run()
					{
						caster.setCasting(name, true);
						caster.setMana(manacost);

						LargeFireball firebomb = player.launchProjectile(LargeFireball.class);
						firebomb.setFireTicks(firebombfireticks);
						firebomb.setGravity(gravity);
						firebomb.setShooter(player);
						firebombs.add(firebomb);

						cast(player);

						player.getWorld().spigot().playEffect(player.getLocation(), Effect.BLAZE_SHOOT);

						cooldown.start(player.getName());

						caster.setCasting(name, false);

						new BukkitRunnable()
						{
							@Override
							public void run()
							{
								if (!firebomb.isDead())
								{
									firebombs.remove(firebomb);
									firebomb.remove();
								}
							}

						}.runTaskLater(Main.getInstance(), (long) (seconds * 20));
					}

				}.runTaskLater(Main.getInstance(), warmup.getDuration());
			}
		}

		return true;
	}

	/*-@SuppressWarnings("deprecation")
	@EventHandler
	public void onProjectileHit(ProjectileHitEvent event)
	{
		Projectile projectile = event.getEntity();
	
		if (projectile instanceof LargeFireball && firebombs.contains(projectile))
		{
			LargeFireball firebomb = (LargeFireball) projectile;
	
			List<Entity> e = firebomb.getNearbyEntities(areaofeffect, areaofeffect, areaofeffect);
	
			for (Entity target : e)
			{
				if (firebomb.getShooter() instanceof Player && !target.equals(firebomb.getShooter()))
				{
					if (target instanceof LivingEntity)
					{
						((Damageable) target).damage(damage);
						target.setFireTicks(targetfireticks);
	
						target.getWorld().spigot().playEffect(target.getLocation().add(0.0D, 0.5D, 0.0D), Effect.FLAME,
								0, 0, 0.2F, 0.2F, 0.2F, 0.1F, 50, 16);
						target.getWorld().playSound(target.getLocation(), Sound.BLOCK_FIRE_AMBIENT, 7.0F, 1.0F);
	
						firebombs.remove(firebomb);
						firebomb.remove();
	
						if (singletarget)
						{
							return;
						}
					}
				}
			}
	
			if (explosion > 0)
			{
				firebomb.getWorld().createExplosion(firebomb.getLocation(), explosion, incendiary);
	
				return;
			}
		}
	}*/

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event)
	{
		if (event.getDamager() instanceof LargeFireball)
		{
			LargeFireball firebomb = (LargeFireball) event.getDamager();

			if (firebombs.contains(firebomb))
			{
				List<Entity> entities = firebomb.getNearbyEntities(areaofeffect, areaofeffect, areaofeffect);

				for (Entity target : entities)
				{
					if (firebomb.getShooter() instanceof Player && !target.equals(firebomb.getShooter()))
					{
						if (target instanceof LivingEntity)
						{
							event.setDamage(damage);
							target.setFireTicks(targetfireticks);

							target.getWorld().spigot().playEffect(target.getLocation().add(0.0D, 0.5D, 0.0D),
									Effect.FLAME, 0, 0, 0.2F, 0.2F, 0.2F, 0.1F, 50, 16);
							target.getWorld().playSound(target.getLocation(), Sound.BLOCK_FIRE_AMBIENT, 7.0F, 1.0F);

							firebombs.remove(firebomb);
							firebomb.remove();

							if (singletarget)
							{
								return;
							}
						}
					}
				}

				if (explosion > 0)
				{
					firebomb.getWorld().createExplosion(firebomb.getLocation(), explosion, incendiary);

					return;
				}
			}
		}
	}
}
