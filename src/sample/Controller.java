package sample;

import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.math.BigInteger;
import java.security.*;
import java.security.spec.RSAPublicKeySpec;
import org.apache.commons.codec.binary.Base64;
public class Controller {

    @FXML
    private TextArea UPublicKey;

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
    private Cipher cipher;

    @FXML
    public void onUploadAction(ActionEvent actionEvent) throws Exception {
        String sharedPublicKey = UPublicKey.getText();
        String modulus = sharedPublicKey.substring(0, sharedPublicKey.indexOf("\n"));
        String exponent = sharedPublicKey.substring(sharedPublicKey.indexOf("\n")+1);
        System.out.println("Mod="+modulus);
        System.out.println("exp="+exponent);
        RSAPublicKeySpec spec = new RSAPublicKeySpec(new BigInteger(modulus), new BigInteger(exponent));
        KeyFactory fact = KeyFactory.getInstance("RSA");
        PublicKey shared = fact.generatePublic(spec);
        SymmetricKey symmetricKey = new SymmetricKey("blockchain",16, "AES");
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(null);
        String symm = symmetricKey.encryptFile(file);
        ExecuteCommands executeCommands = new ExecuteCommands();
        String ret = executeCommands.execute("ipfs add "+file.getPath());
        ret = ret.substring(ret.indexOf(" "));
        String hash = ret.substring(ret.indexOf(" ")+1,ret.substring(ret.indexOf(" ")+1).indexOf(file.getName()));
        System.out.println(hash);
        System.out.println(symm);
        hash = encryptText(hash, shared);
        symm = encryptText(symm, shared);
        UOutput.setText("Hash: "+hash+"\n"+"Key: "+symm);
    }

    @FXML
    public void onDownloadAction(ActionEvent actionEvent) throws Exception {
        String hash = decryptText(DHash.getText(),privateKey);
        String key = decryptText(DSymmKey.getText(), privateKey);
        ExecuteCommands executeCommands = new ExecuteCommands();
        String string = executeCommands.execute("ipfs get "+hash);
        System.out.println(string);
        System.out.println(key);
        File f= new File(hash);
        SymmetricKey symmetricKey = new SymmetricKey("12",16,"AES");
        symmetricKey.decryptFile(f,key);
        System.out.println("Completed Process");
    }

    public void set(PublicKey publicKey, PrivateKey privateKey) throws Exception {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
        System.out.println(publicKey);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        RSAPublicKeySpec spec = keyFactory.getKeySpec(publicKey, RSAPublicKeySpec.class);
        String print = spec.getModulus()+"\n"+spec.getPublicExponent();
        val.setText(print);
    }

    public String encryptText(String msg, PublicKey key)
            throws NoSuchAlgorithmException, NoSuchPaddingException,
            UnsupportedEncodingException, IllegalBlockSizeException,
            BadPaddingException, InvalidKeyException {
        cipher = Cipher.getInstance("RSA");
        this.cipher.init(Cipher.ENCRYPT_MODE, key);
        return Base64.encodeBase64String(cipher.doFinal(msg.getBytes("UTF-8")));
    }

    public String decryptText(String msg, PrivateKey key)
            throws Exception {
        cipher = Cipher.getInstance("RSA");
        this.cipher.init(Cipher.DECRYPT_MODE, key);
        return new String(cipher.doFinal(Base64.decodeBase64(msg)), "UTF-8");
    }


}
