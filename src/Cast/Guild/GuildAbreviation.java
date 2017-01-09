package Cast.Guild;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import Cast.CommandInterface;
import Cast.Main;
import Cast.Essentials.Caster;
import net.md_5.bungee.api.ChatColor;

public class GuildAbreviation implements CommandInterface
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
			
			if (caster.getGuild().getLeader().equals(player.getUniqueId()))
			{
				if (args[1].length() > 4)
				{
					player.sendMessage(Guilds.header + " Abreviations Cannot Be Over Four Characters!");
					return false;
				}
				
				else
				{
					caster.getGuild().setAbreviation(args[1]);
					player.sendMessage(Guilds.header + ChatColor.GRAY + " You Have Successfully Set/Changed The Guild Abreviation.");
				}
			}
			
			else
			{
				player.sendMessage(Guilds.header + ChatColor.GRAY + " You Must Be A Leader To Change The Guild Abreviation.");
			}
		}
		
		return true;
	}

}
