package io.github.qiangyt.common.misc;

import org.springframework.stereotype.Component;

@Component
public class IdGenerator {
  
  public String newId() {
    return UuidHelper.shortUuid();
  }

}
