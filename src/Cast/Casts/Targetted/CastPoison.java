package Cast.Casts.Targetted;

import Cast.CommandInterface;
import Cast.Essentials.Caster;
import Cast.Essentials.Effects.Poison;
import Cast.Main;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.scheduler.BukkitRunnable;

public class CastPoison extends Targetted implements CommandInterface, Listener
{
	private Poison poison;

	private int duration;
	private int range;
	private int damagepertick;
	private int amplifier;

	public CastPoison(String name, String description)
	{
		super(name, description);

		warmup.setDuration(0);
		warmup.setAmplifier(0);
		cooldown.setCooldown(40);
		manacost = 3;

		info.add(ChatColor.DARK_AQUA + "WarmUp: " + ChatColor.GRAY + warmup.getDuration() / 20.0 + " Seconds");
		info.add(ChatColor.DARK_AQUA + "Cooldown: " + ChatColor.GRAY + cooldown.getCooldown() / 20.0 + " Seconds");
		info.add(ChatColor.DARK_AQUA + "Cost: " + ChatColor.GRAY + manacost + " MP");

		duration = 40;
		range = 10;
		damagepertick = 2;
		amplifier = 2;

		poison = new Poison(damagepertick, duration, amplifier);

		info.add(ChatColor.DARK_AQUA + "Duration: " + ChatColor.GRAY + duration / 20.0 + " Seconds");
		info.add(ChatColor.DARK_AQUA + "Range: " + ChatColor.GRAY + range + " Blocks");
		info.add(ChatColor.DARK_AQUA + "Damage Per Tick: " + ChatColor.GRAY + damagepertick + " HP");
		info.add(ChatColor.DARK_AQUA + "Amplifier: " + ChatColor.GRAY + amplifier);

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

				LivingEntity target = getTarget(player, range, false, false);

				if (target != null && !target.equals(player))
				{
					warmup.start(caster, name);

					new BukkitRunnable()
					{
						@SuppressWarnings("deprecation")
						@Override
						public void run()
						{
							caster.setCasting(name, true);
							caster.setEffect("Poisoning", duration);
							caster.setMana(manacost);

							poison.start(caster, target, name);

							target.getWorld().spigot().playEffect(target.getLocation().add(0, 1, 0), Effect.SLIME, 0, 0,
									0.5F, 1.0F, 0.5F, 0.1F, 50, 16);
							target.getWorld().playSound(target.getLocation(), Sound.BLOCK_GRAVEL_FALL, 1.0F, 1.0F);

							caster.setBossBarEntity(target);

							cast(player, target);

							cooldown.start(player.getName());

							caster.setCasting(name, false);
						}

					}.runTaskLater(Main.getInstance(), warmup.getDuration());
				}
			}
		}

		return true;
	}

	@EventHandler
	public void onEntityDamageEvent(EntityDamageEvent event)
	{
		if (event.getCause().equals(DamageCause.POISON))
		{
			if (poison.getAffectedPlayers().contains(event.getEntity().getUniqueId()))
			{
				event.setDamage(damagepertick);
			}
		}
	}
}
