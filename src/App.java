import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class App extends Application{
    public static void main(String[] args) throws Exception {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

       primaryStage.setTitle("AGORA DEU");

       Button botao = new Button("CARQUEIE AQUI");

       botao.setOnAction(new EventHandler<ActionEvent>() {

        @Override
        public void handle(ActionEvent event) {
           System.out.println("CARQUEOU NO BOTAO BIXO");
        }
        
       });

       StackPane root = new StackPane();

       root.getChildren().addAll(botao);

       primaryStage.setScene(new Scene(root,300,200));

       primaryStage.show();
    }
}
