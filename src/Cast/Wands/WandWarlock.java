package Cast.Wands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Material;
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
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import Cast.CommandInterface;
import Cast.Main;
import Cast.Essentials.Caster;

public class WandWarlock extends Wand implements CommandInterface, Listener
{
	private List<WitherSkull> witherskulls = new ArrayList<WitherSkull>();

	public WandWarlock(String name)
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
		charged = false;
		explode = false;

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
			Caster caster = Main.getCasters().get(player.getUniqueId());

			material = player.getInventory().getItemInMainHand().getType();

			if (caster.getType().equalsIgnoreCase(name) && !cooldown.hasCooldown(player.getName())
					&& !caster.isCasting(name) && !caster.isWarmingUp() && !caster.isSilenced(name)
					&& !caster.isStunned(name) && caster.hasMana(manacost, name))
			{
				if (player.getInventory().getItemInMainHand().getType().equals(Material.BLAZE_ROD))
				{
					caster.setCasting(name, true);
					caster.setMana(manacost);

					WitherSkull witherskull = player.launchProjectile(WitherSkull.class);
					witherskull.setShooter(player);
					witherskull.setGravity(gravity);
					witherskull.setCharged(charged);

					witherskulls.add(witherskull);

					player.getWorld().spigot().playEffect(player.getLocation(), Effect.WITHER_SHOOT);

					cooldown.start(player.getName());

					caster.setCasting(name, false);

					new BukkitRunnable()
					{
						@Override
						public void run()
						{
							if (!witherskull.isDead())
							{
								player.getWorld().spigot().playEffect(witherskull.getLocation().add(0, 0.2, 0),
										Effect.WITCH_MAGIC, 0, 0, 0.2F, 0.2F, 0.2F, 0, 4, 128);
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
							if (!witherskull.isDead())
							{
								witherskulls.remove(witherskull);
								witherskull.remove();
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

		if (projectile instanceof WitherSkull && witherskulls.contains(projectile))
		{
			WitherSkull witherskull = (WitherSkull) projectile;

			List<Entity> e = witherskull.getNearbyEntities(areaofeffect, areaofeffect, areaofeffect);

			for (Entity target : e)
			{
				if (witherskull.getShooter() instanceof Player && !target.equals(witherskull.getShooter()))
				{
					if (target instanceof LivingEntity)
					{
						((Damageable) target).damage(damage);

						witherskulls.remove(witherskull);
						witherskull.remove();

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
	public void onEntityExplodeEvent(EntityExplodeEvent event)
	{
		if (event.getEntity() instanceof WitherSkull && witherskulls.contains(event.getEntity()))
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
