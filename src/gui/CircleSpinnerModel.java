package gui;
import javax.swing.SpinnerNumberModel;


public class CircleSpinnerModel extends SpinnerNumberModel
{
	private static final long serialVersionUID = 8723504631967916079L;
	private final int min, max;
	private final int diff;
	private int value;
	public CircleSpinnerModel(int min, int max)
	{
		this.min=min;
		this.max=max;
		this.diff=max-min;
		if(diff<0)
			throw new IllegalArgumentException("min has to be smaller than max!");
		value = min;
	}
	@Override
	public Object getValue()
	{
		return value;
	}

	@Override
	public void setValue(Object value)
	{
		int v = (Integer) value;
		if(min<=v&&v<=max)
		{
			this.value=v;
			
		}
		this.fireStateChanged();
	}

	@Override
	public Object getNextValue()
	{
		return this.value>=this.max?this.min:this.value+1;
	}

	@Override
	public Object getPreviousValue()
	{
		return this.value<=this.min?this.max:this.value-1;
	}


}
