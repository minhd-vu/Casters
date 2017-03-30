package Casters.Casts.Actives.Projectiles;

import Casters.Casters;
import Casters.CommandInterface;
import Casters.Essentials.Caster;
import Casters.Essentials.Effects.Stun;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class CastHook extends Projectile implements CommandInterface
{
	private Stun stun;

	private int range;
	private double velocity;
	private boolean gravity;

	public CastHook(String name, String description)
	{
		super(name, description);

		warmup.setDuration(0);
		warmup.setAmplifier(0);
		cooldown.setCooldown(100);
		manacost = 5;

		info.add(ChatColor.DARK_AQUA + "WarmUp: " + ChatColor.GRAY + warmup.getDuration() / 20.0 + " Seconds");
		info.add(ChatColor.DARK_AQUA + "Cooldown: " + ChatColor.GRAY + cooldown.getCooldown() / 20.0 + " Seconds");
		info.add(ChatColor.DARK_AQUA + "Cost: " + ChatColor.GRAY + manacost + " MP");

		stun = new Stun(20);

		range = 10;
		velocity = 1.5;
		gravity = false;

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
				warmup.start(caster, name);

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

							Arrow hook = (Arrow) player.getWorld().spawnEntity(player.getEyeLocation(), EntityType.ARROW);
							hook.setShooter(player);
							hook.setVelocity(caster.getPlayer().getEyeLocation().getDirection().normalize().multiply(velocity));
							hook.setGravity(gravity);

							projectiles.add(hook.getUniqueId());

							player.getWorld().playSound(player.getEyeLocation(), Sound.ENTITY_SNOWBALL_THROW, 8.0F, -1.0F); // TODO: Test Out Sound & Everything.
							//player.getWorld().spigot().playEffect(player.getLocation(), Effect.WITHER_SHOOT);

							cast(player);

							new BukkitRunnable()
							{
								@SuppressWarnings("deprecation")
								@Override
								public void run()
								{
									if (!hook.isDead())
									{
										player.getWorld().spigot().playEffect(hook.getLocation(), Effect.FIREWORKS_SPARK, 0, 0, 0.0F, 0.0F, 0.0F, 0.1F, 1, 1);
										//player.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, hook.getLocation(), 1, 0.0, 0.0, 0.0);
									}

									else
									{
										this.cancel();
										return;
									}
								}

							}.runTaskTimer(Casters.getInstance(), 2, 1);

							caster.setCasting(name, false);
						}

						cooldown.start(player.getName());
					}

				}.runTaskLater(Casters.getInstance(), warmup.getDuration());
			}
		}

		return true;
	}

	@EventHandler
	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event)
	{
		if (event.getDamager() instanceof Arrow)
		{
			Arrow hook = (Arrow) event.getDamager();

			if (projectiles.contains(hook.getUniqueId()) && hook.getShooter() instanceof Player && event.getEntity() instanceof LivingEntity)
			{
				Player player = (Player) hook.getShooter();
				Caster caster = Casters.getCasters().get(player.getUniqueId());
				LivingEntity target = (LivingEntity) event.getEntity();

				event.setCancelled(true);

				if (!caster.sameParty(target))
				{
					target.getWorld().playSound(target.getLocation(), Sound.BLOCK_ANVIL_FALL, 8.0F, -1.0F);

//					stun.start(target);

					//Vector vector = player.getLocation().toVector().subtract(target.getLocation().toVector());

					target.setVelocity(target.getVelocity().multiply(20));
				}
			}
		}
	}
}