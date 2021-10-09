package com.boot.controller.pearAdmin;

// import com.boot.config.GenerateProperties;
import com.alibaba.fastjson.JSON;
import com.boot.config.GenerateProperties;
import com.boot.constant.Constant;
import com.boot.data.ResponseData.layuiJSON;
import com.boot.pojo.Code;
import com.boot.service.GenerateModelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller("PearGenerateController")
@RequestMapping(path = "/pear")
@CrossOrigin
@Slf4j
public class GenerateController {

  @Autowired
  private GenerateProperties generateProperties;

  @Autowired
  private GenerateModelService generateModelService;

  @GetMapping(path = "/togenerate")
  public String toGeneratePage(Model model) {
    Code code = new Code();
    log.info("加载配置文件内容......");
    code.setGenerateModel(generateProperties.getGenerateModel());
    code.setModelSerialize(generateProperties.getModelSerialize());
    code.setModelGetterAndSetter(generateProperties.getModelGetterAndSetter());
    code.setModelConstructor(generateProperties.getModelConstructor());
    code.setGeneratePackage(generateProperties.getGeneratePackage());
    code.setGenerateModelPath(generateProperties.getGenerateModelPath());
    code.setGenerateDatabase(generateProperties.getGenerateDatabase());
    code.setDatabaseHost(generateProperties.getDatabaseHost());
    code.setDatabasePort(generateProperties.getDatabasePort());
    code.setDatabaseUser(generateProperties.getDatabaseUser());
    code.setDatabasePassword(generateProperties.getDatabasePassword());
    code.setDatabaseDriver(generateProperties.getDatabaseDriver());
    code.setGenerateTable(generateProperties.getGenerateTable());
    code.setGenerateMapper(generateProperties.getGenerateMapper());
    code.setGenerateServiceAndImpl(generateProperties.getGenerateServiceAndImpl());
    log.info("加载完成......");

    model.addAttribute("codeconfig", code);

    return "back/newback/article/autoCode";
  }

  @PostMapping(path = "/generate")
  @ResponseBody
  public String generate(Code code) {

    System.out.println(code);

    layuiJSON json = new layuiJSON();
    boolean res = generateModelService.generate(code);
    if (res) // 此时执行成功
    {
      json.setMsg("生成代码成功");
      json.setSuccess(true);
    } else {
        json.setMsg("生成代码失败");
        json.setSuccess(false);
    }
    return JSON.toJSONString(json);
  }
}
