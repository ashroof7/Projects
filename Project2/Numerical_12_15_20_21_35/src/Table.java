import java.util.Arrays;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.util.Callback;



public class Table extends Group{

	

	private TableView<String[]> table = new TableView<String[]>();
	
    private int cols ;
    private int rows ;
	private String[] labels;
	private ObservableList<String[]> data = FXCollections.observableArrayList();
	
	//new added
	private double[][]A;
	private double[][]  B;
	private double[] initialGuess ;
	private boolean usingInitalGuess;
	private int Bs;
	
	public Table(int rows, int cols, String[] colNames, boolean usingGuess, int Bs){
		this.rows = rows;
		this.cols = cols;
		labels = colNames;
		usingInitalGuess = usingGuess;
		this.Bs = Bs;
		
		setTable();
		table.setMaxSize(700, 450);
		getChildren().add(table);
	}
	

    public void setTable() {
        //insert new rows
        for (int i = 0; i < rows; i++) {
			String s[] = new String[cols];
			Arrays.fill(s, "0");
			data.add(s);
		}

        table.setEditable(true);
        Callback<TableColumn<String[],String>, TableCell<String[], String>> cellFactory = 
        	    new Callback<TableColumn<String[],String>, TableCell<String[], String>>() {
        	        public TableCell<String[],String> call(TableColumn<String[],String> p) {
        	            return new EditingCell();
        	    }
        	};
        
        for (int i = 0; i <cols ; i++) {
			TableColumn<String[], String> col = new TableColumn<String[], String>(labels[i]);
			col.setMinWidth(77);
			final int k = i;
			col.setCellValueFactory(new Callback<CellDataFeatures<String[],String>,ObservableValue<String>>(){
                public ObservableValue<String> call(CellDataFeatures<String[], String> param) {
                    return new SimpleStringProperty(param.getValue()[k]);
                } });
			
			col.setCellFactory(cellFactory);
			
			col.setOnEditCommit(new EventHandler<CellEditEvent<String[], String>>() {
			    @Override public void handle(CellEditEvent<String[], String> t) {
			        ((String[])t.getTableView().getItems().get(
			            t.getTablePosition().getRow()))[k] = (t.getNewValue());
			    }
			});
			
			
			table.getColumns().add(col);
		}
        table.setItems(data); 
    }
	
	public ObservableList<String[]> getData(){
		return data;
	}
	public void setData(ObservableList<String[]> data){
		this.data = data;
		table.setItems(data);
	}
	
	public double[][] getA(){
		A = new double[rows][rows];
		int i = 0;
		for (String[] row : data) {
			for (int j = 0; j < rows; j++) {
				A[i][j] = Double.parseDouble(row[j]);
			}
			i++;
		}
		
		System.out.println("A : ");
		for (int j = 0; j < A.length; j++) {
			System.out.println(Arrays.toString(A[j]));
		}
		return A;
	}  
	

	public double[][] getB(){
		B = new double[rows][Bs];
		int i = 0;
		int minus = usingInitalGuess?1:0;
		
		for (String[] row : data) {
			for (int j = rows ; j < cols - minus ; j++) {
				B[i][j-rows] = Double.parseDouble(row[j]);
			}
			i++;
		}
		
		System.out.println("B : ");
		for (int j = 0; j < B.length; j++) {
			System.out.println(Arrays.toString(B[j]));
		}
		return B;
		
	}
	
	public double[] getInitGuess(){
		initialGuess = new double[rows];
		int i = 0;
		if (!usingInitalGuess) throw new IllegalStateException("No guess array available");
		
		for (String[] row : data) {
			initialGuess[i] = Double.parseDouble(row[cols-1]);
			i++;
		}
		
		System.out.println("intialGuess: "+Arrays.toString(initialGuess));
		return initialGuess;
	}
}
