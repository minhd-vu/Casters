package Cast.Essentials;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Creature;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerLevelChangeEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scheduler.BukkitRunnable;

import Cast.Main;

public class Experience implements Listener
{
	private float explossgeneral;

	private float expgainplayer;
	private float explossplayer;
	private float expgaincreature;
	private float explosscreature;

	private float expgainbreak;
	private float expgainmine;
	private float expgaincraft;
	private float expgainfarm;

	private double scale;

	private String header = ChatColor.DARK_GRAY + "[" + ChatColor.DARK_AQUA + "Casters" + ChatColor.DARK_GRAY + "]"
			+ ChatColor.WHITE + " ";

	public Experience()
	{
		explossgeneral = 10;

		expgainplayer = 50;
		explossplayer = 25;
		expgaincreature = 10;
		explosscreature = 10;

		expgainbreak = 1;
		expgainmine = 10;
		expgaincraft = 5;
		expgainfarm = 5;

		scale = 1;
	}

	@EventHandler
	public void onPlayerLevelChangeEvent(PlayerLevelChangeEvent event)
	{
		Caster caster = Main.getCasters().get(event.getPlayer().getUniqueId());

		if (!(caster.getTypeLevel() == caster.getPlayer().getLevel()))
		{
			caster.getPlayer().setLevel(caster.getTypeLevel());
		}
	}

	@EventHandler
	public void onPlayerExpChangeEvent(PlayerExpChangeEvent event)
	{
		if (Main.getCasters().containsKey(event.getPlayer().getUniqueId()))
		{
			event.setAmount(0);
		}
	}

	@EventHandler
	public void onEntityDeathEvent(EntityDeathEvent event)
	{
		if (event.getEntity() instanceof LivingEntity)
		{
			if (event.getEntity().getKiller() instanceof LivingEntity)
			{
				if (event.getEntity().getKiller() instanceof Player)
				{
					Caster caster = Main.getCasters().get(event.getEntity().getKiller().getUniqueId());

					if (event.getEntity().equals(caster.getPlayer()))
					{
						caster.setTypeExp(caster.getTypeExp() - explossplayer);
						caster.getPlayer().sendMessage(header + ChatColor.GRAY + "Lost " + ChatColor.WHITE
								+ explossplayer + ChatColor.GRAY + " Experience!");
					}

					else if (event.getEntity().getKiller().equals(caster.getPlayer()))
					{
						if (!(caster.getTypeLevel() == caster.getTypeMaxLevel()))
						{
							if (event.getEntity() instanceof Player)
							{
								caster.setTypeExp(caster.getTypeExp() + expgainplayer);
								caster.getPlayer().sendMessage(header + ChatColor.GRAY + "Gained " + ChatColor.WHITE
										+ expgainplayer + ChatColor.GRAY + " Experience!");
							}

							else if (event.getEntity() instanceof Creature)
							{
								caster.setTypeExp(caster.getTypeExp() + expgaincreature);
								caster.getPlayer().sendMessage(header + ChatColor.GRAY + "Gained " + ChatColor.WHITE
										+ expgaincreature + ChatColor.GRAY + " Experience!");
							}

							if (caster.getTypeExp() >= caster.getTypeMaxExp())
							{
								caster.setTypeLevel(caster.getTypeLevel() + 1);
								caster.setTypeExp(caster.getTypeExp() - caster.getTypeMaxExp());
								caster.setTypeMaxExp();

								caster.getPlayer().setLevel(caster.getTypeLevel());
								caster.getPlayer().playSound(caster.getPlayer().getLocation(),
										Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);
							}

							caster.getPlayer().setExp(caster.getTypeExp() / caster.getTypeMaxExp());
						}
					}
				}

				else if (event.getEntity().getKiller() instanceof Creature)
				{

					Caster caster = Main.getCasters().get(event.getEntity().getUniqueId());

					if (event.getEntity().equals(caster.getPlayer()))
					{
						caster.setTypeExp(caster.getTypeExp() - explosscreature);
						caster.getPlayer().sendMessage(header + ChatColor.GRAY + "Lost " + ChatColor.WHITE
								+ explosscreature + ChatColor.GRAY + " Experience!");
					}
				}

			}

			else if (Main.getCasters().containsKey(event.getEntity().getUniqueId()))
			{
				Caster caster = Main.getCasters().get(event.getEntity().getUniqueId());

				caster.setTypeExp(caster.getTypeExp() - explossgeneral);
				caster.getPlayer().sendMessage(header + ChatColor.GRAY + "Lost " + ChatColor.WHITE + explossgeneral
						+ ChatColor.GRAY + " Experience!");
			}
		}
	}

