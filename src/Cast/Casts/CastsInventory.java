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
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.List;

public class CastsInventory implements CommandInterface, Listener
{
	private int inventorysize;

	public CastsInventory()
	{
		inventorysize = 9;
	}

	@Override
	@Deprecated
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if (sender instanceof Player)
		{
			Caster caster = Main.getCasters().get(((Player) sender).getUniqueId());

			Inventory inventory = Bukkit.createInventory(caster.getPlayer(), inventorysize, ChatColor.DARK_AQUA + caster.getPlayer().getName() + "'s" + ChatColor.AQUA + " Casts!");

			for (String name : caster.getCasts().keySet())
			{
				if (!(Main.getCasts().get(name) instanceof Passive))
				{
					Cast cast = Main.getCasts().get(name);

					ItemStack castitem = new ItemStack(Material.SKULL_ITEM, 1);
					//castitem.addEnchantment(Enchantment.DAMAGE_ALL, 1);
					SkullMeta castmeta = (SkullMeta) castitem.getItemMeta();
					castmeta.setOwner(Bukkit.getOfflinePlayer("SirGoldenNugget").getUniqueId().toString());
					castmeta.setDisplayName(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + name);
					castmeta.addEnchant(Enchantment.DURABILITY, 1, true);
					castmeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

					List<String> description = cast.getInfo().subList(1, cast.getInfo().size() - 1);
					description.add(0, ChatColor.AQUA + cast.getDescription());

					castmeta.setLore(description);
					castitem.setItemMeta(castmeta);

					inventory.addItem(castitem);
				}
			}

			caster.getPlayer().openInventory(inventory);
		}

		return true;
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
