package Cast.Guild;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import Cast.CommandInterface;
import Cast.Main;
import Cast.Essentials.Caster;

public class GuildCreate implements CommandInterface
{
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if (sender instanceof Player)
		{
			Player player = (Player) sender;
			Caster caster = Main.getCasters().get(player.getUniqueId());

			if (args.length == 2)
			{
				if (args[1].length() > 10)
				{
					player.sendMessage(Guilds.header + ChatColor.GRAY + " That Name Is Too Long!");
				}

				else
				{
					caster.setGuild(new Guild(args[1], player));
					player.sendMessage(Guilds.header + ChatColor.WHITE + " You" + ChatColor.GRAY
							+ " Have Successfully Created The Guild: " + ChatColor.WHITE + caster.getGuild().getName());
				}
			}
		}

		return true;
	}
}
