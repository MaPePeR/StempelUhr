package data;
import java.awt.Color;

import zeitgeber.AZeit;
import zeitgeber.MinGesamtZeitForMinProduktiv;
import zeitgeber.Spinner100Zeit;
import zeitgeber.Zeit;


public class Config
{
	public static Spinner100Zeit minProduktivForPlus=new Spinner100Zeit(7,59);
	public static Spinner100Zeit abwesenheitZeit = new Spinner100Zeit(0,0);
	public static AZeit minGesamtZeitForPlus=new MinGesamtZeitForMinProduktiv(minProduktivForPlus,abwesenheitZeit);
	
	
	public static final Color arbeitColor = Color.RED;
	public static final Color pauseColor = Color.GREEN;
	public static final Color minGesamtZeitForPlusColor = Color.BLACK;
	public static final Color endZeitColor = Color.BLUE;
	public static final Color uhrZeitColor = Color.MAGENTA;
	public static final Color abwesenheitColor = Color.CYAN;
	public static final Color skalaColor = Color.LIGHT_GRAY;
	public static final Color skalaTimeColor = Color.BLACK;
	private static ZeitAbschnitt[] abschnitte=null;
	
	public static ZeitAbschnitt[] getAbschnitte()
	{
		if(abschnitte!=null)
			return abschnitte;
		abschnitte=new ZeitAbschnitt[]{
				new ZeitAbschnitt(new Zeit(6,0), true),
				new ZeitAbschnitt(new Zeit(0,40), false),
				new ZeitAbschnitt(new Zeit(3,0), true),
				new ZeitAbschnitt(new Zeit(0,15), false),
				new ZeitAbschnitt(new Zeit(1,0), true),
		};
		return abschnitte;
	}
	public static class ZeitAbschnitt
	{
		public final Zeit dauer;
		public final boolean arbeit;
		public ZeitAbschnitt(Zeit dauer, boolean arbeit)
		{
			this.dauer=dauer;
			this.arbeit=arbeit;
		}
	}
}
