package Cast.Casts;

import Cast.Casts.Types.Cast;
import Cast.Casts.Types.Passive;
import Cast.CommandInterface;
import Cast.Essentials.Caster;
import Cast.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CastsInventory implements CommandInterface, Listener
{
	private HashMap<String, ItemStack> castitems;

	private int inventorysize;

	@Deprecated
	public CastsInventory()
	{
		castitems = new HashMap<String, ItemStack>();

		for (String name : Main.getCasts().keySet())
		{
			Cast cast = Main.getCasts().get(name);

			if (!(cast instanceof Passive))
			{
				ItemStack castitem = new ItemStack(Material.SKULL_ITEM, 1);
				SkullMeta castmeta = (SkullMeta) castitem.getItemMeta();
				castmeta.setOwner(Bukkit.getOfflinePlayer("SirGoldenNugget").toString());
				castmeta.setDisplayName(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + name);

				List<String> description = new ArrayList<String>();
				description.add(0, ChatColor.AQUA + cast.getDescription());

				for (int i = 1; i < cast.getInfo().size(); ++i)
				{
					description.add(cast.getInfo().get(i));
				}

				castmeta.setLore(description);
				castitem.setItemMeta(castmeta);

				castitems.put(name, castitem.clone()); // Aliasing Probably?
			}
		}

		inventorysize = 9;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if (sender instanceof Player)
		{
			Caster caster = Main.getCasters().get(((Player) sender).getUniqueId());

			Inventory inventory = Bukkit.createInventory(caster.getPlayer(), inventorysize, ChatColor.DARK_AQUA + caster.getPlayer().getName() + "'s" + ChatColor.AQUA + " Casts!");

			for (String cast : caster.getCasts().keySet())
			{
				caster.getPlayer().sendMessage(cast);

				if (castitems.get(cast) != null)
				{
					inventory.addItem(castitems.get(cast));
				}
			}

			caster.getPlayer().openInventory(inventory);

			return true;
		}

		return false;
	}

	@EventHandler
	public void onPlayerDropItemEvent(PlayerDropItemEvent event)
	{
		if (castitems.values().contains(event.getItemDrop()))
		{
			event.getItemDrop().remove();
		}
	}

	@EventHandler
	public void onPlayerItemHeldEvent(PlayerItemHeldEvent event)
	{
		Player player = event.getPlayer();
		Caster caster = Main.getCasters().get(player.getUniqueId());

		ItemStack item = player.getInventory().getItem(event.getNewSlot());

		if (item != null && item.hasItemMeta() && item.getItemMeta().getDisplayName() != null)
		{
			String cast = ChatColor.stripColor(item.getItemMeta().getDisplayName());

			if (caster.getCasts().containsKey(cast))
			{
				player.getInventory().setHeldItemSlot(event.getPreviousSlot());

				player.performCommand("cast " + cast.toLowerCase());
			}
		}
	}

	@EventHandler
	public void PlayerInteractEvent(PlayerInteractEvent event)
	{
		if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
		{
			if (event.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.SKULL_ITEM) ||
					event.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.SKULL))
			{
				if (Main.getCasts().containsKey(ChatColor.stripColor(event.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName())))
				{
					event.setCancelled(true);
				}
			}

			else if (event.getPlayer().getInventory().getItemInOffHand().getType().equals(Material.SKULL_ITEM) ||
					event.getPlayer().getInventory().getItemInOffHand().getType().equals(Material.SKULL))
			{
				if (Main.getCasts().containsKey(ChatColor.stripColor(event.getPlayer().getInventory().getItemInOffHand().getItemMeta().getDisplayName())))
				{
					event.setCancelled(true);
				}
			}
		}
	}
}
