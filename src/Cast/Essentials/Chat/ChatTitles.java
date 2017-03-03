package Cast.Essentials.Chat;

import Cast.CommandInterface;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChatTitles extends Chat implements CommandInterface
{
	private String fill = "---------------------";

	public ChatTitles()
	{
		pages.setHeader(ChatColor.DARK_GRAY + fill + "-[" + ChatColor.DARK_AQUA + "Chat Titles" + ChatColor.DARK_GRAY
				+ "]" + fill);
		pages.setError("Chat Titles");
		pages.setCommand("chat titles");
		pages.setPage(titles);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if (sender instanceof Player)
		{
			Player player = (Player) sender;

			pages.display(player, args, 1);
		}

		return true;
	}
}
