package com.base.ad.ui;

import android.databinding.DataBindingUtil;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;

import com.base.ad.BaseActivity;
import com.base.ad.R;
import com.base.ad.SimplexToast;
import com.base.ad.api.ApiClient;
import com.base.ad.api.ZjService;
import com.base.ad.bean.LoginRet;
import com.base.ad.bean.UploadTokenRet;
import com.base.ad.databinding.ActivityTestBinding;

import java.io.IOException;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Response;

public class TestActivity extends BaseActivity {
    private final CompositeDisposable disposables = new CompositeDisposable();
    String TAG ="TestActivity";
    ActivityTestBinding mBinding;
    @Override
    protected int getContentView() {
        return R.layout.activity_test;
    }

    @Override
    protected void setBinding(int layout) {
        mBinding = DataBindingUtil.setContentView(this,layout);
        mBinding.setEmployee(new Employee());
    }

    public class Employee{
        public void onclick(View view){
            SimplexToast.show(TestActivity.this,"click");
            onRunSchedulerExampleButtonClicked();
        }
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        disposables.clear();
    }

    void onRunSchedulerExampleButtonClicked() {
        disposables.add(sampleObservable()
                // Run on a background thread
                .subscribeOn(Schedulers.io())
                // Be notified on the main thread
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<UploadTokenRet>() {
                    @Override public void onComplete() {
                        Log.d(TAG, "onComplete()");
                    }

                    @Override public void onError(Throwable e) {
                        Log.e(TAG, "onError()", e);
                    }

                    @Override public void onNext(UploadTokenRet response) {
                        if(response!=null){
                            Log.d(TAG, "onNext(" + response.getCode() + ")");
                        }
                    }
                }));
    }

    static Observable<UploadTokenRet> sampleObservable() {
        return Observable.defer(new Callable<ObservableSource<? extends UploadTokenRet>>() {
            @Override public ObservableSource<? extends UploadTokenRet> call() throws Exception {
                // Do some long running operation


                ZjService zzService = ApiClient.getCLIENT().create(ZjService.class);
                Call<UploadTokenRet> call = zzService.login("ss","ss","ss");
                UploadTokenRet myContactsRet = null;
                try {
                    Response<UploadTokenRet> retResponse = call.execute();
                    myContactsRet = retResponse.body();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return Observable.just(myContactsRet);
            }
        });
    }
}
