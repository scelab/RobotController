package wav;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SpeechPlayer {

	private final Object lock;
	private Process process;

	public SpeechPlayer() {
		lock = new Object();
	}

	public int play(Path path) {
		stop();
		synchronized (lock) {
			try {
				process = new ProcessBuilder("aplay", path.toString()).start();
				return getmsec(path);
			} catch (IOException e) {
				e.printStackTrace();
				return -1;
			}
		}
	}

	public void stop() {
		if (process != null && process.isAlive()) {
			try {
				new ProcessBuilder("killall", "aplay").start();
				while (process.isAlive()) { Thread.sleep(10); }
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			}
			process = null;
		}
	}

	private int getmsec(Path path) {
		try (AudioInputStream ais = AudioSystem.getAudioInputStream(path.toFile())) {
			long length = ais.getFrameLength();
			AudioFormat af = ais.getFormat();
			float frame = af.getSampleRate();
			int msec = (int)((length/frame) * 1000);
			return msec;
		} catch (IOException | UnsupportedAudioFileException e) {
			e.printStackTrace();
			return -1;
		}
	}

	public static void main(String[] args) throws InterruptedException {
		Path path = Paths.get("/home/root/openjtalk/open_jtalk-1.11/output.wav");
		SpeechPlayer player = new SpeechPlayer();
		player.play(path);
		Thread.sleep(1500);
		player.play(path);
		Thread.sleep(10000);
	}
}
