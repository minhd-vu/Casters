package Casters.Commands;

import Casters.CommandInterface;
import Casters.Essentials.Chat.Pages;
import Casters.Casters;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

import java.util.ArrayList;
import java.util.List;

public class CastersRecipes implements CommandInterface
{
	private Pages pages = new Pages();
	private List<String> commands = new ArrayList<String>();

	private String header = ChatColor.DARK_GRAY + "-[" + ChatColor.DARK_AQUA + "Recipes" + ChatColor.DARK_GRAY + "]";
	private String fill = ChatColor.DARK_GRAY + "----------------------";

	public CastersRecipes()
	{
		ItemStack leather = new ItemStack(Material.LEATHER, 1);
		ShapedRecipe rottenfleshtoleather = new ShapedRecipe(leather);
		rottenfleshtoleather.shape("%%%", "%%%", "%%%");
		rottenfleshtoleather.setIngredient('%', Material.ROTTEN_FLESH);
		Casters.getInstance().getServer().addRecipe(rottenfleshtoleather);

		ItemStack chainmailhelmet = new ItemStack(Material.CHAINMAIL_HELMET, 1);
		ShapedRecipe ironfencetochainmailhelmet = new ShapedRecipe(chainmailhelmet);
		ironfencetochainmailhelmet.shape("&&&", "& &");
		ironfencetochainmailhelmet.setIngredient('&', Material.IRON_FENCE);
		Casters.getInstance().getServer().addRecipe(ironfencetochainmailhelmet);

		ItemStack chainmailchestplate = new ItemStack(Material.CHAINMAIL_CHESTPLATE, 1);
		ShapedRecipe ironfencetochainmailchestplate = new ShapedRecipe(chainmailchestplate);
		ironfencetochainmailchestplate.shape("& &", "&&&", "&&&");
		ironfencetochainmailchestplate.setIngredient('&', Material.IRON_FENCE);
		Casters.getInstance().getServer().addRecipe(ironfencetochainmailchestplate);

		ItemStack chainmailleggings = new ItemStack(Material.CHAINMAIL_LEGGINGS, 1);
		ShapedRecipe ironfencetochainmailleggings = new ShapedRecipe(chainmailleggings);
		ironfencetochainmailleggings.shape("&&&", "& &", "& &");
		ironfencetochainmailleggings.setIngredient('&', Material.IRON_FENCE);
		Casters.getInstance().getServer().addRecipe(ironfencetochainmailleggings);

		ItemStack chainmailboots = new ItemStack(Material.CHAINMAIL_BOOTS, 1);
		ShapedRecipe ironfencetochainmailboots = new ShapedRecipe(chainmailboots);
		ironfencetochainmailboots.shape("& &", "& &");
		ironfencetochainmailboots.setIngredient('&', Material.IRON_FENCE);
		Casters.getInstance().getServer().addRecipe(ironfencetochainmailboots);

		commands.add(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_AQUA + "1" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY
				+ "9 Rotten Flesh -> 1 Leather.");
		commands.add(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_AQUA + "2" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY
				+ "Use Iron Fence To Craft Chainmail Armor.");

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
			if (args.length <= 2)
			{
				Player player = (Player) sender;

				pages.display(player, args, 1);
			}
		}

		return true;
	}
}
