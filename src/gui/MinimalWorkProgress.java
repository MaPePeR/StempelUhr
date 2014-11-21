package gui;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;

import zeitgeber.AZeit;
import zeitgeber.Zeit;


public class MinimalWorkProgress extends AWorkProgress
{
	private static final long serialVersionUID = -5531251461326590719L;

	public MinimalWorkProgress(AZeit startZeit, AZeit uhr, AZeit hoverZeit)
	{
		super(startZeit, uhr, hoverZeit);
		this.setPreferredSize(new Dimension(12*60/2,200));
	}

	private AZeit startHour;
	private float pixelPerMin;
	protected void initialize(Graphics2D g)
	{
		startHour=new Zeit(this.startZeit.getHour(),0);
		pixelPerMin=this.getWidth()/(float)(12*60);
		AZeit t=startHour;
		Zeit oneHour=new Zeit(1,0);
		for(int i=0;i<12;t=t.add(oneHour),i++)
		{
			g.drawString(""+t.getHour(), timeToPixel(t), (int)(this.getHeight()*0.1));
		}
	}
	@Override
	protected void drawHour(Graphics2D g, int i, Zeit zeit, float f)
	{
//		if(zeit.getHour()>=startZeit.getHour())
//		{
//			g.drawString(""+zeit.getHour(), timeToPixel(zeit), (int)(this.getHeight()*f));
//		}
	}

	private int timeToPixel(AZeit t1)
	{
		return (int) ((startHour.vonNach(t1)).getMinutes()*pixelPerMin);
	}


	@Override
	protected void drawLine(Graphics2D g, AZeit t1, float posPercent,
			float sizePercent)
	{
		final int x=timeToPixel(t1);
		g.drawLine(x, (int)(this.getHeight()*posPercent), x, (int)(this.getHeight()*(posPercent+sizePercent)));

	}

	@Override
	protected void drawBox(Graphics2D g, AZeit t1, AZeit t2, float posPercent,
			float sizePercent)
	{
		final int x1=timeToPixel(t1);
		final int width=timeToPixel(t2)-x1;
		g.fillRect(x1, (int)(posPercent*this.getHeight()), width, (int)(this.getHeight()*sizePercent));
	}

	@Override
	public int pixelToMinutes(Point p)
	{
		return (int) ((p.getX()/pixelPerMin+startHour.getMinutes()));
	}

}
