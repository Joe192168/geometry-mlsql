package com.geominfo.mlsql.controller;

import com.geominfo.mlsql.commons.Message;
import com.geominfo.mlsql.services.impl.HdfsService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

/**
 * @title: HdfdController
 * @date 2021/4/27 16:15
 */

@RestController
@RequestMapping("/hdfs")
@Api(value="Hdfs文件系统接口",tags={"用户账户操作接口"})
@Slf4j
public class HdfdController {

    @Autowired
    private HdfsService hdfsService;
    
    /***
     * @Description: 上传文件接口
     * @Author: zrd 
     * @Date: 2021/4/28 14:11
     * @param souPath 本地文件路径
     * @param dstPath hdfs系统相对路径
     * @return com.geominfo.mlsql.commons.Message
     */
    @GetMapping("/uploadFile")
    public Message uploadFile(@RequestParam String souPath,@RequestParam String dstPath) {
        try {
            hdfsService.uploadFileToHdfs(souPath,dstPath);
        } catch (IOException e) {
            log.error(MessageFormat.format("上传文件至HDFS失败，srcFile:{0},dstPath:{1}",souPath,dstPath),e);
            return new Message().error("上传文件至HDFS失败");
        }
        return new Message().ok();
    }

    /***
     * @Description: 列出指定路径所有文件
     * @Author: zrd
     * @Date: 2021/4/28 9:47
     * @param route 所需列出文件目标路径
     * @return com.geominfo.mlsql.commons.Message
     */
    @GetMapping("/listFilesByRoute")
    public Message getAllFileByRoute(@RequestParam String route) {
        List<Map<String, Object>> maps = hdfsService.listFiles(route, null);
        return new Message().ok().addData("data",maps);
    }

    /***
     * @Description: 从hdfs下载文件接口
     * @Author: zrd
     * @Date: 2021/4/29 10:02
     * @param souPath HDFS的相对目录路径
     * @param dstPath 下载之后本地文件路径
     * @return com.geominfo.mlsql.commons.Message
     */
    @GetMapping("/downloadFile")
    public Message downloadFile(@RequestParam String souPath,@RequestParam String dstPath) {
        try {
            hdfsService.downloadFileFromHdfs(souPath,dstPath);
        } catch (IOException e) {
            log.error("从HDFS下载文件至本地失败，srcFile:{}",souPath,e);
        }
        return new Message().ok();
    }

    /***
     * @Description: 删除文件接口
     * @Author: zrd
     * @Date: 2021/4/29 10:55
     * @param path 被删除文件路径
     * @return com.geominfo.mlsql.commons.Message
     */
    @GetMapping("/deletetFile")
    public Message deleteFile(@RequestParam String path) {
        boolean delete = hdfsService.delete(path);
        if (delete) {
            return new Message().ok("删除成功");
        }else {
            return new Message().error("删除失败，文件不存在或已删除");
        }
    }

    /***
     * @Description: 创建文件接口
     * @Author: zrd
     * @Date: 2021/4/30 10:23
     * @param path 文件夹路径
     * @return com.geominfo.mlsql.commons.Message
     */
    @GetMapping("/mkdir")
    public Message mkdir(@RequestParam String path) {
        boolean mkdir = hdfsService.mkdir(path);
        if (mkdir) {
            return new Message().ok("创建文件夹成功");
        }else {
            return new Message().error("创建文件夹失败");
        }
    }
}
