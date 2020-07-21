package led;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONException;
import org.json.JSONObject;

import main.ServerIO;

public class LedServer implements Runnable {

	private final int port;
	private final LedController controller;
	private final LedConverter converter;

	public LedServer (int port, LedController controller, LedConverter converter) {
		this.port = port;
		this.controller = controller;
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
				while (true) {
					byte[] data = io.read();
					String text = new String(data);
					try {
						JSONObject obj = new JSONObject(text);
						int msec = obj.getInt("msec");
						Map<Byte, Short> map = converter.jsonToMap(obj.getJSONObject("map"));
						controller.set(msec, map);
						io.write(String.valueOf(msec).getBytes());
					} catch (JSONException e) {
						e.printStackTrace();
						io.write(String.valueOf(-1).getBytes());
					}
				}
			} catch (IOException e) {
				//e.printStackTrace();
				System.out.println("[LedServer] Client is disconnected.");
			}
		}
	}
}
