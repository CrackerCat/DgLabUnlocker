package sakura.kooi.dglabunlocker.injector;

import static sakura.kooi.dglabunlocker.utils.ExceptionLogger.withCatch;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import sakura.kooi.dglabunlocker.hooks.HookEnforceLocalStrength;
import sakura.kooi.dglabunlocker.variables.InjectPoints;
import sakura.kooi.dglabunlocker.variables.ModuleSettings;

public class InjectControlledStrengthButton implements IHookPointInjector {
    public void apply(Context context, ClassLoader classLoader) {
        InjectPoints.class_StrengthTouchListeners.forEach(classTouchListener -> {
            XposedHelpers.findAndHookMethod(classTouchListener, classLoader,
                    "onTouch", View.class, MotionEvent.class, new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) {
                            withCatch("HookEnforceLocalStrength", () -> {
                                if (ModuleSettings.enforceLocalStrength)
                                    HookEnforceLocalStrength.INSTANCE.handleLocalStrengthChange(context);
                            });
                        }
                    });
        });
    }
}
