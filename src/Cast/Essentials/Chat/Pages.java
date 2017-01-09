package Cast.Essentials.Chat;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class Pages
{
	private String header;
	private String error;
	private String cmd;

	private int size = 8;

	private boolean set = false;

	private String bar = ChatColor.DARK_GRAY + "-----------------------------------------------------";
	private String fill = ChatColor.DARK_GRAY + "---------------------";

	private HashMap<Integer, String> pages = new HashMap<Integer, String>();
	private TextComponent footer = new TextComponent(
			ChatColor.DARK_GRAY + "[" + ChatColor.AQUA + "Next Page" + ChatColor.DARK_GRAY + "]");

	public void setHeader(String header)
	{
		this.header = "\n" + header;
	}

	public void setError(String error)
	{
		this.error = ChatColor.DARK_GRAY + "[" + ChatColor.DARK_AQUA + error + ChatColor.DARK_GRAY + "]"
				+ ChatColor.GRAY + " ";
	}

	public void setCommand(String cmd)
	{
		this.cmd = cmd;
	}

	public boolean hasPages()
	{
		return set;
	}

	public void setPage(HashMap<String, String> list)
	{
		String message = "";
		int count = 0;
		int page = 1;

		for (Entry<String, String> entry : list.entrySet())
		{
			message += "\n" + ChatColor.GRAY + entry.getKey() + " - " + entry.getValue();
			count++;

			if (count == page * size)
			{
				pages.put(page, message);

				++page;
				message = "";
			}
		}

		if (message != null && message.length() != 0)
		{
			pages.put(page, message);
		}

		set = true;
	}

	public void setPage(List<String> list)
	{
		String message = "";
		int count = 0;
		int page = 1;

		for (String entry : list)
		{
			message += "\n" + entry;
			count++;

			if (count == page * size)
			{
				pages.put(page, message);

				++page;
				message = "";
			}
		}

		if (message != null && message.length() != 0)
		{
			pages.put(page, message);
		}

		set = true;
	}

	public void setPage(String[] list)
	{
		String message = "";
		int count = 0;
		int page = 1;

		for (String entry : list)
		{
			message += "\n" + entry;
			count++;

			if (count == page * size)
			{
				pages.put(page, message);

				++page;
				message = "";
			}
		}

		if (message != null && message.length() != 0)
		{
			pages.put(page, message);
		}

		set = true;
	}

	public void display(Player player, String[] args, int base)
	{
		int page = 0;

		if (args.length == base)
		{
			page = 1;
		}

		else if (args.length == base + 1)
		{
			try
			{
				page = Integer.parseInt(args[base]);

				if (!pages.containsKey(page))
				{
					player.sendMessage(error + ChatColor.GRAY + "That Is Not A Valid Page Number!");
					return;
				}
			}

			catch (NumberFormatException e)
			{
				player.sendMessage(error + ChatColor.GRAY + "You Must Input A Number!");
				return;
			}
		}

		if (args.length > base + 1)
		{
			return;
		}

		TextComponent message = new TextComponent(header + pages.get(page));

		footer.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
				new ComponentBuilder(ChatColor.GRAY + "Go To The Next Page!").create()));
		footer.setClickEvent(
				new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/" + cmd + " " + Integer.toString(page + 1)));

		if (!pages.containsKey(page + 1))
		{
			message.addExtra("\n" + bar);
		}

		else
		{
			message.addExtra("\n" + fill + "-");
			message.addExtra(footer);
			message.addExtra(fill);
		}

		player.spigot().sendMessage(message);
	}
}
