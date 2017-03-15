package Casters.Casts.Targetted;

import Casters.Casters;
import Casters.Casts.Actives.Projectiles.Projectile;
import Casters.CommandInterface;
import Casters.Essentials.Caster;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_11_R1.EntityLiving;
import net.minecraft.server.v1_11_R1.EntityTNTPrimed;
import net.minecraft.server.v1_11_R1.Explosion;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftTNTPrimed;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.Field;
import java.util.Set;

public class CastBomb extends Projectile implements CommandInterface, Listener
{
	private double damage;
	private int range;
	private int fuse;
	private double velocity;
	private boolean gravity;
	private boolean incendiary;

	public CastBomb(String name, String description)
	{
		super(name, description);

		warmup.setDuration(20);
		warmup.setAmplifier(5);
		cooldown.setCooldown(100);
		manacost = 5;

		info.add(ChatColor.DARK_AQUA + "WarmUp: " + ChatColor.GRAY + warmup.getDuration() / 20.0 + " Seconds");
		info.add(ChatColor.DARK_AQUA + "Cooldown: " + ChatColor.GRAY + cooldown.getCooldown() / 20.0 + " Seconds");
		info.add(ChatColor.DARK_AQUA + "Cost: " + ChatColor.GRAY + manacost + " MP");

		damage = 10.0;
		gravity = true;
		range = 8;
		fuse = 40;
		velocity = 1.0;
		incendiary = true;

		info.add(ChatColor.DARK_AQUA + "Damage: " + ChatColor.GRAY + damage + " HP");
		info.add(ChatColor.DARK_AQUA + "Range: " + ChatColor.GRAY + range + " Blocks");
		info.add(ChatColor.DARK_AQUA + "Fuse: " + ChatColor.GRAY + fuse / 20.0 + " Seconds");

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
					@Override
					public void run()
					{
						if (!caster.isInterrupted())
						{
							caster.setCasting(name, true);
							caster.setMana(manacost);

							TNTPrimed bomb = null;

							Block target = caster.getPlayer().getTargetBlock((Set<Material>) null, range);

							if (target == null || target.getType().equals(Material.AIR))
							{
								bomb = (TNTPrimed) player.getWorld().spawnEntity(player.getEyeLocation(), EntityType.PRIMED_TNT);
								bomb.setVelocity(player.getEyeLocation().getDirection().normalize().multiply(velocity));
							}

							else
							{
								bomb = (TNTPrimed) player.getWorld().spawnEntity(target.getLocation().add(0, 1, 0), EntityType.PRIMED_TNT);
							}

							bomb.setGravity(gravity);
							bomb.setFuseTicks(fuse);
							bomb.setIsIncendiary(incendiary);
							
							EntityLiving nmsEntityLiving = (EntityLiving) (((CraftLivingEntity) player).getHandle());
							EntityTNTPrimed nmsTNT = (EntityTNTPrimed) (((CraftTNTPrimed) bomb).getHandle());

							try
							{
								Field sourcefield = EntityTNTPrimed.class.getDeclaredField("source");
								sourcefield.setAccessible(true);
								sourcefield.set(nmsTNT, nmsEntityLiving);
							}

							catch (Exception e)
							{
								e.printStackTrace();
							}

							projectiles.add(bomb.getUniqueId());

							cast(player);

							caster.setCasting(name, false);
						}

						cooldown.start(player.getName()); // TODO: Check Interrupt And Everything.
					}

				}.runTaskLater(Casters.getInstance(), warmup.getDuration());
			}
		}

		return true;
	}

	@EventHandler
	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event)
	{
		if (event.getDamager() instanceof TNTPrimed)
		{
			TNTPrimed bomb = (TNTPrimed) event.getDamager();

			if (projectiles.contains(bomb.getUniqueId()) && bomb.getSource() instanceof Player)
			{
				Caster caster = Casters.getCasters().get(bomb.getSource().getUniqueId());

				event.setCancelled(true);

				if (event.getEntity() instanceof LivingEntity && !event.getEntity().equals(caster.getPlayer()))
				{
					LivingEntity target = (LivingEntity) event.getEntity();

					if (!caster.sameParty(target))
					{
						target.damage(damage); // TODO: Check If Hit Party.
						caster.setBossBarEntity(target);
					}
				}
			}
		}
	}
}
