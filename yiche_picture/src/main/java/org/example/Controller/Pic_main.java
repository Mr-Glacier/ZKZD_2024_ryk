package org.example.Controller;

import org.example.Until.HelpCreateFile;

public class Pic_main {

    public static void main(String[] args) {

        New_pic_Controller NP = new New_pic_Controller();
        String main_save_path = "F:\\A_ZKZD_2024\\yiche_pic\\";

        String txt_savePath = main_save_path+"txt\\";
        String pic_savePath = main_save_path+"pic\\";

        String first_txt_save_path = txt_savePath+"versionTage_html\\";
        String first_txt_name = "_tag.txt";
        String html_url = "https://car.yiche.com/";
        HelpCreateFile.Method_Creat_folder(first_txt_save_path);

//        下载
//        NP.Method_1_DownVersionPicTag(first_txt_save_path,first_txt_name,html_url);

//        NP.Method_2_Analysis(first_txt_save_path,first_txt_name);

//        3.下载具有图片的版本页面'
        String second_path = "F:\\A_ZKZD_2024\\yiche_pic\\txt\\version_detail_html\\";
        String second_naem = "_detail.txt";
        NP.Method_3_Down_detail_versionPage(second_path,second_naem);


//        4.解析具体的东西
//        NP.Method_4_Analysis_detail_versionPage(second_path,second_naem);

//        5.下载详情图片页面

    }
}
