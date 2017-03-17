package Casters.Casts.Actives;

import Casters.Casters;
import Casters.CommandInterface;
import Casters.Essentials.Caster;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import static org.bukkit.Effect.IRON_DOOR_CLOSE;

public class CastDefensiveStance extends Active implements CommandInterface
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

	@SuppressWarnings("deprecated")
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
				warmup.start(caster, name);

				new BukkitRunnable()
				{
					@Override
					public void run()
					{
						if (!caster.isInterrupted())
						{
							caster.setCasting(name, true);
							caster.setEffect("Defending", duration);
							caster.setMana(manacost);

							if (caster.hasParty())
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

							new BukkitRunnable()
							{
								private int count = 0;
								private boolean interrupted = false;

								@Override
								public void run()
								{
									if (caster.hasParty())
									{
										for (Caster member : caster.getParty().getMembers())
										{
											if (member.isInterrupted())
											{
												isInterrupted(member);
											}
										}
									}

									else
									{
										isInterrupted(caster);
									}

									if (++count > duration || interrupted)
									{
										this.cancel();
										return;
									}
								}

								private void isInterrupted(Caster caster)
								{
									if (caster.isInterrupted())
									{
										caster.getPlayer().removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
										caster.setEffect("Defending", 0.0);

										interrupted = true;
									}
								}

							}.runTaskTimer(Casters.getInstance(), 1, 1);

							player.getWorld().spigot().playEffect(player.getLocation().add(0, 1, 0), IRON_DOOR_CLOSE, 0, 0, 0.5F, 0.5F, 0.5F, 0.1F, 50, 16);
							player.getWorld().playSound(player.getLocation(), Sound.BLOCK_ANVIL_PLACE, 0.8F, 1.0F);

							cast(player);

							new BukkitRunnable()
							{
								@Override
								public void run()
								{
									if (caster.isCasting(name))
									{
										decast(player);
										caster.setCasting(name, false);
									}
								}

							}.runTaskLater(Casters.getInstance(), duration);
						}

						cooldown.start(player.getName());
					}

				}.runTaskLater(Casters.getInstance(), warmup.getDuration());
			}
		}

		return true;
	}
}
