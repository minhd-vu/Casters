package Cast.Essentials.Chat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import Cast.CommandInterface;
import Cast.Main;
import Cast.Essentials.Caster;

public class Chat implements CommandInterface, Listener
{
	protected Pages pages = new Pages();
	protected HashMap<String, String> titles = new HashMap<String, String>();
	protected String header = ChatColor.DARK_GRAY + "[" + ChatColor.DARK_AQUA + "Chat" + ChatColor.DARK_GRAY + "]";

	private int localrange;
	private int shoutrange;
	private int roleplayrange;

	private List<String> commands = new ArrayList<String>();
	private String fill = "------------------------";

	public Chat()
	{
		commands.add(ChatColor.DARK_AQUA + "/chat" + ChatColor.AQUA + " <page>" + ChatColor.GRAY + " - Lists All Chat Commands.");
		commands.add(ChatColor.DARK_AQUA + "/chat" + ChatColor.AQUA + " titles" + ChatColor.GRAY + " - Lists All Avaliable Titles.");
		commands.add(ChatColor.DARK_AQUA + "/chat" + ChatColor.AQUA + " channel" + ChatColor.GRAY + " - Lists All Public Channels.");
		commands.add(ChatColor.DARK_AQUA + "/chat" + ChatColor.AQUA + " channel <channel>" + ChatColor.GRAY + " - Join A Chat Channel.");

		titles.put("Guardian", ChatColor.DARK_GRAY + "[" + ChatColor.RED + "Guardian" + ChatColor.DARK_GRAY + "]");
		titles.put("Cavalier", ChatColor.DARK_GRAY + "[" + ChatColor.RED + "Cavalier" + ChatColor.DARK_GRAY + "]");
		titles.put("Barbarian", ChatColor.DARK_GRAY + "[" + ChatColor.RED + "Barbarian" + ChatColor.DARK_GRAY + "]");
		titles.put("Blackguard", ChatColor.DARK_GRAY + "[" + ChatColor.RED + "Blackguard" + ChatColor.DARK_GRAY + "]");
		titles.put("Assassin", ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + "Assassin" + ChatColor.DARK_GRAY + "]");
		titles.put("Duelist", ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + "Duelist" + ChatColor.DARK_GRAY + "]");
		titles.put("Fletcher", ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + "Fletcher" + ChatColor.DARK_GRAY + "]");
		titles.put("Musketeer", ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + "Musketeer" + ChatColor.DARK_GRAY + "]");
		titles.put("Distorter", ChatColor.DARK_GRAY + "[" + ChatColor.BLUE + "Distorter" + ChatColor.DARK_GRAY + "]");
		titles.put("Inferno", ChatColor.DARK_GRAY + "[" + ChatColor.BLUE + "Inferno" + ChatColor.DARK_GRAY + "]");
		titles.put("Shaman", ChatColor.DARK_GRAY + "[" + ChatColor.BLUE + "Shaman" + ChatColor.DARK_GRAY + "]");
		titles.put("Warlock", ChatColor.DARK_GRAY + "[" + ChatColor.BLUE + "Warlock" + ChatColor.DARK_GRAY + "]");
		titles.put("Oracle", ChatColor.DARK_GRAY + "[" + ChatColor.YELLOW + "Oracle" + ChatColor.DARK_GRAY + "]");
		titles.put("Bloodmage", ChatColor.DARK_GRAY + "[" + ChatColor.YELLOW + "Bloodmage" + ChatColor.DARK_GRAY + "]");
		titles.put("Monk", ChatColor.DARK_GRAY + "[" + ChatColor.YELLOW + "Monk" + ChatColor.DARK_GRAY + "]");
		titles.put("Templar", ChatColor.DARK_GRAY + "[" + ChatColor.YELLOW + "Templar" + ChatColor.DARK_GRAY + "]");

		pages.setHeader(ChatColor.DARK_GRAY + fill + header + fill);
		pages.setError("Chat");
		pages.setCommand("chat");
		pages.setPage(commands);

		localrange = Main.getConfigChats().getInt("Range.Local");
		shoutrange = Main.getConfigChats().getInt("Range.Shout");
		roleplayrange = Main.getConfigChats().getInt("Range.Roleplay");
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

	@EventHandler
	public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event)
	{
		Caster caster = Main.getCasters().get(event.getPlayer().getUniqueId());

		String message = "";

		switch (caster.getChannel())
		{
			case "Global":
				message += ChatColor.DARK_GRAY + "[" + ChatColor.AQUA + "GBL" + ChatColor.DARK_GRAY + "] ";
				break;

			case "Local":
				message += ChatColor.DARK_GRAY + "[" + ChatColor.YELLOW + "LOC" + ChatColor.DARK_GRAY + "] ";

				for (Player player : Bukkit.getOnlinePlayers())
				{
					if (!caster.getPlayer().getNearbyEntities(localrange, localrange, localrange).contains(player))
					{
						event.getRecipients().remove(player);
					}
				}

				break;

			case "Shout":
				message += ChatColor.DARK_GRAY + "[" + ChatColor.RED + "SHO" + ChatColor.DARK_GRAY + "] ";

				for (Player player : Bukkit.getOnlinePlayers())
				{
					if (!caster.getPlayer().getNearbyEntities(shoutrange, shoutrange, shoutrange).contains(player))
					{
						event.getRecipients().remove(player);
					}
				}

				break;

			case "Roleplay":
				message += ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + "RPG" + ChatColor.DARK_GRAY + "] ";

				for (Player player : Bukkit.getOnlinePlayers())
				{
					if (!caster.getPlayer().getNearbyEntities(roleplayrange, roleplayrange, roleplayrange).contains(player))
					{
						event.getRecipients().remove(player);
					}
				}

				break;

			case "Looking For Group":
				message += ChatColor.DARK_GRAY + "[" + ChatColor.DARK_PURPLE + "LFG" + ChatColor.DARK_GRAY + "] ";
				break;

			case "Trade":
				message += ChatColor.DARK_GRAY + "[" + ChatColor.DARK_GREEN + "TRD" + ChatColor.DARK_GRAY + "] ";
				break;

			case "Help":
				message += ChatColor.DARK_GRAY + "[" + ChatColor.BLUE + "HLP" + ChatColor.DARK_GRAY + "] ";
				break;

			case "Party":
				message += ChatColor.DARK_GRAY + "[" + ChatColor.GOLD + "PTY" + ChatColor.DARK_GRAY + "] ";

				for (Caster player : Main.getCasters().values())
				{
					if (!caster.getParty().getMembers().contains(player))
					{
						event.getRecipients().remove(player);
					}
				}

				break;

			default:
				break;
		}

		if (titles.containsKey(caster.getTitle()))
		{
			message += titles.get(caster.getTitle()) + " ";
		}

		event.setFormat(message + ChatColor.WHITE + event.getPlayer().getDisplayName() + ChatColor.DARK_AQUA + " >> " + ChatColor.GRAY + event.getMessage());
	}
}
