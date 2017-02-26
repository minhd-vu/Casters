package Cast.Casts.Types;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;

import Cast.Main;
import Cast.Essentials.Chat.Pages;

public class Passive
{
	protected String name;
	protected Pages pages = new Pages();
	protected List<String> info = new ArrayList<String>();

	protected int level;

	protected String header = ChatColor.DARK_GRAY + "[" + ChatColor.DARK_AQUA + "Cast" + ChatColor.DARK_GRAY + "]";
	protected String fill = "------------------------";

	public Passive(String name)
	{
		this.name = name;

		info.add(ChatColor.DARK_AQUA + name + " Passive: " + Main.getCasts().get(name));

		pages.setHeader(ChatColor.DARK_GRAY + fill + header + fill);
		pages.setCommand("cast " + name + " info");
		pages.setError("Cast " + name);
	}

	public String getName()
	{
		return name;
	}

	public List<String> getInfo()
	{
		return info;
	}
}
