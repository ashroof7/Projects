package GUI;


import java.io.File;
import java.util.HashMap;
import java.util.Map;

import methods.interpolation.Divide;
import methods.interpolation.Lagrange;
import methods.nonlineareq.EquationsSolver;
import methods.nonlineareq.methods.Method;
import methods.nonlineareq.methods.Method.Solution;

import parse.FileParser;
import parse.Parser;
import parse.PointParser;

import function.Function;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


public class Main extends Application  {

	
	public static void main(String[] args) {
		launch(args);
	}
	
	Function fn ;
	PointParser pp;
	Parser p; 
	Function fn2 ;
	Function fn3 ;
	
	TextArea points = new TextArea();
	TextField input = new TextField();
	//Buttons
	VBox buttons = new VBox(7);
	Button fixedPoint = new Button("Fixed Point");
	Button bisection = new Button("Bisection");
	Button falsePosition = new Button("False Position");
	Button newtonRaphson = new Button("Newton Raphson");
	Button secant = new Button("Secant");
	Button lagrange = new Button("Lagrange");
	Button dividedDif = new Button("Divided Difference");
	Button load = new Button("Load");
	Button plot = new Button("Plot");
	Button onlyPlot = new Button("Only Plot");
	Separator separator = new Separator();
	
	
	HBox all = new HBox(11);
	TextField output = new TextField();
	VBox inout = new VBox(7);
	Label in = new Label("Input");
	Label out = new Label("output");
	Font font = new Font("Ubuntu", 17);
	
	
	//infoIn
	TextField maxIt = new TextField();
	TextField startPoint = new TextField();
	TextField endPoint = new TextField();
	TextField PrecIn = new TextField();
	Label maxItLabel = new Label("Max It");
	Label startPointLabel = new Label("Start point");
	Label endPointLabel = new Label("End point");
	Label PrecInLabel = new Label("Precision");  
	HBox infoIn = new HBox(7);

	//infoOut
	TextField it = new TextField();
	TextField PrecOut = new TextField();
	TextField root = new TextField();
	TextField stop  = new TextField();
	TextField errorOut = new TextField(); 
	Label itLabel = new Label("Iterations");
	Label PrecOutLabel = new Label("Precision");
	Label rootLabel = new Label("Root");
	Label stopLabel = new Label("Stop Reason");
	Label errorOutLabel = new Label("Error");
	HBox infoOut = new HBox(7);
	HBox errorBox = new HBox(7);
	
	private void setListenrs1(){

		dividedDif.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				try {
					createFn();
					Divide d = new Divide(fn, pp.getXArray());
					output.setText(d.solve().toString());
					errorOut.setText(d.errorBound().toString());
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});
		
