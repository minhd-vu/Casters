package Cast.Essentials.Chat;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import Cast.CommandInterface;
import Cast.Main;
import Cast.Essentials.Caster;

public class ChatChannel implements CommandInterface
{
	private Pages pages = new Pages();
	private List<String> commands = new ArrayList<String>();

	private String header = ChatColor.DARK_GRAY + "[" + ChatColor.DARK_AQUA + "Chat Channel" + ChatColor.DARK_GRAY
			+ "]";
	private String fill = "--------------------";

	public ChatChannel()
	{
		commands.add(ChatColor.AQUA + "GBL" + ChatColor.GRAY + " - Global Chat, Everyone Can See This.");
		commands.add(
				ChatColor.YELLOW + "LOC" + ChatColor.GRAY + " - Local Chat, People Within 64 Blocks Can See This.");
		commands.add(ChatColor.RED + "SHO" + ChatColor.GRAY + " - Shout Chat, People Within 128 Blocks Can See This.");
		commands.add(ChatColor.GREEN + "RPG" + ChatColor.GRAY + " - Roleplay Chat, For People Who Wish To RP");
		commands.add(ChatColor.DARK_PURPLE + "LFG" + ChatColor.GRAY
				+ " - Looking For Group Chat, For People Who Need A Group.");
		commands.add(ChatColor.DARK_GREEN + "TRD" + ChatColor.GRAY + " - Trade Chat, For People Who Wish To Trade.");
		commands.add(ChatColor.BLUE + "HLP" + ChatColor.GRAY + " - Help Chat, For Those Who Need Help.");

		pages.setHeader(ChatColor.DARK_GRAY + fill + header + fill);
		pages.setError("Chat Channel");
		pages.setCommand("chat channel");
		pages.setPage(commands);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if (sender instanceof Player)
		{
			Player player = (Player) sender;
			Caster caster = Main.getCasters().get(player.getUniqueId());

			if (args.length == 2)
			{
				switch (args[1].toLowerCase())
				{
					case "g":
					case "gbl":
					case "global":
						caster.setChannel("Global");
						player.sendMessage(header + ChatColor.GRAY + " You Have Joined The Global Channel!");
						break;

					case "l":
					case "loc":
					case "local":
						caster.setChannel("Local");
						player.sendMessage(header + ChatColor.GRAY + " You Have Joined The Local Channel!");
						break;

					case "s":
					case "sho":
					case "shout":
						caster.setChannel("Shout");
						player.sendMessage(header + ChatColor.GRAY + " You Have Joined The Shout Channel!");
						break;

					case "rp":
					case "rpg":
					case "roleplay":
						caster.setChannel("Roleplay");
						player.sendMessage(header + ChatColor.GRAY + " You Have Joined The Roleplay Channel!");
						break;

					case "lfg":
					case "lookingforgroup":
						caster.setChannel("Looking For Group");
						player.sendMessage(header + ChatColor.GRAY + " You Have Joined The Looking For Group Channel!");
						break;

					case "t":
					case "trd":
					case "trade":
						caster.setChannel("Trade");
						player.sendMessage(header + ChatColor.GRAY + " You Have Joined The Trade Channel!");
						break;

					case "h":
					case "hlp":
					case "help":
						caster.setChannel("Help");
						player.sendMessage(header + ChatColor.GRAY + " You Have Joined The Help Channel!");
						break;

					default:
						player.sendMessage(header + ChatColor.GRAY + " That Is Not A Valid Channel!");
						break;
				}

				return true;
			}

			pages.display(player, args, 1);
		}

		return true;
	}

}
