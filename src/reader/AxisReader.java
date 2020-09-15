package reader;

import java.util.HashMap;
import java.util.Map;

import jp.vstone.RobotLib.CRobotMotion;
import jp.vstone.RobotLib.CRobotPose;

public class AxisReader {

	private final Object lock;
	private final CRobotMotion motion;


	public AxisReader(CRobotMotion motion) {
		lock = new Object();
		this.motion = motion;
	}

	public Map<Byte, Short> read() {
		Map<Byte, Short> map = new HashMap<Byte, Short>();
		Short[] vals = motion.getReadpos();
		if (vals == null){
			return map;
		}
		for (int i = 0; i < vals.length; i++) {
			map.put((byte)(i + 1), vals[i]);
		}
		return map;
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
