/**
Program:   Assignment 2: Application – Ball Maze
Filename:  CBallMaze.java                              
@author:   © Neetu Kumari Das                       
Course:    BSc Computing Year 1        
Module:    CSY1020 Problem Solving & Programming       
Tutor:     Kumar Lamichhane                                   
@version:  2.0 Incorporates Artificial Intelligence!  
Date:      01/06/18                                    
 */

//importing classes of different packages
import java.awt.*;//imports classes for AWT like Label, Textfield, etc. 
import java.awt.event.*; 
import java.io.File; //imports all the classes of io package when input/output stream is used. 
import javax.swing.*;
import javax.swing.border.*; //imports classes for border.
import javax.sound.sampled.*; //imports classes for audio.

//
public class CBallMaze extends JFrame implements ActionListener //calls for public class to inherit features of JFrame.
{
	private JPanel jPLefPanel, jPRigPanel, jPDowPanel; //holds the panels
	private JMenuBar jMMenuB; // holds the menubar
	private JMenu jMbScene, jMbEdit, jMbControl, jMbHelp; //holds the menu.
	private JMenuItem jMIAsExit, jMIAsAct, jMIAsRun, jMIAsPause, jMIAsReset, jMIAsAbout; //holds the menu items of menu
	private JLabel jLOptLbl, jLSquLbl, jLDirLbl, jLSpeedLbl, jLTimerLbl, jLDot1Lbl, jLDot2Lbl; //holds the labels
	private JTextField jTFdOpt, jTFdSqu, jTFdDir, jTfdHrs, jTfdMins, jTfdSecs; //holds the textfields.

	//holds the buttons.
	private JButton jBBut1, jBButUp, jBBut2, jBButLeft, jBBut3, jBButRight, jBBut4, jBButDown, jBBut5;
	private JButton  jBButOpt1, jBButOpt2, jBButOpt3, jBButExit, jBButAct, jBButRun, jBButPause, jBButReset, jBButWest; 

	private JLabel[][] jLSandMaze = new JLabel[16][13];//creating 2D array of grid [16] [13] by using JLabel for maze

	//holds the images
	private ImageIcon iconSandBlock, iconGoldBall, iconSandStone, iconWhiteBg, iconGoal;
	private ImageIcon iconWestDir, iconEastDir, iconNorthDir, iconSouthDir, iconMouse;

	private JSlider jSSlider; //holds the slider
	Clip downFallSound; //it is for audio

	//initialising the ball position in the maze.
	int gballx = 15;
	int gbally = 0;
	//initialising the position of mouses in the maze.
	int mousex = 0;
	int mouse1x = 0;

	//for timer
	private int ticksofTimer = 0;
	private Timer allTime, downFallTimer, autoRunTimer, autoMouseMove;

	private boolean checkForOpt2 = false;//initializing the option 2 as false 
	private boolean checkOpt2Applause =false; //initializing the option 2 as false for the applause clip

	//Creating the frame
	public static void main(String[]args)
	{
		CBallMaze f= new CBallMaze(); //creating object
		f.setSize(775, 650);// sets the size of frame.
		f.setDefaultCloseOperation(EXIT_ON_CLOSE); //closes the frame
		f.setLocationRelativeTo(null);//keeps the frame at the centre.
		f.setResizable(false);//makes the frame unresizeable
		f.setTitle("CBallMaze - Ball Maze Application");//sets the title of the frame
		ImageIcon iconGreenfoot = new ImageIcon("./images/greenfoot.png");
		f.setIconImage(iconGreenfoot.getImage());//sets the image of the frame
		f.menuBar();//calls the method menuBar
		f.createGUI();//calls the method createGUI
		f.setVisible(true);//makes the frame visible
	}

