package Casters.Wands;

import Casters.Essentials.Chat.Pages;
import Casters.Essentials.Schedulers.Cooldown;
import Casters.Essentials.Schedulers.WarmUp;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

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

		pages.setHeader(ChatColor.DARK_GRAY + fill + cheader + fill);
		pages.setError("Wand " + name);
	}
}
