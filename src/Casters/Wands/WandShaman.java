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
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class WandShaman extends Wand implements CommandInterface, Listener
{
	private List<Snowball> snowballs = new ArrayList<Snowball>();

	public WandShaman(String name)
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
		velocity = 1;

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

					Snowball snowball = player.launchProjectile(Snowball.class);
					snowball.setVelocity(snowball.getVelocity().normalize().multiply(velocity));
					snowball.setGravity(gravity);
					snowball.setShooter(player);

					snowballs.add(snowball);

					player.getWorld().playSound(player.getLocation(), Sound.ENTITY_SNOWBALL_THROW, 8.0F, 1.0F);

					cooldown.start(player.getName());

					caster.setCasting(name, false);

					new BukkitRunnable()
					{
						@SuppressWarnings("deprecation")
						@Override
						public void run()
						{
							if (!snowball.isDead())
							{
								player.getWorld().spigot().playEffect(snowball.getLocation().add(0, 0.2, 0),
										Effect.FIREWORKS_SPARK, 0, 0, 0.2F, 0.2F, 0.2F, 0, 4, 128);
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
							if (!snowball.isDead())
							{
								snowballs.remove(snowball);
								snowball.remove();
							}
						}

					}.runTaskLater(Casters.getInstance(), (long) timer);
				}
			}
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onProjectileHit(ProjectileHitEvent event)
	{
		Projectile projectile = event.getEntity();

		if (projectile instanceof Snowball && snowballs.contains(projectile))
		{
			Snowball snowball = (Snowball) projectile;

			List<Entity> e = snowball.getNearbyEntities(areaofeffect, areaofeffect, areaofeffect);

			for (Entity target : e)
			{
				if (snowball.getShooter() instanceof Player && !target.equals(snowball.getShooter()))
				{
					if (target instanceof LivingEntity)
					{
						((Damageable) target).damage(damage);

						target.getWorld().spigot().playEffect(target.getLocation().add(0.0D, 1.0D, 0.0D),
								Effect.SNOWBALL_BREAK, 0, 0, 0.2F, 0.2F, 0.2F, 0.1F, 50, 16);
						target.getWorld().playSound(target.getLocation(), Sound.BLOCK_GLASS_BREAK, 8.0F, 1.0F);

						snowballs.remove(snowball);
						snowball.remove();

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
