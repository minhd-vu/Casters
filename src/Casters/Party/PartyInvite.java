package Casters.Party;

import Casters.Casters;
import Casters.CommandInterface;
import Casters.Essentials.Caster;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class PartyInvite implements CommandInterface
{
	private static final int duration = 30000;

	private TextComponent accept;
	private TextComponent decline;

	public PartyInvite()
	{
		accept = new TextComponent(ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + "Accept" + ChatColor.DARK_GRAY + "]");
		accept.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.GRAY + "Accept The Party Request").create()));
		accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/party accept"));

		decline = new TextComponent(ChatColor.DARK_GRAY + "[" + ChatColor.RED + "Decline" + ChatColor.DARK_GRAY + "]");
		decline.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.GRAY + "Decline The Party Request").create()));
		decline.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/party decline"));
	}

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
						caster.getPlayer().sendMessage(Party.header + ChatColor.GRAY + " You Are Already In The Party.");
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

								TextComponent message = new TextComponent(
										Party.header + " " + ChatColor.WHITE + caster.getPlayer().getName() + ChatColor.GRAY + " Invites You To Join His/Her Party!");
								message.addExtra("\n");
								message.addExtra(accept);
								message.addExtra(" ");
								message.addExtra(decline);

								c.getPlayer().spigot().sendMessage(message);

								// TODO: Check If Clicking Works.

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
				//caster.getPlayer().performCommand("party create");
			}
		}

		return true;
	}

}
