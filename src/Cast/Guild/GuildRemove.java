package Cast.Guild;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import Cast.CommandInterface;
import Cast.Main;
import Cast.Essentials.Caster;
import net.md_5.bungee.api.ChatColor;

public class GuildRemove implements CommandInterface
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

			if (caster.getGuild().getOfficers().contains(caster.getPlayer().getUniqueId())
					|| caster.getGuild().getLeader().equals(caster.getPlayer().getUniqueId()))
			{
				if (Bukkit.getPlayer(args[1]) != null)
				{
					Player removed = Bukkit.getPlayer(args[1]);
					caster.getGuild().getMembers().remove(removed.getUniqueId());

					if (caster.getGuild().getLeader().equals(caster.getPlayer().getUniqueId()))
					{
						caster.getGuild().getMembers().remove(removed.getUniqueId());
					}
				}

				for (OfflinePlayer removed : Bukkit.getOfflinePlayers())
				{
					caster.getGuild().getMembers().remove(removed.getPlayer().getUniqueId());

					if (caster.getGuild().getLeader().equals(caster.getPlayer().getUniqueId()))
					{
						caster.getGuild().getMembers().remove(removed.getPlayer().getUniqueId());
						caster.getGuild().getOfficers().remove(removed.getPlayer().getUniqueId());
					}
				}

				player.sendMessage(Guilds.header + ChatColor.GRAY + " You Have Succesfully Removed That Player From Your Guild.");
			}

			else
			{
				player.sendMessage(Guilds.header + ChatColor.GRAY
						+ " You Must Be A Leader Or Officer To Be Able To Remove Members.");
			}

		}

		return true;
	}

}
