package org.example;

import org.example.Controller.pic_Controller;
import org.example.Until.HelpCreateFile;
import org.example.Until.SentEmail;

public class Main {
    public static void main(String[] args) {
        pic_Controller PC= new pic_Controller();

        String main_save_path = "F:\\A_ZKZD_2024\\yiche_pic\\";

        String txt_savePath = main_save_path+"txt\\";
        String pic_savePath = main_save_path+"pic\\";

        String brand_txt_save_path = txt_savePath+"brand_html\\";
        String brand_txt_name = "brand.txt";
        String brand_html_url = "https://photo.yiche.com/";
        HelpCreateFile.Method_Creat_folder(brand_txt_save_path);
//        1.下载保存品牌页面
//        PC.Method_1_DownBrand(brand_txt_save_path,brand_txt_name,brand_html_url);

//        2.解析品牌
//        PC.Method_2_AnalysisBrands(brand_txt_save_path, brand_txt_name);


//        3.下载各个品牌下的车型
        String mid_model_save_path = txt_savePath+"mid_model\\";
        String mid_model_save_name = "mid_model.txt";
        HelpCreateFile.Method_Creat_folder(mid_model_save_path);
//        PC.Method_3_Down_Model(mid_model_save_path, mid_model_save_name);

//        4.解析品牌的在售未售情况
//        PC.Method_4_Make_mid_model(mid_model_save_path, mid_model_save_name);

//        5.下载车型页面
        String model_page = txt_savePath+"model\\";
        String model_name = "_model.txt";
        HelpCreateFile.Method_Creat_folder(model_page);
//        PC.Method_5_Down_mid_page(model_page,model_name);

//        6.解析车型页面
//        PC.Method_6_Analysis_Model(model_page,model_name);


//        7.下载各个车型页面
        String version_list_save_path = txt_savePath+"versionList\\";
        String versionList_name = "_version.txt";
        HelpCreateFile.Method_Creat_folder(version_list_save_path);
//        for (int i = 0; i <3 ; i++) {
//            PC.Method_7_Down_ModelHTML(version_list_save_path,versionList_name);
//        }



//        8.解析版本列表获得版本
//        PC.Method_8_Analysis_VersionList(version_list_save_path,versionList_name);


//        9.下载具体版本页面
        String versionPage = txt_savePath+"versionHtml\\";
        String version_name = "_version.txt";
        HelpCreateFile.Method_Creat_folder(versionPage);

        for (int i = 0; i < 5; i++) {
            PC.Method_9_DownVersionHtml(versionPage,version_name);
        }


        SentEmail sentEmail = new SentEmail();
        sentEmail.Method_SentEmail(0,"程序完成了奥");

    }
}