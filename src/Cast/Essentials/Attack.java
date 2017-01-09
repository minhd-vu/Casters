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
				if (caster.getWeapon().contains(caster.getPlayer().getInventory().getItemInMainHand().getType()))
				{
					event.setDamage(caster.getStrength() * caster.getStrengthScale() + event.getDamage());
				}

				else
				{
					event.setDamage(caster.getStrength() * caster.getStrengthScale());
				}
			}
		}

		else if (event.getDamager() instanceof Creature)
		{
			if (Main.getConfigMobs().getDouble(event.getDamager().getName() + ".Damage") > 0)
			{
				event.setDamage(Main.getConfigMobs().getDouble(event.getDamager().getName() + ".Damage"));
			}
		}
	}
}
