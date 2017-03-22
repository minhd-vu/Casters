package Casters.Essentials;

import net.minecraft.server.v1_11_R1.AttributeInstance;
import net.minecraft.server.v1_11_R1.EntityInsentient;
import net.minecraft.server.v1_11_R1.GenericAttributes;
import net.minecraft.server.v1_11_R1.PathEntity;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import java.util.Set;

public class Mob
{
	private EntityType entitytype;
	private int maxhealth;
	private int damage;

	public Mob(EntityType entitytype, int maxhealth, int damage)
	{
		this.entitytype = entitytype;
		this.maxhealth = maxhealth;
		this.damage = damage;
	}

	public static boolean setEntityTargetLocation(Location location, Entity entity, double speed)
	{
		if (!entity.isValid())
		{
			return true;
		}

		net.minecraft.server.v1_11_R1.Entity entityt = ((CraftEntity) entity).getHandle();
		((EntityInsentient) entityt).getNavigation().a(2);
		Object entityf = ((CraftEntity) entity).getHandle();
		PathEntity path = ((EntityInsentient) entityf).getNavigation().a(location.getX(), location.getY(), location.getZ());

		if (path != null)
		{
			((EntityInsentient) entityf).getNavigation().a(path, 1.0D);
			((EntityInsentient) entityf).getNavigation().a(2.0D);
		}

		AttributeInstance attributes = ((EntityInsentient) ((CraftEntity) entity).getHandle()).getAttributeInstance(GenericAttributes.MOVEMENT_SPEED);
		attributes.setValue(speed);

		return false;
	}

	public EntityType getEntityType()
	{
		return entitytype;
	}

	public int getMaxHealth()
	{
		return maxhealth;
	}

	public int getDamage()
	{
		return damage;
	}
}
