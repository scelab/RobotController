package led;

import java.util.Map;

import org.json.JSONObject;

public interface LedConverter {
	public Map<Byte, Short> jsonToMap(JSONObject obj);
}
