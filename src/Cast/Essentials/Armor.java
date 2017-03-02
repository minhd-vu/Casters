package Cast.Essentials;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import Cast.Main;

public class Armor implements Listener
{
	private String header = ChatColor.DARK_GRAY + "[" + ChatColor.DARK_AQUA + "Casters" + ChatColor.DARK_GRAY + "]"
			+ ChatColor.WHITE + " ";

	private boolean getArmor(Material material)
	{
		switch (material)
		{
		case DIAMOND_HELMET:
		case GOLD_HELMET:
		case IRON_HELMET:
		case CHAINMAIL_HELMET:
		case LEATHER_HELMET:
		case DIAMOND_CHESTPLATE:
		case GOLD_CHESTPLATE:
		case IRON_CHESTPLATE:
		case CHAINMAIL_CHESTPLATE:
		case LEATHER_CHESTPLATE:
		case DIAMOND_LEGGINGS:
		case GOLD_LEGGINGS:
		case IRON_LEGGINGS:
		case CHAINMAIL_LEGGINGS:
		case LEATHER_LEGGINGS:
		case DIAMOND_BOOTS:
		case GOLD_BOOTS:
		case IRON_BOOTS:
		case CHAINMAIL_BOOTS:
		case LEATHER_BOOTS:
			return true;
		default:
			return false;
		}
	}

	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent event)
	{
		Caster caster = Main.getCasters().get(event.getPlayer().getUniqueId());

		if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
		{
			if (!getArmor(event.getPlayer().getInventory().getItemInMainHand().getType()))
			{
				return;
			}

			for (Material material : caster.getArmor())
			{
				if (event.getPlayer().getInventory().getItemInMainHand().getType().equals(material))
				{
					return;
				}
			}

			event.getPlayer().sendMessage(header + ChatColor.GRAY + "Your Class Cannot Wear That.");
			event.setCancelled(true);
			event.getPlayer().updateInventory();
		}
	}

	@EventHandler
	public void onInventoryClickEvent(InventoryClickEvent event)
	{
		boolean shift = false;
		boolean numberkey = false;

		if (event.getClick().equals(ClickType.SHIFT_LEFT) || event.getClick().equals(ClickType.SHIFT_RIGHT))
		{
			shift = true;
		}

		if (event.getClick().equals(ClickType.NUMBER_KEY))
		{
			numberkey = true;
		}

		if (!event.getSlotType().equals(SlotType.ARMOR) && !event.getSlotType().equals(SlotType.QUICKBAR)
				&& !event.getSlotType().equals(SlotType.CONTAINER))
		{
			return;
		}

		if (event.getClickedInventory() != null && !event.getClickedInventory().getType().equals(InventoryType.PLAYER))
		{
			return;
		}

		if (!event.getInventory().getType().equals(InventoryType.CRAFTING)
				&& !event.getInventory().getType().equals(InventoryType.PLAYER))
		{
			return;
		}

		if (!(event.getWhoClicked() instanceof Player))
		{
			return;
		}

		if (event.getCurrentItem() == null)
		{
			return;
		}

		Caster caster = Main.getCasters().get(event.getWhoClicked().getUniqueId());

		if (shift)
		{
			if (!getArmor(event.getCurrentItem().getType()))
			{
				return;
			}

			if (caster.getArmor().contains(event.getCurrentItem().getType()))
			{
				return;
			}

			event.getWhoClicked().sendMessage(header + ChatColor.GRAY + "Your Class Cannot Wear That.");
			event.setCancelled(true);
			((Player) event.getWhoClicked()).updateInventory();
		}

		else if (numberkey)
		{
			if (!getArmor(event.getClickedInventory().getItem(event.getHotbarButton()).getType()))
			{
				return;
			}

			if (caster.getArmor().contains(event.getClickedInventory().getItem(event.getHotbarButton()).getType()))
			{
				return;
			}

			event.getWhoClicked().sendMessage(header + "Your Class Cannot Wear That.");
			event.setCancelled(true);
			((Player) event.getWhoClicked()).updateInventory();
		}

		else if (event.getSlotType().equals(SlotType.ARMOR))
		{
			if (!getArmor(event.getCursor().getType()))
			{
				return;
			}

			if (caster.getArmor().contains(event.getCursor().getType()))
			{
				return;
			}

			event.getWhoClicked().sendMessage(header + ChatColor.GRAY + "Your Class Cannot Wear That.");
			event.setCancelled(true);
			((Player) event.getWhoClicked()).updateInventory();
		}

		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				ItemStack armor;

				if (caster.getPlayer().getInventory().getHelmet() != null
						&& !caster.getArmor().contains(caster.getPlayer().getInventory().getHelmet().getType()))
				{
					armor = caster.getPlayer().getInventory().getHelmet();
					caster.getPlayer().getInventory().setHelmet(null);
					caster.getPlayer().getInventory().addItem(armor);
				}

				if (caster.getPlayer().getInventory().getChestplate() != null
						&& !caster.getArmor().contains(caster.getPlayer().getInventory().getChestplate().getType()))
				{
					armor = caster.getPlayer().getInventory().getChestplate();
					caster.getPlayer().getInventory().setChestplate(null);
					caster.getPlayer().getInventory().addItem(armor);
				}

				if (caster.getPlayer().getInventory().getLeggings() != null
						&& !caster.getArmor().contains(caster.getPlayer().getInventory().getLeggings().getType()))
				{
					armor = caster.getPlayer().getInventory().getLeggings();
					caster.getPlayer().getInventory().setLeggings(null);
					caster.getPlayer().getInventory().addItem(armor);
				}

				if (caster.getPlayer().getInventory().getBoots() != null
						&& !caster.getArmor().contains(caster.getPlayer().getInventory().getBoots().getType()))
				{
					armor = caster.getPlayer().getInventory().getBoots();
					caster.getPlayer().getInventory().setBoots(null);
					caster.getPlayer().getInventory().addItem(armor);
				}
			}

		}.runTaskLater(Main.getInstance(), 20);
	}

	@EventHandler
	public void onBlockDispenseEvent(BlockDispenseEvent event)
	{
		if (!getArmor(event.getItem().getType()))
		{
			return;
		}

		Location location = event.getBlock().getLocation();
		location.getChunk().getEntities();

		for (Entity target : location.getChunk().getEntities())
		{
			if (target instanceof Player)
			{
				Caster caster = Main.getCasters().get(target.getUniqueId());

				if (caster.getPlayer().getLocation().distance(location) < 2)
				{
					if (caster.getArmor().contains(event.getItem().getType()))
					{
						return;
					}

					caster.getPlayer().sendMessage(header + ChatColor.GRAY + "Your Class Cannot Wear That.");
					event.setCancelled(true);
					caster.getPlayer().updateInventory();
				}

				break;
			}
		}
	}
}
