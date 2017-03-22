package Casters.Casts.Actives;

import Casters.Casters;
import Casters.CommandInterface;
import Casters.Essentials.Caster;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Makes All Party Members Or Enemies Spawn On Pigs.
 */
public class CastHogRiders extends Active implements CommandInterface, Listener
{
	private int duration;
	private int range;

	public CastHogRiders(String name, String description)
	{
		super(name, description);

		warmup.setDuration(20);
		warmup.setAmplifier(5);
		cooldown.setCooldown(40);
		manacost = 3;

		info.add(ChatColor.DARK_AQUA + "WarmUp: " + ChatColor.GRAY + warmup.getDuration() / 20.0 + " Seconds");
		info.add(ChatColor.DARK_AQUA + "Cooldown: " + ChatColor.GRAY + cooldown.getCooldown() / 20.0 + " Seconds");
		info.add(ChatColor.DARK_AQUA + "Cost: " + ChatColor.GRAY + manacost + " MP");

		duration = 100;
		range = 12;

		info.add(ChatColor.DARK_AQUA + "Duration: " + ChatColor.GRAY + duration + " HP");
		info.add(ChatColor.DARK_AQUA + "Range: " + ChatColor.GRAY + range + " Blocks");

		pages.setPage(info);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if (sender instanceof Player)
		{
			Player player = (Player) sender;
			Caster caster = Casters.getCasters().get(player.getUniqueId());

			if (args.length == 2 && args[1].equalsIgnoreCase("info"))
			{
				pages.display(player, args, 2);

				return true;
			}

			else if (args.length == 1 && caster.canCast(name, cooldown, manacost))
			{
				warmup.start(caster, name);

				new BukkitRunnable()
				{
					@Override
					public void run()
					{
						if (!caster.isInterrupted())
						{
							caster.setCasting(name, true);
							caster.setMana(manacost);

							for (Caster member : caster.getParty().getMembers())
							{
								if (caster.getPlayer().getLocation().distance(member.getPlayer().getLocation()) < range)
								{
									Pig pig = (Pig) member.getPlayer().getWorld().spawnEntity(member.getPlayer().getLocation(), EntityType.PIG);

									pig.setAdult();
									pig.setSaddle(true);
									pig.setAI(false);
									pig.addPassenger(member.getPlayer());

//									ItemStack item = member.getPlayer().getInventory().getItemInMainHand();


									new BukkitRunnable()
									{
										@Override
										public void run()
										{
											if (pig.isValid())
											{
												member.getPlayer().leaveVehicle();
												pig.remove();
											}
										}

									}.runTaskLater(Casters.getInstance(), duration);
								}
							}

							cast(player);

							caster.setCasting(name, false);
						}

						cooldown.start(player.getName());
					}

				}.runTaskLater(Casters.getInstance(), warmup.getDuration());
			}
		}
		return true;
	}
}

