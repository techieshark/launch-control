package rocketview;

import cansocket.*;

import java.awt.Font;
import java.lang.reflect.*;
import javax.swing.*;

/*----------------------------------------------------------------
 * Prints all message to the message text box
 */
class TextObserver extends JTextArea implements CanObserver
{
	String msgSyms[];

    protected int discardLength(int id)
    {
	return (id >>> 4) & 0xfff;
    }

    public TextObserver(CanDispatch dispatch) throws IllegalAccessException {
	super( 15, 40 ); // row, column

	dispatch.add(this);

	// initialize the map of CAN msg symbols
	int i, msg;
	msgSyms = new String[0x1000];
	Field fields[] = CanBusIDs.class.getFields();
	for (i=0; i<fields.length; i++)
	{
		msg = discardLength(fields[i].getInt(null));
		msgSyms[msg] = fields[i].getName();
	}

	// construct a JTextArea
	this.setLineWrap( true );
	this.setFont( new Font( "Monospaced", Font.PLAIN, 10 ));
    }

    public void message(CanMessage msg)
    {
	int nid = msg.getId() & (0x1f << 11);
	switch(nid)
	{
		case CanBusIDs.FC_NID:
		case CanBusIDs.FC_IMU_NID:
		case CanBusIDs.FC_GPS_NID:
			return;
	}

	int verb = msg.getId() & ((0x3 << 8) | CanBusIDs.CID_REQUEST);
	switch(verb)
	{
		/* XXX: would like to filter CID_REPORT, but it's the
		 * same verb as CID_ERROR, so we can't. For the same
		 * reason, don't block CID_INFO or related. */
		case CanBusIDs.CID_ACTION_BC:
		case CanBusIDs.CID_TEST:
		case CanBusIDs.CID_SET:
		case CanBusIDs.CID_ACK:
		case CanBusIDs.CID_GET:
		case CanBusIDs.CID_DATA:
			return;
	}

	// filter out all id's that are handled elsewhere
	switch(msg.getId())
	{
		case CanBusIDs.REC_REPORT_MODE:
		case CanBusIDs.REC_REPORT_PYRO:
		case CanBusIDs.REC_REPORT_TIMER:
			return;
	}

	if (msgSyms[discardLength(msg.getId())] != null)
		append( msgSyms[discardLength(msg.getId())] + ": ");
	append( msg.toString() );
	append( "\n" );
	//Try to keep the scrollpane looking at the tail of the log
	JScrollPane scrollpane = (JScrollPane) getParent().getParent();
	final JScrollBar vertBar = scrollpane.getVerticalScrollBar();
	SwingUtilities.invokeLater(new Runnable() {
	  public void run() {
	    if (! vertBar.getValueIsAdjusting()) 
	      vertBar.setValue(vertBar.getMaximum());
	  }
	});
    }
}
