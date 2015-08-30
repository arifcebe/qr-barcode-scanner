package com.cebe.barcodescanner;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.zxing.Result;

import me.dm7.barcodescanner.core.IViewFinder;
import me.dm7.barcodescanner.core.ViewFinderView;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class MainActivity extends AppCompatActivity
        implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        mScannerView = new ZXingScannerView(this){
            @Override
            protected IViewFinder createViewFinderView(Context context) {
                return new CustomViewFinderView(context);
            }
        };
        setContentView(mScannerView);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void handleResult(Result result) {
        Toast.makeText(MainActivity.this, "content : "+result.getText().toString()+
                ", format = "+result.getBarcodeFormat().toString(),
                Toast.LENGTH_LONG)
            .show();
    }

    private static class CustomViewFinderView extends ViewFinderView{

        public static final String TRADE_MARK_TEXT = "cebe_ganteng";
        public static final int TRADE_MARK_TEXT_SIZE_SP = 40;
        public final Paint PAINT = new Paint();

        public CustomViewFinderView(Context context) {
            super(context);
            init();
        }

        public CustomViewFinderView(Context context, AttributeSet attrs) {
            super(context, attrs);
            init();
        }

        private void init(){
            PAINT.setColor(Color.BLUE);
            PAINT.setAntiAlias(true);
            float textPixelSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                    TRADE_MARK_TEXT_SIZE_SP,getResources().getDisplayMetrics());
            PAINT.setTextSize(textPixelSize);
        }

        private void drawTradeMark(Canvas canvas){
            Rect framingRect = getFramingRect();
            float tradeMarkTop;
            float tradeMarkLef;

            if(framingRect != null){
                tradeMarkTop = framingRect.bottom + PAINT.getTextSize() + 10;
                tradeMarkLef = framingRect.left;
            }else{
                tradeMarkTop = 10;
                tradeMarkLef = canvas.getHeight() - PAINT.getTextSize() - 10;

            }
            canvas.drawText(TRADE_MARK_TEXT,tradeMarkLef,tradeMarkTop,PAINT);
        }

        @Override
        public void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            drawTradeMark(canvas);
        }
    }

}
