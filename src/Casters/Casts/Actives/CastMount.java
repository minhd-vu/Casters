package Casters.Casts.Actives;

import Casters.Casters;
import Casters.CommandInterface;
import Casters.Essentials.Caster;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CastMount extends Active implements CommandInterface, Listener
{
	private static List<UUID> horses;

	public CastMount(String name, String description)
	{
		super(name, description);

		warmup.setDuration(20);
		warmup.setAmplifier(5);
		cooldown.setCooldown(40);
		manacost = 3;

		info.add(ChatColor.DARK_AQUA + "WarmUp: " + ChatColor.GRAY + warmup.getDuration() / 20.0 + " Seconds");
		info.add(ChatColor.DARK_AQUA + "Cooldown: " + ChatColor.GRAY + cooldown.getCooldown() / 20.0 + " Seconds");
		info.add(ChatColor.DARK_AQUA + "Cost: " + ChatColor.GRAY + manacost + " MP");

		pages.setPage(info);

		horses = new ArrayList<UUID>();
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
				if (player.isInsideVehicle())
				{
					player.sendMessage(header + ChatColor.GRAY + " You Cannot Be Riding Anything When Using " + ChatColor.WHITE + name + ChatColor.GRAY + "!");

					return false;
				}

				warmup.start(caster, name);

				new BukkitRunnable()
				{
					@Override
					public void run()
					{
						if (!caster.isInterrupted())
						{
							caster.setCasting(name, true);

							Horse horse = (Horse) player.getWorld().spawnEntity(player.getLocation(), EntityType.HORSE);
							horse.setAdult();
							horse.getInventory().setSaddle(new ItemStack(Material.SADDLE));
							horse.setOwner(player);
							horse.addPassenger(player);
							horses.add(horse.getUniqueId());

							cast(player);

							caster.setCasting(name, false);
						}

						cooldown.start(player.getName());
					}

				}.runTaskLater(Casters.getInstance(), warmup.getDuration());
			}
		}

		return false;
	}

	@EventHandler
	public void onVehicleExitEvent(VehicleExitEvent event)
	{
		if (event.getVehicle() instanceof Horse)
		{
			if (horses.contains(event.getVehicle().getUniqueId()))
			{
				horses.remove(event.getVehicle().getUniqueId());
				event.getVehicle().remove();
			}
		}
	}

	@EventHandler
	public void onVehicleEnterEvent(VehicleEnterEvent event)
	{
		if (event.getVehicle() instanceof Horse)
		{
			Caster caster = Casters.getCasters().get(event.getEntered().getUniqueId());

			if (!caster.getCasts().containsKey(name))
			{
				caster.getPlayer().sendMessage(header + ChatColor.GRAY + " You Must Be Have The Cast " + ChatColor.WHITE + name + ChatColor.GRAY + " To Ride A Horse!");

				event.setCancelled(true);
			}
		}
	}
}
