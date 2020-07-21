package led;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

public class LedConverter_Sota implements LedConverter {

	private final Map<String, Byte> map = new HashMap<String, Byte>() {
		{
			put("PWR_BTN_R", (byte)  0);
			put("PWR_BTN_G", (byte)  1);
			put("PWR_BTN_B", (byte)  2);
			put("R_EYE_R",   (byte)  8);
			put("R_EYE_G",   (byte)  9);
			put("R_EYE_B",   (byte) 10);
			put("L_EYE_R",   (byte) 11);
			put("L_EYE_G",   (byte) 12);
			put("L_EYE_B",   (byte) 13);
			put("MOUTH",     (byte) 14);
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
