package com.power.generator.code.impl;

import com.power.generator.code.ICodeBuilder;
import com.power.generator.constant.ConstVal;
import com.power.generator.constant.GeneratorConstant;
import com.power.generator.utils.BeetlTemplateUtil;
import com.power.generator.utils.CodeWriteUtil;
import com.power.generator.utils.PathUtil;
import org.beetl.core.Template;

import java.util.HashMap;
import java.util.Map;

/**
 * Description:
 * 主要用于生成一些公用基础代码
 *
 * @author yu 2018/06/29.
 */
public class BaseCodeBuilder implements ICodeBuilder {

    private static final String ERROR_CODE_ENUM = "ErrorCodeEnum";

    private static final String BASE_CONTROLLER = "BaseController";

    private static final String RESULT_UTIL = "ResultUtil";

    private static final String ERROR_CODE = "ErrorCode";

    /**
     *
     */
    private Map<String,String> paths;

    public BaseCodeBuilder(){
        buildPath();
        buildCode();
    }

    @Override
    public void buildPath() {
        String mainPth = getMainPath();
        String javaPath = getJavaSrcPath();
        paths = new HashMap<>();
        String enumPath = PathUtil.joinPath(javaPath,basePackage()+"."+ConstVal.ENUM_PACKAGE);
        String controllerPath = PathUtil.joinPath(javaPath,basePackage()+"."+ConstVal.CONTROLLER);
        String utilPath = PathUtil.joinPath(javaPath,basePackage()+"."+ConstVal.UTIL_PACKAGE);
        String annotationPath = PathUtil.joinPath(javaPath,basePackage()+"."+ConstVal.ANNOTATION_PACKAGE);
        paths.put(ERROR_CODE_ENUM,enumPath);
        paths.put(BASE_CONTROLLER,controllerPath);
        paths.put(RESULT_UTIL,utilPath);
        paths.put(ConstVal.ANNOTATION_PACKAGE,annotationPath);
        PathUtil.mkdirs(paths);

    }

    @Override
    public void buildCode() {
        CodeWriteUtil.writeFileNotAppend(handleTemplates());
    }


    @Override
    public Map<String, String> handleTemplates() {
        //key is path
        Map<String,String> templates = new HashMap<>(2);

        Template baseControllerTpl = BeetlTemplateUtil.getByName("BaseController.btl");
        baseControllerTpl.binding(GeneratorConstant.COMMON_VARIABLE);
        String baseControllerOut = paths.get(BASE_CONTROLLER)+ConstVal.FILE_SEPARATOR+"BaseController.java";
        templates.put(baseControllerOut,baseControllerTpl.render());

        Template errorCodeTpl = BeetlTemplateUtil.getByName("ErrorCode.btl");
        String errorCodeOut = paths.get(ConstVal.ANNOTATION_PACKAGE)+ConstVal.FILE_SEPARATOR+"ErrorCode.java";
        errorCodeTpl.binding(GeneratorConstant.COMMON_VARIABLE);
        templates.put(errorCodeOut,errorCodeTpl.render());

        Template errorCodeEnumTpl = BeetlTemplateUtil.getByName("ErrorCodeEnum.btl");
        errorCodeEnumTpl.binding(GeneratorConstant.COMMON_VARIABLE);
        String errorCodeEnumOut = paths.get(ERROR_CODE_ENUM)+ConstVal.FILE_SEPARATOR+"ErrorCodeEnum.java";
        templates.put(errorCodeEnumOut,errorCodeEnumTpl.render());

        Template resultUtilTpl = BeetlTemplateUtil.getByName("ResultUtil.btl");
        resultUtilTpl.binding(GeneratorConstant.COMMON_VARIABLE);
        String resultUtilOut = paths.get(RESULT_UTIL)+ConstVal.FILE_SEPARATOR+"ResultUtil.java";
        templates.put(resultUtilOut,resultUtilTpl.render());

        return templates;
    }
}
