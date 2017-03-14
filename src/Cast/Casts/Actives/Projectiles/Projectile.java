package Cast.Casts.Actives.Projectiles;

import Cast.Casts.Actives.Active;
import Cast.Main;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class Projectile extends Active implements Listener
{
	protected List<UUID> projectiles;

	public Projectile(String name, String description)
	{
		super(name, description);

		projectiles = new ArrayList<UUID>();
	}

	@EventHandler
	public void onProjectileHitEvent(ProjectileHitEvent event)
	{
		if (projectiles.contains(event.getEntity().getUniqueId()))
		{
			projectiles.remove(event.getEntity().getUniqueId());
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onEntityExplodeEvent(EntityExplodeEvent event)
	{
		if (projectiles.contains(event.getEntity().getUniqueId()))
		{
			event.setCancelled(true);

			if (event.isCancelled())
			{
				event.getEntity().getWorld().spigot().playEffect(event.getEntity().getLocation().add(0.0D, 0.5D, 0.0D), Effect.EXPLOSION, 0, 0, 0.2F, 0.2F, 0.2F, 0.1F, 50, 16);
				event.getEntity().getWorld().spigot().playEffect(event.getEntity().getLocation(), Effect.EXPLOSION_LARGE);
			}

			projectiles.remove(event.getEntity().getUniqueId());
		}
	}
}
