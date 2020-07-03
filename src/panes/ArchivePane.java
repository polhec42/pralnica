package panes;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.io.font.FontProgram;
import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.canvas.draw.DottedLine;
import com.itextpdf.kernel.pdf.canvas.draw.ILineDrawer;
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.Style;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.pdf.draw.DottedLineSeparator;
import database.Cloth;
import database.Connect;
import database.Order;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import utilities.AlertBox;
import utilities.Fonts;

import java.net.URL;
import java.util.ArrayList;


public class ArchivePane extends BorderPane {

    GridPane searchPart;
    StackPane showPart;
    HBox pdfPart;

    TableView table;

    TextField input;
    ComboBox<String> category;

    public ArchivePane(){
        this.setPadding(new Insets(10, 10, 10, 10));

        searchPart = new GridPane();
        showPart = new StackPane();
        pdfPart = new HBox();

        configureSearchPart();
        configureShowPart();
        configurePdfPart();

        this.setTop(searchPart);
        this.setCenter(showPart);
        this.setBottom(pdfPart);
    }

    private void configureShowPart() {

        this.showPart.setPadding(new Insets(10,10,10,10));

        table = new TableView();

        TableColumn<String, Order> firstColumn = new TableColumn<>("Šifra");
        firstColumn.setCellValueFactory(new PropertyValueFactory<>("cypher"));

        TableColumn<String, Order> secondColumn = new TableColumn<>("Ime prostovoljca");
        secondColumn.setCellValueFactory(new PropertyValueFactory<>("volunteer"));

        TableColumn<String, Order> thirdColumn = new TableColumn<>("Število kosov oblačil");
        thirdColumn.setCellValueFactory(new PropertyValueFactory<>("numberOfClothes"));

        TableColumn<String, Order> fourthColumn = new TableColumn<>("Datum");
        fourthColumn.setCellValueFactory(new PropertyValueFactory<>("date"));


        table.getColumns().addAll(firstColumn, secondColumn, thirdColumn, fourthColumn);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);



        table.setRowFactory(e -> {
            TableRow<Order> row = new TableRow<>();
            row.setOnMouseClicked(ev -> {
                if(ev.getClickCount() == 2 && !row.isEmpty()){

                    Stage newWindow = new Stage();
                    newWindow.setTitle("Podrobnosti");
                    newWindow.setScene(new Scene(new DetailsPane(row.getItem())));
                    newWindow.show();
                    newWindow.setOnCloseRequest(evs -> this.updateData());
                }
            });
            return row;
        });

