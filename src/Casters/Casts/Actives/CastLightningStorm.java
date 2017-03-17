package Casters.Casts.Actives;

import Casters.Casters;
import Casters.CommandInterface;
import Casters.Essentials.Caster;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class CastLightningStorm extends Active implements CommandInterface, Listener
{
	private double damage;
	private int range;
	private int explosion;
	private boolean explode;
	private boolean incendiary;

	public CastLightningStorm(String name, String description)
	{
		super(name, description);

		warmup.setDuration(20);
		warmup.setAmplifier(5);
		cooldown.setCooldown(40);
		manacost = 3;

		info.add(ChatColor.DARK_AQUA + "WarmUp: " + ChatColor.GRAY + warmup.getDuration() / 20.0 + " Seconds");
		info.add(ChatColor.DARK_AQUA + "Cooldown: " + ChatColor.GRAY + cooldown.getCooldown() / 20.0 + " Seconds");
		info.add(ChatColor.DARK_AQUA + "Cost: " + ChatColor.GRAY + manacost + " MP");

		damage = 10;
		range = 12;
		explosion = 0;
		explode = false;
		incendiary = false;

		info.add(ChatColor.DARK_AQUA + "Damage: " + ChatColor.GRAY + damage + " HP");
		info.add(ChatColor.DARK_AQUA + "Range: " + ChatColor.GRAY + range + " Blocks");

		pages.setPage(info);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if (sender instanceof Player)
		{
			Player player = (Player) sender;
			Caster caster = Casters.getCasters().get(player.getUniqueId());

			if (args.length == 2 && args[1].equalsIgnoreCase("info"))
			{
				pages.display(player, args, 2);

				return true;
			}

			else if (args.length == 1 && caster.canCast(name, cooldown, manacost))
			{
				List<Entity> targets = player.getNearbyEntities(range, range, range);
				List<LivingEntity> livingtargets = new ArrayList<LivingEntity>();

				for (Entity target : targets)
				{
					if (target instanceof LivingEntity && !caster.sameParty(target))
					{
						livingtargets.add((LivingEntity) target);
					}
				}

				if (livingtargets.size() > 0)
				{
					warmup.start(caster, name);

					new BukkitRunnable()
					{
						@Override
						public void run()
						{
							if (!caster.isInterrupted())
							{
								caster.setCasting(name, true);
								caster.setMana(manacost);

								for (LivingEntity target : livingtargets)
								{
									target.getWorld().spigot().strikeLightningEffect(target.getLocation(), true);
									target.getWorld().playSound(target.getLocation(), Sound.ENTITY_LIGHTNING_THUNDER, 8.0F, 1.0F);

									((LivingEntity) target).damage(damage);
									caster.setBossBarEntity((LivingEntity) target);
								}

								cast(player);

								caster.setCasting(name, false);
							}

							cooldown.start(player.getName());
						}

					}.runTaskLater(Casters.getInstance(), warmup.getDuration());
				}

				else
				{
					player.sendMessage(header + ChatColor.WHITE + name + ChatColor.GRAY + ": No Targets In Range!");
				}
			}
		}

		return true;
	}
}
