package led;

import java.util.Map;

import jp.vstone.RobotLib.CRobotMotion;
import jp.vstone.RobotLib.CRobotPose;

public class LedController {

	private Object lock;
	private CRobotMotion motion;

	public LedController(CRobotMotion motion) {
		lock = new Object();
		this.motion = motion;
	}

	public void set(int msec, Map<Byte, Short> map) {
		synchronized (lock) {
			CRobotPose pose = new CRobotPose();
			pose.SetLed(map);
			Byte[] ids = map.keySet().toArray(new Byte[map.size()]);
			motion.LockLEDHandle(ids);
			motion.play(pose, msec);
			motion.UnLockLEDHandle(ids);
		}
	}
}
