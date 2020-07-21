package main;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import jp.vstone.RobotLib.CRobotMotion;
import led.LedController;
import led.LedConverter;
import led.LedConverter_CommU;
import led.LedConverter_Sota;
import led.LedServer;
import pose.AxisController;
import pose.PoseConverter;
import pose.PoseConverter_CommU;
import pose.PoseConverter_Sota;
import pose.PoseServer;
import wav.SpeechPlayer;
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

		// WavServer
		SpeechPlayer speechPlayer = new SpeechPlayer();
		WavServer wavServer = new WavServer(Params.wavPort, speechPlayer);
		ex.execute(wavServer);

		// PoseServer
		AxisController axisController = new AxisController(motion);
		PoseConverter poseConverter = null;
		if      (Params.robotType.equals("CommU")) poseConverter = new PoseConverter_CommU();
		else if (Params.robotType.equals("Sota"))  poseConverter = new PoseConverter_Sota();
		PoseServer poseServer = new PoseServer(Params.posePort, axisController, poseConverter);
		ex.execute(poseServer);

		// LedServer
		LedController ledController = new LedController(motion);
		LedConverter ledConverter = null;
		if      (Params.robotType.equals("CommU")) ledConverter = new LedConverter_CommU();
		else if (Params.robotType.equals("Sota"))  ledConverter = new LedConverter_Sota();
		LedServer ledServer = new LedServer(Params.ledPort, ledController, ledConverter);
		ex.execute(ledServer);
	}

}
