package Casters.Casts.Actives;

import Casters.Casters;
import Casters.CommandInterface;
import Casters.Essentials.Caster;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class CastWhirlwind extends Active implements CommandInterface
{
	private double damage;
	private int range;
	private int period;
	private int duration;
	private int amplifier;

	public CastWhirlwind(String name, String description)
	{
		super(name, description);

		warmup.setDuration(0);
		warmup.setAmplifier(0);
		cooldown.setCooldown(40);
		manacost = 3;

		info.add(ChatColor.DARK_AQUA + "WarmUp: " + ChatColor.GRAY + warmup.getDuration() / 20.0 + " Seconds");
		info.add(ChatColor.DARK_AQUA + "Cooldown: " + ChatColor.GRAY + cooldown.getCooldown() / 20.0 + " Seconds");
		info.add(ChatColor.DARK_AQUA + "Cost: " + ChatColor.GRAY + manacost + " MP");

		damage = 5;
		range = 2;
		period = 10;
		duration = 100;
		amplifier = 3;

		info.add(ChatColor.DARK_AQUA + "Damage: " + ChatColor.GRAY + damage + " HP");
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

							player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, duration, amplifier));

							caster.setEffect("Spinning", duration);

							new BukkitRunnable()
							{
								@Override
								public void run()
								{
									if (!caster.hasEffect("Spinning") || caster.isInterrupted())
									{
										player.removePotionEffect(PotionEffectType.SLOW);
										caster.setEffect("Spinning", 0);
										caster.setCasting(name, false); // TODO: Test Interrupt With Kuro.
										decast(player);

										cancel();
										return;
									}

									List<Entity> entities = player.getNearbyEntities(range, range, range);

									for (Entity entity : entities)
									{
										if (entity instanceof LivingEntity && entity.isValid())
										{
											((LivingEntity) entity).damage(damage);
											caster.setBossBarEntity((Damageable) entity);
											entity.getWorld().playSound(((LivingEntity) entity).getEyeLocation(), Sound.ENTITY_ITEM_BREAK, 8.0F, 2.0F);
										}
									}

									player.getWorld().playSound(player.getEyeLocation(), Sound.ENTITY_BLAZE_SHOOT, 8.0F, -1.0F);
								}

							}.runTaskTimer(Casters.getInstance(), 0, period);

							cast(player);
						}

						cooldown.start(player.getName());
					}

				}.runTaskLater(Casters.getInstance(), warmup.getDuration());
			}
		}

		return true;
	}
}
