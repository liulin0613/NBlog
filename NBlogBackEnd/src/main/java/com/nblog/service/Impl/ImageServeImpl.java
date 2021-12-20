package com.nblog.service.Impl;

import com.nblog.dto.Result;
import com.nblog.entity.UserInfo;
import com.nblog.service.ImageService;
import com.nblog.service.UserService;
import com.nblog.utils.ThreadUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

import static com.nblog.constant.CommonConst.AVATAR_PATH;
import static com.nblog.constant.CommonConst.IMAGE_PATH;

@Service
@Slf4j
public class ImageServeImpl implements ImageService {
    private UserService userService;

    @Autowired
    public ImageServeImpl(UserService userService) {
        this.userService = userService;
    }

    // 线程安全的时间格式化器
    private ThreadLocal<SimpleDateFormat> localSimpleDataFormatOnlyYMD =
            ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyyMMdd"));

    /**
     * 图片上传
     * @param image 图片
     * @return Result
     */
    @Override
    public Result<String> upload(String image) {
        //获取当前用户 ID
        int id= userService.getCurrentUser().getId();
        //图片文件夹路径
        String base= IMAGE_PATH + id + File.separator;
        //获取随机 uuid
        String uuid = UUID.randomUUID().toString().replaceAll("-","").substring(0,6);
        //获取线程安全的时间格式化
        SimpleDateFormat dateFormat = localSimpleDataFormatOnlyYMD.get();
        //生成当前日期作为前缀
        String datePrefix = dateFormat.format(new Date());

        File file=new File(base);

        //如果文件夹不存在，则新建
        if (!file.exists()){
            boolean res=file.mkdirs();

            String event="为用户 ID 为 [ "+id+" ] 创建文件夹 "+ res;
            log.info(event);
        }

        //生成图片
        generateImage(image,base + datePrefix + uuid + ".png");

        return Result.ok(datePrefix + uuid);
    }

    @Override
    public Result<Boolean> del(String path) {
        //获取当前用户 ID
        int id= userService.getCurrentUser().getId();

        //图片文件夹路径
        String base= IMAGE_PATH + id + File.separator;
        File file = new File(base + path +".png");

        // 如果图片存在，删除图片
        if(file.exists()){
            boolean res=file.delete();

            String event="用户 ID 为 [ "+ id +" ] 删除图片: [ "+ path +" .png ] 状态: ["+res+"]";
            log.info(event);
        }

        return Result.ok(true);
    }

    @Override
    public Result<String> updateAvatar(String img) {
        // 获取当前用户
        UserInfo info = userService.getCurrentUser();
        int id = info.getId();

        // 生成随机 uuid
        String uuid = UUID.randomUUID().toString().replaceAll("-","").substring(0,6);

        // 生成头像图片
        generateImage(img,AVATAR_PATH + id +"_"+ uuid+".png");

        // 更新数据库中的图片
        userService.updateImg(id,id +"_"+ uuid);

        // 异步更新 redis 中的用户信息
        ThreadUtils.getExecutorService().execute(()->{
            info.setAvatar(id +"_"+ uuid);
            userService.saveUser(id+"",info);
        });

        return Result.ok(id +"_"+ uuid);
    }

    /**
     * 生成图片
     * @param imgStr 图片 base64 编码
     * @param imgFilePath 保存路径
     */
    private void generateImage(String imgStr, String imgFilePath) {
        if (imgStr == null) // 图像数据为空
            return;

        // 获取图片的 base64 编码
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
