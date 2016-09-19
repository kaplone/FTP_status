package main.java.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.swing.JOptionPane;

import org.apache.commons.collections.bag.SynchronizedSortedBag;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;

import main.java.application.NewCompte_controller;
import javafx.stage.Stage;
import main.java.models.Settings;

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
	private static String command = "";
	private static Session session;
	
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
					session = jsch.getSession("root", Settings.getAdresse_serveur(), 22);
	
				    session.setPassword(password);
				    
				  //extra config code
				    java.util.Properties config = new java.util.Properties(); 
				    config.put("StrictHostKeyChecking", "no");
				    session.setConfig(config);
				    
				    session.connect();
				    
				    System.out.println("connecté");
				    
				    String command = "cat /etc/passwd";
				    
                    uid_chefDeProjet = envoiCommande(command);
                    
                    done = true;
                    exists = false;  // le check ne se fait pas encore
                                     // TODO : parser STDout et chercher userName ...
				    
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
					session = jsch.getSession("root", Settings.getAdresse_serveur(), 22);
   
				    session.setPassword(password);
				    
				  //extra config code
				    java.util.Properties config = new java.util.Properties(); 
				    config.put("StrictHostKeyChecking", "no");
				    session.setConfig(config);
				    
				    session.connect();

				    command = String.format("useradd -p %s -m %s -d /var/www/vhosts/satellite-multimedia.com/%s.satellite-multimedia.com/%s.satellite-multimedia.com -o -u %s -g psacln -k /etc/skel_ftp -s /bin/false",
				    		                       pass, userName, Settings.getChefDeProjet(), userName, uid_chefDeProjet);
				    
				    envoiCommande(command);

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
	
	public static boolean addReadme(String userName, String pass){
		
		done = false;
		password = app.decryptedUserPassword;
		
	    Runnable runCreate = new Runnable() {
		
			@Override
			public void run() {
				
				String command = "";
				
				JSch jsch=new JSch();  
				try {
					session = jsch.getSession("root", Settings.getAdresse_serveur(), 22);
   
				    session.setPassword(password);
				    
				  //extra config code
				    java.util.Properties config = new java.util.Properties(); 
				    config.put("StrictHostKeyChecking", "no");
				    session.setConfig(config);
				    
				    session.connect();

//				    command = String.format("printf 'adresse : ftp://%s.satellite-multimedia.com\\nlogin : %s\\npass : %s' > /var/www/vhosts/satellite-multimedia.com/%s.satellite-multimedia.com/%s.satellite-multimedia.com/README.txt",
//				    		                Settings.getChefDeProjet(), userName , pass, Settings.getChefDeProjet(), userName );
				    
				    command = String.format("printf 'adresse : ftp://%s\\r\\nlogin : %s\\r\\npass : %s' > /var/www/vhosts/satellite-multimedia.com/%s.satellite-multimedia.com/%s.satellite-multimedia.com/README.txt",
    		                Settings.getAdresse_serveur(), userName , pass, Settings.getChefDeProjet(), userName );
				    
				    envoiCommande(command);

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
					session = jsch.getSession("root", Settings.getAdresse_serveur(), 22);
   
				    session.setPassword(password);
				    
				  //extra config code
				    java.util.Properties config = new java.util.Properties(); 
				    config.put("StrictHostKeyChecking", "no");
				    session.setConfig(config);
				    
				    session.connect();

                    command = String.format("echo '%s:%s' | chpasswd",
                            userName, pass);

                    envoiCommande(command);
    
				    
				} catch (JSchException | IOException e) {
					done = false;
					System.out.println("\n[ERROR] commande : '" + command + "' echouée\n");
					e.printStackTrace();

				}
				    
				    
			}
	    };
	    
	    runCreate.run();
	    
	    WriteConfig.addServeur(userName, Settings.getAdresse_serveur(), userName, fenetre);
	    
	    return done;
	}
	
	public static boolean CreateDir(String dir){
		
		Caporal.connect();

		done = false;
		
	    Runnable runCreate = new Runnable() {
		
			@Override
			public void run() {
				
				System.out.println("entered run()");
				
				String command = "";
				
				JSch jsch=new JSch();  
				try {
					session = jsch.getSession("root", Settings.getAdresse_serveur(), 22);
   
				    session.setPassword(password);
				    
				  //extra config code
				    java.util.Properties config = new java.util.Properties(); 
				    config.put("StrictHostKeyChecking", "no");
				    session.setConfig(config);
				    
				    session.connect();
				    
				    System.out.println("session.connect() connected");

				    command = String.format("mkdir %s", dir);
				    
				    envoiCommande(command);

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
	
    public static boolean moveFile(String file, String destination){


		done = false;
		//password = app.decryptedUserPassword;
		
	    Runnable runCreate = new Runnable() {
		
			@Override
			public void run() {
				
				String command = "";
				
				JSch jsch=new JSch();  
				try {
					session = jsch.getSession("root", Settings.getAdresse_serveur(), 22);
   
				    session.setPassword(password);
				    
				  //extra config code
				    java.util.Properties config = new java.util.Properties(); 
				    config.put("StrictHostKeyChecking", "no");
				    session.setConfig(config);
				    
				    session.connect();

				    command = String.format("mv '%s' %s", file, destination);
				    
				    envoiCommande(command);

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
    
    public static String traduction_chemin(String username, String chef_de_projet){
    	
    	String file;
    	
    	ArrayList<String> anciens = new ArrayList<>();
    	anciens.addAll(Arrays.asList(new String [] {"casto", "agglo", "earth", "fast", "fire", "go", "ready", "speed", "water", "wind"}));
    	
    	if (username.equals(chef_de_projet) || anciens.contains(username)){
    		file = Settings.getRacine_serveur() + "/" + username + ".satellite-multimedia.com";
    		System.out.println("cas 1 : " + file);
    	}
    	else{
    		file = Settings.getRacine_serveur() + "/" + chef_de_projet + ".satellite-multimedia.com/" + username + ".satellite-multimedia.com";
    		System.out.println("cas 2 : " + file);
    	}
    	
    	return file;
    }
    
    static void nouvelle_commande(String file, String chef) {
    	
    	System.out.println("nouvelle commande pour : " + file);
    	
    	command = String.format("python /ftpscript/FTP_alert/main_cli.py  '%s' %d %d", file, Math.round(Settings.getSeuilVert()), Math.round(Settings.getTailleMin()));
	    System.out.println(command);
	    
		
		JSch jsch=new JSch(); 
	    try {
	    	session = jsch.getSession(chef, Settings.getAdresse_serveur(), 22);
	    	   
		    session.setPassword(password);
		    
		  //extra config code
		    java.util.Properties config = new java.util.Properties(); 
		    config.put("StrictHostKeyChecking", "no");
		    session.setConfig(config);
		    
		    session.connect();
			envoiCommande(command);
		} catch (JSchException | IOException e) {
			System.out.println("\n[ERROR] commande : '" + command + "' echouée\n");
			e.printStackTrace();
			done = false;
		}
    }
    
    public static String listDir(String username, String chef_de_projet, String pass){
    	
    	System.out.println("exécution listdir() pour " + username);
    	
    	final Stream<String> files = Arrays.asList(username.split(",")).stream().map(a -> traduction_chemin(a.trim(), chef_de_projet));

		done = false;
		password = pass;
		
	    Runnable runCreate = new Runnable() {
		
			@Override
			public void run() {
				sortie = "";
				files.forEach(a -> nouvelle_commande(a, chef_de_projet));	    
			}
	    };
	    
	    runCreate.run();
	    
	    System.out.println(sortie);

	    return sortie;
	}
	
	public static String envoiCommande(String command) throws JSchException, IOException{
		
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
