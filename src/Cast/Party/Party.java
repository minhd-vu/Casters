package Cast.Party;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Party
{
	private UUID leader;
	private List<UUID> members = new ArrayList<UUID>();
	
	public Party(UUID leader)
	{
		this.leader = leader;
	}
	
	public void setLeader(UUID leader)
	{
		this.leader = leader;
	}
	
	public UUID getLeader()
	{
		return leader;
	}
	
	public List<UUID> getMembers()
	{
		return members;
	}
}
