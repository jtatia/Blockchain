package sample;

import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import javax.swing.event.ChangeListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.security.PrivateKey;
import java.security.PublicKey;

public class Controller {

    @FXML
    private TextField UPublicKey;

    @FXML
    private Button Upload;

    @FXML
    private TextArea UOutput;

    @FXML
    private TextField DHash;

    @FXML
    private TextField DSymmKey;

    @FXML
    private Button Download;

    @FXML
    private TextArea val;

    private PublicKey publicKey;
    private PrivateKey privateKey;



    @FXML
    public void onUploadAction(ActionEvent actionEvent) throws Exception {
        String sharedPublicKey = UPublicKey.getText();
        SymmetricKey symmetricKey = new SymmetricKey("blockchain",16, "AES");
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(null);
        String symm = symmetricKey.encryptFile(file);
        ExecuteCommands executeCommands = new ExecuteCommands();
        executeCommands.execute("cd "+file.getPath());
        String ret = executeCommands.execute("ipfs add "+file.getName());
       // System.out.println(ret);
       // System.out.println(symm);
    }

    @FXML
    public void onDownloadAction(ActionEvent actionEvent) {

    }

    public void set(PublicKey publicKey, PrivateKey privateKey) {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
        System.out.println(publicKey);
        String print = "Public Key: "+ publicKey;
        val.setText(print);
    }
}
