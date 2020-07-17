package com.geominfo.mlsql.utils;


import com.geominfo.mlsql.constants.Constants;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: geometry-mlsql
 * @description: 路径处理工具类
 * @author: BJZ
 * @create: 2020-07-10 10:19
 * @version: 1.0.0
 */
@Service
public class PathFunUtil {

    private  String rootPath ;
    private  ArrayList<String> endPath ;

    public PathFunUtil(){}

    public PathFunUtil(String rootPath)
    {
        this.rootPath = rootPath ;
        endPath = new ArrayList<>() ;
        endPath.add(stripSuffix(rootPath , Constants.HTTP_SEPARATED)) ;
    }


    public  PathFunUtil add(String path)
    {
        String curStr = stripSuffix(stripSuffix(path ,Constants.HTTP_SEPARATED) ,Constants.HTTP_SEPARATED) ;
        if(!curStr.isEmpty()){
            endPath.add(curStr) ;
        }
        return this;
    }

    public  PathFunUtil slash(String path){
       return  add(path) ;
    }

    public  String toPath(){
        return mkString(endPath) ;
    }

    public  String mkString(List<String> endPath)
    {
        StringBuilder stringBuilder = new StringBuilder() ;
        int len = endPath.size();
        for(int i = Constants.ZERO ; i < len ; i++){
            stringBuilder.append(endPath.get(i)) ;
            if( i != len - Constants.ONE ){
                stringBuilder .append(Constants.HTTP_SEPARATED) ;
            }
        }
        return stringBuilder.toString() ;
    }


    public  String stripPrefix(String target ,String prefix)
    {
        if(target.startsWith(prefix))
            return target.substring(prefix.length()) ;
        else
           return target ;

    }

    public  String stripSuffix(String target ,String suffix)
    {
        if(target.endsWith(suffix))
            return target.substring(Constants.ZERO , target.length() - suffix.length()) ;
        else
            return target ;

    }



}