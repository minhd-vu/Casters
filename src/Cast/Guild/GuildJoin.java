package Cast.Guild;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import Cast.CommandInterface;
import Cast.Main;
import Cast.Essentials.Caster;

public class GuildJoin implements CommandInterface
{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if (sender instanceof Player)
		{
			Player player = (Player) sender;
			Caster caster = Main.getCasters().get(player.getUniqueId());
			
			if (args.length != 2)
			{
				return false;
			}
			
			if (caster.getGuild() != null)
			{
				player.sendMessage(Guilds.header + ChatColor.GRAY + " You Must Leave Your Current Guild Before You Can Join Another One.");
				return false;
			}
			
			for (Guild guild : Main.getGuilds())
			{
				if (guild.getName().equalsIgnoreCase(args[1]))
				{
					if (guild.getPending().contains(player.getUniqueId()))
					{
						caster.setGuild(guild);
						guild.getMembers().add(player.getUniqueId());
					}
					
					else
					{
						player.sendMessage(Guilds.header + ChatColor.GRAY + " You Must Be Invited Before You Can Join This Guild.");
						return false;
					}
				}
			}
		}
		
		return false;
	}

}
