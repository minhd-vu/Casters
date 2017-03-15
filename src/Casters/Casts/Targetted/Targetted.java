package Casters.Casts.Targetted;

import Casters.Casts.Cast;
import Casters.Essentials.Caster;
import Casters.Casters;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockIterator;

import java.util.ArrayList;
import java.util.List;

public abstract class Targetted extends Cast
{
	public Targetted(String name, String description)
	{
		super(name, description);
	}

	/*-
	public LivingEntity getTarget(Player player, int range)
	{
		List<Entity> entity = player.getNearbyEntities(range, range, range);
	
		Set<Material> transparent = new HashSet<Material>();
		transparent.add(Material.AIR);
		transparent.add(Material.WATER);
		transparent.add(Material.STATIONARY_WATER);
		transparent.add(Material.GRASS);
		transparent.add(Material.LONG_GRASS);
		transparent.add(Material.VINE);
		transparent.add(Material.GLASS);
		transparent.add(Material.THIN_GLASS);
		transparent.add(Material.STAINED_GLASS);
		transparent.add(Material.STAINED_GLASS_PANE);
	
		List<Block> lineofsight = player.getLineOfSight(transparent, range);
	
		for (Block block : lineofsight)
		{
			if (block.getType() != Material.AIR && block.getType() != Material.WATER
					&& block.getType() != Material.STATIONARY_WATER && block.getType() != Material.GRASS
					&& block.getType() != Material.LONG_GRASS && block.getType() != Material.VINE
					&& block.getType() != Material.GLASS && block.getType() != Material.THIN_GLASS
					&& block.getType() != Material.STAINED_GLASS && block.getType() != Material.STAINED_GLASS_PANE)
			{
				break;
			}
	
			Location low = block.getLocation();
			Location high = low.clone().add(1, 1, 1);
	
			AxisAlignedBB boundingbox =
					new AxisAlignedBB(low.getX(), low.getY(), low.getZ(), high.getX(), high.getY(), high.getZ());
	
			for (Entity target : entity)
			{
				if (target instanceof LivingEntity)
				{
					if (target.getLocation().distance(player.getEyeLocation()) <= range
							&& ((CraftEntity) target).getHandle().getBoundingBox().b(boundingbox))
					{
						return (LivingEntity) target;
					}
				}
			}
		}
	
		return player;
	}
	*/

	public LivingEntity getTarget(Player player, int range, boolean targetself, boolean targetpartymembers)
	{
		List<Entity> entities = player.getNearbyEntities(range, range, range);
		ArrayList<LivingEntity> livingentities = new ArrayList<LivingEntity>();

		entities.remove(player.getVehicle());

		for (Entity e : entities)
		{
			if (e instanceof LivingEntity)
			{
				livingentities.add((LivingEntity) e);
			}
		}

		BlockIterator blockiterator = new BlockIterator(player, range);

		Block block;
		Location loc;

		int bx, by, bz;
		double ex, ey, ez;

		outerloop:
		while (blockiterator.hasNext())
		{
			block = blockiterator.next();
			bx = block.getX();
			by = block.getY();
			bz = block.getZ();

			for (LivingEntity entity : livingentities)
			{
				loc = entity.getLocation();

				ex = loc.getX();
				ey = loc.getY();
				ez = loc.getZ();

				if ((bx - 0.75 <= ex && ex <= bx + 1.75) && (bz - 0.75 <= ez && ez <= bz + 1.75) && (by - 1.0 <= ey && ey <= by + 2.5))
				{
					if (entity instanceof Player)
					{
						Caster caster = Casters.getCasters().get(player.getUniqueId());

						if (caster.hasParty() && caster.getParty().getMembers().contains(Casters.getCasters().get(entity.getUniqueId())))
						{
							if (targetpartymembers)
							{
								return entity;
							}

							break outerloop;
						}

						if (targetpartymembers)
						{
							return null;
						}
					}

					else if (targetpartymembers)
					{
						break outerloop;
					}

					return entity;
				}
			}
		}

		if (targetself)
		{
			return player;
		}

		return null;
	}

	/*-
	public LivingEntity getTarget(Player player, int range)
	{
		ArrayList<Entity> entities = (ArrayList<Entity>) player.getNearbyEntities(range, range, range);
		ArrayList<Block> sightblock = (ArrayList<Block>) player.getLineOfSight((Set<Material>) null, range);
		ArrayList<Location> sight = new ArrayList<Location>();
	
		for (int i = 0; i < sightblock.size(); i++)
		{
			sight.add(sightblock.get(i).getLocation());
		}
	
		for (int i = 0; i < sight.size(); i++)
		{
			for (int k = 0; k < entities.size(); k++)
			{
				if (Math.abs(entities.get(k).getLocation().getX() - sight.get(i).getX()) < 1.3)
				{
					if (Math.abs(entities.get(k).getLocation().getY() - sight.get(i).getY()) < 1.5)
					{
						if (Math.abs(entities.get(k).getLocation().getZ() - sight.get(i).getZ()) < 1.3)
						{
							if (entities.get(k) instanceof LivingEntity)
							{
								return (LivingEntity) entities.get(k);
							}
						}
					}
				}
			}
		}
	
		return null;
	}
	*/
}
