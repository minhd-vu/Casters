package Cast.Essentials;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.connorlinfoot.actionbarapi.ActionBarAPI;

import Cast.Main;
import Cast.Configs.Config;
import Cast.Essentials.Effects.Effect;
import Cast.Party.Invite;
import Cast.Party.Party;
import me.tigerhix.lib.scoreboard.ScoreboardLib;
import me.tigerhix.lib.scoreboard.common.EntryBuilder;
import me.tigerhix.lib.scoreboard.common.animate.HighlightedString;
import me.tigerhix.lib.scoreboard.type.Entry;
import me.tigerhix.lib.scoreboard.type.Scoreboard;
import me.tigerhix.lib.scoreboard.type.ScoreboardHandler;

public class Caster
{
	private Player player;

	private Party party;
	private Invite invite;

	private Config config;

	private String title;
	private String channel;

	private String type;
	private String race;
	private String job;

	private int typelevel;
	private int typemaxlevel;

	private int racelevel;
	private int racemaxlevel;

	private int joblevel;
	private int jobmaxlevel;

	private double scale;

	private float typeexp;
	private float typemaxexp;

	private float raceexp;
	private float racemaxexp;

	private float jobexp;
	private float jobmaxexp;

	private double health;
	private double maxhealth;
	private double basemaxhealth;
	private double healthregen;
	private double basehealthregen;

	private double mana;
	private double maxmana;
	private double basemaxmana;
	private double manaregen;
	private double basemanaregen;
	private double manatimer;

	private int points;
	private int strength;
	private int constitution;
	private int dexterity;
	private int intellect;
	private int wisdom;

	private double strengthscale;
	private double constitutionscale;
	private double dexterityscale;
	private double intellectscale;
	private double wisdomscale;
	private double wisdomregenscale;

	private Set<Material> armor = new HashSet<Material>();
	private HashMap<Material, Integer> weapon = new HashMap<Material, Integer>();

	private HashMap<String, Boolean> casting = new HashMap<String, Boolean>();
	private HashMap<String, Boolean> warmingup = new HashMap<String, Boolean>();
	private HashMap<String, Effect> effects = new HashMap<String, Effect>();
	private HashMap<String, Integer> casts = new HashMap<String, Integer>();

	private final String header = ChatColor.DARK_GRAY + "[" + ChatColor.DARK_AQUA + "Cast" + ChatColor.DARK_GRAY + "]"
			+ ChatColor.WHITE + " ";

