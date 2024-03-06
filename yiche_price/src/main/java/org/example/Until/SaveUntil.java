package org.example.Until;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;


public class SaveUntil {
    // 存文件的 工具类
    public void Method_SaveFile(String path, String Content) {
        //这个是覆盖的存储方式
        try {
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(path, false), 331074);//165537
            bufferedOutputStream.write(Content.getBytes(StandardCharsets.UTF_8));   //StandardCharsets.UTF_8
            bufferedOutputStream.flush();
            bufferedOutputStream.close();
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }



    public void Method_SaveFile_True(String path, String Content) {
        //这个是叠加的存储方式
        try {
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(path, true), 331074);//165537
            bufferedOutputStream.write(Content.getBytes(StandardCharsets.UTF_8));   //StandardCharsets.UTF_8  "GB2312"
            bufferedOutputStream.flush();
            bufferedOutputStream.close();
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }


    public boolean Method_SavePic(String path,String fileName,String urlString){
        try{
            // 构造URL
            URL url = new URL(urlString);
            // 打开连接
            URLConnection con = url.openConnection();
            // 输入流
            InputStream is = con.getInputStream();
            // 1K的数据缓冲
            byte[] bs = new byte[81920];
            // 读取到的数据长度
            int len;
            // 输出的文件流
            String filename = path+fileName;  //下载路径及下载图片名称
            File file = new File(filename);
            FileOutputStream os = new FileOutputStream(file, true);
            // 开始读取
            while ((len = is.read(bs)) != -1) {
                os.write(bs, 0, len);
            }
            // 完毕，关闭所有链接
            os.close();
            is.close();
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
