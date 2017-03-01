package Cast.Casts;

import java.util.List;

import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import Cast.CommandInterface;
import Cast.Main;
import Cast.Casts.Types.TargettedCast;
import Cast.Essentials.Caster;
import net.md_5.bungee.api.ChatColor;

public class CastPoison extends TargettedCast implements CommandInterface, Listener
{
	private int duration;
	private int range;
	private int amplifier;

	public CastPoison(String name, String description)
	{
		super(name, description);

		warmup.setDuration(0);
		warmup.setAmplifier(0);
		cooldown.setCooldown(40);
		manacost = 3;

		info.add(ChatColor.DARK_AQUA + "WarmUp: " + ChatColor.GRAY + warmup.getDuration() / 20.0 + " Seconds.");
		info.add(ChatColor.DARK_AQUA + "Cooldown: " + ChatColor.GRAY + cooldown.getCooldown() / 20.0 + " Seconds.");
		info.add(ChatColor.DARK_AQUA + "Cost: " + ChatColor.GRAY + manacost + " MP.");

		duration = 300;
		range = 10;
		amplifier = 1;

		info.add(ChatColor.DARK_AQUA + "Duration: " + ChatColor.GRAY + duration / 20.0 + " Seconds");
		info.add(ChatColor.DARK_AQUA + "Range: " + ChatColor.GRAY + range + " Blocks");
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

			else if (args.length == 1 && caster.hasCast(name) && !caster.isCasting(name) && !caster.isWarmingUp()
					&& !caster.isSilenced(name) && !caster.isStunned(name) && !cooldown.hasCooldown(player, name)
					&& caster.hasMana(manacost, name))
			{

				LivingEntity target = getTarget(player, range);

				if (target != null && !target.equals(player))
				{
					if (warmup.getDuration() > 0)
					{
						warmup.start(Main.getInstance(), caster, name);
					}

					new BukkitRunnable()
					{
						@SuppressWarnings("deprecation")
						@Override
						public void run()
						{
							caster.setCasting(name, true);
							caster.setEffect("Poisoning", duration);
							caster.setMana(manacost);

							target.addPotionEffect(
									new PotionEffect(PotionEffectType.INVISIBILITY, duration, amplifier));

							cast(player, target);

							cooldown.start(player.getName());

							caster.setCasting(name, true);
							caster.setMana(manacost);

							target.getWorld().spigot().playEffect(target.getLocation().add(0, 1, 0), Effect.SLIME, 0, 0,
									0.5F, 1.0F, 0.5F, 0.1F, 50, 16);
							target.getWorld().playSound(target.getLocation(), Sound.BLOCK_GRAVEL_FALL, 1.0F, 1.0F);

							cast(player, target);

							if (target instanceof Player)
							{
								Caster targetcaster = Main.getCasters().get(target.getUniqueId());
								targetcaster.setEffect("Poisoned", duration);
								target.sendMessage(header + "You" + ChatColor.GRAY + " Are " + ChatColor.WHITE
										+ "Poisoned" + ChatColor.GRAY + "!");
							}

							List<Entity> entities = target.getNearbyEntities(16, 16, 16);

							for (Entity player : entities)
							{
								if (player instanceof Player)
								{
									player.sendMessage(header + target.getName() + ChatColor.GRAY + " Is "
											+ ChatColor.WHITE + "Poisoned" + ChatColor.GRAY + "!");
								}
							}

							new BukkitRunnable()
							{
								@Override
								public void run()
								{
									if (target instanceof Player)
									{
										target.sendMessage(header + "You" + ChatColor.GRAY + " Have Stopped "
												+ ChatColor.WHITE + "Being Poisoned" + ChatColor.GRAY + "!");
									}

									List<Entity> e = target.getNearbyEntities(16, 16, 16);

									for (Entity player : e)
									{
										if (player instanceof Player)
										{
											player.sendMessage(header + target.getName() + ChatColor.GRAY
													+ " Has Stopped " + ChatColor.WHITE + "Being Poisoned"
													+ ChatColor.GRAY + "!");
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

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onEntityDamageEvent(EntityDamageEvent event)
	{
		if (event.getCause().equals(DamageCause.POISON))
		{
			if (event.getEntity() instanceof Player)
			{
				Player player = (Player) event.getEntity();

				if (player.hasPotionEffect(PotionEffectType.POISON))
				{
					event.setDamage(player.getMaxHealth()
							/ player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getDefaultValue());
				}
			}
		}
	}
}
