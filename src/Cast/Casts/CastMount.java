package Cast.Casts;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.inventory.ItemStack;

import Cast.CommandInterface;
import Cast.Main;
import Cast.Casts.Types.ActiveCast;
import Cast.Essentials.Caster;

public class CastMount extends ActiveCast implements CommandInterface, Listener
{
	private static List<Horse> horses;

	public CastMount(String name)
	{
		super(name);

		warmup.setDuration(0);
		warmup.setAmplifier(0);
		cooldown.setCooldown(40);
		manacost = 3;

		info.add(ChatColor.DARK_AQUA + name + " Cast:");
		info.add(ChatColor.DARK_AQUA + "WarmUp: " + ChatColor.GRAY + warmup.getDuration() / 20.0 + " Seconds.");
		info.add(ChatColor.DARK_AQUA + "Cooldown: " + ChatColor.GRAY + cooldown.getCooldown() / 20.0 + " Seconds.");
		info.add(ChatColor.DARK_AQUA + "Cost: " + ChatColor.GRAY + manacost + " MP.");

		pages.setPage(info);

		horses = new ArrayList<Horse>();
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
				if (player.isInsideVehicle())
				{
					player.sendMessage(header + ChatColor.GRAY + " You Cannot Be Riding Anything When Using "
							+ ChatColor.WHITE + name + ChatColor.GRAY + "!");

					return false;
				}

				Horse horse = (Horse) player.getWorld().spawnEntity(player.getLocation(), EntityType.HORSE);
				horse.setAdult();
				horse.getInventory().setSaddle(new ItemStack(Material.SADDLE));
				horse.setOwner(player);
				horse.addPassenger(player);
				horses.add(horse);
			}
		}

		return false;
	}

	@EventHandler
	public void onVehicleExitEvent(VehicleExitEvent event)
	{
		if (event.getVehicle() instanceof Horse)
		{
			if (horses.contains((Horse) event.getVehicle()))
			{
				horses.remove((Horse) event.getVehicle());
				event.getVehicle().remove();
			}
		}
	}
}
