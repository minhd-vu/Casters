package Cast.Essentials;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

import Cast.Main;

public class Recipes
{
	public void Recpies()
	{
		ItemStack leather = new ItemStack(Material.LEATHER, 1);
		ShapedRecipe rottenfleshtoleather = new ShapedRecipe(leather);

		rottenfleshtoleather.shape("%%%", "%%%", "%%%");
		rottenfleshtoleather.setIngredient('%', Material.ROTTEN_FLESH);

		Main.getInstance().getServer().addRecipe(rottenfleshtoleather);
	}
}
