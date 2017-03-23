package Casters.Casts.Actives;

import Casters.Casters;
import Casters.CommandInterface;
import Casters.Essentials.Caster;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BlockIterator;

public class CastFlash extends Active implements CommandInterface
{
	private int distance;

	public CastFlash(String name, String description)
	{
		super(name, description);

		warmup.setDuration(0);
		warmup.setAmplifier(0);
		cooldown.setCooldown(40);
		manacost = 3;

		info.add(ChatColor.DARK_AQUA + "WarmUp: " + ChatColor.GRAY + warmup.getDuration() / 20.0 + " Seconds");
		info.add(ChatColor.DARK_AQUA + "Cooldown: " + ChatColor.GRAY + cooldown.getCooldown() / 20.0 + " Seconds");
		info.add(ChatColor.DARK_AQUA + "Cost: " + ChatColor.GRAY + manacost + " MP");

		distance = 8;

		info.add(ChatColor.DARK_AQUA + "Distance: " + ChatColor.GRAY + distance + " Blocks");

		pages.setPage(info);
	}

	@SuppressWarnings("deprecated")
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

							BlockIterator blockiterator = new BlockIterator((LivingEntity) player, (int) distance);

							Location target = player.getLocation();

							while (blockiterator.hasNext())
							{
								Block block = blockiterator.next();

								target = block.getLocation().add(0, 1, 0);

								if (block.getType().isSolid())
								{
									break;
								}
							}

							player.getWorld().spigot().playEffect(player.getLocation().add(0, 1, 0), Effect.FLAME, 0, 0, 0.2F, 0.2F, 0.2F, 0.1F, 50, 2);
							player.getWorld().playSound(player.getEyeLocation(), Sound.ENTITY_BLAZE_SHOOT, 8.0F, -1.0F);
							player.teleport(target.setDirection(player.getEyeLocation().getDirection()));

							new BukkitRunnable()
							{
								@Override
								public void run()
								{
									player.getWorld().spigot().playEffect(player.getLocation().add(0, 1, 0), Effect.FLAME, 0, 0, 0.2F, 0.2F, 0.2F, 0.1F, 50, 2);
								}

							}.runTaskLater(Casters.getInstance(), 1);

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
