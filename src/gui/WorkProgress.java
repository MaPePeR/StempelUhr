package gui;

import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import zeitgeber.AZeit;
import zeitgeber.Zeit;
import data.Config;
import data.ZeitListener;

public class WorkProgress extends JPanel implements ZeitListener
{
	private static final long serialVersionUID = 22490528284878589L;
	private AZeit uhr;
	private AZeit startZeit; 
	public WorkProgress(AZeit startZeit, AZeit uhr)
	{

		this.startZeit=startZeit;
		this.setPreferredSize(new Dimension(24*60/2,140));
		this.uhr=uhr;
		uhr.addZeitListener(this);
		startZeit.addZeitListener(this);
	}
	@Override
	protected void paintComponent(Graphics g_)
	{
		super.paintComponent(g_);
		Graphics2D g = (Graphics2D)g_;
		calcPixelPerMin();
		Zeit t = startZeit.getZeit();
		Zeit t2;
		for(Config.ZeitAbschnitt z: Config.getAbschnitte())
		{
			t2=t.add(z.dauer);
			g.setColor(z.arbeit?Config.arbeitColor:Config.pauseColor);
			drawBox(g,t,t2);
			t=t2;
		}
		g.setColor(Config.minGesamtZeitForPlusColor);
		drawBox(g, startZeit, startZeit.add(Config.minGesamtZeitForPlus), 115,20);
		
		g.setStroke(new BasicStroke(2f));

		
		g.setColor(Config.endZeitColor);
		g.drawLine(timeToPixel(uhr), 0, timeToPixel(uhr), this.getHeight());
		g.setStroke(new BasicStroke(1f));
		g.setColor(Config.skalaColor);
		for(int i=0;i<=24;i++)
		{
			g.drawLine(timeToPixel(i, 0), 0, timeToPixel(i, 0), 10);
			g.drawString(""+i, timeToPixel(i, 0), 10);
			g.drawLine(timeToPixel(i, 30), 0, timeToPixel(i, 30), 5);
		}
	}
	private void calcPixelPerMin()
	{
		pixelPerMin = this.getWidth()/(24.*60);
	}
	private double pixelPerMin = this.getWidth()/(24.*60);
	private int timeToPixel(AZeit t)
	{
		return (int)(t.getMinutes()*pixelPerMin);
	}
	private int timeToPixel(int hour, int min)
	{
		return (int)((hour*60+min)*pixelPerMin);
	}
	private void drawBox(Graphics g, AZeit t1, AZeit t2)
	{
		drawBox(g, t1, t2, 15, 100);
	}
	
	private void drawBox(Graphics g, AZeit t1, AZeit t2, int pos, int size)
	{
		if(t1.vor(t2))
		{
			g.fillRect(timeToPixel(t1), pos, timeToPixel(t2.sub(t1)),size);
		}
		else
		{
			g.fillRect(timeToPixel(t1), pos, this.getWidth(), size);
			g.fillRect(0, pos, timeToPixel(t2), size);
		}
	}
	@Override
	public void timeHasChanged()
	{
		this.repaint();
	}
}
