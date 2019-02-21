package com.server.b.controller;

import com.luhuiguo.fastdfs.domain.StorePath;
import com.luhuiguo.fastdfs.service.FastFileStorageClient;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class UploadController {
    @Autowired
    private FastFileStorageClient storageClient;
    /**
     *
     * @param myFile  从浏览器提交过来
     * @return
     */
    @PostMapping("/up")
    public String upload(@RequestParam("myFile") MultipartFile myFile) throws IOException {
        //获取文件后缀名
        String extension = FilenameUtils.getExtension(myFile.getOriginalFilename());
        //将要上传的文件存入FastDFS
        StorePath sp=storageClient.uploadFile("group1",myFile.getInputStream(),myFile.getSize(),extension);
        System.out.println(">>>>>>>>>>>>>>>group="+sp.getGroup()+"   getPath="+sp.getPath()+"<<<<<<<<<<<<<<<");
        return "<a href=/dows?pathName="+sp.getPath()+">下载</a>";
    }
    @GetMapping("/dows")
    public void download(HttpServletResponse re,String pathName) throws IOException {

        re.setHeader("Content-Disposition","attachment;filename="+System.currentTimeMillis()+".jpg");
		//去linux下载对应的文件
        String groupName="group1";
        byte[] bytes = storageClient.downloadFile(groupName, pathName);
        re.getOutputStream().write(bytes);

    }
}