package org.example.Controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.example.Dao.DaoFather;
import org.example.Entity.Model_pic;
import org.example.Entity.Version_pic;
import org.example.Entity.brand_pic;
import org.example.Entity.model_pic_mid;
import org.example.Until.MD5Until;
import org.example.Until.ReadUntil;
import org.example.Until.SaveUntil;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class pic_Controller {

    private SaveUntil saveUntil = new SaveUntil();
    private ReadUntil readUntil = new ReadUntil();

    public String Method_Down(String url) {
        try {
            Document document = Jsoup.parse(new URL(url).openStream(), "UTF-8", url);
            Thread.sleep(550);
            return document.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error";
        }
    }

    public void Method_1_DownBrand(String savePath, String fileName, String url) {
        String content = Method_Down(url);
        saveUntil.Method_SaveFile(savePath + fileName, content);
        System.out.println("完成品牌页面下载");
    }

    public void Method_2_AnalysisBrands(String savePath, String fileName) {
        DaoFather dao_brand_pic = new DaoFather(0, 0);
        String content = readUntil.Method_ReadFile(savePath + fileName);

        Document document = Jsoup.parse(content);

//        String url = "https://photo.yiche.com/";
        try {
//            Document document = Jsoup.parse(new URL(url).openStream(), "UTF-8", url);
            Elements Items1 = document.select(".car-tree-master").select(".car-tree-master-group");

            for (int i = 0; i < Items1.size(); i++) {

                Element item2 = Items1.get(i);

                String letter = item2.select(".car-tree-master-letter").text();

                System.out.println(letter);
                Elements Items3 = item2.select(".car-tree-master-info");

                for (Element Items4 : Items3) {
                    String brand_url = "https://photo.yiche.com" + Items4.select(".car-tree-master-item.ka").attr("href");
                    String brand_id = Items4.select(".car-tree-master-item.ka").attr("data-id");
                    String brand_name = Items4.select(".car-tree-master-item-name").text();
//                    System.out.println(brand_id + brand_name + "\n" + brand_url);
                    System.out.println(brand_name);
                    brand_pic bean = new brand_pic();
                    bean.setC_brand_id(brand_id);
                    bean.setC_brand_name(brand_name);
                    bean.setC_brand_url(brand_url);
                    bean.setC_letter(letter);
                    bean.setC_DownState("否");
                    bean.setC_DownTime("--");
                    dao_brand_pic.MethodInsert(bean);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void Method_3_Down_Model(String savePath_first,String saveName) {
        DaoFather dao_brand_pic = new DaoFather(0, 0);

        ArrayList<Object> beanList = dao_brand_pic.Method_Find();

        for (Object bean : beanList) {
            brand_pic brand_pic = (brand_pic) bean;

            String brand_id = brand_pic.getC_brand_id();
            String brand_url = brand_pic.getC_brand_url();

            int C_ID = brand_pic.getC_ID();
            String content = Method_Down(brand_url);
            System.out.println(brand_id);
            String DownState = brand_pic.getC_DownState();
            if (DownState.equals("否")){
                if (!content.equals("Error")){
                    saveUntil.Method_SaveFile(savePath_first + brand_id + saveName, content);
                    dao_brand_pic.Method_ChangeState(C_ID);
                }
            }
//            String brand_url_no_sale = brand_url+"?sortType=4&saleStatus=6";
//
//            String content_2 = Method_Down(brand_url_no_sale);
//
//            saveUntil.Method_SaveFile(savePath_noSale+brand_id+".txt", content_2);
        }
    }

    public void Method_4_Make_mid_model(String savePath,String name) {
        DaoFather dao_brand_pic = new DaoFather(0, 0);
        DaoFather dao_model_mid = new DaoFather(0, 1);
        ArrayList<Object> beanList = dao_brand_pic.Method_Find();

        for (Object bean : beanList) {
            brand_pic brand_pic = (brand_pic) bean;
            String brand_id = brand_pic.getC_brand_id();
            String brand_name = brand_pic.getC_brand_name();

            String content = readUntil.Method_ReadFile(savePath + brand_id + name);

            Document document = Jsoup.parse(content);

            Elements Items = document.select(".tuku-tab-container").select(".tuku-tab-menu");

            Elements Item_Sale = Items.select("a");

            for (Element item : Item_Sale) {
                String data_sale = item.attr("data-sale");
                String data_masterid = item.attr("data-masterid");
                String page_url = "https://photo.yiche.com" + item.attr("href");
                String type_page = item.text();
                System.out.println(type_page + "\t" + page_url + "\t" + data_masterid);

                model_pic_mid m = new model_pic_mid();

                m.setC_brand_id(data_masterid);
                m.setC_type(data_sale);
                m.setC_page_type(type_page);
                m.setC_DownState("否");
                m.setC_DownTime("--");
                m.setC_brand_url(page_url);
                m.setC_brand_name(brand_name);
                dao_model_mid.MethodInsert(m);
            }
        }
    }


    public void Method_5_Down_mid_page(String savePath, String savename) {
        DaoFather dao_model_mid = new DaoFather(0, 1);
        ArrayList<Object> beanList = dao_model_mid.Method_Find();

        for (Object bean : beanList) {
            model_pic_mid m = (model_pic_mid) bean;
            String url = m.getC_brand_url();
            String DownState = m.getC_DownState();
            String brand_id = m.getC_brand_id();
            String type = m.getC_type();
            int C_ID = m.getC_ID();
            if (DownState.equals("否")) {
                String content = Method_Down(url);
                if (!content.equals("Error")) {
                    saveUntil.Method_SaveFile(savePath + brand_id + "_"+type+savename, content);
                    dao_model_mid.Method_ChangeState(C_ID);
                    System.out.println(C_ID);
                }
            }
        }
    }

    public void Method_6_Analysis_Model(String path, String name) {
        DaoFather dao_model_mid = new DaoFather(0, 1);
        ArrayList<Object> beanList = dao_model_mid.Method_Find();
        DaoFather dao_model_pic = new DaoFather(0, 2);

        for (Object bean : beanList) {
            model_pic_mid m = (model_pic_mid) bean;
            String url = m.getC_brand_url();
            String DownState = m.getC_DownState();
            String brand_id = m.getC_brand_id();
            String type = m.getC_type();
            if (type.equals("1")) {
                String contrnt = readUntil.Method_ReadFile(path + brand_id + "_"+type+name);
                Document document = Jsoup.parse(contrnt);
                Elements Items = document.select(".img-item-content");
                for (Element Item2 : Items) {
                    String model_url = Item2.attr("href");
                    String model_name = Item2.select(".img-item-title").text();
                    String model_pic_num = Item2.select(".img-num").text();
                    String model_id = Item2.attr("data-id");
                    String model_first_pic = Item2.select(".img-item-pic").select(".lazyload").attr("data-original");
                    String model_price = Item2.select(".img-item-price").text();

                    Model_pic model_pic = new Model_pic();
                    model_pic.setC_model_first_pic(model_first_pic);
                    model_pic.setC_model_id(model_id);
                    model_pic.setC_model_pic_num(model_pic_num);
                    model_pic.setC_model_price(model_price);
                    model_pic.setC_model_type("在售");
                    model_pic.setC_model_first_pic(model_first_pic);
                    model_pic.setC_model_name(model_name);
                    model_pic.setC_model_url(model_url);
                    System.out.println(model_name);
                    model_pic.setC_brand_id(brand_id);
                    model_pic.setC_DownState("否");
                    dao_model_pic.MethodInsert(model_pic);
                }
            }
            if (type.equals("2")) {
                String contrnt = readUntil.Method_ReadFile(path + brand_id + "_"+type+name);
                Document document = Jsoup.parse(contrnt);
                Elements Items = document.select(".img-item-content");
                for (Element Item2 : Items) {
                    String model_url = Item2.attr("href");
                    String model_name = Item2.select(".img-item-title").text();
                    String model_pic_num = Item2.select(".img-num").text();
                    String model_id = Item2.attr("data-id");
                    String model_first_pic = Item2.select(".img-item-pic").select(".lazyload").attr("data-original");
                    String model_price = Item2.select(".img-item-price").text();

                    Model_pic model_pic = new Model_pic();
                    model_pic.setC_model_first_pic(model_first_pic);
                    model_pic.setC_model_id(model_id);
                    model_pic.setC_model_pic_num(model_pic_num);
                    model_pic.setC_model_price(model_price);
                    model_pic.setC_model_type("未售/停售");
                    model_pic.setC_model_first_pic(model_first_pic);
                    model_pic.setC_model_name(model_name);
                    model_pic.setC_model_url(model_url);
                    model_pic.setC_brand_id(brand_id);
                    model_pic.setC_DownState("否");
                    System.out.println(model_name);
                    dao_model_pic.MethodInsert(model_pic);
                }
            }
        }
    }


    public void Method_7_Down_ModelHTML(String savePath,String saveName) {
        DaoFather dao_model_pic = new DaoFather(0, 2);

        ArrayList<Object> BeanList = dao_model_pic.Method_Find();
        for (Object bean : BeanList) {
            Model_pic model_pic = (Model_pic) bean;
            String model_id = model_pic.getC_model_id();

//            https://photo.yiche.com/photo/photolist_7350_master_15/
            String url = "https://photo.yiche.com" + model_pic.getC_model_url();
            int C_ID = model_pic.getC_ID();
            String DownState = model_pic.getC_DownState();
            if (DownState.equals("否")) {
                String content = Method_Down(url);
                if (!content.equals("Error")){
                    saveUntil.Method_SaveFile(savePath + model_id + saveName, content);
                    System.out.println(C_ID);
                    dao_model_pic.Method_ChangeState(C_ID);
                }
            }
        }
    }

    public void Method_8_Analysis_VersionList(String savePath,String saveName){
        DaoFather dao_model_pic = new DaoFather(0, 2);
        DaoFather dao_version = new DaoFather(0,3);

        ArrayList<Object> BeanList = dao_model_pic.Method_Find();
        for (Object bean : BeanList) {
            Model_pic model_pic = (Model_pic) bean;
            String brand_id = model_pic.getC_brand_id();
            String model_id = model_pic.getC_model_id();
            String content = readUntil.Method_ReadFile(savePath + model_id + saveName);
            Document document = Jsoup.parse(content);
            Elements Items = document.select(".tags-select");
            System.out.println(Items.size());
            for (int i = 0; i < Items.size(); i++) {
                Element Item = Items.get(i);
                String type = Item.select(".tags-select-title").text();
                System.out.println(type);
                if (!type.equals("在售")){
                    Elements versionList = Item.select(".tags-select-list").select("a");
                    for (int j = 0; j < versionList.size(); j++) {
                        Element one_version = versionList.get(j);

                        String versin_url = "https://photo.yiche.com" + one_version.attr("href");
                        String version_name = one_version.text();
                        String model_id1 = one_version.attr("point-refid");

                        String version_id = versin_url.substring(versin_url.indexOf("_style_") + 7, versin_url.indexOf("_master_"));
                        System.out.println(version_id);
                        Version_pic version_pic = new Version_pic();
                        version_pic.setC_brand_id(brand_id);
                        version_pic.setC_version_id(version_id);
                        version_pic.setC_version_name(version_name);
                        version_pic.setC_version_type(type);
                        version_pic.setC_version_url(versin_url);
                        version_pic.setC_model_id(model_id1);
                        version_pic.setC_DownState("否");
                        version_pic.setC_DownTime("--");
                        dao_version.MethodInsert(version_pic);

                    }
                }
            }
        }
    }


    public void Method_9_DownVersionHtml(String savePath,String saveName){
        DaoFather dao_version = new DaoFather(0,3);
        ArrayList<Object> BeanList = dao_version.Method_Find();

        for (Object bean:BeanList){
            Version_pic version_pic = (Version_pic) bean;
            String version_id  = version_pic.getC_version_id();
            String DownState  = version_pic.getC_DownState();
            String url = version_pic.getC_version_url();
            int C_ID = version_pic.getC_ID();
            if (DownState.equals("否")){
                String content  = Method_Down(url);
                if (!content.equals("Error")){
                    saveUntil.Method_SaveFile(savePath+version_id+saveName,content);
                    dao_version.Method_ChangeState(C_ID);
                    System.out.println(C_ID);
                }
            }
        }
    }


}
