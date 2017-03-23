package Casters.Casts.Passives;

import Casters.Casters;
import Casters.Essentials.Caster;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class PassiveFlameshield extends Passive
{
	private double percentage;

	public PassiveFlameshield(String name, String description)
	{
		super(name, description);

		cooldown.setCooldown(0);
		manacost = 0;

		percentage = 50.0;

		info.add(ChatColor.DARK_AQUA + "Fire Damage Reduction: " + ChatColor.GRAY + percentage + "%");

		pages.setPage(info);
	}

	@EventHandler
	public void onEntityDamageEvent(EntityDamageEvent event)
	{
		if (event.getEntity() instanceof Player)
		{
			Caster caster = Casters.getCasters().get(event.getEntity().getUniqueId());

			if (caster.canCastPassive(name, cooldown, manacost))
			{
				if (event.getCause().equals(DamageCause.FIRE) || event.getCause().equals(DamageCause.FIRE_TICK)
						|| event.getCause().equals(DamageCause.LAVA))
				{
					event.setDamage(event.getDamage() * percentage / 100.0);
				}
			}
		}
	}
}
