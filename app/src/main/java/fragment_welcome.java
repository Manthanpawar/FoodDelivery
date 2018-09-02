package foodxpress.foodxpress;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toolbar;


/**
 * A simple {@link Fragment} subclass.
 */
public class fragment_welcome extends Fragment {
    private ViewPager viewPager;
    private SectionpagerAdapter sectionpagerAdapter;
    private TabLayout tabLayout;
    public fragment_welcome() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fragment_welcome, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewPager=(ViewPager)view.findViewById(R.id.tabPager);
        sectionpagerAdapter=new SectionpagerAdapter(getFragmentManager());
        viewPager.setAdapter(sectionpagerAdapter);
        tabLayout=(TabLayout)view.findViewById(R.id.maintabs);
        tabLayout.setupWithViewPager(viewPager);
        getActivity().setTitle("Food Xpress");
    }
}
