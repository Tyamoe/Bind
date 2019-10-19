package application;
	
import java.awt.AWTException;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import controller.MainPageController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;

public class Main extends Application implements NativeKeyListener
{
	public static Main mInstance = null;

	public static boolean Searching = false;
    private boolean primaryKey = false;
    private boolean secondaryKey = false;
    private Boolean tertiaryKey = false;
    private boolean Blank = false;
    
    private static String[][] ListenKeys;
    private static String[][] ListenFor1;
    private static String[][] ListenFor2;
    
    private int ListenCount1 = 0;
    private int ListenCount2 = 0;
    
    private String currentText = "";
    private String currentKey1 = "";
    private String currentKey2 = "";
    private String currentKey3 = "";
	
	public Main()
	{
		mInstance = this;
	}
	
	static Preferences prefs = Preferences.userNodeForPackage(Main.class);
	
	private static Stage mainStage;

    private static TrayIcon trayIcon;
    private String nextKey = "";
    
	@Override
	public void start(Stage primaryStage) 
	{
		Platform.setImplicitExit(false);
		try 
		{
			Parent root = FXMLLoader.load(getClass().getResource("/fxml/MainPage.fxml"));
			Scene scene = new Scene(root);
			
			primaryStage.setScene(scene);
			mainStage = primaryStage;
			
			Image image = new Image("/images/binds.png");
			mainStage.getIcons().add(image);
			
			createTrayIcon(mainStage);
			
			mainStage.show();
		} 
		catch(Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	public void createTrayIcon(final Stage stage) 
    {
		if (SystemTray.isSupported()) 
		{
	      	SystemTray tray = SystemTray.getSystemTray();
	           
	      	java.awt.Image image = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/binds.png"));
	            
	      	mainStage.setOnCloseRequest(new EventHandler<WindowEvent>()
	      	{
	      		@Override
	      		public void handle(WindowEvent t) 
	      		{
	      			hide(mainStage);
	      		}
	    	});
	            
	      	final ActionListener closeListener = new ActionListener() 
	      	{
	      		@Override
	      		public void actionPerformed(java.awt.event.ActionEvent e)
	      		{
	      			System.exit(0);
	      		}
	      	};
	
	      	ActionListener showListener = new ActionListener() 
	      	{
	         	@Override
	         	public void actionPerformed(java.awt.event.ActionEvent e) 
	         	{
	         		Platform.runLater(new Runnable()
	         		{
	         			@Override
	         			public void run()
	         			{
	         				mainStage.show();
	         			}
	         		});
	        	}
	     	 };
	            
	    	PopupMenu popup = new PopupMenu();
	            
	      	MenuItem showItem = new MenuItem("Open");
	      	showItem.addActionListener(showListener);
	   		popup.add(showItem);
	
	   		MenuItem closeItem = new MenuItem("Exit");
	   		closeItem.addActionListener(closeListener);
	 	 	popup.add(closeItem);
	            
	 	 	trayIcon = new TrayIcon(image, "binds", popup);
	 	 	trayIcon.setImageAutoSize(true);
	 	 	trayIcon.addActionListener(showListener);
	
	 	 	boolean TrayIsIn = false;
	 	 	int whichTray = -1;
	 	 	TrayIcon[] tCon = tray.getTrayIcons();
	            
	 	 	for(int p = 0; p < tCon.length; p++)
	 	 	{
	 	 		if(tCon[p].getToolTip().equals(trayIcon.getToolTip()))
	   	    	{
	 	 			TrayIsIn = true;
	 	 			whichTray = p;
	 	 			break;
	   	    	}
	 	 	}
	            
	 	 	if(TrayIsIn)
	 	 	{
	 	 		TrayIcon itemp = tCon[whichTray];
	 	 		tray.remove(itemp);
	            	
	 	 		try 
	 	 		{
	 	 			tray.add(trayIcon);
	 	 		} 
	 	 		catch (AWTException e) 
	 	 		{
	 	 			System.err.println(e);
	 	 		}
	 	 	}
	 	 	else
	   	    {
	 	 		try 
	 	 		{
	 	 			tray.add(trayIcon);
	 	 		} 
	 	 		catch (AWTException e) 
	 	 		{
	 	 			System.err.println(e);
	 	 		}
	   	    }
		}
    }
	
	public void ResetListenKeys()
	{
		String[][] newKeys = new String[1][4];
		System.arraycopy(ListenKeys, 0, newKeys, 0, ListenKeys.length);
	
		ListenKeys = newKeys;
		ListenKeys[0][0] = "admin";
		ListenKeys[0][1] = "Ctrl";
		ListenKeys[0][2] = "K";
		ListenKeys[0][3] = "";
	}
	
	public void SetListenKeys(String[][]temp)
	{
		ListenKeys = temp;
		ListenFor1 = new String[1][4];
	}
	
	public void nativeKeyPressed(NativeKeyEvent e) 
	{
        if(Searching)
        {
        	nextKey = NativeKeyEvent.getKeyText(e.getKeyCode());
    		
    		if(!primaryKey)
    		{
    			String[][] k = new String[1][4];
    			ListenFor1 = k;
    			ListenCount1 = 0;
    			
    			for(int c = 0; c < ListenKeys.length; c++)
    	        {
    		        currentKey1 = ListenKeys[c][1];
    		        
    		        if(currentKey1.equals(nextKey))
    		        {
    		        	if(ListenCount1 == 0)
    		        	{
    		        		ListenFor1[ListenCount1][0] = ListenKeys[c][0];
    		        		ListenFor1[ListenCount1][1] = ListenKeys[c][1];
    		        		ListenFor1[ListenCount1][2] = ListenKeys[c][2];
    		        		ListenFor1[ListenCount1][3] = ListenKeys[c][3];
    		        	}
    		        	else
    		        	{
    		        		String[][] l = new String[ListenFor1.length + 1][4];
    		        		System.arraycopy(ListenFor1, 0, l, 0, ListenFor1.length);   
    		        		ListenFor1 = l;
    		        		ListenFor1[ListenFor1.length-1][0] = ListenKeys[c][0];
    		        		ListenFor1[ListenFor1.length-1][1] = ListenKeys[c][1];
    		        		ListenFor1[ListenFor1.length-1][2] = ListenKeys[c][2];
    		        		ListenFor1[ListenFor1.length-1][3] = ListenKeys[c][3];
    		        	}
    		        	ListenCount1++;
    		        	primaryKey = true;
    		        }
    	        }
    		}
    		else
    		{
    			if(!secondaryKey)
    			{
    				String[][] w = new String[1][4];
        			ListenFor2 = w;
        			ListenCount2 = 0;
        			
    				for(int c = 0; c < ListenFor1.length; c++)
        	        {
        		        currentKey2 = ListenFor1[c][2];
        		        
        		        if(currentKey2.equals(nextKey))
        		        {
        		        	if(ListenCount2 == 0)
        		        	{
        		        		ListenFor2[ListenCount2][0] = ListenFor1[c][0];
        		        		ListenFor2[ListenCount2][1] = ListenFor1[c][1];
        		        		ListenFor2[ListenCount2][2] = ListenFor1[c][2];
        		        		ListenFor2[ListenCount2][3] = ListenFor1[c][3];
        		        	}
        		        	else
        		        	{
        		        		String[][] a = new String[ListenFor2.length + 1][4];
        		        		System.arraycopy(ListenFor2, 0, a, 0, ListenFor2.length);   
        		        		
        		        		ListenFor2 = a;
        		        		
        		        		ListenFor2[ListenFor2.length-1][0] = ListenFor1[c][0];
        		        		ListenFor2[ListenFor2.length-1][1] = ListenFor1[c][1];
        		        		ListenFor2[ListenFor2.length-1][2] = ListenFor1[c][2];
        		        		ListenFor2[ListenFor2.length-1][3] = ListenFor1[c][3];
        		        	}
        		        	secondaryKey = true;
        		        	
        		        	//************************************************
        		        	String f = ListenFor2[ListenCount2][3];
             		        
             		        if(f.equals(""))
             		        {
                 	        	currentText = ListenFor2[ListenCount2][0];
                 	        	Blank = true;
             		        }
        		        	//************************************************
        		        	ListenCount2++;
        		        }
        	        }
    				if(Blank)
    				{
    					int fCount = 0;
    					for(int c = 0; c < ListenFor2.length; c++)
            	        {
    						String f = ListenFor2[c][3];
             	        	
    						if(f.equals(""))
             		        {
                 	        	currentText = ListenFor2[c][0];
                 	        	fCount++;
             		        }
            	        }
    					if(fCount == ListenFor2.length)
    					{
    						Blank = false;
    						
        		        	Searching = false;
        		        	
        		        	try 
        		        	{
        						MainPageController.mpInstance.Shortcut(currentText);
        					} 
        		        	catch (AWTException e1) 
        		        	{
        						e1.printStackTrace();
        					}
        		        	
        		        	primaryKey = false;
        		        	secondaryKey = false;
        		        	tertiaryKey = false;
        		        	
        		        	try 
        		        	{
        						Thread.sleep(1000);
        					} 
        		        	catch (InterruptedException e1) 
        		        	{
        						e1.printStackTrace();
        					}
        		        	Searching = true;
        		        	return;
    					}
    				}
    			}
    			else
    			{
    				if(!tertiaryKey)
    				{
    					for(int c = 0; c < ListenFor2.length; c++)
            	        {
            		        currentKey3 = ListenFor2[c][3];
            		        
            		        if(currentKey3.equals(nextKey))
            		        {
            		        	Blank = false;
            		        	currentText = ListenFor2[c][0];

            		        	Searching = false;
            		        	
            		        	try 
            		        	{
            						MainPageController.mpInstance.Shortcut(currentText);
            					} 
            		        	catch (AWTException e1) 
            		        	{
            						e1.printStackTrace();
            					}
            		        	
            		        	primaryKey = false;
            		        	secondaryKey = false;
            		        	tertiaryKey = false;
            		        	
            		        	try 
            		        	{
            						Thread.sleep(1000);
            					} 
            		        	catch (InterruptedException e1) 
            		        	{
            						e1.printStackTrace();
            					}
            		        	Searching = true;
            		        	return;
            		        }
            	        }
    				}
    			}
    		}
    		
	        //If Blank found wait for x seconds then type blankText
    		new java.util.Timer().schedule(

    			    new java.util.TimerTask() 
    			    {
    			        @Override
    			        public void run() 
    			        {
    			        	if(!tertiaryKey && Blank)
    			        	{
            		        	Searching = false;
            		        	
            		        	try 
            		        	{
            						MainPageController.mpInstance.Shortcut(currentText);
            					} 
            		        	catch (AWTException e1) 
            		        	{
            						e1.printStackTrace();
            					}
            		        	
            		        	primaryKey = false;
            		        	secondaryKey = false;
            		        	tertiaryKey = false;
            		        	Blank = false;
            		        	
            		        	try 
            		        	{
            						Thread.sleep(1000);
            					} 
            		        	catch (InterruptedException e1) 
            		        	{
            						e1.printStackTrace();
            					}
            		        	Searching = true;
            		        	return;
    			        	}
    			        }
    			    }, 
    			    1500 
    			);
    		
    		//if x time passed reset
    		new java.util.Timer().schedule(

    			    new java.util.TimerTask() 
    			    {
    			        @Override
    			        public void run() 
    			        {
    			        	if(!secondaryKey && primaryKey)
    			        	{
	    			        	primaryKey = false;
	        		        	secondaryKey = false;
	        		        	tertiaryKey = false;
	        		        	Blank = false;
	    			        	Searching = true;
    			        	}
    			        }
    			    }, 
    			    2000 
    			);
        }
    }
	
	public void nativeKeyReleased(NativeKeyEvent e) 
	{
		if(NativeKeyEvent.getKeyText(e.getKeyCode()).equals(currentKey1) && primaryKey) 
		{
			primaryKey = false;
		}
	        
	 	if(NativeKeyEvent.getKeyText(e.getKeyCode()).equals(currentKey2) && secondaryKey) 
		{
	      	secondaryKey = false;
	 	}
	        
	  	if(NativeKeyEvent.getKeyText(e.getKeyCode()).equals(currentKey3) && tertiaryKey) 
	 	{
	  		tertiaryKey = false;
		}
	}

	public void nativeKeyTyped(NativeKeyEvent arg0)  { }
	
	private static void hide(final Stage stage)
    {
        Platform.runLater(new Runnable()
        {
            @Override
            public void run() 
            {
                if (SystemTray.isSupported())
                {
                    stage.hide();
                } 
                else
                {
                    System.exit(0);
                }
            }
        });
    }
	
	public String encode(String enc, int offset) 
	{
	    offset = offset % 26 + 26;
	    StringBuilder encoded = new StringBuilder();
	    for (char i : enc.toCharArray()) 
	    {
	        if (Character.isLetter(i)) 
	        {
	            if (Character.isUpperCase(i))
	            {
	                encoded.append((char) ('A' + (i - 'A' + offset) % 26));
	            } 
	            else 
	            {
	                encoded.append((char) ('a' + (i - 'a' + offset) % 26));
	            }
	        } 
	        else 
	        {
	            encoded.append(i);
	        }
	    }
	    return encoded.toString();
	}
	
	public String decode(String enc, int offset) 
	{
	    return encode(enc, 26 - offset);
	}
		
	public static void main(String[] args) 
	{
		// Get the logger for "org.jnativehook" and set the level to warning.
		Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
		logger.setLevel(Level.WARNING);

		// Don't forget to disable the parent handlers.
		logger.setUseParentHandlers(false);
		
		try 
		{
            GlobalScreen.registerNativeHook();
        }
        catch (NativeHookException ex) 
		{
            System.err.println("There was a problem registering the native hook.");
            System.err.println(ex.getMessage());

            System.exit(1);
        }

        GlobalScreen.addNativeKeyListener(new Main());
        
		launch(args);
	}
}