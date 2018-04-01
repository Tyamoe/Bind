package controller;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import  java.util.prefs.*;

import application.Main;

public class MainPageController
{
	public static MainPageController mpInstance = null;
	
	public MainPageController()
	{
		mpInstance = this;
	}
	
	public void initialize()
	{
        System.out.println("I am running!w");
        SetupPage();
	}
	
	//NODES
	
	@FXML
	private AnchorPane ThePane;
	
	@FXML
	private AnchorPane SettingsPane;
	
	@FXML
	private AnchorPane SettingsDisplay;
	
	@FXML
	private AnchorPane ConfirmationPane;
	
	@FXML
	private TextField fieldPName;
	
	@FXML
	private TextField fieldPassword1;
	
	@FXML
	private PasswordField fieldPassword2;
	
	@FXML
	private TextField fieldPassword111;
	
	@FXML
	private PasswordField fieldPassword211;
	
	@FXML
	private TextField fieldPassword11;
	
	@FXML
	private PasswordField fieldPassword21;
	
	@FXML
	private AnchorPane EnterPasswordPane;

	@FXML
	private Label lblPassword;

	@FXML
	private Label lblName;

	@FXML
	private Label lblPassword1;
	
	@FXML
	private Button btnSettings;

	@FXML
	private Button btnShowHide1;
	
	@FXML
	private Button btnShowHide21;
	
	@FXML
	private Button btnShowHide2;
	
	@FXML
	private AnchorPane MenuBar;
		
		@FXML
		private Label MainHeader;

		@FXML
		private MenuButton btnProfile;
		
		@FXML
		private Button btnRefresh;

	@FXML
	private AnchorPane CreateProfilePane;
	
		@FXML
		private TextField fieldProfileName;
	
	@FXML
	private ScrollPane sPane;
		
		@FXML
		private AnchorPane Container;
	
	@FXML
	private Button btnAdd;
	
	@FXML
	private Button btnEdit;
	
	@FXML
	private Button btnDelete;
	
	@FXML
	private Button btnSubmit;
	
	@FXML
	private Button btnCancel;
	
	private AnchorPane[] ShortcutPaneArray;
	private TextField[] ShortcutTextArray;
	private TextField[] ShortcutKey1Array;
	private TextField[] ShortcutKey2Array;
	private TextField[] ShortcutKey3Array;
	private CheckBox[] ShortcutBoxArray;
	
	private AnchorPane newPane;
	private TextField newText;
	private TextField newKey1;
	private TextField newKey2;
	private TextField newKey3;
	private CheckBox newBox;
	
	private Tooltip tipText = new Tooltip();
	private Tooltip tipKey = new Tooltip();
	
	public Timeline timeline;
	
	//Vars
	
	Preferences prefs = Preferences.userNodeForPackage(MainPageController.class);
	
	private String currentID = "";
	
	private String ProfileName = ""; 
	private int ProfileID = -1;
	
	private int State = -1;
	private String[][] thisKeyList;
	
	public void SetupPage()
	{
		System.out.println("Setup Page");
		///Reseting!
		//prefs.putInt("Profiles", 0);
		//

		Main.Searching = false;
		
		btnSettings.setDisable(true);
		btnRefresh.setDisable(true);
		
		btnAdd.setDisable(true);
		btnEdit.setDisable(true);
		
		btnAdd.setVisible(false);
		btnEdit.setVisible(false);
		
		btnDelete.setVisible(false);
		btnCancel.setVisible(false);
		btnSubmit.setVisible(false);

		btnDelete.setDisable(true);
		btnCancel.setDisable(true);
		btnSubmit.setDisable(true);
		
		btnProfile.getItems().clear();
		
		tipText.setText("Place Text Here!");
		tipKey.setText("(Left Mouse) Click Here then press the key you want!");
		
		hackTooltipStartTiming(tipText);
		hackTooltipStartTiming(tipKey);
		
		int profCount = prefs.getInt("Profiles", 0);
		String[] tempProf = new String[profCount];
		String[][] splitProf = new String[profCount][3];
		
		for(int p = 0; p < profCount; p++)
		{
			tempProf[p] = prefs.get("PROF" + p, "");
			System.out.println("tempProf: " + tempProf[p]);
			
			if(tempProf[p].equals("8/:Deleted_0"))
			{
				continue;
			}
			
			String[] temp = tempProf[p].split(":");

		//	System.out.println(temp.length + " prefs..." + temp[2]);
			
			for(int l = 0; l < temp.length; l++)
			{

				System.out.println(l + ". " + temp[l]);
			}
			
			System.out.println(profCount + " Info..." + tempProf[p]);
			
			for(int r = 0; r < 3; r++)
			{
				splitProf[p][r] = temp[r];
			}
			
			MenuItem newItem = new MenuItem();
			
			newItem.setText(splitProf[p][0]);
			newItem.setUserData(splitProf[p][1] + ":" + splitProf[p][2]);
			
			newItem.setOnAction(new EventHandler<ActionEvent>() 
			{
				@Override
				public void handle(ActionEvent event) 
				{
					System.out.println("Setting Up Display..." + newItem.getUserData().toString());
					String g = newItem.getUserData().toString();
					String[] h = g.split(":");
					g = h[0];
					
					/*
						btnShowHide1.setId("showhide1");
						fieldPassword21.setVisible(true);
						fieldPassword11.setVisible(false);
					 */
					//If profile is password protected open password pane
					if(prefs.getBoolean(g + "isProtected", false))
					{
						EnterPasswordPane.setVisible(true);
						
						btnShowHide21.setId("showhide2");
						Password = true;
						fieldPassword211.setVisible(true);
						fieldPassword111.setVisible(false);
						
						currentID = g;
						ProfileName = newItem.getText();
						ProfileID = Integer.parseInt(h[1]);
						
						return;
					}
					
					ProfileName = newItem.getText();
					ProfileID = Integer.parseInt(h[1]);
					btnProfile.setTooltip(new Tooltip(ProfileName));
					SetupDisplay(g);
				}
			});
			
			btnProfile.getItems().add(newItem);
			System.out.println("Check");
		}
		
		MenuItem AddNew = new MenuItem();
		
		AddNew.setText("New");
		
		AddNew.setOnAction(new EventHandler<ActionEvent>() 
		{
			@Override
			public void handle(ActionEvent event) 
			{
				System.out.println("Showing Profile Pane!");
				CreateProfilePane.setVisible(true);
			}
		});
		btnProfile.getItems().add(AddNew);
		
		if(profCount > 0)
		{
			//Add Edit Profile Options
		}
	}
	
