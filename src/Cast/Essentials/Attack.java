package Cast.Essentials;

import org.bukkit.entity.Creature;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import Cast.Main;

public class Attack implements Listener
{
	@EventHandler
	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event)
	{
		if (event.getDamager() instanceof Player)
		{
			Caster caster = Main.getCasters().get(event.getDamager().getUniqueId());

			if (event.getCause().equals(DamageCause.ENTITY_ATTACK))
			{
				if (caster.getWeapon().containsKey(caster.getPlayer().getInventory().getItemInMainHand().getType()))
				{
					event.setDamage(caster.getStrength() * caster.getStrengthScale()
							+ caster.getWeapon().get(caster.getPlayer().getInventory().getItemInMainHand().getType()));
				}

				else
				{
					event.setDamage(caster.getStrength() * caster.getStrengthScale());
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
	}
}
