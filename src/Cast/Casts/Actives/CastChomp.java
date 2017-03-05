package Cast.Casts.Actives;

import Cast.Casts.Types.ActiveCast;
import Cast.CommandInterface;
import Cast.Essentials.Caster;
import Cast.Main;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BlockIterator;

public class CastChomp extends ActiveCast implements CommandInterface, Listener
{
	private double range;
	private double damage;

	public CastChomp(String name, String description)
	{
		super(name, description);

		warmup.setDuration(0);
		warmup.setAmplifier(0);
		cooldown.setCooldown(100);
		manacost = 5;

		info.add(ChatColor.DARK_AQUA + "WarmUp: " + ChatColor.GRAY + warmup.getDuration() / 20.0 + " Seconds");
		info.add(ChatColor.DARK_AQUA + "Cooldown: " + ChatColor.GRAY + cooldown.getCooldown() / 20.0 + " Seconds");
		info.add(ChatColor.DARK_AQUA + "Cost: " + ChatColor.GRAY + manacost + " MP");

		range = 10;
		damage = 10;

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
			Caster caster = Main.getCasters().get(player.getUniqueId());

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
					@SuppressWarnings("deprecation")
					@Override
					public void run()
					{
						caster.setCasting(name, true);
						caster.setMana(manacost);

						BlockIterator iter = new BlockIterator((LivingEntity) player, (int) range);

						new BukkitRunnable()
						{
							@Override
							public void run()
							{
								if (iter.hasNext())
								{
									Block block = iter.next();

									((EvokerFangs) player.getWorld().spawnEntity(block.getLocation().clone().add(0, -1, 0), EntityType.EVOKER_FANGS)).setOwner(player);

									if (block.getType().isSolid())
									{
										cancel();
									}
								}
							}
						}.runTaskTimer(Main.getInstance(), 0, 2);

						cast(player);

						cooldown.start(player.getName());

						caster.setCasting(name, false);
					}

				}.runTaskLater(Main.getInstance(), warmup.getDuration());
			}
		}

		return true;
	}

	@EventHandler
	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event)
	{
		if (event.getDamager() instanceof EvokerFangs)
		{
			EvokerFangs chomp = (EvokerFangs) event.getDamager();

			if (chomp.getOwner() instanceof Player)
			{
				Caster caster = Main.getCasters().get(chomp.getOwner().getUniqueId());

				event.setDamage(damage);

				if (caster.hasParty())
				{
					if (event.getEntity() instanceof Player)
					{
						Caster target = Main.getCasters().get(event.getEntity().getUniqueId());

						if (caster.sameParty(target))
						{
							event.setCancelled(true);
							return;
						}
					}
				}

				if (event.getEntity() instanceof Damageable)
				{
					caster.setBossBarEntity((Damageable) event.getEntity());
				}
			}
		}
	}
}
