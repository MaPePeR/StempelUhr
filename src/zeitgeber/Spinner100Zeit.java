package zeitgeber;

import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Spinner100Zeit extends AZeit implements ChangeListener {

	public Spinner100Zeit(int starthour, int startminute) {
		this.hour=starthour;
		this.min = startminute;
	}
	@Override
	public int getMin() {
		return min;
	}

	@Override
	public int getHour() {
		return hour;
	}

	@Override
	public int getMinutes() {
		return hour*60+min;
	}

	int hour;
	int min;
	@Override
	public void stateChanged(ChangeEvent e) {
		if (e.getSource() instanceof JSpinner) {
			JSpinner spinner = (JSpinner) e.getSource();
			double value = (Double)spinner.getValue();
			this.hour=(int) Math.floor(value);
			this.min= (int) ((value - hour)*60);
			this.fireZeitHasChanged();
		}
		
	}

}
