package zeitgeber;

import java.util.LinkedList;
import java.util.List;

import data.ZeitListener;



public abstract class AZeit implements Comparable<AZeit>
{
	public abstract int getMin();
	public abstract int getHour();
	public abstract int getMinutes();
	public Zeit add(AZeit t)
	{
		return new Zeit(this.getMinutes()+t.getMinutes());
	}
	public Zeit sub(AZeit t)
	{
		return new Zeit(this.getMinutes()-t.getMinutes());
	}
	public boolean vor(AZeit t)
	{
		return this.getMinutes()<t.getMinutes();
	}
	public boolean nach(AZeit t)
	{
		return this.getMinutes()>t.getMinutes();
	}
	protected final void fireZeitHasChanged()
	{
		for(ZeitListener zl: listeners)
		{
			zl.timeHasChanged();
		}
	}
	private List<ZeitListener> listeners = new LinkedList<ZeitListener>();
	public void addZeitListener(ZeitListener zl)
	{
		listeners.add(zl);
	}
	public void removeZeitListener(ZeitListener zl)
	{
		listeners.remove(zl);
	}
	public String toString()
	{
		return String.format("%d:%02d", this.getHour(),this.getMin());
	}
	public final Zeit getZeit()
	{
		return new Zeit(this.getMinutes());
	}
	public final Zeit vonNach(AZeit t)
	{
		return new Zeit(24*60-this.getMinutes()+t.getMinutes());
	}
	@Override
	public int compareTo(AZeit o)
	{
		return this.getMinutes()-o.getMinutes();
	}
	
	public String toBase100() {
		return String.format("%.2f", this.getMinutes()/60.0);
	}
	
	public String getNormalAnd100() {
		return String.format("%d:%02d (%.2f)", this.getHour(),this.getMin(), this.getMinutes()/60.0);
	}
	public String getNormalAnd100(String prefix) {
		return String.format("%s%d:%02d (%s%.2f)", prefix, this.getHour(),this.getMin(), prefix, this.getMinutes()/60.0);
	}
}
