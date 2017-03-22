package Casters.Casts.Actives;

import Casters.Casters;
import Casters.CommandInterface;
import Casters.Essentials.Caster;
import Casters.Essentials.Mob;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class CastStampede extends Active implements CommandInterface, Listener
{
	private double damage;
	private int range;
	private int duration;
	private int size;
	private double speed;
	private int horsehitrange;

	public CastStampede(String name, String description)
	{
		super(name, description);

		warmup.setDuration(40);
		warmup.setAmplifier(5);
		cooldown.setCooldown(100);
		manacost = 3;

		info.add(ChatColor.DARK_AQUA + "WarmUp: " + ChatColor.GRAY + warmup.getDuration() / 20.0 + " Seconds");
		info.add(ChatColor.DARK_AQUA + "Cooldown: " + ChatColor.GRAY + cooldown.getCooldown() / 20.0 + " Seconds");
		info.add(ChatColor.DARK_AQUA + "Cost: " + ChatColor.GRAY + manacost + " MP");

		damage = 5;
		range = 16;
		duration = 45;
		size = 3;
		speed = 0.32;
		horsehitrange = 1;

		info.add(ChatColor.DARK_AQUA + "Damage: " + ChatColor.GRAY + damage + " HP");
		info.add(ChatColor.DARK_AQUA + "Range: " + ChatColor.GRAY + range + " Blocks");
		info.add(ChatColor.DARK_AQUA + "Duration: " + ChatColor.GRAY + duration / 20.0 + " Seconds");

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
							if (!player.isInsideVehicle() && !(player.getVehicle() instanceof Horse))
							{
								player.sendMessage(header + ChatColor.GRAY + " You Must Be On A " + ChatColor.WHITE + "Horse" + ChatColor.GRAY + " To Cast This!");
								return;
							}

							caster.setCasting(name, true);
							caster.setMana(manacost);

							List<UUID> horses = new ArrayList<UUID>();

							Location target = player.getTargetBlock((Set<Material>) null, range).getLocation();

							Vector direction = player.getLocation().getDirection().setY(0.0).normalize();
							Vector leftdirection = new Vector(direction.getZ(), 0.0, -direction.getX()).normalize();
							Vector rightdirection = new Vector(-direction.getZ(), 0.0, direction.getX()).normalize();

							for (int i = 0; i < size; ++i)
							{
								Location leftlocation = player.getLocation().add(leftdirection.multiply(i + 1));
								leftlocation = player.getWorld().getHighestBlockAt(leftlocation).getLocation().setDirection(direction);

								Location rightlocation = player.getLocation().add(rightdirection.multiply(i + 1));
								rightlocation = player.getWorld().getHighestBlockAt(rightlocation).getLocation().setDirection(direction);

								Horse left = (Horse) player.getWorld().spawnEntity(leftlocation, EntityType.HORSE);
								Horse right = (Horse) player.getWorld().spawnEntity(rightlocation, EntityType.HORSE);

								left.setOwner(player);
								right.setOwner(player);

								left.setAdult();
								right.setAdult();

								final Location lefttarget =
										player.getWorld().getHighestBlockAt(leftlocation.add(leftlocation.getDirection().normalize().multiply(range))).getLocation();

								final Location righttarget =
										player.getWorld().getHighestBlockAt(rightlocation.add(rightlocation.getDirection().normalize().multiply(range))).getLocation();

								new BukkitRunnable()
								{
									@Override
									public void run()
									{
										if (Mob.setEntityTargetLocation(lefttarget, left, speed))
										{
											this.cancel();
										}
									}

								}.runTaskTimer(Casters.getInstance(), 0, 2);

								new BukkitRunnable()
								{
									@Override
									public void run()
									{
										if (Mob.setEntityTargetLocation(righttarget, right, speed))
										{
											this.cancel();
										}
									}

								}.runTaskTimer(Casters.getInstance(), 0, 2);

								horses.add(left.getUniqueId());
								horses.add(right.getUniqueId());
							}

							player.getWorld().playSound(player.getLocation(), Sound.ENTITY_HORSE_ANGRY, 8.0F, 1.0F);
							player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, 8.0F, 1.0F);

							cast(player);

							caster.setCasting(name, false);

							new BukkitRunnable()
							{
								private int count = 0;
								private List<UUID> hitentities = new ArrayList<UUID>();

								@Override
								public void run()
								{
									for (UUID uuid : horses)
									{
										Horse horse = (Horse) Bukkit.getEntity(uuid);

										if (horse.isValid())
										{
											List<Entity> entities = horse.getNearbyEntities(horsehitrange, horsehitrange, horsehitrange);

											entities.remove(caster.getPlayer());
											entities.remove(caster.getPlayer().getVehicle());

											for (UUID id : horses)
											{
												Entity entity = Bukkit.getEntity(id);

												if (entities.contains(entity))
												{
													entities.remove(entity);
												}
											}

											for (Entity entity : entities)
											{
												if (entity instanceof LivingEntity && !hitentities.contains(entity.getUniqueId()) && !caster.sameParty(entity))
												{
													((LivingEntity) entity).damage(damage); // TODO: Test The Particle & Damage.
													caster.setBossBarEntity((Damageable) entity);
													entity.getWorld().spawnParticle(Particle.SWEEP_ATTACK, ((LivingEntity) entity).getEyeLocation(), 5, 1.0, 1.0, 1.0);

													hitentities.add(entity.getUniqueId());
												}
											}
										}
									}

									if (++count > duration)
									{
										hitentities.clear();
										this.cancel();
										return;
									}
								}

							}.runTaskTimer(Casters.getInstance(), 0, 2);

							new BukkitRunnable()
							{
								@Override
								public void run()
								{
									for (UUID horse : horses)
									{
										if (Bukkit.getEntity(horse).isValid())
										{
											Bukkit.getEntity(horse).getWorld()
													.spawnParticle(Particle.BLOCK_CRACK, Bukkit.getEntity(horse).getLocation().add(0, 1, 0), 50, 1.0, 1.0, 1.0);
											Bukkit.getEntity(horse).remove();
										}
									}

									horses.clear();
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
