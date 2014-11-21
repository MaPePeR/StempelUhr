package gui;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

import zeitgeber.HoverZeit;
import zeitgeber.SpinnerZeit;
import zeitgeber.Uhrzeit;
import data.Config;


public class Dialog extends JFrame implements ItemListener,ActionListener, MouseWheelListener
{

	private static final long serialVersionUID = -1735790613959414473L;

	/*JSpinner+Model fuer Stunden+Minuten eingabe.*/
	CircleSpinnerModel hour_spin_model = new CircleSpinnerModel(0, 23);
	JSpinner hour_spin = new JSpinner(hour_spin_model);
	CircleSpinnerModel min_spin_model = new CircleSpinnerModel(0, 59);
	JSpinner min_spin = new JSpinner(min_spin_model);
	/*Wandelt den Inhalt der JSpinner in ein Zeit-Objekt*/
	SpinnerZeit spinnerZeit = new SpinnerZeit(hour_spin, min_spin);
	
	/*Wird geteilt*/
	Uhrzeit uhr = new Uhrzeit();
	
	
	
	/*Liefert die markierte Stunde auf der Anzeige oder die aktuelle Uhrzeit*/
	HoverZeit hoverZeit = new HoverZeit(uhr);

//	AWorkProgress progress = new ClockWorkProgress(spinnerZeit,uhr,hoverZeit);
	AWorkProgress progress2 = new MinimalWorkProgress(spinnerZeit, uhr, hoverZeit);

	
	/*Checkbox aktiviert Timer, Timer oder Button startet Aktualisierung von Rechnung.*/
	JButton aktualisiere = new JButton("\u21bb");
	JCheckBox auto = new JCheckBox("auto",true);
	Timer timer = new Timer(60*1000,this);
	
	
	SpinnerNumberModel ziel_zeit_model = new SpinnerNumberModel(Config.minProduktivForPlus.getMinutes()/60.0,0,12,0.05);
	JSpinner ziel_zeit = new JSpinner(ziel_zeit_model);
	
	SpinnerNumberModel abwesenheit_zeit_model = new SpinnerNumberModel(Config.abwesenheitZeit.getMinutes()/60.0,0,12,5.0/60);
	JSpinner abwesenheit_zeit_spinner = new JSpinner(abwesenheit_zeit_model);
	
	JPanel spinContainer = new JPanel();
	JPanel controlContainer = new JPanel();
	
	ZeitRechner rechner = new ZeitRechner(spinnerZeit,hoverZeit,uhr, Config.abwesenheitZeit);
	
