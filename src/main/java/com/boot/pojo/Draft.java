package com.boot.pojo;

import io.swagger.annotations.ApiModel;

/** @author 游政杰 */
@ApiModel("草稿箱实体类")
public class Draft {

  private int id;
  private String username;
  private String title;
  private String content;
  private String created;
  private String modified;
  private String tags;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getCreated() {
    return created;
  }

  public void setCreated(String created) {
    this.created = created;
  }

  public String getModified() {
    return modified;
  }

  public void setModified(String modified) {
    this.modified = modified;
  }

  public String getTags() {
    return tags;
  }

  public void setTags(String tags) {
    this.tags = tags;
  }

  @Override
  public String toString() {
    return "Draft{"
        + "id="
        + id
        + ", username='"
        + username
        + '\''
        + ", title='"
        + title
        + '\''
        + ", content='"
        + content
        + '\''
        + ", created='"
        + created
        + '\''
        + ", modified='"
        + modified
        + '\''
        + ", tags='"
        + tags
        + '\''
        + '}';
  }
}
