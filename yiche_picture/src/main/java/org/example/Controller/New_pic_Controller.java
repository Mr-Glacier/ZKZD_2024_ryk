package org.example.Controller;

import org.example.Dao.DaoFather;
import org.example.Entity.Bean_version_pic;
import org.example.Entity.Bean_version_pic_state;
import org.example.Until.ReadUntil;
import org.example.Until.SaveUntil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URL;
import java.util.ArrayList;

public class New_pic_Controller {
    private ReadUntil readUntil = new ReadUntil();
    private SaveUntil saveUntil = new SaveUntil();

    public String Method_DownHTML(String url) {
        try {
            Document document = Jsoup.parse(new URL(url).openStream(), "UTF-8", url);
            Thread.sleep(860);
            return document.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error";
        }
    }

    public void Method_1_DownVersionPicTag(String savePath,String saveName,String main_url){
        DaoFather dao_version = new DaoFather(0,0);
        ArrayList<Object> BeanList = dao_version.Method_Find();
        for (Object bean:BeanList){
            Bean_version_pic bean_version_pic = (Bean_version_pic)bean;
            int C_ID = bean_version_pic.getC_ID();
            String versionID = bean_version_pic.getC_version_id();
            String DownState = bean_version_pic.getC_DownState();
            String url = main_url + bean_version_pic.getC_allSpell()+"/m"+versionID+"/";
            System.out.println(url);
            if (DownState.equals("否")){
                String content= Method_DownHTML(url);
                if(!content.equals("Error")){
                    saveUntil.Method_SaveFile(savePath+versionID+saveName, content);
                    dao_version.Method_ChangeState(C_ID);
                    System.out.println(C_ID);
                }
            }
        }
    }


    public void Method_2_Analysis(String savePath,String saveName){
        DaoFather dao_version = new DaoFather(0,0);
        DaoFather dao_state = new DaoFather(0,1);

        ArrayList<Object> BeanList = dao_version.Method_Find();
        for (Object bean:BeanList) {
            Bean_version_pic bean_version_pic = (Bean_version_pic) bean;
            int C_ID = bean_version_pic.getC_ID();
            String versionID = bean_version_pic.getC_version_id();

            String content  = readUntil.Method_ReadFile(savePath+versionID+saveName);

            Document document = Jsoup.parse(content);
            Elements Item = document.select(".more");

            String tage = Item.select(".text").text().replace(">>", "");
            int pic_count = 0;
            String url = "-" ;
            if (!tage.contains("无实拍图")){
                pic_count  = Integer.parseInt(tage.replace("共", "").replace("张实拍图", ""));
                url = "https:"+document.select(".big-img").select(".more").attr("href");
            }
            Bean_version_pic_state bean_version_pic_state = new Bean_version_pic_state();
            bean_version_pic_state.setC_DownState("否");
            bean_version_pic_state.setC_pic_count(pic_count);
            bean_version_pic_state.setC_pic_url(url);
            bean_version_pic_state.setC_tage(tage);
            bean_version_pic_state.setC_version_id(versionID);
            dao_state.Method_Insert(bean_version_pic_state);
            System.out.println(url);
        }
    }
    public void Method_3_Down_detail_versionPage(String savePath,String saveName){
        DaoFather dao_state = new DaoFather(0,1);
        ArrayList<Object> BeanList = dao_state.Method_Find();

        for (Object bean:BeanList){
            Bean_version_pic_state bean_version_pic_state = (Bean_version_pic_state) bean;

            int count  = bean_version_pic_state.getC_pic_count();
            String DownState  = bean_version_pic_state.getC_DownState();
            String versionID = bean_version_pic_state.getC_version_id();
            String version_url = bean_version_pic_state.getC_pic_url();
            int C_ID = bean_version_pic_state.getC_ID();
            if (count!=0){
                if (DownState.equals("否")){
                    System.out.println(C_ID);
                    String  content  = Method_DownHTML(version_url);
                    if (!content.equals("Error")){
                        saveUntil.Method_SaveFile(savePath+versionID+saveName,content);
                        dao_state.Method_ChangeState(C_ID);
                        System.out.println("下载完成->"+versionID);
                    }
                }
            }
        }
    }

    public void Method_4_Analysis_detail_versionPage(String savePath,String saveName){
        DaoFather daoFather = new DaoFather(0,1);
        ArrayList<Object> BeanList = daoFather.Method_Find();
        for (Object bean:BeanList){
            Bean_version_pic_state bean_version_pic_state = (Bean_version_pic_state) bean;
            String versionId = bean_version_pic_state.getC_version_id();

            String content  = readUntil.Method_ReadFile(savePath+versionId+saveName);
            Document document = Jsoup.parse(content);
            Elements Items = document.select(".img-list-main");

            System.out.println(Items.size());

            for (int i = 0; i < Items.size(); i++) {

                Element Item =  Items.get(i);

                Elements Item2 = Item.select(".tuku-sub-tab-container");
                String  title = Item2.select(".tuku-sub-tab-title").text();
                System.out.println(title);

                if(title.equals("实拍图")){
                    Elements Items3 = Item.select(".tuku-sub-tab-menu").select("a");
                    System.out.println(Items3.size());
                    for (Element Item4 :Items3){
                        String url = "https://photo.yiche.com"+Item4.attr("href");
                        String type = Item4.text();
                        System.out.println(url);
                        System.out.println(type);
                    }
                }else {
                    Elements Item3 = Item.select(".tuku-sub-tab-dest");
                    String url= "https://photo.yiche.com"+Item3.select("a").attr("href");
                    System.out.println(url);
                }


            }
        }
    }
}
