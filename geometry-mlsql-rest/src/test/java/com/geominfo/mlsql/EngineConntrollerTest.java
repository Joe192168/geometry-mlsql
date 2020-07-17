package com.geominfo.mlsql;

import com.alibaba.fastjson.JSONObject;
import com.geominfo.mlsql.domain.vo.MLSQLExecInfo;
import com.geominfo.mlsql.domain.vo.MlsqlScriptFile;
import com.geominfo.mlsql.domain.vo.MlsqlUser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
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

    @Autowired
    private MockHttpServletRequest mockHttpServletRequest;


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
        String responseString = mvc.perform(MockMvcRequestBuilders.get("/scriptfile/getcontent")
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .param("id", "72"))
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
    /**
      * description: 权限验证测试用例
      * author: anan
      * date: 2020/6/8
      * param: awh_test1赋权、awh_test2未赋权
      * return:
     */

    @Test
    public void addAuthRunScript() throws Exception {
        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
        String sql = " set user=\"root\";\n" +
                "\n" +
                " connect jdbc where\n" +
                " url=\"jdbc:mysql://10.0.0.165:3306/wow?characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&tinyInt1isBit=false\"\n" +
                " and driver=\"com.mysql.jdbc.Driver\"\n" +
                " and user=\"root\"\n" +
                " and password=\"mysql\"\n" +
                " as db_1;\n" +
                " \n" +
                " load jdbc.`db_1.awh_test2` as table1;\n" +
                "\n" +
                "select * from table1 as output;";
        //sql = "!show resource;";
        params.add("sql",sql);
        params.add("owner","awh@gmail.com");
        //权限验证 false 开启验证；true不开启
        params.add("skipAuth","false");
        params.add("context.__auth_client__","streaming.dsl.auth.meta.client.MLSQLConsoleClient");
        params.add("context.__auth_server_url__","http://10.0.0.152:9002/api_v1/table/auth");
        params.add("context.__auth_secret__","123");



        String responseString = mvc.perform(post("/run/script")
                .contentType(MediaType.APPLICATION_JSON)
                .params(params))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andDo(print())
                .andReturn().getResponse().getContentAsString(Charset.forName("UTF8"));
        System.out.println("执行结果1 = " + responseString);
    }

    /**
      * description: 绑定多个engine，根据策略选择一个engine执行
      * author: anan
      * date: 2020/6/9
      * param:
      * return:
     */

    @Test
    public void strategyEngine() throws Exception {
        String responseString = mvc.perform(MockMvcRequestBuilders.get("/user/login")
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .param("userName", "awh@gmail.com")
                .param("password", "123456"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andReturn().getResponse().getContentAsString(Charset.forName("UTF8"));

        JSONObject resultJson = JSONObject.parseObject(responseString);
        String dataOne = resultJson.get("data").toString();
        JSONObject dataOneValue = JSONObject.parseObject(dataOne);
        JSONObject dataTwo = JSONObject.parseObject(dataOneValue.getString("data"));
        MlsqlUser mlsqlUser = dataTwo.toJavaObject(MlsqlUser.class);
        String backend_tags = mlsqlUser.getBackendTags();

        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
        params.add("sql","select 1 as a,'安静' as b as bbc;");
        params.add("tags",backend_tags);

        responseString = mvc.perform(post("/run/script")
                .contentType(MediaType.APPLICATION_JSON)
                .params(params))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andDo(print())
                .andReturn().getResponse().getContentAsString(Charset.forName("UTF8"));
        System.out.println("执行结果 = " + responseString);
    }
}
