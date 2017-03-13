package Cast.Casts.Passives.Firearms;

import org.bukkit.ChatColor;
import org.bukkit.Material;

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
		velocity = 4.0;
		;
		maxaccuracy = 0;
		minaccuracy = 0;
		timer = 100;
		gravity = false;
		recoil = 2.0;

		info.add(ChatColor.DARK_AQUA + "Damage: " + ChatColor.AQUA + damage + " HP");
		info.add(ChatColor.DARK_AQUA + "Headshot: " + ChatColor.AQUA + decimalformat.format(headshot * 100) + "%");
		info.add(ChatColor.DARK_AQUA + "Reload: " + ChatColor.AQUA + cooldown.getCooldown() / 20.0 + " Seconds");
		info.add(ChatColor.DARK_AQUA + "Recoil: " + ChatColor.AQUA + cooldown.getCooldown() / 20.0 + " Seconds");

		pages.setPage(info);
	}
}
