package Casters.Casts;

import Casters.Casters;
import Casters.Casts.Passives.Passive;
import Casters.CommandInterface;
import Casters.Essentials.Caster;
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

		for (String name : Casters.getCasts().keySet())
		{
			Cast cast = Casters.getCasts().get(name);

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
		meta.addPage(
				ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "1: Setup\n" + ChatColor.BLACK + "In Order To " + ChatColor.DARK_AQUA + ChatColor.BOLD + "Cast" + ChatColor.BLACK +
						", Place One Of The " + ChatColor.DARK_AQUA + ChatColor.BOLD + "Cast\nItems" + ChatColor.DARK_GRAY + " (Found In The Casts Menu)" + ChatColor.BLACK +
						" And Place It In Your " + ChatColor.DARK_AQUA + ChatColor.BOLD + "Hotbar.");
		meta.addPage(ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "2: Casting\n" + ChatColor.BLACK +
				"Next, All You Are Required To Do Is Either Press Your" + ChatColor.DARK_AQUA + ChatColor.BOLD + " Hotbar Keys " + ChatColor.DARK_GRAY + "(Usually 1-9)" +
				ChatColor.BLACK + " Or " + ChatColor.DARK_AQUA + ChatColor.BOLD + "Scroll" + ChatColor.BLACK + " Using The Mouse " + ChatColor.DARK_GRAY + "(Not Recommended)" +
				ChatColor.BLACK + ".");
		meta.addPage(ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "3: Warnings\n" + ChatColor.BLACK + "If You Drop Or Die With Your" + ChatColor.DARK_AQUA + ChatColor.BOLD +
				" Cast Heads" + ChatColor.BLACK + ", You They Will Be" + ChatColor.RED + ChatColor.BOLD + " Deleted" + ChatColor.BLACK + ". The " + ChatColor.DARK_AQUA +
				ChatColor.BOLD + "Cast\nHeads" + ChatColor.BLACK + " Are Also Not Placable. Do Not Rename Heads To" + ChatColor.DARK_AQUA + ChatColor.BOLD +
				" Casts " + ChatColor.BLACK + "Or They Will Be " + ChatColor.RED + ChatColor.BOLD + "Destroyed" + ChatColor.BLACK + "!");

		instructions.setItemMeta(meta);

		inventorysize = 9;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if (sender instanceof Player)
		{
			Caster caster = Casters.getCasters().get(((Player) sender).getUniqueId());

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
		if (isCastItem(event.getItemDrop().getItemStack()))
		{
			event.getItemDrop().remove();
		}
	}

	private boolean isCastItem(ItemStack castitem)
	{
		if (castitem != null && castitem.hasItemMeta())
		{
			return castitems.containsKey(ChatColor.stripColor(castitem.getItemMeta().getDisplayName()));
		}

		return false;
	}

	@EventHandler
	public void onPlayerDeathEvent(PlayerDeathEvent event)
	{
		List<ItemStack> castitems = new ArrayList<ItemStack>();

		for (ItemStack drop : event.getDrops())
		{
			if (isCastItem(drop))
			{
				castitems.add(drop);
			}
		}

		event.getDrops().removeAll(castitems);
	}

	@EventHandler
	public void onPlayerItemHeldEvent(PlayerItemHeldEvent event)
	{
		Player player = event.getPlayer();
		Caster caster = Casters.getCasters().get(player.getUniqueId());

		ItemStack newitem = player.getInventory().getItem(event.getNewSlot());
		ItemStack olditem = player.getInventory().getItem(event.getPreviousSlot());

		if (isCastItem(newitem))
		{
			String cast = ChatColor.stripColor(newitem.getItemMeta().getDisplayName());

			if (caster.getCasts().containsKey(cast))
			{
				if (!isCastItem(olditem))
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
			event.setCancelled(isCastItem(event.getPlayer().getInventory().getItemInMainHand()) || isCastItem(event.getPlayer().getInventory().getItemInOffHand()));
		}
	}
}
