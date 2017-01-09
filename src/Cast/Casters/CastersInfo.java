package Cast.Casters;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import Cast.CommandInterface;
import Cast.Main;

public class CastersInfo implements CommandInterface
{
	private String header = ChatColor.DARK_GRAY + "[" + ChatColor.DARK_AQUA + "Casters Info" + ChatColor.DARK_GRAY
			+ "] ";
	private String fill = "--------------------";
	private String bar = "-----------------------------------------------------";

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if (sender instanceof Player)
		{
			Player player = (Player) sender;

			if (args.length != 2)
			{
				return false;
			}

			boolean type = false;
			boolean race = false;
			boolean job = false;

			String name = "";
			String description = "";

			if (args[1].equalsIgnoreCase("Guardian"))
			{
				name = "Guardian";
				description = "Protector Of All";
				type = true;
			}

			else if (args[1].equalsIgnoreCase("Cavalier"))
			{
				name = "Cavalier";
				description = "Rider Of Horses";
				type = true;
			}

			else if (args[1].equalsIgnoreCase("Barbarian"))
			{
				name = "Barbarian";
				description = "Swinger Of Axes";
				type = true;
			}

			else if (args[1].equalsIgnoreCase("Blackguard"))
			{
				name = "Blackguard";
				description = "Bringer Of Darkness";
				type = true;
			}

			else if (args[1].equalsIgnoreCase("Assassin"))
			{
				name = "Assassin";
				description = "Dealer Of Damage";
				type = true;
			}

			else if (args[1].equalsIgnoreCase("Duelist"))
			{
				name = "Duelist";
				description = "Wielder Of Weapons";
				type = true;
			}

			else if (args[1].equalsIgnoreCase("Fletcher"))
			{
				name = "Fletcher";
				description = "Shooter Of Arrows";
				type = true;
			}

			else if (args[1].equalsIgnoreCase("Musketeer"))
			{
				name = "Musketeer";
				description = "Firer Of Firearms";
				type = true;
			}

			else if (args[1].equalsIgnoreCase("Distorter"))
			{
				name = "Distorter";
				description = "Manipulator Of Reality";
				type = true;
			}

			else if (args[1].equalsIgnoreCase("Inferno"))
			{
				name = "Inferno";
				description = "Master Of Fire";
				type = true;
			}

			else if (args[1].equalsIgnoreCase("Shaman"))
			{
				name = "Shaman";
				description = "Influencer Of Dark Spirits";
				type = true;
			}

			else if (args[1].equalsIgnoreCase("Warlock"))
			{
				name = "Warlock";
				description = "Conductor Of Spirits";
				type = true;
			}

			else if (args[1].equalsIgnoreCase("Oracle"))
			{
				name = "Oracle";
				description = "Predictor Of Future";
				type = true;
			}

			else if (args[1].equalsIgnoreCase("Bloodmage"))
			{
				name = "Bloodmage";
				description = "Controller Of Blood";
				type = true;
			}

			else if (args[1].equalsIgnoreCase("Monk"))
			{
				name = "Monk";
				description = "Fighter Of Values";
				type = true;
			}

			else if (args[1].equalsIgnoreCase("Templar"))
			{
				name = "Templar";
				description = "Doer Of Holy Works";
				type = true;
			}

			else if (args[1].equalsIgnoreCase("Dwarf"))
			{
				name = "Dwarf";
				description = "Drinker Of Drinks";
				race = true;
			}

			else if (args[1].equalsIgnoreCase("Human"))
			{
				name = "Human";
				description = "Becommer Of Basic";
				race = true;
			}

			else if (args[1].equalsIgnoreCase("Elf"))
			{
				name = "Elf";
				description = "Grower Of Ears";
				race = true;
			}

			else if (args[1].equalsIgnoreCase("Troll"))
			{
				name = "Troll";
				description = "Needer Of Braces";
				race = true;
			}

			else if (args[1].equalsIgnoreCase("Goblin"))
			{
				name = "Goblin";
				description = "Nicer Of Skin Tone";
				race = true;
			}

			else if (args[1].equalsIgnoreCase("Orc"))
			{
				name = "Orc";
				description = "Bearer Of Muscles";
				race = true;
			}

			else if (args[1].equalsIgnoreCase("Giant"))
			{
				name = "Giant";
				description = "Much Tall Of Very Large";
				race = true;
			}

			else if (args[1].equalsIgnoreCase("Alchemist"))
			{
				name = "Alchemist";
				description = "Brewer Of Potions";
				job = true;
			}

			else if (args[1].equalsIgnoreCase("Enchanter"))
			{
				name = "Enchanter";
				description = "Enchanter Of Armors/Tools";
				job = true;
			}

			else if (args[1].equalsIgnoreCase("Blacksmith"))
			{
				name = "Blacksmith";
				description = "Forger Of Armors/Tools";
				job = true;
			}

			else if (args[1].equalsIgnoreCase("Engineer"))
			{
				name = "Engineer";
				description = "Developer Of Redstone";
				job = true;
			}

			else if (args[1].equalsIgnoreCase("Artisan"))
			{
				name = "Artisan";
				description = "Crafter Of Crafts";
				job = true;
			}

			else if (args[1].equalsIgnoreCase("Farmer"))
			{
				name = "Farmer";
				description = "Cultivator Of Crops";
				job = true;
			}

			else if (args[1].equalsIgnoreCase("Miner"))
			{
				name = "Miner";
				description = "Breaker Of Ores";
				job = true;
			}

			else
			{
				player.sendMessage(header + ChatColor.GRAY + "That Class/Race/Job Does Not Exist!");
				return false;
			}

			if (type)
			{
				player.sendMessage("\n" + ChatColor.DARK_GRAY + fill + "-" + header.substring(0, header.length() - 1)
						+ fill + "\n" + ChatColor.DARK_AQUA + name + ChatColor.GRAY + " - " + description + "."
						+ ChatColor.DARK_AQUA + "\nBase Stats:" + ChatColor.DARK_AQUA + "\nStrength: " + ChatColor.GRAY
						+ Main.getConfigType().getInt(name + ".Strength") + ChatColor.DARK_AQUA + "\nConstitution: "
						+ ChatColor.GRAY + Main.getConfigType().getInt(name + ".Constitution") + ChatColor.DARK_AQUA
						+ "\nDexterity: " + ChatColor.GRAY + Main.getConfigType().getInt(name + ".Dexterity")
						+ ChatColor.DARK_AQUA + "\nIntellect: " + ChatColor.GRAY
						+ Main.getConfigType().getInt(name + ".Intellect") + ChatColor.DARK_AQUA + "\nWisdom: "
						+ ChatColor.GRAY + Main.getConfigType().getInt(name + ".Wisdom") + "\n" + ChatColor.DARK_GRAY
						+ bar);
			}

			else if (race)
			{
				player.sendMessage("\n" + ChatColor.DARK_GRAY + fill + "-" + header.substring(0, header.length() - 1)
						+ fill + "\n" + ChatColor.DARK_AQUA + name + ChatColor.GRAY + " - " + description + "."
						+ ChatColor.DARK_AQUA + "\nBonuses:" + ChatColor.DARK_AQUA + "\nStrength: " + ChatColor.GRAY
						+ Main.getConfigRace().getInt(name + ".Strength") + ChatColor.DARK_AQUA + "\nConstitution: "
						+ ChatColor.GRAY + Main.getConfigRace().getInt(name + ".Constitution") + ChatColor.DARK_AQUA
						+ "\nDexterity: " + ChatColor.GRAY + Main.getConfigRace().getInt(name + ".Dexterity")
						+ ChatColor.DARK_AQUA + "\nIntellect: " + ChatColor.GRAY
						+ Main.getConfigRace().getInt(name + ".Intellect") + ChatColor.DARK_AQUA + "\nWisdom: "
						+ ChatColor.GRAY + Main.getConfigRace().getInt(name + ".Wisdom") + "\n" + ChatColor.DARK_GRAY
						+ bar);
			}

			else if (job)
			{
				player.sendMessage("\n" + ChatColor.DARK_GRAY + fill + "-" + header.substring(0, header.length() - 1)
						+ fill + "\n" + ChatColor.DARK_AQUA + name + ChatColor.GRAY + " - " + description + "."
						+ ChatColor.DARK_AQUA + "\nStrength: " + ChatColor.GRAY
						+ Main.getConfigJob().getInt(name + ".Strength") + ChatColor.DARK_AQUA + "\nConstitution: "
						+ ChatColor.GRAY + Main.getConfigJob().getInt(name + ".Constitution") + ChatColor.DARK_AQUA
						+ "\nDexterity: " + ChatColor.GRAY + Main.getConfigJob().getInt(name + ".Dexterity")
						+ ChatColor.DARK_AQUA + "\nIntellect: " + ChatColor.GRAY
						+ Main.getConfigJob().getInt(name + ".Intellect") + ChatColor.DARK_AQUA + "\nWisdom: "
						+ ChatColor.GRAY + Main.getConfigJob().getInt(name + ".Wisdom") + "\n" + ChatColor.DARK_GRAY
						+ bar);
			}
		}

		return true;
	}

}
