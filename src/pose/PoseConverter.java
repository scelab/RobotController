package pose;

import java.util.Map;

import org.json.JSONObject;

public interface PoseConverter {
	public Map<Byte, Short> jsonToMap(JSONObject obj);
}
