package Cast.Casts.Passives.Firearms;

import Cast.Casts.Types.Passive;
import Cast.Essentials.Caster;
import Cast.Essentials.Schedulers.Cooldown;
import Cast.Essentials.Schedulers.WarmUp;
import Cast.Main;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LlamaSpit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public abstract class Firearm extends Passive
{
	protected List<LlamaSpit> bullets;

	protected Material firearm;

	protected WarmUp warmup;
	protected Cooldown cooldown;

	protected double damage;
	protected double headshot;
	protected int shots;
	protected double velocity;
	protected double maxaccuracy;
	protected double minaccuracy;
	protected long timer;
	protected long reload;
	protected boolean gravity;

	private int count;

	public Firearm(String name, String description)
	{
		super(name, description);

		bullets = new ArrayList<LlamaSpit>();

		warmup = new WarmUp();
		cooldown = new Cooldown();
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event)
	{
		if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
		{
			Player player = event.getPlayer();
			Caster caster = Main.getCasters().get(player.getUniqueId());

			if (caster.canCast(name, cooldown, 0))
			{
				if (player.getInventory().getItemInMainHand().getType().equals(firearm))
				{
					count = 0;

					new BukkitRunnable()
					{
						@Override
						public void run()
						{
							// TODO: Add Headshots. Guardian Beams As Sniper. Knockback (Recoil).

							if (++count > shots)
							{
								this.cancel();
								return;
							}

							LlamaSpit bullet = (LlamaSpit) player.getWorld().spawnEntity(player.getEyeLocation(), EntityType.LLAMA_SPIT);
							bullet.setShooter(player);
							bullet.setGravity(gravity);
							bullet.setVelocity(caster.getPlayer().getLocation().getDirection()
									.add(new Vector(Math.random() * maxaccuracy - minaccuracy, Math.random() * maxaccuracy - minaccuracy,
											Math.random() * maxaccuracy - minaccuracy)).normalize().multiply(velocity));

							bullets.add(bullet);

							new BukkitRunnable()
							{
								@Override
								public void run()
								{
									bullet.remove();
								}

							}.runTaskLater(Main.getInstance(), timer);
						}

					}.runTaskTimer(Main.getInstance(), 0, 1);

					player.getWorld().playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1.0F, 2.5f); // TODO: Change The Pitch For Unique Effects.

					cooldown.start(player.getName());
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

				if (bullets.contains(bullet))
				{
					double projectileheight = bullet.getLocation().getY();
					double playerbodyheight = event.getEntity().getLocation().getY() + 1.35;

					if (projectileheight > playerbodyheight)
					{
						caster.getPlayer().playSound(caster.getPlayer().getEyeLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1.0F, 1.0F); // TODO: This Needs To Be Fixed.

						event.setDamage(damage * headshot);
					}

					else
					{
						event.setDamage(damage);
					}

					bullets.remove(bullet);
				}
			}
		}
	}
}
