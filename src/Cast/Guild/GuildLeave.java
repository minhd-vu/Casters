package Cast.Guild;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import Cast.CommandInterface;
import Cast.Main;
import Cast.Essentials.Caster;
import net.md_5.bungee.api.ChatColor;

public class GuildLeave implements CommandInterface
{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if (sender instanceof Player)
		{
			Player player = (Player) sender;
			Caster caster = Main.getCasters().get(player.getUniqueId());
			
			if (args.length != 1)
			{
				return false;
			}
			
			if (caster.getGuild() == null)
			{
				player.sendMessage(Guilds.header + ChatColor.GRAY
						+ " You Cannot Leave A Guild If You Were Not Part Of One In THe First Place!");
			}
			
			else if (caster.getGuild().getMembers().contains(player.getUniqueId()) ||
					caster.getGuild().getOfficers().contains(player.getUniqueId()))
			{
				caster.getGuild().getMembers().remove(player.getUniqueId());
				caster.getGuild().getOfficers().remove(player.getUniqueId());

				player.sendMessage(Guilds.header + ChatColor.GRAY + " You Have Successfully Left The Guild: "
						+ caster.getGuild().getName());

				caster.setGuild(null);
			}
			
			else if (caster.getGuild().getLeader().equals(player.getUniqueId()))
			{
				player.sendMessage(Guilds.header + ChatColor.GRAY
						+ " You Must Set Another Leader Before Leaving Or Disband The Disband The Guild All Together.");
			}
		}
		
		return true;
	}

}
