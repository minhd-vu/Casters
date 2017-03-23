package Casters.Casts.Actives;

import Casters.Casters;
import Casters.CommandInterface;
import Casters.Essentials.Caster;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Set;

public class CastWarcry extends Active implements CommandInterface
{
	private int range;
	private int duration;
	private int speed;
	private int strength;

	public CastWarcry(String name, String description)
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
		duration = 100;
		speed = 3;
		strength = 3;

		info.add(ChatColor.DARK_AQUA + "Duration: " + ChatColor.GRAY + duration / 20.0 + " Seconds");
		info.add(ChatColor.DARK_AQUA + "Range: " + ChatColor.GRAY + range + " Blocks");
		info.add(ChatColor.DARK_AQUA + "Strength Boost: " + ChatColor.GRAY + strength);
		info.add(ChatColor.DARK_AQUA + "Speed Boost: " + ChatColor.GRAY + speed);

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

							for (Caster member : members)
							{
								if (caster.getPlayer().getLocation().distance(member.getPlayer().getLocation()) < range) // TODO: Test With Kuro.
								{
									member.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, duration, strength));
									member.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, duration, speed));
									member.setEffect("Warcry", duration);
									member.getPlayer().sendMessage(header + " The Cry Brings Out Your Warrior!");
								}
							}

							new BukkitRunnable()
							{
								@Override
								public void run()
								{
									for (Caster member : members)
									{
										member.getPlayer().sendMessage(header + " The Warcry Fades.");
									}
								}

							}.runTaskLater(Casters.getInstance(), duration);

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

