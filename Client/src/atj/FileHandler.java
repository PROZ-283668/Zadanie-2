package atj;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class FileHandler
{
	private File file;
	private FileDownloadWindow fileDownloadWindow;

	public void writeFile(ByteBuffer buf)
	{
		FileOutputStream fout;
		FileChannel channel;
		Stage stage = new Stage();
		fileDownloadWindow = new FileDownloadWindow();

		try
		{
			fileDownloadWindow.start(stage);
		} catch (Exception e)
		{
			e.printStackTrace();
		}

		String fileName = fileDownloadWindow.getPath();

		if (fileName != null)
		{
			file = new File(fileName);
			try
			{
				fout = new FileOutputStream(file, false);
				channel = fout.getChannel();
				channel.write(buf);
				channel.close();
				fout.close();

				System.out.println(fileName + " was saved");
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		} else
		{
			System.out.println("File was not saved");
		}
	}

	public ByteBuffer readFile()
	{
		try
		{
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Choose file to send.");
			file = fileChooser.showOpenDialog(null);

			FileInputStream fin = new FileInputStream(file);
			ByteBuffer result = ByteBuffer.allocate((int) file.length());
			int b;

			while ((b = fin.read()) != -1)
			{
				result.put((byte) b);
			}

			result.flip();
			fin.close();
			return result;
		} catch (IOException e)
		{
			e.printStackTrace();
			return null;
		}
	}
}
