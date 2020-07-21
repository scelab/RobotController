package pose;

import java.util.Map;

import jp.vstone.RobotLib.CRobotMotion;
import jp.vstone.RobotLib.CRobotPose;

public class AxisController {

	private final Object lock;
	private final CRobotMotion motion;


	public AxisController(CRobotMotion motion) {
		lock = new Object();
		this.motion = motion;
	}

	public void set(int msec, Map<Byte, Short> map) {
		synchronized (lock) {
			CRobotPose pose = new CRobotPose();
			pose.SetPose(map);
			Byte[] ids = map.keySet().toArray(new Byte[map.size()]);
			motion.LockServoHandle(ids);
			motion.play(pose, msec);
			motion.UnLockServoHandle(ids);
		}
	}
}
