package Cast.Casts.Targetted;

import Cast.Casts.Types.TargettedCast;
import Cast.CommandInterface;
import Cast.Essentials.Caster;
import Cast.Main;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class CastBeasts extends TargettedCast implements CommandInterface, Listener
{
	private double duration;
	private int amount;
	private int range;
	private double wolfdamage;

	public CastBeasts(String name, String description)
	{
		super(name, description);

		warmup.setDuration(20);
		warmup.setAmplifier(5);
		cooldown.setCooldown(100);
		manacost = 3;

		info.add(ChatColor.DARK_AQUA + "WarmUp: " + ChatColor.GRAY + warmup.getDuration() / 20.0 + " Seconds");
		info.add(ChatColor.DARK_AQUA + "Cooldown: " + ChatColor.GRAY + cooldown.getCooldown() / 20.0 + " Seconds");
		info.add(ChatColor.DARK_AQUA + "Cost: " + ChatColor.GRAY + manacost + " MP");

		duration = 0;
		amount = 3;
		range = 10;
		wolfdamage = 3;

		info.add(duration == 0 ? ChatColor.DARK_AQUA + "Duration: " + ChatColor.GRAY + "Forever"
				: ChatColor.DARK_AQUA + "Duration: " + ChatColor.GRAY + duration / 20 + " Seconds");
		info.add(ChatColor.DARK_AQUA + "Amount: " + ChatColor.GRAY + amount + " Wolves");
		info.add(ChatColor.DARK_AQUA + "Range: " + ChatColor.GRAY + range + " Blocks");
		info.add(ChatColor.DARK_AQUA + "Wolf Damage: " + ChatColor.GRAY + wolfdamage + " HP");

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
				LivingEntity target = getTarget(player, range, false, false);

				if (target != null && !target.equals(player) && !(target instanceof Wolf))
				{
					warmup.start(caster, target, name);

					new BukkitRunnable()
					{
						@Override
						public void run()
						{
							caster.setCasting(name, true);
							caster.setMana(manacost);

							ArrayList<Wolf> wolves = new ArrayList<Wolf>();

							for (int i = 0; i < amount; ++i)
							{
								wolves.add((Wolf) player.getWorld().spawnEntity(player.getLocation(), EntityType.WOLF));
							}

							for (Wolf wolf : wolves)
							{
								wolf.setOwner(player);
								wolf.setTarget(target);
							}

							cast(player, target);

							cooldown.start(player.getName());

							caster.setCasting(name, false);

							if (duration > 0)
							{
								new BukkitRunnable()
								{
									@Override
									public void run()
									{
										for (Wolf wolf : wolves)
										{
											wolf.remove();
										}
									}

								}.runTaskLater(Main.getInstance(), (long) duration);
							}
						}

					}.runTaskLater(Main.getInstance(), warmup.getDuration());
				}
			}
		}

		return true;
	}

	@EventHandler
	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event)
	{
		if (event.getDamager() instanceof Wolf)
		{
			Wolf wolf = (Wolf) event.getDamager();

			if (wolf.getOwner() instanceof Player)
			{
				Caster caster = Main.getCasters().get(wolf.getOwner().getUniqueId());

				if (event.getEntity() instanceof LivingEntity)
				{
					if (event.getEntity() instanceof Player)
					{
						if (caster.sameParty(Main.getCasters().get(event.getEntity().getUniqueId())))
						{
							event.setCancelled(true);
						}
					}

					event.setDamage(wolfdamage);
					caster.setBossBarEntity((LivingEntity) event.getEntity());
				}
			}
		}
	}
}
