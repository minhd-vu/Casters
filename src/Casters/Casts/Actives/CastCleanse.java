package Casters.Casts.Actives;

import Casters.Casters;
import Casters.CommandInterface;
import Casters.Essentials.Caster;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Set;

public class CastCleanse extends Active implements CommandInterface, Listener
{
	private int range;

	public CastCleanse(String name, String description)
	{
		super(name, description);

		warmup.setDuration(20);
		warmup.setAmplifier(5);
		cooldown.setCooldown(40);
		manacost = 3;

		info.add(ChatColor.DARK_AQUA + "WarmUp: " + ChatColor.GRAY + warmup.getDuration() / 20.0 + " Seconds");
		info.add(ChatColor.DARK_AQUA + "Cooldown: " + ChatColor.GRAY + cooldown.getCooldown() / 20.0 + " Seconds");
		info.add(ChatColor.DARK_AQUA + "Cost: " + ChatColor.GRAY + manacost + " MP");

		range = 6;

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

							Set<Caster> members = new HashSet<Caster>();

							if (caster.hasParty())
							{
								members.addAll(caster.getParty().getMembers());
							}

							else
							{
								members.add(caster);
							}

							cast(player);

							for (Caster member : members)
							{
								if (caster.getPlayer().getLocation().distance(member.getPlayer().getLocation()) < range)
								{
									if (member.getPlayer().getFireTicks() > 0 || member.getPlayer().hasPotionEffect(PotionEffectType.SLOW) ||
											member.getPlayer().hasPotionEffect(PotionEffectType.BLINDNESS) || member.getPlayer().hasPotionEffect(PotionEffectType.CONFUSION) ||
											member.getPlayer().hasPotionEffect(PotionEffectType.WEAKNESS) || member.getPlayer().hasPotionEffect(PotionEffectType.POISON) ||
											member.getPlayer().hasPotionEffect(PotionEffectType.HUNGER) || member.getPlayer().hasPotionEffect(PotionEffectType.SLOW_DIGGING) ||
											member.getPlayer().hasPotionEffect(PotionEffectType.WITHER) || member.getPlayer().hasPotionEffect(PotionEffectType.UNLUCK))
									{
										member.getPlayer().setFireTicks(0);

										if (!caster.isWarmingUp())
										{
											member.getPlayer().removePotionEffect(PotionEffectType.SLOW);
										}

										member.getPlayer().removePotionEffect(PotionEffectType.BLINDNESS);
										member.getPlayer().removePotionEffect(PotionEffectType.CONFUSION);
										member.getPlayer().removePotionEffect(PotionEffectType.WEAKNESS);
										member.getPlayer().removePotionEffect(PotionEffectType.POISON);
										member.getPlayer().removePotionEffect(PotionEffectType.HUNGER);
										member.getPlayer().removePotionEffect(PotionEffectType.SLOW_DIGGING);
										member.getPlayer().removePotionEffect(PotionEffectType.WITHER);
										member.getPlayer().removePotionEffect(PotionEffectType.UNLUCK);

										cast(caster.getPlayer(), member.getPlayer());
									}
								}
							}

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

