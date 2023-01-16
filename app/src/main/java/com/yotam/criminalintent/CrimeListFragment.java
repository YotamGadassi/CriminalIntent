package com.yotam.criminalintent;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;

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
            startActivity(intent);
        }
    }

    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder>
    {
        private List<Crime> m_crimeList;

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
    }

    private CrimeAdapter m_crimeAdapter;
    private RecyclerView m_recyclerView;
    private int m_lastPositionClicked;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        updateUI();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);
        m_recyclerView = (RecyclerView) view.findViewById(R.id.crime_recycler_view);
        m_recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();
        return view;
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
            m_crimeAdapter.notifyItemChanged(m_lastPositionClicked);
        }
    }
}
