/**
 * Copyright(C) 2020 Christopher Hensley. 
 * Do as thou wilt shall be the whole of the License. 
 * Love is the License, love under will.
 **/
package chensley.da;

import javafx.application.Application;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.Scene;
import javafx.stage.Stage;

@SuppressWarnings("restriction") //Eclipse does not properly recognize javafx APIs
public class UserInterface extends Application{
	
	//These are static because of restrictions on JavaFX
	private static Config config;
	public static void setConfig(Config config) { UserInterface.config = config; }
	
	private GraphicsContext termCtxt; //Context for drawing to simulated terminal
	private final Color background = Color.web(config.term().background());
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle(config.title());
		HBox root = new HBox();
		
		//Create simulated terminal
		Canvas terminal = 
				new Canvas(config.term().width() * config.term().fontSize(), 
						config.term().height() * config.term().fontSize());
		termCtxt = terminal.getGraphicsContext2D();
		termCtxt.setFill(background);
		termCtxt.fillRect(0, 0, terminal.getWidth(), terminal.getHeight());
		root.getChildren().addAll(terminal);
		
		//Build game window
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.sizeToScene();
		primaryStage.show();
	}
	
	public void display() {
		Application.launch();
	}
}
