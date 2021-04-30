package com.geominfo.mlsql;


import com.alibaba.fastjson.JSONObject;
import com.geominfo.mlsql.services.impl.HdfsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
public class GeometryMlsqlRestApplicationTests {

	@Autowired
	HdfsService hdfsService;
	/**
	 * 测试列出某个目录下面的文件
	 */
	@Test
	public void testListFiles(){
		List<Map<String,Object>> result = hdfsService.listFiles("/home/users/ryan@gmail.com/tmp/upload2",null);

		result.forEach(fileMap -> {
			fileMap.forEach((key,value) -> {
				System.out.println(key + "--" + value);
			});
			System.out.println();
		});
	}

	@Test
	public void uploadFile(){
		try {
			hdfsService.uploadFileToHdfs("C:\\Users\\Lenovo\\Desktop\\20190323140321501.png","/home/users/ryan@gmail.com");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void downloadFile(){
		try {
			hdfsService.downloadFileFromHdfs("/home/users/ryan@gmail.com/tmp/upload/cat.jpg","C:\\Users\\Lenovo\\Desktop\\test");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void mkdirTest(){
		Object dirWithTree = hdfsService.getAllDirWithTree("/home/users/ryan@gmail.com", null);
		String s = JSONObject.toJSONString(dirWithTree);
		System.out.println(s);
	}
}
