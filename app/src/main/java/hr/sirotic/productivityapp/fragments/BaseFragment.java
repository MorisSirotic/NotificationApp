package hr.sirotic.productivityapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import hr.sirotic.productivityapp.R;


/**
 * A simple {@link Fragment} subclass.
 */
public abstract class BaseFragment extends Fragment {
    private FragmentActivity parent;

    public BaseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        parent = getActivity();

        Toolbar toolbar = parent.findViewById(R.id.toolbar);
        ((AppCompatActivity) parent).setSupportActionBar(toolbar);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_base, container, false);
    }

    public AppCompatActivity getParentActivity() {
        return (AppCompatActivity) parent;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {

        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public <T extends Fragment> void startNewFragment(T t) {
        int id = getParentActivity().findViewById(R.id.fragContainer).getId();
        getParentActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(id, t)
                .commit();
    }

}
