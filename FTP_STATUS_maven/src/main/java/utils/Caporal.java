package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Path;

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
	
	static private String uid_chefDeProjet;
	
	final private static String home =  System.getProperty("user.home");
	final private static String secret_pwd = "ftp_secret.conf";
	final private static File secret_file = new File(home, secret_pwd);
	
	
	private static String sortie = "";
	
	public static boolean connect(){
		
		done = false;
		
		try {
			app = new EncryptDecrypt(secret_file.getPath(),"pass_serveur","encrypted");
			done = true;
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return done;
		}
		
		password = app.decryptedUserPassword;
		
		System.out.println("mot de passe décrypté");
		return done;
	}
	
	
	public static boolean checkUserExists(String userName){
		
		connect();
		
		done = false;

		Runnable readUsers = new Runnable() {
			
			@Override
			public void run() {
				
				JSch jsch=new JSch();  
				
				LoadConfig.loadAdresse();
				
				try {
					Session session = jsch.getSession("root", Settings.getAdresse_serveur(), 22);
	
				    session.setPassword(password);
				    
				  //extra config code
				    java.util.Properties config = new java.util.Properties(); 
				    config.put("StrictHostKeyChecking", "no");
				    session.setConfig(config);
				    
				    session.connect();
				    
				    System.out.println("connecté");
				    
				    String command = "cat /etc/passwd";
				    
                    uid_chefDeProjet = envoiCommande(session, command);
                    
                    done = true;
                    exists = true;
				    
				} catch (JSchException | IOException e) {
					done = false;
					e.printStackTrace();

				}
			}
		};
		
		readUsers.run();
		
		return exists;
		
	}
	
	public static boolean createUser(String userName, String pass){
		
		done = false;
		password = app.decryptedUserPassword;
		
	    Runnable runCreate = new Runnable() {
		
			@Override
			public void run() {
				
				String command = "";
				
				JSch jsch=new JSch();  
				try {
					Session session = jsch.getSession("root", Settings.getAdresse_serveur(), 22);
   
				    session.setPassword(password);
				    
				  //extra config code
				    java.util.Properties config = new java.util.Properties(); 
				    config.put("StrictHostKeyChecking", "no");
				    session.setConfig(config);
				    
				    session.connect();

				    command = String.format("useradd -p %s -m %s -d /home/satmulti/sd/%s/www/%s -o -u %s -g users -k /etc/skel_ftp -s /bin/false",
				    		                       pass, userName, Settings.getChefDeProjet(), userName, uid_chefDeProjet);
				    
				    envoiCommande(session, command);

				} catch (JSchException | IOException e) {
					done = false;
					System.out.println("\n[ERROR] commande : '" + command + "' echouée\n");
					e.printStackTrace();

				}
				    
				    
			}
	    };
	    
	    runCreate.run();

	    return done;
	}
	
	public static boolean setUserPasswd(String userName, String pass, Stage fenetre){
		
		done = false;
		
	    Runnable runCreate = new Runnable() {
		
			@Override
			public void run() {
				
				String command = "";
				
				JSch jsch=new JSch();  
				try {
					Session session = jsch.getSession("root", Settings.getAdresse_serveur(), 22);
   
				    session.setPassword(password);
				    
				  //extra config code
				    java.util.Properties config = new java.util.Properties(); 
				    config.put("StrictHostKeyChecking", "no");
				    session.setConfig(config);
				    
				    session.connect();

                    command = String.format("echo '%s:%s' | chpasswd",
                            userName, pass);

                    envoiCommande(session, command);
    
				    
				} catch (JSchException | IOException e) {
					done = false;
					System.out.println("\n[ERROR] commande : '" + command + "' echouée\n");
					e.printStackTrace();

				}
				    
				    
			}
	    };
	    
	    runCreate.run();
	    
	    WriteConfig.addServeur(userName, Settings.getAdresse_serveur(), userName, pass);
	    fenetre.close();
	    
	    return done;
	}
	
    public static boolean moveFile(String file, String destination, String chef_de_projet, String pass){


		done = false;
		password = pass;
		
	    Runnable runCreate = new Runnable() {
		
			@Override
			public void run() {
				
				String command = "";
				
				JSch jsch=new JSch();  
				try {
					Session session = jsch.getSession(chef_de_projet, Settings.getAdresse_serveur(), 22);
   
				    session.setPassword(password);
				    
				  //extra config code
				    java.util.Properties config = new java.util.Properties(); 
				    config.put("StrictHostKeyChecking", "no");
				    session.setConfig(config);
				    
				    session.connect();

				    command = String.format("mv %s %s", file, destination);
				    
				    envoiCommande(session, command);

				} catch (JSchException | IOException e) {
					done = false;
					System.out.println("\n[ERROR] commande : '" + command + "' echouée\n");
					e.printStackTrace();

				}
				    
				    
			}
	    };
	    
	    runCreate.run();

	    return done;
	}
    
    public static String listDir(String username, String chef_de_projet, String pass){

	    String file = Settings.getRacine_serveur() + "/" + username;
	    
	    System.out.println(file);
		
		done = false;
		password = pass;
		
	    Runnable runCreate = new Runnable() {
		
			@Override
			public void run() {
				
				String command = "";
				
				JSch jsch=new JSch();  
				try {
					Session session = jsch.getSession(chef_de_projet, Settings.getAdresse_serveur(), 22);
   
				    session.setPassword(password);
				    
				  //extra config code
				    java.util.Properties config = new java.util.Properties(); 
				    config.put("StrictHostKeyChecking", "no");
				    session.setConfig(config);
				    
				    session.connect();

				    command = String.format("python /ftpscript/FTP_alert/main_cli.py  '%s' %d %d", file, Math.round(Settings.getSeuilVert()), Math.round(Settings.getTailleMin()));
				    
				    envoiCommande(session, command);
				    
				    done = true;

				} catch (JSchException | IOException e) {
					done = false;
					System.out.println("\n[ERROR] commande : '" + command + "' echouée\n");
					e.printStackTrace();

				}
				    
				    
			}
	    };
	    
	    runCreate.run();
	    
	    System.out.println(sortie);

	    return sortie;
	}
	
	public static String envoiCommande(Session session, String command) throws JSchException, IOException{
		
		System.out.println("\n[TODO] : '" + command + "'\n");
		
		Channel channel=session.openChannel("exec");
	    ((ChannelExec)channel).setCommand(command);
	    
        channel.setInputStream(null);
	    
	    ((ChannelExec)channel).setErrStream(System.err);

	    Reader in= new InputStreamReader(channel.getInputStream());

	    channel.connect();
	    
	    
	    BufferedReader br = new BufferedReader(in);
    	
    	String s = br.readLine();
		
    	while(s != null){
    		
    		sortie += (s + "\n");
    		
    		if(command.equals("cat /etc/passwd") && s.startsWith(String.format("%s:", Settings.getChefDeProjet() ))){
    			uid_chefDeProjet = s.split(":")[2];
    		}
    		
    		s = br.readLine();

    	}
	    
	    channel.disconnect();
	    session.disconnect();
	    
	    done = true;
	    
	    System.out.println("\n[OK] commande : '" + command + "' effectuée\n");

	    return uid_chefDeProjet;

		
	}


}
