package Cast.Casts.Passives.Firearms;

import Cast.Casts.Passives.Passive;
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
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public abstract class Firearm extends Passive
{
	protected static final DecimalFormat decimalformat = new DecimalFormat("###.#");
	protected List<LlamaSpit> bullets;
	protected Material firearm;

	protected double damage;
	protected double headshot;
	protected int shots;
	protected double velocity;
	protected double maxaccuracy;
	protected double minaccuracy;
	protected long timer;
	protected long reload;
	protected boolean gravity;
	protected double recoil;
	protected float volume;
	protected float pitch;
	protected int amplitude;
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
		if (event.getPlayer().getInventory().getItemInMainHand().getType().equals(firearm))
		{
			Player player = event.getPlayer();
			Caster caster = Main.getCasters().get(player.getUniqueId());

			if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
			{
				if (caster.canCastPassive(name, cooldown, 0))
				{
					count = 0;

					new BukkitRunnable()
					{
						@Override
						public void run()
						{
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

					player.getWorld().playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, volume, pitch);
					player.setVelocity(player.getEyeLocation().getDirection().setY(0).normalize().multiply(-recoil));

					cooldown.start(player.getName());
				}
			}
		}
	}

	@EventHandler
	public void onPlayerToggleSneakEvent(PlayerToggleSneakEvent event)
	{
		if (event.getPlayer().getInventory().getItemInMainHand().getType().equals(firearm))
		{
			Player player = event.getPlayer();
			Caster caster = Main.getCasters().get(player.getUniqueId());

			if (caster.canCastPassive(name, new Cooldown(), 0))
			{
				if (event.isSneaking())
				{
					player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, amplitude, true, true));
				}

				else
				{
					player.removePotionEffect(PotionEffectType.SLOW);
				}
			}
		}
	}

	@EventHandler
	public void onPlayerItemHeldEvent(PlayerItemHeldEvent event)
	{
		Caster caster = Main.getCasters().get(event.getPlayer().getUniqueId());

		if (caster.getType().getCasts().containsKey(name))
		{
			ItemStack item = caster.getPlayer().getInventory().getItem(event.getPreviousSlot());

			if (item != null && item.getType().equals(firearm))
			{
				if (caster.getPlayer().isSneaking())
				{
					caster.getPlayer().removePotionEffect(PotionEffectType.SLOW);
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
				Caster caster = Main.getCasters().get(((Player) bullet.getShooter()).getUniqueId());

				if (bullets.contains(bullet))
				{
					if (bullet.getLocation().getY() > event.getEntity().getLocation().getY() + 1.35)
					{
						if (caster.sameParty(event.getEntity()))
						{
							event.setCancelled(true);
							return;
						}

						caster.getPlayer().getWorld().playSound(caster.getPlayer().getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1.0F, 1.0F);
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
