import java.io.IOException;
import java.io.InputStream;

import com.fazecast.jSerialComm.SerialPort;
import java.util.Arrays;

public class Arduino {
	private static final String PORT_ID = "Arduino Uno";
	private static final String OSX_PORT_PATH = "/dev/cu.usbmodem11301";
        private static final int MAX_READ_ATTEMPTS = 1000;
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

	public void writePacket(Packet packet) {
		this.write(packet.compile());
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
                byte calcCRC = crosscheck.getCRC();
                //System.out.println("Got CRC: "+readCRC+" Calced CRC: "+calcCRC);
                return readCRC == calcCRC;
	}

	public byte[] listenForAndReadPacket() throws Exception {
                this.waitForPacketStart();
		byte inputPackets[] = this.digestPacket();
                this.waitForPacketStop();
                if (validateCRC(inputPackets)) {
                    System.out.println("CRC GOOD");
                }
                else {
                    System.out.println("CRC BAD");
                }
		return inputPackets;
	}
        
        public void sendCommand(Packet packet, int steps, int delay) throws Exception {
		int[] deltas = new int[Packet.PACKET_LENGTH];
                //System.out.println(Arrays.toString(packet.positions));
                //System.out.println(Arrays.toString(prevPacket.positions));
                if (!Arrays.equals(packet.positions, prevPacket.positions)) {
                    //This current implementation does not work without utilizing rounding (Math.Round() etc) which will slow down procesisng
                    //Branching off for implementaiton with full 180 degree sweep each time isntea dof step-wise sweep
                    for(int i=0;i<deltas.length;i++) {
                        deltas[i] = ( ((int)packet.positions[i] & 0xFF) - ((int)prevPacket.positions[i] & 0xFF) ) / steps;
                    }
            
                    for(int i=0;i<steps;i++) {
                        for(int j=0;j<Packet.PACKET_LENGTH;j++) {
                            packet.positions[j] = (byte)(((int)prevPacket.positions[j])+(i*deltas[j]));
                        }
                        System.out.println("sending packet: " + packet.toString());
                        this.write(packet.compile());
                        System.out.println("wrote packet. listening..");
                        byte rpacket[] = this.listenForAndReadPacket();
                        System.out.print("got packet back: ");
                        for (byte b : rpacket) {
                            System.out.print((b & 0xFF) + ", ");
                        }
                        System.out.println();
                        Thread.sleep(delay);
                    }
                }
                else {
                    System.out.println("Packet same as previous");
                    System.out.println("sending packet: " + packet.toString());
                    this.write(packet.compile());
                    System.out.println("wrote packet. listening..");
                    byte rpacket[] = this.listenForAndReadPacket();
                    System.out.print("got packet back: ");
                    for (byte b : rpacket) {
                        System.out.print((b & 0xFF) + ", ");
                    }
                    System.out.println();
                }
                System.arraycopy(prevPacket.positions, 0, packet.positions, 0, 10);
	}
        
        public void sendCommand(Packet packet) throws Exception {
            System.out.println("packet: " + packet.toString());
            this.write(packet.compile());
            System.out.println("wrote packet. listening..");
            byte rpacket[] = this.listenForAndReadPacket();
            System.out.println("got packet back: ");
            for (byte b : rpacket) {
                System.out.print((b & 0xFF) + ", ");
            }
            System.out.println();
            prevPacket = packet;
        }
}
