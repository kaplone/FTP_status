package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.swing.JOptionPane;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;

import application.NewCompte_controller;
import javafx.stage.Stage;
import models.Settings;

public class Caporal {
	
	static EncryptDecrypt app;
	static boolean exists = false;
	static private String password; 
	static private boolean done;
	
	final private static String home =  System.getProperty("user.home");
	final private static String secret_pwd = "ftp_secret.conf";
	final private static File secret_file = new File(home, secret_pwd);
	
	public static boolean checkUserExists(String userName){
		
		try {
			app = new EncryptDecrypt(secret_file.getPath(),"pass_serveur","encrypted");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		password = app.decryptedUserPassword;
		
		System.out.println("mot de passe décrypté");

		Runnable readUsers = new Runnable() {
			
			@Override
			public void run() {
				
				JSch jsch=new JSch();  
				
				System.out.println("avant adresse lue");
				
				LoadConfig.loadAdresse();
				
				System.out.println("adresse lue");
				
				try {
					Session session = jsch.getSession("root", Settings.getAdresse_serveur(), 22);
					// username and password will be given via UserInfo interface.
				    //UserInfo ui= new MyUserInfo();
	
				    session.setPassword(password);
				    
				  //extra config code
				    java.util.Properties config = new java.util.Properties(); 
				    config.put("StrictHostKeyChecking", "no");
				    session.setConfig(config);
				    
				    session.connect();
				    
				    System.out.println("connecté");
				    
				    String command = "cat /etc/passwd";
				    
				    Channel channel=session.openChannel("exec");
				    ((ChannelExec)channel).setCommand(command);
				    
				    channel.setInputStream(null);
				    
				    ((ChannelExec)channel).setErrStream(System.err);

				    Reader in= new InputStreamReader(channel.getInputStream());

				    channel.connect();
				    
				    
				    BufferedReader br = new BufferedReader(in);
			    	
			    	String s = br.readLine();
					
			    	while(s != null){
			    		
			    		System.out.println(s.split(":")[0]);
			    		
			    		if (s.split(":")[0].equals(userName)){
			    		    exists = true;	
			    		    break;
			    		}
			    		s = br.readLine();

			    	}
				    
				    channel.disconnect();
				    session.disconnect();

				} catch (JSchException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		
		readUsers.run();
		
		return exists;
		
	}
	
	public static boolean createUser(String userName, String pass, Stage fenetre){
		
		done = false;
		password = app.decryptedUserPassword;
		
	    Runnable runCreate = new Runnable() {
		
			@Override
			public void run() {
				
				JSch jsch=new JSch();  
				try {
					Session session = jsch.getSession("root", Settings.getAdresse_serveur(), 22);
					// username and password will be given via UserInfo interface.
				    //UserInfo ui= new MyUserInfo();
   
				    session.setPassword(password);
				    
				  //extra config code
				    java.util.Properties config = new java.util.Properties(); 
				    config.put("StrictHostKeyChecking", "no");
				    session.setConfig(config);
				    
				    session.connect();
				    
				    System.out.println(String.format("useradd -p %s -m %s -d /home/%s/%s -g %s -G users ",
				    		                       pass, userName, Settings.getChefDeProjet(), userName, Settings.getChefDeProjet()));
				    
				    String command = String.format("useradd -p %s -m %s -d /home/%s/%s -g %s -G users",
				    		                       pass, userName, Settings.getChefDeProjet(), userName, Settings.getChefDeProjet());
				    
				    Channel channel=session.openChannel("exec");
				    ((ChannelExec)channel).setCommand(command);
				    
                    channel.setInputStream(null);
				    
				    ((ChannelExec)channel).setErrStream(System.err);

				    Reader in= new InputStreamReader(channel.getInputStream());

				    channel.connect();
				    
				    
				    BufferedReader br = new BufferedReader(in);
			    	
			    	String s = br.readLine();
					
			    	while(s != null){
			    		
			    		System.out.println(s);
			    		s = br.readLine();

			    	}
				    
				    channel.disconnect();
				    session.disconnect();
				    
				    done = true;
				    
				    System.out.println("le compte a été créé ...");
				} catch (JSchException | IOException e) {
					done = false;
					e.printStackTrace();

				}
			}
	    };
	    
	    runCreate.run();
	    
	    WriteConfig.addServeur(userName, Settings.getAdresse_serveur(), userName, pass);
	    fenetre.close();
	    
	    return done;
	}


}
