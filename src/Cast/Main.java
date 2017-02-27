package Cast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import Cast.Casters.Casters;
import Cast.Casters.CastersArmor;
import Cast.Casters.CastersChoose;
import Cast.Casters.CastersClasses;
import Cast.Casters.CastersInfo;
import Cast.Casters.CastersJobs;
import Cast.Casters.CastersLevel;
import Cast.Casters.CastersRaces;
import Cast.Casters.CastersRecipes;
import Cast.Casters.CastersStats;
import Cast.Casters.CastersWeapon;
import Cast.Casters.CastersWhoIs;
import Cast.Casts.CastBackstab;
import Cast.Casts.CastBandage;
import Cast.Casts.CastBeasts;
import Cast.Casts.CastBolt;
import Cast.Casts.CastBomb;
import Cast.Casts.CastChainLightning;
import Cast.Casts.CastCharge;
import Cast.Casts.CastDarkBomb;
import Cast.Casts.CastFireBomb;
import Cast.Casts.CastFireCharge;
import Cast.Casts.CastFireball;
import Cast.Casts.CastLightningStorm;
import Cast.Casts.CastList;
import Cast.Casts.CastMount;
import Cast.Casts.CastReflect;
import Cast.Casts.CastRevive;
import Cast.Casts.CastSiphon;
import Cast.Casts.CastStrike;
import Cast.Casts.CastTaunt;
import Cast.Casts.CastVanish;
import Cast.Casts.Casts;
import Cast.Casts.Passives.PassiveBackstab;
import Cast.Casts.Types.Cast;
import Cast.Configs.ConfigManager;
import Cast.Essentials.Armor;
import Cast.Essentials.Attack;
import Cast.Essentials.Caster;
import Cast.Essentials.Class;
import Cast.Essentials.Enchant;
import Cast.Essentials.Experience;
import Cast.Essentials.Mob;
import Cast.Essentials.Chat.Chat;
import Cast.Essentials.Chat.ChatChannel;
import Cast.Essentials.Chat.ChatTitles;
import Cast.Party.Parties;
import Cast.Party.Party;
import Cast.Party.PartyAccept;
import Cast.Party.PartyChat;
import Cast.Party.PartyCreate;
import Cast.Party.PartyDecline;
import Cast.Party.PartyDisband;
import Cast.Party.PartyInvite;
import Cast.Party.PartyLeader;
import Cast.Party.PartyLeave;
import Cast.Party.PartyMembers;
import Cast.Wands.WandDistorter;
import Cast.Wands.WandInferno;
import Cast.Wands.WandList;
import Cast.Wands.WandShaman;
import Cast.Wands.WandWarlock;
import Cast.Wands.Wands;
import net.md_5.bungee.api.ChatColor;

public class Main extends JavaPlugin implements Listener
{
	private static Main instance;

	private static ConfigManager manager;

	private static List<Class> classes;
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
	private static CastBackstab castbackstab;
	private static CastSiphon castsiphon;
	private static CastTaunt casttaunt;
	private static CastVanish castvanish;
	private static CastBomb castbomb;
	private static CastMount castmount;

	private static PassiveBackstab passivebackstab;

	private static List<Party> parties;

	private static Parties partycmd;
	private static PartyCreate partycreate;
	private static PartyMembers partymembers;
	private static PartyInvite partyinvite;
	private static PartyAccept partyaccept;
	private static PartyDecline partydecline;
	private static PartyChat partychat;
	private static PartyLeader partyleader;
	private static PartyLeave partyleave;
	private static PartyDisband partydisband;

