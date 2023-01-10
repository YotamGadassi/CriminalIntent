package com.yotam.criminalintent;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CrimeListFragment extends Fragment
{
    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private Crime m_crime;
        private TextView m_titleTextView;
        private TextView m_dateTextView;

        public CrimeHolder(LayoutInflater inflater, ViewGroup parent, int layoutId)
        {
            super(inflater.inflate(layoutId, parent, false));
            itemView.setOnClickListener(this);
            m_titleTextView = itemView.findViewById(R.id.crime_title);
            m_dateTextView = itemView.findViewById(R.id.crime_date);
            if(layoutId == R.layout.list_item_crime_required_police)
            {
                Button callPoliceButton = itemView.findViewById(R.id.police_call);
                callPoliceButton.setOnClickListener(v -> Toast.makeText(getActivity(),"Called Police",Toast.LENGTH_SHORT).show());
            }
        }

        public void Bind(Crime crime)
        {
            m_crime = crime;

            m_titleTextView.setText(crime.GetTitle());
            m_dateTextView.setText(crime.GetDate().toString());
        }

        @Override
        public void onClick(View v)
        {
            Toast.makeText(getActivity(),m_crime.GetTitle() + " clicked!", Toast.LENGTH_SHORT).show();
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

            return new CrimeHolder(inflater, parent, viewType);
        }

        @Override
        public int getItemViewType(int position)
        {
            Crime currCrime = m_crimeList.get(position);
            if(currCrime.isRequiredPolice())
            {
                return R.layout.list_item_crime_required_police;
            }

            return R.layout.list_item_crime;
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
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
        CrimeLab crimeLab = CrimeLab.getInstance();
        m_crimeAdapter = new CrimeAdapter(crimeLab.getCrimes());
        m_recyclerView.setAdapter(m_crimeAdapter);
    }
}
