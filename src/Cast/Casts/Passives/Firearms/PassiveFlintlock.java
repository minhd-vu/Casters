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

public class PassiveFlintlock extends Firearm
{
	public PassiveFlintlock(String name, String description)
	{
		super(name, description);

		firearm = Material.IRON_BARDING;

		warmup.setDuration(0);
		warmup.setAmplifier(0);
		cooldown.setCooldown(20);

		damage = 25;
		headshot = 1.25;
		shots = 1;
		velocity = 2.0;
		maxaccuracy = 0.2;
		minaccuracy = 0.1;
		timer = 100;
		reload = 40;
		gravity = false;

		info.add(ChatColor.DARK_AQUA + "Damage: " + damage + " HP");
		info.add(ChatColor.DARK_AQUA + "Headshot: " + headshot * 100 + "%");
		info.add(ChatColor.DARK_AQUA + "Reload: " + reload / 20.0 + " Seconds");

		pages.setPage(info);
	}
}
