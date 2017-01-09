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
import org.bukkit.entity.SmallFireball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import Cast.CommandInterface;
import Cast.Main;
import Cast.Casts.Types.ActiveCast;
import Cast.Essentials.Caster;
import net.md_5.bungee.api.ChatColor;

public class CastFireCharge extends ActiveCast implements CommandInterface, Listener
{
	private List<SmallFireball> firecharges = new ArrayList<SmallFireball>();
	private double seconds;
	private double damage;
	private boolean gravity;
	private int firechargefireticks;
	private int targetfireticks;
	private int areaofeffect;
	private int explosion;
	private boolean incendiary;
	private boolean singletarget;

	public CastFireCharge(String name)
	{
		super(name);

		seconds = Main.getConfigCasts().getDouble("FireCharge.DeletionTimer");
		damage = Main.getConfigCasts().getDouble("FireCharge.Damage");
		gravity = Main.getConfigCasts().getBoolean("FireCharge.Gravity");
		firechargefireticks = Main.getConfigCasts().getInt("FireCharge.FireTicks.FireCharge");
		targetfireticks = Main.getConfigCasts().getInt("FireCharge.FireTicks.Target");
		areaofeffect = Main.getConfigCasts().getInt("FireCharge.AreaOfEffect");
		explosion = Main.getConfigCasts().getInt("FireCharge.Explosion");
		incendiary = Main.getConfigCasts().getBoolean("FireCharge.Incendiary");
		singletarget = Main.getConfigCasts().getBoolean("FireCharge.SingleTarget");

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

						SmallFireball firecharge = (SmallFireball) player.launchProjectile(SmallFireball.class);
						firecharge.setFireTicks(firechargefireticks);
						firecharge.setGravity(gravity);
						firecharge.setShooter(player);
						firecharges.add(firecharge);

						cast(player);

						player.getWorld().spigot().playEffect(player.getLocation(), Effect.BLAZE_SHOOT);

						cooldown.start(player.getName());

						caster.setCasting(name, false);

						new BukkitRunnable()
						{
							@Override
							public void run()
							{
								if (!firecharge.isDead())
								{
									firecharges.remove(firecharge);
									firecharge.remove();
								}
							}

						}.runTaskLater(Main.getInstance(), (long) (seconds * 20));
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

		if (projectile instanceof SmallFireball && firecharges.contains(projectile))
		{
			SmallFireball firecharge = (SmallFireball) projectile;

			List<Entity> e = firecharge.getNearbyEntities(areaofeffect, areaofeffect, areaofeffect);

			for (Entity target : e)
			{
				if (firecharge.getShooter() instanceof Player && !target.equals(firecharge.getShooter()))
				{
					if (target instanceof LivingEntity)
					{
						((Damageable) target).damage(damage);
						target.setFireTicks(targetfireticks);

						target.getWorld().spigot().playEffect(target.getLocation().add(0.0D, 0.5D, 0.0D), Effect.FLAME,
								0, 0, 0.2F, 0.2F, 0.2F, 0.1F, 50, 16);
						target.getWorld().playSound(target.getLocation(), Sound.BLOCK_FIRE_AMBIENT, 7.0F, 1.0F);

						firecharges.remove(firecharge);
						firecharge.remove();

						if (singletarget)
						{
							return;
						}
					}
				}
			}

			if (explosion > 0)
			{
				firecharge.getWorld().createExplosion(firecharge.getLocation(), explosion, incendiary);

				return;
			}
		}
	}
}
