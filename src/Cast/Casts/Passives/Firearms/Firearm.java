package Cast.Casts.Passives.Firearms;

import Cast.Casts.Types.Passive;
import Cast.Essentials.Caster;
import Cast.Main;
import org.bukkit.ChatColor;
import org.bukkit.entity.LlamaSpit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.ArrayList;
import java.util.List;

public abstract class Firearm extends Passive
{
	protected List<LlamaSpit> bullets;

	protected double damage;
	protected int shots;
	protected double velocity;
	protected double maxaccuracy;
	protected double minaccuracy;
	protected long timer;
	protected long reload;

	public Firearm(String name, String description)
	{
		super(name, description);

		bullets = new ArrayList<LlamaSpit>();
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
					event.setDamage(damage);
				}
			}
		}
	}
}
