import javafx.application.Application;
import javafx.stage.Stage;
import views.MainView;
import utils.JPAUtil;

public class App extends Application {

    @Override
    public void start(Stage stage) {
        MainView.mostrar(stage);
    }

    public static void main(String[] args) {
        launch(args);
        JPAUtil.getEntityManagerFactory(); 
    }
}