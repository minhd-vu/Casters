package Cast.Casts;

import Cast.Casts.Types.Passive;
import Cast.CommandInterface;
import Cast.Essentials.Caster;
import Cast.Main;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class CastsInventory implements CommandInterface, Listener
{
	private int inventorysize;
	private Inventory inventory;

	public CastsInventory()
	{
		inventorysize = 9;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if (sender instanceof Player)
		{
			Caster caster = Main.getCasters().get(((Player) sender).getUniqueId());

			inventory = Bukkit.createInventory(caster.getPlayer(), inventorysize, ChatColor.DARK_AQUA + caster.getPlayer().getName() + "'s" + ChatColor.AQUA + " Casts!");

			for (String cast : caster.getCasts().keySet())
			{
				if (!(Main.getCasts().get(cast) instanceof Passive))
				{
					ItemStack castitem = new ItemStack(Material.SKULL_ITEM, 1);
					SkullMeta castmeta = (SkullMeta) castitem.getItemMeta();
					castmeta.setOwner("SirGoldenNugget");
					castmeta.setDisplayName(cast);

					inventory.addItem(castitem);
				}
			}

			caster.getPlayer().openInventory(inventory);
		}

		return true;
	}

	@EventHandler
	public void onInventoryClickEvent(InventoryClickEvent event)
	{
		if (event.getInventory().equals(inventory))
		{

		}
	}

	@EventHandler
	public void onPlayerItemHeldEvent(PlayerItemHeldEvent event)
	{
		Caster caster = Main.getCasters().get(event.getPlayer());
	}
}
