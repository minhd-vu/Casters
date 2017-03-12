package Cast.Casts.Passives.Firearms;

import Cast.Casts.Types.Passive;
import org.bukkit.entity.LlamaSpit;

import java.util.ArrayList;
import java.util.List;

public abstract class Firearm extends Passive
{
	private List<LlamaSpit> bullets;

	private double damage;
	private int shots;
	private double velocity;
	private double maxaccuracy;
	private double minaccuracy;

	public Firearm(String name, String description)
	{
		super(name, description);

		bullets = new ArrayList<LlamaSpit>();
	}
}
