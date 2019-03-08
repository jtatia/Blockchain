package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.security.*;

public class Main extends Application {

    private static PublicKey publicKey;
    private static PrivateKey privateKey;

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader();
        primaryStage.setTitle("File Sharing");
        loader.setLocation(Main.class.getResource("sample.fxml"));
        Scene scene = new Scene(loader.load());
        primaryStage.setScene(scene);
        Main main = new Main();
        main.Generate();
        Controller controller = loader.<Controller>getController();
        controller.set(publicKey,privateKey);
        primaryStage.show();
    }

    public static void Generate() throws Exception {
        KeyPairGenerator keygen = KeyPairGenerator.getInstance("RSA");
        keygen.initialize(1024);
        KeyPair keyPair = keygen.generateKeyPair();
        publicKey = keyPair.getPublic();
        privateKey = keyPair.getPrivate();
    }

    public static void main(String[] args) throws Exception{
        Application.launch(Main.class, args);
    }
}
