package com.geominfo.mlsql.service.scriptfile.impl;

import com.geominfo.mlsql.domain.vo.FullPathAndScriptFile;
import com.geominfo.mlsql.domain.vo.IDParentID;
import com.geominfo.mlsql.domain.vo.MlsqlScriptFile;
import com.geominfo.mlsql.domain.vo.MlsqlUser;
import com.geominfo.mlsql.service.cluster.BackendService;
import com.geominfo.mlsql.service.scriptfile.QuillScriptFileService;
import com.geominfo.mlsql.service.scriptfile.ScriptFileService;
import com.geominfo.mlsql.service.user.UserService;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.apache.logging.log4j.core.script.ScriptFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.java2d.pipe.DrawImagePipe;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @program: geometry-mlsql
 * @description: QuillScriptFileService
 * @author: BJZ
 * @create: 2020-11-23 17:25
 * @version: 1.0.0
 */
@Service
public class QuillScriptFileServiceImpl implements QuillScriptFileService {

    @Autowired
    private ScriptFileService scriptFileService;

    @Autowired
    private BackendService backendService ;

    @Autowired
    private UserService userService ;

    @Override
    public MlsqlScriptFile findScriptFile(int scriptId) {
        return scriptFileService.getScriptById(scriptId);
    }

    @Override
    public String buildFullPath(MlsqlScriptFile scriptFile) {
        List<String> nameList = new ArrayList<>();
        MlsqlScriptFile item = scriptFile;
        nameList.add(item.getName());
        while (item.getId() != 0 && item.getParentId() != null) {
            item = findScriptFile(item.getParentId());
            nameList.add(item.getName());
        }

        return reverseAndMkString(nameList);
    }

    private List<FullPathAndScriptFile> buffer = new ArrayList() ;

    @Override
    public List<FullPathAndScriptFile> findProjectFiles(String owner, String projectName) {

        MlsqlUser mlsqlUser =  userService.getUserByName(owner) ;

        List<MlsqlScriptFile> scriptFiles =  backendService.findProjectFiles(mlsqlUser.getId()) ;

        Map<Integer , MlsqlScriptFile> idToScriptFile =
                scriptFiles.stream().collect(Collectors.toMap(e -> e.getId(), e -> e));

        List<IDParentID> allDIP = buildTree(scriptFiles) ;

        List<IDParentID> projects = allDIP.isEmpty() ? null : allDIP.get(1).getChildren() ;

        IDParentID targetProject =
                projects.stream().filter(p -> p.getName().equals(projectName)).collect(Collectors.toList()).get(1) ;


        collectPaths(targetProject ,idToScriptFile) ;

        return buffer;
    }



    @Override
    public boolean isInPackage(FullPathAndScriptFile currentScriptFile, List<FullPathAndScriptFile> projectFiles) {

        String[] curFPath = currentScriptFile.getPath().split("/");

        return projectFiles.stream()
                .map(sf -> sf.getPath().split("/"))
                .filter(sfArray -> sfArray[sfArray.length - 1].equals("__init__.py"))
                .map(sfArray ->
                        dropRight(sfArray)).filter(f -> f[f.length - 1].equals(dropRight(curFPath)[curFPath.length - 1]))
                .findFirst()
                .get().length > 0;

//        return false;
    }



    @Override
    public String findProjectNameFileIn(int id) {
        MlsqlScriptFile item = findScriptFile(id);
        String projectName = "";
        while (item.getParentId() != null && item.getParentId() != null) {
            item = findScriptFile(item.getParentId());
            if (item.getParentId() != null && item.getParentId() != null)
                projectName = item.getName();
        }
        return projectName;
    }

    @Override
    public List<IDParentID> buildTree(List<MlsqlScriptFile> scriptFiles) {

        List<IDParentID> item = scriptFiles.stream().map(msf ->
                new IDParentID(msf.getId(), msf.getParentId(),
                        msf.getName(), new ArrayList<>())).collect(Collectors.toList());

        List<IDParentID> ROOTS = new ArrayList<>();
        Map<Object, Integer> tempMap = new ConcurrentHashMap<>();
        Map<IDParentID, Integer> itemsWithIndex = new ConcurrentHashMap<>();


        for (int i = 0; i < item.size(); i++)
            itemsWithIndex.put(item.get(i), i);

        itemsWithIndex.forEach((curItem, index) -> tempMap.put(curItem.getId(), index));

        itemsWithIndex.forEach((curItem, index) -> {
            if (curItem.getId() != null && (int) curItem.getId() != 0) {
                IDParentID curIDP = new IDParentID(
                        curItem.getId(),
                        curItem.getParentID(),
                        curItem.getName(),
                        new ArrayList<>());
                List<IDParentID> curList = new ArrayList<>();
                curList.add(curIDP);
                item.get((int) curItem.getParentID()).setChildren(
                        curList);
            } else {
                ROOTS.add(curItem);
            }
        });

        return ROOTS;
    }

    //--------------- private tool------------------------------
    private  String[] dropRight(String[] tageter) {
        int len = tageter.length;
        String[] cur = new String[len - 1];
        for (int i = 0; i < len - 1; i++)
            cur[i] = tageter[i];
        return cur;
    }

    private String reverseAndMkString(List<String> targeter){

        StringBuffer sf = new StringBuffer() ;

        Collections.reverse(targeter);
        int len = targeter.size() ;
        for(int i = 0 ; i < len ;i++)
        {
            sf.append(targeter.get(i)) ;
            if(i != len -1)
                sf.append("/") ;
        }

        return sf.toString() ;
    }

    private String getFullPath( Map<Integer , MlsqlScriptFile> idToScriptFile,int id) {

        List<String> pathBuffer = new ArrayList() ;
        MlsqlScriptFile item = idToScriptFile.get(id) ;
        pathBuffer.add(item.getName()) ;
        while (item.getParentId() != 0 && item.getParentId() != null){
            item =  idToScriptFile.get(item.getParentId()) ;
            pathBuffer.add(item.getName()) ;
        }

        return reverseAndMkString(pathBuffer);
    }

    private void collectPaths(IDParentID file , Map<Integer , MlsqlScriptFile> idToScriptFile) {

        if(file.getChildren().size() == 0){
            FullPathAndScriptFile fp  = new FullPathAndScriptFile(file.getId().toString(),
                    idToScriptFile.get(Integer.valueOf(file.getId().toString()))
            );
            buffer.add(fp) ;
        }else{
           file.getChildren().forEach(item -> collectPaths(item ,idToScriptFile));
        }
    }

}