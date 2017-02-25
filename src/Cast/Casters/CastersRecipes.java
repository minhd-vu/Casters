package Cast.Casters;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

import Cast.CommandInterface;
import Cast.Main;
import Cast.Essentials.Chat.Pages;

public class CastersRecipes implements CommandInterface
{
	private Pages pages = new Pages();
	private List<String> commands = new ArrayList<String>();

	private String header = ChatColor.DARK_GRAY + "-[" + ChatColor.DARK_AQUA + "Recipes" + ChatColor.DARK_GRAY + "]";
	private String fill = ChatColor.DARK_GRAY + "----------------------";

	public void Recpies()
	{
		ItemStack leather = new ItemStack(Material.LEATHER, 1);
		ShapedRecipe rottenfleshtoleather = new ShapedRecipe(leather);
		rottenfleshtoleather.shape("%%%", "%%%", "%%%");
		rottenfleshtoleather.setIngredient('%', Material.ROTTEN_FLESH);
		Main.getInstance().getServer().addRecipe(rottenfleshtoleather);

		ItemStack chainmailhelmet = new ItemStack(Material.CHAINMAIL_HELMET, 1);
		ShapedRecipe ironfencetochainmailhelmet = new ShapedRecipe(chainmailhelmet);
		ironfencetochainmailhelmet.shape("&&&", "& &");
		ironfencetochainmailhelmet.setIngredient('&', Material.IRON_FENCE);
		Main.getInstance().getServer().addRecipe(ironfencetochainmailhelmet);

		commands.add(ChatColor.GRAY + "9 Rotten Flesh -> 1 Leather.");
		commands.add(ChatColor.GRAY + "Use Iron Fence To Craft Chainmail Armor.");

		pages.setHeader(fill + header + fill);
		pages.setError("Casters");
		pages.setCommand("casters");
		pages.setPage(commands);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if (sender instanceof Player)
		{
			Player player = (Player) sender;

			pages.display(player, args, 0);
		}

		return true;
	}
}
