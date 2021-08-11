package com.boot;

import cn.hutool.core.collection.ConcurrentHashSet;
import com.boot.utils.SnowId;
import com.github.yitter.contract.IdGeneratorOptions;
import com.github.yitter.idgen.YitIdHelper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;

@SpringBootTest
public class SnowTest {

//    private IdGeneratorOptions options = new IdGeneratorOptions((short) 1);


  /** 雪花漂移算法Test */
  @Test
  public void snowTest() {
      ConcurrentHashSet<Object> sets = new ConcurrentHashSet<>(); //存放雪花漂移算法生成分布式id
//      options.SeqBitLength=10;
//      YitIdHelper.setIdGenerator(options);
      //模拟并发
      long start = System.currentTimeMillis();
      for (int i = 0; i < 1000000; i++) {
//          long id = YitIdHelper.nextId();

          long id = SnowId.nextId();
          sets.add(id);
//            System.out.println(id);
    }
      long end = System.currentTimeMillis();

      System.out.println("总共耗时："+(end-start)+"ms");

      System.out.println(sets.size()); //如果大小=并发量（也就是循环的次数）就说明成功
  }
}
