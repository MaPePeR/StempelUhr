package zeitgeber;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class Uhrzeit extends AZeit 
{
	Calendar calendar = new GregorianCalendar();
	public void aktualisiere()
	{
		calendar.setTimeInMillis(System.currentTimeMillis());
		this.fireZeitHasChanged();
	}
	public int getMinutes()
	{
		return 60*getHour()+getMin();
	}
	public int getHour()
	{
		return calendar.get(Calendar.HOUR_OF_DAY);
	}
	public int getMin()
	{
		return calendar.get(Calendar.MINUTE);
	}


}