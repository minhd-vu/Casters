package Cast.Party;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import Cast.CommandInterface;
import Cast.Main;
import Cast.Essentials.Caster;

public class PartyInvite implements CommandInterface
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
				if (caster.getParty() != null)
				{
					for (Caster c : Main.getCasters().values())
					{
						if (c.getPlayer().getName().equals(args[1]))
						{
							c.setInvite(new Invite(caster));
							c.getPlayer().sendMessage(Party.header + caster.getPlayer().getName() + " Invites You To Join His/Her Party!\n" + Party.header + "Do /party accept or decline");
						}
					}
				}
			}
		}
		
		return false;
	}

}
