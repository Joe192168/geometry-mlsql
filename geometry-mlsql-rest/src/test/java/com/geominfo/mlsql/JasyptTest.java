package com.geominfo.mlsql;

import org.jasypt.util.text.BasicTextEncryptor;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @program: console demo
 * @description:
 * @author: anan
 * @create: 2020-06-02 15:57
 * @version: 1.0.0
 */
@SpringBootTest
public class JasyptTest {
    @Test
    public void testJasypt(){
        BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
        //加密所需的salt(盐)
        textEncryptor.setPassword("swhdg");
        //要加密的数据（数据库的用户名或密码）
        String password = textEncryptor.encrypt("123456");
        System.out.println("password:"+password);
    }
}
