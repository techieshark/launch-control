package widgets;

import cansocket.*;

import java.text.*;
import java.util.*;
import javax.swing.*;

public class RocketState extends JLabel implements Observer
{
	protected static final String[] stateStrings = {
		"EvaluatePowerup",
		"Initialize",
		"Idle",
		"PreflightCheck",
		"Ready",
		"Armed",
		"RocketReady",
		"LaunchAbort",
		"Boost",
		"Coast",
		"DeployDrogue",
		"DescendDrogue",
		"DeployMain",
		"DescendMain",
		"RecoveryWait",
		"RecoverySleep",
		"PowerDown",
		"LawnDart",
	};

	protected static final DateFormat df = new SimpleDateFormat("HH:mm:ss.SSS: ");

	protected ImageIcon greenled = new ImageIcon("src/widgets/greenled.png");
	protected ImageIcon redled = new ImageIcon("src/widgets/redled.png");

	protected int state = -1;
	protected int detail = -1;

	protected static final long delay = 1200; /* link timeout delay (millisecs) */
	protected final java.util.Timer timer = new java.util.Timer(true /*daemon*/);
	protected LinkStateChecker task;

	protected class LinkStateChecker extends TimerTask
	{
		public void run()
		{
			// If the task is activated, then it has been too long since
			// we recieved a message
			setIcon(redled);
		}
	}


	public RocketState()
	{
		System.out.println("green width: " + greenled.getIconWidth());
		updateText();
	}

	public void update(NetMessage msg)
	{
		update(null, msg);
	}


	public void update(Observable o, Object arg)
	{
		if (!(arg instanceof CanMessage))
			return;

		update((CanMessage) arg);
	}

	public void update(CanMessage msg)
	{
		switch(msg.getId11())
		{
			case CanBusIDs.FC_REPORT_STATE >> 5:
				setState((int) msg.getData8(0) & 0xff);
				break;
			case CanBusIDs.FC_REPORT_STATE_DETAIL >> 5:
				setDetail(msg.getData32(0));
				break;
		}
	}

	protected void setState(int state)
	{
		if(this.state != state)
			detail = -1;
		this.state = state;
		updateText();
	}

	protected void setDetail(int detail)
	{
		this.detail = detail;
		updateText();
	}

	protected void updateText()
	{
		if(state < 0)
		{
			setText("unknown");
			setIcon(redled);
			return;
		}

		StringBuffer b = new StringBuffer(df.format(new Date()));
		if(state >= stateStrings.length)
			b.append("unknown (").append(state).append(")");
		else
			b.append(stateStrings[state]);
		if(detail >= 0)
			b.append(" [").append(Integer.toBinaryString(detail)).append("]");
		setText(b.toString());

		// set a task to run after delay milliseconds, which will happen
		// unless we've recieved a message (and thus entered this function)
		// before that time
		setIcon(greenled);
		if (task != null)
			task.cancel();
		task = new LinkStateChecker(); // will set "led" to red
		timer.schedule(task, delay);

	}
}