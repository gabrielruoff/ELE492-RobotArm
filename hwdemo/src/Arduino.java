import java.io.InputStream;

import com.fazecast.jSerialComm.SerialPort;

public class Arduino {
	private static final String PORT_ID = "Arduino Uno";
	private static final int BAUD_RATE = 9600;
	private SerialPort serialPort;
	
	public Arduino(SerialPort serialPort)
	{
		this.serialPort = serialPort;
	}
	
	public static SerialPort detectArduino() throws Exception
	{
		SerialPort ports[] = SerialPort.getCommPorts();
		for(SerialPort port : ports)
		{
			if(port.getDescriptivePortName().startsWith(PORT_ID))
			{
				return port;
			}
		}
		throw new Exception("Unable to detect Arduino");
	}
	
	public void open() throws InterruptedException
	{
		serialPort.openPort();
		Thread.sleep(1000);
		serialPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
		serialPort.setBaudRate(BAUD_RATE);
	}
	
	public void write(byte[] message)
	{
		serialPort.writeBytes(message, message.length);
	}
	
	public void writePacket(Packet packet)
	{
		this.write(packet.compile());
	}
	
	public InputStream getInputStream()
	{
		return serialPort.getInputStream();
	}

}