	public Caster(Player player)
	{
		this.player = player;

		party = null;
		invite = null;

		if (!Main.getConfigManager().getFileExists(this.player.getName() + ".yml"))
		{
			config = Main.getConfigManager().getNewConfig(this.player.getName() + ".yml",
					new String[] { this.player.getName() + " Config File." });
			setNewConfig();

			Bukkit.broadcastMessage(ChatColor.DARK_AQUA + "Welcome " + ChatColor.WHITE + player.getName()
					+ ChatColor.DARK_AQUA + " To CasterCraft!");
		}

		else
		{
			config = Main.getConfigManager().getConfig(this.player.getName() + ".yml");
		}

		getConfigType();
		getConfigRace();
		getConfigJob();
		getConfigChat();
		getConfigExperience();
		getConfigStats();
		getConfigHealth();
		getConfigMana();

		effects.put("Stunned", new Effect());
		effects.put("Bleeding", new Effect());
		effects.put("Siphoning", new Effect());
		effects.put("Siphoned", new Effect());
		effects.put("Silenced", new Effect());
		effects.put("Reflecting", new Effect());
		effects.put("Taunted", new Effect());
		effects.put("Taunting", new Effect());
		effects.put("Invisible", new Effect());
		effects.put("Backstabbing", new Effect());

		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				if (mana + manaregen <= maxmana)
				{
					mana += manaregen;
				}

				else
				{
					mana = maxmana;
				}
			}

		}.runTaskTimer(Main.getInstance(), 0, (long) manatimer);

		Scoreboard scoreboard = (Scoreboard) ScoreboardLib.createScoreboard(player).setHandler(new ScoreboardHandler()
		{
			private final HighlightedString casterstext =
					new HighlightedString("Casters", ChatColor.DARK_AQUA.toString() + ChatColor.BOLD.toString(),
							ChatColor.AQUA.toString() + ChatColor.BOLD.toString());
			private final HighlightedString nametext = new HighlightedString(player.getName(), "&6", "&e");

			public String getTitle(Player player)
			{
				return null;
			}

			public List<Entry> getEntries(Player player)
			{
				EntryBuilder entrybuilder = new EntryBuilder();
				entrybuilder.next("    " + ChatColor.DARK_GRAY + ChatColor.MAGIC + "###" + casterstext.next()
						+ ChatColor.DARK_GRAY + ChatColor.MAGIC + "###");
				entrybuilder.next("    " + nametext.next());
				entrybuilder.blank();
				entrybuilder.next("    " + ChatColor.GREEN + "Class: " + type);
				entrybuilder.next("    " + ChatColor.DARK_GREEN + "Race: " + race);
				entrybuilder.next("    " + ChatColor.BLUE + "Job: " + job);
				entrybuilder.blank();
				entrybuilder.next("    " + ChatColor.DARK_AQUA + "Cooldowns:");

				for (String cast : Main.getCasts().keySet())
				{
					if (Main.getCasts().get(cast).getCooldown().hasCooldown(player.getName()))
					{
						entrybuilder.next("    " + ChatColor.AQUA + Main.getCasts().get(cast).getName() + ": "
								+ Main.getCasts().get(cast).getCooldown().getCooldown(player.getName()));
					}
				}

				entrybuilder.blank();

				entrybuilder.next("    " + ChatColor.DARK_PURPLE + "Effects:");

				for (String effect : effects.keySet())
				{
					if (effects.get(effect).hasTime())
					{
						entrybuilder
								.next("    " + ChatColor.LIGHT_PURPLE + effect + " " + effects.get(effect).getTime());
					}
				}

				entrybuilder.blank();

				return entrybuilder.build();
			}
		}).setUpdateInterval(2);

		scoreboard.activate();

		new BukkitRunnable()
		{
			@SuppressWarnings("deprecation")
			@Override
			public void run()
			{
				ActionBarAPI.sendActionBar(player,
						ChatColor.RED + "❤ " + Double.parseDouble(new DecimalFormat("##.#").format(player.getHealth()))
								+ "/" + Double.parseDouble(new DecimalFormat("##.#").format(player.getMaxHealth()))
								+ "        " + ChatColor.BLUE + "✦ "
								+ Double.parseDouble(new DecimalFormat("##.#").format(mana)) + "/"
								+ Double.parseDouble(new DecimalFormat("##.#").format(maxmana)));
			}

		}.runTaskTimer(Main.getInstance(), 0, 2);
	}

	public Caster(UUID uuid)
	{
		this.player = Bukkit.getPlayer(uuid);
	}

	public Player getPlayer()
	{
		return player;
	}

	public Party getParty()
	{
		return party;
	}

	public boolean hasParty()
	{
		return party != null;
	}

	public Invite getInvite()
	{
		return invite;
	}

	public boolean hasInvite()
	{
		return invite != null;
	}

	private void getConfigType()
	{
		type = config.getString("Type");

		for (Type c : Main.getClasses())
		{
			if (c.getName().equals(type))
			{
				strengthscale = c.getStrengthScale();
				constitutionscale = c.getConstitutionScale();
				dexterityscale = c.getDexterityScale();
				intellectscale = c.getIntellectScale();
				wisdomscale = c.getWisdomScale();
				wisdomregenscale = c.getWisdomScale();

				armor.addAll(c.getArmor());
				weapon.putAll(c.getWeapon());
				casts.putAll(c.getCasts());

				break;
			}
		}
	}

	private void getConfigRace()
	{
		race = config.getString("Race");

		for (Type c : Main.getRaces())
		{
			if (c.getName().equals(race))
			{
				armor.addAll(c.getArmor());
				weapon.putAll(c.getWeapon());
				casts.putAll(c.getCasts());

				break;
			}
		}
	}

	private void getConfigJob()
	{
		job = config.getString("Job");

		for (Type c : Main.getJobs())
		{
			if (c.getName().equals(job))
			{
				armor.addAll(c.getArmor());
				weapon.putAll(c.getWeapon());
				casts.putAll(c.getCasts());

				break;
			}
		}
	}

	private void getConfigChat()
	{
		channel = config.getString("Channel");
		title = config.getString("Title");
	}

	private void getConfigExperience()
	{
		typelevel = config.getInt("Level.Type.Current");
		typemaxlevel = config.getInt("Level.Type.Max");
		racelevel = config.getInt("Level.Race.Current");
		racemaxlevel = config.getInt("Level.Race.Max");
		joblevel = config.getInt("Level.Job.Current");
		jobmaxlevel = config.getInt("Level.Job.Max");

		typeexp = (float) config.getDouble("Exp.Type.Current");
		typemaxexp = (float) config.getDouble("Exp.Type.Max");
		raceexp = (float) config.getDouble("Exp.Race.Current");
		racemaxexp = (float) config.getDouble("Exp.Race.Max");
		jobexp = (float) config.getDouble("Exp.Job.Current");
		jobmaxexp = (float) config.getDouble("Exp.Job.Max");

		player.setLevel(typelevel);
		player.setExp(typeexp / typemaxexp);
	}

	private void getConfigStats()
	{
		points = config.getInt("Stats.Points");
		strength = config.getInt("Stats.Strength");
		constitution = config.getInt("Stats.Constitution");
		dexterity = config.getInt("Stats.Dexterity");
		intellect = config.getInt("Stats.Intellect");
		wisdom = config.getInt("Stats.Wisdom");
	}

	@SuppressWarnings("deprecation")
	private void getConfigHealth()
	{
		health = config.getDouble("Health.Current");
		basemaxhealth = config.getDouble("Health.Max");
		maxhealth = basemaxhealth + constitution * constitutionscale;

		if (health > maxhealth)
		{
			health = maxhealth;
		}

		player.setMaxHealth(maxhealth);
		player.setHealth(health);
		player.setHealthScale(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getDefaultValue());
	}

	private void getConfigMana()
	{
		mana = config.getDouble("Mana.Current");
		basemaxmana = config.getDouble("Mana.Max");
		maxmana = basemaxmana + wisdom * wisdomscale;

		basemanaregen = config.getDouble("Mana.Regen");
		manaregen = basemanaregen + wisdom * wisdomregenscale;
		manatimer = config.getDouble("Mana.Timer");

		if (mana > maxmana)
		{
			mana = maxmana;
		}
	}

	public String getChannel()
	{
		return channel;
	}

	public String getTitle()
	{
		return title;
	}

	public String getType()
	{
		return type;
	}

	public String getRace()
	{
		return race;
	}

	public String getJob()
	{
		return job;
	}

	public boolean hasEffect(String name)
	{
		if (effects.containsKey(name))
		{
			return effects.get(name).hasTime();
		}

		return false;
	}

	public int getTypeLevel()
	{
		return typelevel;
	}

	public int getTypeMaxLevel()
	{
		return typemaxlevel;
	}

	public float getTypeExp()
	{
		return typeexp;
	}

	public float getTypeMaxExp()
	{
		return typemaxexp;
	}

	public int getRaceLevel()
	{
		return racelevel;
	}

	public int getRaceMaxLevel()
	{
		return racemaxlevel;
	}

	public float getRaceExp()
	{
		return raceexp;
	}

	public float getRaceMaxExp()
	{
		return racemaxexp;
	}

	public int getJobLevel()
	{
		return joblevel;
	}

	public int getJobMaxLevel()
	{
		return jobmaxlevel;
	}

	public float getJobExp()
	{
		return jobexp;
	}

	public float getJobMaxExp()
	{
		return jobmaxexp;
	}

	public double getHealth()
	{
		return health;
	}

	public double getMaxHealth()
	{
		return maxhealth;
	}

	public boolean hasMana(double manacost, String name)
	{
		if (mana >= manacost)
		{
			return true;
		}

		player.sendMessage(header + ChatColor.WHITE + name + ChatColor.GRAY + ": Not Enough Mana!");

		return false;
	}

	public double getMaxMana()
	{
		return maxmana;
	}

	public int getPoints()
	{
		return points;
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

	public double getStrengthScale()
	{
		return strengthscale;
	}

	public double getConstitutionScale()
	{
		return constitutionscale;
	}

	public double getDexterityScale()
	{
		return dexterityscale;
	}

	public double getIntellectScale()
	{
		return intellectscale;
	}

	public double getWisdomScale()
	{
		return wisdomscale;
	}

	public Set<Material> getArmor()
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

	public boolean hasCast(String name)
	{
		if (!casts.containsKey(name))
		{
			player.sendMessage(
					header + "You" + ChatColor.GRAY + " Cannot Cast " + ChatColor.WHITE + name + ChatColor.GRAY + "!");
			return false;
		}

		else if (casts.get(name) > typelevel)
		{
			player.sendMessage(header + "You" + ChatColor.GRAY + " Must Be Level " + ChatColor.WHITE + casts.get(name)
					+ ChatColor.GRAY + " To Use " + ChatColor.WHITE + name + ChatColor.GRAY + "!");
			return false;
		}

		return true;
	}

	public int getCastLevel(String name)
	{
		if (casts.containsKey(name))
		{
			return casts.get(name);
		}

		return 0;
	}

	public boolean isCasting(String name)
	{
		if (casting.containsKey(name))
		{
			return casting.get(name);
		}

		return false;
	}

	public boolean isWarmingUp()
	{
		for (boolean bool : warmingup.values())
		{
			if (bool)
			{
				return true;
			}
		}

		return false;
	}

	public boolean isWarmingUp(String name)
	{
		if (warmingup.containsKey(name))
		{
			return warmingup.get(name);
		}

		return false;
	}

	public boolean isStunned(String name)
	{
		if (effects.get("Stunned").hasTime())
		{
			player.sendMessage(header + ChatColor.WHITE + "You" + ChatColor.GRAY + " Cannot Cast " + ChatColor.WHITE
					+ name + ChatColor.GRAY + " While Stunned!");
			return true;
		}

		return false;
	}

	public boolean isSilenced(String name)
	{
		if (effects.get("Silenced").hasTime())
		{
			player.sendMessage(header + ChatColor.WHITE + "You" + ChatColor.GRAY + " Cannot Cast " + ChatColor.WHITE
					+ name + ChatColor.GRAY + " While Silenced!");
			return true;
		}

		return false;
	}

	public void setParty(Party party)
	{
		this.party = party;
	}

	public void setInvite(Invite invite)
	{
		this.invite = invite;
	}

	public void setConfig()
	{
		config.set("Type", type);
		config.set("Race", race);
		config.set("Job", job);
		config.set("Channel", channel);
		config.set("Title", title);
		config.set("Level.Type.Current", typelevel);
		config.set("Level.Type.Max", typemaxlevel);
		config.set("Level.Race.Current", racelevel);
		config.set("Level.Race.Max", racemaxlevel);
		config.set("Level.Job.Current", joblevel);
		config.set("Level.Job.Max", jobmaxlevel);
		config.set("Health.Current", player.getHealth());
		config.set("Health.Max", basemaxhealth);
		config.set("Mana.Current", mana);
		config.set("Mana.Max", basemaxmana);
		config.set("Mana.Regen", basemanaregen);
		config.set("Exp.Type.Current", typeexp);
		config.set("Exp.Type.Max", typemaxexp);
		config.set("Exp.Race.Current", raceexp);
		config.set("Exp.Race.Max", racemaxexp);
		config.set("Exp.Job.Current", jobexp);
		config.set("Exp.Job.Max", jobmaxexp);
		config.set("Stats.Points", points);
		config.set("Stats.Strength", strength);
		config.set("Stats.Constitution", constitution);
		config.set("Stats.Dexterity", dexterity);
		config.set("Stats.Intellect", intellect);
		config.set("Stats.Wisdom", wisdom);
		config.save();
	}

	private void setNewConfig()
	{
		config.set("Type", "Peasant");
		config.set("Race", "Soul");
		config.set("Job", "Wanderer");
		config.set("Channel", "Global");
		config.set("Title", null);
		config.set("Level.Type.Current", 1);
		config.set("Level.Type.Max", 50);
		config.set("Level.Race.Current", 1);
		config.set("Level.Race.Max", 50);
		config.set("Level.Job.Current", 1);
		config.set("Level.Job.Max", 50);
		config.set("Health.Current", 20.0D);
		config.set("Health.Max", 20.0D);
		config.set("Mana.Current", 20.0D);
		config.set("Mana.Max", 20.0D);
		config.set("Mana.Regen", 1.0D);
		config.set("Mana.Timer", 20.0D);
		config.set("Exp.Type.Current", 0);
		config.set("Exp.Type.Max", 100);
		config.set("Exp.Race.Current", 0);
		config.set("Exp.Race.Max", 100);
		config.set("Exp.Job.Current", 0);
		config.set("Exp.Job.Max", 100);
		config.set("Stats.Points", 0);
		config.set("Stats.Strength", 0);
		config.set("Stats.Constitution", 0);
		config.set("Stats.Dexterity", 0);
		config.set("Stats.Intellect", 0);
		config.set("Stats.Wisdom", 0);
		config.save();
	}

	public void setCasting(String name, boolean casting)
	{
		this.casting.put(name, casting);
	}

	public void setWarmingUp(String name, boolean warmingup)
	{
		this.warmingup.put(name, warmingup);
	}

	public void setEffect(String name, double duration)
	{
		if (effects.containsKey(name))
		{
			effects.get(name).setDuration(duration);
		}
	}

	public void setChannel(String channel)
	{
		this.channel = channel;
	}

	public void getConfigs()
	{
		armor.clear();
		weapon.clear();
		casts.clear();

		getConfigJob();
		getConfigRace();
		getConfigType();
	}

	public void setType(String type)
	{
		this.type = type;
		config.set("Type", this.type);
		getConfigs();

		player.sendMessage(header + ChatColor.GRAY + "You Have Chosen The Path Of The " + ChatColor.WHITE + this.type
				+ ChatColor.GRAY + "!");
	}

	public void setRace(String race)
	{
		this.race = race;
		config.set("Race", this.race);
		getConfigs();

		player.sendMessage(header + ChatColor.GRAY + "You Have Chosen The Path Of The " + ChatColor.WHITE + this.race
				+ ChatColor.GRAY + "!");
	}

	public void setJob(String job)
	{
		this.job = job;
		config.set("Job", this.job);
		getConfigs();

		player.sendMessage(header + ChatColor.GRAY + "You Have Chosen The Path Of The " + ChatColor.WHITE + this.job
				+ ChatColor.GRAY + "!");
	}

	public void setTypeLevel(int typelevel)
	{
		this.typelevel = typelevel;
	}

	public void setTypeExp(float typeexp)
	{
		this.typeexp = typeexp;
	}

	public void setTypeMaxExp()
	{
		typemaxexp = (float) (typelevel * scale + scale);
	}

	public void setRaceLevel(int racelevel)
	{
		this.racelevel = racelevel;
	}

	public void setRaceExp(float raceexp)
	{
		this.raceexp = raceexp;
	}

	public void setRaceMaxExp()
	{
		racemaxexp = (float) (typelevel * scale + scale);
	}

	public void setJobLevel(int joblevel)
	{
		this.joblevel = joblevel;
	}

	public void setJobExp(float jobexp)
	{
		this.jobexp = jobexp;
	}

	public void setJobMaxExp()
	{
		jobmaxexp = (float) (joblevel * scale + scale);
	}

	public void setScale(double scale)
	{
		this.scale = scale;
	}

	public void setHealth(double health)
	{
		this.health = health;
	}

	public void setMaxHealth(double maxhealth)
	{
		this.maxhealth = maxhealth;
	}

	public void setMana(double manacost)
	{
		mana -= manacost;
	}

	public void setMaxMana(double maxmana)
	{
		this.maxmana = maxmana;
	}

	public void setPoints(int points)
	{
		this.points = points;
	}

	public void setStrength(int strength)
	{
		this.strength = strength;
	}

	public void setConstitution(int constitution)
	{
		this.constitution = constitution;
	}

	public void setDexterity(int dexterity)
	{
		this.dexterity = dexterity;
	}

	public void setIntellect(int intellect)
	{
		this.intellect = intellect;
	}

	public void setWisdom(int wisdom)
	{
		this.wisdom = wisdom;
	}
}
