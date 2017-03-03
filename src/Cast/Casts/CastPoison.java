package Cast.Casts;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import Cast.CommandInterface;
import Cast.Main;
import Cast.Casts.Types.TargettedCast;
import Cast.Essentials.Caster;

public class CastPoison extends TargettedCast implements CommandInterface, Listener
{
	private int duration;
	private int range;
	private int damagepertick;
	private int amplifier;

	private List<LivingEntity> poisons = new ArrayList<LivingEntity>();

	public CastPoison(String name, String description)
	{
		super(name, description);

		warmup.setDuration(0);
		warmup.setAmplifier(0);
		cooldown.setCooldown(40);
		manacost = 3;

		info.add(ChatColor.DARK_AQUA + "WarmUp: " + ChatColor.GRAY + warmup.getDuration() / 20.0 + " Seconds");
		info.add(ChatColor.DARK_AQUA + "Cooldown: " + ChatColor.GRAY + cooldown.getCooldown() / 20.0 + " Seconds");
		info.add(ChatColor.DARK_AQUA + "Cost: " + ChatColor.GRAY + manacost + " MP");

		duration = 40;
		range = 10;
		damagepertick = 2;
		amplifier = 2;

		info.add(ChatColor.DARK_AQUA + "Duration: " + ChatColor.GRAY + duration / 20.0 + " Seconds");
		info.add(ChatColor.DARK_AQUA + "Range: " + ChatColor.GRAY + range + " Blocks");
		info.add(ChatColor.DARK_AQUA + "Damage Per Tick: " + ChatColor.GRAY + damagepertick + " HP");
		info.add(ChatColor.DARK_AQUA + "Amplifier: " + ChatColor.GRAY + amplifier);

		pages.setPage(info);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if (sender instanceof Player)
		{
			Player player = (Player) sender;
			Caster caster = Main.getCasters().get(player.getUniqueId());

			if (args.length == 2 && args[1].equalsIgnoreCase("info"))
			{
				pages.display(player, args, 2);

				return true;
			}

			else if (args.length == 1 && caster.canCast(name, cooldown, manacost))
			{

				LivingEntity target = getTarget(player, range, false);

				if (target != null && !target.equals(player))
				{
					warmup.start(caster, name);

					new BukkitRunnable()
					{
						@SuppressWarnings("deprecation")
						@Override
						public void run()
						{
							caster.setCasting(name, true);
							caster.setEffect("Poisoning", duration);
							caster.setMana(manacost);

							target.addPotionEffect(new PotionEffect(PotionEffectType.POISON, duration, amplifier));
							target.getWorld().spigot().playEffect(target.getLocation().add(0, 1, 0), Effect.SLIME, 0, 0,
									0.5F, 1.0F, 0.5F, 0.1F, 50, 16);
							target.getWorld().playSound(target.getLocation(), Sound.BLOCK_GRAVEL_FALL, 1.0F, 1.0F);

							poisons.add(target);

							cast(player, target);

							if (target instanceof Player)
							{
								Caster targetcaster = Main.getCasters().get(target.getUniqueId());
								targetcaster.setEffect("Poisoned", duration);
								target.sendMessage(header + ChatColor.WHITE + " You" + ChatColor.GRAY + " Are "
										+ ChatColor.WHITE + "Poisoned" + ChatColor.GRAY + "!");
							}

							List<Entity> entities = target.getNearbyEntities(16, 16, 16);

							for (Entity player : entities)
							{
								if (player instanceof Player)
								{
									player.sendMessage(
											header + ChatColor.WHITE + " " + target.getName() + ChatColor.GRAY + " Is "
													+ ChatColor.WHITE + "Poisoned" + ChatColor.GRAY + "!");
								}
							}

							new BukkitRunnable()
							{
								@Override
								public void run()
								{
									poisons.remove(target);

									if (target instanceof Player)
									{
										target.sendMessage(header + ChatColor.WHITE + " You" + ChatColor.GRAY
												+ " Have Stopped Being " + ChatColor.WHITE + "Poisoned" + ChatColor.GRAY
												+ "!");
									}

									List<Entity> e = target.getNearbyEntities(16, 16, 16);

									for (Entity player : e)
									{
										if (player instanceof Player)
										{
											player.sendMessage(header + ChatColor.WHITE + " " + target.getName()
													+ ChatColor.GRAY + " Has Stopped Being " + ChatColor.WHITE
													+ "Poisoned" + ChatColor.GRAY + "!");
										}
									}

									caster.setCasting(name, false);
								}

							}.runTaskLater(Main.getInstance(), duration);

							cooldown.start(player.getName());
						}

					}.runTaskLater(Main.getInstance(), warmup.getDuration());
				}
			}
		}

		return true;
	}

	@EventHandler
	public void onEntityDamageEvent(EntityDamageEvent event)
	{
		/*-if (event.getCause().equals(DamageCause.POISON))
		{
			if (event.getEntity() instanceof LivingEntity)
			{
				if (poisons.contains((LivingEntity) event.getEntity()))
				{
					event.setDamage(damagepertick);
				}
			}
		}*/
	}
}
