package Cast.Casts;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Effect;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.WitherSkull;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import Cast.CommandInterface;
import Cast.Main;
import Cast.Casts.Types.ActiveCast;
import Cast.Essentials.Caster;
import net.md_5.bungee.api.ChatColor;

public class CastDarkBomb extends ActiveCast implements CommandInterface, Listener
{
	private List<WitherSkull> darkbombs = new ArrayList<WitherSkull>();
	private double seconds;
	private double damage;
	private boolean gravity;
	private boolean charged;
	private boolean incendiary;
	private int darkbombfireticks;
	private int targetfireticks;
	private int areaofeffect;
	private float explosion;
	private int duration;
	private int amplifier;
	private boolean explode;

	public CastDarkBomb(String name)
	{
		super(name);

		seconds = Main.getConfigCasts().getDouble("DarkBomb.DeletionTimer");
		damage = Main.getConfigCasts().getDouble("DarkBomb.Damage");
		gravity = Main.getConfigCasts().getBoolean("DarkBomb.Gravity");
		charged = Main.getConfigCasts().getBoolean("DarkBomb.Charged");
		darkbombfireticks = Main.getConfigCasts().getInt("DarkBomb.FireTicks.DarkBomb");
		targetfireticks = Main.getConfigCasts().getInt("DarkBomb.FireTicks.Target");
		areaofeffect = Main.getConfigCasts().getInt("DarkBomb.AreaOfEffect");
		explosion = Main.getConfigCasts().getInt("DarkBomb.Explosion");
		duration = Main.getConfigCasts().getInt("DarkBomb.Duration");
		amplifier = Main.getConfigCasts().getInt("DarkBomb.Amplifier");
		explode = Main.getConfigCasts().getBoolean("DarkBomb.Explode");

		info.add(ChatColor.DARK_AQUA + "Damage: " + ChatColor.GRAY + damage + " HP");
		info.add(ChatColor.DARK_AQUA + "FireTicks: " + ChatColor.GRAY + targetfireticks / 20);
		info.add(ChatColor.DARK_AQUA + "Wither: " + ChatColor.GRAY + amplifier);

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

						WitherSkull darkbomb = player.launchProjectile(WitherSkull.class);
						darkbomb.setFireTicks(darkbombfireticks);
						darkbomb.setGravity(gravity);
						darkbomb.setCharged(charged);
						darkbomb.setIsIncendiary(incendiary);
						darkbomb.setYield(explosion);
						darkbomb.setShooter(player);
						darkbombs.add(darkbomb);

						cast(player);

						player.getWorld().spigot().playEffect(player.getLocation(), Effect.WITHER_SHOOT);

						cooldown.start(player.getName());

						caster.setCasting(name, false);

						new BukkitRunnable()
						{
							@Override
							public void run()
							{
								if (!darkbomb.isDead())
								{
									darkbombs.remove(darkbomb);
									darkbomb.remove();
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

		if (projectile instanceof WitherSkull && darkbombs.contains(projectile))
		{
			WitherSkull darkbomb = (WitherSkull) projectile;

			List<Entity> e = darkbomb.getNearbyEntities(areaofeffect, areaofeffect, areaofeffect);

			for (Entity target : e)
			{
				if (darkbomb.getShooter() instanceof Player && !target.equals(darkbomb.getShooter()))
				{
					if (target instanceof LivingEntity)
					{
						((Damageable) target).damage(damage);
						target.setFireTicks(targetfireticks);
						((LivingEntity) target)
								.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, duration, amplifier));

						darkbombs.remove(darkbomb);
						darkbomb.remove();
					}

					return;
				}
			}
		}
	}

	@EventHandler
	public void onEntityExplodeEvent(EntityExplodeEvent event)
	{
		if (event.getEntity() instanceof WitherSkull && darkbombs.contains(event.getEntity()))
		{
			event.setCancelled(!explode);

			if (event.isCancelled())
			{
				event.getEntity().getWorld().spigot().playEffect(event.getEntity().getLocation().add(0.0D, 0.5D, 0.0D),
						Effect.EXPLOSION, 0, 0, 0.2F, 0.2F, 0.2F, 0.1F, 50, 16);
				event.getEntity().getWorld().spigot().playEffect(event.getEntity().getLocation(),
						Effect.EXPLOSION_LARGE);
			}
		}
	}
}