	//creating the menuBar
	private void menuBar() 
	{
		jMMenuB = new JMenuBar();
		this.setJMenuBar(jMMenuB);

		//adding the menu in the menuBar
		jMbScene= new JMenu("Scenario");
		jMMenuB.add(jMbScene);

		jMbEdit= new JMenu("Edit");
		jMMenuB.add(jMbEdit);

		jMbControl= new JMenu("Controls");
		jMMenuB.add(jMbControl);

		jMbHelp= new JMenu("Help");
		jMMenuB.add(jMbHelp);

		//adding the menu items in the menu.
		jMIAsExit = new JMenuItem("Exit");
		jMbScene.add(jMIAsExit);
		jMIAsExit.addActionListener(this);

		jMIAsAct = new JMenuItem("Act");
		jMIAsAct.setIcon(new ImageIcon("./images/step.png"));//sets the image of 'act'
		jMbControl.add(jMIAsAct);
		jMIAsAct.addActionListener(this);

		jMIAsRun = new JMenuItem("Run");
		jMIAsRun.setIcon(new ImageIcon("./images/run.png"));
		jMbControl.add(jMIAsRun);
		jMIAsRun.addActionListener(this);

		jMIAsPause = new JMenuItem("Pause");
		jMIAsPause.setIcon(new ImageIcon("./images/pause.png"));
		jMbControl.add(jMIAsPause);
		jMIAsPause.addActionListener(this);

		jMIAsReset = new JMenuItem("Reset");
		jMIAsReset.setIcon(new ImageIcon("./images/reset.png"));
		jMbControl.add(jMIAsReset);
		jMIAsReset.addActionListener(this);

		jMIAsAbout = new JMenuItem("About");
		jMbHelp.add(jMIAsAbout);
		jMIAsAbout.addActionListener(this);
	}

	//creating the GUI application
	private void createGUI() 
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container bg= getContentPane();//gets the container

		//for borders
		Border raisedbevel=BorderFactory.createRaisedBevelBorder();//for raisedbevel border
		Border loweredbevel=BorderFactory.createRaisedBevelBorder();//for loweredbevel border
		Border compound= BorderFactory.createCompoundBorder(
				raisedbevel, loweredbevel); // for compound raisedbevel and loweredbevel border

