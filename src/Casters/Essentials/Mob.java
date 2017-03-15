package Casters.Essentials;

import org.bukkit.entity.EntityType;

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
