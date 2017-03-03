package Cast.Party;

import Cast.Essentials.Caster;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class Party
{
	public static String header = ChatColor.DARK_GRAY + "[" + ChatColor.DARK_AQUA + "Party" + ChatColor.DARK_GRAY + "]";

	private Caster leader;
	private List<Caster> members = new ArrayList<Caster>();

	public Party(Caster leader)
	{
		this.leader = leader;
		members.add(leader);
	}

	public Caster getLeader()
	{
		return leader;
	}

	public void setLeader(Caster leader)
	{
		this.leader = leader;
	}

	public List<Caster> getMembers()
	{
		return members;
	}
}
