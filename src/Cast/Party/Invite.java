package Cast.Party;

import Cast.Essentials.Caster;

public class Invite
{
	private boolean invite;
	private Caster sender;
	
	public Invite(Caster sender)
	{
		invite = true;
		this.sender = sender;
	}
	
	public boolean hasInvite()
	{
		return invite;
	}
	
	public Caster getSender()
	{
		return sender;
	}
	
	public void setSender(Caster sender)
	{
		this.sender = sender;
	}
}
