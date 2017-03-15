package Casters.Party;

import Casters.Essentials.Caster;

public class Invite
{
	private Caster sender;

	public Invite(Caster sender)
	{
		this.sender = sender;
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
