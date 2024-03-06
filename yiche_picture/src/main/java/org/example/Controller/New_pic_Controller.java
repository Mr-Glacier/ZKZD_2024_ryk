package org.example.Controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.example.Dao.DaoFather;
import org.example.Entity.*;
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
import java.util.Date;

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

    public void Method_1_DownVersionPicTag(String savePath, String saveName, String main_url) {
        DaoFather dao_version = new DaoFather(0, 0);
        ArrayList<Object> BeanList = dao_version.Method_Find();
        for (Object bean : BeanList) {
            Bean_version_pic bean_version_pic = (Bean_version_pic) bean;
            int C_ID = bean_version_pic.getC_ID();
            String versionID = bean_version_pic.getC_version_id();
            String DownState = bean_version_pic.getC_DownState();
            String url = main_url + bean_version_pic.getC_allSpell() + "/m" + versionID + "/";
            System.out.println(url);
            if (DownState.equals("否")) {
                String content = Method_DownHTML(url);
                if (!content.equals("Error")) {
                    saveUntil.Method_SaveFile(savePath + versionID + saveName, content);
                    dao_version.Method_ChangeState(C_ID);
                    System.out.println(C_ID);
                }
            }
        }
    }


    public void Method_2_Analysis(String savePath, String saveName) {
        DaoFather dao_version = new DaoFather(0, 0);
        DaoFather dao_state = new DaoFather(0, 1);

        ArrayList<Object> BeanList = dao_version.Method_Find();
        for (Object bean : BeanList) {
            Bean_version_pic bean_version_pic = (Bean_version_pic) bean;
            int C_ID = bean_version_pic.getC_ID();
            String versionID = bean_version_pic.getC_version_id();

            String content = readUntil.Method_ReadFile(savePath + versionID + saveName);

            Document document = Jsoup.parse(content);
            Elements Item = document.select(".more");

            String tage = Item.select(".text").text().replace(">>", "");
            int pic_count = 0;
            String url = "-";
            if (!tage.contains("无实拍图")) {
                pic_count = Integer.parseInt(tage.replace("共", "").replace("张实拍图", ""));
                url = "https:" + document.select(".big-img").select(".more").attr("href");
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

    public void Method_3_Down_detail_versionPage(String savePath, String saveName) {
        DaoFather dao_state = new DaoFather(0, 1);
        ArrayList<Object> BeanList = dao_state.Method_Find();

        for (Object bean : BeanList) {
            Bean_version_pic_state bean_version_pic_state = (Bean_version_pic_state) bean;

            int count = bean_version_pic_state.getC_pic_count();
            String DownState = bean_version_pic_state.getC_DownState();
            String versionID = bean_version_pic_state.getC_version_id();
            String version_url = bean_version_pic_state.getC_pic_url();
            int C_ID = bean_version_pic_state.getC_ID();
            if (count != 0) {
                if (DownState.equals("否")) {
                    System.out.println(C_ID);
                    String content = Method_DownHTML(version_url);
                    if (!content.equals("Error")) {
                        saveUntil.Method_SaveFile(savePath + versionID + saveName, content);
                        dao_state.Method_ChangeState(C_ID);
                        System.out.println("下载完成->" + versionID);
                    }
                }
            }
        }
    }

    public void Method_4_Analysis_detail_versionPage(String savePath, String saveName) {
        DaoFather daoFather = new DaoFather(0, 1);
        DaoFather dao_pic_detail = new DaoFather(0, 2);
        ArrayList<Object> BeanList = daoFather.Method_Find();
        for (Object bean : BeanList) {
            Bean_version_pic_state bean_version_pic_state = (Bean_version_pic_state) bean;
            String versionId = bean_version_pic_state.getC_version_id();

            int count = bean_version_pic_state.getC_pic_count();
            if (count != 0) {
                String content = readUntil.Method_ReadFile(savePath + versionId + saveName);
                Document document = Jsoup.parse(content);
                Elements Items = document.select(".img-list-main");

                System.out.println(Items.size());

                for (int i = 0; i < Items.size(); i++) {

                    Element Item = Items.get(i);

                    Elements Item2 = Item.select(".tuku-sub-tab-container");
                    String title = Item2.select(".tuku-sub-tab-title").text();
                    System.out.println(title);

                    if (title.equals("实拍图")) {
                        Elements Items3 = Item.select(".tuku-sub-tab-menu").select("a");
                        System.out.println(Items3.size());
                        for (Element Item4 : Items3) {
                            String url = "https://photo.yiche.com" + Item4.attr("href");
                            String type = Item4.text();
                            System.out.println(url);
                            System.out.println(type);
                            Bean_version_pic_detail bean_version_pic_detail = new Bean_version_pic_detail();
                            bean_version_pic_detail.setC_version_id(versionId);
                            bean_version_pic_detail.setC_pic_type(title);
                            bean_version_pic_detail.setC_pic_type2(type);
                            bean_version_pic_detail.setC_pic_url(url);
                            bean_version_pic_detail.setC_DownState("否");
                            dao_pic_detail.Method_Insert(bean_version_pic_detail);
                        }
                    } else {
                        Elements Item3 = Item.select(".tuku-sub-tab-dest");
                        String url = "https://photo.yiche.com" + Item3.select("a").attr("href");
                        System.out.println(url);
                        Bean_version_pic_detail bean_version_pic_detail = new Bean_version_pic_detail();
                        bean_version_pic_detail.setC_version_id(versionId);
                        bean_version_pic_detail.setC_pic_type(title);
                        bean_version_pic_detail.setC_pic_type2("-");
                        bean_version_pic_detail.setC_pic_url(url);
                        bean_version_pic_detail.setC_DownState("否");
                        dao_pic_detail.Method_Insert(bean_version_pic_detail);
                    }
                }
            }

        }
    }

    public void Method_5_Down_type_version(String savePath, String saveName) {
        DaoFather dao_pic_detail = new DaoFather(0, 2);
        ArrayList<Object> BeanList = dao_pic_detail.Method_Find();
        for (Object bean : BeanList) {
            Bean_version_pic_detail bean_version_pic_detail = (Bean_version_pic_detail) bean;
            String DownState = bean_version_pic_detail.getC_DownState();
            String type1 = bean_version_pic_detail.getC_pic_type();
            String versionID = bean_version_pic_detail.getC_version_id();
            String url = bean_version_pic_detail.getC_pic_url();
            String type2 = bean_version_pic_detail.getC_pic_type2();
            if (type1.equals("实拍图")) {
                if (DownState.equals("否")) {
                    String content = Method_DownHTML(url);
                    if (!content.equals("Error")) {
                        saveUntil.Method_SaveFile(savePath + versionID + "_" + type2 + saveName, content);
                        dao_pic_detail.Method_ChangeState(bean_version_pic_detail.getC_ID());
                        System.out.println(bean_version_pic_detail.getC_ID());
                    }
                }
            }
        }
    }

    public void Method_6_Analysis_pictype(String savePath, String saveName) {
        DaoFather dao_pic_detail = new DaoFather(0, 2);
        DaoFather dao_pic_pages = new DaoFather(0, 3);

        ArrayList<Object> BeanList = dao_pic_detail.Method_Find();
        for (Object bean : BeanList) {
            Bean_version_pic_detail bean_version_pic_detail = (Bean_version_pic_detail) bean;
            String DownState = bean_version_pic_detail.getC_DownState();
            String type1 = bean_version_pic_detail.getC_pic_type();
            String versionID = bean_version_pic_detail.getC_version_id();
            String url = bean_version_pic_detail.getC_pic_url();
            String type2 = bean_version_pic_detail.getC_pic_type2();
            if (type1.equals("实拍图")) {
                String content = readUntil.Method_ReadFile(savePath + versionID + "_" + type2 + saveName);
                System.out.println(versionID + "_" + type2 + saveName);
                Document document = Jsoup.parse(content);

                Elements Page_Iytem = document.select(".img-list-pageNation").select(".pagenation-box.pagenation").select("a");

                if (Page_Iytem.size() == 0) {
                    Bean_version_pic_pages bean_version_pic_pages = new Bean_version_pic_pages();
                    bean_version_pic_pages.setC_PageType(type2);
                    bean_version_pic_pages.setC_versionID(versionID);
                    bean_version_pic_pages.setC_PageUrl(url);
                    bean_version_pic_pages.setC_PagNUmber(1);
                    bean_version_pic_pages.setC_DownState("否");
                    bean_version_pic_pages.setC_DownTime("--");
                    dao_pic_pages.MethodInsert(bean_version_pic_pages);
                }
                if (Page_Iytem.size() > 0) {
                    if (Page_Iytem.toString().contains("···")) {
                        System.out.println("-------HHHHHHH--------");
                        int pagesCount = Integer.parseInt(Page_Iytem.get(Page_Iytem.size() - 2).text());
                        for (int i = 1; i <= pagesCount; i++) {
                            Bean_version_pic_pages bean_version_pic_pages = new Bean_version_pic_pages();
                            bean_version_pic_pages.setC_PageType(type2);
                            bean_version_pic_pages.setC_versionID(versionID);
                            bean_version_pic_pages.setC_PageUrl(url + "?page=" + i);
                            bean_version_pic_pages.setC_DownState("否");
                            bean_version_pic_pages.setC_DownTime("--");
                            dao_pic_pages.MethodInsert(bean_version_pic_pages);
                        }
                    } else {
                        for (Element element : Page_Iytem) {

                            String page_count = element.text();
                            String page_url = "https://photo.yiche.com" + element.attr("href");
                            if (page_count.equals("<") || page_count.equals(">") || page_count.equals("···") || page_count.equals("&gt;") || page_count.equals("&amp;t;") || page_count.equals("&t;")) {
                                System.out.println("-----");
                            } else {
                                Bean_version_pic_pages bean_version_pic_pages = new Bean_version_pic_pages();
                                bean_version_pic_pages.setC_PageType(type2);
                                bean_version_pic_pages.setC_versionID(versionID);
                                if (page_count.equals("1")) {
                                    bean_version_pic_pages.setC_PageUrl(url);
                                } else {
                                    bean_version_pic_pages.setC_PageUrl(page_url);
                                }
                                bean_version_pic_pages.setC_DownState("否");
                                bean_version_pic_pages.setC_DownTime("--");
                                bean_version_pic_pages.setC_PagNUmber(Integer.parseInt(page_count));
                                dao_pic_pages.MethodInsert(bean_version_pic_pages);
                            }
                        }
                    }
                }
            }
        }
    }

    public void Method_7_DownPage(String savePath, String saveName) {
        DaoFather dao_pic_pages = new DaoFather(0, 3);
        ArrayList<Object> BeanList = dao_pic_pages.Method_Find();
        for (Object bean : BeanList) {
            Bean_version_pic_pages bean_version_pic_pages = (Bean_version_pic_pages) bean;
            String DownState = bean_version_pic_pages.getC_DownState();
            int count = bean_version_pic_pages.getC_PagNUmber();
            int C_ID = bean_version_pic_pages.getC_ID();
            String type = bean_version_pic_pages.getC_PageType();
            String versionID = bean_version_pic_pages.getC_versionID();
            String url = bean_version_pic_pages.getC_PageUrl();
            if (count != 1) {
                if (DownState.equals("否")) {
                    System.out.println(versionID);
                    String content = Method_DownHTML(url);
                    if (!content.equals("Error")) {
                        saveUntil.Method_SaveFile(savePath + versionID + "_" + type + count + saveName, content);
                        dao_pic_pages.Method_ChangeState(C_ID);
                        System.out.println(C_ID);
                    }
                }
            }
        }
    }

    public void Method_8_Analysis(String page1savePath, String page1saveName, String moreSavePath) {
        DaoFather dao_pic_pages = new DaoFather(0, 3);
        DaoFather dao_pic_api = new DaoFather(0,4);
        DaoFather dao_pic_down = new DaoFather(0,5);

        ArrayList<Object> BeanList = dao_pic_pages.Method_Find();
        for (Object bean : BeanList) {
            Bean_version_pic_pages bean_version_pic_pages = (Bean_version_pic_pages) bean;
            String DownState = bean_version_pic_pages.getC_DownState();
            int count = bean_version_pic_pages.getC_PagNUmber();
            int C_ID = bean_version_pic_pages.getC_ID();
            String type = bean_version_pic_pages.getC_PageType();
            String versionID = bean_version_pic_pages.getC_versionID();
            String url = bean_version_pic_pages.getC_PageUrl();
            String content = "";
            if (count == 1) {
                content = readUntil.Method_ReadFile(page1savePath + versionID + "_" + type + page1saveName);
            }else {
                content = readUntil.Method_ReadFile(moreSavePath + versionID + "_" + type + count + page1saveName);
            }

            Document document = Jsoup.parse(content);
            Elements imagItems = document.select(".img-list-main");
            System.out.println("图片区域个数->\t"+imagItems.size());

            for (Element imgList:imagItems){
                Elements oneTypeList = imgList.select(".tuku-serial-img-list");

                Elements Judge = oneTypeList.select("span").select(".tuku-serial-img-item");
                if (Judge.size()==0){
                    System.out.println("---无---");
                    Elements imag_pic_List = oneTypeList.select("a");
                    for (Element element:imag_pic_List){
                        String Pid_ID = element.attr("point-cid");
                        String pic_source_url = element.select("img").attr("data-webp");
                        String pic_deal_url = pic_source_url.replace("w300", "w1200");
                        Bean_version_pic_downpic bean_version_pic_downpic = new Bean_version_pic_downpic();
                        bean_version_pic_downpic.setC_dealUrl(pic_deal_url);
                        bean_version_pic_downpic.setC_picID(Pid_ID);
                        bean_version_pic_downpic.setC_sourceUrl(pic_source_url);
                        bean_version_pic_downpic.setC_version_id(versionID);
                        bean_version_pic_downpic.setC_type(type);
                        bean_version_pic_downpic.setC_DownState("否");
                        dao_pic_down.MethodInsert(bean_version_pic_downpic);
                    }
                }else {
//                    String albumid = Judge.attr("data-albumid");
//                    System.out.println(albumid);
//                    Bean_version_pic_requestapi bean_version_pic_requestapi = new Bean_version_pic_requestapi();
//                    bean_version_pic_requestapi.setC_albumid(albumid);
//                    bean_version_pic_requestapi.setC_DownState("否");
//                    bean_version_pic_requestapi.setC_version_id(versionID);
//                    bean_version_pic_requestapi.setC_DownTime("--");
//                    bean_version_pic_requestapi.setC_type(type);
//                    dao_pic_api.MethodInsert(bean_version_pic_requestapi);
                }
            }
        }
    }


    public void Method_9_RequestAPI(String savePath,String saveName,String url){
        DaoFather dao_pic_api = new DaoFather(0,4);
        ArrayList<Object> BeanList = dao_pic_api.Method_Find();
        for (Object bean:BeanList){
            Bean_version_pic_requestapi bean_api = (Bean_version_pic_requestapi) bean;
            String DownState = bean_api.getC_DownState();
            String versionID = bean_api.getC_version_id();
            String albumid = bean_api.getC_albumid();
            String type = bean_api.getC_type();
            int C_ID = bean_api.getC_ID();
            int groupId = 0;
            switch (type) {
                case "外观":
                    groupId = 6;
                    break;
                case "前排":
                    groupId= 7;
                    break;
                case "后排":
                    groupId= 8;
                    break;
            }
            if (DownState.equals("否")){
                System.out.println(C_ID);
                String parm = "{\"albumId\":\""+albumid+"\",\"albumType\":\"1\",\"groupId\":\""+groupId+"\"}";
                System.out.println(parm);
                String content = Method_RequestAPI(url, parm);
                if (!content.equals("Error")){
                    JSONObject jsonObject = JSONObject.parseObject(content);
                    System.out.println(jsonObject.getString("status")+"\t"+jsonObject.getString("message"));
                    if (jsonObject.getString("status").equals("1")){
                        saveUntil.Method_SaveFile(savePath+versionID+"_"+albumid+"_"+type+saveName, content);
                        dao_pic_api.Method_ChangeState(C_ID);
                    }
                }
            }
        }
    }

    public String Method_RequestAPI(String main_Url, String param) {
        String resultJson = "Error";
        String timestamp = String.valueOf(System.currentTimeMillis());
//        String param = "{\"carIds\":\"" + versionID + "\",\"cityId\":\"201\"}";

        String o = "19DDD1FBDFF065D3A4DA777D2D7A81EC";
        String s = "cid=" + 508 + "&param=" + param + o + timestamp;
        String md5_Str = MD5Until.Method_Make_MD5(s);
        //System.out.println(md5_Str);
        String Cookie = "CIGUID=849ec451-0627-4ee7-8139-7d0a7233d10a; auto_id=6618b3b2d19037859fee9321a2165348; UserGuid=849ec451-0627-4ee7-8139-7d0a7233d10a; CIGDCID=1f1c18bfd1a13ef3a7c0b2edee9ef3ca; G_CIGDCID=1f1c18bfd1a13ef3a7c0b2edee9ef3ca; selectcity=110100; selectcityid=201; selectcityName=%E5%8C%97%E4%BA%AC; Hm_lvt_610fee5a506c80c9e1a46aa9a2de2e44=1702474832,1703750079; isWebP=true; locatecity=110100; bitauto_ipregion=101.27.236.186%3A%E6%B2%B3%E5%8C%97%E7%9C%81%E4%BF%9D%E5%AE%9A%E5%B8%82%3B201%2C%E5%8C%97%E4%BA%AC%2Cbeijing; yiche_did=5e58467469f6b2c8732f3492175f2a13_._1000_._0_._847319_._905548_._W2311281141108493357; csids=8014_2556_2855_5476_10188_6435_6209_2573_3750_5786; Hm_lpvt_610fee5a506c80c9e1a46aa9a2de2e44=1703842561";
        try {
            String param_url = URLEncoder.encode(param, "UTF-8");
            String main_url = main_Url + "cid=508&param=" + param_url;
//            System.out.println(main_url);
            Connection.Response res = Jsoup.connect(main_url).method(Connection.Method.GET)
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
                    .header("X-City-Id", "201")
                    .header("X-Ip-Address", "101.27.236.186")
                    .header("X-Platform", "pc")
                    .header("X-Sign", md5_Str)
                    .header("X-User-Guid", "849ec451-0627-4ee7-8139-7d0a7233d10a")
                    .header("Cookie", Cookie)
                    .header("Content-Type", "application/json;charset=UTF-8")
                    .header("Cid", "508")
                    .header("Sec-Ch-Ua", "\"Not_A Brand\";v=\"8\", \"Chromium\";v=\"120\", \"Google Chrome\";v=\"120\"")
                    .header("Sec-Ch-Ua-Mobile", "?0")
                    .header("Sec-Ch-Ua-Platform", "\"Windows\"")
                    .header("Sec-Fetch-Dest", "empty")
                    .header("Sec-Fetch-Mode", "cors")
                    .header("Sec-Fetch-Site", "same-site")
                    .header("X-Timestamp", timestamp)
                    .ignoreContentType(true)
                    .ignoreHttpErrors(true).execute();
            resultJson = res.body();
            Thread.sleep(480);
//            System.out.println(resultJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultJson;
    }


    public void Method_10_AnalysisRequestJSON(String savePath,String saveName){
        DaoFather dao_pic_api = new DaoFather(0,4);
        DaoFather dao_pic_down = new DaoFather(0,6);

        ArrayList<Object> BeanList = dao_pic_api.Method_Find();
        for (Object bean:BeanList){
            Bean_version_pic_requestapi bean_api = (Bean_version_pic_requestapi)bean;
            String versionID = bean_api.getC_version_id();
            String albumid = bean_api.getC_albumid();
            String type = bean_api.getC_type();
            System.out.println(bean_api.getC_ID());
            String content = readUntil.Method_ReadFile(savePath+versionID+"_"+albumid+"_"+type+saveName);
            JSONArray dataArray = JSONObject.parseObject(content).getJSONObject("data").getJSONArray("list");

            for (int i = 0; i < dataArray.size(); i++) {

                JSONObject object = dataArray.getJSONObject(i);
                String picID = object.getString("photoId");
                String pic_source_url = object.getString("pathWp");
//                https://img5.bitautoimg.com/usercenter/autoalbum/files/20131011/816/w{0}_m82_yichecar_16283981646767.JPG.webp
                String pic_deal_url = pic_source_url.replace("w{0}", "w1200");

                Bean_version_pic_downpic2 bean_down = new Bean_version_pic_downpic2();
                bean_down.setC_picID(picID);
                bean_down.setC_type(type);
                bean_down.setC_version_id(versionID);
                bean_down.setC_dealUrl(pic_deal_url);
                bean_down.setC_sourceUrl(pic_source_url);
                bean_down.setC_DownState("否");
                bean_down.setC_DownTime("--");
                bean_down.setC_albumid(albumid);
                dao_pic_down.MethodInsert(bean_down);
            }
        }
    }
}
