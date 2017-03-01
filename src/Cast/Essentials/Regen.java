package Cast.Essentials;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

import Cast.Main;

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

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerItemConsumeEvent(PlayerItemConsumeEvent event)
	{
		if (event.getItem().getType().equals(Material.POTION))
		{
			Caster caster = Main.getCasters().get(event.getPlayer().getUniqueId());

			// event.getItem().getItemMeta().equals(obj)

			if (Potion.fromItemStack(event.getItem()).getType().equals(PotionType.INSTANT_HEAL))
			{
				if (Potion.fromItemStack(event.getItem()).getLevel() == 1)
				{

				}
			}
		}
	}
}
