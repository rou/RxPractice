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
import rx.functions.Action1;

@RunWith(AndroidJUnit4.class)
@MediumTest
public class BroadcastConfigureTest {

    private BroadcastConfigure mConfigure;

    @Before
    public void setUp() {
        mConfigure = new BroadcastConfigure(InstrumentationRegistry.getContext());
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
        final CountDownLatch latch = new CountDownLatch(1);
        Subscriber<Boolean> subscriber = new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Boolean isMute) {
                latch.countDown();
                Assert.assertEquals(isMute, mConfigure.getIsMute());
                Assert.assertTrue(isMute);
            }
        };
        mConfigure.isMuteObservable().subscribe(subscriber);
        mConfigure.setIsMute(true);
        latch.await();
        mConfigure.isMuteObservable().unsafeSubscribe(subscriber);
    }
}
