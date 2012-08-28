import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;

import javax.swing.JOptionPane;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Main extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	// Concerning solvers
	boolean precIsDouble = true;
	boolean withPivoting = false ;
	int maxItNumber = 20;
	double prec = 0.0001;
	int Bs = 1;
	int neqn = 0;

	Stage stage;
	Scene scene;
	Stage tableStage;
	Group root = new Group();
	VBox vb = new VBox(15);
	Label title = new Label("Solving System of Eqns");
	Label nEqnText = new Label("# Eqns");
	Label precText = new Label("Precesion");
	Label maxItText = new Label("MaxIt");
	Label BText = new Label("# B");
	
	ToggleButton isFloatButton = new ToggleButton("Float");
	ToggleButton pivotingButton = new ToggleButton("Pivoting");
	
	TextField nEqnField = new TextField();
	TextField precField = new TextField();
	TextField maxItField = new TextField();
	TextField BField = new TextField();
	ChoiceBox<String> cp = new ChoiceBox<String>(
			FXCollections.observableArrayList("Guassian Elimination",
					"LU Decomposition", "Jacobi", "Guass Seidel"));

	RadioButton loadFile = new RadioButton("Load from file");
	RadioButton insTable = new RadioButton("Insert into table");

	Button next = new Button("Next");
	Button cancel = new Button("Cancel");
	HBox footer = new HBox(7);

	ToggleGroup tg = new ToggleGroup();
	HBox hb = new HBox(7);
	HBox hb2 = new HBox(7);
	VBox tgVBox = new VBox(7);

	HBox fileBox = new HBox(7);
	TextField filePath = new TextField();
	Button browse = new Button("Browse");
	Table table;

	// for table frame
	VBox tableVb = new VBox(7);
	Button tableNext = new Button("Next");
	Button tableBack = new Button("Back");

	public void readFromFile()throws IOException{
		@SuppressWarnings("resource")
		BufferedReader br = new BufferedReader(new FileReader(filePath.getText()));
		ObservableList<String[]> data = FXCollections.observableArrayList();
		for (int i = 0; i < neqn ; i++) {
			String[] temp = br.readLine().split(" ");
			data.add(temp);
		}
		table.setData(data);
	}
	
	public void createTable(){
		try {
			Bs = Integer.parseInt(BField.getText().trim());
		} catch (Exception e) {
			System.out.println("Default value of Bs loaded = 1");
		}

		int n = 0;
		try {
			n = Integer.parseInt(nEqnField.getText().trim());
			neqn = n;
		} catch (Exception e) {
			System.out.println("Wrong data :|");
			return;
		}
			root.getChildren().clear();

			// FIXME if u added new options numbers differ
			int selection = cp.getSelectionModel().getSelectedIndex();
			if (selection == 2 || selection == 3) {
				// only on B
				String[] colNames = new String[n + 2];
				colNames[n ] = " B ";
				colNames[n + 1] = "InitGuess";
				for (int i = 0; i < n; i++)
					colNames[i] = "X" + (i + 1);
				table = new Table(n, n + 2, colNames, true,1);
				
			}else if (selection == 1){ //LU chosen
				String[] colNames = new String[n + Bs];
				for (int i = n; i < n+Bs; i++)
					colNames[i] = "B" + (i + 1-n);
				for (int i = 0; i < n; i++)
					colNames[i] = "X" + (i + 1);
				table = new Table(n, n + Bs, colNames, false,Bs);
				
			} else {
				//Guassian Elimination
				String[] colNames = new String[n + 1];
				colNames[n] = " B ";
				for (int i = 0; i < n; i++)
					colNames[i] = "X" + (i + 1);
				table = new Table(n, n + 1, colNames, false,1);

			}

			tableVb.getChildren().clear();
			setTableNodes();
			root.getChildren().add(tableVb);

			tableStage = new Stage();
			tableStage.setTitle("Eqns System");
			tableStage.setScene(scene);
			tableStage.show();
			stage.close();
				
			precIsDouble = !isFloatButton.isSelected();
			withPivoting = pivotingButton.isSelected();
			//reading prec and max it
			try{
				prec = Double.parseDouble(precField.getText().trim());
			}catch(NumberFormatException e){
				System.err.println("Error in prec value, defult value loaded");
			}
			try{
				maxItNumber = Integer.parseInt(maxItField.getText().trim());
			}catch(NumberFormatException e){
				System.err.println("Error in maxIt value, defult value loaded");
			}

	}
	
	public void setNodes() {
		title.setAlignment(Pos.TOP_CENTER);
		title.setFont(Font.font("Ubuntu", 45));
		title.setPrefWidth(500);
		nEqnField.setPrefWidth(30);
		BField.setPrefWidth(30);
		precField.setPrefWidth(50);
		maxItField.setPrefWidth(35);
		loadFile.setToggleGroup(tg);
		insTable.setToggleGroup(tg);
		footer.getChildren().addAll(next, cancel);
		footer.setAlignment(Pos.BOTTOM_RIGHT);
		hb.getChildren().addAll(nEqnText, nEqnField, cp,pivotingButton,isFloatButton);
		hb2.getChildren().addAll(precText,precField,maxItText,maxItField,BText,BField);
		filePath.setPrefWidth(320);
		fileBox.getChildren().addAll(loadFile, filePath, browse);
		tgVBox.getChildren().addAll(fileBox, insTable, footer);
		vb.getChildren().addAll(title, hb,hb2, tgVBox);
		vb.setPadding(new Insets(9, 9, 9, 9));
	}

	public void setTableNodes() {

		HBox tableButtons = new HBox(7);
		tableButtons.getChildren().addAll(table, tableNext, tableBack);
		tableButtons.setAlignment(Pos.BOTTOM_RIGHT);
		tableButtons.setPadding(new Insets(0, 9, 0, 0));
		tableVb.setPadding(new Insets(0, 0, 9, 0));
		tableVb.getChildren().addAll(table, tableButtons);

	}

	public void showOutput(String s, double time){
		VBox VB = new VBox(7);
		Label notice = new Label ("Full steps are saved in a file named DetailedSoln.txt");
		Label X = new Label ("X = \n"+s);
		Label timeLabel = new Label("time : "+time+" ms");
		X.setFont(new Font("Ubuntu",20));
		VB.setAlignment(Pos.CENTER);
		VB.setPadding(new Insets(11,11,11,11));
		VB.getChildren().addAll(X,timeLabel,notice);
		Stage st = new Stage();
		st.setTitle("Output");
		Scene sc = new Scene(VB);
		st.setScene(sc);
		st.show();
	}
	
	public void handlers() {
		browse.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				System.out.println("file");
				FileChooser fc = new FileChooser();
				File file = fc.showOpenDialog(null);
				filePath.setText(file.getAbsolutePath());
			}
		});

		cancel.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				System.exit(0);
			}
		});

		next.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
					createTable();
					if (tg.getSelectedToggle().equals(loadFile)) {
						
						try {readFromFile();}catch(IOException e){
							System.err.println("can't read from file");
						}
					}
				  
			}
		});

		tableNext.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				
				try{factory();}
				catch (Exception e) {
					JOptionPane.showMessageDialog(null, e.getMessage());
				}
				table.requestFocus();
			}
		});

		tableBack.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				root.getChildren().clear();
				root.getChildren().add(vb);
				tableStage.close();
				stage.show();
				stage.setScene(scene);

			}
		});
	}
	
	public void factory() throws Exception{
		
		
		int p = cp.getSelectionModel().getSelectedIndex();
		float[][] A = null;
		float[][] B = null;
		float[] B1D = null;
		float[] initGuess = null;
		double[][] Ad = table.getA();
		double[][] Bd = table.getB();
		double[] Bd1D = new double[Bd.length];
		for (int i = 0; i < Bd1D.length; i++) 
			Bd1D[i] = Bd[i][0];
		
		
		if (!precIsDouble) {
			// getting the float value of the arrays :D
			A = new float[Ad.length][Ad.length];
			B = new float[Bd.length][Bd[0].length];
			B1D = new float[B.length];
			for (int i = 0; i < A.length; i++) {
				for (int j = 0; j < B.length; j++)
					A[i][j] = (float) Ad[i][j];
				for (int j = 0; j < B.length; j++)
					B[i][j] = (float) Bd[i][j];
			}
			for (int i = 0; i < Bd1D.length; i++) 
				B1D[i] = B[i][0];
			
			
			// FIXME if added new options p values change
			if (p == 2 || p == 4) {
				double[] initGuessd = table.getInitGuess();
				initGuess = new float[initGuessd.length];
				for (int i = 0; i < initGuess.length; i++)
					initGuess[i] = (float) initGuessd[i];
			}
		}
		System.out.println(" p = "+p);
		
		switch (p) {
		
		case 0: // Guassian Elimination;
			if (table.getData().get(0).length < neqn +1)
				throw new IllegalStateException("Dimension Error");
			
			if (precIsDouble) {
				EliminationDouble solver = new EliminationDouble(Ad, Bd1D,withPivoting);
				 solver.solve();
				showOutput(Arrays.toString(solver.getX()),solver.getTime());
			} else {
				EliminationFloat solver = new EliminationFloat(A, B1D, withPivoting);
				 solver.solve();
				showOutput(Arrays.toString(solver.getX()),solver.getTime());
			}

			break;
		case 1: // LU Decomposition;
			if (table.getData().get(0).length < neqn + Bs)
				throw new IllegalStateException("Dimension Error");
			if (precIsDouble) {
				LUCompositionDouble solver = new LUCompositionDouble(Ad,Bd,withPivoting);
				 solver.solve();
				String s = "";
				LinkedList<double[]> solns = solver.getResult();
				for (double[] ds : solns) {
					s+=Arrays.toString(ds)+"\n";
				}
				showOutput(s,solver.getTime());
			} else {
				LUCompositionFloat solver = new LUCompositionFloat(A, B,withPivoting);
				 solver.solve();
				String s = "";
				LinkedList<float[]> solns = solver.getResult();
				for (float[] ds : solns) {
					s+=Arrays.toString(ds)+"\n";
				}
				showOutput(s,solver.getTime());
			}
			break;

		case 2: // Jacobi
			if (table.getData().get(0).length < neqn +2){
				System.err.println("Dimension Error");
				return;
			}
			if (precIsDouble) {
				GaussSeidelvsJacobi solver = new GaussSeidelvsJacobi(
						maxItNumber, prec, true, Ad,Bd1D,table.getInitGuess());
				
				System.err.println("A : ");
				for (int j = 0; j < Ad.length; j++) {
					System.err.println(Arrays.toString(Ad[j]));
				}				
				System.err.println("B : ");
					System.err.println(Arrays.toString(Bd1D));
					
				LinkedList<double[]>allX = solver.run();
				System.err.println("what H returns : "+ solver.getFinalX(allX));
				
				showOutput(Arrays.toString(solver.getFinalX(allX)),solver.getTime());
				solver.write(allX);
		
			} else {
				GaussSeidelvsJacobiF solver = new GaussSeidelvsJacobiF(maxItNumber, (float) prec, true, A,B1D,initGuess);
				LinkedList<float[]>allX = solver.run();
				showOutput(Arrays.toString(solver.getFinalX(allX)),solver.getTime());
				solver.write(allX);
			}
			break;

		case 3: // Guass Seidel
			if (table.getData().get(0).length < neqn +2){
				System.err.println("Dimension Error");
				return;
			}
			if (precIsDouble) {
				GaussSeidelvsJacobi solver = new GaussSeidelvsJacobi(
						maxItNumber, prec, false, Ad,Bd1D,table.getInitGuess());
				LinkedList<double[]>allX = solver.run();
				System.out.println("solve  "+solver.getFinalX(allX));
				
				showOutput(Arrays.toString(solver.getFinalX(allX)),solver.getTime());
				solver.write(allX);
			} else {
				GaussSeidelvsJacobiF solver = new GaussSeidelvsJacobiF(maxItNumber, (float) prec, false, A,B1D,initGuess);
				LinkedList<float[]>allX = solver.run();
				showOutput(Arrays.toString(solver.getFinalX(allX)),solver.getTime());
				solver.write(allX);
			}
			break;

		default:
			throw new IllegalArgumentException("NO Method is Selected");
		}

	}
	
	@Override
	public void start(Stage arg0) throws Exception {
		stage = new Stage();

		stage.setTitle("System fo Eqns Solver");
		setNodes();
		handlers();
		root.getChildren().add(vb);
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
}
