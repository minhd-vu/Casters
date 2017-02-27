package Cast.Essentials;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Material;

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

	private int strengthscale;
	private int constitutionscale;
	private int dexterityscale;
	private int intellectscale;
	private int wisdomscale;

	public Type(String name, String description, int strength, int constitution, int dexterity, int intellect,
			int wisdom)
	{
		this.name = name;
		this.description = description;

		armor = new ArrayList<Material>();
		weapon = new HashMap<Material, Integer>();
		casts = new HashMap<String, Integer>();

		this.strength = strength;
		this.constitution = constitution;
		this.dexterity = dexterity;
		this.intellect = intellect;
		this.wisdom = wisdom;
	}

	public Type(String name, String description, int strength, int constitution, int dexterity, int intellect,
			int wisdom, int strengthscale, int constitutionscale, int dexterityscale, int intellectscale,
			int wisdomscale)
	{
		this.name = name;
		this.description = description;

		armor = new ArrayList<Material>();
		weapon = new HashMap<Material, Integer>();
		casts = new HashMap<String, Integer>();

		this.strength = strength;
		this.constitution = constitution;
		this.dexterity = dexterity;
		this.intellect = intellect;
		this.wisdom = wisdom;

		this.strengthscale = strengthscale;
		this.constitutionscale = constitutionscale;
		this.dexterityscale = dexterityscale;
		this.intellectscale = intellectscale;
		this.wisdomscale = wisdomscale;
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

	public int getConstitution()
	{
		return constitution;
	}

	public int getDexterity()
	{
		return dexterity;
	}

	public int getIntellect()
	{
		return intellect;
	}

	public int getWisdom()
	{
		return wisdom;
	}

	public int getStrengthScale()
	{
		return strengthscale;
	}

	public int getConstitutionScale()
	{
		return constitutionscale;
	}

	public int getDexterityScale()
	{
		return dexterityscale;
	}

	public int getIntellectScale()
	{
		return intellectscale;
	}

	public int getWisdomScale()
	{
		return wisdomscale;
	}
}
