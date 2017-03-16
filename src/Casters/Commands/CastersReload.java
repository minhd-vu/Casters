package Casters.Commands;

import Casters.Casters;
import Casters.CommandInterface;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class CastersReload implements CommandInterface
{
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		Casters.getInstance().getServer().getPluginManager().disablePlugin(Casters.getInstance());
		Casters.getInstance().getServer().getPluginManager().enablePlugin(Casters.getInstance()); // TODO: Test Out Command.

		return false;
	}
}