		lagrange.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				try {
					createFn();
					Lagrange l1 = new Lagrange(pp.getPoints());
					fn2 = l1.solve();
					output.setText(fn2.toString());
					if (!input.getText().isEmpty()) {
						fn3 = l1.getError(fn);
						errorOut.setText(fn3.toString());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		load.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				FileChooser fc = new FileChooser();
				File f = fc.showOpenDialog(null);
				try {
					FileParser fp = new FileParser(f);
					fn = fp.getFn();
					maxIt.setText(fp.getMaxIt()+"");
					PrecIn.setText(fp.getPrec()+"");
					startPoint.setText(fp.getStartPt()+"");
					endPoint.setText(fp.getEndPt()+"");
					pp = fp.getPp();
					points.setText(fp.getPoints());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		plot.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				
				LaunchPlotter lp = new LaunchPlotter();
				lp.c.addData(fn, "given");
				if (fn2!=null)
					lp.c.addData(fn2, "interpolated");
				if (fn3!=null)
					lp.c.addData(fn3, "error");
				
				try {
					lp.start(new Stage());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	
		onlyPlot.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				OnlyPlot op = new OnlyPlot();
				try {
					op.start(new Stage());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}
	
	private void setListenrs2(){
		fixedPoint.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				try {
					createFn();
					Map<String, Object> arguments_bundle = new HashMap<String, Object>();

					// Fixed Point;
					arguments_bundle.put(Method.MAX_ITERATIONS,
							Integer.parseInt(maxIt.getText())); // 20 max
																// iterations
					arguments_bundle.put(Method.TOLERANCE,
							Double.parseDouble(PrecIn.getText())); // tolerance
					arguments_bundle.put(Method.START_POINT,
							Double.parseDouble(startPoint.getText())); // the
																		// value
																		// for
																		// p0

					Solution ss = EquationsSolver.solve(fn, arguments_bundle,
							Method.FIXED_POINT_METHOD);
					output.setText("" + ss.solution);
					
					it.setText(ss.iterations+"");
					root.setText(ss.solution+"");
					stop.setText(ss.stopping_reason+"");
					errorOut.setText(ss.error+"");
					
					// points.setText("" + ss.points);
				} catch (Exception e) {
					System.out.println("can't be solved");
				}

			}
		});
		
		bisection.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				try {
					createFn();
					Map<String, Object> arguments_bundle = new HashMap<String, Object>();
					arguments_bundle.put(Method.MAX_ITERATIONS,
							Integer.parseInt(maxIt.getText()));
					arguments_bundle.put(Method.TOLERANCE,
							Double.parseDouble(PrecIn.getText()));
					arguments_bundle.put(Method.START_POINT,
							Double.parseDouble(startPoint.getText()));
					arguments_bundle.put(Method.END_POINT,
							Double.parseDouble(endPoint.getText()));

					Solution ss = EquationsSolver.solve(fn, arguments_bundle,
							Method.BISECTION_METHOD);
					output.setText("" + ss.solution);
					it.setText(ss.iterations+"");
					root.setText(ss.solution+"");
					stop.setText(ss.stopping_reason+"");
					errorOut.setText(ss.error+"");
					// points.setText("" + ss.points);
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("can't be solved");
				}
			}
		});
		
		falsePosition.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				try {
					createFn();
					Map<String, Object> arguments_bundle = new HashMap<String, Object>();
					arguments_bundle.put(Method.MAX_ITERATIONS,
							Integer.parseInt(maxIt.getText()));
					arguments_bundle.put(Method.TOLERANCE,
							Double.parseDouble(PrecIn.getText()));
					arguments_bundle.put(Method.START_POINT,
							Double.parseDouble(startPoint.getText()));
					arguments_bundle.put(Method.END_POINT,
							Double.parseDouble(endPoint.getText()));

					Solution ss = EquationsSolver.solve(fn, arguments_bundle,
							Method.FALSE_POSITION_METHOD);
					output.setText("" + ss.solution);
					// points.setText("" + ss.points);
				} catch (Exception e) {
					System.out.println("can't be solved" + e.getMessage());
					// e.printStackTrace();
				}

			}
		});
		
		secant.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				try {
					createFn();
					Map<String, Object> arguments_bundle = new HashMap<String, Object>();
					arguments_bundle.put(Method.MAX_ITERATIONS,
							Integer.parseInt(maxIt.getText()));
					arguments_bundle.put(Method.TOLERANCE,
							Double.parseDouble(PrecIn.getText()));
					arguments_bundle.put(Method.START_POINT,
							Double.parseDouble(startPoint.getText()));
					arguments_bundle.put(Method.END_POINT,
							Double.parseDouble(endPoint.getText()));

					Solution ss = EquationsSolver.solve(fn, arguments_bundle,
							Method.SECANT_METHOD);
					output.setText("" + ss.solution);
					it.setText(ss.iterations+"");
					root.setText(ss.solution+"");
					stop.setText(ss.stopping_reason+"");
					errorOut.setText(ss.error+"");
					// points.setText("" + ss.points);
				} catch (Exception e) {
					System.out.println("can't be solved" + e.getMessage());
					// e.printStackTrace();
				}

			}
		});
		
		newtonRaphson.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				try {
					createFn();
					Map<String, Object> arguments_bundle = new HashMap<String, Object>();
					arguments_bundle.put(Method.MAX_ITERATIONS,
							Integer.parseInt(maxIt.getText()));
					arguments_bundle.put(Method.TOLERANCE,
							Double.parseDouble(PrecIn.getText()));
					arguments_bundle.put(Method.START_POINT,
							Double.parseDouble(startPoint.getText()));

					Solution ss = EquationsSolver.solve(fn, arguments_bundle,
							Method.NEWTON_METHOD);
					output.setText("" + ss.solution);
					it.setText(ss.iterations+"");
					root.setText(ss.solution+"");
					stop.setText(ss.stopping_reason+"");
					errorOut.setText(ss.error+"");
					// points.setText("" + ss.points);
				} catch (Exception e) {
					System.out.println("can't be solved" + e.getMessage());
					// e.printStackTrace();
				}

			}
		});
	}
	
	private void setInOut(){
		it.setEditable(false);
		PrecOut.setEditable(false);
		root.setEditable(false);
		stop.setEditable(false);
		errorOut.setEditable(false);
		
		maxIt.setPrefWidth(50);
		startPoint.setPrefWidth(50);
		endPoint.setPrefWidth(50);
		PrecIn.setPrefWidth(50);
		it.setPrefWidth(50);
		
		PrecOut.setPrefWidth(50);
		errorOut.setPrefWidth(500);
		root.setPrefWidth(50);
		stop.setPrefWidth(50);
		maxIt.setPrefWidth(50);
		
		fixedPoint.setPrefWidth(145) ;
		bisection.setPrefWidth(145) ;
		falsePosition.setPrefWidth(145) ;
		newtonRaphson.setPrefWidth(145) ;
		secant.setPrefWidth(145) ;
		lagrange.setPrefWidth(145) ;
		dividedDif.setPrefWidth(145) ;
		load.setPrefWidth(145) ;
		plot.setPrefWidth(145) ;
		onlyPlot.setPrefWidth(145) ;
		
	}
	
	public void createFn() throws Exception{
		p = new Parser(input.getText());
		if (!points.getText().trim().isEmpty()){
			System.out.println(points.getText());
			pp = new PointParser(points.getText());
			
		}
		fn = p.getPoly();
	}
	
	private void setNodes(){
		in.setFont(font);
		out.setFont(font);
		setInOut();
		infoIn.getChildren().addAll(maxItLabel, maxIt, PrecInLabel, PrecIn,
			    startPointLabel, startPoint, endPointLabel, endPoint);
		infoOut.getChildren().addAll(itLabel,it,PrecOutLabel,PrecOut,rootLabel,root,
				stopLabel,stop);
		buttons.getChildren().addAll(fixedPoint,bisection,falsePosition,newtonRaphson,
				secant,lagrange,dividedDif,separator,load,plot,onlyPlot);
		
		
		errorBox.getChildren().addAll(errorOutLabel,errorOut);
		
		inout.getChildren().addAll(in,input,points,infoIn,out,output,infoOut,errorBox);
		all.getChildren().addAll(inout,buttons);
		all.setPadding(new Insets(11,11,11,11));
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		Group root = new Group(all);
		Scene scene = new Scene(root);
		setNodes();
		setListenrs1();
		setListenrs2();
		stage.setWidth(715);
		stage.setHeight(430);
		stage.setScene(scene);
		scene.getStylesheets().add("style.css");
		stage.show();
		
	}

}
