package com.geominfo.mlsql.utils

import java.security.MessageDigest

/**
  * program: MLSQL CONSOLE后端接口
  * description: MD5Scala
  * author: anan
  * create: 2020-06-03 14:53
  * version: 1.0.0
  */
object MD5Scala {
    def md5(s: String) = {
    new String(MessageDigest.getInstance("MD5").digest(s.getBytes))
  }
}
