package com.rokid.glass.libbase.message;


import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({MessagePriority.Low, MessagePriority.Normal, MessagePriority.High})
@Retention(RetentionPolicy.SOURCE)
public @interface MessagePriority {
    int Low = 0;
    int Normal = 1;
    int High = 2;
}