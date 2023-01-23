package com.yotam.criminalintent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.UUID;

public class CrimeListFragment extends Fragment
{
    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private Crime m_crime;
        private TextView m_titleTextView;
        private TextView m_dateTextView;
        private ImageView m_handcuffView;

        public CrimeHolder(LayoutInflater inflater, ViewGroup parent, int layoutId)
        {
            super(inflater.inflate(layoutId, parent, false));
            itemView.setOnClickListener(this);
            m_titleTextView = itemView.findViewById(R.id.crime_title);
            m_dateTextView = itemView.findViewById(R.id.crime_date);
            m_handcuffView = itemView.findViewById(R.id.crime_solved);
        }

        public void Bind(Crime crime)
        {
            m_crime = crime;

            m_titleTextView.setText(crime.GetTitle());
            SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, MMM dd, yyyy");
            m_dateTextView.setText(dateFormat.format(crime.GetDate()));
            m_handcuffView.setVisibility(crime.IsSolved() ? View.VISIBLE : View.GONE);
        }

        @Override
        public void onClick(View v)
        {
            m_lastPositionClicked = getAdapterPosition();
            Intent intent = CrimePagerActivity.newIntent(getActivity(), m_lastPositionClicked);
            m_CrimeFragmentLauncher.launch(intent);
        }
    }

    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder>
    {
        public CrimeAdapter(List<Crime> crimeList)
        {
            m_crimeList = crimeList;
        }

        @NonNull
        @Override
        public CrimeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
        {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            return new CrimeHolder(inflater, parent, R.layout.list_item_crime);
        }

        @Override
        public void onBindViewHolder(@NonNull CrimeHolder holder, int position)
        {
            Crime crime = m_crimeList.get(position);
            holder.Bind(crime);
        }

        @Override
        public int getItemCount()
        {
            return m_crimeList.size();
        }

        public void addCrime(Crime crime)
        {
            m_crimeList.add(crime);
        }

        public void removeCrime(int index)
        {
            m_crimeList.remove(index);
            notifyDataSetChanged();
        }

        private List<Crime> m_crimeList;

    }

    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle";
    private CrimeAdapter m_crimeAdapter;
    private RecyclerView m_recyclerView;
    private int m_lastPositionClicked;
    private boolean m_isSubtitleVisible;

    private ActivityResultLauncher<Intent> m_CrimeFragmentLauncher;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        initCrimeFragmentLauncher();
    }

    private void initCrimeFragmentLauncher()
    {
        m_CrimeFragmentLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult()
                , result ->
                {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        boolean isCrimeDeleted = result.getData().getExtras().getBoolean(CrimeFragment.IS_DELETED, false);
                        if (isCrimeDeleted)
                        {
                            UUID crimeUUID = (UUID) result.getData().getExtras().getSerializable(CrimeFragment.DELETED_CRIME_UUID);
                            if(null != crimeUUID)
                            {
                                removeCrime(crimeUUID);
                            }
                        }
                    }
                });
    }

    @Override
    public void onResume()
    {
        super.onResume();
        updateUI();
        updateSubtitle();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);
        m_recyclerView = (RecyclerView) view.findViewById(R.id.crime_recycler_view);
        m_recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();
        if(null != savedInstanceState)
        {
            m_isSubtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE, false);
        }
        updateSubtitle();
        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE, m_isSubtitleVisible);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater)
    {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragement_crime_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.new_crime:
            {
                Crime crime = new Crime();
                CrimeLab.getInstance().addCrime(crime);
                int crimeIndex = CrimeLab.getInstance().getCrimeIndex(crime.GetId());
                Intent intent = CrimePagerActivity.newIntent(getActivity(), crimeIndex);
                m_crimeAdapter.addCrime(crime);
                m_crimeAdapter.notifyItemInserted(crimeIndex);
                m_lastPositionClicked = crimeIndex;
                m_CrimeFragmentLauncher.launch(intent);
                return true;
            }
            case R.id.show_subtitle:
            {
                m_isSubtitleVisible = !m_isSubtitleVisible;
                updateSubtitle();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateUI()
    {
        if(null == m_crimeAdapter)
        {
            CrimeLab crimeLab = CrimeLab.getInstance();
            m_crimeAdapter = new CrimeAdapter(crimeLab.getCrimes());
            m_recyclerView.setAdapter(m_crimeAdapter);
        }
        else
        {
            m_crimeAdapter.notifyDataSetChanged();
        }
    }

    private void updateSubtitle()
    {
        CrimeLab crimeLab = CrimeLab.getInstance();
        int crimeCount = crimeLab.getCrimes().size();
        String subtitle;
        if(m_isSubtitleVisible)
        {
            subtitle = getString(R.string.subtitle_format, crimeCount);
        }
        else
        {
            subtitle = null;
        }

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        ActionBar actionBar = activity.getSupportActionBar();
        actionBar.setSubtitle(subtitle);
    }

    private void removeCrime(UUID crimeId)
    {
        CrimeLab crimeLab = CrimeLab.getInstance();
        Crime crime = crimeLab.getCrime(crimeId);
        int crimeIndex = crimeLab.getCrimeIndex(crimeId);
        crimeLab.removeCrime(crime);
        m_crimeAdapter.removeCrime(crimeIndex);
    }

}
