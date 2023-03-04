package com.wanmi.sbc.goods.collect;

import java.text.SimpleDateFormat;

import java.util.*;


/**
 * @author chenzhen
 */
public class DitaUtil {

    /**
     * 得到yyyy-MM-dd HH:mm:ss<br>
     * 例如：return 2007-05:03 25:03:19<br>
     */
    public static String getCurrentAllDate() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 小写的hh取得12小时，大写的HH取的是24小时
        Date date = new Date();
        return df.format(date);
    }

    /**
     * yyyy-MM-dd HH:mm:ss 天数+-
     * @return
     */
    public static String dayDiff(String datetime,int days){

        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try
        {
            date = format.parse(datetime);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        if (date==null) return "";
        Calendar cal=Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH,days);
        date=cal.getTime();
        //System.out.println("3 days after(or before) is "+format.format(date));
        cal=null;
        return format.format(date);
    }

    public static boolean isBlank(String strIn) {
        if ((strIn == null) || (strIn.trim().equals("")) || (strIn.toLowerCase().equals("null")) || strIn.length() == 0) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isNotBlank(String strIn) {
        if ((strIn == null) || (strIn.trim().equals("")) || (strIn.toLowerCase().equals("null")) || strIn.length() == 0) {
            return false;
        } else {
            return true;
        }
    }

    //过滤出指定的key
    //String filters = "id,name";
    public static List<Map> filter(List<Map> list , String filters){

        List ret = new ArrayList();

        for(int i=0;i<list.size();i++){

            Map newMap = new HashMap();

            Map<String,Object> map = (Map)list.get(i);

            Set set = map.keySet();
            Iterator iterator = set.iterator();
            while (iterator.hasNext()){
                Object next = iterator.next();
                //System.out.println("key为："+next+",value为："+map.get(next));
                if(filters.contains(next.toString())){
                    newMap.put(next,map.get(next));
                }
            }

            if(newMap.size() > 0){
                ret.add(newMap);
            }

        }

        return ret;

    }

    public static void main(String[] args) {

        List data = new ArrayList();

        Map map1 = new HashMap();
        map1.put("id",1);
        map1.put("name","张三1");
        map1.put("sex","1");

        data.add(map1);

        Map map2 = new HashMap();
        map2.put("id",2);
        map2.put("name","张三2");
        map2.put("xxx","2");
        data.add(map2);

        String filters = "id,name";

        List newList = DitaUtil.filter(data,filters);

        System.out.println(newList);

    }

}