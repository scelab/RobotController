package reader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONObject;

import main.ServerIO;
import pose.PoseConverter;

public class ReadServer implements Runnable {

	private final int port;
	private final AxisReader reader;
	private final PoseConverter converter;

	public ReadServer (int port, AxisReader reader, PoseConverter converter) {
		this.port = port;
		this.reader = reader;
		this.converter = converter;
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
				Map<Byte, Short> map = reader.read();
				JSONObject obj = converter.mapToJson(map);
				byte[] data = obj.toString().getBytes();
				io.write(data);
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
