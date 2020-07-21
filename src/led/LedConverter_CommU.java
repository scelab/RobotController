package led;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

public class LedConverter_CommU implements LedConverter {

	private final Map<String, Byte> map = new HashMap<String, Byte>() {
		{
			put("PWR_BTN_R", (byte) 0);
			put("PWR_BTN_G", (byte) 1);
			put("PWR_BTN_B", (byte) 2);
			put("BODY_R",    (byte) 3);
			put("BODY_G",    (byte) 4);
			put("BODY_B",    (byte) 5);
			put("L_CHEEK",   (byte) 6);
			put("R_CHEEK",   (byte) 7);
		}
	};

	@Override
	public Map<Byte, Short> jsonToMap(JSONObject json) {
		Map<Byte, Short> ret = new HashMap<Byte, Short>();
		for (String key : json.keySet()) {
			byte id = map.get(key);
			int val = json.getInt(key);
			if      (val < 0)   val = 0;
			else if (val > 255) val = 255;
			ret.put(id, (short) val);
		}
		return ret;
	}
}
