package com.stillwildman.lazyrunner.utilities;

import android.view.Menu;
import android.view.MenuItem;

import com.stillwildman.lazyrunner.R;
import com.stillwildman.lazyrunner.model.MenuActions;

/**
 * Created by vincent.chang on 2017/5/9.
 */

public class MenuHelper implements MenuActions {

    public static void setMenuOptions(Menu menu, int... actions) {
        menu.clear();

        for (int action : actions) {
            switch (action) {
                case ACTION_REMOVE:
                    menu.add(Menu.NONE, action, Menu.NONE, R.string.remove)
                            .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
                    break;
            }
        }
    }

}
