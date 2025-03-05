package io.github.yangyouwang.module.common.controller;

import io.github.yangyouwang.common.base.controller.BaseController;
import io.github.yangyouwang.common.base.domain.Result;
import io.github.yangyouwang.framework.util.aliyun.SampleOSS;
import io.github.yangyouwang.framework.config.properties.MinioProperties;
import io.github.yangyouwang.module.common.service.MinIoService;
import io.github.yangyouwang.framework.util.aliyun.SampleVod;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 * 通用请求处理
 *
 * @author crud
 */
@Slf4j
@Controller
@RequestMapping("/common")
@RequiredArgsConstructor
public class CommonController extends BaseController {

    private final SampleOSS sampleOSS;

    private final MinIoService minIoService;

    private final MinioProperties minioProperties;

    private final SampleVod sampleVod;

    private static final String SUFFIX = "common";

    /**
     * 跳转到树结构向导页
     * @return 树结构向导页
     */
    @GetMapping("/treePage")
    public String treePage(String url, ModelMap map) {
        map.put("url", url);
        return SUFFIX + "/tree";
    }

    /**
     * 上传图片OSS
     * @param fileDir 图片路径
     * @return 图片路径相应
     */
    @PostMapping("/uploadOSS")
    @ResponseBody
    public Result uploadOSS(MultipartFile file, @RequestParam(value = "fileDir",required = false,defaultValue = "img/def") String fileDir) {
        // 上传文件路径
        String url = sampleOSS.upload( file, fileDir);
        Map<String,Object> ajax = new HashMap<>(16);
        ajax.put("fileName", file.getOriginalFilename());
        ajax.put("url", url);
        return Result.success(ajax);
    }

    /**
     * 上传文件MinIo
     */
    @PostMapping("/uploadFileMinIo")
    @ResponseBody
    public Result uploadFileMinIo(MultipartFile file) throws Exception {
        if(StringUtils.isEmpty(file.getName())){
            return Result.failure("上传文件名称为空",file.getOriginalFilename());
        }
        String bucketName = minioProperties.getBucketName();
        String fileName = minIoService.minioUpload(file, file.getOriginalFilename(), bucketName);
        String url = minIoService.getShowUtrl(fileName, bucketName);
        Map<String,Object> ajax = new HashMap<>(16);
        ajax.put("fileName", fileName);
        ajax.put("url", url);
        return Result.success(ajax);
    }
}
