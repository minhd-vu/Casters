package Cast.Essentials;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityShootBowEvent;

import Cast.Main;

public class Attack implements Listener
{
	private String header = ChatColor.DARK_GRAY + "[" + ChatColor.DARK_AQUA + "Casters" + ChatColor.DARK_GRAY + "]"
			+ ChatColor.WHITE + " ";

	private static HashMap<Arrow, Double> arrows = new HashMap<Arrow, Double>();

	@EventHandler
	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event)
	{
		if (event.getDamager() instanceof Player)
		{
			Caster caster = Main.getCasters().get(event.getDamager().getUniqueId());

			if (event.getCause().equals(DamageCause.ENTITY_ATTACK))
			{
				if (caster.getWeapon().containsKey(caster.getPlayer().getInventory().getItemInMainHand().getType())
						&& !caster.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.BOW))
				{
					event.setDamage(caster.getStrength() * caster.getType().getMeleeDamageScale()
							+ caster.getWeapon().get(caster.getPlayer().getInventory().getItemInMainHand().getType()));
				}

				else
				{
					event.setDamage(caster.getStrength() * caster.getType().getMeleeDamageScale());
				}
			}
		}

		else if (event.getDamager() instanceof Creature)
		{
			for (Mob mob : Main.getMobs())
			{
				if (((Creature) event.getDamager()).getType().equals(mob.getEntityType()) && mob.getDamage() > 0)
				{
					event.setDamage(mob.getDamage());
				}
			}
		}

		else if (event.getDamager() instanceof Arrow)
		{
			Arrow arrow = (Arrow) event.getDamager();

			if (arrows.containsKey(arrow) && arrow.getShooter() instanceof Player)
			{
				Player player = (Player) arrow.getShooter();
				Caster caster = Main.getCasters().get(player.getUniqueId());

				event.setDamage(caster.getWeapon().get(Material.BOW) * arrows.get(arrow)
						+ caster.getDexterity() * caster.getType().getBowDamageScale());
				arrows.remove(arrow);
			}
		}
	}

	@EventHandler
	public void onEntityShootBowEvent(EntityShootBowEvent event)
	{
		if (event.getEntity() instanceof Player && event.getProjectile() instanceof Arrow)
		{
			Caster caster = Main.getCasters().get(event.getEntity().getUniqueId());

			if (!caster.getWeapon().containsKey(Material.BOW))
			{
				caster.getPlayer().sendMessage(header + ChatColor.GRAY + " Your Class Cannot Use A Bow.");
				event.setCancelled(true);
			}

			else
			{
				arrows.put((Arrow) event.getProjectile(), (double) event.getForce());
			}
		}
	}
}
