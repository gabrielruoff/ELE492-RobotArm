import java.io.IOException;
import java.io.InputStream;

import com.fazecast.jSerialComm.SerialPort;

public class Arduino {
	private static final String PORT_ID = "Arduino Uno";
	private static final String OSX_PORT_PATH = "/dev/cu.usbmodem11301";
	public SerialPort serialPort;
	public InputStream inputStream;

	public Arduino(SerialPort serialPort) {
		this.serialPort = serialPort;
	}

	public static SerialPort detectArduino() throws Exception {
		SerialPort ports[] = SerialPort.getCommPorts();
		for (SerialPort port : ports) {
			System.out.println(port.getSystemPortName());
			if (port.getDescriptivePortName().startsWith(PORT_ID) || port.getSystemPortPath().equals(OSX_PORT_PATH)) {
				return port;
			}
		}
		throw new Exception("Unable to detect Arduino");
	}

	public void open(int baudRate) throws InterruptedException {
		serialPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
		serialPort.setBaudRate(baudRate);
		serialPort.openPort();
		Thread.sleep(3000);
		this.inputStream = serialPort.getInputStream();
	}

	public void write(byte[] message) {
		serialPort.writeBytes(message, message.length);
	}

	public void writePacket(Packet packet) {
		this.write(packet.compile());
	}

	public void waitForPacketStart() throws IOException {
		while (true) {
			if (inputStream.available() == 0)
				continue;
			if (inputStream.read() != Packet.START)
				return;
		}
	}

	public boolean checkForPacketStop() throws IOException {
		while (inputStream.available() == 0)
			;
		return inputStream.read() == Packet.STOP;
	}

	public byte[] digestPacket() throws IOException {
		byte packet[] = new byte[Packet.PACKET_LENGTH];
		while (inputStream.available() < Packet.PACKET_LENGTH);
		for (int i = 0; i < Packet.PACKET_LENGTH; i++) {
			packet[i] = (byte) inputStream.read();
		}
		return packet;
	}

	public byte[] listenForAndReadPacket() throws Exception {
		this.waitForPacketStart();
		byte packet[] = this.digestPacket();
		if (!this.checkForPacketStop())
			throw new Exception("Expected packed stop");
		return packet;
	}

}
