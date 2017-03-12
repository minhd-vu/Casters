package Cast.Casts.Passives.Firearms;

import Cast.Essentials.Caster;
import Cast.Main;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LlamaSpit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class PassiveMusket extends Firearm
{
	public PassiveMusket(String name, String description)
	{
		super(name, description);

		damage = 5;
		shots = 1;
		velocity = 4.0;
		timer = 100;
		reload = 100;

		info.add(ChatColor.DARK_AQUA + "Damage: " + damage + " HP");
		info.add(ChatColor.DARK_AQUA + "Reload: " + reload / 20.0 + " Seconds");

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

			if (caster.hasCast(name))
			{
				if (player.getInventory().getItemInMainHand().getType().equals(Material.DIAMOND_BARDING))
				{
					LlamaSpit bullet = (LlamaSpit) player.getWorld().spawnEntity(player.getEyeLocation(), EntityType.LLAMA_SPIT);
					bullet.setShooter(player);
					bullet.setGravity(false);

					Vector velocity = caster.getPlayer().getLocation().getDirection();
					bullet.setVelocity(velocity.normalize().multiply(velocity));

					bullets.add(bullet);

					player.getWorld().playSound(player.getLocation(), Sound.ENTITY_LLAMA_SPIT, 8.0F, 1.0F);

					new BukkitRunnable()
					{
						@Override
						public void run()
						{
							bullets.remove(bullet);
							bullet.remove();
						}

					}.runTaskLater(Main.getInstance(), timer);
				}
			}
		}
	}

//	@SuppressWarnings("deprecation")
//	@EventHandler
//	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event)
//	{
//		if (event.getDamager() instanceof LlamaSpit)
//		{
//			LlamaSpit bullet = (LlamaSpit) event.getDamager();
//
//			if (bullet.getShooter() instanceof Player)
//			{
//				Caster caster = Main.getCasters().get(bullet.getShooter());
//
//				if (bullets.contains(bullet))
//				{
//					event.setDamage(damage);
//				}
//			}
//		}
//	}
}
