package gui;
import java.awt.BorderLayout;
import java.awt.Font;
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
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

import zeitgeber.HoverZeit;
import zeitgeber.SpinnerZeit;
import zeitgeber.Uhrzeit;


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

	AWorkProgress progress = new ClockWorkProgress(spinnerZeit,hoverZeit);

	/*Checkbox aktiviert Timer, Timer oder Button startet Aktualisierung von Rechnung.*/
	JButton aktualisiere = new JButton("Aktualisieren");
	JCheckBox auto = new JCheckBox("auto",false);
	Timer timer = new Timer(60*1000,this);
	ZeitRechner rechner = new ZeitRechner(spinnerZeit,hoverZeit);
	
	
	JPanel spinContainer = new JPanel();
	JPanel controlContainer = new JPanel();
	
	public Dialog()
	{
		super("StempelUhr");
		
		

        
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLayout(new BorderLayout());
		
		

		controlContainer.setLayout(new BorderLayout());
		
		spinContainer.add(hour_spin);
		spinContainer.add(min_spin);
		spinContainer.add(aktualisiere);
		spinContainer.add(auto);
//		
		AWorkProgress progress2 = new MinimalWorkProgress(spinnerZeit, hoverZeit);
		this.add(progress2,BorderLayout.SOUTH);
		progress2.addMouseListener(hoverZeit);
		progress2.addMouseMotionListener(hoverZeit);
		
		controlContainer.add(spinContainer,BorderLayout.NORTH);
		controlContainer.add(rechner,BorderLayout.CENTER);
		
		this.add(progress,BorderLayout.CENTER);
		this.add(controlContainer,BorderLayout.EAST);
		//((DefaultEditor) hour_spin.getEditor()).getTextField().setEditable(true);
		//((DefaultEditor) min_spin.getEditor()).getTextField().setEditable(true);
		
        hour_spin.setEditor(new JSpinner.NumberEditor(hour_spin, "00"));
        min_spin.setEditor(new JSpinner.NumberEditor(min_spin, "00"));
        hour_spin.addMouseWheelListener(this);
        min_spin.addMouseWheelListener(this);
		progress.addMouseListener(hoverZeit);
		progress.addMouseMotionListener(hoverZeit);
		
		auto.addItemListener(this);
	
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
			if(e.getWheelRotation()>0)
			{
				mod.setValue(mod.getPreviousValue());
			}
			else
			{
				mod.setValue(mod.getNextValue());
			}
		}
	}
	
}
