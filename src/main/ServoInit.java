package main;

import jp.vstone.RobotLib.CCommUMotion;
import jp.vstone.RobotLib.CRobotMem;
import jp.vstone.RobotLib.CRobotMotion;
import jp.vstone.RobotLib.CRobotPose;
import jp.vstone.RobotLib.CRobotUtil;
import jp.vstone.RobotLib.CSotaMotion;

public class ServoInit {

	public static CRobotMotion getInstanceOfCRobotMotion(String robotType) {
		if      (Params.robotType.equals("CommU")) return ServoInit.initCommU();
		else if (Params.robotType.equals("Sota"))  return ServoInit.initSota();
		else {
			System.err.println("RobotType is invalid. robotType=" + Params.robotType);
			return null;
		}
	}

	public static CRobotMotion initCommU() {
		CRobotMem mem = new CRobotMem();
		CCommUMotion motion = new CCommUMotion(mem);
		if (mem.Connect()) {
			motion.InitRobot_CommU();
			motion.ServoOn();
			motion.LipSyncEnable(true);
			Byte[]  poseIds  = null, torqueIds  = null, ledIds  = null;
			Short[] poseVals = null, torqueVals = null, ledVals = null;
			poseIds    = new  Byte[]{    1,    2,    3,    4,    5,    6,    7,    8,    9,   10,   11,   12,   13,   14};
			poseVals   = new Short[]{    0,    0,  600,    0, -600,    0,    0,    0,    0,    0,    0,    0,    0,    0};
			torqueIds  = new  Byte[]{    1,    2,    3,    4,    5,    6,    7,    8,    9,   10,   11,   12,   13,   14};
			torqueVals = new Short[]{  100,  100,  100,  100,  100,  100,  100,  100,  100,  100,  100,  100,   50,   50};
			ledIds     = new  Byte[]{    0,    1,    2,    3,    4,    5,    6};
			ledVals    = new Short[]{    0,    0,    0,    0,    0,    0,    0};
			CRobotPose pose = new CRobotPose();
			pose.SetPose(poseIds, poseVals);
			pose.SetTorque(torqueIds, torqueVals);
			pose.SetLed(ledIds, ledVals);
			motion.play(pose, 1500, "RobotController");
			CRobotUtil.wait(1500);
			return motion;
		}
		else {
			System.err.println("The constructer could not connect to the robot memory. ");
			return null;
		}
	}

	public static CRobotMotion initSota() {
		CRobotMem mem = new CRobotMem();
		CSotaMotion motion = new CSotaMotion(mem);
		if (mem.Connect()) {
			motion.InitRobot_Sota();
			motion.ServoOn();
			Byte[]  poseIds  = null, torqueIds  = null, ledIds  = null;
			Short[] poseVals = null, torqueVals = null, ledVals = null;
			poseIds    = new  Byte[]{    1,    2,    3,    4,    5,    6,    7,    8};
			poseVals   = new Short[]{    0, -900,    0,  900,    0,    0,    0,    0};
			torqueIds  = new  Byte[]{    1,    2,    3,    4,    5,    6,    7,    8};
			torqueVals = new Short[]{  100,  100,  100,  100,  100,  100,  100,  100};
			ledIds     = new  Byte[]{    0,    1,    2,    8,    9,   10,   11,   12,   13};
			ledVals    = new Short[]{    0,    0,    0,  180,   80,    0,  180,   80,    0};
			CRobotPose pose = new CRobotPose();
			pose.SetPose(poseIds, poseVals);
			pose.SetTorque(torqueIds, torqueVals);
			pose.SetLed(ledIds, ledVals);
			motion.play(pose, 1500);
			CRobotUtil.wait(1500);
			return motion;
		}
		else {
			System.err.println("The constructer could not connect to the robot memory. ");
			return null;
		}
	}
}
