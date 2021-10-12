package com.devband.stabilawalletforandroid.ui.node;

import com.devband.stabilawalletforandroid.ui.mvp.IView;

public interface NodeView extends IView {

    void displayNodeList(int count);
    void errorNodeList();

}
