package com.zzb.AutomaArticle.utils;


import com.obs.services.ObsClient;
import com.obs.services.model.PutObjectResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;


@Component
public class HwYunUtils {

    @Value("${huaweiyun.obs.endpoint}")
    private String endPoint;
    @Value("${huaweiyun.obs.ak}")
    private String ak;
    @Value("${huaweiyun.obs.sk}")
    private String sk;
    @Value("${huaweiyun.obs.bucketName}")
    private String bucketName;

    public PutObjectResult uploadFile(MultipartFile file, String filename)  {
        // 创建ObsClient实例
        ObsClient obsClient = new ObsClient(ak, sk, endPoint);
        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        PutObjectResult putObjectResult = obsClient.putObject(bucketName, filename, inputStream);
        try {
            obsClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return putObjectResult;
    }
}