	public Dialog()
	{
		super("StempelUhr");
		
		try {
			Toolkit xToolkit = Toolkit.getDefaultToolkit();
			java.lang.reflect.Field awtAppClassNameField =
			    xToolkit.getClass().getDeclaredField("awtAppClassName");
			awtAppClassNameField.setAccessible(true);
			awtAppClassNameField.set(xToolkit, "StempelUhr");
		} catch (Exception e) {}
        
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLayout(new BorderLayout());
		
		aktualisiere.setToolTipText("Aktualisiere Zeit");
		auto.setToolTipText("Aktualisiere Zeit automatisch");
		hour_spin.setToolTipText("Stempelzeit setzen (Stunde)");
		min_spin.setToolTipText("Stempelzeit setzen (Minute)");
		ziel_zeit.setToolTipText("Angestrebte Produktivzeit");
		abwesenheit_zeit_spinner.setToolTipText("Abwesenheitszeit");
		
		controlContainer.setLayout(new BorderLayout());
		
		spinContainer.add(hour_spin);
		spinContainer.add(min_spin);
		spinContainer.add(aktualisiere);
		spinContainer.add(auto);
		spinContainer.add(ziel_zeit);
		spinContainer.add(abwesenheit_zeit_spinner);
//		
		
//		this.add(progress,BorderLayout.CENTER);
//		progress.addMouseListener(hoverZeit);
//		progress.addMouseMotionListener(hoverZeit);
		
		this.add(progress2,BorderLayout.SOUTH);
		progress2.addMouseListener(hoverZeit);
		progress2.addMouseMotionListener(hoverZeit);
		
		controlContainer.add(spinContainer,BorderLayout.NORTH);
		controlContainer.add(rechner,BorderLayout.CENTER);
		
	
		//this.add(controlContainer,BorderLayout.EAST);
		this.add(controlContainer,BorderLayout.CENTER);
		//((DefaultEditor) hour_spin.getEditor()).getTextField().setEditable(true);
		//((DefaultEditor) min_spin.getEditor()).getTextField().setEditable(true);
		
        hour_spin.setEditor(new JSpinner.NumberEditor(hour_spin, "00"));
        min_spin.setEditor(new JSpinner.NumberEditor(min_spin, "00"));
        ziel_zeit.setEditor(new JSpinner.NumberEditor(ziel_zeit, "0.00"));
        abwesenheit_zeit_spinner.setEditor(new JSpinner.NumberEditor(abwesenheit_zeit_spinner, "0.00"));
        hour_spin.addMouseWheelListener(this);
        min_spin.addMouseWheelListener(this);
        ziel_zeit.addMouseWheelListener(this);
        abwesenheit_zeit_spinner.addMouseWheelListener(this);

		ziel_zeit.addChangeListener(Config.minProduktivForPlus);
		abwesenheit_zeit_spinner.addChangeListener(Config.abwesenheitZeit);
		
		auto.addItemListener(this);
		this.itemStateChanged(null);
	
        final Font fnt = new Font("Arial", Font.PLAIN, 12);
        final FontUIResource res = new FontUIResource(fnt);
        UIManager.getLookAndFeelDefaults().put("Button.font", res);
        UIManager.getLookAndFeelDefaults().put("TextField.font", res);
        UIManager.getLookAndFeelDefaults().put("Label.font", res);
        UIManager.getLookAndFeelDefaults().put("CheckBox.font", res);
        SwingUtilities.updateComponentTreeUI(this);
        
		this.setLocation(200, 200);
		this.pack();
		this.setVisible(true);
//		this.setResizable(false);
		
		
		//progress.addMouseMotionListener(hoverZeit);
		hour_spin.setValue(uhr.getHour());
		min_spin.setValue(uhr.getMin());
		
		aktualisiere.addActionListener(this);
	}
	@Override
	public void actionPerformed(ActionEvent e)
	{
		uhr.aktualisiere();
	}
		

	@Override
    public void itemStateChanged(ItemEvent e) {
		if(auto.isSelected())
		{
			final int restzeit = (int) (60*1000-(System.currentTimeMillis())%60000);
			this.actionPerformed(null);
			timer.setInitialDelay(restzeit+500);
			timer.start();
		}
		else
			timer.stop();
	}

	public static void main(String[ ] s )
	{
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run()
			{
				new Dialog();
				
			}
		});
	}
	@Override
	public void mouseWheelMoved(MouseWheelEvent e)
	{
		if(e.getSource() instanceof JSpinner)
		{
			SpinnerModel mod=((JSpinner)e.getSource()).getModel();
			Object val;
			if(e.getWheelRotation()>0)
			{
				val = mod.getPreviousValue();
			}
			else
			{
				val = mod.getNextValue();
			}
			if (mod instanceof SpinnerNumberModel) {
				SpinnerNumberModel numberMod = (SpinnerNumberModel)mod;
				Comparable max = numberMod.getMaximum();
				Comparable min = numberMod.getMinimum();
				if ((val == null && e.getWheelRotation()<0)||(val != null && max != null && max.compareTo(val) < 0)) {
					val = max;
				}
				if ((val == null && e.getWheelRotation()>0)||(val != null && min != null && min.compareTo(val) > 0)) {
					val = min;
				}
			}
			mod.setValue(val);
		}
	}
	
}
