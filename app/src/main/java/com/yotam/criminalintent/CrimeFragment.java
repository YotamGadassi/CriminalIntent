package com.yotam.criminalintent;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static android.widget.CompoundButton.OnCheckedChangeListener;

import java.util.UUID;

public class CrimeFragment extends Fragment
{
    private Crime m_crime;

    private EditText m_titleField;
    private Button m_dateButton;
    private CheckBox m_solvedCheckBox;

    private static final String ARG_CRIME_ID = "crime_id";

    public static CrimeFragment newInstance(UUID crime_id)
    {
        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID, crime_id);
        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //UUID crimeId = (UUID)getActivity().getIntent().getSerializableExtra(CrimeActivity.EXTRA_CRIME_ID);
        UUID crimeId = (UUID)getArguments().getSerializable(ARG_CRIME_ID);
        m_crime = CrimeLab.getInstance().getCrime(crimeId);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_crime, container,false);
        setWidgets(view);
        return view;
    }

    private void setWidgets(View view)
    {
        m_titleField = view.findViewById(R.id.crime_title);
        m_titleField.setText(m_crime.GetTitle());
        m_titleField.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                m_crime.SetTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        m_dateButton = view.findViewById(R.id.crime_date);
        m_dateButton.setText(m_crime.GetDate().toString());
        m_dateButton.setEnabled(false);

        m_solvedCheckBox = view.findViewById(R.id.crime_solved);
        m_solvedCheckBox.setChecked(m_crime.IsSolved());
        m_solvedCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                m_crime.SetSolved(isChecked);
            }
        });

    }
}