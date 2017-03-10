package Cast.Casters;

import Cast.CommandInterface;
import Cast.Essentials.Caster;
import Cast.Essentials.Chat.Pages;
import Cast.Main;
import org.apache.commons.lang3.text.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.lang.String;

public class CastersArmor implements CommandInterface
{
	private Pages pages = new Pages();
	private String fill = "-------------------";
	private String header = ChatColor.DARK_GRAY + "-[" + ChatColor.DARK_AQUA + "Casters Armor" + ChatColor.DARK_GRAY
			+ "]";

	private List<String> armor = new ArrayList<String>();

	public CastersArmor()
	{
		pages.setHeader(ChatColor.DARK_GRAY + fill + header + fill);
		pages.setError("Casters Armor");
		pages.setCommand("casters armor");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if (sender instanceof Player)
		{
			Player player = (Player) sender;
			Caster caster = Main.getCasters().get(player.getUniqueId());

			armor.clear();
			pages.clear();

			armor.add(ChatColor.GRAY + "Permitted Armor:");

			for (Material material : caster.getArmor())
			{
				String armortext = material.toString().toLowerCase().replace("_", " ");

				armor.add(ChatColor.DARK_AQUA + WordUtils.capitalize(armortext));
			}

			pages.setPage(armor);

			pages.display(player, args, 1);
		}

		return true;
	}
}
