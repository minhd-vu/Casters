package Casters.Party;

import Casters.Casters;
import Casters.CommandInterface;
import Casters.Essentials.Caster;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class PartyInvite implements CommandInterface
{
	private static final int duration = 30000;

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if (sender instanceof Player)
		{
			Player player = (Player) sender;
			Caster caster = Casters.getCasters().get(player.getUniqueId());

			if (args.length != 2)
			{
				caster.getPlayer().sendMessage(Party.header + ChatColor.GRAY + " Correct Usage: " + ChatColor.DARK_AQUA
						+ "/party" + ChatColor.AQUA + " invite <name>");
				return false;
			}

			if (caster.hasParty())
			{
				if (caster.isLeader())
				{
					if (caster.getPlayer().getName().equals(args[1]))
					{
						caster.getPlayer()
								.sendMessage(Party.header + ChatColor.GRAY + " You Are Already In The Party.");
						return false;
					}

					for (Caster c : Casters.getCasters().values())
					{
						if (!c.equals(caster) && c.getPlayer().getName().equals(args[1]))
						{
							if (caster.getParty().getMembers().contains(c))
							{
								caster.getPlayer().sendMessage(Party.header + ChatColor.WHITE + " "
										+ c.getPlayer().getName() + ChatColor.GRAY + " Is Already In The Party.");
							}

							else if (!c.hasParty())
							{
								caster.getPlayer().sendMessage(
										Party.header + ChatColor.GRAY + " You Invite " + ChatColor.WHITE + c.getPlayer().getName() + ChatColor.GRAY + " To The Party.");
								c.setInvite(new Invite(caster));
								c.getPlayer().sendMessage(
										Party.header + " " + ChatColor.WHITE + caster.getPlayer().getName() + ChatColor.GRAY + " Invites You To Join His/Her Party!\n" +
												Party.header + ChatColor.DARK_AQUA + " /party" + ChatColor.AQUA + " accept" + ChatColor.GRAY + " To Join The Party.\n" +
												Party.header + ChatColor.DARK_AQUA + " /party" + ChatColor.AQUA + " decline" + ChatColor.GRAY +
												" To Decline The Invitation."); // TODO: Make These Clickable Buttons.

								new BukkitRunnable()
								{
									@Override
									public void run()
									{
										if (c.getInvite() != null)
										{
											c.setInvite(null);
											c.getPlayer().sendMessage(
													Party.header + ChatColor.GRAY + " " + c.getInvite().getSender().getPlayer().getName() + "'s Party Invitation Has Expired.");
										}
									}

								}.runTaskLater(Casters.getInstance(), duration);
							}

							else
							{
								caster.getPlayer().sendMessage(Party.header + ChatColor.WHITE + " " + c.getPlayer().getName() + ChatColor.GRAY + " Is Already In A Party.");
							}

							return true;
						}
					}

					caster.getPlayer().sendMessage(Party.header + ChatColor.GRAY + " That Player Is Not Online!");
				}

				else
				{
					caster.getPlayer().sendMessage(Party.header + ChatColor.GRAY + " You Must Be The Leader To Invite People!");
				}
			}

			else
			{
				caster.getPlayer().performCommand("party create");
			}
		}

		return true;
	}

}
