import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class RobiMathor extends Application {

	private static final KeyCodeCombination KEY_COMBINATION_FULLSCREEN =
		new KeyCodeCombination(KeyCode.ENTER, KeyCombination.ALT_DOWN);

	private BorderPane rootLayout;

	@Override
	public void start(Stage stage) throws Exception {
		stage.setTitle("RobiMathor!");
		rootLayout = new BorderPane();
		rootLayout.setCenter(new Text("HERE WE GO"));

		stage.setFullScreen(true);
		stage.setFullScreenExitHint("");
		stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);

		MenuBar menuBar = new MenuBar();
		Menu mainMenu = new Menu("RobiMathor");
		MenuItem toggleMenu = new MenuItem("Toggle menu");
		MenuItem fullscreenCmd = new MenuItem("Toggle Fullscreen");
		MenuItem exitCmd = new MenuItem("Exit");
		mainMenu.getItems().addAll(toggleMenu, fullscreenCmd, exitCmd);
		menuBar.getMenus().add(mainMenu);

		exitCmd.setOnAction(e -> stage.close());
		fullscreenCmd.setOnAction(e -> stage.setFullScreen(!stage.isFullScreen()));
		fullscreenCmd.setAccelerator(KEY_COMBINATION_FULLSCREEN);

		toggleMenu.setAccelerator(new KeyCodeCombination(KeyCode.ESCAPE));
		toggleMenu.setOnAction(e -> Platform.runLater(() ->
			rootLayout.setTop(rootLayout.getTop() != null ? null : menuBar)));

		rootLayout.setTop(menuBar);
		Button startButton = new Button("Start button");
		Button stopButton = new Button("Stop button");

		Text timeText = new Text();
		timeText.textProperty().set("0");

		Pane controls = new TilePane();
		controls.getChildren().addAll(startButton, stopButton, timeText);
		rootLayout.setBottom(controls);

		Scene scene = new Scene(rootLayout);
		stage.setScene(scene);
		stage.show();
	}
}