	@SuppressWarnings("static-access")
	public void SetupDisplay(String Identity)
	{
		System.out.println("Setting Up Page!");
		Main.mInstance.Searching = true;
		
		Container.getChildren().clear();
		
		btnSettings.setDisable(false);
		btnRefresh.setDisable(false);
		
		btnAdd.setDisable(false);
		btnEdit.setDisable(false);
		
		btnAdd.setVisible(true);
		btnEdit.setVisible(true);
		
		btnDelete.setVisible(false);
		btnCancel.setVisible(false);
		btnSubmit.setVisible(false);

		btnDelete.setDisable(true);
		btnCancel.setDisable(true);
		btnSubmit.setDisable(true);
		
		currentID = Identity;
		
		fieldPName.setText(ProfileName);
		
		if(prefs.getBoolean(currentID + "isProtected", false))
		{
			Tooltip tip = new Tooltip();
			tip.setText("This program does not save your password. If you would like to remove the password from this profile leave this field blank and click the change button.");
			
			fieldPassword1.setTooltip(tip); 
			fieldPassword2.setTooltip(tip); 
		}
		else
		{
			Tooltip tip = new Tooltip();
			tip.setText("To add a password to this profile enter it and click the change button.");
			
			fieldPassword1.setTooltip(tip); 
			fieldPassword2.setTooltip(tip); 
		}
		
		int ShortcutCount = prefs.getInt(Identity + "ShortcutCount", 0);
		System.out.println("ShortcutCount: " + ShortcutCount);
		
		if(ShortcutCount > 5)
		{
			Container.setPrefSize(600.0, (365.0) + ((ShortcutCount - 5) * 55.0));
		}

		String[][] keyList = new String[ShortcutCount][4];
		String[] Text = new String [ShortcutCount];
		
		String[] Key1 = new String [ShortcutCount];
		String[] Key2 = new String [ShortcutCount];
		String[] Key3 = new String [ShortcutCount];
		
		AnchorPane[] ShortcutPane = new AnchorPane[ShortcutCount];
		TextField[] ShortcutText = new TextField[ShortcutCount];
		TextField[] ShortcutKey1 = new TextField[ShortcutCount];
		TextField[] ShortcutKey2 = new TextField[ShortcutCount];
		TextField[] ShortcutKey3 = new TextField[ShortcutCount];
		CheckBox[] ShortcutBox = new CheckBox[ShortcutCount];
		
		for(int u = 0; u < ShortcutCount; u++)
		{
			System.out.println("u: " + u);
			
			String prefName = Identity + "SHORT" + (u);
			System.out.println("prefName: " + prefName);
			
			Text[u] = prefs.get(prefName, "");
			System.out.println("Text[u]: " + Text[u]);
			Text[u] = Main.mInstance.decode(Text[u], 7);
			System.out.println("Text[u]: " + Text[u]);
			
			Key1[u] = prefs.get(Identity + "Key1" + (u), "");
			Key2[u] = prefs.get(Identity + "Key2" + (u), "");
			Key3[u] = prefs.get(Identity + "Key3" + (u), "");
			
			keyList[u][0] = Text[u];
			keyList[u][1] = Key1[u];
			keyList[u][2] = Key2[u];
			keyList[u][3] = Key3[u];
			
			System.out.println("THIS::: " + Identity + "Key1" + (u));
			System.out.println(Key1[u] + " " + Key2[u] + " " + Key3[u]);
			
			ShortcutPane[u] = new AnchorPane();
			
			ShortcutText[u] = new TextField();
			ShortcutKey1[u] = new TextField();
			ShortcutKey2[u] = new TextField();
			ShortcutKey3[u] = new TextField();
			ShortcutBox[u] = new CheckBox();
			
			ShortcutText[u].setText(Text[u]);
			ShortcutKey1[u].setText(Key1[u]);
			ShortcutKey2[u].setText(Key2[u]);
			ShortcutKey3[u].setText(Key3[u]);
			ShortcutBox[u].setText("");
			
			ShortcutText[u].setId(u + "");
			ShortcutKey1[u].setId(u + "");
			ShortcutKey2[u].setId(u + "");
			ShortcutKey3[u].setId(u + "");
			ShortcutBox[u].setId(u + "");

			ShortcutText[u].setAlignment(Pos.CENTER);
			ShortcutKey1[u].setAlignment(Pos.CENTER);
			ShortcutKey2[u].setAlignment(Pos.CENTER);
			ShortcutKey3[u].setAlignment(Pos.CENTER);
			
			ShortcutText[u].setDisable(true);
			ShortcutKey1[u].setDisable(true);
			ShortcutKey2[u].setDisable(true);
			ShortcutKey3[u].setDisable(true);
			
			ShortcutBox[u].setDisable(true);
			ShortcutBox[u].setVisible(false);
			
			ShortcutText[u].setStyle("-fx-opacity: 1;");
			ShortcutKey1[u].setStyle("-fx-opacity: 1;");
			ShortcutKey2[u].setStyle("-fx-opacity: 1;");
			ShortcutKey3[u].setStyle("-fx-opacity: 1;");
			
			ShortcutPane[u].setStyle("-fx-background-color: transparent;");
			
			ShortcutKey1[u].setEditable(false);
			ShortcutKey2[u].setEditable(false);
			ShortcutKey3[u].setEditable(false);
			
			ShortcutBox[u].setOnAction((new EventHandler<ActionEvent>() 
			{
				@Override
				public void handle(ActionEvent event) 
				{
					String Selected = "-fx-background-color: red;";
					String notSelected = "-fx-background-color: transparent;";
					
					CheckBox c = (CheckBox)event.getSource();
					AnchorPane p = (AnchorPane)c.getParent();
					
					if(p.getStyle().equals(Selected))
					{
						p.setStyle(notSelected);
					}
					else
					{
						p.setStyle(Selected);
					}
				}
			}));
			
			ShortcutKey1[u].setOnMouseClicked((new EventHandler<MouseEvent>() 
			{
				@Override
				public void handle(MouseEvent event) 
				{
					System.out.println("Listening for Key 1");
					
					TextField thisField = (TextField)event.getSource();
					
					thisField.setOnKeyPressed(new EventHandler<javafx.scene.input.KeyEvent>() 
					{
						@Override
						public void handle(javafx.scene.input.KeyEvent e)
						{
							System.out.println("Key Pressed " + e.getCode().getName() + " " + e.getText());
							thisField.setText(e.getCode().getName());
						}
					});
				}
			}));
			ShortcutKey2[u].setOnMouseClicked((new EventHandler<MouseEvent>() 
			{
				@Override
				public void handle(MouseEvent event) 
				{
					System.out.println("Listening for Key 2");
					
					TextField thisField = (TextField)event.getSource();
					
					thisField.setOnKeyPressed(new EventHandler<javafx.scene.input.KeyEvent>() 
					{
						@Override
						public void handle(javafx.scene.input.KeyEvent e)
						{
							System.out.println("Key Pressed " + e.getCode().getName() + " " + e.getText());
							thisField.setText(e.getCode().getName());
						}
					});
				}
			}));
			ShortcutKey3[u].setOnMouseClicked((new EventHandler<MouseEvent>() 
			{
				@Override
				public void handle(MouseEvent event) 
				{
					System.out.println("Listening for Key 3");
					
					TextField thisField = (TextField)event.getSource();
					
					thisField.setOnKeyPressed(new EventHandler<javafx.scene.input.KeyEvent>() 
					{
						@Override
						public void handle(javafx.scene.input.KeyEvent e)
						{
							System.out.println("Key Pressed " + e.getCode().getName() + " " + e.getText());
							thisField.setText(e.getCode().getName());
						}
					});
				}
			}));
			
			ShortcutPane[u].getChildren().addAll(ShortcutText[u], ShortcutBox[u], ShortcutKey1[u], ShortcutKey2[u], ShortcutKey3[u]);
			
			Container.getChildren().add(ShortcutPane[u]);
			
			double PaneTop = (20.0 * (u + 1)) + (u * 35);
			System.out.println("PaneTop: " + PaneTop);
			
			//PaneTop = (u == 0) ? PaneTop-=20 : PaneTop;
			System.out.println("PaneTop: " + PaneTop);
			
			ShortcutPane[u].setPrefSize(0.0, 35.0);
			AnchorPane.setTopAnchor(ShortcutPane[u], PaneTop);
			AnchorPane.setLeftAnchor(ShortcutPane[u], 0.0);
			AnchorPane.setRightAnchor(ShortcutPane[u], 15.0);
			AnchorPane.setBottomAnchor(ShortcutPane[u], null);
			
			AnchorPane.setTopAnchor(ShortcutText[u], 0.0);
			AnchorPane.setLeftAnchor(ShortcutText[u], 10.0);
			AnchorPane.setRightAnchor(ShortcutText[u], 370.0);
			AnchorPane.setBottomAnchor(ShortcutText[u], 0.0);
			
			AnchorPane.setTopAnchor(ShortcutBox[u], 0.0);
			AnchorPane.setLeftAnchor(ShortcutBox[u], 227.0);
			AnchorPane.setRightAnchor(ShortcutBox[u], 337.0);
			AnchorPane.setBottomAnchor(ShortcutBox[u], 0.0);
			
			AnchorPane.setTopAnchor(ShortcutKey1[u], 0.0);
			AnchorPane.setLeftAnchor(ShortcutKey1[u], 255.0);
			AnchorPane.setRightAnchor(ShortcutKey1[u], 250.0);
			AnchorPane.setBottomAnchor(ShortcutKey1[u], 0.0);
			
			AnchorPane.setTopAnchor(ShortcutKey2[u], 0.0);
			AnchorPane.setLeftAnchor(ShortcutKey2[u], 375.0);
			AnchorPane.setRightAnchor(ShortcutKey2[u], 130.0);
			AnchorPane.setBottomAnchor(ShortcutKey2[u], 0.0);
			
			AnchorPane.setTopAnchor(ShortcutKey3[u], 0.0);
			AnchorPane.setLeftAnchor(ShortcutKey3[u], 495.0);
			AnchorPane.setRightAnchor(ShortcutKey3[u], 10.0);
			AnchorPane.setBottomAnchor(ShortcutKey3[u], 0.0);
		}
		
		ShortcutPaneArray = ShortcutPane;
		ShortcutTextArray = ShortcutText;
		ShortcutBoxArray = ShortcutBox;
		ShortcutKey1Array = ShortcutKey1;
		ShortcutKey2Array = ShortcutKey2;
		ShortcutKey3Array = ShortcutKey3;

		Main.mInstance.SetListenKeys(keyList);
		thisKeyList = keyList;
	}
	
