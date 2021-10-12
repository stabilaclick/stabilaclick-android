package com.devband.stabilawalletforandroid.ui.node.adapter;

public interface AdapterImmutableDataModel<W,O> {
    O getModel(int position);
    int getSize();
    void setModelList(W w);
    void clear();
}
