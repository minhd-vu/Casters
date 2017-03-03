package Cast.Essentials;

import Cast.Main;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.event.player.PlayerItemConsumeEvent;

public class Regen implements Listener
{
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onEntityRegainHealthEvent(EntityRegainHealthEvent event)
	{
		if (event.getEntity() instanceof Player)
		{
			if (event.getRegainReason().equals(RegainReason.EATING)
					|| event.getRegainReason().equals(RegainReason.SATIATED)
					|| event.getRegainReason().equals(RegainReason.MAGIC_REGEN))
			{
				Player player = (Player) event.getEntity();
				event.setAmount(player.getMaxHealth() / 20.0);
			}
		}
	}

	@EventHandler
	public void onPlayerItemConsumeEvent(PlayerItemConsumeEvent event)
	{
		if (event.getItem().getType().equals(Material.POTION))
		{
			@SuppressWarnings("unused")
			Caster caster = Main.getCasters().get(event.getPlayer().getUniqueId());

			/*-
			Potion potion = Potion.fromItemStack(event.getItem());
			
			if (potion.getEffects().contains(PotionEffectType.))
			{
				if (Potion.fromItemStack(event.getItem()).getLevel())
				{
					event.setCancelled(true);
					caster.setHealth(caster.getHealth() + potion.getEffects());
				}
			}
			*/
		}
	}
}
