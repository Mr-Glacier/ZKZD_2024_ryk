package org.example.Controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.example.Dao.DaoFather;
import org.example.Entity.BeanCity;
import org.example.Until.HelpCreateFile;
import org.example.Until.HttpUntil;
import org.example.Until.ReadUntil;
import org.example.Until.SaveUntil;

import java.util.ArrayList;

public class PriceController {
    private SaveUntil saveUntil = new SaveUntil();
    private ReadUntil readUntil = new ReadUntil();
    private HttpUntil httpUntil = new HttpUntil();



    /**
     * 从指定URL下载城市数据的JSON文件并保存到本地。
     *
     * @param savePath 保存文件的路径。
     * @param saveName 保存文件的名称。
     * @param urlString 要下载数据的URL地址。
     */
    public void Method_1_DownCityJSON(String savePath,String saveName,String urlString){
        // 向指定URL发送请求，并获取返回的内容
        String content = httpUntil.Method_RequestAPI(urlString,"{}");
        // 如果请求成功，内容不等于"Error"
        if (!content.equals("Error")){
            // 将获取到的内容保存到指定路径和名称的文件中
            saveUntil.Method_SaveFile(savePath+saveName,content);
            // 打印下载完成的提示信息
            System.out.println("地区文件下载完毕");
        }

    }

    /**
     * 分析并保存城市的详细信息到数据库。
     * @param citySavePath 城市信息文件在本地的存储路径。
     * @param saveName 城市信息文件的名称。
     * 方法流程：
     * 1. 从指定路径读取城市信息文件内容。
     * 2. 将文件内容解析为JSON数组，遍历每个城市信息。
     * 3. 提取每个城市的关键信息（城市ID、城市名称、城市级别、区域ID、父区域ID）。
     * 4. 创建BeanCity实例，设置城市信息。
     * 5. 将城市信息插入到数据库中。
     */
    public void Method_2_AnalysisCity(String citySavePath,String saveName){
        // 从指定路径读取城市信息文件
        String content = readUntil.Method_ReadFile(citySavePath+saveName);
        // 解析文件内容为JSON数组
        JSONArray cityList = JSONObject.parseObject(content).getJSONObject("data").getJSONArray("list");

        // 创建DaoFather实例，用于数据库操作
        DaoFather daoFather = new DaoFather(0,0);
        // 遍历城市信息列表，逐个插入数据库
        for(int i=0;i<cityList.size();i++){
            // 提取城市信息字段
            String cityId = cityList.getJSONObject(i).getString("CityId");
            String cityName = cityList.getJSONObject(i).getString("CityName");
            String engName = cityList.getJSONObject(i).getString("EngName");
            String cityLevel = cityList.getJSONObject(i).getString("CityLevel");
            String parentId = cityList.getJSONObject(i).getString("ParentId");
            String initial = cityList.getJSONObject(i).getString("Initial");
            String regionId = cityList.getJSONObject(i).getString("RegionId");
            String parentRegionId = cityList.getJSONObject(i).getString("ParentRegionId");

            // 创建BeanCity实例并设置城市信息
            BeanCity beanCity = new BeanCity();

            beanCity.setCityId(cityId);
            beanCity.setCityName(cityName);
            beanCity.setEngName(engName);
            beanCity.setCityLevel(cityLevel);
            beanCity.setParentId(parentId);
            beanCity.setInitial(initial);
            beanCity.setRegionId(regionId);
            beanCity.setParentRegionId(parentRegionId);
            System.out.println(cityName);
            // 插入城市信息到数据库
            daoFather.MethodInsert(beanCity);
        }
    }


    public void Method_3_Down_detail_versionPage(String savePath, String saveName) {
        DaoFather dao_version = new DaoFather(0,1);
        DaoFather dao_city = new DaoFather(0,0);

        ArrayList<Object> beanList = dao_version.Method_Find();
    }





}
