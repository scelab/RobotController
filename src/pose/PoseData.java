package pose;

import java.util.Map;

import main.Data4Q;

public class PoseData implements Data4Q {

	public final int msec;
	public final Map<Byte, Short> map;
	public final String type = "PoseData";

	public PoseData(int msec, Map<Byte, Short> map) {
		this.msec = msec;
		this.map = map;
	}

	@Override
	public String getType() {
		return type;
	}
}
