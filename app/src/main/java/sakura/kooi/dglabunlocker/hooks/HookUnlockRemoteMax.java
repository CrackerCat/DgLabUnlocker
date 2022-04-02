package sakura.kooi.dglabunlocker.hooks;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import de.robv.android.xposed.XC_MethodHook;

public class HookUnlockRemoteMax {
    public static final HookUnlockRemoteMax INSTANCE = new HookUnlockRemoteMax();

    private HookUnlockRemoteMax() {
    }

    public void unlockRemoteMaxStrength(XC_MethodHook.MethodHookParam param, Context context) throws ReflectiveOperationException {
        Object thisObject = param.thisObject;
        Field fieldChannelA = thisObject.getClass().getDeclaredField("a_channel_strength_range");
        Field fieldChannelB = thisObject.getClass().getDeclaredField("b_channel_strength_range");
        fieldChannelA.setAccessible(true);
        fieldChannelB.setAccessible(true);

        Object barChannelA = fieldChannelA.get(thisObject);
        Object barChannelB = fieldChannelB.get(thisObject);
        if (barChannelA == null || barChannelB == null) {
            Log.e("DgLabUnlocker", "Cannot found progress bar ui");
            return;
        }
        Method setMax = barChannelA.getClass().getDeclaredMethod("setMax", int.class);
        setMax.setAccessible(true);
        setMax.invoke(barChannelA, 276);
        setMax.invoke(barChannelB, 276);
        Toast.makeText(context, "远程最大强度解锁成功", Toast.LENGTH_SHORT).show();
    }
}
