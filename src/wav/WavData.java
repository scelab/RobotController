package wav;

import java.nio.file.Path;

import main.Data4Q;

public class WavData implements Data4Q {

	public final String type = "WavData";
	public final Path path;

	public WavData(Path path) {
		this.path = path;
	}

	@Override
	public String getType() {
		return type;
	}
}
