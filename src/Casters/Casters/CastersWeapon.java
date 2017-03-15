package Casters.Casters;

import Casters.CommandInterface;
import Casters.Essentials.Caster;
import Casters.Essentials.Chat.Pages;
import Casters.Main;
import org.apache.commons.lang3.text.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CastersWeapon implements CommandInterface
{
	private Pages pages = new Pages();
	private String fill = "-------------------";
	private String header = ChatColor.DARK_GRAY + "[" + ChatColor.DARK_AQUA + "Casters Weapon" + ChatColor.DARK_GRAY
			+ "]";

	private List<String> weapon = new ArrayList<String>();

	public CastersWeapon()
	{
		pages.setHeader(ChatColor.DARK_GRAY + fill + header + fill);
		pages.setError("Casters Weapon");
		pages.setCommand("casters weapon");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if (sender instanceof Player)
		{
			Player player = (Player) sender;
			Caster caster = Main.getCasters().get(player.getUniqueId());

			weapon.clear();
			pages.clear();

			weapon.add(ChatColor.GRAY + "Permitted Weapons: ");

			for (Material material : caster.getWeapon().keySet())
			{
				String weapontext = material.toString().toLowerCase().replace("_", " ");

				weapon.add(ChatColor.DARK_AQUA + WordUtils.capitalize(weapontext) + ChatColor.AQUA + " - " + caster.getWeapon().get(material));
			}

			pages.setPage(weapon);

			pages.display(player, args, 1);
		}

		return true;
	}
}
