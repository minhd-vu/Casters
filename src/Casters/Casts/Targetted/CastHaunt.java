package Casters.Casts.Targetted;

import Casters.Casters;
import Casters.CommandInterface;
import Casters.Essentials.Caster;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class CastHaunt extends Targetted implements CommandInterface
{
	private int duration;
	private int range;
	private int damage;
	private int amplifier;

	public CastHaunt(String name, String description)
	{
		super(name, description);

		warmup.setDuration(0);
		warmup.setAmplifier(0);
		cooldown.setCooldown(40);
		manacost = 3;

		info.add(ChatColor.DARK_AQUA + "WarmUp: " + ChatColor.GRAY + warmup.getDuration() / 20.0 + " Seconds");
		info.add(ChatColor.DARK_AQUA + "Cooldown: " + ChatColor.GRAY + cooldown.getCooldown() / 20.0 + " Seconds");
		info.add(ChatColor.DARK_AQUA + "Cost: " + ChatColor.GRAY + manacost + " MP");

		duration = 60;
		range = 10;
		damage = 2;
		amplifier = 2;

		info.add(ChatColor.DARK_AQUA + "Duration: " + ChatColor.GRAY + duration / 20.0 + " Seconds");
		info.add(ChatColor.DARK_AQUA + "Range: " + ChatColor.GRAY + range + " Blocks");
		info.add(ChatColor.DARK_AQUA + "Damage: " + ChatColor.GRAY + damage + " HP");
		info.add(ChatColor.DARK_AQUA + "Amplifier: " + ChatColor.GRAY + amplifier);

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
				LivingEntity target = getTarget(player, range, false, false);

				if (target != null && !target.equals(player))
				{
					warmup.start(caster, name);

					new BukkitRunnable()
					{
						@SuppressWarnings("deprecation")
						@Override
						public void run()
						{
							if (!caster.isInterrupted())
							{
								caster.setCasting(name, true);
								caster.setEffect("Haunted", duration);
								caster.setMana(manacost);

								target.damage(damage);
								target.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, duration, amplifier));
								target.getWorld().playSound(target.getLocation(), Sound.ENTITY_ENDERMEN_SCREAM, 8.0F, 1.0F);

								caster.setBossBarEntity(target);

								cast(player, target);

								caster.setCasting(name, false);
							}

							cooldown.start(player.getName());
						}

					}.runTaskLater(Casters.getInstance(), warmup.getDuration());
				}
			}
		}

		return true;
	}
}
