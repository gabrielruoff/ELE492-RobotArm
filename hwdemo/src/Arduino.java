import java.io.IOException;
import java.io.InputStream;

import com.fazecast.jSerialComm.SerialPort;

public class Arduino {
	private static final String PORT_ID = "Arduino Uno";
	private static final String OSX_PORT_PATH = "/dev/cu.usbmodem11301";
	public static final int MAX_READ_ATTEMPTS = 5000;
	public SerialPort serialPort;
	public InputStream inputStream;
	public Packet prevPacket;

	public Arduino(SerialPort serialPort) {
		this.prevPacket = new Packet(Packet.startingPositions);
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

	@Deprecated
	public void writePacket(Packet packet) {
		this.write(packet.compile());
	}
	
	public void writePacketVarSpeed(Packet packet, int steps, int delay) throws Exception {
		System.out.println(oldPacket.toString()+" -> "+packet.toString());
		if(packet.equals(oldPacket))
			return;
		float deltas[] = new float[Packet.PACKET_LENGTH];
		int deltaSum = 0;
//		System.out.print("deltas: ");
		for(int i=0;i<deltas.length;i++)
		{
//			System.out.println(byteToInt(packet.positions[i])+"-"+byteToInt(oldPacket.positions[i])+")/(float)"+steps);
			deltas[i] = (byteToInt(packet.positions[i])-byteToInt(oldPacket.positions[i]))/(float)steps;
//			System.out.print(deltas[i]+", ");
			deltaSum+=Math.abs(deltas[i]);
		}
		System.out.println();
		for(int i=1;i<=steps;i++)
		{
			for(int j=0;j<Packet.PACKET_LENGTH;j++)
			{
				packet.positions[j] = (byte)(oldPacket.positions[j]+(i*deltas[j]));
			}
			System.out.print("Step #"+i+" ");
			System.out.println("packet: "+packet.toString());
			this.write(packet.compile());
//			System.out.println("wrote packet. listening..");
			byte rpacket[] = this.listenForAndReadPacket();
//			System.out.println("got packet back: ");
//			for (byte b : rpacket) {
//				System.out.print((b & 0xFF) + ", ");
//			}
//			System.out.println();
			Thread.sleep(delay);
		}
		java.lang.System.arraycopy(packet.positions, 0, oldPacket.positions, 0, Packet.PACKET_LENGTH);
//		this.write(packet.compile());
	}

	public void waitForPacketStart() throws IOException {
            while (true) {
		if (inputStream.available() > 0 && (byte)inputStream.read() == Packet.START) {
                    return;
                }	
            }
	}

	public void waitForPacketStop() throws Exception {
            int c = 0;
            while (true) {
		c++;
                if (inputStream.available() > 0 && (byte)inputStream.read() == Packet.STOP) {
                    break;
                }
                if (c > MAX_READ_ATTEMPTS) {
                    throw new Exception("Expected packed stop");
                }
            }
	}

	public byte[] digestPacket() throws IOException {
		byte inputPackets[] = new byte[Packet.PACKET_LENGTH+1];
                for (int i = 0; i < Packet.PACKET_LENGTH+1; i++) {
                    while(true) {
                        if (inputStream.available() > 0) {
                            inputPackets[i] = (byte)inputStream.read();
                            break;
                        }
                    }
                }
		return inputPackets;
	}
        
        public boolean validateCRC(byte inputPackets[]) throws IOException {
		byte readCRC = inputPackets[10];
                byte packetData[] = new byte[10];
                System.arraycopy(inputPackets, 0, packetData, 0, 10);
                Packet crosscheck  = new Packet(packetData);
                crosscheck.setCRC();
                byte calcCRC = crosscheck.getCRC();
//                System.out.println("Got CRC: "+readCRC+" Calced CRC: "+calcCRC);
                return readCRC == calcCRC;
	}

	public byte[] listenForAndReadPacket() throws Exception {
                this.waitForPacketStart();
		byte inputPackets[] = this.digestPacket();
                this.waitForPacketStop();
                if (validateCRC(inputPackets)) {
                    //System.out.println("CRC GOOD");
                }
                else {
//                    System.out.println("CRC BAD");
                    throw new Exception("BAD CRC");
                }
		return inputPackets;
	}
	
	private int byteToInt(byte b)
	{
		return (b & 0xFF);
	}

}
