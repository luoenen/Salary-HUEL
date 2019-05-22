package com.luosenen.huel.core;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.res.ResourcesCompat;

import com.google.android.material.snackbar.Snackbar;
import com.igexin.sdk.PushManager;
import com.instacart.library.truetime.TrueTime;
import com.instacart.library.truetime.TrueTimeRx;
import com.luosenen.huel.R;
import com.luosenen.huel.push.Push;
import com.luosenen.huel.push.PushService;
import com.luosenen.huel.utils.MyUtils;

import java.io.IOException;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class BookedActivity extends Activity {
    private EditText cookie;
    private Button but_start;
    private TextView showResult;
    private TextView showTimer;
    public static String js, jsCode;
    public static String result;
    public static String floor, seat_X, seat_Y;
    private double count = 0;
    private TextView web, software,teach,google;
    private TextView toKnow;
    private static Date date;
    private static Date goal;
    private static int flag = 0;
    private static Date netDate;
    private static long start,end,total;
    private TextView say;
    private static String childThreadCookie;
    Timer timer = new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booked);
        PushManager.getInstance().initialize(getApplicationContext(), Push.class);
        PushManager.getInstance().registerPushIntentService(getApplicationContext(), PushService.class);
        getInfo();
        init();
        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),WebActivity.class));
            }
        });
        but_start.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (floor.equals("0") || seat_Y.equals("0") || seat_X.equals("0")) {
                    Snackbar snackbar = Snackbar.make(findViewById(R.id.bookedLayout),
                            "您暂未订阅秒选服务，请线联系微信：19939374645订阅服务",
                            Snackbar.LENGTH_SHORT);
                    snackbar.setActionTextColor(Color.WHITE);
                    View mView = snackbar.getView();
                    mView.setBackgroundColor(Color.GRAY);
                    snackbar.show();
                    return;
                }
                String cookies = cookie.getText().toString().trim();
                if(cookies.equals("")){
                    Snackbar snackbar = Snackbar.make(findViewById(R.id.bookedLayout),
                            "Cookie 抓包上方输入框信息必填",
                            Snackbar.LENGTH_SHORT);
                    snackbar.setActionTextColor(Color.WHITE);
                    View mView = snackbar.getView();
                    mView.setBackgroundColor(Color.GRAY);
                    snackbar.show();
                    return;
                }

                try {
                    NetTime nets = new NetTime();
                    Thread threads = new Thread(nets);
                    threads.start();
                    threads.join(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (netDate == null){
                    Snackbar snackbar = Snackbar.make(findViewById(R.id.bookedLayout),
                            "中国科学技术大学NTP授时请求失败，返回重试",
                            Snackbar.LENGTH_SHORT);
                    snackbar.setActionTextColor(Color.WHITE);
                    View mView = snackbar.getView();
                    mView.setBackgroundColor(Color.GRAY);
                    snackbar.show();
                    return;
                }
                Date getTime = netDate;
                Date judgement = new Date();
                judgement.setMinutes(48);
                if(getTime.getMinutes()<judgement.getMinutes()){
                    Snackbar snackbar = Snackbar.make(findViewById(R.id.bookedLayout),
                            "为保证秒选有效，软件判定选座为7:48开放秒选!,请等待到7:48",
                            Snackbar.LENGTH_SHORT);
                    snackbar.setActionTextColor(Color.WHITE);
                    View mView = snackbar.getView();
                    mView.setBackgroundColor(Color.GRAY);
                    snackbar.show();
                    return;
                }
                but_start.setEnabled(false);
                cookie.setFocusable(false);
                cookie.setFocusableInTouchMode(false);
                try {
                    childThreadCookie = cookies;
                    ChildThread child = new ChildThread();
                    Thread thread = new Thread(child);
                    thread.start();
                    thread.join(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                showResult.setText("等待系统获取验证码...");
                if (js == null) {
                    Snackbar snackbar = Snackbar.make(findViewById(R.id.bookedLayout),
                            "验证系统失败，请返回重试",
                            Snackbar.LENGTH_SHORT);
                    snackbar.setActionTextColor(Color.WHITE);
                    View mView = snackbar.getView();
                    mView.setBackgroundColor(Color.GRAY);
                    snackbar.show();
                    but_start.setEnabled(true);
                    cookie.setFocusable(true);
                    cookie.setFocusableInTouchMode(true);
                    showResult.setText("验证系统失败，请返回重试");
                    return;
                }
                Log.i("myjs",js);
                showResult.setText("暂未开始抢座");
                jsCode = MyUtils.cv(js.replace(".js", ""));

                start = System.currentTimeMillis();
                try {
                    NetTime netss = new NetTime();
                    Thread threadss = new Thread(netss);
                    threadss.start();
                    threadss.join(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                end = System.currentTimeMillis();
                total = end-start;
                date= netDate;
                goal = new Date();
                goal.setHours(date.getHours());
                goal.setMinutes(50);
                goal.setSeconds(0);
                count = (((goal.getTime()-date.getTime()))/1000);
                Log.i("count",count+"秒"+goal.getMinutes()+"分"+goal.getSeconds()+"秒"+"-------"+date.getMinutes()+"分"+date.getSeconds());
                timer.schedule(task, 100, 100);
            }
        });
        web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.bilibili.com/video/av44994621/")));
            }
        });
        software.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://raw.githubusercontent.com/luoenen/HUELibraryCourse/master/package.apk")));
            }
        });
        teach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.bilibili.com/video/av50764673/")));
            }
        });
        toKnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder localBuilder = new AlertDialog.Builder(BookedActivity.this);
                localBuilder.setTitle("⚠️选座操作必要须知：");
                localBuilder.setMessage("\t\t\t\t1.在选座开始前1分钟务必完成本操作。\n\t\t\t\t2.选座抓包教程和抓包工具在下方，通过点击查看自主学习，本程序" +
                        "只保障同学们能够按操作选到位置，并非替同学们选座，而是提供计秒选速度，一定要自主学习。\n\t\t\t\t3.抓包后输入上方输入框等待提示" +
                        "信息，提示成功则点上方选座按钮，提示失败则返回重试。\n\t\t\t\t4.切记：在完成上述操作后，请务必等待下方计时结束再启动'微信来选座'，在计时期间，" +
                        "一定不要进入微信来选座公众号，不要刷新页面，否则一定会导致选座失败，没有例外，切记切记。\n\t\t\t\t5.请联系19939374645订阅选座服务，一旦订阅成功，开发者持有最终解释权，如无明显Bug概不退换任何费用，放弃支持则退回客户对应订阅费用。");
                localBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
                    {
                        /**
                         * 确定操作
                         * */
                        Snackbar snackbar = Snackbar.make(findViewById(R.id.bookedLayout),
                                "已知晓",
                                Snackbar.LENGTH_SHORT);
                        snackbar.setActionTextColor(Color.WHITE);
                        View mView = snackbar.getView();
                        mView.setBackgroundColor(Color.GRAY);
                        snackbar.show();
                    }
                });
                localBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
                    {
                        /**
                         * 确定操作
                         * */
                        Snackbar snackbar = Snackbar.make(findViewById(R.id.bookedLayout),
                                "详细反复阅读",
                                Snackbar.LENGTH_SHORT);
                        snackbar.setActionTextColor(Color.WHITE);
                        View mView = snackbar.getView();
                        mView.setBackgroundColor(Color.GRAY);
                        snackbar.show();
                    }
                });

                /***
                 * 设置点击返回键不会消失
                 * */
                localBuilder.setCancelable(false).create();

                localBuilder.show();
            }
        });

    }
    public void init() {
        cookie = findViewById(R.id.weChat_cookie);
        but_start = findViewById(R.id.start);
        showResult = findViewById(R.id.result);
        showTimer = findViewById(R.id.timer);
        web = findViewById(R.id.get_cookie);
        web.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG );
        software = findViewById(R.id.get_software);
        software.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG );
        teach = findViewById(R.id.teach);
        teach.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG );
        toKnow = findViewById(R.id.know);
        toKnow.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG );
        say = findViewById(R.id.mySay);
        Typeface typeface = ResourcesCompat.getFont(this, R.font.font);
        say.setTypeface(typeface);
        google = findViewById(R.id.webLayout);
    }

    public void getJsCode(final String cookie) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    js = MyUtils.getJs(MyUtils.index_url, cookie);
                    Log.i("js",js);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }).start();
    }

    public void booked(final String cookie) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String url = MyUtils.booked_url + floor + "&" + jsCode + "=" + seat_X + "," + seat_Y + "&yzm=";
                    result = MyUtils.netWork(url, cookie);
                    final String results = new String(result.getBytes());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showResult.setText(results);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    TimerTask task = new TimerTask() {

        @Override
        public void run() {
            runOnUiThread(new Runnable() {      // UI thread
                @Override
                public void run() {
                    count =(count-0.1);
                    String cookies = cookie.getText().toString().trim();
                    Date s = new Date();
                    if(s.getMinutes()>=50&&s.getSeconds() == 0&&flag == 0){
                        booked(cookies);
                        flag+=1;
                    }
                    if (count <= 0) {
                        showTimer.setText("请稍后进入来选座结果查看");
                        Log.i("mytime", String.valueOf(new Date()));
                        booked(cookies);
                        Date d = new Date();
                        if (d.getMinutes()>=50){
                            booked(cookies);
                        }else {
                            try {
                                Thread.sleep(500);
                                booked(cookies);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        showResult.setText(result);
                        timer.cancel();
                    }else {
                        showTimer.setText(""+count + "秒");
                    }
                }
            });
        }
    };

    public void getInfo() {
        Intent intent = getIntent();
        floor = intent.getStringExtra("floor");
        seat_X = intent.getStringExtra("seat_X");
        seat_Y = intent.getStringExtra("seat_Y");
    }

    class ChildThread implements Runnable {

        @Override
        public void run() {
            try {
                js = MyUtils.getJs(MyUtils.index_url, childThreadCookie);
                Log.i("js",js);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    class NetTime implements Runnable {

        @Override
        public void run() {
            try {
                TrueTime.build().withNtpHost("time.ustc.edu.cn").initialize();
                netDate = TrueTimeRx.now();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}