package com.geominfo.mlsql;

import com.alibaba.fastjson.JSONObject;
import com.geominfo.mlsql.domain.vo.MlsqlScriptFile;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.Charset;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @program: MLSQL CONSOLE后端接口
 * @description: EngineConntrollerTest
 * @author: anan
 * @create: 2020-06-05 15:23
 * @version: 1.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@WebAppConfiguration
public class EngineConntrollerTest {
    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;

    @Before
    public void setUp(){
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }
    /**
      * description: 测试执行脚本
      * author: anan
      * date: 2020/6/5
      * param:
      * return:
     */

    @Test
    public void runSql() throws Exception {
        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
        params.add("sql","select 1 as a,'安静' as b as bbc;");

        String responseString = mvc.perform(post("/run/script")
                .contentType(MediaType.APPLICATION_JSON)
                .params(params))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andDo(print())
                .andReturn().getResponse().getContentAsString(Charset.forName("UTF8"));
        System.out.println("执行结果 = " + responseString);
    }
    /**
      * description: 测试通过id，获取文件内容，将内容post给engine执行
      * author: anan
      * date: 2020/6/5
      * param:
      * return:
     */

    @Test
    public void getFileContentRunSql() throws Exception{
        String responseString = mvc.perform(MockMvcRequestBuilders.get("/script_file/getcontent")
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .param("id", "185"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andReturn().getResponse().getContentAsString();
        JSONObject resultJson = JSONObject.parseObject(responseString);
        String dataOne = resultJson.get("data").toString();
        JSONObject dataOneValue = JSONObject.parseObject(dataOne);
        JSONObject dataTwo = JSONObject.parseObject(dataOneValue.getString("data"));
        MlsqlScriptFile mlsqlScriptFile = dataTwo.toJavaObject(MlsqlScriptFile.class);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("sql", mlsqlScriptFile.getContent());

        responseString = mvc.perform(post("/run/script")
                .contentType(MediaType.APPLICATION_JSON)
                .params(params))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andReturn().getResponse().getContentAsString(Charset.forName("UTF8"));


        System.out.println("执行结果 = " + responseString);
    }
}
