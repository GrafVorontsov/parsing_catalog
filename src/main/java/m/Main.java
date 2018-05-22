package m;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

public class Main {
    private static Connection mysqlConnection = new MysqlHelper().getConnection();

    public static void main(String[] args) throws IOException, SQLException {
        Auto amortizator;
        Document doc;
        String tmp = "";
        String car_tmp = "";
        String marka_name;
        String model_name;
        String car_name;
        int marka_id = 0;
        int model_id = 0;
        int car_id = 0;
        ArrayList<Auto> amortizators = new ArrayList<>();
        LinkedHashSet<Auto> autoSet = new LinkedHashSet<>(amortizators);
        String automobile = null;
        String correctionUpdate = null;

        java.sql.PreparedStatement preparedStmt_marka;
        java.sql.PreparedStatement preparedStmt_model;
        java.sql.PreparedStatement preparedStmt_car;
        java.sql.PreparedStatement preparedStmt_amortizator;

        String query_marka = "insert into marka (marka_id, marka_name)"
                + " values (?, ?)";

        String query_model = "insert into models (model_id, model_name, marka_id)"
                + " values (?, ?, ?)";

        String query_car = "insert into cars (car_id, car_name, model_id, model_name, marka_id)"
                + " values (?, ?, ?, ?, ?)";

        String query_amortizator = "insert into amortizators (marka_id, model_id, car_id, marka_name, model_name, car_name, correction, year, range_type, install, art_number, info, info_lowering, jpg, pdf, search)"
                + " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        //Формируем список нужных файлов
        File folder = new File("E:\\1\\finder\\finder.koni.ie\\com\\en\\catalog");
        ArrayList<File> fileLinkedList = new ArrayList<>();
        getFiles(folder, fileLinkedList);

        //парсим каждый файл по очереди
        for (File input1 : fileLinkedList) {
            String[] arrayFromString = input1.toString().split("\\\\");
            marka_name = arrayFromString[7];
            model_name = arrayFromString[8];

            if (marka_name.equals(tmp)) {
                //не добавляем марку авто
                //добавляем модель
                ++model_id;

                preparedStmt_model = mysqlConnection.prepareStatement(query_model);
                preparedStmt_model.setInt (1, model_id);
                preparedStmt_model.setString (2, model_name);
                preparedStmt_model.setInt (3, marka_id);
                preparedStmt_model.execute();

            } else {
                // Добавление марок авто + Моделей в базу данных
                ++marka_id;
                ++model_id;

                preparedStmt_marka = mysqlConnection.prepareStatement(query_marka);
                preparedStmt_marka.setInt (1, marka_id);
                preparedStmt_marka.setString (2, marka_name);
                preparedStmt_marka.execute();

                preparedStmt_model = mysqlConnection.prepareStatement(query_model);
                preparedStmt_model.setInt (1, model_id);
                preparedStmt_model.setString (2, model_name);
                preparedStmt_model.setInt (3, marka_id);
                preparedStmt_model.execute();

                tmp = marka_name; //перезаписываем переменную tmp для сравнения
            }

            doc = Jsoup.parse(input1, "UTF-8");

            Elements articles = doc.select("tr.article");

            for (Element article : articles) {
                car_name = article.select("td.first > span.name").text();
                String correction = article.select("td.first").text().replace(car_name, "").trim();
                String year = article.select("td.year.number").text();
                String range_type = article.select("td.range").text();
                String install = article.select("td:matches(^Front$), td:matches(^Rear$), td:matches(^Kit$)").text();
                String art_number = article.select("td.artnr ").text();

                //получаем данные только из div.content, тут находится всё info
                Elements info = article.select("td.info > div.icons > div.with_tooltip div.content");
                //получаем данные h3, p, .icon в виде (теги + текст)
                Elements info_Notes = info.select("h3:not(:matches(^PDF),:matches(^Lowering)), p:not(:has(img), :matches(^Front:), :matches(^Rear:))");
                //получаем данные Lowering
                Elements info_Lowering = info.select("h3:matches(^Lowering) ,p:matches(^Front:), p:matches(^Rear:)");

                //собираем всё в коллекции
                ArrayList<String> notes_all = new ArrayList<>();
                ArrayList<String> notes_Lowering = new ArrayList<>();
                String note;

                for (Element n: info_Notes) {
                    note = n.toString();
                    notes_all.add(note);
                }

                for (Element n: info_Lowering) {
                    note = n.toString();
                    notes_Lowering.add(note);
                }

                //вытаскиваем названия изображений
                String imgJpg = article.select("a").attr("onclick");
                int str1 = imgJpg.lastIndexOf("http://finder.koni.ie/com/en/popup?img=/img/products/");
                int str2 = imgJpg.indexOf("*/, 'popup',");
                String jpg = "";
                if (!imgJpg.isEmpty()) {
                    jpg = imgJpg.substring(str1, str2).replace("http://finder.koni.ie/com/en/popup?img=/img/products/","");
                }


                //вытаскиваем названия pdf
                Elements filePdf = article.getElementsByAttributeValueEnding("tppabs", "pdf");
                ArrayList<String> pdf;
                HashSet<String> hash_pdf = new HashSet<>();
                String filename_pdf;
                String prev_name = "";

                for (Element f: filePdf) {
                    if(f.hasAttr("tppabs")){
                        filename_pdf = f.attr("tppabs").replace("http://finder.koni.ie/files/pdf/", "");
                        if (!filename_pdf.equals(prev_name)){
                            hash_pdf.add(filename_pdf + "\n");
                            prev_name = filename_pdf;
                        }
                    }
                }
                pdf = new ArrayList<>(hash_pdf);

                if (!correction.isEmpty()){         //если correction не пустое
                    correctionUpdate = correction;  //сохраняем переменную correction
                }else{                              // если пустое то надо добавить
                    assert automobile != null;
                    if(automobile.equals(car_name)){ //automobile == car_name
                        correction = correctionUpdate;
                    }
                }

                if (car_name.equals(car_tmp) || car_name.isEmpty()) {

                    car_name = automobile; // если пустое то надо добавить
                    correction = correctionUpdate;

                    //создание объекта
                    amortizator = new Auto(marka_id, model_id, car_id, marka_name, model_name, car_name, correction, year, range_type, install, art_number, notes_all, notes_Lowering, jpg, pdf);

                    //собираем всё в массив
                    amortizators.add(amortizator);

                }else{
                    ++car_id;

                    preparedStmt_car = mysqlConnection.prepareStatement(query_car);
                    preparedStmt_car.setInt (1, car_id);
                    preparedStmt_car.setString (2, car_name);
                    preparedStmt_car.setInt (3, model_id);
                    preparedStmt_car.setString (4, model_name);
                    preparedStmt_car.setInt (5, marka_id);
                    preparedStmt_car.execute();

                    automobile = car_name;          //сохраняем car_name
                    correctionUpdate = correction;  //сохраняем переменную correction

                    //создание объекта
                    amortizator = new Auto(marka_id, model_id, car_id, marka_name, model_name, car_name, correction, year, range_type, install, art_number, notes_all, notes_Lowering, jpg, pdf);

                    //собираем всё в массив
                    amortizators.add(amortizator);

                    car_tmp = car_name; //перезаписываем переменную car_tmp для сравнения
                }
            }
        }

        //пробегаем по массиву и подставляем недостающие значения
        String getYear = null;
        String getRange = null;

        for (Auto a : amortizators) {

            if (!a.getYear().isEmpty()) {
                getYear = a.getYear();
            } else {
                a.setYear(getYear);
            }

            if (!a.getRange_type().isEmpty()) {
                getRange = a.getRange_type();
            } else {
                a.setRange_type(getRange);
            }

            //делаем уникальный массив данных
            autoSet.add(a);
        }

        //вывод на консоль
        //заливка в БД
        int auto_marka_id;
        int auto_model_id;
        int auto_car_id;
        String auto_marka_name;
        String auto_model_name;
        String auto_car_name;
        String auto_correction;
        String auto_year;
        String auto_range_type;
        String auto_install;
        String auto_art_number;
        ArrayList<String> auto_info;
        ArrayList<String> auto_info_lowering;
        String auto_jpg;
        ArrayList<String> auto_pdf;
        String search;

        for (Auto autoS : autoSet) {

            auto_marka_id = autoS.getMarka_id();
            auto_model_id = autoS.getModel_id();
            auto_car_id = autoS.getCar_id();
            auto_marka_name = autoS.getMarka_name();
            auto_model_name = autoS.getModel_name();
            auto_car_name = autoS.getCar_name();
            auto_correction = autoS.getCorrection();
            auto_year = autoS.getYear();
            auto_range_type = autoS.getRange_type();
            auto_install = autoS.getInstall();
            auto_art_number = autoS.getArt_number();
            auto_info = autoS.getInfo();
            auto_info_lowering = autoS.getInfo_lowering();
            auto_jpg = autoS.getJpg();
            auto_pdf = autoS.getPdf();
            search = auto_art_number.replaceAll("\\W", "");

            preparedStmt_amortizator = mysqlConnection.prepareStatement(query_amortizator);

            preparedStmt_amortizator.setInt (1, auto_marka_id);
            preparedStmt_amortizator.setInt (2, auto_model_id);
            preparedStmt_amortizator.setInt (3, auto_car_id);
            preparedStmt_amortizator.setString (4, auto_marka_name);
            preparedStmt_amortizator.setString (5, auto_model_name);
            preparedStmt_amortizator.setString (6, auto_car_name);
            preparedStmt_amortizator.setString (7, auto_correction);
            preparedStmt_amortizator.setString (8, auto_year);
            preparedStmt_amortizator.setString (9, auto_range_type);
            preparedStmt_amortizator.setString (10, auto_install);
            preparedStmt_amortizator.setString (11, auto_art_number);
            preparedStmt_amortizator.setString (12, getInfo(auto_info));
            preparedStmt_amortizator.setString (13, getInfo(auto_info_lowering));
            preparedStmt_amortizator.setString (14, auto_jpg);
            preparedStmt_amortizator.setString (15, getInfo(auto_pdf));
            preparedStmt_amortizator.setString (16, search);

            preparedStmt_amortizator.addBatch();
            preparedStmt_amortizator.executeBatch();
        }
    }


    private static ArrayList<File> getFiles(File folder, ArrayList<File> fileLinkedList) throws
            NullPointerException {
        for (File f : Objects.requireNonNull(folder.listFiles())) {
            if (f.isDirectory()) {
                //марка
                File folder1 = new File(folder, f.getName());
                for (File fNext : Objects.requireNonNull(folder1.listFiles())) {
                    if (fNext.isDirectory()) {
                        //модель
                        File folder2 = new File(folder1, fNext.getName());
                        for (File fNext2 : Objects.requireNonNull(folder2.listFiles())) {
                            if (fNext2.isFile() && fNext2.getName().equals("all.htm")) {
                                fileLinkedList.add(fNext2);
                            }
                        }
                    }
                }
            }
        }
        return fileLinkedList;
    }

    private static String getInfo(ArrayList<String> info) {

        StringBuilder buffer = new StringBuilder();
        boolean processedFirst = false;
        String firstParam, secondParam = null;

        for (String record : info) {
            if (processedFirst)
                buffer.append(";");
            buffer.append(record);
            processedFirst = true;
        }
        firstParam = buffer.toString();
        return firstParam;
    }
}