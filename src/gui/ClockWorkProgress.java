package gui;

import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.geom.Arc2D;
import java.awt.geom.Rectangle2D;

import zeitgeber.AZeit;
import zeitgeber.Zeit;

public class ClockWorkProgress extends AWorkProgress
{

	private static final long serialVersionUID = 2476962221094781453L;
	private double durchmesser;
	private double radius;
	public ClockWorkProgress(AZeit startZeit, AZeit uhr, AZeit hoverZeit)
	{
		super(startZeit, uhr, hoverZeit);
		this.setPreferredSize(new Dimension(200,200));
	}

	@Override
	protected void initialize(Graphics2D g)
	{
		durchmesser = Math.min(this.getHeight(),this.getWidth());
		radius = durchmesser/2;
		g.translate(this.getWidth()/2-radius, this.getHeight()/2-radius);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//		g.drawLine(0, 0, 200, 200);
//		g.drawLine(200, 0, 0, 200);
	}

	private int minsToDegrees(int mins)
	{
		return 360*mins/(12*60);
		//return 6*mins/24;
		//return mins/4;
	}
	
	double timeToAngle(AZeit t1)
	{
		return Math.toRadians(minsToDegrees( t1.getMinutes()%(12*60))-90);
	}
	double timeToDegree(AZeit t1)
	{
		//return Math.toRadians(minsToDegrees(t1.getMinutes()%(12*60))-90);
		return (12.*60+t1.getMinutes())%(12*60)/2.0;
	}
	@Override
	protected void drawLine(Graphics2D g, AZeit t1, float posPercent,
			float sizePercent)
	{
		if(posPercent>1 || posPercent<0 || sizePercent>1 || sizePercent<0)
			throw new IllegalArgumentException();
		g.setStroke(new BasicStroke());
		final double angle=timeToAngle(t1);
		final double sin=Math.sin(angle);
		final double cos=Math.cos(angle);
		g.drawLine(
				(int)(radius+cos*(1-posPercent)*radius), 
				(int)(radius+sin*(1-posPercent)*radius),
				(int)(radius+cos*(1-posPercent-sizePercent)*radius), 
				(int)(radius+sin*(1-posPercent-sizePercent)*radius)
				);
	}

	@Override
	protected void drawBox(Graphics2D g, AZeit t1, AZeit t2, float posPercent,
			float sizePercent)
	{
		g.setStroke(new BasicStroke((float) (radius*sizePercent/2),BasicStroke.CAP_BUTT,BasicStroke.JOIN_BEVEL));		
		Arc2D a = new Arc2D.Float();
		a.setArcByCenter(radius, radius, (1-posPercent-sizePercent/2.)*radius/2. + radius/4.+1,-timeToDegree(t1)+90,-timeToDegree(t1.vonNach(t2)), Arc2D.OPEN);
		g.draw(a);
//		g.setStroke(new BasicStroke());
//		g.setColor(Color.white);
//		g.draw(a.getBounds());
//		g.drawLine(0, 0, (int)(durchmesser), (int)(durchmesser));
//		g.drawLine(0, (int)(durchmesser), (int)(durchmesser), 0);
//		g.setStroke(new BasicStroke());

//		g.draw(a);
//		g.drawArc((int)(durchmesser*(posPercent)/2+radius*sizePercent/4),
//				(int)(durchmesser*(posPercent)/2+radius*sizePercent/4),
//				(int)(durchmesser-durchmesser*(posPercent)-radius*sizePercent/2),
//				(int)(durchmesser-durchmesser*(posPercent)-radius*sizePercent/2),
//				-(int)timeToDegree(t1)+90,
//				-(int)timeToDegree(t1.vonNach(t2)));
		//System.out.println(((t1.vonNach(t2).getMinutes()%(12*60))));
//		drawLine(g, t1, posPercent, sizePercent);
//		drawLine(g, t2, posPercent, sizePercent);
	}

	@Override
	public int pixelToMinutes(Point p)
	{
		int buf = 12*60*((720+(int)(Math.toDegrees(Math.atan2(p.getY()-this.getHeight()/2., p.getX()-this.getWidth()/2.)))+90)%360)/360;
		return buf>this.startZeit.getMinutes()?buf:buf+12*60;
	}

	@Override
	protected void drawHour(Graphics2D g,int h, Zeit t1, float posPercent)
	{
		if(!(1<=h && h<=12))
			return;
		final double angle=timeToAngle(t1);
		final double sin=Math.sin(angle);
		final double cos=Math.cos(angle);
		Rectangle2D bounds = g.getFontMetrics(g.getFont()).getStringBounds(""+t1.getHour(), g);
		final int fwidth=(int)bounds.getWidth();
		final int fheight=(int)bounds.getHeight();
		g.drawString(""+t1.getHour(),
				(int)(radius+cos*(1-posPercent)*radius)-fwidth/2, 
				(int)(radius+sin*(1-posPercent)*radius)+fheight/2
				);
		
	}

}
