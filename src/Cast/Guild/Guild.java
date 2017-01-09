package Cast.Guild;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.entity.Player;

public class Guild
{
	private String name;
	private String abreviation;
	private String description;

	private UUID leader;
	private List<UUID> officers = new ArrayList<UUID>();
	private List<UUID> members = new ArrayList<UUID>();
	private List<UUID> pending = new ArrayList<UUID>();
	
	private int bank;
	private int size;
	private double tax;
	
	public Guild(String name)
	{
		setName(name);
	}
	
	public Guild(String name, Player leader)
	{
		setName(name);
		setLeader(leader.getUniqueId());
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getAbreviation()
	{
		return abreviation;
	}

	public void setAbreviation(String abreviation)
	{
		this.abreviation = abreviation;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public UUID getLeader()
	{
		return leader;
	}

	public void setLeader(UUID leader)
	{
		this.leader = leader;
	}

	public List<UUID> getOfficers()
	{
		return officers;
	}

	public void setOfficers(List<UUID> officers)
	{
		this.officers = officers;
	}

	public List<UUID> getMembers()
	{
		return members;
	}

	public void setMembers(List<UUID> members)
	{
		this.members = members;
	}
	
	public List<UUID> getPending()
	{
		return pending;
	}
	
	public void setPending(List<UUID> pending)
	{
		this.pending = pending;
	}
	
	public int getBank()
	{
		return bank;
	}
	
	public void setBank(int bank)
	{
		this.bank = bank;
	}
	
	public int getSize()
	{
		return size;
	}
	
	public void setSize(int size)
	{
		this.size = size;
	}
	
	public double getTax()
	{
		return tax;
	}
	
	public void setTax(double tax)
	{
		this.tax = tax;
	}
}
