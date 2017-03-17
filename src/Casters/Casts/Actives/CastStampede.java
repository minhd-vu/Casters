package Casters.Casts.Actives;

import Casters.Casters;
import Casters.CommandInterface;
import Casters.Essentials.Caster;
import net.minecraft.server.v1_11_R1.AttributeInstance;
import net.minecraft.server.v1_11_R1.EntityInsentient;
import net.minecraft.server.v1_11_R1.GenericAttributes;
import net.minecraft.server.v1_11_R1.PathEntity;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CastStampede extends Active implements CommandInterface, Listener
{
	private double damage;
	private int range;
	private int duration;
	private int size;
	private double speed;

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
		range = 4;
		duration = 40;
		size = 3;
		speed = 0.8;

		info.add(ChatColor.DARK_AQUA + "Damage: " + ChatColor.GRAY + damage + " HP");
		info.add(ChatColor.DARK_AQUA + "Range: " + ChatColor.GRAY + range + " Blocks");
		info.add(ChatColor.DARK_AQUA + "Duration: " + ChatColor.GRAY + duration / 20.0 + " Seconds");

		pages.setPage(info);
	}

	private void moveHorse(Location location, Horse horse, double speed)
	{
		net.minecraft.server.v1_11_R1.Entity horset = ((CraftEntity) horse).getHandle();

		((EntityInsentient) horset).getNavigation().a(2);
		Object horsef = ((CraftEntity) horse).getHandle();
		PathEntity path = ((EntityInsentient) horsef).getNavigation().a(location.getX() + 1, location.getY(), location.getZ() + 1);

		if (path != null)
		{
			((EntityInsentient) horsef).getNavigation().a(path, 1.0D);
			((EntityInsentient) horsef).getNavigation().a(2.0D);
		}

		AttributeInstance attributes = ((EntityInsentient) ((CraftEntity) horse).getHandle()).getAttributeInstance(GenericAttributes.MOVEMENT_SPEED);
		attributes.setValue(speed);

//		new BukkitRunnable()
//		{
//			public void run()
//			{
//			}
//
//		}.runTaskTimer(Casters.getInstance(), 0L, 20L);
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

							Vector direction = player.getLocation().getDirection().normalize();
							Vector leftdirection = new Vector(direction.getZ(), 0.0, -direction.getX()).normalize();
							Vector rightdirection = new Vector(-direction.getZ(), 0.0, direction.getX()).normalize();

							for (int i = 0; i < size; ++i)
							{
								Horse left = (Horse) player.getWorld().spawnEntity(player.getLocation().add(leftdirection.multiply(i + 1)), EntityType.HORSE);
								Horse right = (Horse) player.getWorld().spawnEntity(player.getLocation().add(rightdirection.multiply(i + 1)), EntityType.HORSE);

								left.setOwner(player);
								right.setOwner(player);

								left.setAdult();
								right.setAdult();

								left.setVelocity(direction.multiply(speed));
								right.setVelocity(direction.multiply(speed));

								horses.add(left.getUniqueId());
								horses.add(right.getUniqueId());
							}

							player.getWorld().playSound(player.getLocation(), Sound.ENTITY_HORSE_ANGRY, 8.0F, 1.0F);
							player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, 8.0F, 1.0F);

							cast(player);

							caster.setCasting(name, false);

							new BukkitRunnable()
							{
								@Override
								public void run()
								{

								}
							}.runTaskTimer(Casters.getInstance(), 0, 2);

							new BukkitRunnable()
							{
								@Override
								public void run()
								{
									for (UUID horse : horses)
									{
										if (!Bukkit.getEntity(horse).isDead())
										{
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
