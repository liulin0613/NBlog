package com.zxqax.nblog.service.Impl;

import com.zxqax.nblog.dao.LoginDao;
import com.zxqax.nblog.dto.Result;
import com.zxqax.nblog.service.ImageService;
import com.zxqax.nblog.service.RegisterService;
import com.zxqax.nblog.service.UserService;
import com.zxqax.nblog.utils.ThreadLocalUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

@Service
@Slf4j
public class ImageServeImpl implements ImageService {

    private final String SERVER_PATH=File.separator + "static" + File.separator+ "images" + File.separator;
    private final String Avatar_SERVER_PATH = File.separator + "static" + File.separator+ "avatar" + File.separator;

    @Autowired
    UserService userService;

    @Autowired
    LoginDao loginDao;

    /**
     * 图片上传
     * @param image 图片
     * @return Result
     */
    @Override
    public Result<?> upload(String image) {
        //获取当前用户 ID
        int id= userService.getCurrentUser().getId();
        //图片文件夹路径
        String base= SERVER_PATH + id + File.separator;
        //获取随机 uuid
        String uuid = UUID.randomUUID().toString().replaceAll("-","").substring(0,6);
        //获取线程安全的时间格式化
        SimpleDateFormat dateFormat = ThreadLocalUtils.localSimpleDataFormatOnlyYMD.get();
        //生成当前日期作为前缀
        String datePrefix = dateFormat.format(new Date());

        File file=new File(base);
        //如果文件夹不存在，则新建
        if (!file.exists()){
            boolean success=file.mkdirs();

            String event="为用户 ID 为 [ "+id+" ] 创建文件夹 "+success;
            log.info(event);
        }

        //生成图片
        generateImage(image,base + datePrefix + uuid + ".png");

        return Result.ok(datePrefix + uuid);
    }

    @Override
    public Result<?> del(String path) {
        //获取当前用户 ID
        int id= userService.getCurrentUser().getId();
        //图片文件夹路径
        String base= SERVER_PATH + id + File.separator;
        File file = new File(base + path+".png");

        // 如果图片存在，删除图片
        if(file.exists()){
            String event="用户 ID 为 [ "+ id +" ] 删除图片: [ "+ path +" .png ] 状态: ["+file.delete()+"]";
            log.info(event);
        }

        return Result.ok();
    }

    @Override
    public Result<?> alertAvatar(String img) {
        //获取当前用户 ID
        int id= userService.getCurrentUser().getId();
        if(id!=-1){
            //图片文件夹路径
            String base= Avatar_SERVER_PATH;
            //获取随机 uuid
            String uuid = UUID.randomUUID().toString().replaceAll("-","").substring(0,6);
            generateImage(img,base + id +"_"+ uuid+".png");
            userService.updateImg(id,id +"_"+ uuid);
            return Result.ok(id +"_"+ uuid);
        }else{
            return Result.fail();
        }
    }

    /**
     * 生成图片
     * @param imgStr 图片 base64 编码
     * @param imgFilePath 保存路径
     */
    private void generateImage(String imgStr, String imgFilePath) {
        if (imgStr == null) // 图像数据为空
            return;

        imgStr=imgStr.substring(imgStr.indexOf("base64,")+7);

        Base64.Decoder decoder = Base64.getDecoder();;
        try {
            // Base64解码
            byte[] bytes = decoder.decode(imgStr);
            for (int i = 0; i < bytes.length; ++i) {
                if (bytes[i] < 0) {// 调整异常数据
                    bytes[i] += 256;
                }
            }
            // 生成 .png 图片
            OutputStream out = new FileOutputStream(imgFilePath);
            out.write(bytes);
            out.flush();
            out.close();
        } catch (Exception ignored) {
        }
    }
}
