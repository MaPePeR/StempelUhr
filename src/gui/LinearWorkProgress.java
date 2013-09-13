package gui;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;

import zeitgeber.AZeit;
import zeitgeber.Zeit;

public class LinearWorkProgress extends AWorkProgress
{
	private static final long serialVersionUID = 4649807201579673414L;
	public LinearWorkProgress(AZeit startZeit, AZeit uhr)
	{
		super(startZeit, uhr);
		this.setPreferredSize(new Dimension(24*60/2,140));

	}

	
	private double pixelPerMin;
	@Override
	protected void initialize(Graphics2D g)
	{
		pixelPerMin = this.getWidth()/(24.*60);

	}

	@Override
	protected void drawLine(Graphics2D g, AZeit t1, float posPercent,
			float sizePercent)
	{
		g.drawLine(timeToPixel(t1), percentToPixel(posPercent), timeToPixel(t1), percentToPixel(sizePercent));

	}

	private int percentToPixel(float percent)
	{
		return (int)( this.getHeight()*percent);
	}
	@Override
	protected void drawBox(Graphics2D g, AZeit t1, AZeit t2, float posPercent,
			float sizePercent)
	{
		if(t1.vor(t2))
		{
			g.fillRect(timeToPixel(t1),percentToPixel(posPercent), timeToPixel(t2.sub(t1)),percentToPixel(sizePercent));
		}
		else
		{
			g.fillRect(timeToPixel(t1), percentToPixel(posPercent), this.getWidth(), percentToPixel(sizePercent));
			g.fillRect(0, percentToPixel(posPercent), timeToPixel(t2), percentToPixel(sizePercent));
		}
	}
	private int timeToPixel(AZeit t)
	{
		return (int)(t.getMinutes()*pixelPerMin);
	}

	@Override
	public int pixelToMinutes(Point p)
	{
		return (int) (p.getX()/pixelPerMin);
	}

	@Override
	protected void drawHour(Graphics2D g, int  h, Zeit zeit, float f)
	{

		g.drawString(""+h, timeToPixel(zeit),percentToPixel(f));
	}
}
