package Cast.Wands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;

import Cast.Main;
import Cast.Essentials.Chat.Pages;
import Cast.Essentials.Schedulers.Cooldown;
import Cast.Essentials.Schedulers.WarmUp;

public class Wand
{
	protected String name;

	protected Cooldown cooldown = new Cooldown();
	protected WarmUp warmup = new WarmUp();
	protected double manacost;
	protected double timer;
	protected double damage;
	protected double velocity;
	protected boolean gravity;
	protected int areaofeffect;
	protected boolean singletarget;
	protected boolean charged;
	protected boolean explode;
	protected boolean incendiary;

	protected Pages pages = new Pages();
	protected List<String> info = new ArrayList<String>();

	protected ChatColor chatcolor;
	protected Material material;

	protected String cheader = ChatColor.DARK_GRAY + "[" + ChatColor.DARK_AQUA + "Wand" + ChatColor.DARK_GRAY + "]";
	protected String fill = "------------------------";

	public Wand(String name)
	{
		this.name = name;
		


		/*-
		warmup.setDuration(Main.getConfigWands(), name + ".WarmUp.Duration");
		warmup.setAmplifier(Main.getConfigWands(), name + ".WarmUp.Amplifier");
		cooldown.setCooldown(Main.getConfigWands(), "Wand." + name + ".Cooldown");
		timer = Main.getConfigWands().getDouble("Wand." + name + ".DeletionTimer");
		damage = Main.getConfigWands().getDouble("Wand." + name + ".Damage");
		velocity = Main.getConfigWands().getDouble("Wand." + name + ".Velocity");
		manacost = Main.getConfigWands().getDouble("Wand." + name + ".ManaCost");
		gravity = Main.getConfigWands().getBoolean("Wand." + name + ".Gravity");
		areaofeffect = Main.getConfigWands().getInt("Wand." + name + ".AreaOfEffect");
		singletarget = Main.getConfigWands().getBoolean("Wand." + name + ".SingleTarget");
		charged = Main.getConfigWands().getBoolean("Wand." + name + ".Charged");
		explode = Main.getConfigWands().getBoolean("Wand." + name + ".Explode");
		incendiary = Main.getConfigWands().getBoolean("Wand." + name + ".Incendiary");

		info.add(ChatColor.DARK_AQUA + name + " Wand:");
		info.add(ChatColor.DARK_AQUA + "WarmUp: " + ChatColor.GRAY + warmup.getDuration() / 20.0 + " Seconds.");
		info.add(ChatColor.DARK_AQUA + "Cooldown: " + ChatColor.GRAY + cooldown.getCooldown() / 20.0 + " Seconds.");
		info.add(ChatColor.DARK_AQUA + "Cost: " + ChatColor.GRAY + manacost + " MP.");
		info.add(ChatColor.DARK_AQUA + "Damage: " + ChatColor.GRAY + damage + " HP.");*/

		pages.setHeader(ChatColor.DARK_GRAY + fill + cheader + fill);
		pages.setError("Wand " + name);
	}
}
