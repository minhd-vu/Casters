package Cast.Casts.Passives.Firearms;

import Cast.Casts.Types.Passive;
import Cast.CommandInterface;
import Cast.Essentials.Caster;
import Cast.Main;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LlamaSpit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class PassiveFirearms extends Passive implements CommandInterface, Listener
{
	private List<LlamaSpit> ironbullets;
	private List<LlamaSpit> goldbullets;
	private List<LlamaSpit> diamondbullets;

	private double irondamage;
	private double golddamage;
	private double diamonddamage;

	private double ironnumberofshots;
	private double goldnumberofshots;
	private double diamondnumberofshots;

	private double ironvelocity;
	private double goldvelocity;
	private double diamondvelocity;

	private double maxironaccuracy;
	private double minironaccuracy;

	private int timer;
	private int count;

	public PassiveFirearms(String name, String description)
	{
		super(name, description);

		ironbullets = new ArrayList<LlamaSpit>();
		diamondbullets = new ArrayList<LlamaSpit>();

		irondamage = 3;
		golddamage = 2;
		diamonddamage = 10;

		ironnumberofshots = 10;
		ironvelocity = 2.0;

		diamondvelocity = 4.0;

		maxironaccuracy = 0.3;
		minironaccuracy = 0.15;

		timer = 100;

		info.add(ChatColor.DARK_AQUA + "Iron Damage: " + irondamage + " HP");
		info.add(ChatColor.DARK_AQUA + "Gold Damage: " + golddamage + " HP");
		info.add(ChatColor.DARK_AQUA + "Diamond Damage: " + diamonddamage + " HP");

		pages.setPage(info);
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event)
	{
		if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
		{
			Player player = event.getPlayer();
			Caster caster = Main.getCasters().get(player.getUniqueId());

			if (caster.hasCast(name))
			{
				if (player.getInventory().getItemInMainHand().getType().equals(Material.IRON_BARDING))
				{
					count = 0;

					new BukkitRunnable()
					{
						@Override
						public void run()
						{
							++count;

							if (count > ironnumberofshots)
							{
								this.cancel();
								return;
							}

							LlamaSpit bullet = (LlamaSpit) player.getWorld().spawnEntity(player.getEyeLocation(), EntityType.LLAMA_SPIT);
							bullet.setShooter(player);
							bullet.setGravity(false);
							//bullet.setVelocity(caster.getPlayer().getEyeLocation().getDirection().normalize().multiply(ironvelocity));

							Vector velocity = caster.getPlayer().getLocation().getDirection();
							velocity.add(new Vector(Math.random() * maxironaccuracy - minironaccuracy, Math.random() * maxironaccuracy - minironaccuracy,
									Math.random() * maxironaccuracy - minironaccuracy));
							bullet.setVelocity(velocity);

							ironbullets.add(bullet);
						}

					}.runTaskTimer(Main.getInstance(), 0, 1);

					player.getWorld().playSound(player.getLocation(), Sound.ENTITY_LLAMA_SPIT, 8.0F, 1.0F);

					new BukkitRunnable()
					{
						@Override
						public void run()
						{
							ironbullets.clear();
						}

					}.runTaskLater(Main.getInstance(), timer);
				}

				else if (player.getInventory().getItemInMainHand().getType().equals(Material.GOLD_BARDING))
				{

				}

				else if (player.getInventory().getItemInMainHand().getType().equals(Material.DIAMOND_BARDING))
				{
					LlamaSpit bullet = (LlamaSpit) player.getWorld().spawnEntity(player.getEyeLocation(), EntityType.LLAMA_SPIT);
					bullet.setShooter(player);
					bullet.setGravity(false);

					Vector velocity = caster.getPlayer().getLocation().getDirection();
					bullet.setVelocity(velocity.normalize().multiply(diamondvelocity));

					diamondbullets.add(bullet);

					player.getWorld().playSound(player.getLocation(), Sound.ENTITY_LLAMA_SPIT, 8.0F, 1.0F);

					new BukkitRunnable()
					{
						@Override
						public void run()
						{
							//diamondbullets.clear();
						}

					}.runTaskLater(Main.getInstance(), timer);
				}
			}
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event)
	{
		if (event.getDamager() instanceof LlamaSpit)
		{
			LlamaSpit bullet = (LlamaSpit) event.getDamager();

			if (bullet.getShooter() instanceof Player)
			{
				Caster caster = Main.getCasters().get(bullet.getShooter());

				if (ironbullets.contains(bullet))
				{
					event.setDamage(irondamage);
				}

//				else if (goldbullets.contains(bullet))
//				{
//					event.setDamage(golddamage);
//				}

				else if (diamondbullets.contains(bullet))
				{
					event.setDamage(diamonddamage);
				}
			}
		}
	}
}
