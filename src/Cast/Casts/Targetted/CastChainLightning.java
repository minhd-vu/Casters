package Cast.Casts.Targetted;

import Cast.Casts.Types.TargettedCast;
import Cast.CommandInterface;
import Cast.Essentials.Caster;
import Cast.Main;
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

public class CastChainLightning extends TargettedCast implements CommandInterface, Listener
{
	private int period;
	private double damage;
	private int playerrange;
	private int targetrange;
	private int explosion;
	private boolean explode;
	private boolean incendiary;

	public CastChainLightning(String name, String description)
	{
		super(name, description);

		warmup.setDuration(50);
		warmup.setAmplifier(5);
		cooldown.setCooldown(100);
		manacost = 3;

		info.add(ChatColor.DARK_AQUA + "WarmUp: " + ChatColor.GRAY + warmup.getDuration() / 20.0 + " Seconds");
		info.add(ChatColor.DARK_AQUA + "Cooldown: " + ChatColor.GRAY + cooldown.getCooldown() / 20.0 + " Seconds");
		info.add(ChatColor.DARK_AQUA + "Cost: " + ChatColor.GRAY + manacost + " MP");

		period = 10;
		damage = 5;
		playerrange = 10;
		targetrange = 8;
		explosion = 0;
		explode = false;
		incendiary = false;

		info.add(ChatColor.DARK_AQUA + "Damage: " + ChatColor.GRAY + damage + " HP");
		info.add(ChatColor.DARK_AQUA + "PlayerRange: " + ChatColor.GRAY + playerrange + " Blocks");
		info.add(ChatColor.DARK_AQUA + "TargetRange: " + ChatColor.GRAY + targetrange + " Blocks");

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
				LivingEntity target = getTarget(player, playerrange, false, false);

				if (target != null && !target.equals(player))
				{
					warmup.start(caster, target, name);

					new BukkitRunnable()
					{
						LivingEntity le = target;
						ArrayList<LivingEntity> oldtargets = new ArrayList<LivingEntity>();

						@Override
						public void run()
						{
							caster.setCasting(name, true);
							caster.setMana(manacost);

							new BukkitRunnable()
							{
								@Override
								public void run()
								{
									le = StrikeLightning(player, le, oldtargets);
									caster.setBossBarEntity(le);

									if (le == null)
									{
										caster.setCasting(name, false);
										this.cancel();
									}
								}

							}.runTaskTimer(Main.getInstance(), 0, period);

							cast(player, target);

							cooldown.start(player.getName());
						}

					}.runTaskLater(Main.getInstance(), warmup.getDuration());
				}
			}
		}

		return true;
	}

	public LivingEntity StrikeLightning(Player player, LivingEntity target, ArrayList<LivingEntity> oldtargets)
	{
		target.getWorld().spigot().strikeLightningEffect(target.getLocation(), true);
		target.getWorld().playSound(target.getLocation(), Sound.ENTITY_LIGHTNING_THUNDER, 1.0F, 1.0F);
		target.damage(damage);

		oldtargets.add(target);

		if (explode)
		{
			target.getWorld().createExplosion(target.getLocation(), explosion, incendiary);
		}

		List<Entity> targets = target.getNearbyEntities(targetrange, targetrange, targetrange);

		for (Entity e : targets)
		{
			if (e instanceof LivingEntity && !e.equals(target) && !e.equals(player) && !oldtargets.contains(e))
			{
				return (LivingEntity) e;
			}
		}

		return null;
	}
}
