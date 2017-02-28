package Cast.Essentials;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;

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
}
