package led;

import java.util.Map;

import main.Data4Q;

public class LedData implements Data4Q {

	public final int msec;
	public final Map<Byte, Short> map;
	public final String type = "LedData";

	public LedData(int msec, Map<Byte, Short> map) {
		this.msec = msec;
		this.map = map;
	}

	@Override
	public String getType() {
		return type;
	}
}
