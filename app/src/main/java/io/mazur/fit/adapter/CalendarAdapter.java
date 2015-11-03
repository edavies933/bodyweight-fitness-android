package io.mazur.fit.adapter;

import android.support.v4.view.PagerAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.mazur.fit.R;
import io.mazur.fit.model.ActivityState;
import io.mazur.fit.view.CalendarItemView;
import io.mazur.fit.view.widget.ViewPager;
import rx.Observable;
import rx.subjects.PublishSubject;

public class CalendarAdapter extends PagerAdapter {
    public static final int DEFAULT_POSITION = 60;

    private ViewPager mViewCalendarPager;

    private int mCurrentPosition = DEFAULT_POSITION;

    private final PublishSubject<Integer> mOnPageSelectedSubject = PublishSubject.create();

    public CalendarAdapter(ViewPager viewCalendarPager) {
        super();

        mViewCalendarPager = viewCalendarPager;
        mViewCalendarPager.addOnPageChangeListener(new android.support.v4.view.ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mOnPageSelectedSubject.onNext(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    public Object instantiateItem(ViewGroup viewGroup, int position) {
        final ViewPager viewPager = (ViewPager) viewGroup;

        CalendarItemView calendarItemView = (CalendarItemView) LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.view_calendar_item, viewGroup, false);

        calendarItemView.onCreate(position);
        calendarItemView.onCreateView();

        mOnPageSelectedSubject.subscribe(calendarItemView.getCalendarItemPresenter()::onViewPagerPositionChanged);

        viewPager.addView(calendarItemView);

        return calendarItemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ViewPager viewPager = (ViewPager) container;
        viewPager.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public int getCount() {
        return 120;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public void notifyDataSetChanged() {
        int position = mCurrentPosition;

        super.notifyDataSetChanged();

        mViewCalendarPager.setCurrentItem(position, false);
    }

    public Observable<Integer> getOnPageSelectedSubject() {
        return mOnPageSelectedSubject.asObservable();
    }
}
