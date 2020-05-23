package com.rokid.glass.libbase.message.face;


import com.rokid.glass.libbase.message.dto.FaceInfoBean;
import com.rokid.glass.libbase.message.enums.MessageDirection;

/**
 * Author: Zack
 * Email:  newzzack@gmail.com
 * Date:   2019.08.30 16:02
 */
public class RespOnlineSingleFaceMessage extends FaceMessage {

    public enum ServerErrorCode {
        OK(0, "请求成功"),
        SERVER_INVALID(-1, "服务器不可用"),
        RES_NOT_EXIST(1, "无法访问资源"),
        PARAM_MISSING(2, "缺少必填字段"),
        PARAM_OUT_OF_RANGE(3, "字段输入不符合限制"),
        IMAGE_ERROR(4, "读图错误"),
        NO_FACE_DETECTED(5, "未检测到人脸"),
        NO_FACE_RECOGNIZED(6, "未识别到人脸"),
        FACE_NUM_EXCEEDED_LIMIT(7, "人脸数量超过限制"),
        EXPIRED(9999, "调用服务失败,试用已到期");

        private int code;
        private String desc;

        ServerErrorCode(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        @Override
        public String toString() {
            return "ServerErrorCode{" +
                    "code=" + code +
                    ", desc='" + desc + '\'' +
                    '}';
        }

        public int getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }

    }
    private FaceInfoBean mFaceInfoBean;
    private ServerErrorCode mServerCode;
    private int mTrackId;

    public RespOnlineSingleFaceMessage() {
        super(MessageDirection.MOBILE_TO_GLASS, FaceMessageType.SINGLE_FACE_RECOGNIZE);
    }

    public RespOnlineSingleFaceMessage(FaceInfoBean faceInfoBean, ServerErrorCode serverCode, int trackId) {
        super(MessageDirection.MOBILE_TO_GLASS, FaceMessageType.SINGLE_FACE_RECOGNIZE);
        mFaceInfoBean = faceInfoBean;
        mServerCode = serverCode;
        mTrackId = trackId;
    }

    public FaceInfoBean getFaceInfoBean() {
        return mFaceInfoBean;
    }

    public void setFaceInfoBean(FaceInfoBean faceInfoBean) {
        mFaceInfoBean = faceInfoBean;
    }

    public ServerErrorCode getServerCode() {
        return mServerCode;
    }

    public void setServerCode(ServerErrorCode serverCode) {
        mServerCode = serverCode;
    }

    public int getTrackId() {
        return mTrackId;
    }

    public void setTrackId(int trackId) {
        mTrackId = trackId;
    }

    @Override
    public String toString() {
        return "RespOnlineSingleFaceMessage{" +
                "mFaceInfoBean=" + mFaceInfoBean +
                ", mServerCode=" + mServerCode +
                ", mTrackId=" + mTrackId +
                ", mId=" + mId +
                ", mMessageType=" + mMessageType +
                ", mDirection=" + mDirection +
                ", mServerCode=" + mServerCode +
                '}';
    }
}
