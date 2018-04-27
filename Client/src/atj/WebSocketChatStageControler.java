package atj;

import java.io.IOException;
import java.net.URI;
import java.nio.ByteBuffer;

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class WebSocketChatStageControler
{
	@FXML
	TextField userTextField;
	@FXML
	TextArea chatTextArea;
	@FXML
	TextField messageTextField;
	@FXML
	Button btnSet;
	@FXML
	Button btnSend;
	@FXML
	Button btnFile;
	private String user;
	private WebSocketClient webSocketClient;
	private FileHandler fileHandler;

	@FXML
	public void initialize()
	{
		webSocketClient = new WebSocketClient();
		fileHandler = new FileHandler();
		user = userTextField.getText();
	}

	@FXML
	private void btnSet_Click()
	{
		if (userTextField.getText().isEmpty())
		{
			return;
		}
		user = userTextField.getText();
		webSocketClient.sendMessage(" joined the conversation.");
		System.out.println(user + " joined the conversation.");
		btnSet.setDisable(true);
		userTextField.setDisable(true);
	}

	@FXML
	private void btnSend_Click()
	{
		if (messageTextField.getText().isEmpty())
		{
			return;
		}
		webSocketClient.sendMessage(messageTextField.getText());
		messageTextField.clear();
	}

	@FXML
	private void btnFile_Click()
	{
		webSocketClient.sendFile(fileHandler.readFile());
	}

	public void closeSession(CloseReason closeReason)
	{
		try
		{
			webSocketClient.session.close(closeReason);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	@ClientEndpoint
	public class WebSocketClient
	{
		private Session session;

		public WebSocketClient()
		{
			connectToWebSocket();
		}

		@OnOpen
		public void onOpen(Session session)
		{
			System.out.println("Connection is opened.");
			this.session = session;
		}

		@OnClose
		public void onClose(CloseReason closeReason)
		{
			System.out.println("Connection is closed: " + closeReason.getReasonPhrase());
		}

		@OnError
		public void onError(Throwable throwable)
		{
			System.out.println("Error occured");
			throwable.printStackTrace();
		}

		@OnMessage
		public void onMessage(String message, Session session)
		{
			System.out.println("Message was received");
			chatTextArea.setText(chatTextArea.getText() + message + "\n");
		}

		@OnMessage
		public void onMessage(ByteBuffer buf, Session session)
		{
			System.out.println("File was recieved");
			Platform.runLater(() -> fileHandler.writeFile(buf));
		}

		private void connectToWebSocket()
		{
			WebSocketContainer webSocketContainer = ContainerProvider.getWebSocketContainer();
			try
			{
				URI uri = URI.create("ws://localhost:8080/WebSocketEndpoint/websocketendpoint");
				webSocketContainer.connectToServer(this, uri);
			} catch (DeploymentException | IOException e)
			{
				e.printStackTrace();
			}
		}

		public void sendMessage(String message)
		{
			try
			{
				System.out.println("Message was sent: " + message);
				session.getBasicRemote().sendText(user + ": " + message);
			} catch (IOException ex)
			{
				ex.printStackTrace();
			}
		}

		public void sendFile(ByteBuffer buf)
		{
			try
			{
				System.out.println("File was sent");
				session.getBasicRemote().sendBinary(buf);
			} catch (IOException ex)
			{
				ex.printStackTrace();
			}
		}
	} // public class WebSocketClient
} // public class WebSocketChatStageControler