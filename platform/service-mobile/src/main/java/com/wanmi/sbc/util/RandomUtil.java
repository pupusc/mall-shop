package com.wanmi.sbc.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/10/18 1:47 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
public class RandomUtil {


    /**
     * 获取随机数量
     * @param rawCount
     * @param backCount
     * @return
     */
    public static Collection<Integer> getRandom(int rawCount, int backCount) {
        if (backCount > rawCount) {
            backCount = rawCount;
        }
        List<Integer> randomList = new ArrayList<>();
        while (randomList.size() < backCount) {
            int i = (int)(Math.random() * rawCount);       //  生成0-100的随机数
            randomList.add(i);
        }
        return randomList;
    }


    /**
     * 获取随机数量
     * @param rawCount
     * @return
     */
    public static Integer getRandom(int rawCount) {
        if (rawCount <= 0) {
            return null;
        }
        return (int)(Math.random() * rawCount);
    }

    public static void main(String[] args) {
        System.out.println(getRandom(10));
    }
}
