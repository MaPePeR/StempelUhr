package gui;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import zeitgeber.AZeit;
import zeitgeber.Zeit;
import data.Config;
import data.ZeitListener;


public class ZeitRechner extends JPanel implements ZeitListener
{
	private static final long serialVersionUID = -4921294151324453428L;
	AZeit hoverZeit;
	AZeit startZeit;
	AZeit uhr;
	AZeit abwesenheitZeit;
	private class ZeitPunktResult implements ZeitListener {
		public ZeitPunktResult(AZeit zeit) {
			this.zeit=zeit;
			this.zeit.addZeitListener(this);
		}
		AZeit zeit;
		JLabel jUhrzeit = new JLabel();
		JLabel jGesamtZeit = new JLabel();
		JLabel jProduktivZeit = new JLabel();
		JLabel jPausenZeit = new JLabel();
		JLabel jKontoVeraenderung = new JLabel();
		JLabel jRestZeit = new JLabel();
		@Override
		public void timeHasChanged() {
			recalculate(zeit, this);
		}
	}
	
	ZeitPunktResult hoverOrUhrResult;
	ZeitPunktResult markerResult;
	
	public ZeitRechner(AZeit startZeit,AZeit hoverZeit, AZeit uhr, AZeit abwesenheitZeit)
	{
		this.hoverZeit = hoverZeit;
		this.hoverZeit.addZeitListener(this);
		this.startZeit = startZeit;
		this.startZeit.addZeitListener(this);
		this.uhr = uhr;
		this.uhr.addZeitListener(this);
		this.abwesenheitZeit = abwesenheitZeit;
		this.abwesenheitZeit.addZeitListener(this);
		Config.minGesamtZeitForPlus.addZeitListener(this);
		Config.minProduktivForPlus.addZeitListener(this);
		this.setLayout(new GridBagLayout());
		
		this.hoverOrUhrResult = new ZeitPunktResult(hoverZeit);
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor=GridBagConstraints.LINE_START;
		gbc.ipadx = 10;
		gbc.fill=GridBagConstraints.HORIZONTAL;
		gbc.gridx=0;
		gbc.gridy=GridBagConstraints.RELATIVE;
		this.add(new JLabel("von - bis"),gbc);
		this.add(new JLabel("Gesamtzeit:"),gbc);
		this.add(new JLabel("Produktivzeit:"),gbc);
		this.add(new JLabel("Pausenzeit:"),gbc);
		this.add(new JLabel("Differenz:"),gbc);
		this.add(new JLabel("Restzeit bis Ziel:"),gbc);
		addResult(hoverOrUhrResult,1);
	}
	private void addResult(ZeitPunktResult r, int gridx) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor=GridBagConstraints.LINE_START;
		gbc.ipadx = 10;
		gbc.fill=GridBagConstraints.HORIZONTAL;
		gbc.gridy=GridBagConstraints.RELATIVE;
		gbc.gridx=gridx;
		gbc.weightx=1;
		this.add(r.jUhrzeit,gbc);
		this.add(r.jGesamtZeit,gbc);
		this.add(r.jProduktivZeit,gbc);
		this.add(r.jPausenZeit,gbc);
		this.add(r.jKontoVeraenderung,gbc);
		this.add(r.jRestZeit,gbc);
	}
	@Override
	public void timeHasChanged()
	{
		this.recalculate(hoverZeit.getZeit(),hoverOrUhrResult);
		if (markerResult != null) {
			this.recalculate(markerResult.zeit, markerResult);
		}
		
	}
	private String diffString(AZeit von, AZeit nach) {
		if (von.vor(nach)) {
			return "in " + nach.sub(von).toString();
		} else {
			return "vor " + von.sub(nach).toString();
		}
	}
	private void recalculate(AZeit zeit, ZeitPunktResult r)
	{
		AZeit gesamtZeit=startZeit.vonNach(zeit);;
		char kontoVeraenderungPrefix;
		AZeit kontoveraenderung;
		AZeit restZeit; 
		Config.ZeitAbschnitt[] a = Config.getAbschnitte();
		AZeit produktivZeit=new Zeit(0);
		AZeit pausenZeit=new Zeit(0);
		Zeit abschnittEnde=new Zeit(0);
		Zeit abschnittStart=new Zeit(0);
		for(int i=0;i<a.length;i++)
		{
			abschnittEnde=abschnittStart.add(a[i].dauer);
			if(gesamtZeit.compareTo(abschnittEnde)>0)
			{
				//Der aktuelle Abschnitt ist bereits vollendet
				if(a[i].arbeit)
					produktivZeit=produktivZeit.add(a[i].dauer);
				else
					pausenZeit=pausenZeit.add(a[i].dauer);
			}
			else 
			{
				//Der aktuelle Abschnitt ist nicht vollendet
				//Wir fuegen die zeit, die wir bereits in diesem abschnitt sind hinzu.
				if(a[i].arbeit)
					produktivZeit=produktivZeit.add(gesamtZeit.sub(abschnittStart));
				else
					pausenZeit=pausenZeit.add(gesamtZeit.sub(abschnittStart));
				//und beenden die Schleife
				break;
			}
			abschnittStart=abschnittEnde;
		}
		
		if (abwesenheitZeit.laenger(pausenZeit)) {
			AZeit abwesenheitAbzug = abwesenheitZeit.sub(pausenZeit);
			if (produktivZeit.laenger(abwesenheitAbzug)) {
				produktivZeit = produktivZeit.sub(abwesenheitAbzug);
			} else {
				produktivZeit = new Zeit(0,0);
			}
		}
		
		if(produktivZeit.compareTo(Config.minProduktivForPlus)<=0)
		{
			kontoVeraenderungPrefix='-';
			kontoveraenderung=Config.minProduktivForPlus.sub(produktivZeit);
		}
		else
		{
			kontoVeraenderungPrefix='+';
			kontoveraenderung=produktivZeit.sub(Config.minProduktivForPlus);
		}
		if(gesamtZeit.compareTo(Config.minGesamtZeitForPlus) <= 0) { 
			restZeit = Config.minGesamtZeitForPlus.sub(gesamtZeit);
		} else {
			restZeit = new Zeit(0);
		}
		
		r.jUhrzeit.setText(startZeit.toString()+" - "+hoverZeit.toString() + 
				((hoverZeit.compareTo(this.uhr) == 0) ? "" : " (" + diffString(uhr, hoverZeit) + ")"));
		r.jGesamtZeit.setText(gesamtZeit.getNormalAnd100());
		r.jProduktivZeit.setText(produktivZeit.getNormalAnd100());
		r.jPausenZeit.setText(pausenZeit.getNormalAnd100());
		r.jKontoVeraenderung.setText(kontoveraenderung.getNormalAnd100(""+kontoVeraenderungPrefix));
		r.jRestZeit.setText(restZeit.getNormalAnd100());
	}
}
