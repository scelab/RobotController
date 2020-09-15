package wav;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import main.Data4Q;
import main.ServerIO;

public class WavServer implements Runnable {

	private final int port;
	private BlockingQueue<Data4Q> q;
	private final Object lock;

	public WavServer (int port, BlockingQueue<Data4Q> q) {
		this.port = port;
		this.q = q;
		this.lock = new Object();
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
				byte[] data = io.read();
				synchronized (lock) {
					Path path = Paths.get("__temp_wav");
					Files.write(path, data);
					q.add(new WavData(path));
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					socket.close();
				} catch (IOException e) {}
			}
		}
	}
}
