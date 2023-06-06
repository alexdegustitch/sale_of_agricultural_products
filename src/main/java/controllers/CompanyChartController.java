/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import beans.Company;
import beans.Farmer;
import beans.ItemOrder;
import beans.NurseryGarden;
import beans.Order;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.servlet.http.HttpSession;
import models.OrderModel;

import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.axes.cartesian.CartesianScales;
import org.primefaces.model.charts.axes.cartesian.linear.CartesianLinearAxes;
import org.primefaces.model.charts.axes.cartesian.linear.CartesianLinearTicks;
import org.primefaces.model.charts.bar.BarChartDataSet;
import org.primefaces.model.charts.bar.BarChartModel;
import org.primefaces.model.charts.bar.BarChartOptions;
import org.primefaces.model.charts.optionconfig.legend.Legend;
import org.primefaces.model.charts.optionconfig.legend.LegendLabel;
import org.primefaces.model.charts.optionconfig.title.Title;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import models.FarmerModel;
import models.ItemOrderModel;
import models.NurseryGardenModel;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author Aleksandar
 */
@ManagedBean
@SessionScoped
public class CompanyChartController {

    private BarChartModel barModel;

    private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,
            Font.BOLD);
    private static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.NORMAL, BaseColor.RED);
    private static Font redFontBold = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.BOLD, BaseColor.RED);
    private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16,
            Font.BOLD);
    private static Font small = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.NORMAL);

    private StreamedContent file;

    public void check_type() {
        HttpSession session = util.SessionUtils.getSession();
        String type = (String) session.getAttribute("type");

        FacesContext context = FacesContext.getCurrentInstance();
        if (type == null) {
            try {
                context.getExternalContext().redirect("index.xhtml");
            } catch (IOException ex) {
                Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (type.equals("a")) {

            try {
                context.getExternalContext().redirect("admin.xhtml");
            } catch (IOException ex) {
                Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (type.equals("f")) {
            try {
                context.getExternalContext().redirect("farmer.xhtml");
            } catch (IOException ex) {
                Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void create_bar_chart() {
        HttpSession session = util.SessionUtils.getSession();
        Company company = (Company) session.getAttribute("company");

        barModel = new BarChartModel();
        ChartData data = new ChartData();

        BarChartDataSet barDataSet = new BarChartDataSet();
        barDataSet.setLabel("Last 30-Day Orders' Hirstory - " + company.getFull_name());

        List<Number> values = new LinkedList<>();
        List<String> labels = new LinkedList<>();

        LocalDate date;
        List<Order> orders;

        for (int i = 30; i > 0; i--) {
            date = LocalDate.now();
            date = date.minus(i, ChronoUnit.DAYS);

            orders = OrderModel.get_accepted_orders_for_date_company(date, company.getShort_name());
            values.add(orders.size());
            labels.add(date.toString());

        }

        barDataSet.setData(values);

        barDataSet.setBackgroundColor("rgba(255, 99, 132, 0.2)");
        barDataSet.setBorderColor("rgb(255, 99, 132)");
        barDataSet.setBorderWidth(1);

        data.addChartDataSet(barDataSet);

        data.setLabels(labels);

        barModel.setData(data);

        //options
        BarChartOptions options = new BarChartOptions();
        CartesianScales cScales = new CartesianScales();
        CartesianLinearAxes linearAxes = new CartesianLinearAxes();
        CartesianLinearTicks ticks = new CartesianLinearTicks();
        ticks.setBeginAtZero(true);
        ticks.setStepSize(1);

        linearAxes.setTicks(ticks);
        cScales.addYAxesData(linearAxes);
        options.setScales(cScales);

        Title title = new Title();
        title.setDisplay(true);
        title.setText("Bar Chart");
        options.setTitle(title);

        Legend legend = new Legend();
        legend.setDisplay(true);
        legend.setPosition("top");
        LegendLabel legendLabels = new LegendLabel();
        legendLabels.setFontStyle("bold");
        legendLabels.setFontColor("#2980B9");
        legendLabels.setFontSize(24);
        legend.setLabels(legendLabels);
        options.setLegend(legend);

        barModel.setOptions(options);
    }

    public void create_pdf() {
        HttpSession session = util.SessionUtils.getSession();
        Company company = (Company) session.getAttribute("company");

        Document document = new Document();

        //String file_name = "C:\\pictures\\iTextHelloWorld-" + (int) (Math.random() * 100000) + ".pdf";
        //String file_name = "report.pdf";
        String file_name = "C:\\pictures\\report.pdf";
        try {
            PdfWriter instance = PdfWriter.getInstance(document, new FileOutputStream("report.pdf"));

            document.open();

            Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);

            //title
            Paragraph title = new Paragraph();
            // We add one empty line
            addEmptyLine(title, 1);
            // Lets write a big header
            title.add(new Paragraph("Last 30-Day Orders' Hirstory - " + company.getFull_name(), catFont));

            addEmptyLine(title, 1);
            //Chunk header = new Chunk("Last 30 Days Report for Company - " + company.getFull_name());
            title.setAlignment(Element.ALIGN_CENTER);
            title.setIndentationLeft(50);
            document.add(title);

            LocalDate date;
            List<Order> orders;

            for (int i = 30; i > 0; i--) {
                date = LocalDate.now();
                date = date.minus(i, ChronoUnit.DAYS);

                orders = OrderModel.get_accepted_orders_for_date_company(date, company.getShort_name());

                Chunk chunk = new Chunk("Date: " + date.toString(), subFont);

                Paragraph p = new Paragraph(chunk);
                p.setFirstLineIndent(2.0f);

                document.add(p);
                if (orders.isEmpty()) {
                    chunk = new Chunk("No Orders", small);
                    document.add(chunk);

                }
                for (int j = 0; j < orders.size(); j++) {
                    Order o = orders.get(j);

                    chunk = new Chunk((j + 1) + ". Time of order: " + o.getDate_of_order().toLocalTime().toString() + "\n", small);
                    document.add(chunk);

                    Farmer f = FarmerModel.find_farmer_by_username(o.getFarmer_username());
                    if (f == null) {
                        chunk = new Chunk("USER DELETED\n", small);
                    } else {
                        chunk = new Chunk("Farmer's name: " + f.getFirst_name() + " " + f.getLast_name() + "\n", small);
                    }
                    document.add(chunk);

                    NurseryGarden ng = NurseryGardenModel.find_nursery_garden(o.getId_nursery());

                    if (ng == null) {
                        chunk = new Chunk("NURSERY ADDRESS UNKNOWN\n", small);
                    }else{
                        chunk = new Chunk("Nursery's address: " + ng.getLocation() + "\n", small);
                    }
                    document.add(chunk);

                    chunk = new Chunk("Price: " + o.getPrice() + "\n", redFontBold);
                    document.add(chunk);

                    List<ItemOrder> items = ItemOrderModel.get_not_canceled_items_for_order(o.getId_order());

                    Paragraph items_p = new Paragraph();
                    items_p.setExtraParagraphSpace(4.0f);

                    for (int k = 0; k < items.size(); k++) {
                        ItemOrder item_order = items.get(k);
                        Chunk item_c = new Chunk("Item: " + item_order.getItem_name() + ", amount: " + item_order.getAmount() + ", ", small);
                        Chunk item_price = new Chunk("price: " + item_order.getPrice() + "\n", redFont);

                        items_p.add(item_c);
                        items_p.add(item_price);
                    }

                    addEmptyLine(items_p, 2);
                    document.add(items_p);
                }

            }

            document.close();

            export_pdf();
        } catch (FileNotFoundException | DocumentException ex) {
            Logger.getLogger(CompanyChartController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void export_pdf() {
        HttpSession session = util.SessionUtils.getSession();
        Company company = (Company) session.getAttribute("company");

        try {
            FacesContext context = FacesContext.getCurrentInstance();
            ExternalContext externalContext = context.getExternalContext();

            externalContext.responseReset();
            externalContext.setResponseContentType("application/pdf");
            externalContext.setResponseHeader("Content-Disposition", "attachment;filename=\"report" + company.getShort_name() + "-" + LocalDate.now().toString() + " .pdf\"");

            FileInputStream inputStream = new FileInputStream(new File("report.pdf"));
            OutputStream outputStream = externalContext.getResponseOutputStream();

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            inputStream.close();
            context.responseComplete();

        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
        //FileInputStream instream = null;
        //FileOutputStream outstream = null;
        //try {
        // File infile = new File("C:\\pictures\\report.pdf");
        //File outfile = new File("C:\\pictures\\report3.pdf");
        //instream = new FileInputStream(infile);
        //outstream = new FileOutputStream(outfile);
        //byte[] buffer = new byte[1024];
        //int length;
        /*copying the contents from input stream to
    	     * output stream using read and write methods
         */
        //while ((length = instream.read(buffer)) > 0) {
        //   outstream.write(buffer, 0, length);
        //}
        //Closing the input/output file streams
        //System.out.println("File copied successfully!!");
        //File file = new File("C:\\file.csv");
        //InputStream input = new FileInputStream(file);
        //ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        //setFile(new DefaultStreamedContent(instream, externalContext.getMimeType(infile.getName()), infile.getName()));
        //System.out.println("PREP = " + file.getName());
        //instream.close();
        //outstream.close();
        //ovde krece export
        //} catch (IOException ioe) {
        //    ioe.printStackTrace();
        //}
    }

    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }

    public BarChartModel getBarModel() {
        return barModel;
    }

    public void setBarModel(BarChartModel barModel) {
        this.barModel = barModel;
    }

    public StreamedContent getFile() {
        return file;
    }

    public void setFile(StreamedContent file) {
        this.file = file;
    }

}
