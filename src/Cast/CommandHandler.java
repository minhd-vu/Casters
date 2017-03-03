package Cast;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class CommandHandler implements CommandExecutor
{
	private HashMap<String, CommandInterface> commands = new HashMap<String, CommandInterface>();

	public void register(String name, CommandInterface cmd)
	{
		commands.put(name, cmd);
	}

	public boolean exists(String name)
	{
		return commands.containsKey(name);
	}

	public CommandInterface getExecutor(String name)
	{
		return commands.get(name);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if (sender instanceof Player)
		{
			if (args.length == 0)
			{
				getExecutor(cmd.getName().toLowerCase()).onCommand(sender, cmd, label, args);

				return true;
			}

			if (args.length > 0)
			{
				try
				{
					if (Integer.parseInt(args[0]) > 0)
					{
						getExecutor(cmd.getName().toLowerCase()).onCommand(sender, cmd, label, args);

						return true;
					}
				}
				catch (NumberFormatException e)
				{

				}

				if (exists(args[0].toLowerCase()))
				{
					getExecutor(args[0].toLowerCase()).onCommand(sender, cmd, label, args);

					return true;
				}
				else
				{
					sender.sendMessage(ChatColor.RED + "That Command Does Not Exist!");

					return true;
				}
			}
		}
		else
		{
			sender.sendMessage(ChatColor.RED + "Player Sender Required.");

			return true;
		}

		return false;
	}
}
