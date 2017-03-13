package Cast.Casts.Passives.Firearms;

import org.bukkit.ChatColor;
import org.bukkit.Material;

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
		gravity = false;
		recoil = 1.0;
		volume = 4.0F;
		pitch = 1.5F;
		amplitude = 2;

		info.add(ChatColor.DARK_AQUA + "Damage: " + ChatColor.AQUA + damage + " HP");
		info.add(ChatColor.DARK_AQUA + "Headshot: " + ChatColor.AQUA + decimalformat.format(headshot * 100) + "%");
		info.add(ChatColor.DARK_AQUA + "Reload: " + ChatColor.AQUA + cooldown.getCooldown() / 20.0 + " Seconds");
		info.add(ChatColor.DARK_AQUA + "Recoil: " + ChatColor.AQUA + "Medium");
		info.add(ChatColor.DARK_AQUA + "Accuracy: " + ChatColor.AQUA + (1 - (maxaccuracy - minaccuracy / 2.0)) * 100 + "%");

		pages.setPage(info);
	}
}
