package Cast.Casts.Types;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_10_R1.AxisAlignedBB;

public class TargettedCast extends Cast
{
	public TargettedCast(String name)
	{
		super(name);
	}

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

			AxisAlignedBB boundingbox = new AxisAlignedBB(low.getX(), low.getY(), low.getZ(), high.getX(), high.getY(), high.getZ());

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
	
	/*public LivingEntity getTarget(Player player, int range)
	{
		List<Entity> entities = player.getNearbyEntities(range, range, range);
		ArrayList<LivingEntity> livingentities = new ArrayList<LivingEntity>();

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
				if ((bx - .75 <= ex && ex <= bx + 1.75) && (bz - .75 <= ez && ez <= bz + 1.75)
						&& (by - 1 <= ey && ey <= by + 2.5))
				{
					return entity;
				}
			}
		}
		
		return null;
	}*/

	/*public LivingEntity getTarget(Player player, int range)
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
	}*/
}
