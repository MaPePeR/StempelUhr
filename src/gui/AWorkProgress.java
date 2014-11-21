package gui;

import java.awt.Color;
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
	protected AZeit hoverZeit;
	protected AZeit startZeit; 
	public AWorkProgress(AZeit startZeit, AZeit uhr, AZeit hoverZeit)
	{

		this.startZeit=startZeit;
		this.uhr=uhr;
		this.hoverZeit=hoverZeit;
		hoverZeit.addZeitListener(this);
		uhr.addZeitListener(this);
		startZeit.addZeitListener(this);
		Config.minGesamtZeitForPlus.addZeitListener(this);

	}
	@Override
	protected void paintComponent(Graphics g_)
	{
		super.paintComponent(g_);
		Graphics2D g = (Graphics2D)g_;
		initialize(g);
		Zeit t = startZeit.getZeit();
		Zeit t2;
		AZeit restAbwesenheit = Config.abwesenheitZeit;
		for(Config.ZeitAbschnitt z: Config.getAbschnitte())
		{
			t2=t.add(z.dauer);
			g.setColor(z.arbeit?Config.arbeitColor:Config.pauseColor);
			drawBox(g,t,t2,0.2f,0.5f);
			if (!z.arbeit && restAbwesenheit.getMinutes() > 0 && t.sub(startZeit).vor(Config.minGesamtZeitForPlus)) {
				g.setColor(Config.abwesenheitColor);
				if (t2.sub(startZeit).nach(Config.minGesamtZeitForPlus)) {
					drawBox(g, t, startZeit.add(Config.minGesamtZeitForPlus), 0.2f, 0.25f);
					restAbwesenheit = restAbwesenheit.sub(startZeit.add(Config.minGesamtZeitForPlus).sub(t));
				} else if (restAbwesenheit.laenger(z.dauer)) {
					drawBox(g, t, t.add(z.dauer), 0.2f, 0.25f);
					restAbwesenheit = restAbwesenheit.sub(z.dauer);
				} else {
					drawBox(g, t, t.add(restAbwesenheit), 0.2f, 0.25f);
					restAbwesenheit = new Zeit(0,0);
				}
			}
			t=t2;
		}
		g.setColor(Config.minGesamtZeitForPlusColor);
		drawBox(g, startZeit, startZeit.add(Config.minGesamtZeitForPlus), 0.7f,0.3f);
		if (restAbwesenheit.getMinutes()>0) {
			g.setColor(Config.abwesenheitColor);
			//drawBox(g, startZeit.add(Config.minGesamtZeitForPlus).sub(restAbwesenheit), startZeit.add(Config.minGesamtZeitForPlus), 0.7f, 0.15f);
			drawBox(g, startZeit, startZeit.add(restAbwesenheit),0.7f,0.15f);
		}


		
		g.setColor(Config.endZeitColor);
		drawLine(g,hoverZeit,0,1);
		if (hoverZeit.compareTo(uhr) != 0) {
			g.setColor(Config.uhrZeitColor);
			drawLine(g,uhr,0,1);
		}
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
