package zeitgeber;

public class Zeit extends AZeit
{
	private final int hour;
	private final int min;
	public Zeit(int minutes)
	{
		this.hour=(24+minutes/60)%24;
		this.min=minutes%60;
//		if(this.hour<0 || this.hour>=24)
//			throw new IllegalArgumentException();
		if(this.min<0 || this.hour>=60)
			throw new IllegalArgumentException();
	}
	public Zeit(int hour, int min)
	{
		this(hour*60+min);
	}
	public int getHour()
	{
		return hour;
	}
	public int getMin()
	{
		return min;
	}
	public int getMinutes()
	{
		return hour*60+min;
	}


}
