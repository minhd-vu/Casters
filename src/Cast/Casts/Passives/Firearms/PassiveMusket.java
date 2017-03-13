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

		firearm = Material.DIAMOND_BARDING;

		warmup.setDuration(0);
		warmup.setAmplifier(0);
		cooldown.setCooldown(85);

		damage = 75;
		headshot = 1.5;
		shots = 1;
		velocity = 4.5;;
		maxaccuracy = 0;
		minaccuracy = 0;
		timer = 100;
		reload = 100;
		gravity = false;
		recoil = 5.0;

		info.add(ChatColor.DARK_AQUA + "Damage: " + ChatColor.AQUA + damage + " HP");
		info.add(ChatColor.DARK_AQUA + "Headshot: " + ChatColor.AQUA + headshot * 100 + "%");
		info.add(ChatColor.DARK_AQUA + "Reload: " + ChatColor.AQUA + reload / 20.0 + " Seconds");

		pages.setPage(info);
	}
}
