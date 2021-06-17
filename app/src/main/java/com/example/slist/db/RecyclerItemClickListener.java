package com.example.slist.db;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

//public class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {
//
//    private RecyclerViewClickListener clickListener;
//
//    private GestureDetector gestureDetector;
//
//
//
//    public RecyclerItemClickListener(Context context, final RecyclerView recyclerView, final RecyclerViewClickListener clickListener) {
//        this.clickListener = clickListener;
//        gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
//            @Override
//            public boolean onSingleTapUp(MotionEvent e) {
//                return true;
//            }
//
//            @Override
//            public void onLongPress(MotionEvent e) {
//                View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
//                if (child != null && clickListener != null) {
//                    clickListener.onLongClick(child, recyclerView.getChildPosition(child));
//                }
//            }
//        });
//}
//
//
//    @Override
//    public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
//        View childView = rv.findChildViewUnder(e.getX(), e.getY());
//        if(childView != null && clickListener != null && gestureDetector.onTouchEvent(e)){
//            clickListener.onClick(childView, rv.getChildAdapterPosition(childView));
//        }
//        return false;
//    }
//
//    @Override
//    public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
//
//    }
//
//    @Override
//    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
//
//    }
//
//}
