package Cast.Casts.Passives;

import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import Cast.CommandInterface;
import Cast.Main;
import Cast.Casts.Types.Passive;
import Cast.Essentials.Caster;
import net.md_5.bungee.api.ChatColor;

public class PassiveBackstab extends Passive implements CommandInterface, Listener
{
	private int percentage;
	private int sneaking;

	public PassiveBackstab(String name)
	{
		super(name);

		percentage = 150;
		sneaking = 200;

		info.add(ChatColor.DARK_AQUA + "Bonus Damage: " + percentage + "%");
		info.add(ChatColor.DARK_AQUA + "Sneaking Damage: " + sneaking + "%");

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
		}

		return true;
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event)
	{
		if (event.getDamager() instanceof Player && event.getEntity() instanceof LivingEntity)
		{
			Player player = (Player) event.getDamager();
			Caster caster = Main.getCasters().get(player.getUniqueId());
			LivingEntity target = (LivingEntity) event.getEntity();

			if (caster.hasCast(name))
			{
				if (event.getEntity().getLocation().getDirection().dot(player.getLocation().getDirection()) > 0.0D)
				{
					if (player.isSneaking())
					{
						target.damage(event.getDamage() * (sneaking / 100));
					}

					else
					{
						target.damage(event.getDamage() * (percentage / 100));
					}

					target.getWorld().spigot().playEffect(target.getLocation(), Effect.COLOURED_DUST, 0, 0, 0.2F, 1.0F,
							0.2F, 0.0F, 30, 16);
					target.getWorld().playSound(target.getLocation(), Sound.ENTITY_ENDERDRAGON_HURT, 1.0F, 0.6F);

					event.setCancelled(true);
				}
			}
		}
	}
}
