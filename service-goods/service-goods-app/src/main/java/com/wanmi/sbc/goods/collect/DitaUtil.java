package com.wanmi.sbc.goods.collect;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.wanmi.sbc.goods.jpa.PersistenceResult;

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

    //过滤出指定的key
    //String filters = "id,name";
    //pagesize 5
    //1,5   6,10
    public static PersistenceResult filter(List<Map> list , String filters,int from,int to,int pagesize){

        PersistenceResult pr = new PersistenceResult();

        List ret = new ArrayList();

        for(int i=0; i<list.size(); i++){

            if(from <= (i+1) && (i+1) <= to ){
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

        }

        pr.setResultList(ret);
        pr.setTotal(list.size());
        pr.setPagecount((int) Math.ceil(list.size() / (double) pagesize));
        pr.setPagesize(pagesize);
        return pr;

    }

    public static void main(String[] args) {

        List data = new ArrayList();

        Map map1 = new HashMap();  map1.put("id",1);  map1.put("name","张三1"); map1.put("sex","1"); data.add(map1);
        Map map2 = new HashMap();  map2.put("id",2);  map2.put("name","张三2"); map2.put("xxx","2"); data.add(map2);
        Map map3 = new HashMap();  map3.put("id",3);  map3.put("name","张三3"); map3.put("xxx","3"); data.add(map3);
        Map map4 = new HashMap();  map4.put("id",4);  map4.put("name","张三4"); map4.put("xxx","4"); data.add(map4);
        Map map5 = new HashMap();  map5.put("id",5);  map5.put("name","张三5"); map5.put("xxx","5"); data.add(map5);
        Map map6 = new HashMap();  map6.put("id",6);  map6.put("name","张三6"); map6.put("xxx","6"); data.add(map6);
        Map map7 = new HashMap();  map7.put("id",7);  map7.put("name","张三7"); map7.put("xxx","7"); data.add(map7);

        //String filters = "id,name";
        //List newList = DitaUtil.filter(data,filters);

        String filters = "id,name";
        PersistenceResult pr = DitaUtil.filter(data,filters,1,5,5);

        String json = JSON.toJSONString(pr, SerializerFeature.WriteMapNullValue);

        System.out.println(json);

    }

}