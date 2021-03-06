package com.devband.stabilawalletforandroid.ui.node;

import com.devband.stabilawalletforandroid.ui.mvp.BasePresenter;
import com.devband.stabilawalletforandroid.ui.node.adapter.AdapterImmutableDataModel;
import com.devband.stabilawalletforandroid.rxjava.RxJavaSchedulers;
import com.devband.stabilawalletforandroid.stabila.Stabila;

import org.stabila.api.GrpcAPI;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

public class NodePresenter extends BasePresenter<NodeView> {

    private AdapterImmutableDataModel<GrpcAPI.NodeList,GrpcAPI.Node> adapterDataModel;
    private Stabila mStabila;
    private RxJavaSchedulers mRxJavaSchedulers;

    public NodePresenter(NodeView view, Stabila stabila, RxJavaSchedulers rxJavaSchedulers) {
        super(view);
        this.mStabila = stabila;
        this.mRxJavaSchedulers = rxJavaSchedulers;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onDestroy() {

    }

    public void setAdapterDataModel(AdapterImmutableDataModel<GrpcAPI.NodeList,GrpcAPI.Node> adapterDataModel) {
        this.adapterDataModel = adapterDataModel;
    }


    public void getStabilaNodeList(){
        mStabila.getNodeList()
        .subscribeOn(mRxJavaSchedulers.getIo())
        .observeOn(mRxJavaSchedulers.getMainThread())
        .subscribe(new SingleObserver<GrpcAPI.NodeList>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(GrpcAPI.NodeList nodeList) {
                adapterDataModel.setModelList(nodeList);
                mView.displayNodeList(nodeList.getNodesCount());
            }

            @Override
            public void onError(Throwable e) {
                mView.errorNodeList();
                adapterDataModel.clear();
            }
        });
    }
}
