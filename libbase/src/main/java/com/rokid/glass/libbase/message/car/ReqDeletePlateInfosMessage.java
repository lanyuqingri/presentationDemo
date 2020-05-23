package com.rokid.glass.libbase.message.car;

import java.util.List;

public class ReqDeletePlateInfosMessage extends ReqPlateInfoMessage {
    private boolean isAll;
    private List<String> plates;
    public ReqDeletePlateInfosMessage(){
        super(PlateInfoType.DELETE_PLATE_INFOS);
    }

    public ReqDeletePlateInfosMessage(boolean isAll,List<String> plates){
        super(PlateInfoType.DELETE_PLATE_INFOS);
        this.plates = plates;
        this.isAll = isAll;
    }

    public List<String> getPlates() {
        return plates;
    }

    public void setPlates(List<String> plates) {
        this.plates = plates;
    }

    public boolean isAll() {
        return isAll;
    }

    public void setAll(boolean all) {
        isAll = all;
    }
}
