
package org.holoeverywhere.app;

import java.util.Map;
import java.util.WeakHashMap;

import org.holoeverywhere.HoloEverywhere;
import org.holoeverywhere.LayoutInflater;
import org.holoeverywhere.internal.WindowDecorView;

import android.content.Context;
import android.util.TypedValue;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

import com.actionbarsherlock.R;
import com.actionbarsherlock.internal.view.menu.ContextMenuDecorView.ContextMenuListenersProvider;
import com.actionbarsherlock.internal.view.menu.ContextMenuItemWrapper;
import com.actionbarsherlock.internal.view.menu.ContextMenuListener;
import com.actionbarsherlock.internal.view.menu.ContextMenuWrapper;
import com.actionbarsherlock.view.ContextMenu;
import com.actionbarsherlock.view.MenuItem;

public class Dialog extends android.app.Dialog implements ContextMenuListener,
        ContextMenuListenersProvider {
    private static final int checkTheme(Context context, int theme) {
        if (theme >= 0x01000000) {
            return theme;
        }
        TypedValue value = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.dialogTheme, value, true);
        if (value.resourceId > 0) {
            return value.resourceId;
        }
        return R.style.Holo_Theme_Dialog;
    }

    private Map<View, ContextMenuListener> mContextMenuListeners;

    public Dialog(Context context) {
        this(context, 0);
    }

    public Dialog(Context context, boolean cancelable,
            OnCancelListener cancelListener) {
        this(context);
        setCancelable(cancelable);
        setOnCancelListener(cancelListener);
    }

    public Dialog(Context context, int theme) {
        super(new ContextThemeWrapperPlus(context, checkTheme(context, theme)),
                checkTheme(context, theme));
        setCancelable(true);
    }

    @Override
    public void addContentView(View view, LayoutParams params) {
        getWindow().addContentView(prepareDecorView(view, params), params);
    }

    @Override
    public ContextMenuListener getContextMenuListener(View view) {
        if (mContextMenuListeners == null) {
            return null;
        }
        return mContextMenuListeners.get(view);
    }

    @Override
    public LayoutInflater getLayoutInflater() {
        return LayoutInflater.from(getContext());
    }

    @Override
    public final boolean onContextItemSelected(android.view.MenuItem item) {
        return onContextItemSelected(new ContextMenuItemWrapper(item));
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item instanceof ContextMenuItemWrapper) {
            return super.onContextItemSelected(((ContextMenuItemWrapper) item)
                    .unwrap());
        }
        return false;
    }

    @Override
    public void onContextMenuClosed(ContextMenu menu) {
        if (menu instanceof ContextMenuWrapper) {
            super.onContextMenuClosed(((ContextMenuWrapper) menu).unwrap());
        }
    }

    @Override
    public final void onContextMenuClosed(Menu menu) {
        if (menu instanceof android.view.ContextMenu) {
            onContextMenuClosed(new ContextMenuWrapper(
                    (android.view.ContextMenu) menu));
        } else {
            super.onContextMenuClosed(menu);
        }
    }

    @Override
    public final void onCreateContextMenu(android.view.ContextMenu menu,
            View view, ContextMenuInfo menuInfo) {
        onCreateContextMenu(new ContextMenuWrapper(menu), view, menuInfo);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view,
            ContextMenuInfo menuInfo) {
        if (menu instanceof ContextMenuWrapper) {
            super.onCreateContextMenu(((ContextMenuWrapper) menu).unwrap(),
                    view, menuInfo);
        }
    }

    public View prepareDecorView(View v) {
        return prepareDecorView(v, null);
    }

    public View prepareDecorView(View v, ViewGroup.LayoutParams params) {
        if (v instanceof WindowDecorView) {
            ((WindowDecorView) v).setProvider(this);
            return v;
        }
        WindowDecorView window = new WindowDecorView(getContext(), v, params);
        window.setProvider(this);
        return window;
    }

    @Override
    public void registerForContextMenu(View view) {
        if (HoloEverywhere.WRAP_TO_NATIVE_CONTEXT_MENU) {
            super.registerForContextMenu(view);
        } else {
            registerForContextMenu(view, this);
        }
    }

    public void registerForContextMenu(View view, ContextMenuListener listener) {
        if (mContextMenuListeners == null) {
            mContextMenuListeners = new WeakHashMap<View, ContextMenuListener>();
        }
        mContextMenuListeners.put(view, listener);
    }

    @Override
    public void setCancelable(boolean flag) {
        super.setCancelable(flag);
        setCanceledOnTouchOutside(flag);
    }

    @Override
    public void setContentView(int layoutResID) {
        setContentView(getLayoutInflater().makeDecorView(layoutResID));
    }

    @Override
    public void setContentView(View view) {
        setContentView(view, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }

    @Override
    public void setContentView(View view, LayoutParams params) {
        getWindow().setContentView(prepareDecorView(view, params), params);
    }

    @Override
    public void unregisterForContextMenu(View view) {
        if (HoloEverywhere.WRAP_TO_NATIVE_CONTEXT_MENU) {
            super.unregisterForContextMenu(view);
        } else {
            if (mContextMenuListeners != null) {
                mContextMenuListeners.remove(view);
            }
        }
    }
}
