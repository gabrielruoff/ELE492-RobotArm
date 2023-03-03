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
                if (c > 1000) {
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
                    //System.out.println("CRC GOOD");
                }
                else {
//                    System.out.println("CRC BAD");
                    throw new Exception("BAD CRC");
                }
		return inputPackets;
	}

}
