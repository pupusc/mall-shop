package com.wanmi.sbc.util;

import java.util.Collection;
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
        Set<Integer> randomSet = new TreeSet<>();
        while (randomSet.size() < backCount) {
            int i = (int)(Math.random() * rawCount);       //  生成0-100的随机数
            randomSet.add(i);
        }
        return randomSet;
    }

    public static void main(String[] args) {
        System.out.println(getRandom(20, 10));
    }
}
