/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.icics.mss.superrent.manager.managerinventory;


import java.io.File;
import ca.ubc.icics.mss.superrent.validation.Validate;
import static ca.ubc.icics.mss.superrent.validation.Validate.isEmptyComboBox;
import static ca.ubc.icics.mss.superrent.validation.Validate.isEmptyTextField;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;


/**
 * FXML Controller class
 *
 * @author warrior
 */
public class InventoryManageController implements Initializable {

    /**
     * Initializes the controller class.
     */
    Connection con=null;
    @FXML
    private Label avatype;
    @FXML
    private Label avacate;
    @FXML
    private Label avacom;
    @FXML
    private Label avayear;
    @FXML
    private Label avalo;
    @FXML
    private Label avadir;
    @FXML
    private Label avano;
    
    @FXML
    private Label uvatype;
    @FXML
    private Label uvacate;
    @FXML
    private Label uvacom;
    @FXML
    private Label uvayear;
    @FXML
    private Label uvalo;
    @FXML
    private Label uvadir;
    @FXML
    private Label uvano;
    
    
            
            
            
            
    @FXML
    private TextField direction;
    @FXML
    private TextField sPlate;
    @FXML
    private TextField edirection;
    @FXML
    private TextField ecompany;
    @FXML
    private TextField company;
    @FXML
    private Pane inpane;
    @FXML
    private Pane edpane;
    @FXML
    private Pane Forsale;
    @FXML
    private ComboBox Location;
    @FXML
    private ComboBox Year;
    @FXML
    private ComboBox Type;
    @FXML
    private ComboBox location;
    @FXML
    private ComboBox year;
    @FXML
    private ComboBox aCategory;
    @FXML
    private TextField PlateNo;
    @FXML
    private ComboBox type;
    @FXML
    private ComboBox elocation;
    @FXML
    private ComboBox eyear;
    @FXML
    private ComboBox etype;
    @FXML
    private ComboBox Category;
    @FXML
    private TextField aPlateNo;
    @FXML
    private ComboBox Status;
    @FXML
    private TextField esaleprice;
    @FXML
    private TableView <Intb>tb;
    @FXML
    private Button Edit;
    @FXML
    private Pane salepane;
    @FXML
    private TextField forsaleprice;
    @FXML
    private Button movetosale;
    @FXML
    private Button cancelsale;
    @FXML
    private Button sale;
    @FXML
    private ComboBox eCategory;
    @FXML
    private TextField saleprice;
    @FXML
    private DatePicker saledate;
    String vID;
    @FXML
    private TableColumn<Intb,String>tID;
    private TableColumn<Intb,String>  tName;
    private TableColumn<Intb,String> tBranch;
    private TableColumn<Intb,String> tType;
    private TableColumn<Intb,String> tCate;
    private TableColumn<Intb,String> tStatus;
    private TableColumn<Intb,Image> aArt;
    private TableColumn<Intb,String>tYear;
    private ObservableList data=FXCollections.observableArrayList();
    String status;
    @FXML
    private void search(ActionEvent event) throws SQLException, ClassNotFoundException{
        query(sqlstring());   
    }
    @FXML
    private void sPlate(ActionEvent event) throws SQLException, ClassNotFoundException{
        String s;
        if(!sPlate.getText().equals("")){
            s="SELECT vehicle_id,vehicle_name,vehicle_category,vehicle_type,plate_number,vehicle_thumbnail,vehicle_manufactured_year, location FROM vehicle, branch where vehicle.branch_id=branch.branch_id and plate_number like '%"+sPlate.getText()+"%';";
            query(s);
        }
        else
            query(sqlstring());
               
      
            

    }
    private void SetStatus(ComboBox cb) throws SQLException, ClassNotFoundException{
        ObservableList content=FXCollections.observableArrayList();
        content.add("ALL");
        content.add("Available");
        content.add("For Sale");
        content.add("Sold");
        cb.setItems(content);
        cb.getSelectionModel().selectFirst();
    }
    private void SetYear(ComboBox cb){
        String[] yearno=new String[10];
        ObservableList content=FXCollections.observableArrayList();
        content.add("ALL");
        for(int i=0;i<10;i++){
            yearno[i]=2005+i+"";
            content.add(yearno[i]);
        }
        cb.setItems(content);
        cb.getSelectionModel().selectFirst();
    }
    private void SetType(ComboBox cb){
        ObservableList content=FXCollections.observableArrayList();
        content.add("ALL");
        content.add("CAR");
        content.add("TRUCK");
        cb.setItems(content);
    }
    private void SetCategory(ComboBox cb) throws SQLException, ClassNotFoundException{
        getcon();
        String s="SELECT  vehicle_category FROM vehicle_category";
        ResultSet rs = con.createStatement().executeQuery(s);
        ObservableList content=FXCollections.observableArrayList();
        content.add("ALL");
        while(rs.next()){
            content.add(rs.getString(1));
        }
        cb.setItems(content);
        con.close();
        cb.getSelectionModel().selectFirst();
    }
    private void SetLocation(ComboBox cb) throws SQLException, ClassNotFoundException{
        getcon();
        String s="SELECT location FROM branch";
        ResultSet rs = con.createStatement().executeQuery(s);
        ObservableList content=FXCollections.observableArrayList();
        content.add("ALL");
        while(rs.next()){
            content.add(rs.getString(1));
        }
        cb.setItems(content);
        con.close();
        cb.getSelectionModel().selectFirst();
        
    }
    private boolean validateAll(ComboBox comboBox,Label error){
boolean valid = true;
        ObservableList<String> styleClass = comboBox.getStyleClass();
        if (comboBox.getValue().toString() == "ALL") {
            if (! styleClass.contains("error")) {
                styleClass.add("error");
            }
            error.setVisible(true);
            error.setText("This field is required");
        } else {
            // remove all occurrences:
            styleClass.removeAll(Collections.singleton("error"));
            valid = false;
            error.setVisible(false);
        }
        return valid;
    }
    private String sqlstring(){
        
        String s;
        s="SELECT vehicle_id,vehicle_name,vehicle_category,vehicle_type,plate_number,vehicle_thumbnail,vehicle_manufactured_year, location FROM vehicle, branch where vehicle.branch_id=branch.branch_id";
        if(Status.getValue().toString().equals("For Sale")){
            s="SELECT vehicle.vehicle_id,vehicle_name,vehicle_category,vehicle_type,plate_number,vehicle_thumbnail,vehicle_manufactured_year,location FROM vehicle, branch, for_sale_vehicle where vehicle.branch_id=branch.branch_id and vehicle.vehicle_id=for_sale_vehicle.vehicle_id";
            System.out.println(s);
        }
        if(Status.getValue().toString().equals("Sold")){
            s="SELECT vehicle.vehicle_id,vehicle_name,vehicle_category,vehicle_type,plate_number,vehicle_thumbnail,vehicle_manufactured_year,location FROM vehicle, branch, sold_vehicle where vehicle.branch_id=branch.branch_id and vehicle.vehicle_id=sold_vehicle.vehicle_id";
        }
        if(Status.getValue().toString().equals("Available")){
            s="SELECT vehicle.vehicle_id,vehicle_name,vehicle_category,vehicle_type,plate_number,vehicle_thumbnail,vehicle_manufactured_year,location "
                    + "FROM vehicle, branch "
                    + "where vehicle.branch_id=branch.branch_id"
                    +" and vehicle.vehicle_id  not in (select vehicle_id from for_sale_vehicle)"
                    +" and vehicle.vehicle_id  not in (select vehicle_id from sold_vehicle)";
        }
        if(!Location.getValue().toString().equals("ALL")){
            s=s+" and location='"+Location.getValue().toString()+"'";            
        }
        if(!Year.getValue().toString().equals("ALL")){
            s=s+" and vehicle_manufactured_year='"+Year.getValue().toString()+"'";
        }
        if(!Type.getValue().toString().equals("ALL")){
            s=s+" and vehicle_category='"+Type.getValue().toString()+"'";
        }
        return s;
    }
    private void query(String s) throws SQLException, ClassNotFoundException{
            data.clear();
            getcon();
            s=s+";";
           
            ResultSet rs = con.createStatement().executeQuery(s);
           // ResultSet rsf=con.createStatement().executeQuery("select vehicle_id from ");
            
            while(rs.next()){
                Intb in =new Intb();
                in.setVehicleID(rs.getString("vehicle_id"));
                in.setPlateNumber(rs.getString("plate_number"));
                in.setBranch(rs.getString("location"));
                in.setName(rs.getString("vehicle_name"));
                in.setYear(rs.getString("vehicle_manufactured_year"));
                in.setCategory(rs.getString("vehicle_category"));
                in.setType(rs.getString("vehicle_type"));
                in.setPic(rs.getBinaryStream("vehicle_thumbnail"));
                in.setStatus(get_status(rs.getString("vehicle_id")));
                data.add(in);
            }
            con.close();
            movetosale.setVisible(false);
            cancelsale.setVisible(false);
            sale.setVisible(false);
    }
    private String get_status(String id){
        String s=new String();
        String sql="select vehicle_id from ";
        try {
          
            ResultSet rs=con.createStatement().executeQuery(sql+"sold_vehicle where vehicle_id='"+id+"'");
            ResultSet rs1=con.createStatement().executeQuery(sql+"for_sale_vehicle where vehicle_id='"+id+"'");
            if(rs.next())
                s="Sold";
            else if(rs1.next())
                s="For Sale";
            else
                s="Available";
           
            
        } catch (SQLException ex) {
            Logger.getLogger(InventoryManageController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        return s;
    }
    
    private void getcon() throws SQLException, ClassNotFoundException{
        String dbUrl="jdbc:mysql://dbserver.mss.icics.ubc.ca/team02";
	String dbUserName="team02";
	String dbPassword="s0ftw@re";
	String jdbcName="com.mysql.jdbc.Driver";
        Class.forName(jdbcName);
        con=DriverManager.getConnection(dbUrl,dbUserName,dbPassword);
        //System.out.println("SUCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC");
    }
    
    @FXML
    public void AddVehicle(ActionEvent event){
        inpane.setVisible(true);
    }
    @FXML
    private void browseaction(ActionEvent event) {
        //Stage primaryStage;
        Stage stage=new Stage();
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("ALL files (*.*)", "*.*");
        fileChooser.getExtensionFilters().add(extFilter);
        System.out.println("aaaaaaaaaaa");
        File file=fileChooser.showOpenDialog(stage);
        System.out.println(file);
        direction.setText(file.toString()); 
       // fileChooser.showOpenDialog(primaryStage);
               // System.out.println(file);   
    }
    @FXML
        private void ebrowseaction(ActionEvent event) {
        //Stage primaryStage;
        Stage stage=new Stage();
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("ALL files (*.*)", "*.*");
        fileChooser.getExtensionFilters().add(extFilter);
        System.out.println("aaaaaaaaaaa");
        File file=fileChooser.showOpenDialog(stage);
        System.out.println(file);
        edirection.setText(file.toString()); 
       // fileChooser.showOpenDialog(primaryStage);
               // System.out.println(file);   
    }
    @FXML
    public void Cancelin(ActionEvent event){
        inpane.setVisible(false);
        company.setText("");
        direction.setText("");
        type.getSelectionModel().select("ALL");
        location.getSelectionModel().select("ALL");
        year.getSelectionModel().select("ALL");
        seterror();
        
        
    }
    @FXML
    public void eCancelin(ActionEvent event) throws SQLException{
        edpane.setVisible(false);
        Forsale.setVisible(false);
        forsaleprice.setText("");
        esaleprice.setText("");
        esaleprice.setEditable(false);
        seterror();
        con.close();
    }
    @FXML
    public void EditVehicle(ActionEvent event) throws SQLException, ClassNotFoundException{
        
        edpane.setVisible(true);
        Intb in=tb.getSelectionModel().getSelectedItem();
        vID=in.getVehicleID();
        elocation.getSelectionModel().select(in.Branch);
        etype.getSelectionModel().select(in.Type);
        eCategory.getSelectionModel().select(in.Category);
        //edirection.setText(in.Pic);
        ecompany.setText(in.Name);
        PlateNo.setText(in.PlateNumber);
        eyear.getSelectionModel().select(in.Year);
        status=in.Status;
        if(in.Status.equals("For Sale")){
            esaleprice.setEditable(true);
            getcon();
            ResultSet rs=con.createStatement().executeQuery("select for_sale_price from for_sale_vehicle where vehicle_id='"+in.VehicleID+"';");
            rs.next();
            esaleprice.setText(rs.getString(1));  
            con.close();
        }
    }
    @FXML
    public void ShowEdButton(ActionEvent event){
        Edit.setVisible(true);
        System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
    }
    
    @FXML
    public void Movetosale(ActionEvent event){
        Forsale.setVisible(true);    
        
    }
    @FXML
    public void cancelsale(ActionEvent event){
        Intb in=tb.getSelectionModel().getSelectedItem();
        try {
            getcon();
            con.createStatement().executeUpdate("delete from for_sale_vehicle where vehicle_id='"+in.VehicleID+"';");
            con.close();
            refresh();
            cancelsale.setVisible(false);
            
        } catch (SQLException ex) {
            Logger.getLogger(InventoryManageController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(InventoryManageController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    @FXML
    public void add(ActionEvent event) throws FileNotFoundException{
        Boolean a,b,c,d,e,f,g;
        a=isEmptyTextField (aPlateNo, avano);
        b=isEmptyComboBox(type,avatype)||validateAll(type,avatype);
        c=isEmptyComboBox(location,avalo)||validateAll(location,avalo);
        d=isEmptyComboBox(aCategory,avacate)||validateAll(aCategory,avacate);
        e=isEmptyComboBox(year,avayear)||validateAll(year,avayear);
        f=isEmptyTextField (company, avacom);
        g=isEmptyTextField (direction, avadir);
       
        

        if(!(a||b||c||d||e||f||g)){
             String et=type.getValue().toString();
        String ec=company.getText();
        String el=location.getValue().toString();
        String eCate=aCategory.getValue().toString();
        String Pl=aPlateNo.getText();
        String y=year.getValue().toString();
        String path=direction.getText();
                    try {
            getcon();
                        System.out.println("$$$$$$$$$$$$$$$$$$$$");
            ResultSet rs=con.createStatement().executeQuery("select branch_id from branch where location='"+el+"';");
            rs.next();

            String eb=rs.getString(1);
            String sql="insert into vehicle"
                + "(plate_number,vehicle_name,vehicle_type,vehicle_category,vehicle_thumbnail,vehicle_manufactured_year,branch_id)"
                + " values('"+Pl+"','"+ec+"','"+et+"','"+eCate+"',?,'"+y+"','"+eb+"');";
            //System.out.println(sql);
            PreparedStatement stmt=con.prepareStatement(sql);
            File image=new File(path);
            FileInputStream fis=new FileInputStream(image);
            stmt.setBinaryStream(1, fis, (int)image.length());
            stmt.execute();
            con.close();
            query(sqlstring());
            seterror();
            refresh();
            
        } catch (SQLException ex) {
            Logger.getLogger(InventoryManageController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(InventoryManageController.class.getName()).log(Level.SEVERE, null, ex);
        }
        }
        
        

        
    }
    
    @FXML
    public void opensale(ActionEvent event){
        salepane.setVisible(true);
        
    }
    @FXML
    public void sale(ActionEvent event) throws SQLException, ClassNotFoundException{
        String price=saleprice.getText();
        int y=saledate.getValue().getYear();
        int m=saledate.getValue().getMonth().getValue();
        int d=saledate.getValue().getDayOfMonth();
       // System.out.println(y+"-"+m+"-"+d);
        String date=y+"-"+m+"-"+d;
        Intb in=tb.getSelectionModel().getSelectedItem();
        String id=in.VehicleID;
        getcon();
        String s="insert into sold_vehicle values('"+id+"','"+price+"','"+date+"')";
        con.createStatement().execute(s);
        con.close();
        query(sqlstring());
        salepane.setVisible(false);
        saleprice.setText("");
        saledate.setValue(LocalDate.MIN);      
    }
    @FXML
    public void cansale(ActionEvent event){
         salepane.setVisible(false);
        saleprice.setText("");
        saledate.setValue(LocalDate.MIN);
    }
    @FXML void Moveforsale(ActionEvent event){
        String price=forsaleprice.getText();
        Intb in=tb.getSelectionModel().getSelectedItem();
        String id=in.VehicleID;
        try {
            getcon();
            String sql="insert into for_sale_vehicle(vehicle_id,for_sale_price) values('"+id+"','"+price+"');";
            con.createStatement().executeUpdate(sql);
            con.close();
            forsaleprice.setText("");
            Forsale.setVisible(false);
            movetosale.setVisible(false);
            cancelsale.setVisible(true);
            
        } catch (SQLException ex) {
            Logger.getLogger(InventoryManageController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(InventoryManageController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    @FXML
    public void Update(ActionEvent event){
         Boolean a,b,c,d,e,f,g;
        a=isEmptyTextField (PlateNo, uvano);
        b=isEmptyComboBox(etype,uvatype)||validateAll(etype,uvatype);
        c=isEmptyComboBox(elocation,uvalo)||validateAll(elocation,uvalo);
        d=isEmptyComboBox(eCategory,uvacate)||validateAll(eCategory,uvacate);
        e=isEmptyComboBox(eyear,uvayear)||validateAll(eyear,uvayear);
        f=isEmptyTextField (ecompany, uvacom);
        //g=isEmptyTextField (edirection, uvadir);
        if(!(a||b||c||d||e||f)){
        String et=etype.getValue().toString();
        String ec=ecompany.getText();
        String el=elocation.getValue().toString();
        String eCate=eCategory.getValue().toString();
        String Pl=PlateNo.getText();
        String year=eyear.getValue().toString();
        
        try {
            getcon();
            ResultSet rs=con.createStatement().executeQuery("select branch_id from branch where location='"+el+"';");
            rs.next();
            String eb=rs.getString(1);
            String sql="update vehicle set vehicle_manufactured_year='"+year+"', plate_number='"+Pl+"', vehicle_type='"+et+"', vehicle_category='"+eCate+"', vehicle_name='"+ec+"', branch_id='"+eb+"' where vehicle_id='"+vID+"';";
            System.out.println(sql);
      
            con.createStatement().executeUpdate(sql);
            

             if(status.equals("For Sale")){
            esaleprice.setEditable(true);
            
            con.createStatement().executeUpdate("update for_sale_vehicle set for_sale_price='"+esaleprice.getText()+"'where vehicle_id='"+vID+"';");
            
            }
             if(!isEmptyTextField(edirection)){
                 String path=edirection.getText();
                 String sqld="update vehicle set vehicle_thumbnail=? where vehicle_id='"+vID+"';";
                  PreparedStatement stmt=con.prepareStatement(sqld);
                  File image=new File(path);
                  FileInputStream fis=new FileInputStream(image);
                  stmt.setBinaryStream(1, fis, (int)image.length());
                  stmt.execute();
                 
             }
            esaleprice.setEditable(false);
            esaleprice.setText("");
            edpane.setVisible(false);
            con.close();
            seterror();
            refresh();
        } catch (SQLException ex) {
            Logger.getLogger(InventoryManageController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(InventoryManageController.class.getName()).log(Level.SEVERE, null, ex);
        }    catch (FileNotFoundException ex) {
                 Logger.getLogger(InventoryManageController.class.getName()).log(Level.SEVERE, null, ex);
             }
        }
        
        
    }
    @FXML
    public void tbact(){
        if(!tb.getSelectionModel().isEmpty()){
        Intb in=tb.getSelectionModel().getSelectedItem();
        if(in.Status.equals("For Sale")){
            movetosale.setVisible(false);
            sale.setVisible(true);
            cancelsale.setVisible(true);
            
        }
        if(in.Status.equals("Available")){
            sale.setVisible(false);
            movetosale.setVisible(true);
            cancelsale.setVisible(false);
        }
        if(in.Status.equals("Sold")){
            sale.setVisible(false);
            movetosale.setVisible(false);
            cancelsale.setVisible(false);
        }
        }
    
    }
    @FXML
    public void refresh() throws SQLException, ClassNotFoundException{
        inpane.setVisible(false);
        movetosale.setVisible(false);
        sale.setVisible(false);
        cancelsale.setVisible(false);
        type.getSelectionModel().selectFirst();
        company.setText("");
        location.getSelectionModel().selectFirst();
        aCategory.getSelectionModel().selectFirst();
        aPlateNo.setText("");
        year.getSelectionModel().selectFirst(); 
    }
    public void seterror(){
           avatype.setVisible(false);
           avacate.setVisible(false);
           avacom.setVisible(false);
           avayear.setVisible(false);
           avalo.setVisible(false);
           avadir.setVisible(false);
           avano.setVisible(false);
           uvatype.setVisible(false);
           uvacate.setVisible(false);
           uvacom.setVisible(false);
           uvayear.setVisible(false);
           uvalo.setVisible(false);
           uvadir.setVisible(false);
           uvano.setVisible(false);
    }
    
    
public void getTableView()
   {
       
	tb.setTableMenuButtonVisible(true);
        
        aArt = new TableColumn<>("Album");
	aArt.setCellValueFactory(new PropertyValueFactory("Pic"));
	aArt.setPrefWidth(150);
	
        tID=new TableColumn<>("Plate Number");
        tID.setPrefWidth(80);
        tID.setCellValueFactory(new PropertyValueFactory("PlateNumber"));
        
        tName=new TableColumn<>("Name");
        tName.setPrefWidth(80);
        tName.setCellValueFactory(new PropertyValueFactory("Name"));
        
        tCate=new TableColumn<>("Category");
        tCate.setPrefWidth(80);
        tCate.setCellValueFactory(new PropertyValueFactory("Category"));

        
        tType=new TableColumn<>("Type");
        tType.setPrefWidth(80);
        tType.setCellValueFactory(new PropertyValueFactory("Type"));
        
        tYear=new TableColumn<>("Year");
        tYear.setPrefWidth(80);
        tYear.setCellValueFactory(new PropertyValueFactory("Year"));
        
        tStatus=new TableColumn<>("Status");
        tStatus.setPrefWidth(80);
        tStatus.setCellValueFactory(new PropertyValueFactory("Status"));
        
        tBranch=new TableColumn<>("Branch");
        tBranch.setPrefWidth(80);
        tBranch.setCellValueFactory(new PropertyValueFactory("Branch"));
        
        aArt.setCellFactory(new Callback<TableColumn<Intb, Image>, TableCell<Intb, Image>>(){

            @Override
            public TableCell<Intb, Image> call(TableColumn<Intb, Image> param) {
                  TableCell<Intb, Image> cell = new TableCell<Intb, Image>(){
                       ImageView img=new ImageView();
                      @Override
                      public void updateItem(Image item, boolean empty){
                          if(item!=null){
                            VBox vb=new VBox();
                            vb.setAlignment(Pos.CENTER);
                            System.out.println("FFFFFFFFFFFFFFFFFFFFFFFFFFff");
                            img.setImage(item);
                            img.setFitHeight(50);
                            img.setFitWidth(75);
                            
                            vb.getChildren().addAll(img);
                            setGraphic(vb);
                      }else{
                             System.out.println("SSSSSSSSSSSSSSSSSSSS");
                             img.setImage(null);
                          }
                  }
                  };
                  return cell;
                }
        });
        tb.setItems(data);
        tb.getColumns().addAll(aArt,tID,tName,tCate,tType,tYear,tBranch,tStatus);         
   } 
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        SetYear(Year);
        getTableView();
        try {
            SetLocation(Location);
            SetCategory(Type);
            SetStatus(Status);
            SetType(etype);
            SetType(type);
            SetLocation(location);
            SetCategory(aCategory);
            SetYear(year);
            SetLocation(elocation);
            SetCategory(eCategory);
            SetYear(eyear);
            query(sqlstring());
        } catch (SQLException ex) {
            Logger.getLogger(InventoryManageController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(InventoryManageController.class.getName()).log(Level.SEVERE, null, ex);
        }   
    } 
    
}
