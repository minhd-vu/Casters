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
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class CastFireBomb extends Projectile implements CommandInterface
{
	private int timer;
	private double damage;
	private boolean gravity;
	private int firebombfireticks;
	private int targetfireticks;
	private int areaofeffect;
	private int explosion;
	private boolean incendiary;
	private boolean singletarget;
	private double velocity;

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

		timer = 100;
		damage = 10;
		gravity = false;
		firebombfireticks = 100;
		targetfireticks = 50;
		areaofeffect = 5;
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

							LargeFireball firebomb = (LargeFireball) player.getWorld().spawnEntity(player.getEyeLocation(), EntityType.FIREBALL);
							firebomb.setShooter(player);
							firebomb.setVelocity(caster.getPlayer().getEyeLocation().getDirection().normalize().multiply(velocity));
							firebomb.setFireTicks(firebombfireticks);
							firebomb.setGravity(gravity);

							projectiles.add(firebomb.getUniqueId());

							cast(player);

							player.getWorld().spigot().playEffect(player.getLocation(), Effect.BLAZE_SHOOT);

							caster.setCasting(name, false);

							new BukkitRunnable()
							{
								@Override
								public void run()
								{
									if (firebomb.isValid())
									{
										projectiles.remove(firebomb.getUniqueId());
										firebomb.remove();
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
		if (event.getDamager() instanceof LargeFireball)
		{
			LargeFireball firebomb = (LargeFireball) event.getDamager();

			if (projectiles.contains(firebomb.getUniqueId()) && firebomb.getShooter() instanceof Player)
			{
				Caster caster = Casters.getCasters().get(((Player) firebomb.getShooter()).getUniqueId());

				event.setCancelled(true);

				List<Entity> entities = firebomb.getNearbyEntities(areaofeffect, areaofeffect, areaofeffect);

				for (Entity target : entities)
				{
					if (target instanceof LivingEntity && !target.equals(firebomb.getShooter()))
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
