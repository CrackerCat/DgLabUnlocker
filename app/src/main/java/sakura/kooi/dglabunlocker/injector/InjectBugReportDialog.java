package sakura.kooi.dglabunlocker.injector;

import static sakura.kooi.dglabunlocker.utils.ExceptionLogger.withCatch;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.lang.reflect.Field;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import sakura.kooi.dglabunlocker.ui.ConfigurationDialog;
import sakura.kooi.dglabunlocker.variables.InjectPoints;
import sakura.kooi.dglabunlocker.variables.ModuleSettings;

public class InjectBugReportDialog implements IHookPointInjector {
    public void apply(Context context, ClassLoader classLoader) {
        XposedHelpers.findAndHookMethod(InjectPoints.class_FuncSelectDialog, classLoader,
                "onCreate", Bundle.class, new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        withCatch("InjectBugReportDialog FuncSelectDialog", () -> {
                            Log.i("DgLabUnlocker", "SafeButtonHook: replacing onClickListener");
                            Dialog dialog = (Dialog) param.thisObject;
                            Field btnField = dialog.getClass().getDeclaredField("safe_button");
                            btnField.setAccessible(true);
                            View btn = (View) btnField.get(dialog);
                            if (btn == null) {
                                Log.e("DgLabUnlocker", "SettingDialog: Inject failed, button is null");
                                return;
                            }

                            btn.setOnClickListener(e -> withCatch("SettingDialog open", () -> ModuleSettings.showSettingsDialog(btn.getContext())));
                        });
                    }
                });

        XposedHelpers.findAndHookMethod(InjectPoints.class_BugDialog, classLoader,
                "onCreate", Bundle.class, new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) {
                        withCatch("InjectBugReportDialog BugDialog", () -> {
                            Log.i("DgLabUnlocker", "SettingDialog: replacing layout of BugDialog");
                            Dialog dialog = (Dialog) param.thisObject;
                            dialog.setContentView(ConfigurationDialog.createSettingsPanel(dialog.getContext()));
                        });
                    }
                });
    }
}