        this.showPart.getChildren().add(table);
    }

    public void configureSearchPart(){

        this.searchPart.setHgap(5);

        category = new ComboBox<>();
        category.getItems().addAll(
                "Šifra",
                "Prostovoljec",
                "Obleka"
        );
        category.getSelectionModel().selectFirst();
        GridPane.setConstraints(category, 0, 0);
        searchPart.getChildren().add(category);

        input = new TextField();
        input.setPromptText(category.getSelectionModel().getSelectedItem());
        GridPane.setConstraints(input, 1, 0);
        searchPart.getChildren().add(input);

        category.setOnAction(e -> input.setPromptText(category.getSelectionModel().getSelectedItem()));

        Button submit = new Button("Išči");
        submit.setOnAction(ev -> {
            String searchCategory = category.getSelectionModel().getSelectedItem();
            String inputString = input.getText();

            this.table.getItems().clear();
            this.table.getItems().addAll(new Connect().getFilteredOrders(searchCategory, inputString, 1));
        });

        GridPane.setConstraints(submit, 2, 0);
        searchPart.getChildren().add(submit);

        searchPart.setAlignment(
                Pos.BASELINE_CENTER
        );

    }

    public void configurePdfPart(){

        this.pdfPart.setSpacing(5);

        Label dateFrom = new Label("Od datuma:");
        DatePicker datePickerFrom = new DatePicker();

        Label dateTo = new Label("Do datuma:");
        DatePicker datePickerTo = new DatePicker();
        Button submit = new Button("Ustvari PDF");

        submit.setOnAction(ev -> {
            ArrayList<Order> orders = new Connect().getFilteredOrders(datePickerFrom.getValue(), datePickerTo.getValue(), 1);
            String pdfName = datePickerFrom.getValue().toString() + " " +  datePickerTo.getValue().toString();
            createPdf(pdfName, orders);
            AlertBox.success("PDF je uspešno ustvarjen pod imenom: " + pdfName);
            new Connect().deleteOrders(orders);
            this.updateData();
        });

        this.pdfPart.getChildren().addAll(dateFrom, datePickerFrom, dateTo, datePickerTo, submit);
    }

    private void createPdf(String fileName, ArrayList<Order> orders){

        String file = ArchivePane.class.getResource("/") + fileName + ".pdf";
        file = file.substring(6);
        System.gc();
        try {

            PdfWriter writer = new PdfWriter(file);
            PdfDocument pdfDoc = new PdfDocument(writer);

            // Adding a new page
            pdfDoc.addNewPage();

            // Creating a Document
            Document document = new Document(pdfDoc);

            for(Order o : orders){
                ArrayList<Cloth> cloths = new Connect().getClothesOfOrder(o.getId());

                Paragraph cypher = new Paragraph("Šifra: " + o.getCypher()).setFont(Fonts.getRegularFont());
                Paragraph volunteer = new Paragraph("Ime prostovoljca: " + o.getVolunteer()).setFont(Fonts.getRegularFont());
                Paragraph date = new Paragraph("Datum: " + o.getDate()).setFont(Fonts.getRegularFont());

                document.add(cypher);
                document.add(volunteer);
                document.add(date);


                Style bold = new Style();
                PdfFont boldFont = PdfFontFactory.createFont(FontConstants.HELVETICA);
                bold.setFont(boldFont).setFontSize(12);
                bold.setBold();

                Table regularTable = createTable(cloths, false);

                document.add(regularTable);
                document.add(new Paragraph(new Text("\n")));

                ;
                //Morebitno doplačilo
                boolean payed = false;
                ArrayList<Cloth> payedCloths = new ArrayList<>();
                for(Cloth c : cloths){
                    if(c.getPayed() == 1){
                        payed = true;
                        payedCloths.add(c);
                    }
                }
                if(payed) {
                    document.add(new Paragraph(new Text("Morebitno doplačilo: DA")));
                    document.add(new Paragraph(new Text("\n")));
                    Table payedTable = createTable(payedCloths, true);
                    document.add(payedTable);
                    document.add(new Paragraph(new Text("\n")));
                }
                //

                LineSeparator line = new LineSeparator(new SolidLine());
                document.add(line);
            }

            Paragraph header = new Paragraph("Evidenčni list: Pranje oblačil")
                    .setFont(Fonts.getRegularFont())
                    .setFontSize(12)
                    .setFontColor(Color.BLACK);

            for (int i = 1; i <= pdfDoc.getNumberOfPages(); i++) {
                float x = 20;
                float y = pdfDoc.getPage(i).getPageSize().getTop() - 20;
                document.showTextAligned(header.setFontColor(Color.BLACK), x, y, i,
                        TextAlignment.LEFT, VerticalAlignment.BOTTOM, 0);
            }

            // Closing the document
            document.close();
            //System.out.println("PDF Created");
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private Table createTable(ArrayList<Cloth> cloths, boolean payed){

        try {

            Table table;

            if(payed){
                table = new Table(new float[]{50F, 50F, 100F, 600F, 50F, 50F, 50F});
            }else{
                table = new Table(new float[]{50F, 50F, 100F, 550F, 50F, 50F});
            }

            table.addHeaderCell(new Paragraph().add(new Text("Obleka").setFont(Fonts.getExtraBoldFont())));
            table.addHeaderCell(new Paragraph().add(new Text("Kategorija").setFont(Fonts.getExtraBoldFont())));
            table.addHeaderCell(new Paragraph().add(new Text("Barva").setFont(Fonts.getExtraBoldFont())));
            table.addHeaderCell(new Paragraph().add(new Text("Podrobnosti").setFont(Fonts.getExtraBoldFont())));
            table.addHeaderCell(new Paragraph().add(new Text("Urejeno").setFont(Fonts.getExtraBoldFont())));
            table.addHeaderCell(new Paragraph().add(new Text("Oddano").setFont(Fonts.getExtraBoldFont())));

            if(payed) table.addHeaderCell(new Paragraph().add(new Text("Plačano").setFont(Fonts.getExtraBoldFont())));

            for (Cloth c : cloths) {
                table.addCell(new Paragraph(c.getCloth()).setFont(Fonts.getRegularFont()));
                table.addCell(new Paragraph(c.getCategory()).setFont(Fonts.getRegularFont()));
                table.addCell(new Paragraph(c.getColor()).setFont(Fonts.getRegularFont()));
                table.addCell(new Paragraph(c.getDetail()).setFont(Fonts.getRegularFont()));
                table.addCell(new Paragraph(c.getDone() == 1 ? "Da" : "Ne").setFont(Fonts.getRegularFont()));
                table.addCell(new Paragraph(c.getTaken() == 1 ? "Da" : "Ne").setFont(Fonts.getRegularFont()));
                if (payed) {
                    table.addCell(new Paragraph(c.getPayed() == 1 ? "Da" : "Ne").setFont(Fonts.getRegularFont()));
                }
            }

            return table;
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    public void updateData(){

        this.table.getItems().clear();

        if(this.input.getText().length() > 0){
            String searchCategory = category.getSelectionModel().getSelectedItem();
            String inputString = input.getText();
            this.table.getItems().addAll(new Connect().getFilteredOrders(searchCategory, inputString, 1));
        }else {
            this.table.getItems().addAll(new Connect().getOrders(1));
        }
    }
}
