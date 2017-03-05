package Cast.Casts.Targetted;

import Cast.Casts.Types.TargettedCast;
import Cast.CommandInterface;
import Cast.Essentials.Caster;
import Cast.Main;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class CastSeeker extends TargettedCast implements CommandInterface, Listener
{
	private double damage;
	private int range;
	private int deletiontimer;

	public CastSeeker(String name, String description)
	{
		super(name ,description);

		warmup.setDuration(0);
		warmup.setAmplifier(0);
		cooldown.setCooldown(100);
		manacost = 5;

		info.add(ChatColor.DARK_AQUA + "WarmUp: " + ChatColor.GRAY + warmup.getDuration() / 20.0 + " Seconds");
		info.add(ChatColor.DARK_AQUA + "Cooldown: " + ChatColor.GRAY + cooldown.getCooldown() / 20.0 + " Seconds");
		info.add(ChatColor.DARK_AQUA + "Cost: " + ChatColor.GRAY + manacost + " MP");

		range = 10;
		damage = 10;
		deletiontimer = 600;

		info.add(ChatColor.DARK_AQUA + "Damage: " + ChatColor.GRAY + damage + " HP");
		info.add(ChatColor.DARK_AQUA + "Range: " + ChatColor.GRAY + range + " Blocks");

		pages.setPage(info);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if (sender instanceof Player)
		{
			Caster caster = Main.getCasters().get(((Player) sender).getUniqueId());

			if (args.length == 2 && args[1].equalsIgnoreCase("info"))
			{
				pages.display(caster.getPlayer(), args, 2);

				return true;
			}

			else if (args.length == 1 && caster.canCast(name, cooldown, manacost))
			{
				LivingEntity target = getTarget(caster.getPlayer(), range, false, false);

				if (target != null && !target.equals(caster.getPlayer()))
				{
					warmup.start(caster, name);

					new BukkitRunnable()
					{
						@SuppressWarnings("deprecation")
						@Override
						public void run()
						{
							caster.setCasting(name, true);
							caster.setMana(manacost);

							ShulkerBullet seeker = (ShulkerBullet) caster.getPlayer().getWorld().spawnEntity(caster.getPlayer().getLocation(), EntityType.SHULKER_BULLET);
							seeker.setShooter(caster.getPlayer());
							seeker.setTarget(target);

							new BukkitRunnable()
							{
								@Override
								public void run()
								{
									if (!seeker.isDead())
									{
										seeker.remove();
									}
								}
							}.runTaskLater(Main.getInstance(), deletiontimer);

							cast(caster.getPlayer());

							cooldown.start(caster.getPlayer().getName());

							caster.setCasting(name, false);
						}

					}.runTaskLater(Main.getInstance(), warmup.getDuration());
				}
			}
		}

		return false;
	}

	@EventHandler
	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event)
	{
		if (event.getDamager() instanceof ShulkerBullet)
		{
			ShulkerBullet seeker = (ShulkerBullet) event.getDamager();

			if (seeker.getShooter() instanceof Player)
			{
				Caster caster = Main.getCasters().get(((Player) seeker.getShooter()).getUniqueId());

				event.setDamage(damage);

				if (caster.hasParty())
				{
					if (event.getEntity() instanceof Player)
					{
						Caster target = Main.getCasters().get(event.getEntity().getUniqueId());

						if (caster.getParty().getMembers().contains(target))
						{
							event.setCancelled(true);
						}
					}
				}
			}
		}
	}
}