	public void Refresh(ActionEvent e)
	{
		System.out.println("Refreshing!");
		SetupDisplay(currentID);
	}
	
	public void CreateProfile(ActionEvent e)
	{
		System.out.println("Showing Profile Pane!");
		CreateProfilePane.setVisible(true);
		
		Password = true;
		btnShowHide1.setId("showhide1");
		fieldPassword21.setVisible(true);
		fieldPassword11.setVisible(false);
	}
	
	public void ExitProfilePane(ActionEvent e)
	{
		System.out.println("Exiting Profile Pane!");
		CreateProfilePane.setVisible(false);
		fieldProfileName.setText("");
	}
	
	public void SubmitNewProfile(ActionEvent e)
	{
		System.out.println("Submitted New Profile!");
		
		String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
		SecureRandom rnd = new SecureRandom();
		
		StringBuilder sb = new StringBuilder(3);
		for( int i = 0; i < 3; i++ ) 
		{
			sb.append( AB.charAt(rnd.nextInt(AB.length())));
		}
		
		String ID = sb.toString();
		String Name = fieldProfileName.getText();
		String pass = fieldPassword21.getText();
		pass = pass.trim();
		
		boolean p = (pass.equals("") || pass.equals(" ")) ? false : true;
		
		if(p)
		{
			//Hash and Save Password
			String SALT = "my-salt-text";
			pass = SALT + pass;
			String hashPass = generateHash(pass);

			prefs.putBoolean(ID + "isProtected", true);
			prefs.put(ID + "Password", hashPass);
		}
		else
		{
			//Save no password
			prefs.putBoolean(ID + "isProtected", false);
			prefs.put(ID + "Password", "q");
		}
		
		prefs.put("PROF" + (prefs.getInt("Profiles", 0)), Name + ":" + ID + ":" + (prefs.getInt("Profiles", 0)));
		prefs.putInt(ID + "ShortcutCount", 0);

		System.out.println("Profiles: " + prefs.getInt("Profiles", 0));
		prefs.putInt("Profiles", (prefs.getInt("Profiles", 0)) + 1);
		System.out.println("Profiles: " + prefs.getInt("Profiles", 0));

		ProfileName = Name; 
		ProfileID = prefs.getInt("Profiles", 0);
		
		CreateProfilePane.setVisible(false);
		fieldProfileName.setText("");
		
		System.out.println("Created New Profile!");

		System.out.println("Details: " + ProfileName + " : " + pass + " : " + prefs.get("PROF" + (prefs.getInt("Profiles", 0)), ""));
		
		SetupPage();
		SetupDisplay(ID);
	}
	
