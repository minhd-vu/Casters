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

public class PassiveBlunderbuss extends Firearm
{
	public PassiveBlunderbuss(String name, String description)
	{
		super(name, description);

		firearm = Material.GOLD_BARDING;

		warmup.setDuration(0);
		warmup.setAmplifier(0);
		cooldown.setCooldown(70);

		damage = 50;
		headshot = 1.1;
		shots = 10;
		velocity = 1.0;
		maxaccuracy = 0.3;
		minaccuracy = 0.15;
		timer = 100;
		reload = 70;
		gravity = false;

		info.add(ChatColor.DARK_AQUA + "Damage: " + damage + " HP");
		info.add(ChatColor.DARK_AQUA + "Headshot: " + headshot * 100 + "%");
		info.add(ChatColor.DARK_AQUA + "Reload: " + reload / 20.0 + " Seconds");

		pages.setPage(info);
	}
}
