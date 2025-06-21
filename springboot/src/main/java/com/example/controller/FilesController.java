package com.example.controller;

import com.example.config.Result;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;

/**
 * @author dsh
 */
@RestController
@RequestMapping("/files")
public class FilesController {

    @PostMapping("/upload")
    public Result upload(@RequestParam("file") MultipartFile file) {
        String fipath = System.getProperty("user.dir") + "/files/";
//        System.out.println("目标路径: " + fipath);

        try {
            Path path = Paths.get(fipath);
//            System.out.println("目标路径: " + path);
            // 如果目录不存在，则创建它
            if (!Files.exists(path)) {
                // 创建多级目录
                Files.createDirectories(path);
            }

            // 构建文件名并保存文件
            String fileName = file.getOriginalFilename();
            Path targetLocation = null;
            if (fileName != null) {
                targetLocation = path.resolve(fileName);
            }
//            System.out.println("目标路径: " + targetLocation);
            //  保存文件
            file.transferTo(targetLocation);

            // 生成可访问的 URL（需要静态资源配置）
            String url = "http://localhost:8080/files/download/" + fileName;
//            System.out.println("访问地址: " + url);
            return Result.success(url);

        } catch (IOException e) {
            e.printStackTrace();
            return Result.fail();
        }
    }

    @GetMapping("/download/{filename}")
    public void download(@PathVariable String filename, HttpServletResponse response) {
        String filePath = System.getProperty("user.dir") + "/files/" + filename;
        try {
            Path path = Paths.get(filePath);
            // 判断文件是否存在
            if (!Files.exists(path)) {
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        try {
            //  设置响应头信息
            response.setContentType("application/octet-stream");
            String encodedFilename = URLEncoder.encode(filename, StandardCharsets.UTF_8)
                                            .replaceAll("\\+", "%20");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + encodedFilename + "\"; filename*=UTF-8''" + encodedFilename);
            //  读取文件内容并写入响应输出流
            byte[] bytes = Files.readAllBytes(Paths.get(filePath));
            //  写入响应输出流
            response.getOutputStream().write(bytes);
            //  刷新响应输出流
            response.flushBuffer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