	@EventHandler
	public void onPlayerRespawnEvent(PlayerRespawnEvent event)
	{
		Caster caster = Main.getCasters().get(event.getPlayer().getUniqueId());

		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				caster.getPlayer().setExp(caster.getTypeExp() / caster.getTypeMaxExp());
			}

		}.runTaskLater(Main.getInstance(), 20);
	}

	@EventHandler
	public void onBlockBreakEvent(BlockBreakEvent event)
	{
		Caster caster = Main.getCasters().get(event.getPlayer().getUniqueId());

		if (event.getPlayer().equals(caster.getPlayer()))
		{
			if (event.getBlock().getType().equals(Material.IRON_ORE)
					|| event.getBlock().getType().equals(Material.GOLD_ORE)
					|| event.getBlock().getType().equals(Material.LAPIS_ORE)
					|| event.getBlock().getType().equals(Material.COAL_ORE)
					|| event.getBlock().getType().equals(Material.REDSTONE_ORE)
					|| event.getBlock().getType().equals(Material.DIAMOND_ORE)
					|| event.getBlock().getType().equals(Material.EMERALD_ORE))
			{
				caster.setJobExp(caster.getJobExp() + expgainmine);
				caster.getPlayer().sendMessage(header + ChatColor.GRAY + "Gained " + ChatColor.WHITE + expgainmine
						+ ChatColor.GRAY + " Experience!");
			}

			else if (event.getBlock().getType().equals(Material.PUMPKIN)
					|| event.getBlock().getType().equals(Material.CROPS)
					|| event.getBlock().getType().equals(Material.MELON_BLOCK)
					|| event.getBlock().getType().equals(Material.SUGAR_CANE_BLOCK)
					|| event.getBlock().getType().equals(Material.POTATO)
					|| event.getBlock().getType().equals(Material.CARROT)
					|| event.getBlock().getType().equals(Material.COCOA)
					|| event.getBlock().getType().equals(Material.BEETROOT_BLOCK)
					|| event.getBlock().getType().equals(Material.NETHER_WART_BLOCK))
			{
				caster.setJobExp(caster.getJobExp() + expgainfarm);
				caster.getPlayer().sendMessage(header + ChatColor.GRAY + "Gained " + ChatColor.WHITE + expgainfarm
						+ ChatColor.GRAY + " Experience!");
			}

			else
			{
				caster.setJobExp(caster.getJobExp() + expgainbreak);
				caster.getPlayer().sendMessage(header + ChatColor.GRAY + "Gained " + ChatColor.WHITE + expgainbreak
						+ ChatColor.GRAY + " Experience!");
			}

			if (caster.getJobExp() >= caster.getJobMaxExp())
			{
				caster.setJobLevel(caster.getJobLevel() + 1);
				caster.setJobExp(caster.getJobExp() - caster.getJobMaxExp());
				caster.setJobMaxExp();

				caster.getPlayer().playSound(caster.getPlayer().getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);
			}
		}
	}

	@EventHandler
	public void onCraftItemEvent(CraftItemEvent event)
	{
		Caster caster = Main.getCasters().get(event.getWhoClicked().getUniqueId());

		caster.setJobExp(caster.getJobExp() + expgaincraft);
		caster.getPlayer().sendMessage(
				header + ChatColor.GRAY + "Gained " + ChatColor.WHITE + expgaincraft + ChatColor.GRAY + " Experience!");

		if (caster.getJobExp() >= caster.getJobMaxExp())
		{
			caster.setJobLevel(caster.getJobLevel() + 1);
			caster.setJobExp(caster.getJobExp() - caster.getJobMaxExp());
			caster.setJobMaxExp();

			caster.getPlayer().playSound(caster.getPlayer().getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);
		}
	}
}
