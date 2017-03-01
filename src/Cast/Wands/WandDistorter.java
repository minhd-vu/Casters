package Cast.Wands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.scheduler.BukkitRunnable;

import Cast.CommandInterface;
import Cast.Main;
import Cast.Essentials.Caster;

public class WandDistorter extends Wand implements CommandInterface, Listener
{
	private List<EnderPearl> enderpearls = new ArrayList<EnderPearl>();

	public WandDistorter(String name)
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
			Caster caster = Main.getCasters().get(player.getUniqueId());

			material = player.getInventory().getItemInMainHand().getType();

			if (caster.getType().getName().equalsIgnoreCase(name) && !cooldown.hasCooldown(player.getName())
					&& !caster.isCasting(name) && !caster.isWarmingUp() && !caster.isSilenced(name)
					&& !caster.isStunned(name) && caster.hasMana(manacost, name))
			{
				if (player.getInventory().getItemInMainHand().getType().equals(Material.BLAZE_ROD))
				{
					caster.setCasting(name, true);
					caster.setMana(manacost);

					EnderPearl enderpearl = player.launchProjectile(EnderPearl.class);
					enderpearl.setShooter(player);
					enderpearl.setGravity(gravity);

					enderpearls.add(enderpearl);

					player.getWorld().spigot().playEffect(player.getLocation(), Effect.ENDEREYE_LAUNCH);

					cooldown.start(player.getName());

					caster.setCasting(name, false);

					new BukkitRunnable()
					{
						@Override
						public void run()
						{
							if (!enderpearl.isDead())
							{
								player.getWorld().spigot().playEffect(enderpearl.getLocation().add(0, 0.1, 0),
										Effect.MAGIC_CRIT, 0, 0, 0.2F, 0.2F, 0.2F, 0, 4, 128);
							}

							else
							{
								this.cancel();
								return;
							}
						}

					}.runTaskTimer(Main.getInstance(), 2, 1);

					new BukkitRunnable()
					{
						@Override
						public void run()
						{
							if (!enderpearl.isDead())
							{
								enderpearls.remove(enderpearl);
								enderpearl.remove();
							}
						}

					}.runTaskLater(Main.getInstance(), (long) timer);
				}
			}
		}
	}

	@EventHandler
	public void onProjectileHit(ProjectileHitEvent event)
	{
		Projectile projectile = event.getEntity();

		if (projectile instanceof EnderPearl && enderpearls.contains(projectile))
		{
			EnderPearl enderpearl = (EnderPearl) projectile;

			List<Entity> e = enderpearl.getNearbyEntities(areaofeffect, areaofeffect, areaofeffect);

			for (Entity target : e)
			{
				if (enderpearl.getShooter() instanceof Player && !target.equals(enderpearl.getShooter()))
				{
					if (target instanceof LivingEntity)
					{
						((Damageable) target).damage(damage);

						target.getWorld().playSound(target.getLocation(), Sound.ENTITY_ENDERDRAGON_FIREBALL_EXPLODE,
								8.0F, 1.0F);

						enderpearls.remove(enderpearl);
						enderpearl.remove();

						if (singletarget)
						{
							return;
						}
					}
				}
			}
		}
	}

	@EventHandler
	public void onPlayerTeleportEvent(PlayerTeleportEvent event)
	{
		if (event.getCause().equals(TeleportCause.ENDER_PEARL))
		{
			if (!Material.ENDER_PEARL.equals(material)
					|| !Material.ENDER_PEARL.equals(event.getPlayer().getInventory().getItemInMainHand()))
				;
			{
				event.setCancelled(true);
			}
		}
	}
}
