package com.boot.controller.pearAdmin;

import com.alibaba.fastjson.JSON;
import com.boot.annotation.Operation;
import com.boot.data.ResponseData.LayuiData;
import com.boot.data.ResponseData.LayuiJSON;
import com.boot.pojo.Article;
import com.boot.pojo.Draft;
import com.boot.service.DraftService;
import com.boot.service.ArticleService;
import com.boot.service.UserDetailService;
import com.boot.utils.SpringSecurityUtil;
import com.boot.utils.IpUtils;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(path = "/pear")
@Api("我的草稿")
public class DraftController {

  @Autowired private DraftService draftService;

  @Autowired private SpringSecurityUtil springSecurityUtil;

  @Autowired private HttpSession session;

  @Autowired private ArticleService articleService;

  @Autowired private UserDetailService userDetailService;

  private Logger logger = Logger.getLogger(DraftController.class);

  @RequestMapping(path = "/toDraft")
  public String toDraft() {

    return "back/newback/article/draft_list";
  }

  @ResponseBody
  @RequestMapping(path = "/draftData")
  public String draftData(
      @RequestParam(value = "page", defaultValue = "1") int page,
      @RequestParam(value = "limit", defaultValue = "6") int limit,
      @RequestParam(value = "title", defaultValue = "") String title) {

    if(!StringUtils.isBlank(title))
    {
      PageHelper.startPage(page,limit);
      List<Draft> drafts = draftService.selectDraftByTitle(title);
      LayuiData<Draft> draftlayuiData = new LayuiData<>();
      draftlayuiData.setCode(0);
      draftlayuiData.setMsg("");
      draftlayuiData.setData(drafts);
      draftlayuiData.setCount(1);
      return JSON.toJSONString(draftlayuiData);
    }else {
      LayuiData<Draft> data = new LayuiData<Draft>();

      String username = springSecurityUtil.currentUser(session);

      PageHelper.startPage(page, limit);
      List<Draft> drafts = draftService.selectAllDraft(username);

      int count = draftService.selectDraftCount(username);

      data.setMsg("");
      data.setCode(0);
      data.setData(drafts);
      data.setCount(count);

      return JSON.toJSONString(data);
    }

  }

  @Operation("进入编辑草稿页面")
  @RequestMapping(path = "/toChangeDraft")
  public String toChangeDraft(
      int draftid, Model model, HttpSession session, HttpServletRequest request) {

    Draft draft = draftService.selectDraftByID(draftid);

    model.addAttribute("contents", draft);
    //    userDetail userDetail = userDetailService.selectUserDetailByUserName(username);
    //    model.addAttribute("userDetail", userDetail);
    return "back/newback/article/draft_edit";
  }

  @Operation("发布草稿")
  @RequestMapping(path = "/draft/publish")
  @ResponseBody
  public String publish(
      Article article,
      @RequestParam(value = "editArticleId", defaultValue = "-1") int editArticleId,
      HttpSession session,
      HttpServletRequest request) {

    LayuiJSON json = new LayuiJSON(); // 封装json数据传入前台

    if (editArticleId == -1) { // 说明不能发布

      json.setSuccess(false);
      json.setMsg("发布失败");
      return JSON.toJSONString(json);
    } else {

      try {
        // 发布操作代码
        draftService.publishDraft(article, editArticleId);

        // 打印日志操作
        String username = springSecurityUtil.currentUser(session);
        java.util.Date date2 = new java.util.Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = simpleDateFormat.format(date2);
        String ipAddr = IpUtils.getIpAddr(request);
        json.setSuccess(true);
        json.setMsg("发布成功");
        logger.debug(time + "   用户名：" + username + "发布成功,ip为：" + ipAddr);

      } catch (Exception e) {
        /**
         * 在publishDraft方法中，因为操作数据库的代码都在操作redis的上面 所以当操作数据库的代码报错，会立刻进行回滚，所以我们大可不用担心数据库的错误
         * redis的语句如果报错则也会触发数据库的回滚，并且redis也执行不成功 所以我们不用在controller层进行数据的恢复。
         */
        e.printStackTrace();
        json.setSuccess(false);
        json.setMsg("发布失败");
      }

      return JSON.toJSONString(json);
    }
  }

  @Operation("保存草稿")
  @RequestMapping(path = "/modifyDraft")
  @ResponseBody // 要加
  @ApiOperation(value = "保存草稿")
  public String modify(
      String content,
      @RequestParam(value = "editArticleId", defaultValue = "-99") int editArticleId,
      Draft draft,
      HttpSession session,
      HttpServletRequest request) {
    LayuiJSON json = new LayuiJSON();

    if (editArticleId == -99) { // =-99说明不能修改
      json.setSuccess(false);
      json.setMsg("保存失败");
      return JSON.toJSONString(json);
    } else { // 可修改

      try {
        draft.setId(editArticleId);
        Date date = new Date();
        java.sql.Date date1 = new java.sql.Date(date.getTime());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String modifyTime = simpleDateFormat.format(date1);
        draft.setModified(modifyTime);
        if (draft.getTitle() == null) {
          draft.setTitle("");
        }
        if (draft.getContent() == null) {
          draft.setContent("");
        }
        if (draft.getTags() == null) {
          draft.setTags("");
        }
        draftService.modifyDraft(draft);
        json.setSuccess(true);
        json.setMsg("保存成功");
        return JSON.toJSONString(json);
      } catch (Exception e) {
        e.printStackTrace();
        json.setSuccess(false);
        json.setMsg("保存失败");
        return JSON.toJSONString(json);
      }
    }
  }


  //放入草稿
  @Operation("放入草稿")
  @ResponseBody
  @RequestMapping(path = "/add/draft")
  public String addDraft(Draft draft){

    LayuiJSON json=new LayuiJSON();

    try{
      String username = springSecurityUtil.currentUser(session);
      draft.setUsername(username);
      Date date = new Date();
      java.sql.Date date1 = new java.sql.Date(date.getTime());
      SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      String createdTime = simpleDateFormat.format(date1);
      draft.setCreated(createdTime);
      if (draft.getTitle() == null) {
        draft.setTitle("");
      }
      if (draft.getContent() == null) {
        draft.setContent("");
      }
      if (draft.getTags() == null) {
        draft.setTags("");
      }
      draftService.addDraft(draft);

      json.setMsg("放入草稿箱成功");
      json.setSuccess(true);
    }catch (Exception e){
      e.printStackTrace();
      json.setMsg("放入草稿箱失败");
      json.setSuccess(false);
    }

    return JSON.toJSONString(json);
  }


  @ResponseBody
  @RequestMapping(path = "/del/draft/{id}")
  public String deleteDraft(@PathVariable("id") int id){

    LayuiJSON json=new LayuiJSON();

    try{
      draftService.deleteDraftByID(id);
      json.setSuccess(true);
      json.setMsg("删除成功");
    }catch (Exception e){
      e.printStackTrace();
      json.setSuccess(false);
      json.setMsg("删除失败");
    }
    return JSON.toJSONString(json);

  }

  @ResponseBody
  @RequestMapping(path = "/batchDel/draft/{ids}")
  public String batchDeleteDraft(@PathVariable("ids") String ids){

    LayuiJSON json=new LayuiJSON();

    try{
      String[] split = ids.split(",");
      for (String s : split) {
        draftService.deleteDraftByID(Integer.parseInt(s));
      }
      json.setSuccess(true);
      json.setMsg("删除成功");
    }catch (Exception e){
      e.printStackTrace();
      json.setSuccess(false);
      json.setMsg("删除失败");
    }
    return JSON.toJSONString(json);

  }

}
