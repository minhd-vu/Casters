package Cast.Casts.Passives;

import Cast.Casts.Types.Passive;
import Cast.CommandInterface;
import Cast.Essentials.Caster;
import Cast.Main;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LlamaSpit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class PassiveFirearms extends Passive implements CommandInterface, Listener
{
	private double irondamage;
	private double golddamage;
	private double diamonddamage;

	private double numberofshots;

	private double velocity;

	private int timer;

	public PassiveFirearms(String name, String description)
	{
		super(name, description);

		irondamage = 3;
		golddamage = 2;
		diamonddamage = 3;

		numberofshots = 5;
		velocity = 2.0;

		timer = 100;

		info.add(ChatColor.DARK_AQUA + "Gold Damage: " + golddamage + " HP");
		info.add(ChatColor.DARK_AQUA + "Iron Damage: " + irondamage + " HP");
		info.add(ChatColor.DARK_AQUA + "Diamond Damage: " + diamonddamage + " HP");

		pages.setPage(info);
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event)
	{
		if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
		{
			Player player = event.getPlayer();
			Caster caster = Main.getCasters().get(player.getUniqueId());

			if (player.getInventory().getItemInMainHand().getType().equals(Material.IRON_BARDING))
			{
				for (int i = 0; i < numberofshots; ++i)
				{
					// TODO: Add In Multiple Shots For Shotgun.
				}

				LlamaSpit bullet = (LlamaSpit) player.getWorld().spawnEntity(player.getEyeLocation(), EntityType.LLAMA_SPIT);
				bullet.setShooter(player);
				bullet.setGravity(false);
				bullet.setVelocity(caster.getPlayer().getEyeLocation().getDirection().normalize().multiply(velocity));

				player.getWorld().playSound(player.getLocation(), Sound.ENTITY_LLAMA_SPIT, 8.0F, 1.0F);

				new BukkitRunnable()
				{
					@Override
					public void run()
					{
						if (!bullet.isDead())
						{
							bullet.remove();
						}
					}

				}.runTaskLater(Main.getInstance(), timer);
			}
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event)
	{
		if (event.getDamager() instanceof LlamaSpit)
		{
			LlamaSpit bullet = (LlamaSpit) event.getDamager();

			if (event.getEntity() instanceof Damageable)
			{
				event.setDamage(irondamage);
			}
		}
	}
}
