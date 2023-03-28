package lib;
import java.io.IOException;
import java.io.InputStream;

import com.fazecast.jSerialComm.SerialPort;

public class Arduino {
	private static final String PORT_ID = "Arduino Uno";
	private static final String OSX_PORT_PATH = "/dev/cu.usbmodem11301";
	public static final int MAX_READ_ATTEMPTS = 5000;
	public static final int WRITE_FREQUENCY = 20;
	public static final Packet idlePosition = new Packet(new byte[] {90,97,0,90,90,(byte)180,(byte)180,(byte)180,(byte)180,(byte)180});
	public Packet floatingTarget = new Packet(new byte[] {90,97,0,90,90,(byte)180,(byte)180,(byte)180,(byte)180,(byte)180});
	public SerialPort serialPort;
	public InputStream inputStream;
	public Packet oldPacket;

	public Arduino(SerialPort serialPort) {
		this.oldPacket = idlePosition;
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
	
	public void writePacketConstantSpeed(Packet packet, float velocity) throws Exception {
		System.out.println(oldPacket.toString()+" -> "+packet.toString());
		if(packet.equals(oldPacket))
			return;
		float steps[] = new float[Packet.PACKET_LENGTH];
//		System.out.print("steps: ");
		for(int i=0;i<steps.length;i++)
		{
			float diff = byteToInt(packet.positions[i])-byteToInt(oldPacket.positions[i]);
			steps[i] = velocity*(getSign(diff))/WRITE_FREQUENCY;
//			System.out.print(steps[i]+", ");
		}
//		System.out.println();
		while(!done(steps)) {
			for(int i=0;i<steps.length;i++) {
//				System.out.println("incrementing j"+i+" by "+steps[i]+" ("+oldPacket.positions[i]+"->"+(oldPacket.positions[i]+steps[i])+")");
				if(steps[i]==0)
					continue;
				// if close enough, snap position
				if(Math.abs(packet.positions[i]-oldPacket.positions[i]) < Math.abs(steps[i])) {
//					System.out.println("snapping j"+i+" to final position "+packet.positions[i]);
					oldPacket.positions[i] = packet.positions[i];
					steps[i] = 0;
				} else {
					oldPacket.positions[i] += steps[i];
				}
				System.out.println("writing "+oldPacket.toString());
				this.write(oldPacket.compile());
				byte rpacket[] = this.listenForAndReadPacket();
			}
			Thread.sleep(1000/WRITE_FREQUENCY);
		}
		System.out.println("move done");
		java.lang.System.arraycopy(packet.positions, 0, oldPacket.positions, 0, Packet.PACKET_LENGTH);
	}
	
	public void moveToFloatingTarget(float velocity, boolean sim) throws Exception {
//		System.out.println(oldPacket.toString()+" -> "+floatingTarget.toString());
		if(floatingTarget.equals(oldPacket))
			return;
		float steps[] = new float[Packet.PACKET_LENGTH];
//		System.out.print("steps: ");
		for(int i=0;i<steps.length;i++)
		{
			float diff = byteToInt(floatingTarget.positions[i])-byteToInt(oldPacket.positions[i]);
			steps[i] = velocity*(getSign(diff))/WRITE_FREQUENCY;
			if(steps[i]==0)
				continue;
			if(Math.abs(floatingTarget.positions[i]-oldPacket.realPositions[i]) < Math.abs(steps[i])) {
//				System.out.println("snapping j"+i+" to final position "+packet.positions[i]);
				oldPacket.realPositions[i] = floatingTarget.realPositions[i];
				steps[i] = 0;
			} else {
				oldPacket.realPositions[i] += steps[i];
			}
//			System.out.print(steps[i]+", ");
		}
//		System.out.println();
//		System.out.println("writing "+oldPacket.toString());
		if(!sim) {
			this.write(oldPacket.compile());
			byte rpacket[] = this.listenForAndReadPacket();
		}
		Thread.sleep(1000/WRITE_FREQUENCY);
	}
	
	private int getSign(float f) {
		if(f>0)
			return 1;
		else if(f<0)
			return -1;
		return 0;
	}
	
	private boolean done(float steps[]) {
		for(int i=0;i<steps.length;i++) {
			if(steps[i]!=0)
				return false;
		}
		return true;
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

	public Packet getFloatingTarget() {
		return floatingTarget;
	}

	public void setFloatingTarget(Packet floatingTarget) {
		this.floatingTarget = floatingTarget;
	}

}
