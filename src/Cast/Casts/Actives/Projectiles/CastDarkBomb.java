package Cast.Casts.Actives.Projectiles;

import Cast.CommandInterface;
import Cast.Essentials.Caster;
import Cast.Main;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Effect;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class CastDarkBomb extends Projectile implements CommandInterface, Listener
{
	private int timer;
	private double damage;
	private boolean gravity;
	private boolean charged;
	private boolean incendiary;
	private int darkbombfireticks;
	private int targetfireticks;
	private int areaofeffect;
	private float explosion;
	private int yield;
	private int duration;
	private int amplifier;
	private boolean explode;
	private double velocity;
	private boolean singletarget;

	public CastDarkBomb(String name, String description)
	{
		super(name, description);

		warmup.setDuration(40);
		warmup.setAmplifier(5);
		cooldown.setCooldown(100);
		manacost = 5;

		info.add(ChatColor.DARK_AQUA + "WarmUp: " + ChatColor.GRAY + warmup.getDuration() / 20.0 + " Seconds");
		info.add(ChatColor.DARK_AQUA + "Cooldown: " + ChatColor.GRAY + cooldown.getCooldown() / 20.0 + " Seconds");
		info.add(ChatColor.DARK_AQUA + "Cost: " + ChatColor.GRAY + manacost + " MP");

		timer = 100;
		damage = 10;
		gravity = false;
		charged = false;
		darkbombfireticks = 0;
		targetfireticks = 0;
		areaofeffect = 1;
		explosion = 0;
		yield = 0;
		duration = 100;
		amplifier = 1;
		velocity = 1.0;
		singletarget = false;

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

						WitherSkull darkbomb = (WitherSkull) player.getWorld().spawnEntity(player.getEyeLocation(), EntityType.WITHER_SKULL);
						darkbomb.setShooter(caster.getPlayer());
						darkbomb.setVelocity(caster.getPlayer().getEyeLocation().getDirection().normalize().multiply(velocity));
						darkbomb.setFireTicks(darkbombfireticks);
						darkbomb.setGravity(gravity);
						darkbomb.setCharged(charged);
						darkbomb.setIsIncendiary(incendiary);
						darkbomb.setYield(yield);

						projectiles.add(darkbomb.getUniqueId());

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
									projectiles.remove(darkbomb.getUniqueId());
									darkbomb.remove();
								}
							}

						}.runTaskLater(Main.getInstance(), timer);
					}

				}.runTaskLater(Main.getInstance(), warmup.getDuration());
			}
		}

		return true;
	}

	@EventHandler
	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event)
	{
		if (event.getDamager() instanceof WitherSkull)
		{
			WitherSkull darkbomb = (WitherSkull) event.getDamager();

			if (projectiles.contains(darkbomb.getUniqueId()) && darkbomb.getShooter() instanceof Player)
			{
				Caster caster = Main.getCasters().get(((Player) darkbomb.getShooter()).getUniqueId());

				event.setCancelled(true);

				List<Entity> entities = darkbomb.getNearbyEntities(areaofeffect, areaofeffect, areaofeffect);

				for (Entity target : entities)
				{
					if (target instanceof LivingEntity && !target.equals(darkbomb.getShooter()))
					{
						if (!caster.sameParty(target))
						{
							((LivingEntity) target).damage(damage);
							target.setFireTicks(targetfireticks);
							((LivingEntity) target).addPotionEffect(new PotionEffect(PotionEffectType.WITHER, duration, amplifier));
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