	public void SubmitPassword(ActionEvent e)
	{
		HideError(lblPassword1);
		
		System.out.println("Submitted Pass");
		String input = fieldPassword211.getText();
		input = input.trim();
		System.out.println("input: " + input);
		
		if(input.equals("") || input.equals(" "))
		{
			ShowError(lblPassword1, "This field is required.");
			return;
		}
		
		//Hash and Save Password
		String SALT = "my-salt-text";
		input = SALT + input;
		String hashPass = generateHash(input);

		String match = prefs.get(currentID + "Password", "");
		
		System.out.println("Matches: " + hashPass + " = " + match);
		
		if(hashPass.equals(match))
		{
			HideError(lblPassword1);
			EnterPasswordPane.setVisible(false);
			
			btnShowHide21.setId("showhide2");
			Password = true;
			fieldPassword211.setVisible(true);
			fieldPassword111.setVisible(false);
			
			fieldPassword211.setText("");
			fieldPassword111.setText("");
			
			btnProfile.setTooltip(new Tooltip(ProfileName));
			SetupDisplay(currentID);
		}
		else
		{
			ShowError(lblPassword1, "Wrong Password.");
			return;
		}
	}
	
	private void ShowError(Label lbl, String str, int...juice)
	{
		System.out.println("Showing Error!");
		
		int y = (juice.length > 0) ? juice[0] : 0;
		
		Tooltip lblTip = new Tooltip();
		
		lblTip.setText(str);
		lbl.setTooltip(lblTip);
		
		if(y == 0)
		{
			lbl.setStyle("-fx-text-fill: red;");
		}
		else
		{
			lbl.setStyle("-fx-text-fill: green;");
		}
	}
	private void HideError(Label lbl)
	{
		System.out.println("Hiding Error!");
		lbl.setTooltip(null);
		
		lbl.setStyle("-fx-text-fill: black;");
	}
	
	public void HidePasswordPane(ActionEvent e)
	{
		EnterPasswordPane.setVisible(false);
		fieldPassword211.setText("");
		fieldPassword111.setText("");
		btnShowHide21.setId("showhide2");
	}
	
