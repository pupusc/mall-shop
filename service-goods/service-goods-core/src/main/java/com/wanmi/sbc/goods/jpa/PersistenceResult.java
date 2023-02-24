package com.wanmi.sbc.goods.jpa;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chenzhen
 */
public class PersistenceResult {

    private List resultList = new ArrayList();
    private int pagenum = 1;
    private int pagesize = 1;
    private int pagecount = 1;	//总页数
    private long total = 0;

    public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

    /**
     * Returns the resultList.
     * 
     * @return List
     */
    public List getResultList() {
        return resultList;
    }

    /**
     * Sets the resultList.
     * 
     * @param resultList
     *            The resultList to set
     */
    public void setResultList(List resultList) {
        this.resultList = resultList;
    }

    /**
     * Returns the pagecount.
     * 
     * @return int
     */
    public int getPagecount() {
        return pagecount;
    }

    /**
     * Returns the pagenum.
     * 
     * @return int
     */
    public int getPagenum() {
        return pagenum;
    }

    /**
     * Returns the pagesize.
     * 
     * @return int
     */
    public int getPagesize() {
        return pagesize;
    }

    /**
     * Sets the pagecount.
     * 
     * @param pagecount
     *            The pagecount to set
     */
    public void setPagecount(int pagecount) {
        this.pagecount = pagecount;
    }

    /**
     * Sets the pagenum.
     * 
     * @param pagenum
     *            The pagenum to set
     */
    public void setPagenum(int pagenum) {
        this.pagenum = pagenum;
    }

    /**
     * Sets the pagesize.
     * 
     * @param pagesize
     *            The pagesize to set
     */
    public void setPagesize(int pagesize) {
        this.pagesize = pagesize;
    }
    
    
}