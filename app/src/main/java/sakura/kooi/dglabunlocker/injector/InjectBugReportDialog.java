package sakura.kooi.dglabunlocker.injector;

import android.annotation.SuppressLint;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.core.util.Supplier;

import de.robv.android.xposed.callbacks.XC_InitPackageResources;
import de.robv.android.xposed.callbacks.XC_LayoutInflated;
import sakura.kooi.dglabunlocker.GlobalVariables;

public class InjectBugReportDialog {
    public static void apply(XC_InitPackageResources.InitPackageResourcesParam resparam) {
        resparam.res.hookLayout("com.bjsm.dungeonlab", "layout", "func_select_dialog_layout", new XC_LayoutInflated() {
            @Override
            public void handleLayoutInflated(LayoutInflatedParam liparam) {
                View button = liparam.view.findViewById(liparam.res.getIdentifier("safe_button", "id", "com.bjsm.dungeonlab"));
                button.setOnClickListener(e -> {
                    try {
                        GlobalVariables.showSettingsDialog(button.getContext());
                    } catch (ReflectiveOperationException ex) {
                        Log.e("DgLabUnlocker", "Failed open settings dialog", ex);
                    }
                });
            }
        });
        resparam.res.hookLayout("com.bjsm.dungeonlab", "layout", "bug_dialog_layout", new XC_LayoutInflated() {
            @Override
            public void handleLayoutInflated(LayoutInflatedParam liparam) {
                if (liparam.view instanceof android.support.v7.widget.CardView) {
                    CardView card = (CardView) liparam.view;
                    card.removeAllViews();
                    card.addView(createSettingsPanel(card, liparam));
                } else {
                    Log.e("DgLabUnlocker", "Failed replace BugDialog to module settings");
                }
            }
        });
    }

    @SuppressLint({"UseSwitchCompatOrMaterialCode", "SetTextI18n", "UseCompatLoadingForDrawables"})
    private static View createSettingsPanel(CardView card, XC_LayoutInflated.LayoutInflatedParam param) {
        LinearLayout layout = new LinearLayout(card.getContext());
        layout.setPadding(8, 8, 8, 8);
        layout.setBackground(param.res.getDrawable(param.res.getIdentifier("theme_yellow_rectangle_line_10", "drawable", "com.bjsm.dungeonlab")));
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(((Supplier<View>)() -> {
            TextView view = new TextView(card.getContext());
            view.setText("DG-Lab Unlocker");
            view.setGravity(Gravity.CENTER);
            return view;
        }).get());
        layout.addView(((Supplier<View>)() -> {
            Switch view = new Switch(card.getContext());
            view.setPadding(0, 8, 0, 0);
            view.setText("解除远程最大强度限制");
            view.setOnClickListener(e -> GlobalVariables.unlockRemoteMaxStrength = view.isSelected());
            view.setSelected(GlobalVariables.unlockRemoteMaxStrength);
            return view;
        }).get());
        layout.addView(((Supplier<View>)() -> {
            Switch view = new Switch(card.getContext());
            view.setPadding(0, 8, 0, 0);
            view.setText("拦截强度翻倍BUG");
            view.setOnClickListener(e -> GlobalVariables.fixDoubleBug = view.isSelected());
            view.setSelected(GlobalVariables.fixDoubleBug);
            return view;
        }).get());
        layout.addView(((Supplier<View>)() -> {
            Switch view = new Switch(card.getContext());
            view.setPadding(0, 8, 0, 0);
            view.setText("拦截非法超高强度");
            view.setOnClickListener(e -> GlobalVariables.deviceProtection = view.isSelected());
            view.setSelected(GlobalVariables.deviceProtection);
            return view;
        }).get());
        layout.addView(((Supplier<View>)() -> {
            Switch view = new Switch(card.getContext());
            view.setPadding(0, 8, 0, 0);
            view.setText("强制限制远程最大强度");
            view.setOnClickListener(e -> GlobalVariables.enforceRemoteMaxStrength = view.isSelected());
            view.setSelected(GlobalVariables.enforceRemoteMaxStrength);
            return view;
        }).get());
        layout.addView(((Supplier<View>)() -> {
            Switch view = new Switch(card.getContext());
            view.setPadding(0, 8, 0, 0);
            view.setText("无视远程最大强度限制");
            view.setEnabled(false);
            view.setOnClickListener(e -> GlobalVariables.bypassRemoteMaxStrength = view.isSelected());
            view.setSelected(GlobalVariables.bypassRemoteMaxStrength);
            return view;
        }).get());
        return layout;
    }
}