	public void AddNewShortcut(ActionEvent e)
	{
		System.out.println("Add New Shortcut!");

		btnAdd.setVisible(false);
		btnAdd.setDisable(true);
		btnEdit.setVisible(false);
		btnEdit.setDisable(true);
		
		btnSubmit.setVisible(true);
		btnSubmit.setDisable(false);
		
		btnCancel.setVisible(true);
		btnCancel.setDisable(false);
		
		State = 1;
		
		newPane = new AnchorPane();
		
		newText = new TextField();
		newKey1 = new TextField();
		newKey2 = new TextField();
		newKey3 = new TextField();
		newBox = new CheckBox();
		
		newText.setTooltip(tipText);
		newKey1.setTooltip(tipKey);
		newKey2.setTooltip(tipKey);
		newKey3.setTooltip(tipKey);
		
		newText.setPromptText("text to write");
		newKey1.setPromptText("hotkey 1");
		newKey2.setPromptText("hotkey 2");
		newKey3.setPromptText("hotkey 3");
		
		newBox.setDisable(true);
		newBox.setVisible(false);
		
		newText.setAlignment(Pos.CENTER);
		newKey1.setAlignment(Pos.CENTER);
		newKey2.setAlignment(Pos.CENTER);
		newKey3.setAlignment(Pos.CENTER);
		
		newKey1.setEditable(false);
		newKey2.setEditable(false);
		newKey3.setEditable(false);
		
		newPane.setStyle("-fx-background-color: transparent;");
		
		newBox.setOnAction((new EventHandler<ActionEvent>() 
		{
			@Override
			public void handle(ActionEvent event) 
			{
				String Selected = "-fx-background-color: red;";
				String notSelected = "-fx-background-color: transparent;";
				
				CheckBox c = (CheckBox)event.getSource();
				AnchorPane p = (AnchorPane)c.getParent();
				
				if(p.getStyle().equals(Selected))
				{
					p.setStyle(notSelected);
				}
				else
				{
					p.setStyle(Selected);
				}
			}
		}));
		
		newKey1.setOnMouseClicked((new EventHandler<MouseEvent>() 
		{
			@Override
			public void handle(MouseEvent event) 
			{
				System.out.println("Listening for Key 3");
				
				TextField thisField = (TextField)event.getSource();
				
				thisField.setOnKeyPressed(new EventHandler<javafx.scene.input.KeyEvent>() 
				{
					@Override
					public void handle(javafx.scene.input.KeyEvent e)
					{
						System.out.println("Key Pressed " + e.getCode().getName() + " " + e.getText());
						thisField.setText(e.getCode().getName());
					}
				});
			}
		}));
		newKey2.setOnMouseClicked((new EventHandler<MouseEvent>() 
		{
			@Override
			public void handle(MouseEvent event) 
			{
				System.out.println("Listening for Key 3");
				
				TextField thisField = (TextField)event.getSource();
				
				thisField.setOnKeyPressed(new EventHandler<javafx.scene.input.KeyEvent>() 
				{
					@Override
					public void handle(javafx.scene.input.KeyEvent e)
					{
						System.out.println("Key Pressed " + e.getCode().getName() + " " + e.getText());
						thisField.setText(e.getCode().getName());
					}
				});
			}
		}));
		newKey3.setOnMouseClicked((new EventHandler<MouseEvent>() 
		{
			@Override
			public void handle(MouseEvent event) 
			{
				System.out.println("Listening for Key 3");
				
				TextField thisField = (TextField)event.getSource();
				
				thisField.setOnKeyPressed(new EventHandler<javafx.scene.input.KeyEvent>() 
				{
					@Override
					public void handle(javafx.scene.input.KeyEvent e)
					{
						System.out.println("Key Pressed " + e.getCode().getName() + " " + e.getText());
						thisField.setText(e.getCode().getName());
					}
				});
			}
		}));
		
		int ShortcutCount = prefs.getInt(currentID + "ShortcutCount", 0);
		ShortcutCount++;
		System.out.println("ShortcutCount: " + ShortcutCount);
		
		if(ShortcutCount > 5)
		{
			Container.setPrefSize(600.0, (365.0) + ((ShortcutCount - 5) * 55.0));
		}
		
		newPane.getChildren().addAll(newText, newBox, newKey1, newKey2, newKey3);
		
		Container.getChildren().add(newPane);
		
		double top = 20.0 + ((ShortcutPaneArray.length) * 55);
		
		if(ShortcutPaneArray.length == 0)
		{
			top = 20.0;
		}
		
		newPane.setPrefSize(0.0, 35.0);
		AnchorPane.setTopAnchor(newPane, top);
		AnchorPane.setLeftAnchor(newPane, 0.0);
		AnchorPane.setRightAnchor(newPane, 15.0);
		AnchorPane.setBottomAnchor(newPane, null);
		
		AnchorPane.setTopAnchor(newText, 0.0);
		AnchorPane.setLeftAnchor(newText, 10.0);
		AnchorPane.setRightAnchor(newText, 370.0);
		AnchorPane.setBottomAnchor(newText, 0.0);
		
		AnchorPane.setTopAnchor(newBox, 0.0);
		AnchorPane.setLeftAnchor(newBox, 227.0);
		AnchorPane.setRightAnchor(newBox, 337.0);
		AnchorPane.setBottomAnchor(newBox, 0.0);
		
		AnchorPane.setTopAnchor(newKey1, 0.0);
		AnchorPane.setLeftAnchor(newKey1, 255.0);
		AnchorPane.setRightAnchor(newKey1, 250.0);
		AnchorPane.setBottomAnchor(newKey1, 0.0);
		
		AnchorPane.setTopAnchor(newKey2, 0.0);
		AnchorPane.setLeftAnchor(newKey2, 375.0);
		AnchorPane.setRightAnchor(newKey2, 130.0);
		AnchorPane.setBottomAnchor(newKey2, 0.0);
		
		AnchorPane.setTopAnchor(newKey3, 0.0);
		AnchorPane.setLeftAnchor(newKey3, 495.0);
		AnchorPane.setRightAnchor(newKey3, 10.0);
		AnchorPane.setBottomAnchor(newKey3, 0.0);
	}
	
	public void EditShortcut(ActionEvent e)
	{
		System.out.println("Edit Shortcut!");
		
		btnAdd.setVisible(false);
		btnAdd.setDisable(true);
		btnEdit.setVisible(false);
		btnEdit.setDisable(true);
		
		btnDelete.setVisible(true);
		btnDelete.setDisable(false);
		
		btnSubmit.setVisible(true);
		btnSubmit.setDisable(false);
		
		btnCancel.setVisible(true);
		btnCancel.setDisable(false);
		
		State = 2;
		
		for(int y = 0; y < ShortcutTextArray.length; y++)
		{
			ShortcutTextArray[y].setDisable(false);
			ShortcutKey1Array[y].setDisable(false);
			ShortcutKey2Array[y].setDisable(false);
			ShortcutKey3Array[y].setDisable(false);
		}
		
		btnEdit.setDisable(true);
	}
	
	public void DeleteShortcut(ActionEvent e)
	{
		System.out.println("Delete Shortcut!");
		
		btnDelete.setVisible(false);
		btnDelete.setDisable(true);
		
		btnSubmit.setVisible(true);
		btnSubmit.setDisable(false);
		
		btnCancel.setVisible(true);
		btnCancel.setDisable(false);
		
		State = 3;
		
		//Show CHeckboxes
		for(int h = 0; h < ShortcutBoxArray.length; h++)
		{
			ShortcutBoxArray[h].setDisable(false);
			ShortcutBoxArray[h].setVisible(true);
			
			ShortcutTextArray[h].setDisable(true);
			ShortcutKey1Array[h].setDisable(true);
			ShortcutKey2Array[h].setDisable(true);
			ShortcutKey3Array[h].setDisable(true);
			
			ShortcutTextArray[h].setStyle("-fx-opacity: 1;");
			ShortcutKey1Array[h].setStyle("-fx-opacity: 1;");
			ShortcutKey2Array[h].setStyle("-fx-opacity: 1;");
			ShortcutKey3Array[h].setStyle("-fx-opacity: 1;");
		}
	}
	
