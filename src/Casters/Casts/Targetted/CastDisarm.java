package Casters.Casts.Targetted;

import Casters.Casters;
import Casters.CommandInterface;
import Casters.Essentials.Caster;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class CastDisarm extends Targetted implements CommandInterface
{
	private double damage;
	private int range;
	private int duration;

	public CastDisarm(String name, String description)
	{
		super(name, description);

		warmup.setDuration(0);
		warmup.setAmplifier(0);
		cooldown.setCooldown(40);
		manacost = 3;

		info.add(ChatColor.DARK_AQUA + "WarmUp: " + ChatColor.GRAY + warmup.getDuration() / 20.0 + " Seconds");
		info.add(ChatColor.DARK_AQUA + "Cooldown: " + ChatColor.GRAY + cooldown.getCooldown() / 20.0 + " Seconds");
		info.add(ChatColor.DARK_AQUA + "Cost: " + ChatColor.GRAY + manacost + " MP");

		damage = 3;
		range = 4;
		duration = 40;

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
				LivingEntity target = getTarget(player, range, false, false);

				if (target != null)
				{
					warmup.start(caster, target, name);

					new BukkitRunnable()
					{
						@SuppressWarnings("deprecation")
						@Override
						public void run()
						{
							if (!caster.isInterrupted())
							{
								caster.setCasting(name, true);
								caster.setMana(manacost);

								target.damage(damage);
								cast(player, target);

								if (target instanceof Player)
								{
									Player tplayer = (Player) target;
									Caster tcaster = Casters.getCasters().get(tplayer.getUniqueId());

									List<ItemStack> weapons = new ArrayList<ItemStack>();

									while (tplayer.getInventory().iterator().hasNext())
									{
										ItemStack weapon = tplayer.getInventory().iterator().next();

										if (tcaster.getWeapons().containsKey(weapon.getType()))
										{
											weapons.add(weapon);
											tplayer.getInventory().remove(weapon);
										}
									}

									if (weapons.size() > 0)
									{
										tplayer.sendMessage(header + " You Have Been Disarmed!");
									}

									new BukkitRunnable()
									{
										@Override
										public void run()
										{
											for (ItemStack weapon : weapons)
											{
												tplayer.getInventory().addItem(weapon);
											}

											tplayer.sendMessage(header + " You Regain Your Weaons!");
										}

									}.runTaskLater(Casters.getInstance(), duration);
								}

								caster.setBossBarEntity(target);

								target.getWorld().spigot().playEffect(target.getLocation().add(0, 1, 0), Effect.CRIT, 0, 0, 0.5F, 1.0F, 0.5F, 0.1F, 50, 16);
								target.getWorld().playSound(target.getLocation(), Sound.ITEM_ARMOR_EQUIP_IRON, 1.0F, 1.0F);


								caster.setCasting(name, false);
							}

							cooldown.start(player.getName());
						}

					}.runTaskLater(Casters.getInstance(), warmup.getDuration());
				}
			}
		}

		return true;
	}
}
