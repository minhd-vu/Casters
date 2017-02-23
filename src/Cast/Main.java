package Cast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
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
import Cast.Casts.CastReflect;
import Cast.Casts.CastRevive;
import Cast.Casts.CastSiphon;
import Cast.Casts.CastStrike;
import Cast.Casts.CastTaunt;
import Cast.Casts.CastVanish;
import Cast.Casts.Casts;
import Cast.Configs.Config;
import Cast.Configs.ConfigManager;
import Cast.Essentials.Armor;
import Cast.Essentials.Attack;
import Cast.Essentials.Caster;
import Cast.Essentials.Enchant;
import Cast.Essentials.Experience;
import Cast.Essentials.Horses;
import Cast.Essentials.Chat.Chat;
import Cast.Essentials.Chat.ChatChannel;
import Cast.Essentials.Chat.ChatTitles;
import Cast.Party.Parties;
import Cast.Party.Party;
import Cast.Party.PartyAccept;
import Cast.Party.PartyCreate;
import Cast.Party.PartyDecline;
import Cast.Party.PartyInvite;
import Cast.Party.PartyMembers;
import Cast.Wands.WandDistorter;
import Cast.Wands.WandInferno;
import Cast.Wands.WandList;
import Cast.Wands.WandShaman;
import Cast.Wands.WandWarlock;
import Cast.Wands.Wands;

public class Main extends JavaPlugin implements Listener
{
	private static Main instance;

	private static ConfigManager manager;

	private static Config wands;
	private static Config casts;
	private static Config exp;
	private static Config types;
	private static Config races;
	private static Config jobs;
	private static Config chats;
	private static Config mobs;

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

	private static Wands wand;
	private static Casts cast;

	private static HashMap<String, String> spells;

	private static Experience experience;
	private static Enchant enchant;
	private static Armor armor;
	private static Attack attack;
	private static Horses horses;

	private static Chat chat;
	private static ChatTitles chattitles;
	private static ChatChannel chatchannel;

	private static WandList wandlist;
	private static WandInferno wandinferno;
	private static WandDistorter wanddistorter;
	private static WandShaman wandshaman;
	private static WandWarlock wandwarlock;

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

	private static List<Party> parties;

	private static Parties partycmd;
	private static PartyCreate partycreate;
	private static PartyMembers partymembers;
	private static PartyInvite partyinvite;
	private static PartyAccept partyaccept;
	private static PartyDecline partydecline;

	@Override
	public void onEnable()
	{
		instance = this;

		manager = new ConfigManager(this);

		exp = manager.getNewConfig("experience.yml", new String[] { "Experience Config File." });
		wands = manager.getNewConfig("wands.yml", new String[] { "Wands Config File." });
		casts = manager.getNewConfig("casts.yml", new String[] { "Casts Config File." });
		types = manager.getNewConfig("classes.yml", new String[] { "Classes Config File." });
		races = manager.getNewConfig("races.yml", new String[] { "Races Config File." });
		jobs = manager.getNewConfig("jobs.yml", new String[] { "Jobs Config File." });
		chats = manager.getNewConfig("chat.yml", new String[] { "Chat Config File." });
		mobs = manager.getNewConfig("mobs.yml", new String[] { "Mobs Config File." });

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

		wand = new Wands();
		cast = new Casts();

		spells = new HashMap<String, String>();
		spells.put("Fireball", "Casts A Fireball");
		spells.put("DarkBomb", "Casts A DarkBomb");
		spells.put("Bolt", "Casts A Bolt");
		spells.put("Revive", "Revive A Player");
		spells.put("FireBomb", "Casts A FireBomb");
		spells.put("FireCharge", "Casts A FireCharge");
		spells.put("Charge", "Charge Your Opponent");
		spells.put("Strike", "Strike You Opponent");
		spells.put("Bandage", "Bandage Yourself Or An Ally.");
		spells.put("Beasts", "Summon A Pack Of Wolves");
		spells.put("LightningStorm", "Cast A Lightning Storm");
		spells.put("ChainLightning", "Consecutively Strikes Opponents");
		spells.put("Reflect", "Reflects All Incoming Damage");
		spells.put("Backstab", "Attacks From The Back Deal More.");
		spells.put("Siphon", "Siphon Health From You Opponent");
		spells.put("Taunt", "Taunt All Nearby Opponents");
		spells.put("Vanish", "Vanish In A Cloud of Smoke");
		spells.put("Bomb", "Casts A Bomb");

		experience = new Experience();
		enchant = new Enchant();
		armor = new Armor();
		attack = new Attack();
		horses = new Horses();

		chat = new Chat();
		chattitles = new ChatTitles();
		chatchannel = new ChatChannel();

		wandlist = new WandList();
		wandinferno = new WandInferno("Inferno");
		wanddistorter = new WandDistorter("Distorter");
		wandshaman = new WandShaman("Shaman");
		wandwarlock = new WandWarlock("Warlock");

		castlist = new CastList();
		castfireball = new CastFireball("Fireball");
		castdarkbomb = new CastDarkBomb("DarkBomb");
		castbolt = new CastBolt("Bolt");
		castrevive = new CastRevive("Revive");
		castfirebomb = new CastFireBomb("FireBomb");
		castfirecharge = new CastFireCharge("FireCharge");
		castcharge = new CastCharge("Charge");
		caststrike = new CastStrike("Strike");
		castbandage = new CastBandage("Bandage");
		castbeasts = new CastBeasts("Beasts");
		castlightningstorm = new CastLightningStorm("LightningStorm");
		castchainlightning = new CastChainLightning("ChainLightning");
		castreflect = new CastReflect("Reflect");
		castbackstab = new CastBackstab("Backstab");
		castsiphon = new CastSiphon("Siphon");
		casttaunt = new CastTaunt("Taunt");
		castvanish = new CastVanish("Vanish");
		castbomb = new CastBomb("Bomb");

		parties = new ArrayList<Party>();
		partycmd = new Parties();
		partycreate = new PartyCreate();
		partymembers = new PartyMembers();
		partyinvite = new PartyInvite();
		partyaccept = new PartyAccept();
		partydecline = new PartyDecline();

		registerCommands();

		registerEvents(this, this, experience, enchant, armor, attack, horses, chat, wandinferno, wanddistorter, wandshaman, wandwarlock, castfireball, castdarkbomb, castbolt, castrevive, castfirebomb, castfirecharge, castcharge, caststrike, castbandage, castbeasts, castlightningstorm, castchainlightning, castreflect, castbackstab, castsiphon, castvanish, castbomb);

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
			player.kickPlayer(this.getName() + " Is Restarting!");
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

		chathandler.register("chat", chat);
		chathandler.register("titles", chattitles);
		chathandler.register("channel", chatchannel);

		partyhandler.register("party", partycmd);
		partyhandler.register("create", partycreate);
		partyhandler.register("members", partymembers);
		partyhandler.register("invite", partyinvite);
		partyhandler.register("accept", partyaccept);
		partyhandler.register("decline", partydecline);

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
	}

