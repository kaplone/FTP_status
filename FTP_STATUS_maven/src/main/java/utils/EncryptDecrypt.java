package main.java.utils;

import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
 
public class EncryptDecrypt {
    private final String propertyFileName;
    private final String propertyKey;
    private final String isPropertyKeyEncrypted;
 
    final String decryptedUserPassword;
 
    /**
     * The constructor does most of the work.
     * It initializes all final variables and invoke two methods
     * for encryption and decryption job. After successful job
     * the constructor puts the decrypted password in variable
     * to be retrieved by calling class.
     * 
	 * 
	 * @param pPropertyFileName /Name of the properties file that contains the password
	 * @param pUserPasswordKey	/Left hand side of the password property as key. 
	 * @param pIsPasswordEncryptedKey 	/Key in the properties file that will tell us if the password is already encrypted or not
	 * 
	 * @throws Exception
	 */
    public EncryptDecrypt(String pPropertyFileName,String pUserPasswordKey, String pIsPasswordEncryptedKey) throws Exception {
        this.propertyFileName = pPropertyFileName;
        this.propertyKey = pUserPasswordKey;
        this.isPropertyKeyEncrypted = pIsPasswordEncryptedKey;
        try {
            encryptPropertyValue();
        } catch (ConfigurationException e) {
            throw new Exception("Problem encountered during encryption process",e);
        }
        decryptedUserPassword = decryptPropertyValue();
 
    }
    
    String passkey;
    //JTextField passwordField=(JTextField)new JPasswordField(80);
    TextField passwordField = new PasswordField();
    
//    public boolean promptPassword(String message){
//        Object[] ob={passwordField}; 
//        int result=
//          JOptionPane.showConfirmDialog(null, ob, message,
//                                        JOptionPane.OK_CANCEL_OPTION);
//        if(result==JOptionPane.OK_OPTION){
//          passkey=passwordField.getText();
//          return true;
//        }
//        else{ 
//          return false; 
//        }
//      }
    
    public void promptPassword(String message){
			
		Stage stage = new Stage();
		stage.setTitle(message);
		
		
		
		
		Button ok = new Button("OK");
		Button annuler = new Button("Annuler");
    			
    	ok.setOnAction(a -> {passkey=passwordField.getText(); 
    			             stage.close();
    			             });
    	annuler.setOnAction(a -> stage.close());
    			
    	Node [] hChildren = new Node [] {ok, annuler};
    	HBox hbox = new HBox(30, hChildren);
    	Node [] vChildren = new Node [] {passwordField, hbox};
    	VBox vbox = new VBox(30, vChildren);
  
    	Scene scene = new Scene(vbox, 500, 100);

    	stage.setScene(scene);
    	stage.showAndWait();

    }
 
    /**
     * The method that encrypt password in the properties file. 
     * This method will first check if the password is already encrypted or not. 
     * If not then only it will encrypt the password.
     * 
     * @throws ConfigurationException
     */
    private void encryptPropertyValue() throws ConfigurationException {
        System.out.println("Starting encryption operation");
        System.out.println("Start reading properties file");
 
        //Apache Commons Configuration 
        PropertiesConfiguration config = new PropertiesConfiguration(propertyFileName);
 
        //Retrieve boolean properties value to see if password is already encrypted or not
        String isEncrypted = config.getString(isPropertyKeyEncrypted);
 
        //Check if password is encrypted?
        if(isEncrypted.equals("false")){
            String tmpPwd = config.getString(propertyKey);
            //System.out.println(tmpPwd); 
            //Encrypt
            StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
            // This is a required password for Jasypt. You will have to use the same password to
            // retrieve decrypted password later. T
            // This password is not the password we are trying to encrypt taken from properties file.
            
            
            promptPassword("entrer la clé de cryptage pour l'encodage");
            
            encryptor.setPassword(passkey);
            String encryptedPassword = encryptor.encrypt(tmpPwd);
            System.out.println("Encryption done and encrypted password is : " + encryptedPassword ); 
 
            // Overwrite password with encrypted password in the properties file using Apache Commons Cinfiguration library
            config.setProperty(propertyKey, encryptedPassword);
            // Set the boolean flag to true to indicate future encryption operation that password is already encrypted
            config.setProperty(isPropertyKeyEncrypted,"true");
            // Save the properties file
            config.save();
        }else{
        	 System.out.println("User password is already encrypted.\n ");
        }
    }
 
    private String decryptPropertyValue() throws ConfigurationException {
    	 System.out.println("Starting decryption");
        PropertiesConfiguration config = new PropertiesConfiguration(propertyFileName);
        String encryptedPropertyValue = config.getString(propertyKey);
        //System.out.println(encryptedPropertyValue); 
 
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        
        promptPassword("entrer la clé de cryptage pour le décodage");
        
        encryptor.setPassword(passkey);
        String decryptedPropertyValue = encryptor.decrypt(encryptedPropertyValue);
        //System.out.println(decryptedPropertyValue); 
 
        return decryptedPropertyValue;
    }
}
