package cansocket;

import java.io.*;

public class ImuGyroMessage extends NetMessage
{
	public static final short fifo_tag = FMT_IMU_GYRO;
	public final double Pdot, Ydot, Rdot; 

	public ImuGyroMessage(DataInputStream dis) throws IOException
	{
		Pdot = dis.readDouble();
		Ydot = dis.readDouble();
		Rdot = dis.readDouble();
	}

	
	public void putMessage(DataOutputStream dos)
	{
		try
		{
			dos.writeDouble(Pdot);
			dos.writeDouble(Ydot);
			dos.writeDouble(Rdot);
		} catch(IOException e) {
			// never happens
		}
	}
}

