package org.en.tealEye.printing.service;

import de.liga.dart.gruppen.service.GruppenService;
import de.liga.dart.common.service.ServiceFactory;
import de.liga.dart.model.Spielort;
import de.liga.dart.model.Ligagruppe;
import de.liga.dart.model.Ligateam;
import de.liga.dart.spielort.service.SpielortService;
import de.liga.util.StringUtils;

import javax.swing.*;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.Attribute;
import javax.print.attribute.standard.MediaSize;
import javax.print.attribute.standard.MediaSizeName;
import java.awt.*;
import java.awt.print.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Set;
import java.util.Vector;

import org.en.tealEye.framework.BeanTableModel;
import org.en.tealEye.printing.controller.GenericThread;

/**
 * Created by IntelliJ IDEA.
 * User: Stephan Pudras
 * Date: 14.09.2009
 * Time: 01:24:41
 * To change this template use File | Settings | File Templates.
 */
public class EnvelopePrintService {

    private Object parentObject;
    final Vector<Vector<String[]>> labelStrings = new Vector<Vector<String[]>>();
    private Vector<ImageIcon> imageIconVec = new Vector<ImageIcon>();

    private int pages;
    private boolean withGraphic;
    private boolean withSender;
    private boolean senderDefault;
    private boolean orderByTable;
    private int format;
    private int orientation;

    private double xAxisAddress;
    private double yAxisAddress;

    private double xAxisSender;
    private double yAxisSender;

    private double xAxisGraphic;
    private double yAxisGraphic;

    private int pagesToPrint;
    private Attribute mediaFormat;
    private int mediaSize;

    private Book book = new Book();
    private PrinterJob pj;
    private PageFormat pf;

    public EnvelopePrintService(Object parentObject, GenericThread tf, int pages) {
        this.parentObject = parentObject;
        this.pages = pages;
        this.mediaSize = mediaSize;


        startPrinterJob();
        aquireData();
        fillBook();
        drawEnvelope();
        getStringArray();
        getPaperWidth();
        drawEnvelope();
        tf.setDone();
        //startPrinting();

    }

    private void startPrinting() {
        pj.setPageable(book);
        pj.setJobName("My book");
    if (pj.printDialog()) {
      try {
        pj.print();
      } catch (PrinterException e) {
        System.out.println(e);
      }
    }
    }

    private void fillBook() {

    }


    private void initGraphics2D(){
        BufferedImage img = null;

        Graphics2D g = img.createGraphics();
    }

    private void drawEnvelope(){
            BufferedImage img;
            for (Vector<String[]> group : labelStrings) {
                for (int y = 0; y < pages; y++) {
                    for (String[] g : group) {
                        img = new BufferedImage((int) (getPaperWidth().getImageableWidth()),(int) (getPaperWidth().getImageableHeight()),BufferedImage.TYPE_BYTE_INDEXED);
                        Graphics2D g2 = img.createGraphics();
                        EnvelopePaint ep = new EnvelopePaint(g2, g, (int)getPaperWidth().getImageableWidth(), (int)getPaperWidth().getImageableHeight());
                        ImageIcon icon = new ImageIcon(img);
                        imageIconVec.add(icon);
                    }
                }
            }
    }

    private void startPrinterJob(){
        pj = PrinterJob.getPrinterJob();
    }

    

    private PageFormat getPaperWidth(){
        PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
        mediaSize = 6;
        aset.add(setMediaSize());
        pf = pj.getPageFormat(aset);
        pf.setOrientation(PageFormat.LANDSCAPE);
        double[] size = new double[2];
            size[0] = pf.getImageableWidth();
            size[1] = pf.getImageableHeight();
        return pf;
    }

    private MediaSizeName setMediaSize(){
        MediaSizeName ms = null;
        switch(mediaSize){
            case 0: ms = MediaSizeName.ISO_C3;break;
            case 1: ms = MediaSizeName.ISO_B4;break;
            case 2: ms = MediaSizeName.ISO_C4;break;
            case 3: ms = MediaSizeName.ISO_B5;break;
            case 4: ms = MediaSizeName.ISO_C5;break;
            case 5: ms = MediaSizeName.ISO_C6;break;
            case 6: ms = MediaSizeName.ISO_DESIGNATED_LONG;break;
            case 7: ms = MediaSizeName.ISO_B6;break;
        }
         return ms;

    }

    public void getStringArray(){
            for (Vector<String[]> group : labelStrings) {
                for (int y = 0; y < pages; y++) {
                    for (String[] g : group) {
                        book.append(new EnvelopeBook(g),pf,1);
                    }
                }
            }
    }


    private void aquireData(){
        ServiceFactory.runAsTransaction(new Runnable() {
            public void run() {
        JTable table = (JTable)parentObject;
        int[] rowSelectionCount = table.getSelectedRows();
        if(rowSelectionCount.length<1){
            rowSelectionCount = new int[table.getRowCount()];
            for(int i = 0; i<table.getRowCount();i++){
                rowSelectionCount[i] = i;
            }
        }
        GruppenService gs = ServiceFactory.get(GruppenService.class);
        Spielort spielort = null;
        String[] rowString;

        ArrayList<Long> ortIdx = new ArrayList<Long>();
        if(table.getName().equals("Table_Ligagruppe")){
            for(int rowIndex:rowSelectionCount){
                Object obj = ((BeanTableModel) table.getModel()).getObject(rowIndex);
                Ligagruppe gruppe = ((Ligagruppe) obj);
                ArrayList<Long> ortIds = new ArrayList<Long>();
                Vector<String[]> groupVec = new Vector<String[]>();
            for(int teamIndex = 1; teamIndex <= 8; teamIndex++){

                    Ligateam lt;
                    try {
                        lt = gruppe.findSpiel(gs.findSpieleInGruppe(gruppe), teamIndex).getLigateam();
                        
                    } catch (NullPointerException e) {
                        lt = null;
                    }
                    if (lt != null) {
                        spielort = ServiceFactory.get(SpielortService.class)
                                .findSpielortById(lt.getSpielort().getSpielortId());
                    }
                    if(!(lt==null)){
                        if(!ortIds.contains(spielort.getSpielortId())){
                            rowString = new String[5];
                            String lines[] = StringUtils.wordWrap(spielort.getSpielortName(), 25);
                            rowString[0] = lines[0];
                            if(lines.length > 1) {
                                rowString[1] = lines[1];
                            } else {
                                rowString[1] = "";
                            }
                            rowString[2] = spielort.getStrasse();

                            rowString[3] = spielort.getPlzUndOrt();
                            rowString[4] = "";
                            groupVec.addElement(rowString);
                            ortIds.add(spielort.getSpielortId());
                            ortIdx.add(spielort.getSpielortId());
                            //System.out.println(rowString[0]);
                        }else{
                            Set<Ligateam> teams = spielort.getLigateamsInGruppe(gruppe);
                            int idx = ortIdx.lastIndexOf(spielort.getSpielortId());

                            //String[] entries = labelStrings.get(idx);
                            ortIds.add(spielort.getSpielortId());
                            int tmpAnzahl = 0;
                            for(Long anzahl : ortIds){
                                if(anzahl == spielort.getSpielortId())
                                    tmpAnzahl++;
                            }
                            //entries[4] = String.valueOf(tmpAnzahl);
                        }
                    }
                }
                    labelStrings.add(groupVec);
            }
        }
    }
        });
    }

    public ImageIcon getGraphic(int page) {
        return imageIconVec.get(page);
    }


}
