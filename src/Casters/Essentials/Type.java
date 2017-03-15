package Casters.Essentials;

import org.bukkit.Material;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Type
{
	private String name;
	private String description;

	private List<Material> armor;
	private HashMap<Material, Integer> weapon;
	private HashMap<String, Integer> casts;

	private int strength;
	private int constitution;
	private int dexterity;
	private int intellect;
	private int wisdom;

	private double meleedamagescale;
	private double bowdamagescale;
	private double movementspeedscale;
	private double maxhealthscale;
	private double healthregenscale;
	private double maxmanascale;
	private double manaregenscale;

	public Type(String name, String description)
	{
		this.name = name;
		this.description = description;

		armor = new ArrayList<Material>();
		weapon = new HashMap<Material, Integer>();
		casts = new HashMap<String, Integer>();
	}

	public String getName()
	{
		return name;
	}

	public String getDescription()
	{
		return description;
	}

	public List<Material> getArmor()
	{
		return armor;
	}

	public HashMap<Material, Integer> getWeapon()
	{
		return weapon;
	}

	public HashMap<String, Integer> getCasts()
	{
		return casts;
	}

	public int getStrength()
	{
		return strength;
	}

	public void setStrength(int strength)
	{
		this.strength = strength;
	}

	public int getConstitution()
	{
		return constitution;
	}

	public void setConstitution(int constitution)
	{
		this.constitution = constitution;
	}

	public int getDexterity()
	{
		return dexterity;
	}

	public void setDexterity(int dexterity)
	{
		this.dexterity = dexterity;
	}

	public int getIntellect()
	{
		return intellect;
	}

	public void setIntellect(int intellect)
	{
		this.intellect = intellect;
	}

	public int getWisdom()
	{
		return wisdom;
	}

	public void setWisdom(int wisdom)
	{
		this.wisdom = wisdom;
	}

	public double getMeleeDamageScale()
	{
		return meleedamagescale;
	}

	public void setMeleeDamageScale(double meleedamagescale)
	{
		this.meleedamagescale = meleedamagescale;
	}

	public double getBowDamageScale()
	{
		return bowdamagescale;
	}

	public void setBowDamageScale(double bowdamagescale)
	{
		this.bowdamagescale = bowdamagescale;
	}

	public double getMovementSpeedScale()
	{
		return movementspeedscale;
	}

	public void setMovementSpeedScale(double movementspeedscale)
	{
		this.movementspeedscale = movementspeedscale;
	}

	public double getMaxHealthScale()
	{
		return maxhealthscale;
	}

	public void setMaxhealthscale(double maxhealthscale)
	{
		this.maxhealthscale = maxhealthscale;
	}

	public double getHealthRegenScale()
	{
		return healthregenscale;
	}

	public void setHealthRegenScale(double healthregenscale)
	{
		this.healthregenscale = healthregenscale;
	}

	public double getMaxManaScale()
	{
		return maxmanascale;
	}

	public void setMaxManaScale(double maxmanascale)
	{
		this.maxmanascale = maxmanascale;
	}

	public double getManaRegenScale()
	{
		return manaregenscale;
	}

	public void setManaRegenScale(double manaregenscale)
	{
		this.manaregenscale = manaregenscale;
	}
}