	@Override
	public void onEnable()
	{
		instance = this;

		manager = new ConfigManager(this);

		classes = new ArrayList<Class>();

		Class paladin = new Class("Paladin", "Description", 2, 5, -2, -1, 3);
		paladin.getArmor().add(Material.DIAMOND_HELMET);
		paladin.getArmor().add(Material.DIAMOND_CHESTPLATE);
		paladin.getArmor().add(Material.DIAMOND_LEGGINGS);
		paladin.getArmor().add(Material.DIAMOND_BOOTS);
		paladin.getWeapon().put(Material.DIAMOND_SWORD, 10);
		paladin.getWeapon().put(Material.IRON_SWORD, 8);
		paladin.getWeapon().put(Material.GOLD_SWORD, 7);
		paladin.getWeapon().put(Material.STONE_SWORD, 6);
		paladin.getWeapon().put(Material.WOOD_SWORD, 5);
		paladin.getCasts().put("Reflect", 1);

		Class cavalier = new Class("Cavalier", "Description", 3, 3, 4, 2, -1);
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

		Class barbarian = new Class("Barbarian", "Description", 5, 3, 2, -4, -3);
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

		Class blackguard = new Class("Blackguard", "Description", 3, 4, 2, 2, -4);
		blackguard.getArmor().add(Material.CHAINMAIL_HELMET);
		blackguard.getArmor().add(Material.IRON_CHESTPLATE);
		blackguard.getArmor().add(Material.IRON_LEGGINGS);
		blackguard.getArmor().add(Material.CHAINMAIL_BOOTS);
		blackguard.getWeapon().put(Material.DIAMOND_SWORD, 10);
		blackguard.getWeapon().put(Material.IRON_SWORD, 8);
		blackguard.getWeapon().put(Material.GOLD_SWORD, 7);
		blackguard.getWeapon().put(Material.STONE_SWORD, 6);
		blackguard.getWeapon().put(Material.WOOD_SWORD, 5);

		Class assassin = new Class("Assassin", "Description", 4, -2, 5, -3, 1);
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

		Class duelist = new Class("Duelist", "Description", 3, 1, 4, -2, -2);
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

		Class fletcher = new Class("Fletcher", "Description", 2, 1, 4, -2, -2);
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

		Class musketeer = new Class("Musketeer", "Description", 1, 2, 3, 1, 1);
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

		Class distorter = new Class("Distorter", "Description", -3, 1, 2, 4, 5);
		distorter.getArmor().add(Material.LEATHER_HELMET);
		distorter.getArmor().add(Material.LEATHER_CHESTPLATE);
		distorter.getArmor().add(Material.LEATHER_LEGGINGS);
		distorter.getArmor().add(Material.DIAMOND_BOOTS);
		distorter.getWeapon().put(Material.DIAMOND_HOE, 5);
		distorter.getWeapon().put(Material.IRON_HOE, 5);
		distorter.getWeapon().put(Material.GOLD_HOE, 5);
		distorter.getWeapon().put(Material.STONE_HOE, 5);
		distorter.getWeapon().put(Material.WOOD_HOE, 5);

		Class inferno = new Class("Inferno", "Description", 0, -2, 1, 5, 4);
		inferno.getArmor().add(Material.LEATHER_HELMET);
		inferno.getArmor().add(Material.LEATHER_CHESTPLATE);
		inferno.getArmor().add(Material.LEATHER_LEGGINGS);
		inferno.getArmor().add(Material.LEATHER_BOOTS);
		cavalier.getWeapon().put(Material.DIAMOND_HOE, 5);
		cavalier.getWeapon().put(Material.IRON_HOE, 5);
		cavalier.getWeapon().put(Material.GOLD_HOE, 5);
		cavalier.getWeapon().put(Material.STONE_HOE, 5);
		cavalier.getWeapon().put(Material.WOOD_HOE, 5);
		inferno.getCasts().put("Fireball", 1);

		Class shaman = new Class("Shaman", "Description", 3, 1, 3, 3, 2);
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
		shaman.getCasts().put("ChainLightning", 5);

		Class warlock = new Class("Warlock", "Description", -2, 4, -2, 4, 4);
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
		warlock.getCasts().put("Siphon", 5);

		Class oracle = new Class("Oracle", "Description", 0, 1, 1, 3, 4);
		oracle.getArmor().add(Material.LEATHER_HELMET);
		oracle.getArmor().add(Material.LEATHER_CHESTPLATE);
		oracle.getArmor().add(Material.IRON_LEGGINGS);
		oracle.getArmor().add(Material.LEATHER_BOOTS);
		oracle.getWeapon().put(Material.DIAMOND_SPADE, 10);
		oracle.getWeapon().put(Material.IRON_SPADE, 7);
		oracle.getWeapon().put(Material.GOLD_SPADE, 6);
		oracle.getWeapon().put(Material.STONE_SPADE, 5);
		oracle.getWeapon().put(Material.WOOD_SPADE, 4);

		Class bloodmage = new Class("Bloodmage", "Description", -4, 4, -3, 5, 3);
		bloodmage.getArmor().add(Material.LEATHER_HELMET);
		bloodmage.getArmor().add(Material.LEATHER_CHESTPLATE);
		bloodmage.getArmor().add(Material.IRON_LEGGINGS);
		bloodmage.getArmor().add(Material.LEATHER_BOOTS);
		bloodmage.getWeapon().put(Material.DIAMOND_SPADE, 10);
		bloodmage.getWeapon().put(Material.IRON_SPADE, 7);
		bloodmage.getWeapon().put(Material.GOLD_SPADE, 6);
		bloodmage.getWeapon().put(Material.STONE_SPADE, 5);
		bloodmage.getWeapon().put(Material.WOOD_SPADE, 4);

		Class monk = new Class("Monk", "Description", 2, 3, 3, 2, 3);
		monk.getArmor().add(Material.LEATHER_HELMET);
		monk.getArmor().add(Material.LEATHER_CHESTPLATE);
		monk.getArmor().add(Material.IRON_LEGGINGS);
		monk.getArmor().add(Material.LEATHER_BOOTS);
		monk.getWeapon().put(Material.DIAMOND_SPADE, 10);
		monk.getWeapon().put(Material.IRON_SPADE, 7);
		monk.getWeapon().put(Material.GOLD_SPADE, 6);
		monk.getWeapon().put(Material.STONE_SPADE, 5);
		monk.getWeapon().put(Material.WOOD_SPADE, 4);

		Class cleric = new Class("Cleric", "Description", 2, 3, 3, 2, 3);
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

		Class dwarf = new Class("Dwarf", "Description", 3, 4, 0, -3, -2);
		dwarf.getArmor().add(Material.DIAMOND_HELMET);
		dwarf.getWeapon().put(Material.DIAMOND_PICKAXE, 5);

		Class human = new Class("Human", "Description", 2, 1, 1, 2, 1);
		human.getCasts().put("Bandage", 1);

		Class elf = new Class("Elf", "Description", -3, -2, 3, 4, 3);
		elf.getArmor().add(Material.GOLD_HELMET);
		elf.getWeapon().put(Material.BOW, 3);

		Class troll = new Class("Troll", "Description", 4, 5, -4, -4, -2);
		troll.getArmor().add(Material.IRON_LEGGINGS);

		Class goblin = new Class("Goblin", "Description", 1, 2, 4, -2, 0);
		goblin.getArmor().add(Material.LEATHER_CHESTPLATE);

		Class giant = new Class("Giant", "Description", 2, 5, -5, -3, 2);
		giant.getArmor().add(Material.IRON_CHESTPLATE);

		Class undead = new Class("Undead", "Description", 0, 3, -2, 3, 2);
		undead.getArmor().add(Material.GOLD_HELMET);

		Class demon = new Class("Demon", "Description", 5, -2, 2, 4, -5);
		demon.getArmor().add(Material.GOLD_HELMET);

		Class alchemist = new Class("Undead", "Description", 0, 3, -2, 3, 2);
		alchemist.getArmor().add(Material.GOLD_HELMET);

		Class enchanter = new Class("Undead", "Description", 0, 3, -2, 3, 2);
		enchanter.getArmor().add(Material.GOLD_HELMET);

		Class blacksmith = new Class("Undead", "Description", 0, 3, -2, 3, 2);
		blacksmith.getArmor().add(Material.GOLD_HELMET);

		Class engineer = new Class("Undead", "Description", 0, 3, -2, 3, 2);
		engineer.getArmor().add(Material.GOLD_HELMET);

		Class artisan = new Class("Undead", "Description", 0, 3, -2, 3, 2);
		artisan.getArmor().add(Material.GOLD_HELMET);

		Class farmer = new Class("Undead", "Description", 0, 3, -2, 3, 2);
		farmer.getArmor().add(Material.GOLD_HELMET);

		Class miner = new Class("Undead", "Description", 0, 3, -2, 3, 2);
		miner.getArmor().add(Material.GOLD_HELMET);

		classes.add(paladin);
		classes.add(cavalier);
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

		classes.add(dwarf);
		classes.add(human);
		classes.add(elf);
		classes.add(troll);
		classes.add(goblin);
		classes.add(giant);
		classes.add(demon);
		classes.add(undead);

		classes.add(alchemist);
		classes.add(enchanter);
		classes.add(blacksmith);
		classes.add(engineer);
		classes.add(artisan);
		classes.add(farmer);
		classes.add(miner);

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
		casts.put("Beasts", castbeasts = new CastBeasts("Beasts", "Summon A Pack Of Wolves."));
		casts.put("LightningStorm",
				castlightningstorm = new CastLightningStorm("LightningStorm", "Cast A Lightning Storm"));
		casts.put("ChainLightning",
				castchainlightning = new CastChainLightning("ChainLightning", "Consecutively Strikes Opponenets"));
		casts.put("Reflect", castreflect = new CastReflect("Reflect", "Relects All Incoming Damage"));
		casts.put("Backstab", castbackstab = new CastBackstab("Backstab", "Attacks From The Back Deal More."));
		casts.put("Siphon", castsiphon = new CastSiphon("Siphon", "Siphons Health From Your Opponent"));
		casts.put("Taunt", casttaunt = new CastTaunt("Taunt", "Taunt All Nearby Opponents."));
		casts.put("Vanish", castvanish = new CastVanish("Vanish", "Vanish In A Cloud Of Smoke."));
		casts.put("Bomb", castbomb = new CastBomb("Bomb", " Places A Explosive Device"));
		casts.put("Mount", castmount = new CastMount("Mount", "Mounts Onto A Horse"));

		// passivebackstab = new PassiveBackstab("Backstab");

		experience = new Experience();
		enchant = new Enchant();
		armor = new Armor();
		attack = new Attack();

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
		partyaccept = new PartyAccept();
		partydecline = new PartyDecline();
		partychat = new PartyChat();
		partyleader = new PartyLeader();
		partyleave = new PartyLeave();
		partydisband = new PartyDisband();

		registerCommands();

		registerEvents(this, this, experience, enchant, armor, attack, chat, wandinferno, wanddistorter, wandshaman,
				wandwarlock, castfireball, castdarkbomb, castbolt, castrevive, castfirebomb, castfirecharge, castcharge,
				caststrike, castbandage, castbeasts, castlightningstorm, castchainlightning, castreflect, castbackstab,
				castsiphon, castvanish, castbomb, castmount);

		/*-
		ScoreboardManager scoreboardmanager = Bukkit.getScoreboardManager();
		Scoreboard scoreboard = scoreboardmanager.getNewScoreboard();
		
		Objective objective = scoreboard.registerNewObjective("showhealth", "health");
		objective.setDisplaySlot(DisplaySlot.BELOW_NAME);
		
		for (Player player : Bukkit.getOnlinePlayers())
		{
			objective.setDisplayName(ChatColor.DARK_GRAY + "/" + ChatColor.RED + " " + (int) player.getMaxHealth());
			player.setScoreboard(scoreboard);
			player.setHealth(player.getHealth());
		}
		*/

		ScoreboardManager scoreboardmanager = Bukkit.getScoreboardManager();
		Scoreboard scoreboard = scoreboardmanager.getNewScoreboard();

		Objective objective = scoreboard.registerNewObjective("showhealth", "health");
		objective.setDisplaySlot(DisplaySlot.BELOW_NAME);
		objective.setDisplayName("Health:");

		for (Player player : Bukkit.getOnlinePlayers())
		{
			Score score = objective.getScore(player.getName());
			score.setScore((int) player.getHealth());
		}

		for (Player player : Bukkit.getOnlinePlayers())
		{
			player.setScoreboard(scoreboard);
		}
	}

