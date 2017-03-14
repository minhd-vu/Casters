package Cast.Essentials.Effects;

import java.text.DecimalFormat;

public class Effect
{
	double duration;
	long time;

	public Effect()
	{
		duration = 0.0;
		time = 0;
	}

	public boolean hasTime()
	{
		if (getTime() > 0)
		{
			return true;
		}

		return false;
	}

	public double getTime()
	{
		return Double.parseDouble(new DecimalFormat("##.#").format(time / 1000.0 + duration - System.currentTimeMillis() / 1000.0));
	}

	public void setDuration(double duration)
	{
		this.duration = duration / 20;
		this.time = System.currentTimeMillis();
	}
}
