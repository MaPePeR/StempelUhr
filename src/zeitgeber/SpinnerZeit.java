package zeitgeber;

import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public class SpinnerZeit extends AZeit implements ChangeListener
{
	private int hour,min;
	private JSpinner hour_spinner;
	private JSpinner min_spinner;
	public SpinnerZeit(JSpinner hour_spinner, JSpinner min_spinner)
	{
		this.min_spinner=min_spinner;
		this.hour_spinner=hour_spinner;
		this.min_spinner.getModel().addChangeListener(this);
		this.hour_spinner.getModel().addChangeListener(this);
		stateChanged(null);
		
	}
	@Override
	public void stateChanged(ChangeEvent e)
	{
		this.hour=(Integer)this.hour_spinner.getValue();
		this.min=(Integer)this.min_spinner.getValue();
		this.fireZeitHasChanged();
	}

	@Override
	public int getMin()
	{
		return min;
	}

	@Override
	public int getHour()
	{
		return hour;
	}

	@Override
	public int getMinutes()
	{
		return 60*hour+min;
	}


}
