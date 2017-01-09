package Cast.Casts;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import Cast.CommandInterface;
import Cast.Main;
import Cast.Casts.Types.ActiveCast;
import Cast.Essentials.Caster;
import net.md_5.bungee.api.ChatColor;

public class CastFireball extends ActiveCast implements CommandInterface, Listener
{
	private List<Snowball> fireballs = new ArrayList<Snowball>();
	private double timer;
	private double damage;
	private double velocity;
	private boolean gravity;
	private int fireballfireticks;
	private int targetfireticks;
	private int areaofeffect;
	private int explosion;
	private boolean incendiary;
	private boolean singletarget;

	public CastFireball(String name)
	{
		super(name);

		timer = Main.getConfigCasts().getDouble("Fireball.DeletionTimer");
		damage = Main.getConfigCasts().getDouble("Fireball.Damage");
		velocity = Main.getConfigCasts().getDouble("Fireball.Velocity");
		gravity = Main.getConfigCasts().getBoolean("Fireball.Gravity");
		fireballfireticks = Main.getConfigCasts().getInt("Fireball.FireTicks.Fireball");
		targetfireticks = Main.getConfigCasts().getInt("Fireball.FireTicks.Target");
		areaofeffect = Main.getConfigCasts().getInt("Fireball.AreaOfEffect");
		explosion = Main.getConfigCasts().getInt("Fireball.Explosion");
		incendiary = Main.getConfigCasts().getBoolean("Fireball.Incendiary");
		singletarget = Main.getConfigCasts().getBoolean("Fireball.SingleTarget");

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

			if (args.length > 1)
			{
				pages.display(player, args, 2);

				return true;
			}

			if (caster.hasCast(name) && !caster.isCasting(name) && !caster.isWarmingUp() && !caster.isSilenced(name)
					&& !caster.isStunned(name) && !cooldown.hasCooldown(player, name) && caster.hasMana(manacost, name))
			{
				if (warmup.getDuration() > 0)
				{
					warmup.start(Main.getInstance(), caster, name);
				}

				new BukkitRunnable()
				{
					@Override
					public void run()
					{
						caster.setCasting(name, true);
						caster.setMana(manacost);

						Snowball fireball = (Snowball) player.launchProjectile(Snowball.class);
						fireball.setVelocity(fireball.getVelocity().normalize().multiply(velocity));
						fireball.setFireTicks(fireballfireticks);
						fireball.setGravity(gravity);
						fireball.setShooter(player);

						fireballs.add(fireball);

						cast(player);

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
									fireballs.remove(fireball);
									fireball.remove();
								}
							}

						}.runTaskLater(Main.getInstance(), (long) (timer));
					}

				}.runTaskLater(Main.getInstance(), warmup.getDuration());
			}
		}

		return true;
	}

	@EventHandler
	public void onProjectileHit(ProjectileHitEvent event)
	{
		Projectile projectile = event.getEntity();

		if (projectile instanceof Snowball && fireballs.contains(projectile))
		{
			Snowball fireball = (Snowball) projectile;

			List<Entity> e = fireball.getNearbyEntities(areaofeffect, areaofeffect, areaofeffect);

			for (Entity target : e)
			{
				if (fireball.getShooter() instanceof Player && !target.equals(fireball.getShooter()))
				{
					if (target instanceof LivingEntity)
					{
						((Damageable) target).damage(damage);
						target.setFireTicks(targetfireticks);

						target.getWorld().spigot().playEffect(target.getLocation().add(0.0D, 1.0D, 0.0D), Effect.FLAME,
								0, 0, 0.2F, 0.2F, 0.2F, 0.1F, 50, 16);
						target.getWorld().playSound(target.getLocation(), Sound.BLOCK_FIRE_AMBIENT, 8.0F, 1.0F);

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
