package atj;

import javax.websocket.CloseReason;
import javax.websocket.CloseReason.CloseCodes;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class MainApp extends Application
{
	private Stage primaryStage;

	@Override
	public void start(Stage primaryStage)
	{
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("JavaFX WebSocket Chat");
		showChat();
	}

	public static void main(String[] args)
	{
		launch(args);
	}

	private void showChat()
	{
		try
		{
			FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("ChatWindow.fxml"));
			AnchorPane root = loader.load();
			Scene scene = new Scene(root);

			primaryStage.setScene(scene);
			primaryStage.setOnHiding(e -> primaryStage_Hiding(e, loader));
			primaryStage.show();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void primaryStage_Hiding(WindowEvent e, FXMLLoader fxmlLoader)
	{
		((WebSocketChatStageControler) fxmlLoader.getController())
				.closeSession(new CloseReason(CloseCodes.NORMAL_CLOSURE, "Stage is hiding"));
	}
}