	@EventHandler
	public void onPlayerQuitEvent(PlayerQuitEvent event)
	{
		casters.get(event.getPlayer().getUniqueId()).setConfig();
		casters.remove(event.getPlayer().getUniqueId());
	}

	@EventHandler
	public void onPlayerKickEvent(PlayerKickEvent event)
	{
		casters.get(event.getPlayer().getUniqueId()).setConfig();
		casters.remove(event.getPlayer().getUniqueId());
	}

	public static Main getInstance()
	{
		return instance;
	}

	public static HashMap<UUID, Caster> getCasters()
	{
		return casters;
	}

	public static HashMap<String, String> getCasts()
	{
		return spells;
	}

	public static ConfigManager getConfigManager()
	{
		return manager;
	}

	public static Config getConfigWands()
	{
		return wands;
	}

	public static Config getConfigCasts()
	{
		return casts;
	}

	public static Config getConfigType()
	{
		return types;
	}

	public static Config getConfigRace()
	{
		return races;
	}

	public static Config getConfigJob()
	{
		return jobs;
	}

	public static Config getConfigExp()
	{
		return exp;
	}

	public static Config getConfigMobs()
	{
		return mobs;
	}

	public static Config getConfigChats()
	{
		return chats;
	}

	public static CastFireball getCastFireball()
	{
		return castfireball;
	}

	public static CastDarkBomb getCastDarkBomb()
	{
		return castdarkbomb;
	}

	public static CastBolt getCastBolt()
	{
		return castbolt;
	}

	public static CastRevive getCastRevive()
	{
		return castrevive;
	}

	public static CastFireBomb getCastFireBomb()
	{
		return castfirebomb;
	}

	public static CastFireCharge getCastFireCharge()
	{
		return castfirecharge;
	}

	public static CastCharge getCastCharge()
	{
		return castcharge;
	}

	public static CastStrike getCastStrike()
	{
		return caststrike;
	}

	public static CastBandage getCastBandage()
	{
		return castbandage;
	}

	public static CastBeasts getCastBeasts()
	{
		return castbeasts;
	}

	public static CastLightningStorm getCastLightningStorm()
	{
		return castlightningstorm;
	}

	public static CastChainLightning getCastChainLightning()
	{
		return castchainlightning;
	}

	public static CastReflect getCastReflect()
	{
		return castreflect;
	}

	public static CastBackstab getCastBackstab()
	{
		return castbackstab;
	}

	public static CastSiphon getCastSiphon()
	{
		return castsiphon;
	}

	public static CastTaunt getCastTaunt()
	{
		return casttaunt;
	}

	public static CastVanish getCastVanish()
	{
		return castvanish;
	}

	public static CastBomb getCastBomb()
	{
		return castbomb;
	}

	public static List<Party> getParties()
	{
		return parties;
	}
}
