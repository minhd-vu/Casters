package Cast.Essentials;

import Cast.Main;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;

public class Enchant implements Listener
{
	private String header = ChatColor.DARK_GRAY + "[" + ChatColor.DARK_AQUA + "Casters" + ChatColor.DARK_GRAY + "]"
			+ ChatColor.WHITE + " ";

	@EventHandler
	public void onEnchantItemEvent(EnchantItemEvent event)
	{
		Caster caster = Main.getCasters().get(event.getEnchanter().getUniqueId());

		if (!caster.getJob().equals("Enchanter"))
		{
			caster.getPlayer().sendMessage(header + "You" + ChatColor.GRAY + " Must Be An " + ChatColor.WHITE
					+ "Enchanter" + ChatColor.GRAY + " To Use That!");
			event.setCancelled(true);
		}
		else if (event.getExpLevelCost() > caster.getJobLevel())
		{
			caster.getPlayer().sendMessage(header + "You" + ChatColor.GRAY + " Do Not Have Enough " + ChatColor.WHITE
					+ "Levels" + ChatColor.GRAY + "!");
			event.setCancelled(true);
		}
		else
		{
			caster.setJobLevel(caster.getJobLevel() - event.getExpLevelCost());

			if (caster.getJobLevel() < 1)
			{
				caster.setJobLevel(1);
			}

			caster.setJobMaxExp();

			caster.getPlayer().sendMessage(header + ChatColor.GRAY + "Successfully Enchanted Item!");
		}
	}
}
