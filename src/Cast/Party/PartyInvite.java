package Cast.Party;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import Cast.CommandInterface;
import Cast.Main;
import Cast.Essentials.Caster;

public class PartyInvite implements CommandInterface
{
	private static final int duration = 30000;

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if (sender instanceof Player)
		{
			Player player = (Player) sender;
			Caster caster = Main.getCasters().get(player.getUniqueId());

			if (args.length != 2)
			{
				caster.getPlayer().sendMessage(Party.header + ChatColor.GRAY + " Correct Usage: " + ChatColor.DARK_AQUA + "/party" + ChatColor.AQUA + " invite <name>");
				return false;
			}

			if (caster.hasParty())
			{
				if (caster.getParty().getLeader().equals(caster))
				{
					if (caster.getPlayer().getName().equals(args[1]))
					{
						caster.getPlayer().sendMessage(Party.header + ChatColor.GRAY + " You Are Already In The Party.");
						return false;
					}

					for (Caster c : Main.getCasters().values())
					{
						if (!c.equals(caster) && c.getPlayer().getName().equals(args[1]))
						{
							if (caster.getParty().getMembers().contains(c))
							{
								caster.getPlayer().sendMessage(Party.header + ChatColor.WHITE + c.getPlayer().getName() + ChatColor.GRAY + " Is Already In The Party.");
							}

							else if (!c.hasParty())
							{
								c.setInvite(new Invite(caster));
								c.getPlayer().sendMessage(Party.header + caster.getPlayer().getName() + " Invites You To Join His/Her Party!\n" + Party.header + " Do " + ChatColor.DARK_AQUA + "/party" + ChatColor.AQUA + " accept" + ChatColor.GRAY + "To Join The Or" + ChatColor.DARK_AQUA + " /party" + ChatColor.AQUA + " decline" + ChatColor.GRAY + " To Decline The Invitation.");

								new BukkitRunnable()
								{
									@Override
									public void run()
									{
										if (c.getInvite() != null)
										{
											c.setInvite(null);
											c.getPlayer().sendMessage(Party.header + ChatColor.GRAY + " " + c.getInvite().getSender().getPlayer().getName() + "'s Party Invitation Has Expired.");
										}
									}

								}.runTaskLater(Main.getInstance(), duration);
							}

							else
							{
								caster.getPlayer().sendMessage(Party.header + ChatColor.WHITE + c.getPlayer().getName() + ChatColor.GRAY + " Is Already In A Party.");
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
				caster.getPlayer().sendMessage(Party.header + ChatColor.GRAY + " You Must Be In A Party To Invite People!");
			}
		}

		return true;
	}

}
