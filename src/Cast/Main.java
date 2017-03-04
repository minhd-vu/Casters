package Cast;

import Cast.Casters.*;
import Cast.Casts.Actives.*;
import Cast.Casts.CastList;
import Cast.Casts.Casts;
import Cast.Casts.Passives.PassiveBackstab;
import Cast.Casts.Passives.PassiveFlameshield;
import Cast.Casts.Targetted.*;
import Cast.Casts.Types.Cast;
import Cast.Configs.ConfigManager;
import Cast.Essentials.*;
import Cast.Essentials.Chat.Chat;
import Cast.Essentials.Chat.ChatChannel;
import Cast.Essentials.Chat.ChatTitles;
import Cast.Party.*;
import Cast.Wands.*;
import com.dbsoftware.titletabandbarapi.barapi.ActionBarManager;
import com.dbsoftware.titletabandbarapi.tabapi.TabManager;
import com.dbsoftware.titletabandbarapi.titleapi.TitleManager;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Main extends JavaPlugin implements Listener
{
	private static Main instance;

	private static ConfigManager manager;

	private static ActionBarManager actionbarmanager;
	private static TitleManager titlemanager;
	private static TabManager tabmanager;

	private static List<Type> types;
	private static List<Type> classes;
	private static List<Type> races;
	private static List<Type> jobs;
	private static List<Mob> mobs;

	private static HashMap<UUID, Caster> casters;

	private static Casters casterscmd;
	private static CastersInfo castersinfo;
	private static CastersLevel casterslevel;
	private static CastersStats castersstats;
	private static CastersChoose casterschoose;
	private static CastersClasses castersclasses;
	private static CastersRaces castersraces;
	private static CastersJobs castersjobs;
	private static CastersArmor castersarmor;
	private static CastersWeapon castersweapon;
	private static CastersWhoIs casterswhois;
	private static CastersRecipes castersrecipes;

	private static Experience experience;
	private static Enchant enchant;
	private static Armor armor;
	private static Attack attack;
	private static Regen regen;

	private static Chat chat;
	private static ChatTitles chattitles;
	private static ChatChannel chatchannel;

	private static Wands wand;
	private static WandList wandlist;
	private static WandInferno wandinferno;
	private static WandDistorter wanddistorter;
	private static WandShaman wandshaman;
	private static WandWarlock wandwarlock;

	private static HashMap<String, Cast> casts;

	private static Casts cast;
	private static CastList castlist;
	private static CastFireball castfireball;
	private static CastDarkBomb castdarkbomb;
	private static CastBolt castbolt;
	private static CastRevive castrevive;
	private static CastFireBomb castfirebomb;
	private static CastFireCharge castfirecharge;
	private static CastCharge castcharge;
	private static CastStrike caststrike;
	private static CastBandage castbandage;
	private static CastBeasts castbeasts;
	private static CastLightningStorm castlightningstorm;
	private static CastChainLightning castchainlightning;
	private static CastReflect castreflect;
	private static CastSiphon castsiphon;
	private static CastTaunt casttaunt;
	private static CastVanish castvanish;
	private static CastBomb castbomb;
	private static CastMount castmount;
	private static CastPoison castpoison;
	private static CastBash castbash;
	private static CastMute castmute;
	private static CastDefensiveStance castdefensivestance;

	private static PassiveBackstab passivebackstab;
	private static PassiveFlameshield passiveflameshield;

	private static List<Party> parties;

	private static Parties partycmd;
	private static PartyCreate partycreate;
	private static PartyMembers partymembers;
	private static PartyInvite partyinvite;
	private static PartyRemove partyremove;
	private static PartyAccept partyaccept;
	private static PartyDecline partydecline;
	private static PartyChat partychat;
	private static PartyLeader partyleader;
	private static PartyLeave partyleave;
	private static PartyDisband partydisband;

	public static Main getInstance()
	{
		return instance;
	}

	public static ActionBarManager getActionBarManager()
	{
		return actionbarmanager;
	}

	public static TitleManager getTitleManager()
	{
		return titlemanager;
	}

	public static TabManager getTabManager()
	{
		return tabmanager;
	}

	public static List<Type> getTypes()
	{
		return types;
	}

	public static List<Type> getClasses()
	{
		return classes;
	}

	public static List<Type> getRaces()
	{
		return races;
	}

	public static List<Type> getJobs()
	{
		return jobs;
	}

	public static List<Mob> getMobs()
	{
		return mobs;
	}

	public static HashMap<UUID, Caster> getCasters()
	{
		return casters;
	}

	public static HashMap<String, Cast> getCasts()
	{
		return casts;
	}

	public static ConfigManager getConfigManager()
	{
		return manager;
	}

	public static List<Party> getParties()
	{
		return parties;
	}

	@Override
	public void onDisable()
	{
		for (Player player : Bukkit.getOnlinePlayers())
		{
			player.kickPlayer(
					ChatColor.DARK_AQUA + "  " + ChatColor.BOLD + "CasterCraft" + ChatColor.GRAY + " Is Restarting!");
		}
	}

	@Override
	public void onEnable()
	{
		instance = this;

		manager = new ConfigManager(this);

		actionbarmanager = new ActionBarManager();
		titlemanager = new TitleManager();
		tabmanager = new TabManager();

		types = new ArrayList<Type>();
		classes = new ArrayList<Type>();
		races = new ArrayList<Type>();
		jobs = new ArrayList<Type>();

		Type paladin = new Type("Paladin", "Description");
		paladin.setStrength(1);
		paladin.setConstitution(1);
		paladin.setDexterity(1);
		paladin.setIntellect(1);
		paladin.setWisdom(1);
		paladin.getArmor().add(Material.DIAMOND_HELMET);
		paladin.getArmor().add(Material.DIAMOND_CHESTPLATE);
		paladin.getArmor().add(Material.DIAMOND_LEGGINGS);
		paladin.getArmor().add(Material.DIAMOND_BOOTS);
		paladin.getWeapon().put(Material.SHIELD, 3);
		paladin.getWeapon().put(Material.DIAMOND_SWORD, 5);
		paladin.getWeapon().put(Material.IRON_SWORD, 4);
		paladin.getWeapon().put(Material.GOLD_SWORD, 3);
		paladin.getWeapon().put(Material.STONE_SWORD, 2);
		paladin.getWeapon().put(Material.WOOD_SWORD, 1);
		paladin.getCasts().put("Reflect", 1);
		paladin.getCasts().put("Bandage", 1);

		Type cavalier = new Type("Cavalier", "Description");
		cavalier.getArmor().add(Material.IRON_HELMET);
		cavalier.getArmor().add(Material.DIAMOND_CHESTPLATE);
		cavalier.getArmor().add(Material.CHAINMAIL_LEGGINGS);
		cavalier.getArmor().add(Material.DIAMOND_BOOTS);
		cavalier.getWeapon().put(Material.DIAMOND_SPADE, 10);
		cavalier.getWeapon().put(Material.IRON_SPADE, 7);
		cavalier.getWeapon().put(Material.GOLD_SPADE, 6);
		cavalier.getWeapon().put(Material.STONE_SPADE, 5);
		cavalier.getWeapon().put(Material.WOOD_SPADE, 4);
		cavalier.getCasts().put("Charge", 1);
		cavalier.getCasts().put("Mount", 1);

		Type barbarian = new Type("Barbarian", "Description");
		barbarian.getArmor().add(Material.DIAMOND_HELMET);
		barbarian.getArmor().add(Material.IRON_CHESTPLATE);
		barbarian.getArmor().add(Material.CHAINMAIL_LEGGINGS);
		barbarian.getArmor().add(Material.CHAINMAIL_BOOTS);
		barbarian.getWeapon().put(Material.DIAMOND_AXE, 10);
		barbarian.getWeapon().put(Material.IRON_AXE, 5);
		barbarian.getWeapon().put(Material.GOLD_AXE, 5);
		barbarian.getWeapon().put(Material.STONE_AXE, 5);
		barbarian.getWeapon().put(Material.WOOD_AXE, 5);
		barbarian.getCasts().put("Taunt", 1);
		barbarian.getCasts().put("Bash", 1);
		barbarian.getCasts().put("Mute", 1);

		Type blackguard = new Type("Blackguard", "Description");
		blackguard.getArmor().add(Material.CHAINMAIL_HELMET);
		blackguard.getArmor().add(Material.IRON_CHESTPLATE);
		blackguard.getArmor().add(Material.IRON_LEGGINGS);
		blackguard.getArmor().add(Material.CHAINMAIL_BOOTS);
		blackguard.getWeapon().put(Material.DIAMOND_SWORD, 10);
		blackguard.getWeapon().put(Material.IRON_SWORD, 8);
		blackguard.getWeapon().put(Material.GOLD_SWORD, 7);
		blackguard.getWeapon().put(Material.STONE_SWORD, 6);
		blackguard.getWeapon().put(Material.WOOD_SWORD, 5);
		blackguard.getCasts().put("Strike", 1);

		Type assassin = new Type("Assassin", "Description");
		assassin.getArmor().add(Material.GOLD_HELMET);
		assassin.getArmor().add(Material.LEATHER_CHESTPLATE);
		assassin.getArmor().add(Material.LEATHER_LEGGINGS);
		assassin.getArmor().add(Material.LEATHER_BOOTS);
		assassin.getWeapon().put(Material.DIAMOND_SWORD, 10);
		assassin.getWeapon().put(Material.IRON_SWORD, 8);
		assassin.getWeapon().put(Material.GOLD_SWORD, 7);
		assassin.getWeapon().put(Material.STONE_SWORD, 6);
		assassin.getWeapon().put(Material.WOOD_SWORD, 5);
		assassin.getCasts().put("Vanish", 1);
		assassin.getCasts().put("Backstab", 1);
		assassin.getCasts().put("Poison", 1);

		Type duelist = new Type("Duelist", "Description");
		duelist.getArmor().add(Material.CHAINMAIL_HELMET);
		duelist.getArmor().add(Material.LEATHER_CHESTPLATE);
		duelist.getArmor().add(Material.LEATHER_LEGGINGS);
		duelist.getArmor().add(Material.IRON_BOOTS);
		duelist.getWeapon().put(Material.DIAMOND_SWORD, 10);
		duelist.getWeapon().put(Material.IRON_SWORD, 8);
		duelist.getWeapon().put(Material.GOLD_SWORD, 7);
		duelist.getWeapon().put(Material.STONE_SWORD, 6);
		duelist.getWeapon().put(Material.WOOD_SWORD, 5);
		duelist.getCasts().put("Strike", 1);

		Type fletcher = new Type("Fletcher", "Description");
		fletcher.getArmor().add(Material.GOLD_HELMET);
		fletcher.getArmor().add(Material.LEATHER_CHESTPLATE);
		fletcher.getArmor().add(Material.LEATHER_LEGGINGS);
		fletcher.getArmor().add(Material.GOLD_BOOTS);
		fletcher.getWeapon().put(Material.BOW, 3);
		fletcher.getWeapon().put(Material.IRON_SWORD, 8);
		fletcher.getWeapon().put(Material.GOLD_SWORD, 7);
		fletcher.getWeapon().put(Material.STONE_SWORD, 6);
		fletcher.getWeapon().put(Material.WOOD_SWORD, 5);
		fletcher.getCasts().put("Beasts", 1);

		Type musketeer = new Type("Musketeer", "Description");
		musketeer.getArmor().add(Material.IRON_HELMET);
		musketeer.getArmor().add(Material.CHAINMAIL_CHESTPLATE);
		musketeer.getArmor().add(Material.LEATHER_LEGGINGS);
		musketeer.getArmor().add(Material.LEATHER_BOOTS);
		musketeer.getWeapon().put(Material.DIAMOND_BARDING, 7);
		musketeer.getWeapon().put(Material.IRON_BARDING, 6);
		musketeer.getWeapon().put(Material.GOLD_BARDING, 5);
		musketeer.getWeapon().put(Material.IRON_SWORD, 8);
		musketeer.getWeapon().put(Material.GOLD_SWORD, 7);
		musketeer.getWeapon().put(Material.STONE_SWORD, 6);
		musketeer.getWeapon().put(Material.WOOD_SWORD, 5);
		musketeer.getCasts().put("Bomb", 1);

		Type distorter = new Type("Distorter", "Description");
		distorter.getArmor().add(Material.LEATHER_HELMET);
		distorter.getArmor().add(Material.LEATHER_CHESTPLATE);
		distorter.getArmor().add(Material.LEATHER_LEGGINGS);
		distorter.getArmor().add(Material.DIAMOND_BOOTS);
		distorter.getWeapon().put(Material.DIAMOND_HOE, 5);
		distorter.getWeapon().put(Material.IRON_HOE, 5);
		distorter.getWeapon().put(Material.GOLD_HOE, 5);
		distorter.getWeapon().put(Material.STONE_HOE, 5);
		distorter.getWeapon().put(Material.WOOD_HOE, 5);
		distorter.getCasts().put("DarkBomb", 1);

		Type inferno = new Type("Inferno", "Description");
		inferno.getArmor().add(Material.LEATHER_HELMET);
		inferno.getArmor().add(Material.LEATHER_CHESTPLATE);
		inferno.getArmor().add(Material.LEATHER_LEGGINGS);
		inferno.getArmor().add(Material.LEATHER_BOOTS);
		inferno.getWeapon().put(Material.DIAMOND_HOE, 5);
		inferno.getWeapon().put(Material.IRON_HOE, 5);
		inferno.getWeapon().put(Material.GOLD_HOE, 5);
		inferno.getWeapon().put(Material.STONE_HOE, 5);
		inferno.getWeapon().put(Material.WOOD_HOE, 5);
		inferno.getCasts().put("Fireball", 1);
		inferno.getCasts().put("FireCharge", 1);
		inferno.getCasts().put("FireBomb", 1);
		inferno.getCasts().put("Flameshield", 1);

		Type shaman = new Type("Shaman", "Description");
		shaman.getArmor().add(Material.CHAINMAIL_HELMET);
		shaman.getArmor().add(Material.CHAINMAIL_CHESTPLATE);
		shaman.getArmor().add(Material.LEATHER_LEGGINGS);
		shaman.getArmor().add(Material.LEATHER_BOOTS);
		shaman.getWeapon().put(Material.DIAMOND_HOE, 5);
		shaman.getWeapon().put(Material.IRON_HOE, 5);
		shaman.getWeapon().put(Material.GOLD_HOE, 5);
		shaman.getWeapon().put(Material.STONE_HOE, 5);
		shaman.getWeapon().put(Material.WOOD_HOE, 5);
		shaman.getCasts().put("Bolt", 1);
		shaman.getCasts().put("LightningStorm", 1);
		shaman.getCasts().put("ChainLightning", 1);

		Type warlock = new Type("Warlock", "Description");
		warlock.getArmor().add(Material.LEATHER_HELMET);
		warlock.getArmor().add(Material.LEATHER_CHESTPLATE);
		warlock.getArmor().add(Material.IRON_LEGGINGS);
		warlock.getArmor().add(Material.LEATHER_BOOTS);
		warlock.getWeapon().put(Material.DIAMOND_HOE, 5);
		warlock.getWeapon().put(Material.IRON_HOE, 5);
		warlock.getWeapon().put(Material.GOLD_HOE, 5);
		warlock.getWeapon().put(Material.STONE_HOE, 5);
		warlock.getWeapon().put(Material.WOOD_HOE, 5);
		warlock.getCasts().put("DarkBomb", 1);
		warlock.getCasts().put("Siphon", 1);

		Type oracle = new Type("Oracle", "Description");
		oracle.getArmor().add(Material.LEATHER_HELMET);
		oracle.getArmor().add(Material.LEATHER_CHESTPLATE);
		oracle.getArmor().add(Material.IRON_LEGGINGS);
		oracle.getArmor().add(Material.LEATHER_BOOTS);
		oracle.getWeapon().put(Material.DIAMOND_SPADE, 10);
		oracle.getWeapon().put(Material.IRON_SPADE, 7);
		oracle.getWeapon().put(Material.GOLD_SPADE, 6);
		oracle.getWeapon().put(Material.STONE_SPADE, 5);
		oracle.getWeapon().put(Material.WOOD_SPADE, 4);

		Type bloodmage = new Type("Bloodmage", "Description");
		bloodmage.getArmor().add(Material.LEATHER_HELMET);
		bloodmage.getArmor().add(Material.LEATHER_CHESTPLATE);
		bloodmage.getArmor().add(Material.IRON_LEGGINGS);
		bloodmage.getArmor().add(Material.LEATHER_BOOTS);
		bloodmage.getWeapon().put(Material.DIAMOND_SPADE, 10);
		bloodmage.getWeapon().put(Material.IRON_SPADE, 7);
		bloodmage.getWeapon().put(Material.GOLD_SPADE, 6);
		bloodmage.getWeapon().put(Material.STONE_SPADE, 5);
		bloodmage.getWeapon().put(Material.WOOD_SPADE, 4);
		bloodmage.getCasts().put("Siphon", 1);

		Type monk = new Type("Monk", "Description");
		monk.getArmor().add(Material.LEATHER_HELMET);
		monk.getArmor().add(Material.LEATHER_CHESTPLATE);
		monk.getArmor().add(Material.IRON_LEGGINGS);
		monk.getArmor().add(Material.LEATHER_BOOTS);
		monk.getWeapon().put(Material.DIAMOND_SPADE, 10);
		monk.getWeapon().put(Material.IRON_SPADE, 7);
		monk.getWeapon().put(Material.GOLD_SPADE, 6);
		monk.getWeapon().put(Material.STONE_SPADE, 5);
		monk.getWeapon().put(Material.WOOD_SPADE, 4);
		monk.getCasts().put("DefensiveStance", 1);

		Type cleric = new Type("Cleric", "Description");
		cleric.getArmor().add(Material.LEATHER_HELMET);
		cleric.getArmor().add(Material.LEATHER_CHESTPLATE);
		cleric.getArmor().add(Material.IRON_LEGGINGS);
		cleric.getArmor().add(Material.LEATHER_BOOTS);
		cleric.getWeapon().put(Material.DIAMOND_SPADE, 10);
		cleric.getWeapon().put(Material.IRON_SPADE, 7);
		cleric.getWeapon().put(Material.GOLD_SPADE, 6);
		cleric.getWeapon().put(Material.STONE_SPADE, 5);
		cleric.getWeapon().put(Material.WOOD_SPADE, 4);
		cleric.getCasts().put("Revive", 1);

		classes.add(paladin);
		classes.add(cavalier);
		classes.add(barbarian);
		classes.add(blackguard);
		classes.add(assassin);
		classes.add(duelist);
		classes.add(fletcher);
		classes.add(musketeer);
		classes.add(distorter);
		classes.add(inferno);
		classes.add(shaman);
		classes.add(warlock);
		classes.add(oracle);
		classes.add(bloodmage);
		classes.add(monk);
		classes.add(cleric);

		Type dwarf = new Type("Dwarf", "Description");
		dwarf.getArmor().add(Material.DIAMOND_HELMET);
		dwarf.getWeapon().put(Material.DIAMOND_PICKAXE, 5);

		Type human = new Type("Human", "Description");
		human.getCasts().put("Bandage", 1);

		Type elf = new Type("Elf", "Description");
		elf.getArmor().add(Material.GOLD_HELMET);
		elf.getWeapon().put(Material.BOW, 3);

		Type troll = new Type("Troll", "Description");
		troll.getArmor().add(Material.IRON_LEGGINGS);

		Type goblin = new Type("Goblin", "Description");
		goblin.getArmor().add(Material.LEATHER_CHESTPLATE);

		Type giant = new Type("Giant", "Description");
		giant.getArmor().add(Material.IRON_CHESTPLATE);

		Type undead = new Type("Undead", "Description");
		undead.getArmor().add(Material.GOLD_HELMET);

		Type demon = new Type("Demon", "Description");
		demon.getArmor().add(Material.GOLD_HELMET);

		races.add(dwarf);
		races.add(human);
		races.add(elf);
		races.add(troll);
		races.add(goblin);
		races.add(giant);
		races.add(demon);
		races.add(undead);

		Type alchemist = new Type("Alchemist", "Description");
		alchemist.getArmor().add(Material.GOLD_HELMET);

		Type enchanter = new Type("Enchanter", "Description");
		enchanter.getArmor().add(Material.GOLD_HELMET);

		Type blacksmith = new Type("Blacksmith", "Description");
		blacksmith.getArmor().add(Material.GOLD_HELMET);

		Type engineer = new Type("Engineer", "Description");
		engineer.getArmor().add(Material.GOLD_HELMET);

		Type artisan = new Type("Artisan", "Description");
		artisan.getArmor().add(Material.GOLD_HELMET);

		Type farmer = new Type("Farmer", "Description");
		farmer.getArmor().add(Material.GOLD_HELMET);

		Type miner = new Type("Miner", "Description");
		miner.getArmor().add(Material.GOLD_HELMET);

		jobs.add(alchemist);
		jobs.add(enchanter);
		jobs.add(blacksmith);
		jobs.add(engineer);
		jobs.add(artisan);
		jobs.add(farmer);
		jobs.add(miner);

		types.addAll(classes);
		types.addAll(races);
		types.addAll(jobs);

		mobs = new ArrayList<Mob>();

		mobs.add(new Mob(EntityType.ZOMBIE, 20, 2));
		mobs.add(new Mob(EntityType.ENDERMAN, 20, 10));
		mobs.add(new Mob(EntityType.SKELETON, 20, 10));
		mobs.add(new Mob(EntityType.SPIDER, 20, 10));
		mobs.add(new Mob(EntityType.CAVE_SPIDER, 20, 10));

		casters = new HashMap<UUID, Caster>();

		casterscmd = new Casters();
		castersinfo = new CastersInfo();
		casterslevel = new CastersLevel();
		castersstats = new CastersStats();
		casterschoose = new CastersChoose();
		castersclasses = new CastersClasses();
		castersraces = new CastersRaces();
		castersjobs = new CastersJobs();
		castersarmor = new CastersArmor();
		castersweapon = new CastersWeapon();
		casterswhois = new CastersWhoIs();
		castersrecipes = new CastersRecipes();

		cast = new Casts();
		castlist = new CastList();
		casts = new HashMap<String, Cast>();

		casts.put("Fireball", castfireball = new CastFireball("Fireball", "Casts A Fireball"));
		casts.put("DarkBomb", castdarkbomb = new CastDarkBomb("DarkBomb", "Casts A Dark Bomb"));
		casts.put("Bolt", castbolt = new CastBolt("Bolt", "Casts A Bolt"));
		casts.put("Revive", castrevive = new CastRevive("Revive", "Revive A Player"));
		casts.put("FireBomb", castfirebomb = new CastFireBomb("FireBomb", "Casts A Fire Bomb"));
		casts.put("FireCharge", castfirecharge = new CastFireCharge("FireCharge", "Casts A Fire Charge"));
		casts.put("Charge", castcharge = new CastCharge("Charge", "Charge Your Opponent"));
		casts.put("Strike", caststrike = new CastStrike("Strike", "Strike Your Opponent"));
		casts.put("Bandage", castbandage = new CastBandage("Bandage", "Bandage Yourself Or An Ally"));
		casts.put("Beasts", castbeasts = new CastBeasts("Beasts", "Summon A Pack Of Wolves"));
		casts.put("LightningStorm",
				castlightningstorm = new CastLightningStorm("LightningStorm", "Cast A Lightning Storm"));
		casts.put("ChainLightning",
				castchainlightning = new CastChainLightning("ChainLightning", "Consecutively Strikes Opponenets"));
		casts.put("Reflect", castreflect = new CastReflect("Reflect", "Relects All Incoming Damage"));
		casts.put("Siphon", castsiphon = new CastSiphon("Siphon", "Siphons Health From Your Opponent"));
		casts.put("Taunt", casttaunt = new CastTaunt("Taunt", "Taunt All Nearby Opponents"));
		casts.put("Vanish", castvanish = new CastVanish("Vanish", "Vanish In A Cloud Of Smoke"));
		casts.put("Bomb", castbomb = new CastBomb("Bomb", " Places A Explosive Device"));
		casts.put("Mount", castmount = new CastMount("Mount", "Mounts Onto A Horse"));
		casts.put("Poison", castpoison = new CastPoison("Poison", "Poisons Your Opponent"));
		casts.put("Bash", castbash = new CastBash("Bash", "Bash Your Opponent And Interrupt Casts"));
		casts.put("Mute", castmute = new CastMute("Mute", "Silence Your Opponent"));
		casts.put("DefensiveStance",
				castdefensivestance = new CastDefensiveStance("DefensiveStance", "Reduces The Damage Party Members Take."));

		casts.put("Backstab", passivebackstab = new PassiveBackstab("Backstab", "Attacks From Behind Deal More"));
		casts.put("Flameshield",
				passiveflameshield = new PassiveFlameshield("Flameshield", "Reduces Fire Damage Dealt To You"));

		experience = new Experience();
		enchant = new Enchant();
		armor = new Armor();
		attack = new Attack();
		regen = new Regen();

		chat = new Chat();
		chattitles = new ChatTitles();
		chatchannel = new ChatChannel();

		wand = new Wands();
		wandlist = new WandList();
		wandinferno = new WandInferno("Inferno");
		wanddistorter = new WandDistorter("Distorter");
		wandshaman = new WandShaman("Shaman");
		wandwarlock = new WandWarlock("Warlock");

		parties = new ArrayList<Party>();
		partycmd = new Parties();
		partycreate = new PartyCreate();
		partymembers = new PartyMembers();
		partyinvite = new PartyInvite();
		partyremove = new PartyRemove();
		partyaccept = new PartyAccept();
		partydecline = new PartyDecline();
		partychat = new PartyChat();
		partyleader = new PartyLeader();
		partyleave = new PartyLeave();
		partydisband = new PartyDisband();

		registerCommands();

		registerEvents(this, this, experience, enchant, armor, attack, regen, chat, wandinferno, wanddistorter,
				wandshaman, wandwarlock, castfireball, castdarkbomb, castbolt, castrevive, castfirebomb, castfirecharge,
				castcharge, caststrike, castbandage, castbeasts, castlightningstorm, castchainlightning, castreflect,
				castsiphon, castvanish, castbomb, castmount, castpoison, passivebackstab, passiveflameshield);

	}

	public void registerCommands()
	{
		CommandHandler castershandler = new CommandHandler();
		CommandHandler wandhandler = new CommandHandler();
		CommandHandler casthandler = new CommandHandler();
		CommandHandler chathandler = new CommandHandler();
		CommandHandler partyhandler = new CommandHandler();

		castershandler.register("casters", casterscmd);
		castershandler.register("info", castersinfo);
		castershandler.register("level", casterslevel);
		castershandler.register("stats", castersstats);
		castershandler.register("choose", casterschoose);
		castershandler.register("classes", castersclasses);
		castershandler.register("races", castersraces);
		castershandler.register("jobs", castersjobs);
		castershandler.register("armor", castersarmor);
		castershandler.register("weapon", castersweapon);
		castershandler.register("whois", casterswhois);
		castershandler.register("recipes", castersrecipes);

		wandhandler.register("wand", wand);
		wandhandler.register("list", wandlist);

		casthandler.register("cast", cast);
		casthandler.register("list", castlist);
		casthandler.register("fireball", castfireball);
		casthandler.register("darkbomb", castdarkbomb);
		casthandler.register("bolt", castbolt);
		casthandler.register("revive", castrevive);
		casthandler.register("firebomb", castfirebomb);
		casthandler.register("firecharge", castfirecharge);
		casthandler.register("charge", castcharge);
		casthandler.register("strike", caststrike);
		casthandler.register("bandage", castbandage);
		casthandler.register("beasts", castbeasts);
		casthandler.register("lightningstorm", castlightningstorm);
		casthandler.register("chainlightning", castchainlightning);
		casthandler.register("reflect", castreflect);
		casthandler.register("siphon", castsiphon);
		casthandler.register("taunt", casttaunt);
		casthandler.register("vanish", castvanish);
		casthandler.register("bomb", castbomb);
		casthandler.register("mount", castmount);
		casthandler.register("poison", castpoison);
		casthandler.register("bash", castbash);
		casthandler.register("mute", castmute);
		casthandler.register("defensivestance", castdefensivestance);

		casthandler.register("backstab", passivebackstab);
		casthandler.register("flameshield", passiveflameshield);

		chathandler.register("chat", chat);
		chathandler.register("titles", chattitles);
		chathandler.register("channel", chatchannel);

		partyhandler.register("party", partycmd);
		partyhandler.register("create", partycreate);
		partyhandler.register("members", partymembers);
		partyhandler.register("invite", partyinvite);
		partyhandler.register("remove", partyremove);
		partyhandler.register("accept", partyaccept);
		partyhandler.register("decline", partydecline);
		partyhandler.register("chat", partychat);
		partyhandler.register("leader", partyleader);
		partyhandler.register("leave", partyleave);
		partyhandler.register("disband", partydisband);

		getCommand("casters").setExecutor(castershandler);
		getCommand("wand").setExecutor(wandhandler);
		getCommand("cast").setExecutor(casthandler);
		getCommand("chat").setExecutor(chathandler);
		getCommand("party").setExecutor(partyhandler);
	}

	public static void registerEvents(Plugin plugin, Listener... listeners)
	{
		for (Listener listener : listeners)
		{
			Bukkit.getServer().getPluginManager().registerEvents(listener, plugin);
		}
	}

	@EventHandler
	public void onPlayerJoinEvent(PlayerJoinEvent event)
	{
		casters.put(event.getPlayer().getUniqueId(), new Caster(event.getPlayer()));
		event.setJoinMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_AQUA + "CasterCraft" + ChatColor.DARK_GRAY + "]"
				+ ChatColor.AQUA + " >> " + ChatColor.WHITE + event.getPlayer().getName() + ChatColor.GREEN
				+ " Has Joined The Server.");
	}

	@EventHandler
	public void onPlayerQuitEvent(PlayerQuitEvent event)
	{
		casters.get(event.getPlayer().getUniqueId()).setConfig();
		casters.remove(event.getPlayer().getUniqueId());
		event.getPlayer().leaveVehicle();
		event.setQuitMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_AQUA + "CasterCraft" + ChatColor.DARK_GRAY + "]"
				+ ChatColor.AQUA + " >> " + ChatColor.WHITE + event.getPlayer().getName() + ChatColor.RED
				+ " Has Left The Server.");
	}

	@EventHandler
	public void onPlayerKickEvent(PlayerKickEvent event)
	{
		casters.get(event.getPlayer().getUniqueId()).setConfig();
		casters.remove(event.getPlayer().getUniqueId());
		event.getPlayer().leaveVehicle();
		event.setLeaveMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_AQUA + "CasterCraft" + ChatColor.DARK_GRAY
				+ "]" + ChatColor.AQUA + " >> " + ChatColor.WHITE + event.getPlayer().getName() + ChatColor.GRAY
				+ " Was Kicked From The Server.");
	}

	@EventHandler
	public void onPlayerDeathEvent(PlayerDeathEvent event)
	{
		event.setDeathMessage(
				ChatColor.DARK_GRAY + "[" + ChatColor.RED + "Death" + ChatColor.DARK_GRAY + "]" + ChatColor.GRAY + " "
						+ WordUtils.capitalize(event.getDeathMessage().replaceFirst(event.getEntity().getName(),
						ChatColor.WHITE + event.getEntity().getName() + ChatColor.GRAY))
						+ ".");
	}
}
