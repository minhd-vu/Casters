package Cast.Casts.Actives;

import Cast.Casts.Types.ActiveCast;
import Cast.CommandInterface;
import Cast.Essentials.Caster;
import Cast.Main;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class CastDefensiveStance extends ActiveCast implements CommandInterface
{
	private int duration;
	private int amplitude;

	public CastDefensiveStance(String name, String description)
	{
		super(name, description);

		warmup.setDuration(0);
		warmup.setAmplifier(0);
		cooldown.setCooldown(40);
		manacost = 3;

		info.add(ChatColor.DARK_AQUA + "WarmUp: " + ChatColor.GRAY + warmup.getDuration() / 20.0 + " Seconds");
		info.add(ChatColor.DARK_AQUA + "Cooldown: " + ChatColor.GRAY + cooldown.getCooldown() / 20.0 + " Seconds");
		info.add(ChatColor.DARK_AQUA + "Cost: " + ChatColor.GRAY + manacost + " MP");

		duration = 100;
		amplitude = 100;

		info.add(ChatColor.DARK_AQUA + "Duration: " + ChatColor.GRAY + duration / 20.0 + " Seconds");
		info.add(ChatColor.DARK_AQUA + "Amplitude: " + ChatColor.GRAY + amplitude + " %");

		pages.setPage(info);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if (sender instanceof Player)
		{
			Player player = (Player) sender;
			Caster caster = Main.getCasters().get(player.getUniqueId());

			if (args.length == 2 && args[1].equalsIgnoreCase("info"))
			{
				pages.display(player, args, 2);

				return true;
			}

			else if (args.length == 1 && caster.canCast(name, cooldown, manacost))
			{
				warmup.start(caster, name);

				new BukkitRunnable()
				{
					@Override
					public void run()
					{
						caster.setCasting(name, true);
						caster.setEffect("Defending", duration);
						caster.setMana(manacost);

						if (caster.getParty() != null)
						{
							for (Caster member : caster.getParty().getMembers())
							{
								member.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, duration, 0));
								member.setEffect("Defending", duration);
							}
						}

						else
						{
							player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, duration, 0));
						}

						player.getWorld().spigot().playEffect(player.getLocation().add(0, 1, 0), Effect.IRON_DOOR_CLOSE, 0, 0,
								0.5F, 0.5F, 0.5F, 0.1F, 50, 16);
						player.getWorld().playSound(player.getLocation(), Sound.BLOCK_ANVIL_PLACE, 0.8F, 1.0F);

						cast(player);

						new BukkitRunnable()
						{
							@Override
							public void run()
							{
								decast(player);
								caster.setCasting(name, false);
							}

						}.runTaskLater(Main.getInstance(), duration);

						cooldown.start(player.getName());
					}

				}.runTaskLater(Main.getInstance(), warmup.getDuration());
			}
		}

		return true;
	}
}