	@Override
	public void onDisable()
	{
		for (Player player : Bukkit.getOnlinePlayers())
		{
			player.kickPlayer("Server Is Restarting!");
		}
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
		casthandler.register("backstab", castbackstab);
		casthandler.register("siphon", castsiphon);
		casthandler.register("taunt", casttaunt);
		casthandler.register("vanish", castvanish);
		casthandler.register("bomb", castbomb);
		casthandler.register("mount", castmount);

		chathandler.register("chat", chat);
		chathandler.register("titles", chattitles);
		chathandler.register("channel", chatchannel);

		partyhandler.register("party", partycmd);
		partyhandler.register("create", partycreate);
		partyhandler.register("members", partymembers);
		partyhandler.register("invite", partyinvite);
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
		event.setJoinMessage(
				ChatColor.WHITE + event.getPlayer().getName() + ChatColor.GRAY + " Has Joined The Server.");
	}

	@EventHandler
	public void onPlayerQuitEvent(PlayerQuitEvent event)
	{
		casters.get(event.getPlayer().getUniqueId()).setConfig();
		casters.remove(event.getPlayer().getUniqueId());
		event.getPlayer().leaveVehicle();
		event.setQuitMessage(ChatColor.WHITE + event.getPlayer().getName() + ChatColor.GRAY + " Has Left The Server.");
	}

	@EventHandler
	public void onPlayerKickEvent(PlayerKickEvent event)
	{
		casters.get(event.getPlayer().getUniqueId()).setConfig();
		casters.remove(event.getPlayer().getUniqueId());
		event.getPlayer().leaveVehicle();
		event.setLeaveMessage(ChatColor.WHITE + event.getPlayer().getName() + ChatColor.GRAY + " Has Left The Server.");
	}

	public static Main getInstance()
	{
		return instance;
	}

	public static List<Class> getClasses()
	{
		return classes;
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
}
