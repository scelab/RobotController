package main;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import jp.vstone.RobotLib.CRobotMotion;
import led.LedController;
import led.LedConverter;
import led.LedConverter_CommU;
import led.LedConverter_Sota;
import led.LedData;
import led.LedServer;
import pose.AxisController;
import pose.PoseConverter;
import pose.PoseConverter_CommU;
import pose.PoseConverter_Sota;
import pose.PoseData;
import pose.PoseServer;
import reader.AxisReader;
import reader.ReadServer;
import wav.SpeechPlayer;
import wav.WavData;
import wav.WavServer;

public class App {
	public static void main(String[] args) {
		// Servo init
		CRobotMotion motion = ServoInit.getInstanceOfCRobotMotion(Params.robotType);
		if (motion == null) {
			System.err.println("Servo was not initialized.");
			System.exit(0);
		}

		ExecutorService ex = Executors.newCachedThreadPool();
		BlockingQueue<Data4Q> q = new LinkedBlockingQueue<Data4Q>();

		// WavServer
		SpeechPlayer speechPlayer = new SpeechPlayer();
		WavServer wavServer = new WavServer(Params.wavPort, q);
		ex.execute(wavServer);

		// PoseServer
		AxisController axisController = new AxisController(motion);
		PoseConverter poseConverter = null;
		if      (Params.robotType.equals("CommU")) poseConverter = new PoseConverter_CommU();
		else if (Params.robotType.equals("Sota"))  poseConverter = new PoseConverter_Sota();
		PoseServer poseServer = new PoseServer(Params.posePort, poseConverter, q);
		ex.execute(poseServer);

		// LedServer
		LedController ledController = new LedController(motion);
		LedConverter ledConverter = null;
		if      (Params.robotType.equals("CommU")) ledConverter = new LedConverter_CommU();
		else if (Params.robotType.equals("Sota"))  ledConverter = new LedConverter_Sota();
		LedServer ledServer = new LedServer(Params.ledPort, ledConverter, q);
		ex.execute(ledServer);

		// ReadServer
		AxisReader axisReader = new AxisReader(motion);
		if      (Params.robotType.equals("CommU")) poseConverter = new PoseConverter_CommU();
		else if (Params.robotType.equals("Sota"))  poseConverter = new PoseConverter_Sota();
		ReadServer readServer = new ReadServer(Params.readPort, axisReader, poseConverter);
		ex.execute(readServer);

		// Event loop
		try {
			while (true) {
				Data4Q d = q.take();
				switch (d.getType()) {
				case "PoseData":
					axisController.set(((PoseData) d).msec, ((PoseData) d).map);
					continue;
				case "LedData":
					ledController.set(((LedData) d).msec, ((LedData) d).map);
					continue;
				case "WabData":
					speechPlayer.play(((WavData) d).path);
					continue;
				}
			}
		} catch (InterruptedException e) {
				e.printStackTrace();
		}
	}
}
