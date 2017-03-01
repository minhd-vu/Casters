package Cast.Casts.Passives;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import Cast.CommandInterface;
import Cast.Main;
import Cast.Casts.Types.Passive;
import Cast.Essentials.Caster;
import net.md_5.bungee.api.ChatColor;

public class PassiveFlameshield extends Passive implements CommandInterface, Listener
{
	private double percentage;

	public PassiveFlameshield(String name, String description)
	{
		super(name, description);

		percentage = 50;

		info.add(ChatColor.DARK_AQUA + "Fire Damage Reduction: " + percentage + "%");

		pages.setPage(info);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if (sender instanceof Player)
		{
			Player player = (Player) sender;
			@SuppressWarnings("unused")
			Caster caster = Main.getCasters().get(player.getUniqueId());

			if (args.length == 2 && args[1].equalsIgnoreCase("info"))
			{
				pages.display(player, args, 2);

				return true;
			}

			else if (args.length == 1)
			{
				player.sendMessage(header + " You Cannot Cast Passives! They Are Always Active.");
			}
		}

		return true;
	}

	@EventHandler
	public void onEntityDamageEvent(EntityDamageEvent event)
	{
		if (event.getEntity() instanceof Player)
		{
			Caster caster = Main.getCasters().get(event.getEntity().getUniqueId());

			if (caster.hasCast(name))
			{
				if (event.getCause().equals(DamageCause.FIRE) || event.getCause().equals(DamageCause.FIRE_TICK))
				{
					event.setDamage(event.getDamage() * percentage / 100.0);
				}
			}
		}
	}
}
