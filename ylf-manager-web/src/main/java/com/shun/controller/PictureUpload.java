package com.shun.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.shun.common.utils.FastDFSClientUtils;
import com.shun.common.utils.JackJsonUtils;

/**
* @author czs			作为图片上传的控制层
* @version 创建时间：2018年5月16日 上午8:14:50 
*/

@Controller
public class PictureUpload {

	@Value("${imageServerUrl}")
	private String imageServerUrl;

	@RequestMapping(value = "/pic/upload", produces = MediaType.TEXT_PLAIN_VALUE + ";charset=utf-8")
	@ResponseBody
	public String uploadFile(MultipartFile uploadFile) {
		// 定义返回的结果
		Map<String, Object> resultMap = new HashMap<>();

		try { // 把图片上传到服务器
			FastDFSClientUtils utils = new FastDFSClientUtils("classpath:conf/FastDfcClient.txt");
			
			// 得到一个图片的地址和文件名
			String fileName = uploadFile.getOriginalFilename(); // 完整的源文件名
			String ext = fileName.substring(fileName.lastIndexOf(".") + 1); // 截取文件的后缀名，这个加1是因为index到的包括点，所以需要去掉
			String imageUrl = utils.uploadFile(uploadFile.getBytes(), ext);

			// 补充完整的文件名
			imageUrl = imageServerUrl + imageUrl;

			// 从http://kindeditor.net/docs/upload.html官网文档可以得出返回结果是一个json，这个json可以用pojo来转json，但是这个参数不太稳定
			resultMap.put("error", 0);
			resultMap.put("url", imageUrl);

		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("error", 1);
			resultMap.put("url", "额，图片上传失败~~~");
		}
		// 将封装好的map返回给页面
		return JackJsonUtils.objectToJson(resultMap);
	}

}
