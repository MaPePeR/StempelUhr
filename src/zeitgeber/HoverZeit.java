package zeitgeber;

import gui.AWorkProgress;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import data.ZeitListener;


public class HoverZeit extends AZeit implements MouseMotionListener, MouseListener, ZeitListener
{

	AZeit alternativ;
	public HoverZeit(AZeit alternativ)
	{
		this.alternativ=alternativ;
		this.alternativ.addZeitListener(this);
	}
	Object hovering=null;
	int minutes;

	@Override
	public void mouseMoved(MouseEvent e)
	{
		Component c = (Component) (e.getSource());
		if(c instanceof AWorkProgress)
		{
			minutes=((AWorkProgress)c).pixelToMinutes(e.getPoint());
			this.fireZeitHasChanged();
		}
	}

	@Override
	public int getMin()
	{
		return getMinutes()%60;
	}

	@Override
	public int getHour()
	{
		return getMinutes()/60;
	}

	@Override
	public int getMinutes()
	{
		if(hovering==null)
			return alternativ.getMinutes();
		else
			return minutes;
	}


	@Override
	public void mouseExited(MouseEvent e){
		if(e.getSource()==hovering)
		{
			hovering=null;
			this.fireZeitHasChanged();
		}
	}

	@Override
	public void timeHasChanged()
	{
		if(hovering==null)
		{
			this.fireZeitHasChanged();
		}
	}

	
	@Override
	public void mouseDragged(MouseEvent e) {}
	

	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e){}

	@Override
	public void mouseReleased(MouseEvent e){}

	@Override
	public void mouseEntered(MouseEvent e){
		hovering = e.getSource();
	}
}
