package Cast.Guild;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import Cast.CommandInterface;
import Cast.Main;
import Cast.Essentials.Caster;

public class GuildDescription implements CommandInterface
{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if (sender instanceof Player)
		{
			Player player = (Player) sender;
			Caster caster = Main.getCasters().get(player.getUniqueId());
			
			if (args.length < 2)
			{
				return false;
			}
			
			if (caster.getGuild().getLeader().equals(player.getUniqueId()))
			{
				String description = "";
				
				for (int i = 1; i < args.length; ++i)
				{
					description += args[i];
				}
				
				caster.getGuild().setDescription(description);
				
				player.sendMessage(Guilds.header + ChatColor.GRAY + " You Have Successfully Set/Changed The Guild Description.");
			}
			
			else
			{
				player.sendMessage(Guilds.header + ChatColor.GRAY + " You Must Be A Leader To Change The Description.");
			}
		}
		
		return true;
	}

}
