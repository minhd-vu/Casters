package Casters.Casts;

import Casters.Casts.Passives.Passive;
import Casters.CommandInterface;
import Casters.Essentials.Caster;
import Casters.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CastsInventory implements CommandInterface, Listener
{
	private ItemStack instructions;
	private HashMap<String, ItemStack> castitems;
	private int inventorysize;

	public CastsInventory()
	{
		castitems = new HashMap<String, ItemStack>();

		for (String name : Main.getCasts().keySet())
		{
			Cast cast = Main.getCasts().get(name);

			if (!(cast instanceof Passive))
			{
				ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (byte) SkullType.PLAYER.ordinal());
				SkullMeta meta = (SkullMeta) item.getItemMeta();

				meta.setOwner("SirGoldenNugget");
				meta.setDisplayName(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + name);

				List<String> description = new ArrayList<String>();
				description.add(0, ChatColor.AQUA + cast.getDescription());

				for (int i = 1; i < cast.getInfo().size(); ++i)
				{
					description.add(cast.getInfo().get(i));
				}

				meta.setLore(description);
				item.setItemMeta(meta);

				castitems.put(name, item);
			}
		}

		instructions = new ItemStack(Material.WRITTEN_BOOK, 1);
		BookMeta meta = (BookMeta) instructions.getItemMeta();

		meta.setDisplayName(ChatColor.AQUA + "Instructions For Casting");
		meta.setAuthor("SirGoldenNugget");
		meta.setGeneration(BookMeta.Generation.ORIGINAL);
		meta.addPage(ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "1:" + ChatColor.BLACK + " In Order To " + ChatColor.DARK_AQUA + ChatColor.BOLD + "Cast" + ChatColor.BLACK +
				", Place One Of The " + ChatColor.DARK_AQUA + ChatColor.BOLD + "Cast Items" + ChatColor.DARK_GRAY + " (Found In The Casts Menu)" + ChatColor.BLACK +
				" And Place It In Your " +
				ChatColor.DARK_AQUA + ChatColor.BOLD + "Hotbar."); // TODO: Complete This; Add Colors.
		meta.addPage(ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "2:" + ChatColor.BLACK +
				" Next, All You Are Required To Do Is Either Press Your" + ChatColor.DARK_AQUA + ChatColor.BOLD + " Hotbar Keys " + ChatColor.DARK_GRAY + "(Usually 1-9)" +
				ChatColor.BLACK + " Or " +
				ChatColor.DARK_AQUA + ChatColor.BOLD + "Scroll" + ChatColor.BLACK + " Using The Mouse " + ChatColor.DARK_GRAY + "(Not Recommended)" + ChatColor.BLACK + ".");
		instructions.setItemMeta(meta);

		inventorysize = 9;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if (sender instanceof Player)
		{
			Caster caster = Main.getCasters().get(((Player) sender).getUniqueId());

			Inventory inventory = Bukkit.createInventory(caster.getPlayer(), inventorysize,
					ChatColor.DARK_AQUA + "" + ChatColor.BOLD + caster.getPlayer().getName() + "'s" + ChatColor.AQUA + "" + ChatColor.BOLD + " Casts!");

			for (String cast : caster.getCasts().keySet())
			{
				if (castitems.get(cast) != null)
				{
					inventory.addItem(castitems.get(cast));
				}
			}

			inventory.setItem(inventorysize - 1, instructions);

			caster.getPlayer().openInventory(inventory);

			return true;
		}

		return false;
	}

	@EventHandler
	public void onPlayerDropItemEvent(PlayerDropItemEvent event)
	{
		if (castitems.values().contains(event.getItemDrop().getItemStack()))
		{
			event.getItemDrop().remove();
		}

		if (event.getItemDrop().getItemStack().equals(instructions))
		{
			event.getItemDrop().remove();
		}
	}

	@EventHandler
	public void onPlayerDeathEvent(PlayerDeathEvent event)
	{
		event.getDrops().removeAll(castitems.values());
		event.getDrops().remove(instructions);
	}

	@EventHandler
	public void onPlayerItemHeldEvent(PlayerItemHeldEvent event)
	{
		Player player = event.getPlayer();
		Caster caster = Main.getCasters().get(player.getUniqueId());

		ItemStack newitem = player.getInventory().getItem(event.getNewSlot());
		ItemStack olditem = player.getInventory().getItem(event.getPreviousSlot());

		if (castitems.containsValue(newitem))
		{
			String cast = ChatColor.stripColor(newitem.getItemMeta().getDisplayName());

			if (caster.getCasts().containsKey(cast))
			{
				if (!castitems.containsValue(olditem))
				{
					player.getInventory().setHeldItemSlot(event.getPreviousSlot());
				}

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

//	@EventHandler
//	public void PlayerInteractEvent(PlayerInteractEvent event)
//	{
//		if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
//		{
//			event.setCancelled(isCastersItem(event.getPlayer().getInventory().getItemInMainHand()) || isCastersItem(event.getPlayer().getInventory().getItemInOffHand()));
//		}
//	}
//
//	private boolean isCastersItem(ItemStack item)
//	{
//		if (item.getType().equals(Material.SKULL_ITEM) || item.equals(Material.SKULL))
//		{
//			// TODO: Need To Fix This.
//		}
//
//		return false;
//	}
}
