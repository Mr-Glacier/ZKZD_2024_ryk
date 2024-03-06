package org.example.Until;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.net.URLEncoder;

public class HttpUntil {
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
}
