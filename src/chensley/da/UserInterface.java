/**
 * Copyright(C) 2020 Christopher Hensley. 
 * Do as thou wilt shall be the whole of the License. 
 * Love is the License, love under will.
 **/
package chensley.da;

import javafx.application.Application;
import javafx.scene.layout.HBox;
import javafx.scene.Scene;
import javafx.stage.Stage;

@SuppressWarnings("restriction") //Eclipse does not properly recognize javafx APIs
public class UserInterface extends Application{
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Digital Artifact");
		
		HBox root = new HBox();
		Scene scene = new Scene(root, 600, 400);
		primaryStage.setScene(scene);
		
		primaryStage.show();
	}
	
	public void display() {
		Application.launch();
	}
}
