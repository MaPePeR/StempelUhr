package gui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;

import javax.swing.JPanel;

import zeitgeber.AZeit;
import zeitgeber.Zeit;
import data.Config;
import data.ZeitListener;

public abstract class AWorkProgress extends JPanel implements ZeitListener
{
	private static final long serialVersionUID = 22490528284878589L;
	protected AZeit uhr;
	protected AZeit startZeit; 
	public AWorkProgress(AZeit startZeit, AZeit uhr)
	{

		this.startZeit=startZeit;
		this.uhr=uhr;
		uhr.addZeitListener(this);
		startZeit.addZeitListener(this);
	}
	@Override
	protected void paintComponent(Graphics g_)
	{
		super.paintComponent(g_);
		Graphics2D g = (Graphics2D)g_;
		initialize(g);
		Zeit t = startZeit.getZeit();
		Zeit t2;
		for(Config.ZeitAbschnitt z: Config.getAbschnitte())
		{
			t2=t.add(z.dauer);
			g.setColor(z.arbeit?Config.arbeitColor:Config.pauseColor);
			drawBox(g,t,t2,0.2f,0.5f);
			t=t2;
		}
		g.setColor(Config.minGesamtZeitForPlusColor);
		drawBox(g, startZeit, startZeit.add(Config.minGesamtZeitForPlus), 0.7f,0.3f);

		
		g.setColor(Config.endZeitColor);
		drawLine(g,uhr,0,1);
		for(int i=0;i<24;i++)
		{
			g.setColor(Config.skalaColor);
			drawLine(g,new Zeit(i,0),0f,0.1f);
			drawLine(g,new Zeit(i,30),0f,0.05f);
			g.setColor(Config.skalaTimeColor);
			drawHour(g,i, new Zeit(i, 0), 0.1f);
		}
	}
	protected abstract void drawHour(Graphics2D g,int i, Zeit zeit, float f);
	protected void initialize(Graphics2D g)
	{
		
	}
	//protected abstract void drawBox(Graphics2D g, AZeit t1, AZeit t2);
	protected abstract void drawLine(Graphics2D g, AZeit t1, float posPercent, float sizePercent);
	protected abstract void drawBox(Graphics2D g, AZeit t1, AZeit t2, float posPercent, float sizePercent);
	public abstract int pixelToMinutes(Point p); 
	@Override
	public void timeHasChanged()
	{
		this.repaint();
	}
}
