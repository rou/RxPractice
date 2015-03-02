package io.rou.rxpractice.models;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.InstrumentationTestCase;
import android.test.suitebuilder.annotation.MediumTest;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;

import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;

@RunWith(AndroidJUnit4.class)
@MediumTest
public class BroadcastConfigureTest {

    private BroadcastConfigure mConfigure;
    private CountDownLatch mOnceLatch;

    @Before
    public void setUp() {
        mConfigure = new BroadcastConfigure(InstrumentationRegistry.getContext());
        mOnceLatch = new CountDownLatch(1);
    }

    @After
    public void tearDown() {
        mConfigure.reset();
    }

    @Test
    public void getIsMuteのデフォルトバリューはfalse() {
        Assert.assertFalse(mConfigure.getIsMute());
    }

    @Test
    public void setIsMuteでミュートの設定が保存される() {
        mConfigure.setIsMute(true);
        Assert.assertTrue(mConfigure.getIsMute());
    }

    @Test
    public void isMuteの変更が通知される() throws Throwable {
        Subscriber<Boolean> subscriber = new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {
            }
            @Override
            public void onError(Throwable e) {
            }
            @Override
            public void onNext(Boolean isMute) {
                mOnceLatch.countDown();
                Assert.assertEquals(isMute, mConfigure.getIsMute());
                Assert.assertTrue(isMute);
            }
        };
        Subscription subscription = mConfigure.isMuteObservable().subscribe(subscriber);
        mConfigure.setIsMute(true);
        mOnceLatch.await();
        subscription.unsubscribe();
    }

    @Test
    public void isMuteの変更が通知されるのをAction1で受け取る() throws Throwable {
        Subscription subscription = mConfigure.isMuteObservable().subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean isMute) {
                mOnceLatch.countDown();
                Assert.assertEquals(isMute, mConfigure.getIsMute());
                Assert.assertTrue(isMute);
            }
        });
        mConfigure.setIsMute(true);
        mOnceLatch.await();
        subscription.unsubscribe();
    }

    @Test
    public void isMuteの変更が通知されるテストをlambdaで書いてみる() throws Throwable {
        Subscription subscription = mConfigure.isMuteObservable().subscribe(isMute -> {
            mOnceLatch.countDown();
            Assert.assertEquals(isMute, mConfigure.getIsMute());
            Assert.assertTrue(isMute);
        });
        mConfigure.setIsMute(true);
        mOnceLatch.await();
        subscription.unsubscribe();
    }
}
