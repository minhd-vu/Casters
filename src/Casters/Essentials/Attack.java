package Casters.Essentials;

import Casters.Casters;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;

public class Attack implements Listener
{
	private static HashMap<Arrow, Double> arrows = new HashMap<Arrow, Double>();
	private String header = ChatColor.DARK_GRAY + "[" + ChatColor.DARK_AQUA + "Casters" + ChatColor.DARK_GRAY + "]"
			+ ChatColor.WHITE + " ";

	@EventHandler
	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event)
	{
		if (event.getDamager() instanceof Player)
		{
			Caster caster = Casters.getCasters().get(event.getDamager().getUniqueId());

			if (event.getEntity() instanceof Damageable)
			{
				Damageable entity = (Damageable) event.getEntity();

				if (event.getCause().equals(DamageCause.ENTITY_ATTACK))
				{
					if (caster.sameParty(entity))
					{
						event.setCancelled(true);
						return;
					}

					if (caster.isWarmingUp())
					{
						caster.setInterrupted(true);
						caster.getPlayer().removePotionEffect(PotionEffectType.SLOW);
					}

					// TODO: Recode This So That It Factors In Enchantments.

					if (caster.getWeapons().containsKey(caster.getPlayer().getInventory().getItemInMainHand().getType()) &&
							!caster.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.BOW))
					{
						event.setDamage(caster.getStrength() * caster.getType().getMeleeDamageScale()
								+ caster.getWeapons().get(caster.getPlayer().getInventory().getItemInMainHand().getType()));
					}

					else
					{
						event.setDamage(caster.getStrength() * caster.getType().getMeleeDamageScale() + 1);
						// TODO: Remove When Strength Is Implemented.
					}
				}

				caster.setBossBarEntity(entity);
			}
		}

		else if (event.getDamager() instanceof Creature)
		{
			for (Mob mob : Casters.getMobs())
			{
				if (((Creature) event.getDamager()).getType().equals(mob.getEntityType()) && mob.getDamage() > 0)
				{
					event.setDamage(mob.getDamage());
				}
			}
		}

		else if (event.getDamager() instanceof Projectile)
		{
			Projectile projectile = (Projectile) event.getDamager();

			if (projectile.getShooter() instanceof Player)
			{
				Caster caster = Casters.getCasters().get(((Player) projectile.getShooter()).getUniqueId());

				if (projectile instanceof Arrow)
				{
					Arrow arrow = (Arrow) event.getDamager();

					if (arrows.containsKey(arrow))
					{
						event.setDamage(caster.getWeapons().get(Material.BOW) * arrows.get(arrow)
								+ caster.getDexterity() * caster.getType().getBowDamageScale());
						arrows.remove(arrow);
					}
				}

				if (event.getEntity() instanceof Player)
				{
					if (caster.sameParty(event.getEntity()))
					{
						event.setCancelled(true);
					}
				}

				if (event.getEntity() instanceof Damageable)
				{
					caster.setBossBarEntity((Damageable) event.getEntity());
				}
			}
		}
	}

	@EventHandler
	public void onEntityShootBowEvent(EntityShootBowEvent event)
	{
		if (event.getEntity() instanceof Player && event.getProjectile() instanceof Arrow)
		{
			Caster caster = Casters.getCasters().get(event.getEntity().getUniqueId());

			if (!caster.getWeapons().containsKey(Material.BOW))
			{
				caster.getPlayer().sendMessage(header + ChatColor.GRAY + "Your Class Cannot Use A Bow.");
				event.setCancelled(true);
			}
			else
			{
				arrows.put((Arrow) event.getProjectile(), (double) event.getForce());
			}
		}
	}

	private void cancelWarmUp(Caster caster)
	{
		if (caster.isWarmingUp())
		{
			caster.setInterrupted(true);
			caster.getPlayer().removePotionEffect(PotionEffectType.SLOW);
		}
	}

	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent event)
	{
		if (event.getAction().equals(Action.LEFT_CLICK_AIR) || event.getAction().equals(Action.LEFT_CLICK_BLOCK))
		{
			cancelWarmUp(Casters.getCasters().get(event.getPlayer().getUniqueId()));
		}
	}
}
