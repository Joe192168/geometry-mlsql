package com.geominfo.mlsql.service.scriptfile;

import com.geominfo.mlsql.domain.vo.FullPathAndScriptFile;
import com.geominfo.mlsql.domain.vo.IDParentID;
import com.geominfo.mlsql.domain.vo.MlsqlScriptFile;



import java.util.List;

/**
 * @program: geometry-mlsql
 * @description: QuillScriptFileService
 * @author: BJZ
 * @create: 2020-11-23 17:41
 * @version: 1.0.0
 */

public interface QuillScriptFileService {

    MlsqlScriptFile findScriptFile(int scriptId ) ;
    String buildFullPath(MlsqlScriptFile scriptFile) ;
    List<FullPathAndScriptFile> findProjectFiles( String owner, String projectName) ;
//    String getFullPath(int id ) ;
//    List<FullPathAndScriptFile> collectPaths(IDParentID file);
   boolean isInPackage(FullPathAndScriptFile currentScriptFile , List<FullPathAndScriptFile> projectFiles);
   String findProjectNameFileIn(int id) ;
   List<IDParentID> buildTree( List<MlsqlScriptFile> scriptFiles) ;
}