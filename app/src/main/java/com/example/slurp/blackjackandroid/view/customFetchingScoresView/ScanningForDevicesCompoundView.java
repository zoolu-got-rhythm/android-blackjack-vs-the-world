package com.example.slurp.blackjackandroid.view.customFetchingScoresView;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import com.example.slurp.blackjackandroid.view.BoardCanvasView;

import java.util.Timer;
import java.util.TimerTask;

public class ScanningForDevicesCompoundView extends LinearLayout {
    private SearchForDevicesTextView searchForDevicesView;
    private RippleWhenTouchesCanvas rippleWhenTouchesCanvas;
    private Timer timer;
    private int squareHeightInDp;
    private Context mContext;

    private final int SCAN_BEAM_FULL_CYCLE_TIME_IN_MS = (1000 / SimulatedTapVisual.FPS) * ScanningBeam.MAX_RADIUS;

    private final int SCANNING_BEAMS_TO_SPAWN_PER_CYCLE_IN_MS =
            Math.round(SCAN_BEAM_FULL_CYCLE_TIME_IN_MS / 1.2f); // spawn 1.2 beams per scanning beam cycle


    private ScanningForDevicesCompoundViewListener mScanningForDevicesCompoundViewListener;

    public interface ScanningForDevicesCompoundViewListener{
        void onMountedToViewTree();
    }

    public ScanningForDevicesCompoundView(Context context,
                                          ScanningForDevicesCompoundViewListener
                                                  scanningForDevicesCompoundViewListener) {

        super(context);
        this.mContext = context;
        this.mScanningForDevicesCompoundViewListener = scanningForDevicesCompoundViewListener;

        final float DEVICE_DENSITY_SCALE = getResources().getDisplayMetrics().density; // dpi  0.75, 1.0, 1.5, 2.0

//        this.squareHeightInDp = Math.round(100 * DEVICE_DENSITY_SCALE);

        this.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        this.setOrientation(LinearLayout.HORIZONTAL);

        ViewTreeObserver viewTreeObserver = this.getViewTreeObserver();

        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //remove listener to ensure only one call is made.
                getViewTreeObserver().removeGlobalOnLayoutListener(this);

                squareHeightInDp = getHeight();

                searchForDevicesView =
                        new SearchForDevicesTextView(mContext,
                                "fetching online scores...", "scores",
                                squareHeightInDp);
                addView(searchForDevicesView);

                rippleWhenTouchesCanvas = new RippleWhenTouchesCanvas(mContext, squareHeightInDp, squareHeightInDp);
//        rippleWhenTouchesCanvas.setBackgroundColor(Color.GREEN);
                addView(rippleWhenTouchesCanvas);

                mScanningForDevicesCompoundViewListener.onMountedToViewTree();
            }
        });
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    public void startScan(){
        this.searchForDevicesView.startScan();

        if(this.timer != null)
            this.timer.cancel();
        this.timer = new Timer();
        this.timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                rippleWhenTouchesCanvas.addTap(new ScanningBeam(
                        new Point(squareHeightInDp / 2, squareHeightInDp / 2)));
            }
        }, 0, SCANNING_BEAMS_TO_SPAWN_PER_CYCLE_IN_MS);
    }

    public void stopScan(){
        this.searchForDevicesView.stopScan();

        if(this.timer != null)
            this.timer.cancel();
        this.timer = null;
    }

}
