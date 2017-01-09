package Cast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
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
import Cast.Guild.Guild;
import Cast.Guild.GuildCreate;
import Cast.Guild.GuildInvite;
import Cast.Guild.GuildJoin;
import Cast.Guild.GuildRemove;
import Cast.Guild.Guilds;
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
	private static Config guilds;

	private static HashMap<UUID, Caster> casters = new HashMap<UUID, Caster>();

	private static Casters casterscmd = new Casters();
	private static CastersInfo castersinfo = new CastersInfo();
	private static CastersLevel casterslevel = new CastersLevel();
	private static CastersStats castersstats = new CastersStats();
	private static CastersChoose casterschoose = new CastersChoose();
	private static CastersClasses castersclasses = new CastersClasses();
	private static CastersRaces castersraces = new CastersRaces();
	private static CastersJobs castersjobs = new CastersJobs();
	private static CastersArmor castersarmor = new CastersArmor();
	private static CastersWeapon castersweapon = new CastersWeapon();
	private static CastersWhoIs casterswhois = new CastersWhoIs();

	private static List<Guild> guildlist = new ArrayList<Guild>();
	private static Guilds guild = new Guilds();
	private static GuildCreate guildcreate = new GuildCreate();
	private static GuildInvite guildinvite = new GuildInvite();
	private static GuildJoin guildjoin = new GuildJoin();
	private static GuildRemove guildremove = new GuildRemove();

	private static Wands wand = new Wands();
	private static Casts cast = new Casts();
	
	private static HashMap<String, String> spells = new HashMap<String, String>();

	private static Experience experience;
	private static Enchant enchant = new Enchant();
	private static Armor armor = new Armor();
	private static Attack attack = new Attack();
	private static Horses horses = new Horses();

	private static Chat chat;
	private static ChatTitles chattitles;
	private static ChatChannel chatchannel = new ChatChannel();

	private static WandList wandlist = new WandList();

	private static WandInferno wandinferno;
	private static WandDistorter wanddistorter;
	private static WandShaman wandshaman;
	private static WandWarlock wandwarlock;

	private static CastList castlist = new CastList();

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

	@SuppressWarnings("unchecked")
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
		guilds = manager.getNewConfig("guilds.yml", new String[] { "Guilds Config File." });
		
		for (String name : (List<String>) guilds.getList("Guilds"))
		{
			Guild guild = new Guild(name);
			guild.setAbreviation(guilds.getString(name + ".Abreviation"));
			guild.setDescription(guilds.getString(name + ".Description"));
			
			for (OfflinePlayer offlineplayer : Bukkit.getOfflinePlayers())
			{
				if (offlineplayer.getName().equals(guilds.getString(name + ".Member")))
				{
					guild.getMembers().add(offlineplayer.getUniqueId());
				}
				
				else if (offlineplayer.getName().equals(guilds.getString(name + ".Officer")))
				{
					guild.getMembers().add(offlineplayer.getUniqueId());
				}
				
				else if (offlineplayer.getName().equals(guilds.getString(name + ".Leader")))
				{
					guild.getMembers().add(offlineplayer.getUniqueId());
				}
			}
		}

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

		chat = new Chat();

		wandinferno = new WandInferno("Inferno");
		wanddistorter = new WandDistorter("Distorter");
		wandshaman = new WandShaman("Shaman");
		wandwarlock = new WandWarlock("Warlock");

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

		registerCommands();

		registerEvents(this, this, experience, enchant, armor, attack, horses, chat, wandinferno, wanddistorter,
				wandshaman, wandwarlock, castfireball, castdarkbomb, castbolt, castrevive, castfirebomb, castfirecharge,
				castcharge, caststrike, castbandage, castbeasts, castlightningstorm, castchainlightning, castreflect,
				castbackstab, castsiphon, castvanish, castbomb);

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
		CommandHandler guildhandler = new CommandHandler();
		CommandHandler wandhandler = new CommandHandler();
		CommandHandler casthandler = new CommandHandler();
		CommandHandler chathandler = new CommandHandler();

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

		guildhandler.register("guild", guild);
		guildhandler.register("create", guildcreate);
		guildhandler.register("invite", guildinvite);
		guildhandler.register("join", guildjoin);
		guildhandler.register("remove", guildremove);

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

		getCommand("casters").setExecutor(castershandler);
		getCommand("guild").setExecutor(guildhandler);
		getCommand("wand").setExecutor(wandhandler);
		getCommand("cast").setExecutor(casthandler);
		getCommand("chat").setExecutor(chathandler);
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

	public static List<Guild> getGuilds()
	{
		return guildlist;
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
	
	public static Config getConfigGuilds()
	{
		return guilds;
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
}
