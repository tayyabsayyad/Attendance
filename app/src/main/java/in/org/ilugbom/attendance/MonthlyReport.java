package in.org.ilugbom.attendance;

import android.os.Environment;
import android.widget.EditText;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.ArrayList;

import javax.security.auth.Subject;

import static android.media.CamcorderProfile.get;

public class MonthlyReport {

    Model model = new Model();
    CreateDivDialog CDD=new CreateDivDialog();
    Msg msg=new Msg();

    ArrayList<String> DateArray = new ArrayList<String>();
    ArrayList<String> Divisions = new ArrayList<String>();
    ArrayList<String> RollNos = new ArrayList<String>();
    ArrayList<String> PresencyLine = new ArrayList<String>();



    int startNo = 5001, LastNo = 5040;
    int numberofstudents = LastNo - startNo +1;

    int columns = 35;
    String[][] headerMatrix = new String[1][8];
    String[][] matrix = new String[numberofstudents][columns];
    String Month[] = {"JANUARY", "FEBRUARY", "MARCH", "APRIL", "MAY", "JUNE", "JULY",
            "AUGUST", "SEPTEMBER", "OCTOBER", "NOVEMBER", "DECEMBER"};

    String NumericMonth[] = {"01", "02", "03", "04", "05", "06",
            "07", "08", "09", "10", "11", "12"};

    String StaticRow [] = {"Sr.No","Roll.No",
            "01", "02", "03", "04", "05", "06", "07", "08", "09", "10",
            "11", "12", "13", "14", "15", "16", "17", "18", "19", "20",
            "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31",
            "Total", "Percent" };

    void AttendanceReportPdf() throws FileNotFoundException,DocumentException{

        File myFile = new File("/sdcard/Monthly Report.pdf");

        OutputStream output = new FileOutputStream(myFile);
        Document document = new Document();
        document = new Document(PageSize.A4.rotate());

        PdfWriter.getInstance(document, output);
        document.open();
//            document.add(new Paragraph("Hello World!"));
        AddHeader(document);
        FillHeaderMmatrix();
        FillMatrix();
        matrixtoPdf(document);

        document.close();

    }


    void AddHeader(Document document) throws DocumentException
    {
        PdfPTable table = new PdfPTable(1);
        PdfPCell cell = new PdfPCell();
        cell.setBorder(PdfPCell.NO_BORDER);
        table.setWidthPercentage(100);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("N. R. SWAMY COLLEGE OF COMMERCE & ECONOMICS AND Smt. THIRUMALAI COLLEGE OF SCIENCE - WADALA"));
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        table.addCell(cell);

        table.setSpacingAfter(5f);
//        table1.setSpacingAfter(5f);

        document.add(table);
//        document.add(table1);
    }

    public void matrixtoPdf(Document document) throws DocumentException{

        float colwidth[] = {4,5,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,4,5};
        float colwidth1[] = {7,9,4,6,5,5,5,5};

        String HeaderRow[] = {"Teacher's Name", " ", "Subject", " ",
                "Class & Div"    ," ", "Month",   " " };

        PdfPTable table1 = new PdfPTable(colwidth1);
        table1.setWidthPercentage(100);

//        for(int i = 0; i < HeaderRow.length; i++){
        //           table1.addCell(HeaderRow[i]);
        //      }

        for(int i = 0; i < headerMatrix[0].length; i++){
            table1.addCell(headerMatrix[0][i]);
        }

        PdfPTable table = new PdfPTable(colwidth);
        table.setWidthPercentage(100);

        for(int i = 0; i < columns; i++){
            table.addCell(StaticRow[i]);
        }

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++)
            {
                table.addCell(matrix[i][j]);
            }
        }

        table.setHeaderRows(1);
        table1.setSpacingAfter(2f);
        document.add(table1);
        document.add(table);

    }

    void FillMatrix(){
        String presence = null, date = "";
        int k = 0 ;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++){
                matrix[i][j]=" ";
            }
        }

        for(int i = 0; i < numberofstudents; i++){
            matrix[i][0]= "" + (i+1);
        }

        for(int i = 0; i < numberofstudents; i++){
            matrix[i][1] = ""+ (startNo+i);
        }

        LoadFromHistory ("XI-C", "08");
        try {
            String Date, AttendenceData;
            for(int i = 0; i < PresencyLine.size(); i++){
                date = DateArray.get(i).substring(0,2);
                AttendenceData = PresencyLine.get(i);
                for(int j = 0; j < 40; j++){
                    matrix[j][Integer.parseInt(date)+1] = Character.toString(AttendenceData.charAt(j));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    void FillHeaderMmatrix(){
        String HeaderRow[] = {"Teacher's Name", " ", "Subject", " ",
                "Class & Div"    ," ", "Month",   " " };
        String month = "",  Div = "";
        String rollNo = "", attendanceLine = "";
        String TrName = CDD.teacher;
        String Subject = CDD.subject;

        for(int i = 0; i < HeaderRow.length; i++){
            headerMatrix[0][i] = HeaderRow[i];
        }

        LoadFromHistory ("XI-C", "08");
        try {
            String date = DateArray.get(0);
            month = date.substring(3, 5);
            for(int i = 0; i < Month.length; i++) {
                if (month.contains(NumericMonth[i])) month = Month[i];
            }

            for(int i = 0; i < Divisions.size(); i++) {
                Div = Divisions.get(i);
//                attendanceLine = PresencyLine.get(i);
//                Msg.show(Div);
//                Msg.show(attendanceLine);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        rollNo = RollNos.get(0);
        Msg.show(rollNo);
        Msg.show(month);
        headerMatrix[0][1] = TrName;
        headerMatrix[0][3] = Subject;
        headerMatrix[0][5] = Div;
        headerMatrix[0][7] = month;
    }

    public void LoadFromHistory(String div, String month ){

        String FileNameWithPath = "/sdcard/AttendanceData.atd";
        try {
            File FileToRead = new File(FileNameWithPath);
            FileInputStream FINS = new FileInputStream(FileToRead);
            BufferedReader bfrReader = new BufferedReader(new InputStreamReader(FINS));
            String AttendanceRecord = null;
            String temp[], Roll;
            DateArray.clear();
            Divisions.clear();
            PresencyLine.clear();
            RollNos.clear();

            while ((AttendanceRecord = bfrReader.readLine()) != null) {

                if (!AttendanceRecord.contains("#")) continue;
                temp = AttendanceRecord.split("#");
                if(temp[0].contains(month) &&  temp[1].contains(div)) {
                    DateArray.add(temp[0]);
                    Divisions.add(temp[1]);
                    RollNos.add(temp[2]);
                    PresencyLine.add(temp[3]);
                }
            }
        } catch (Exception e){

            Msg.show(e.getMessage());
            //    Toast.makeText(getBaseContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }

    }


}