	public void Submit(ActionEvent e)
	{
		if(State == 1)
		{
			boolean Valid = false;
			
			String thisText = newText.getText();
			String thisKey1 = newKey1.getText();
			String thisKey2 = newKey2.getText();
			String thisKey3 = newKey3.getText();
			
			System.out.println("keys: " + thisText + thisKey1 + thisKey2 + thisKey3);
			
			if(!thisText.equals("") && !thisKey1.equals("") && !thisKey2.equals(""))
			{
				Valid = true;
			}
			
			if(!Valid)
			{
				return;
			}
			
			//Valid
			newText.setDisable(true);
			newKey1.setDisable(true);
			newKey2.setDisable(true);
			newKey3.setDisable(true);
			
			newText.setStyle("-fx-opacity: 1;");
			newKey1.setStyle("-fx-opacity: 1;");
			newKey2.setStyle("-fx-opacity: 1;");
			newKey3.setStyle("-fx-opacity: 1;");
			
			newText.setPromptText("");
			newKey1.setPromptText("");
			newKey2.setPromptText("");
			newKey3.setPromptText("");
			
			newText.setTooltip(null);
			newKey1.setTooltip(null);
			newKey2.setTooltip(null);
			newKey3.setTooltip(null);
			
			int curShort = prefs.getInt(currentID + "ShortcutCount", 0);
			System.out.println("curShort: " + curShort);
	
			prefs.putInt(currentID + "ShortcutCount", prefs.getInt(currentID + "ShortcutCount", 0)+1);
	
			String prefName = currentID + "SHORT" + curShort;
			prefs.put(prefName, Main.mInstance.encode(thisText, 7));
			
			prefs.put(currentID + "Key1" + curShort, thisKey1);
			prefs.put(currentID + "Key2" + curShort, thisKey2);
			prefs.put(currentID + "Key3" + curShort, thisKey3);
			
			btnAdd.setVisible(true);
			btnAdd.setDisable(false);
			
			btnSubmit.setVisible(false);
			btnSubmit.setDisable(true);
			
			AnchorPane[] ShortcutPane = new AnchorPane[ShortcutPaneArray.length + 1];
			TextField[] ShortcutText = new TextField[ShortcutTextArray.length + 1];
			TextField[] ShortcutKey1 = new TextField[ShortcutKey1Array.length + 1];
			TextField[] ShortcutKey2 = new TextField[ShortcutKey2Array.length + 1];
			TextField[] ShortcutKey3 = new TextField[ShortcutKey3Array.length + 1];
			CheckBox[] ShortcutBox = new CheckBox[ShortcutBoxArray.length + 1];
			
			System.arraycopy(ShortcutPaneArray, 0, ShortcutPane, 0, ShortcutPaneArray.length);
			System.arraycopy(ShortcutTextArray, 0, ShortcutText, 0, ShortcutTextArray.length);
			System.arraycopy(ShortcutBoxArray, 0, ShortcutBox, 0, ShortcutBoxArray.length);
			System.arraycopy(ShortcutKey1Array, 0, ShortcutKey1, 0, ShortcutKey1Array.length);
			System.arraycopy(ShortcutKey2Array, 0, ShortcutKey2, 0, ShortcutKey2Array.length);
			System.arraycopy(ShortcutKey3Array, 0, ShortcutKey3, 0, ShortcutKey3Array.length);
			
			ShortcutPaneArray = ShortcutPane;
			ShortcutTextArray = ShortcutText;
			ShortcutBoxArray = ShortcutBox;
			ShortcutKey1Array = ShortcutKey1;
			ShortcutKey2Array = ShortcutKey2;
			ShortcutKey3Array = ShortcutKey3;
			
			ShortcutPaneArray[ShortcutPaneArray.length-1] = newPane;
			ShortcutTextArray[ShortcutTextArray.length-1] = newText;
			ShortcutBoxArray[ShortcutBoxArray.length-1] = newBox;
			ShortcutKey1Array[ShortcutKey1Array.length-1] = newKey1;
			ShortcutKey2Array[ShortcutKey2Array.length-1] = newKey2;
			ShortcutKey3Array[ShortcutKey3Array.length-1] = newKey3;
			
			SetupDisplay(currentID);
		}
		else if(State == 2)
		{
			btnAdd.setVisible(true);
			btnAdd.setDisable(false);
			btnEdit.setVisible(true);
			btnEdit.setDisable(false);
			
			btnDelete.setVisible(false);
			btnDelete.setDisable(true);
			
			btnSubmit.setVisible(false);
			btnSubmit.setDisable(true);
			
			btnCancel.setVisible(false);
			btnCancel.setDisable(true);
			
			State = -1;

			System.out.println("length: " + ShortcutTextArray.length);
			
			for(int y = 0; y < ShortcutTextArray.length; y++)
			{
				System.out.println("y: " + y);
				String text = ShortcutTextArray[y].getText();
				String key1 = ShortcutKey1Array[y].getText();
				String key2 = ShortcutKey2Array[y].getText();
				String key3 = ShortcutKey3Array[y].getText();
				
				System.out.println("texts: " + text + key1 + key2 + key3);
				
				String prefName = currentID + "SHORT" + y;
				prefs.put(prefName, Main.mInstance.encode(text, 7));
				
				System.out.println("prefName: " + prefName);
			
				prefs.put(currentID + "Key1" + y, key1);
				prefs.put(currentID + "Key2" + y, key2);
				prefs.put(currentID + "Key3" + y, key3);

				ShortcutTextArray[y].setDisable(true);
				ShortcutKey1Array[y].setDisable(true);
				ShortcutKey2Array[y].setDisable(true);
				ShortcutKey3Array[y].setDisable(true);
				
				/*
					prefs.putInt(currentID + "ShortcutCount", prefs.getInt(currentID + "ShortcutCount", 0) + 1);
			
					int curShort = prefs.getInt(currentID + "ShortcutCount", 0);
		
					String prefName = currentID + "SHORT" + curShort;
					prefs.put(prefName, Main.mInstance.encode(thisText, 7));
				
					prefs.put(currentID + "Key1" + curShort, thisKey1);
					prefs.put(currentID + "Key2" + curShort, thisKey2);
					prefs.put(currentID + "Key3" + curShort, thisKey3);
				 */
			}
			
			btnEdit.setDisable(false);
		}
		else if(State == 3)
		{
			System.out.println("Submit Delete!");
			
			int count = 0;
			int[] checked = new int[ShortcutBoxArray.length];
			
			System.out.println("ShortcutBoxArray.length: " + ShortcutBoxArray.length);
			
			for(int h = 0; h < ShortcutBoxArray.length; h++)
			{
				if(ShortcutBoxArray[h].isSelected())
				{
					checked[count] = h;
					count++;
					System.out.println("count: " + count);
					System.out.println("h: " + h);
				}
			}
			
			String [][] tempList = new String[thisKeyList.length-count][4];		
			int il = 0;
			int ut = 0;
			
			for(int g = 0; g < thisKeyList.length; g++)
			{
				System.out.println("List: " + g + ". " + thisKeyList[g][0] + " " + thisKeyList[g][1] + " " + thisKeyList[g][2] + " " + thisKeyList[g][3]);
			
				if(g == checked[il])
				{
					System.out.println("Skip " + g);
					il++;
				}
				else
				{
					tempList[ut][0] = thisKeyList[g][0];
					tempList[ut][1] = thisKeyList[g][1];
					tempList[ut][2] = thisKeyList[g][2];
					tempList[ut][3] = thisKeyList[g][3];
					ut++;
				}
			}
			
			for(int w = 0; w < prefs.getInt(currentID + "ShortcutCount", 0); w++)
			{
				String prefName = currentID + "SHORT" + (w);
				prefs.put(prefName, "");
				prefs.put(currentID + "Key1" + (w), "");
				prefs.put(currentID + "Key2" + (w), "");
				prefs.put(currentID + "Key3" + (w), "");
			}
			
			System.out.println("Data: " + tempList.length + " " + prefs.getInt(currentID + "ShortcutCount", 0));

			prefs.putInt(currentID + "ShortcutCount", tempList.length);
			
			for(int w = 0; w < tempList.length; w++)
			{
				System.out.println("List: " + w + ". " + tempList[w][0] + " " + tempList[w][1] + " " + tempList[w][2] + " " + tempList[w][3]);
				
				String prefName = currentID + "SHORT" + (w);
				prefs.put(prefName, Main.mInstance.encode(tempList[w][0], 7));
				prefs.put(currentID + "Key1" + (w), tempList[w][1]);
				prefs.put(currentID + "Key2" + (w), tempList[w][2]);
				prefs.put(currentID + "Key3" + (w), tempList[w][3]);
			}
			
			SetupDisplay(currentID);
		}
		else
		{
			
		}
		
	}
	public void Cancel(ActionEvent e)
	{
		if(State == 1)
		{
			SetupDisplay(currentID);
		}
		else if(State == 2)
		{
			SetupDisplay(currentID);
		}
		else if(State == 3)
		{
			SetupDisplay(currentID);
		}
		else
		{
			SetupDisplay(currentID);
		}
	}
	
