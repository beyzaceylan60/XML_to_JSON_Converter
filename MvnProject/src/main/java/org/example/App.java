package org.example;
//JSON formatı için gerekenler
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
//Dosya işlemleri için gerekenler

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
//SAX API için gerekenler

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;
//Nesneleri tutmak için oluşturulan diziler

import java.util.ArrayList;
import java.util.List;

/*
XML to Java to JSON
 */


//Handler sınıfımızı oluşturduk.
public class App extends DefaultHandler{

    //Öznitelikleri: eleman, arrayList, BarajClass sınıfına ait bir nesne
    private String currentElement;
    private List<BarajClass> dataList;
    private BarajClass barajClass;

    @Override
    public void startDocument() {
        dataList = new ArrayList<>();
        //dataList'in içinde XML'den çevrilen dosyalar tutulur. Burada dataList'i oluşturduk.
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes){
        /*
        Buradaki metoda verilerin arasinda oldugu bloklarin ismi yazilir.
        BarajClass sinifina ait nesne olusturulur.
         */
        currentElement = qName;
        if ("dt".equalsIgnoreCase(currentElement)) {
            barajClass = new BarajClass();
        }

    }

    @Override
    public void characters(char[] ch, int start, int length){
        /*
        Burada bloklarin arasindaki veriler okunur string haline getirilir.
        olusturulan nesnenin oznitelikleri okunan verilere gore degistirilir.
         */
        String value = new String(ch, start, length).trim();
        if ("tarih".equalsIgnoreCase(currentElement)) {
            barajClass.setTarih(value);
        } else if ("havza".equalsIgnoreCase(currentElement)) {
            barajClass.setHavza(value);
        } else if ("baraj".equalsIgnoreCase(currentElement)) {
            barajClass.setBaraj(value);
        } else if ("aktif_doluluk_.28..25..29.".equalsIgnoreCase(currentElement)) {
            barajClass.setAktifDoluluk(value);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        /*
        Buradaki metot ile bitis blogu gorulunce olusturulan dataList'e nesnemiz eklenir.
         */
        if ("dt".equals(qName)) {
            getDataList().add(barajClass);
            barajClass = null;
        }
        currentElement = "";
    }

    @Override
    public void endDocument(){
        /*
         XML verilerinin islendigi nokta, burada dataList listesinde toplanmıs olacaktır.
         FileWriter kullanılarak bir dosya olusturuyoruz.
         Burada dataList'i Json haline cevirerek olusturdugumuz dosyaya yaziyoruz.
         Calisip calismadıgından emin olmak icin kullaniciya geri donus mesaji veriyoruz
        */
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String jsonData = objectMapper.writeValueAsString(getDataList());
            FileWriter fileWriter = new FileWriter("baraj.json");
            fileWriter.write(jsonData);
            fileWriter.close();
            System.out.println("JSON dosyası başarıyla oluşturuldu.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<BarajClass> getDataList() {
        return dataList;
    }
    public static void main(String[] args) {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        try {
            /*

            Bu kisim XML'i ayristirmaya yarar.

             */
            //Dosya yolunu degiskene atıyoruz
            String filePath = "C:/Users/sarid/Desktop/aktif-doluluk-24072023 (1).xml";

            //dosya yolunu kaydettiğimiz stringi File yapicisi ile kullanarak nesne oluşturuyoruz.
            File inputFile = new File(filePath);

            //SAXParserFactory nesnesi olusturuyoruz
            SAXParserFactory factory = SAXParserFactory.newInstance();

            //SAXParserFactory'den SAXParser olusturuyoruz.
            SAXParser saxParser = factory.newSAXParser();

            //Handler objesi olusturuyoruz.
            App app = new App();

            //XML dosyasini parse metoduyla ayristiriyoruz.
            saxParser.parse(inputFile, app);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
    class BarajClass {
    /*
    BarajClass veri setlerimizdeki verilere ait ozniteliklerin kontrolunde kullanilir.
    Farkli bir veri setiyle ugrasildigi zaman buraya farkli bir class eklenebilir.
    Veya Class degistirilebilir.
     */
        private String tarih;
        private String havza;
        private String baraj;
        private String aktifDoluluk;

        public BarajClass() {
        }

        public BarajClass(String tarih, String havza, String baraj, String aktifDoluluk) {
            //Elle veri girilmesinde yardimci olacaktir. Burada kullanilmiyor.
            setTarih(tarih);
            setHavza(havza);
            setBaraj(baraj);
            setAktifDoluluk(aktifDoluluk);
        }

        public String getAktifDoluluk() {
            return aktifDoluluk;
        }

        public String getBaraj() {
            return baraj;
        }

        public String getHavza() {
            return havza;
        }

        public String getTarih() {
            return tarih;
        }

        public void setAktifDoluluk(String aktifDoluluk) {
            this.aktifDoluluk = aktifDoluluk;
        }

        public void setBaraj(String baraj) {
            this.baraj = baraj;
        }

        public void setHavza(String havza) {
            this.havza = havza;
        }

        public void setTarih(String tarih) {
            this.tarih = tarih;
        }
    }
