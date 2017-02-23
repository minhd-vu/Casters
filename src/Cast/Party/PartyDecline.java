package Cast.Party;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import Cast.CommandInterface;
import Cast.Main;
import Cast.Essentials.Caster;

public class PartyDecline implements CommandInterface
{
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if (sender instanceof Player)
		{
			Player player = (Player) sender;
			Caster caster = Main.getCasters().get(player.getUniqueId());

			if (caster.hasInvite() && !caster.hasParty())
			{
				caster.getInvite().getSender().getPlayer().sendMessage(Party.header + ChatColor.WHITE + caster.getPlayer().getName() + ChatColor.GRAY + " Has Declined The Party Invitation.");
				caster.setInvite(null);
				caster.getPlayer().sendMessage(Party.header + ChatColor.RED + "You Have Declined The Party Invitation.");
			}

			else
			{
				caster.getPlayer().sendMessage(Party.header + ChatColor.GRAY + " You Do Not Have A Pending Party Invitation!");
			}
		}

		return false;
	}
}
