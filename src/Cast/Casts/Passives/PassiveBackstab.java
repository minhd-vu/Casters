package Cast.Casts.Passives;

import Cast.CommandInterface;
import Cast.Essentials.Caster;
import Cast.Main;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PassiveBackstab extends Passive implements CommandInterface, Listener
{
	private double percentage;
	private double sneaking;

	public PassiveBackstab(String name, String description)
	{
		super(name, description);

		percentage = 150;
		sneaking = 200;

		info.add(ChatColor.DARK_AQUA + "Bonus Damage: " + ChatColor.AQUA + percentage + "%");
		info.add(ChatColor.DARK_AQUA + "Sneaking Damage: " + ChatColor.AQUA + sneaking + "%");

		pages.setPage(info);
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event)
	{
		if (event.getDamager() instanceof Player && event.getEntity() instanceof LivingEntity)
		{
			Player player = (Player) event.getDamager();
			Caster caster = Main.getCasters().get(player.getUniqueId());
			LivingEntity target = (LivingEntity) event.getEntity();

			if (caster.getCasts().containsKey(name))
			{
				if (target.getLocation().getDirection().dot(player.getLocation().getDirection()) > 0.0D)
				{
					if (!(caster.hasParty() && caster.getParty().getMembers().contains(target)))
					{
						if (player.isSneaking())
						{
							target.damage(event.getDamage() * (sneaking / 100.0));
						}
						else
						{
							target.damage(event.getDamage() * (percentage / 100.0));
						}

						target.getWorld().spigot().playEffect(target.getLocation(), Effect.COLOURED_DUST, 0, 0, 0.2F,
								1.0F, 0.2F, 0.0F, 30, 16);
						target.getWorld().playSound(target.getLocation(), Sound.ENTITY_ENDERDRAGON_HURT, 1.0F, 0.6F);
					}

					event.setCancelled(true);
				}
			}
		}
	}
}