	public void Shortcut(String txt) throws AWTException
	{
		System.out.println("Typing " + txt);
		Robot robot = new Robot();
		
        robot.delay(10);
        
        StringSelection stringSelection = new StringSelection(txt);
        
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, stringSelection);

        robot.keyPress(java.awt.event.KeyEvent.VK_CONTROL);
        robot.keyPress(java.awt.event.KeyEvent.VK_V);
        robot.keyRelease(java.awt.event.KeyEvent.VK_V);
        robot.keyRelease(java.awt.event.KeyEvent.VK_CONTROL);
	}
	
	public void ChangeName(ActionEvent e)
	{
		HideError(lblName);
		
		String newName = fieldPName.getText();
		newName = newName.trim();
		
		System.out.println("newName " + newName);
		
		if(newName.equals("") || newName.equals(" "))
		{
			return;
		}

		prefs.put("PROF" + ProfileID, newName + ":" + currentID + ":" + ProfileID);
		System.out.println("get " + prefs.get("PROF" + ProfileID, "No"));
		
		ShowError(lblName, "New profile name is " + newName + ".", 1);
		
		ProfileName = newName;
		
		SetupPage();
		SetupDisplay(currentID);
	}
	
	public void ChangePassword(ActionEvent e)
	{
		HideError(lblPassword);
		
		String newPass = fieldPassword1.getText();
		newPass = newPass.trim();
		
		if(newPass.equals("") || newPass.equals(" "))
		{
			prefs.putBoolean(currentID + "isProtected", false);
			prefs.put(currentID + "Password", "q");
			ShowError(lblPassword, "Password removed from this profile!", 1);
			return;
		}
		
		/*
		 * String SALT = "my-salt-text";
			pass = SALT + pass;
			String hashPass = generateHash(pass);

			prefs.putBoolean(ID + "isProtected", true);
			prefs.put(ID + "Password", hashPass);
		 */
		String SALT = "my-salt-text";
		newPass = SALT + newPass;
		String hashPass = generateHash(newPass);
		
		prefs.putBoolean(currentID + "isProtected", true);
		prefs.put(currentID + "Password", hashPass);

		ShowError(lblPassword, "Password change successful!", 1);
	}
	
	private boolean Password = true;
	
	public void ShowHide(ActionEvent e)
	{
		Button b = (Button)e.getSource();
		
		if(b.getId().toString().equals("showhide2"))
		{
			Password = false;
			b.setId("showhide1");
			fieldPassword2.setVisible(false);
			fieldPassword1.setVisible(true);
			fieldPassword21.setVisible(false);
			fieldPassword11.setVisible(true);
			fieldPassword211.setVisible(false);
			fieldPassword111.setVisible(true);
		}
		else
		{
			Password = true;
			b.setId("showhide2");
			fieldPassword2.setVisible(true);
			fieldPassword1.setVisible(false);
			fieldPassword21.setVisible(true);
			fieldPassword11.setVisible(false);
			fieldPassword211.setVisible(true);
			fieldPassword111.setVisible(false);
		}
	}
	
	public void UpdateText(KeyEvent e)
	{
		if(Password)
		{
			fieldPassword1.setText(fieldPassword2.getText());
			fieldPassword11.setText(fieldPassword21.getText());
			fieldPassword111.setText(fieldPassword211.getText());
		}
		else
		{
			fieldPassword2.setText(fieldPassword1.getText());
			fieldPassword21.setText(fieldPassword11.getText());
			fieldPassword211.setText(fieldPassword111.getText());
		}
	}
	
	public void OpenConfirmationPane(ActionEvent e)
	{
		SettingsPane.setVisible(true);
		SettingsDisplay.setVisible(true);
		ConfirmationPane.setVisible(true);
	}
	public void HideConfirmationPane(ActionEvent e)
	{
		SettingsPane.setVisible(true);
		SettingsDisplay.setVisible(true);
		ConfirmationPane.setVisible(false);
	}
	
	public void DeleteProfile(ActionEvent e)
	{
		prefs.putBoolean(currentID + "isProtected", false);
		prefs.put(currentID + "Password", "");
		
		prefs.put("PROF" + ProfileID, "8/:Deleted_0");
		prefs.putInt(currentID + "ShortcutCount", 0);
		
		Container.getChildren().clear();
		
		SettingsPane.setVisible(false);
		SettingsDisplay.setVisible(false);
		ConfirmationPane.setVisible(false);

		fieldPassword1.setText("");
		
		SetupPage();
		/*
			if(p)
			{
				//Hash and Save Password
				String SALT = "my-salt-text";
				pass = SALT + pass;
				String hashPass = generateHash(pass);
	
				prefs.putBoolean(ID + "isProtected", true);
				prefs.put(ID + "Password", hashPass);
			}
			else
			{
				//Save no password
				prefs.putBoolean(ID + "isProtected", false);
				prefs.put(ID + "Password", "q");
			}
			
			prefs.put("PROF" + (prefs.getInt("Profiles", 0)), Name + ":" + ID + ":" + (prefs.getInt("Profiles", 0)));
			prefs.putInt(ID + "ShortcutCount", 0);
	
			prefs.putInt("Profiles", (prefs.getInt("Profiles", 0)) + 1);
	
			ProfileName = Name; 
			ProfileID = prefs.getInt("Profiles", 0);
			
			CreateProfilePane.setVisible(false);
			fieldProfileName.setText("");
			SetupPage();
			SetupDisplay(ID);
		 */
	}
	
	public void OpenSettings(ActionEvent e)
	{
		SettingsPane.setVisible(true);
		SettingsDisplay.setVisible(true);
		ConfirmationPane.setVisible(false);
		
		Password = true;
		btnShowHide1.setId("showhide2");
		fieldPassword2.setVisible(true);
		fieldPassword1.setVisible(false);

		HideError(lblName);
		HideError(lblPassword);
	}
	
	public void CloseSettings(ActionEvent e)
	{
		SettingsPane.setVisible(false);
		SettingsDisplay.setVisible(false);
		ConfirmationPane.setVisible(false);

		fieldPassword1.setText("");
		
		fieldPassword2.setText("");
	}
	
	public static String generateHash(String input) 
	{
		StringBuilder hash = new StringBuilder();

		try 
		{
			MessageDigest sha = MessageDigest.getInstance("SHA-1");
			byte[] hashedBytes = sha.digest(input.getBytes());
			char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
					'a', 'b', 'c', 'd', 'e', 'f' };
			for (int idx = 0; idx < hashedBytes.length;   idx++) 
			{
				byte b = hashedBytes[idx];
				hash.append(digits[(b & 0xf0) >> 4]);
				hash.append(digits[b & 0x0f]);
			}
		}
		catch (NoSuchAlgorithmException e)
		{
			// handle error here.
		}

		return hash.toString();
	}
	
	public static void hackTooltipStartTiming(Tooltip tooltip) 
	{
	    try 
	    {
	        Field fieldBehavior = tooltip.getClass().getDeclaredField("BEHAVIOR");
	        fieldBehavior.setAccessible(true);
	        Object objBehavior = fieldBehavior.get(tooltip);

	        //Change Open Delay
	        Field fieldOpenDelay = objBehavior.getClass().getDeclaredField("activationTimer");

	        fieldOpenDelay.setAccessible(true);
	        Timeline objTimer = (Timeline) fieldOpenDelay.get(objBehavior);

	        objTimer.getKeyFrames().clear();
	        objTimer.getKeyFrames().add(new KeyFrame(new Duration(250)));
	        
	        //Change Duration
	      	Field fieldDuration = objBehavior.getClass().getDeclaredField("hideTimer");
	      	
	      	fieldDuration.setAccessible(true);
	        Timeline objTimer2 = (Timeline) fieldDuration.get(objBehavior);

	        objTimer2.getKeyFrames().clear();
	        objTimer2.getKeyFrames().add(new KeyFrame(new Duration(5000)));

	        //Change Exit Delay
	      	Field fieldCloseDelay = objBehavior.getClass().getDeclaredField("leftTimer");
	      	
	      	fieldCloseDelay.setAccessible(true);
	        Timeline objTimer3 = (Timeline) fieldCloseDelay.get(objBehavior);

	        objTimer3.getKeyFrames().clear();
	        objTimer3.getKeyFrames().add(new KeyFrame(new Duration(250)));

	        /*************MY EDIT*************
	        Field[] All = objBehavior.getClass().getDeclaredFields();
	        for(int y = 0; y < All.length; y++)
	        {
	        	System.out.println(All[y].getName());
	        }
	        *********************************/
	    } 
	    catch (Exception e)
	    {
	        e.printStackTrace();
	    }
	}
}

