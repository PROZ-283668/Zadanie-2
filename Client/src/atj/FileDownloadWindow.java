package atj;

import java.io.File;
import java.util.Optional;

import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class FileDownloadWindow extends Application
{
	private String path;

	FileDownloadWindow()
	{
		path = null;
	}

	private void chooseDirectory()
	{
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Choose download location");
		Stage stage = new Stage();
		File file = fileChooser.showSaveDialog(stage);

		if (file != null)
		{
			path = file.toString();
			System.out.println("Chosen location: " + path);
		} else
		{
			System.out.println("Download cancelled");
		}
	}

	@Override
	public void start(Stage primaryStage) throws Exception
	{
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("File download");
		alert.setHeaderText("File recieved");
		alert.setContentText("Do you want do download it?");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK)
		{
			chooseDirectory();
		}
	}

	public String getPath()
	{
		return path;
	}

}
