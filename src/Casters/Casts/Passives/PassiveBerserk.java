package Casters.Casts.Passives;

import Casters.Casters;
import Casters.Essentials.Caster;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class PassiveBerserk extends Passive
{
	private double scale;
	private double cap;

	public PassiveBerserk(String name, String description)
	{
		super(name, description);

		cooldown.setCooldown(0);
		manacost = 0;

		scale = 1.5;
		cap = 50.0;

		info.add(ChatColor.DARK_AQUA + "Damage Per Missing Health: " + ChatColor.GRAY + scale + " HP");
		info.add(ChatColor.DARK_AQUA + "Max Damage Cap: " + ChatColor.GRAY + cap + " HP");

		pages.setPage(info);
	}

	@EventHandler
	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event)
	{
		if (event.getDamager() instanceof Player)
		{
			Caster caster = Casters.getCasters().get(event.getDamager().getUniqueId());

			if (caster.canCastPassive(name, cooldown, manacost))
			{
				double damage = (caster.getMaxHealth() - caster.getHealth()) * scale;

				if (damage > cap)
				{
					damage = cap; // TOOD: Test With Kuro.
				}

				event.setDamage(event.getDamage() + damage);
			}
		}
	}
}
