package rocketview;

import cansocket.*;

import java.awt.Font;
import java.util.*;
import javax.swing.*;

/*----------------------------------------------------------------
 * Prints all message to the message text box
 */
class TextObserver extends JTextArea implements Observer
{

    public TextObserver() {

	// construct a JTextArea
	super( 30, 40 ); // row, column
	this.setLineWrap( true );
	this.setFont( new Font( "Monospaced", Font.PLAIN, 10 ));
    }

    public void update(Observable o, Object arg)
    {
	if (!(arg instanceof CanMessage))
		return;
			
	CanMessage msg = (CanMessage) arg;

	// filter out all id's that are handled elsewhere
	switch(msg.getId11())
	{
		case CanBusIDs.FC_REPORT_STATE >> 5:
		case CanBusIDs.IMU_ACCEL_DATA >> 5:
		case CanBusIDs.IMU_GYRO_DATA >> 5:
		case CanBusIDs.PRESS_REPORT_DATA >> 5:
		case CanBusIDs.TEMP_REPORT_DATA >> 5:
		case CanBusIDs.PWR_REPORT_VOLTAGE >> 5:
		case CanBusIDs.PWR_REPORT_CURRENT >> 5:
		case CanBusIDs.PWR_REPORT_CHARGE >> 5:
			return;
	}

	append( msg.toString() );
	append( "\n" );
    }
}
