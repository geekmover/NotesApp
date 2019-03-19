package id.code.notesapp.utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

// Tahapan 1: implements RecyclerView.OnItemTouchListener
// Tahapan 2: Alt + Enter OnItemTouchListener & Memilih 3 method dalam kotak dialog
public class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

    // Tahapan 5: Mendeklarasikan ClickListener & GestureDetector
    private ClickListener clickListener;
    private GestureDetector gestureDetector;

    // Tahpaan 3: Membuat Interface
    public interface ClickListener {
        void onClick(View view, int position);
        void onLongClick(View view, int position);
    }

    // Tahapan 4: Membuat Action Click untuk RecyclerView
    public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
        this.clickListener = clickListener;

        // Tahapan 6: Membuat Gesture Detector dengan SimpleGestureDetector
        gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                View child = recyclerView.findChildViewUnder(e.getX(), e.getY());

                if (child != null && clickListener != null) {
                    clickListener.onLongClick(child, recyclerView.getChildAdapterPosition(child));
                }
            }
        });
    }

    // Tahapan 7: Membuat Intercept onClick
    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        View child = rv.findChildViewUnder(e.getX(), e.getY());

        if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
            clickListener.onClick(child, rv.getChildAdapterPosition(child));
        }

        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}
