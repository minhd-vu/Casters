package Cast.Guild;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import Cast.CommandInterface;
import Cast.Main;
import Cast.Essentials.Caster;
import net.md_5.bungee.api.ChatColor;

public class GuildInvite implements CommandInterface
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
			
			if (caster.getGuild().getOfficers().contains(player.getUniqueId()) ||
					caster.getGuild().getLeader().equals(player.getUniqueId()))
			{
				if (Bukkit.getPlayer(args[1]) != null)
				{
					Player recruit = Bukkit.getPlayer(args[1]);
					caster.getGuild().getPending().add(recruit.getUniqueId());
					recruit.sendMessage(Guilds.header + ChatColor.WHITE + " You" + ChatColor.GRAY
							+ " Have Been Invited To Join The Guild: " + ChatColor.WHITE + caster.getGuild().getName() + "\n"
							+ ChatColor.GRAY + "Enter: /guild join " + ChatColor.WHITE + caster.getGuild().getName());
					player.sendMessage(Guilds.header + " You Have Successfully Invited " + recruit.getName() + " To The Guild.");
				}
				
				else
				{
					player.sendMessage(Guilds.header + " That Player Is Not Currently Online.");
				}
			}
			
			else
			{
				player.sendMessage(Guilds.header + " You Must Be A Leader/Officer To Invite Other People To The Guild.");
			}
		}
		
		return true;
	}

}
