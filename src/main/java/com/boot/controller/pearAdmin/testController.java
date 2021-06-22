package com.boot.controller.pearAdmin;

import io.swagger.annotations.Api;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Api("调试接入新的后台管理系统控制器_仅仅是调试专用")
@Controller
@RequestMapping(path = "/pear")
public class testController {

    @RequestMapping(path = "/admin")
    public String toAdmin(){



        return "back/newback/index";
    }
    @RequestMapping(path = "/login")
    public String tologin(){


        return "back/newback/login";
    }

    @RequestMapping(path = "/console1")
    public String toconsole1(){


        return "back/newback/console/console1";
    }

    @RequestMapping(path = "/console2")
    public String toconsole2(){


        return "back/newback/console/console2";
    }

    @RequestMapping(path = "/toperson")
    public String toPerson(){


        return "back/newback/system/person";
    }


}
