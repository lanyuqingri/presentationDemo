package com.rokid.glass.libbase.message.car;

public class ReqQueryPlateInfosMessage extends ReqPlateInfoMessage {
    private boolean isAll;
    private int pageNum = 1;
    private int pageSize = 10;
    private String searchKey;
    public ReqQueryPlateInfosMessage(){
        super(PlateInfoType.QUERY_PLATE_INFO);
    }

    public ReqQueryPlateInfosMessage(boolean isAll){
        super(PlateInfoType.QUERY_PLATE_INFO);
        this.isAll = isAll;
    }

    public boolean isAll() {
        return isAll;
    }

    public void setAll(boolean all) {
        isAll = all;
    }

    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
