# presentationDemo
此工程可基于Rokid(杭州灵伴科技有限公司)二代分体单目眼镜对接华为mate20、华为mate20 pro、华为mate30、华为mate30 pro 等机型手机，做人脸的检测跟踪，然后手机和眼镜端以不同画面呈现结果,同时可做到人脸、车牌的智能识别，车牌的话采用assets目录预置部分车牌mock数据（有对应车牌图片）做离线识别，人脸只做人脸跟踪/检测以及抠图的示例展示。其他开发者可基于此做在线的人脸比对，然后扩展接口将结果展现在眼镜或者手机端。

## 工程模块简介
app   -------------------应用主工程，里边主要集成眼镜端展示的view以及人脸/车牌识别的主流程。

libbase  ----------------基础模块。集成了人脸、车牌识别sdk等  

libusbcamera  --------usb camera 适配模块，连接手机时可以让手机读取连接的眼镜camera的数据，然后传给人脸/车牌做识别

## 眼镜相关信息获取
### 获取眼镜序列号
String GlassControl#GetSerialNumber();

### 眼镜光机亮度 0-100
int GlassControl#GetBrightness();
void GlassControl#SetBrightness();

### Glass按键事件
目前power键进行光机开关设置，返回键默认交给眼镜ui处理，眼镜段不处理则给到重写RokidBaseActivity#onGlassBackPress()函数

## Glass-Touch事件
接收事件位置在RokidBaseActivity#mOnGlassEvent#OnTochEvent() 可自行获取使用
事件值为2（短按）、3（长按）、4（向前滑动）、5（向后滑动）

### Glass传感器事件
#### 前置光线传感器
RokidBaseActivity#OnLsensorUpdate(lux) lux最小值为0

#### 距离传感器
RokidBaseActivity#onPSensorEvent(status) true可认为是带上眼镜 唤醒光机 false可认为已摘下眼镜 熄灭光机


## 可查看glass硬件事件说明
根目录pdf文件