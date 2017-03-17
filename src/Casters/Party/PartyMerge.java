package Casters.Party;

import Casters.Casters;
import Casters.CommandInterface;
import Casters.Essentials.Caster;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class PartyMerge implements CommandInterface
{
	private static final int duration = 30000;

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if (sender instanceof Player)
		{
			Player player = (Player) sender;
			Caster caster = Casters.getCasters().get(player.getUniqueId());

			if (args.length == 2)
			{
				if (caster.hasParty())
				{
					if (caster.isLeader())
					{
						if (args[1].equalsIgnoreCase("accept"))
						{
							if (caster.hasMergeRequest())
							{
								Caster scaster = caster.getMergeRequest().getSender();

								for (Caster member : caster.getParty().getMembers())
								{
									scaster.getParty().getMembers().add(member);
									member.setParty(scaster.getParty());

									member.getPlayer().sendMessage(
											Party.header + " Your Party Has Been Merged With " + ChatColor.WHITE + scaster.getPlayer().getName() + "'s" + ChatColor.GRAY +
													" Party!"); // TODO: Test Merging Parties.
								}

								caster.setMergeRequest(null);
							}

							else
							{
								player.sendMessage(Party.header + " You Do Not Have Pending Merge Request!");
							}

							return true;
						}

						if (args[2].equalsIgnoreCase("decline"))
						{
							if (caster.hasMergeRequest())
							{
								caster.getMergeRequest().getSender().getPlayer()
										.sendMessage(Party.header + ChatColor.WHITE + player.getName() + " Declines Your Party Merge Request.");
								caster.setMergeRequest(null);
								player.sendMessage(Party.header + ChatColor.WHITE + " You" + ChatColor.GRAY + " Decline The Party Merge Request.");
							}

							else
							{
								player.sendMessage(Party.header + " You Do Not Have Pending Merge Request!");
							}

							return true;
						}

						Player target = Bukkit.getPlayer(args[1]);

						if (player != null)
						{
							Caster tcaster = Casters.getCasters().get(target.getUniqueId());

							if (!caster.sameParty(target))
							{
								if (tcaster.isLeader())
								{
									tcaster.setMergeRequest(new Invite(caster));

									player.sendMessage(Party.header + ChatColor.GRAY + " You Have Sent A Party Merge Request To " + target.getName() + ".");
									target.sendMessage(
											Party.header + player.getName() + " Has Sent " + ChatColor.WHITE + "You" + ChatColor.GRAY + " A Party " + ChatColor.WHITE + "Merge" +
													ChatColor.WHITE + " Request!");

									new BukkitRunnable()
									{
										@Override
										public void run()
										{
											if (tcaster.getInvite() != null)
											{
												tcaster.setInvite(null);
												tcaster.getPlayer().sendMessage(
														Party.header + ChatColor.GRAY + " " + ChatColor.WHITE + tcaster.getInvite().getSender().getPlayer().getName() + "'s " +
																ChatColor.GRAY + "Merge Request Has Expired.");
											}
										}

									}.runTaskLater(Casters.getInstance(), duration);
								}

								else
								{
									player.sendMessage(Party.header + ChatColor.GRAY + " You Can Only " + ChatColor.WHITE + "Request" + ChatColor.GRAY + " A " + ChatColor.WHITE +
											"Merge " + ChatColor.GRAY + "With A " + ChatColor.WHITE + "Party Leader" + ChatColor.GRAY + "!");
								}
							}

							else
							{
								player.sendMessage(
										Party.header + ChatColor.GRAY + ChatColor.WHITE + " You" + ChatColor.GRAY + " Cannot " + ChatColor.WHITE + "Request " + ChatColor.GRAY +
												"A Merge With A " + ChatColor.WHITE + "Party Member" + ChatColor.GRAY + "!");
							}
						}

						else
						{
							player.sendMessage(Party.header + ChatColor.GRAY + " Cannot Find That " + ChatColor.WHITE + "Player" + ChatColor.GRAY + "!");
						}
					}

					else
					{
						player.sendMessage(Party.header + ChatColor.GRAY + " You Must Be A " + ChatColor.WHITE + "Party Leader" + ChatColor.GRAY + " In Order To Merge Parties!");
					}
				}

				else
				{
					player.sendMessage(Party.header + ChatColor.GRAY + " You Must Be In A " + ChatColor.WHITE + "Party" + ChatColor.GRAY + " In Order To Merge Parties!");
				}
			}

			else
			{
				player.sendMessage(Party.header + ChatColor.GRAY + " Correct Usage: " + ChatColor.DARK_AQUA + "/party " + ChatColor.AQUA + " merge <leader>");
			}

		}

		return false;
	}

}
