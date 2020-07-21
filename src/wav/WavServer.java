package wav;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import main.ServerIO;

public class WavServer implements Runnable {

	private final int port;
	private final SpeechPlayer player;

	public WavServer (int port, SpeechPlayer player) {
		this.port = port;
		this.player = player;
	}

	@Override
	public void run() {
		try (ServerSocket serverSocket = new ServerSocket(port)){
			ExecutorService ex = Executors.newCachedThreadPool();
			while (true) {
				Socket socket = serverSocket.accept();
				ex.execute(new RecvThread(socket));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private class RecvThread implements Runnable {
		private Socket socket;

		public RecvThread(Socket socket) {
			this.socket = socket;
		}

		@Override
		public void run() {
			try (InputStream is = socket.getInputStream();
				OutputStream os = socket.getOutputStream()) {
				ServerIO io = new ServerIO(is, os);
				while (true) {
					byte[] data = io.read();
					System.out.println("data.length="+data.length);
					Path path = Paths.get("__temp_wav");
					System.out.println("path="+path);
					Files.write(path, data);
					int msec = player.play(path);
					io.write(String.valueOf(msec).getBytes());
				}
			} catch (IOException e) {
				//e.printStackTrace();
				System.out.println("[WavServer] Client is disconnected.");
			}
		}
	}
}
