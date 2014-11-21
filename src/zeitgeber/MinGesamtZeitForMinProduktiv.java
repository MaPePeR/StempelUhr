package zeitgeber;

import data.Config;
import data.ZeitListener;

public class MinGesamtZeitForMinProduktiv extends AZeit implements ZeitListener {

	private AZeit minProduktivForPlus;
	private AZeit abwesenheit;
	public MinGesamtZeitForMinProduktiv(AZeit minProduktivForPlus,AZeit abwesenheit) {
		this.minProduktivForPlus = minProduktivForPlus;
		this.minProduktivForPlus.addZeitListener(this);
		this.abwesenheit = abwesenheit;
		this.abwesenheit.addZeitListener(this);
		timeHasChanged();
	}
	private int hour;
	private int min;
	@Override
	public void timeHasChanged() {
		Config.ZeitAbschnitt[] a = Config.getAbschnitte();
		
		AZeit gesamtZeit=new Zeit(0);
		AZeit restProduktivZeit = minProduktivForPlus;
		AZeit restAbwesenheit = abwesenheit;
		
		for(int i=0;i<a.length;i++)
		{
			if (a[i].arbeit) {
				if (restProduktivZeit.getMinutes() > a[i].dauer.getMinutes()) {
					restProduktivZeit= restProduktivZeit.sub(a[i].dauer);
					gesamtZeit = gesamtZeit.add(a[i].dauer);
				} else {
					gesamtZeit = gesamtZeit.add(restProduktivZeit);
					break;
				}
			} else {
				if (a[i].dauer.laenger(restAbwesenheit)) {
					restAbwesenheit = new Zeit(0,0);
				} else {
					restAbwesenheit = restAbwesenheit.sub(a[i].dauer);
				}
				gesamtZeit = gesamtZeit.add(a[i].dauer);
			}
		}
		gesamtZeit = gesamtZeit.add(restAbwesenheit);
		this.hour = gesamtZeit.getHour();
		this.min = gesamtZeit.getMin();
		this.fireZeitHasChanged();
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

}