		//Left Panel
		jPLefPanel= new JPanel();//creates new panel
		jPLefPanel.setPreferredSize(new Dimension(580,540));//sets the size of panel
		jPLefPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));//sets the border for the panel 
		bg.add(jPLefPanel, BorderLayout.LINE_START);//adds the panel in the frame

		//Right Panel
		jPRigPanel= new JPanel();
		jPRigPanel.setPreferredSize(new Dimension(190,580));
		jPRigPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		jPRigPanel.setLayout(null);//sets the layout manager to null so that positions can be set manually
		bg.add(jPRigPanel, BorderLayout.LINE_END);

		//Bottom Panel
		jPDowPanel= new JPanel();
		jPDowPanel.setPreferredSize(new Dimension(775,80));
		jPDowPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		jPDowPanel.setLayout(null);//sets the layout manager to null so that positions can be set manually
		bg.add(jPDowPanel, BorderLayout.PAGE_END);

		//images
		iconSandBlock = new ImageIcon("./images/sand.jpg");
		iconGoldBall = new ImageIcon("./images/gold-ball1.png");
		iconSandStone = new ImageIcon("./images/sandstone.jpg");
		iconWhiteBg = new ImageIcon("./images/white.jpg");
		iconGoal = new ImageIcon("./images/goal.jpg");
		iconWestDir = new ImageIcon("./images/west.jpg");
		iconEastDir = new ImageIcon("./images/east.jpg");
		iconNorthDir = new ImageIcon("./images/north.jpg");
		iconSouthDir = new ImageIcon("./images/south.jpg");
		iconMouse = new ImageIcon("./images/mouse.PNG");

		//making maze in the left panel
		jPLefPanel.setLayout(new GridBagLayout()); //places the given components in the grid of rows and columns
		GridBagConstraints gbc = new GridBagConstraints();//gets the constraints for the specified component.
		for(int x=0; x<16; x++)//using 'for' loop for row
		{
			for(int y=0; y<13; y++)//using 'for' loop for column
			{
				gbc.gridx= x;//anchor constraints
				gbc.gridy= y;//anchor constraints

				jLSandMaze[x][y]= new JLabel(iconSandBlock);//creates new label for sandblock

				//setting the image of the golden ball in the position x=15 and y=0
				if(x==15&&y==0) 
				{ 
					jLSandMaze [x][y]= new JLabel(iconGoldBall);		
					gballx=x;
					gbally=y;
				}

				//setting the image of the sandstone in the position x=0 and y=12
				if(x==0&&y==12)
				{
					jLSandMaze [x][y]= new JLabel(iconSandStone);		
				}

				//Inserting white parts
				if(y==1)
				{
					int i=15;
					while( i>9 ) 
					{
						jLSandMaze[i][1]= new JLabel(iconWhiteBg);
						i--;
					}
					int i1=8;
					while( i1>5) 
					{
						jLSandMaze[i1][1]= new JLabel(iconWhiteBg);
						i1--;
					}
					int i2=4;
					while( i2>1) 
					{
						jLSandMaze[i2][1]= new JLabel(iconWhiteBg);
						i2--;
					}
					jLSandMaze[0][1]= new JLabel(iconWhiteBg);
				}
				if(y==2) 
				{
					int i=15;
					while( i>9 ) 
					{
						jLSandMaze[i][2]= new JLabel(iconWhiteBg);
						i--;
					}
					int i1=8;
					while( i1>5) 
					{
						jLSandMaze[i1][2]= new JLabel(iconWhiteBg);
						i1--;
					}
					int i2=4;
					while( i2>1) 
					{
						jLSandMaze[i2][2]= new JLabel(iconWhiteBg);
						i2--;
					}
					jLSandMaze[0][2]= new JLabel(iconWhiteBg);
				}
				if(y==4) 
				{ 
					int i=15;
					while( i>11 ) 
					{
						jLSandMaze[i][4]= new JLabel(iconWhiteBg);
						i--;
					}
					int i1=10;
					while( i1>6) 
					{
						jLSandMaze[i1][4]= new JLabel(iconWhiteBg);
						i1--;
					}
					int i2=5;
					while( i2>2) 
					{
						jLSandMaze[i2][4]= new JLabel(iconWhiteBg);
						i2--;
					}
					jLSandMaze[0][4]= new JLabel(iconWhiteBg);
					jLSandMaze[1][4]= new JLabel(iconWhiteBg);
				}
				if(y==5)
				{
					int i=15;
					while( i>11 ) 
					{
						jLSandMaze[i][5]= new JLabel(iconWhiteBg);
						i--;
					}
					int i1=10;
					while( i1>6) 
					{
						jLSandMaze[i1][5]= new JLabel(iconWhiteBg);
						i1--;
					}
					int i2=5;
					while( i2>2) 
					{
						jLSandMaze[i2][5]= new JLabel(iconWhiteBg);
						i2--;
					}
					jLSandMaze[0][5]= new JLabel(iconWhiteBg);
					jLSandMaze[1][5]= new JLabel(iconWhiteBg);
				}
				if(y==7) 
				{
					int i=15;
					while( i>12 ) 
					{
						jLSandMaze[i][7]= new JLabel(iconWhiteBg);
						i--;
					}
					int i1=11;
					while( i1>5) 
					{
						jLSandMaze[i1][7]= new JLabel(iconWhiteBg);
						i1--;
					}
					int i2=4;
					while( i2>1) 
					{
						jLSandMaze[i2][7]= new JLabel(iconWhiteBg);
						i2--;
					}
					jLSandMaze[0][7]= new JLabel(iconWhiteBg);
				}
				if(y==8) 
				{
					int i=15;
					while( i>12 ) 
					{
						jLSandMaze[i][8]= new JLabel(iconWhiteBg);
						i--;
					}
					int i1=11;
					while( i1>5) 
					{
						jLSandMaze[i1][8]= new JLabel(iconWhiteBg);
						i1--;
					}
					int i2=4;
					while( i2>1) 
					{
						jLSandMaze[i2][8]= new JLabel(iconWhiteBg);
						i2--;
					}
					jLSandMaze[0][8]= new JLabel(iconWhiteBg);
				}
				if(y==10)
				{
					int i=15;
					while( i>7 ) 
					{
						jLSandMaze[i][10]= new JLabel(iconWhiteBg);
						i--;
					}
					int i1=6;
					while( i1>2) 
					{
						jLSandMaze[i1][10]= new JLabel(iconWhiteBg);
						i1--;
					}
					jLSandMaze[0][10]= new JLabel(iconWhiteBg);
					jLSandMaze[1][10]= new JLabel(iconWhiteBg);
				}
				if(y==11)
				{
					int i=15;
					while( i>7) 
					{
						jLSandMaze[i][11]= new JLabel(iconWhiteBg);
						i--;
					}
					int i1=6;
					while( i1>2) 
					{
						jLSandMaze[i1][11]= new JLabel(iconWhiteBg);
						i1--;
					}
					jLSandMaze[0][11]= new JLabel(iconWhiteBg);
					jLSandMaze[1][11]= new JLabel(iconWhiteBg);
				}
				jPLefPanel.add(jLSandMaze[x][y], gbc);//adds all the components of the gbc in the left panel
			}
		}

		//Components of right panel
		jLOptLbl= new JLabel("Option:");//creates the new label for option
		jLOptLbl.setBounds(20, 8, 60, 25);//sets the bounds for option
		jPRigPanel.add(jLOptLbl);//adds the JLOptLbl in right panel
		jTFdOpt= new JTextField(5);// creates new text field for option 
		jTFdOpt.setBounds(110, 8, 70, 25);
		jTFdOpt.setEditable(false);//sets the textfield of option uneditable 
		jPRigPanel.add(jTFdOpt);//adds the textfield for option

		//Square 
		jLSquLbl= new JLabel("Square:");
		jLSquLbl.setBounds(20, 38, 60, 25);
		jPRigPanel.add(jLSquLbl);
		jTFdSqu= new JTextField(5);
		jTFdSqu.setBounds(110, 38, 70, 25);
		jTFdSqu.setEditable(false);
		jPRigPanel.add(jTFdSqu);

		//Direction
		jLDirLbl= new JLabel("Direction:");
		jLDirLbl.setBounds(20, 68, 60, 25);
		jPRigPanel.add(jLDirLbl);
		jTFdDir= new JTextField(5);
		jTFdDir.setBounds(110, 68, 70, 25);
		jTFdDir.setEditable(false);
		jPRigPanel.add(jTFdDir);

		//Timer
		jLTimerLbl = new JLabel("Digital Timer");
		jLTimerLbl.setBounds(60, 105, 100, 25);
		jLTimerLbl.setFont(new Font("Serif",Font.BOLD,15));//sets the font and its size
		jPRigPanel.add(jLTimerLbl);
		allTime = new Timer(700, new ActionListener() { //creates the timer 
			public void actionPerformed(ActionEvent eventTimer1) //it reacts to the action
			{
				jTfdHrs.setText(Integer.toString(ticksofTimer / 3600));//hours
				jTfdMins.setText(Integer.toString(ticksofTimer / 60));//minutes
				jTfdSecs.setText(Integer.toString(ticksofTimer % 60));//seconds
				ticksofTimer = ticksofTimer + 1;//increases the time by 1
			}
		});

		//Timer for making the ball fall down automatically
		downFallTimer = new Timer(600, new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				if(checkForOpt2==true) {//checking the condition for option 2 to be true to execute following code
					if(jLSandMaze[gballx][gbally+1].getIcon().equals(iconSandBlock))//checks if there is sandblock while falling down
					{
						shiftDown();//fall down
						jTFdSqu.setText(gballx + "  ,  " + gbally);//gives the ball position in the square's textfield
						jTFdDir.setText("S");//gives the direction of the movement of ball in the direction's textfield
						downFallClip("drop.wav");//audio for autodownfall of the ball
					}
				}
			}
		});
		freeDownFall();//calling the method 'freeDownFall'

		//Textfield for Hours
		jTfdHrs = new JTextField(5);
		jTfdHrs.setBounds(25, 135, 30, 20);
		jTfdHrs.setBackground(Color.BLACK);//sets the background black
		jTfdHrs.setEnabled(false);//disables the textfield
		jPRigPanel.add(jTfdHrs);

		jLDot1Lbl = new JLabel(":");
		jLDot1Lbl.setBounds(65, 135, 30, 20);
		jPRigPanel.add(jLDot1Lbl);

		//Textfield for Minutes
		jTfdMins = new JTextField(5);
		jTfdMins.setBounds(85, 135, 30, 20);
		jTfdMins.setBackground(Color.BLACK);
		jTfdMins.setEnabled(false);
		jPRigPanel.add(jTfdMins);

		jLDot2Lbl = new JLabel(":");
		jLDot2Lbl.setBounds(130, 135, 30, 20);
		jPRigPanel.add(jLDot2Lbl);

		////Textfield for Seconds
		jTfdSecs = new JTextField(5);
		jTfdSecs.setBounds(145, 135, 30, 20);
		jTfdSecs.setBackground(Color.BLACK);
		jTfdSecs.setEnabled(false);
		jPRigPanel.add(jTfdSecs);

		//Buttons
		jBBut1= new JButton();
		jBBut1.setBounds(25, 190, 50, 30);
		jBBut1.setBorder(loweredbevel);//gives the borders
		jBBut1.setEnabled(false);//disables the buttons
		jPRigPanel.add(jBBut1);

		//Button for upward direction
		jBButUp= new JButton("^");
		jBButUp.setBounds(75, 190, 50, 30);
		jBButUp.setBackground(Color.WHITE);//gives the white color for the background
		jBButUp.setBorder(compound);
		jPRigPanel.add(jBButUp);
		jBButUp.addActionListener(this);//when clicked it acts as specified

		jBBut2= new JButton();
		jBBut2.setBounds(125, 190, 50, 30);
		jBBut2.setEnabled(false);
		jBBut2.setBorder(loweredbevel);
		jPRigPanel.add(jBBut2);

		//Button for left direction
		jBButLeft= new JButton("<");
		jBButLeft.setBounds(25, 220, 50, 30);
		jBButLeft.setBackground(Color.WHITE);
		jBButLeft.setBorder(compound);
		jPRigPanel.add(jBButLeft);
		jBButLeft.addActionListener(this);

		jBBut3= new JButton();
		jBBut3.setBounds(75, 220, 50, 30);
		jBBut3.setEnabled(false);
		jBBut3.setBorder(loweredbevel);
		jPRigPanel.add(jBBut3);

		//Button for right direction
		jBButRight= new JButton(">");
		jBButRight.setBounds(125, 220, 50, 30);
		jBButRight.setBackground(Color.WHITE);
		jBButRight.setBorder(compound);
		jPRigPanel.add(jBButRight);
		jBButRight.addActionListener(this);

		jBBut4= new JButton();
		jBBut4.setBounds(25, 250, 50, 30);
		jBBut4.setEnabled(false);
		jBBut4.setBorder(loweredbevel);
		jPRigPanel.add(jBBut4);

		//Button for downward direction
		jBButDown= new JButton("v");
		jBButDown.setBounds(75, 250, 50, 30);
		jBButDown.setBackground(Color.WHITE);
		jBButDown.setBorder(compound);
		jPRigPanel.add(jBButDown);
		jBButDown.addActionListener(this);

		jBBut5= new JButton();
		jBBut5.setBounds(125, 250, 50, 30);
		jBBut5.setEnabled(false);
		jBBut5.setBorder(loweredbevel);
		jPRigPanel.add(jBBut5);

		//Option 1 button
		jBButOpt1= new JButton("Option 1");
		jBButOpt1.setBounds(17, 315, 82, 30);
		jPRigPanel.add(jBButOpt1);
		jBButOpt1.addActionListener(this);

		//Option 2 button
		jBButOpt2= new JButton("Option 2");
		jBButOpt2.setBounds(102, 315, 82, 30);
		jPRigPanel.add(jBButOpt2);
		jBButOpt2.addActionListener(this);

		//Option 3 button
		jBButOpt3= new JButton("Option 3");
		jBButOpt3.setBounds(17, 350, 82, 30);
		jPRigPanel.add(jBButOpt3);
		jBButOpt3.addActionListener(this);

		//Exit button
		jBButExit= new JButton("Exit");
		jBButExit.setBounds(102, 350, 82, 30);
		jBButExit.addActionListener(this);
		jPRigPanel.add(jBButExit);

		//Button for direction images
		jBButWest= new JButton(iconWestDir);
		jBButWest.setBounds(50, 395, 100, 100);	
		jPRigPanel.add(jBButWest);

		//Act button
		jBButAct= new JButton("Act");
		jBButAct.setIcon(new ImageIcon("./images/step.png"));//sets the image of act 
		jBButAct.setBounds(60, 25, 90, 30);
		jPDowPanel.add(jBButAct);
		jBButAct.addActionListener(this);

		//Run button
		jBButRun= new JButton("Run");
		jBButRun.setIcon(new ImageIcon("./images/run.png"));//sets the image of run
		jBButRun.setBounds(160, 25, 90, 30);
		jPDowPanel.add(jBButRun);
		jBButRun.addActionListener(this);

		//Pause button
		jBButPause= new JButton("Pause");
		jBButPause.setIcon(new ImageIcon("./images/pause.png"));//sets the image of pause
		jBButPause.setBounds(160, 25, 90, 30);
		jPDowPanel.add(jBButPause);
		jBButPause.addActionListener(this);

		//Reset button
		jBButReset= new JButton("Reset");
		jBButReset.setIcon(new ImageIcon("./images/reset.png"));//sets the image of reset
		jBButReset.setBounds(260, 25, 90, 30);
		jBButReset.addActionListener(this);
		jPDowPanel.add(jBButReset);

		//Label for Speed
		jLSpeedLbl = new JLabel("Speed: ");
		jLSpeedLbl.setBounds(465, 25, 70, 35);
		jPDowPanel.add(jLSpeedLbl);

		//Slider
		jSSlider =new JSlider(JSlider.HORIZONTAL, 0, 50, 10);//creates horizontal slider
		jSSlider.setMajorTickSpacing(10); //gives the ticks at position 10
		jSSlider.setPaintTicks(true);//gives the ticks
		jSSlider.setBounds(520, 25, 200, 30);
		jPDowPanel.add(jSSlider);//adds the slider in the down panel
	}

	//movements of the ball
	private void shiftLeft()//creating method for making the ball move left/west 
	{
		jLSandMaze [gballx-1][gbally].setIcon(iconGoldBall);//the image of golden ball is set when moved left
		jLSandMaze [gballx][gbally].setIcon(iconSandBlock);//places the image of sandblock after the ball leaves the position
		gballx = gballx - 1;//move one step left in x-axis
		jBButWest.setIcon(iconWestDir);
		jTFdSqu.setText(gballx + "  ,  " + gbally);
		jTFdDir.setText("W");//gives the west direction in the directon's text field
	}

	private void shiftRight()//creating method for making the ball move right/east
	{
		jLSandMaze[gballx+1][gbally].setIcon(iconGoldBall);//the image of golden ball is set when moved right
		jLSandMaze[gballx][gbally].setIcon(iconSandBlock);
		gballx = gballx + 1;//move one step right in x-axis
		jBButWest.setIcon(iconEastDir);
		jTFdSqu.setText(gballx + "  ,  " + gbally);
		jTFdDir.setText("E");//gives the east direction in the directon's text field
	}

	private void shiftUp()//creating method for making the ball move up/north 
	{
		jLSandMaze[gballx][gbally-1].setIcon(iconGoldBall);//the image of golden ball is set when moved upward
		jLSandMaze[gballx][gbally].setIcon(iconSandBlock);
		gbally = gbally - 1;//move one step up in y-axis
		jBButWest.setIcon(iconNorthDir);
		jTFdSqu.setText(gballx + "  ,  " + gbally);
		jTFdDir.setText("N");//gives the north direction in the directon's text field
	}

	private void shiftDown()//creating method for making the ball move down/south 
	{
		jLSandMaze[gballx][gbally+1].setIcon(iconGoldBall);//the image of golden ball is set when moved downward
		jLSandMaze[gballx][gbally].setIcon(iconSandBlock);
		gbally = gbally + 1;//move one step down in y-axis
		jBButWest.setIcon(iconSouthDir);
		jTFdSqu.setText(gballx + "  ,  " + gbally);
		jTFdDir.setText("S");//gives the south direction in the directon's text field
	}

	private boolean sandStoneReached()//creating method when the ball reaches the sandstone
	{
		if(gballx==0 && gbally==12) //checking the position of ball 
		{ 
			jLSandMaze[0][12].setIcon(iconGoal);//sets the given icon when the ball reaches x=0 and y=12 
			if(checkOpt2Applause==true) 
			{
				downFallClip("applause.wav");//audio for reaching the goal
			}
			allTime.stop();//stops the timer
			JOptionPane.showMessageDialog(null, "CONGRATULATIONS! YOU REACHED THE GOAL.");//displays the message
			dispose();//dispose the whole frame
			CBallMaze.main(null);//creates the same new frame
			return true;

		} 
		return false;
	}

	private boolean canShiftLeft()//creating method for left movement of the ball if there is only sandblock  
	{
		if( jLSandMaze[gballx-1][gbally].getIcon()==iconSandBlock || jLSandMaze[gballx-1][gbally].getIcon()==iconSandStone ) 
		{
			return true;
		}
		else {
			return false;
		}
	}

	private boolean canShiftRight()//creating method for right movement of the ball if there is only sandblock
	{
		if( jLSandMaze[gballx+1][gbally].getIcon()==iconSandBlock) 
		{
			return true;
		}
		else 
		{
			return false;
		}
	}

	private boolean canShiftUp() //creating method for upward movement of the ball if there is only sandblock
	{
		if( jLSandMaze[gballx][gbally-1].getIcon()==iconSandBlock) 
		{
			return true;
		}
		else 
		{
			return false;
		}
	}

	private boolean canShiftDown()//creating method for downward movement of the ball if there is only sandblock 
	{
		if( jLSandMaze[gballx][gbally+1].getIcon()==iconSandBlock) 
		{
			return true;
		}
		else 
		{
			return false;
		}
	}

	public void freeDownFall()//creating method for the making the ball fall down automatically
	{
		try 
		{
			downFallTimer.start();//starts the timer of autodownfall
		}
		catch(Exception event) 
		{
		}
	}

	public void moveAllBlocks()//creating method for act button
	{
		if(gballx>9 && gbally==0)
		{
			shiftLeft();
		}
		else if(gballx==9 && gbally==0 || gbally==1 || gbally==2)
		{
			shiftDown();
		}
		else if(gballx==9 || gballx>6 && gbally==3)
		{
			shiftLeft();
		}
		else if(gballx==6 && gbally==3 || gbally==4 || gbally==5)
		{
			shiftDown();
		}
		else if(gballx==6 && gbally==6)
		{
			shiftLeft();
		}
		else if(gballx==5 && gbally==6 || gbally==7 || gbally==8)
		{
			shiftDown();
		}
		else if(gballx==5 || gballx>2 && gbally==9)
		{
			shiftLeft();
		}
		else if(gballx==2 && gbally==9 || gbally==10 || gbally==11)
		{
			shiftDown();
		}
		else if(gballx==2 || gballx==1 && gbally==12)
		{
			shiftLeft();
			sandStoneReached();
		}
	}

	//Sound for auto downfall of the ball
	public void downFallClip(String dropSound)//creating method for sound of auto downfall 
	{
		try 
		{
			stopSound();//calling 'stopSound' method
			AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File("sounds/"+ dropSound));//gives the position of folder of the sound
			downFallSound=AudioSystem.getClip();//gets the clip from AudioSytem
			downFallSound.open(inputStream);
			downFallSound.start();//starts the sound
		}
		catch(Exception e)
		{
			e.printStackTrace();//shows where the problem has occured
			stopSound();
		}
	}

	public void stopSound() //creating mthod for stopping the sound
	{
		if(downFallSound!= null)
		{
			downFallSound.stop();//stops the sound clip
			downFallSound.close();//closes the sound clip
			downFallSound = null;
		}
	}

	//Action performed when run button is clicked
	public void moveAutomatically() 
	{
		autoRunTimer= new Timer(550, new ActionListener() 
		{
			public void actionPerformed(ActionEvent event) {
				moveAllBlocks();//calling the act method	
			}
		});
		autoRunTimer.start();//starts the timer
	}

	//adding obstacles in Option 3 and making them move 
	public void addObstacle() {
		moveMouseAutomatically();
	}

	//making the first mouse move
	private void mouseMove() {
		if(mousex<15) {
			jLSandMaze[mousex+1][3].setIcon(iconMouse);
			jLSandMaze[mousex][3].setIcon(iconSandBlock);
			mousex = mousex + 1;
		}
		else if(mousex==15) {
			jLSandMaze[mousex][3].setIcon(iconSandBlock);
			mousex=0;
			mouseMove();
		}
	}

	//making the second mouse move
	private void mouse1Move() {
		if(mouse1x<15) {
			jLSandMaze[mouse1x+1][6].setIcon(iconMouse);
			jLSandMaze[mouse1x][6].setIcon(iconSandBlock);
			mouse1x = mouse1x + 1;
		}
		else if(mouse1x==15) {
			jLSandMaze[mouse1x][6].setIcon(iconSandBlock);
			mouse1x=0;
			mouse1Move();
		}
	}

	//making the obstacles move automatically
	public void moveMouseAutomatically() {

		autoMouseMove= new Timer(550, new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) {
				mouseMove();
				mouse1Move();
				collision();
			}
		});
		autoMouseMove.start();
	}

	//when the ball and the mouse collide, following code is executed
	private void collision() {
		if(jLSandMaze[gballx-1][gbally].getIcon()==iconMouse 
				|| jLSandMaze[gballx][gbally+1].getIcon()==iconMouse
				|| jLSandMaze[mousex][3].getIcon()==iconGoldBall
				|| jLSandMaze[mouse1x][6].getIcon()==iconGoldBall){
			JOptionPane.showMessageDialog(null, "GAME OVER!");
			dispose();
			System.exit(0);
		}
	}

	//action performed when reset is clicked
	private void reSet() {
		//creating default scenario
		jLSandMaze[gballx][gbally].setIcon(iconSandBlock);
		jLSandMaze[15][0].setIcon(iconGoldBall);
		jLSandMaze[0][12].setIcon(iconSandStone);
		gballx=15;//initial position of the ball
		gbally=0;//initial position of the ball
		allTime.stop();//stops the timer
		ticksofTimer = 0;
		jTfdHrs.setText(Integer.toString(0));
		jTfdMins.setText(Integer.toString(0));
		jTfdSecs.setText(Integer.toString(0));
		jTFdSqu.setText(gballx + "  ,  " + gbally);
	}

	//move Buttons
	public void actionPerformed(ActionEvent event) 
	{
		if (event.getActionCommand().equals ("<")) //move one step left when it is clicked
		{
			if(canShiftLeft()) 
			{
				shiftLeft();
				sandStoneReached();
			}
		}

		if (event.getActionCommand().equals (">")) //move one step right when it is clicked

		{
			if(canShiftRight())
			{
				shiftRight();
			}
		}

		if (event.getActionCommand().equals ("^")) //move one step up when it is clicked	
		{
			if(canShiftUp())
			{
				shiftUp();
			}
		}

		if (event.getActionCommand().equals ("v"))//move one step down when it is clicked
		{
			if(canShiftDown()) 
			{
				shiftDown();
			}
		}

		//Panel3 buttons
		if(event.getActionCommand().equals("Act")||event.getSource().equals(jMIAsAct))//performs the action of moveAllBlocks method when clicked with each click
		{
			moveAllBlocks();				
		}

		if(event.getActionCommand().equals("Run")||event.getSource().equals(jMIAsRun))//performs the action of moveAutomatically method with one click
		{
			allTime.start();
			jBButRun.setIcon(new ImageIcon("./images/pause.png"));
			jBButRun.setText("Pause");
			moveAutomatically();
		}

		if(event.getActionCommand().equals("Pause")||event.getSource().equals(jMIAsPause)) //stops the ball
		{
			jBButRun.setIcon(new ImageIcon("./images/run.png"));
			jBButRun.setText("Run");
			autoRunTimer.stop();
			allTime.stop();
		}

		if(event.getActionCommand().equals("Reset")||event.getSource().equals(jMIAsReset))//performs the action of reSet method 
		{
			reSet();
		}

		if(event.getActionCommand().equals("About"))//displays the message when clicked
		{
			JOptionPane.showMessageDialog(null, "Program:   Assignment 2: Application – Ball Maze\r\n" + 
					"Filename:  CBallMaze.java                              \r\n" + 
					"@author:   © Neetu Kumari Das                       \r\n" + 
					"Course:    BSc Computing Year 1        \r\n" + 
					"Module:    CSY1020 Problem Solving & Programming       \r\n" + 
					"Tutor:     Kumar Lamichhane                                   \r\n" + 
					"@version:  2.0 Incorporates Artificial Intelligence!  \r\n" + 
					"Date:      01/06/18       ");
		}

		//Options
		//action performed when Option 1 is clicked
		if (event.getSource().equals (jBButOpt1)) 
		{
			JOptionPane.showMessageDialog(null, "Basic level Started!");
			reSet();//calling the 'reSet' method
			checkForOpt2=false; //disabling the autodownfall of the ball
			checkOpt2Applause=false;//disabling the applause clip
			jTFdOpt.setText("1");//sets 1 in the option's textfield 
		}

		//action performed when Option 2 is clicked
		if (event.getSource().equals(jBButOpt2)) 
		{
			JOptionPane.showMessageDialog(null, "Intermediate level Started!");
			reSet();
			checkForOpt2=true;//enabling the autodownfall of the ball
			checkOpt2Applause = true;//enabling the applause clip
			jTFdOpt.setText("2");//sets 2 in the option's textfield 
		}

		//action performed when Option 3 is clicked
		if (event.getSource().equals(jBButOpt3)) 
		{
			JOptionPane.showMessageDialog(null, "Advanced level Started!");
			reSet();
			addObstacle();//calling the method 'addObstacle'
			checkForOpt2=true;
			checkOpt2Applause = true;
			jTFdOpt.setText("3");//sets 3 in the option's textfield 
		}

		//action performed when exit is clicked
		if (event.getSource().equals(jBButExit)||event.getSource().equals(jMIAsExit)) 
		{
			System.exit(0);//exits the frame
		}	
	}
}
