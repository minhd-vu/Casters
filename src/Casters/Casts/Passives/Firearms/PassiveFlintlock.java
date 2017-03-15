package Casters.Casts.Passives.Firearms;

import org.bukkit.ChatColor;
import org.bukkit.Material;

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
		recoil = 0.5;
		volume = 2.0F;
		pitch = 2.0F;
		amplitude = 1;

		info.add(ChatColor.DARK_AQUA + "Damage: " + ChatColor.AQUA + damage + " HP");
		info.add(ChatColor.DARK_AQUA + "Headshot: " + ChatColor.AQUA + decimalformat.format(headshot * 100) + "%");
		info.add(ChatColor.DARK_AQUA + "Reload: " + ChatColor.AQUA + cooldown.getCooldown() / 20.0 + " Seconds");
		info.add(ChatColor.DARK_AQUA + "Recoil: " + ChatColor.AQUA + "Low");
		info.add(ChatColor.DARK_AQUA + "Accuracy: " + ChatColor.AQUA + (1 - (maxaccuracy - minaccuracy / 2.0)) * 100 + "%");

		pages.setPage(info);
	}
}
