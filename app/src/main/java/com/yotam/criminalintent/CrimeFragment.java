package com.yotam.criminalintent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import java.util.Date;
import java.util.UUID;

public class CrimeFragment extends Fragment
{
    public static final String IS_DELETED = "IsCrimeDeleted";
    public static final String DELETED_CRIME_UUID = "DeletedCrimeUuid";

    private Crime m_crime;
    private EditText m_titleField;
    private Button m_dateButton;
    private Button m_reportButton;
    private CheckBox m_solvedCheckBox;

    private ActivityResultLauncher<Intent> m_datePickerResultLauncher;

    private static final String ARG_CRIME_ID = "crime_id";
    private static final String DIALOG_DATE = "DialogDate";

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
        setHasOptionsMenu(true);
        //UUID crimeId = (UUID)getActivity().getIntent().getSerializableExtra(CrimeActivity.EXTRA_CRIME_ID);
        UUID crimeId = (UUID) getArguments().getSerializable(ARG_CRIME_ID);
        m_crime = CrimeLab.getInstance(getActivity()).getCrime(crimeId);

        m_datePickerResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                                                               result ->
                                                               {
                                                                   if (result.getResultCode()
                                                                       == Activity.RESULT_OK)
                                                                   {
                                                                       Date date = (Date) result.getData()
                                                                                                .getExtras()
                                                                                                .getSerializable(DatePickerFragment.DATE_FOR_RESULT_KEY);
                                                                       updateDate(date);
                                                                   }
                                                               });
    }

    @Override
    public void onPause()
    {
        super.onPause();
        CrimeLab.getInstance(getActivity()).updateCrime(m_crime);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_crime, container, false);
        setWidgets(view);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.delete_crime:
            {
                FragmentActivity parentActivity = getActivity();
                Intent parentIntent = parentActivity.getIntent();
                parentIntent.putExtra(IS_DELETED, true);
                parentIntent.putExtra(DELETED_CRIME_UUID, m_crime.GetId());
                parentActivity.setResult(Activity.RESULT_OK, parentIntent);
                parentActivity.finish();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setWidgets(View view)
    {
        m_titleField = view.findViewById(R.id.crime_title);
        m_titleField.setText(m_crime.GetTitle());
        m_titleField.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                m_crime.SetTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s)
            {
            }
        });

        m_dateButton = view.findViewById(R.id.crime_date);
        m_dateButton.setText(m_crime.GetDate().toString());
        m_dateButton.setOnClickListener(v ->
                                        {
                                            Intent datePickerIntent = DatePickerActivity.newIntent(
                                                    getActivity(),
                                                    m_crime.GetDate());
                                            m_datePickerResultLauncher.launch(datePickerIntent);
                                        });

        m_solvedCheckBox = view.findViewById(R.id.crime_solved);
        m_solvedCheckBox.setChecked(m_crime.IsSolved());
        m_solvedCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> m_crime.SetSolved(
                isChecked));

        m_reportButton = view.findViewById(R.id.crime_report);
        m_reportButton.setOnClickListener(v->
        {
            Intent sendReportIntent = new Intent(Intent.ACTION_SEND);
            sendReportIntent.setType("text/plain");
            sendReportIntent.putExtra(Intent.EXTRA_TEXT, getCrimeReport());
            sendReportIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crime_report_subject));
            startActivity(sendReportIntent);
        });

    }

    private void updateDate(Date date)
    {
        m_crime.SetDate(date);
        m_dateButton.setText(m_crime.GetDate().toString());
    }

    private String getCrimeReport()
    {
        int solvedStringId = m_crime.IsSolved() ? R.string.crime_report_solved : R.string.crime_report_unsolved;
        String solvedString = getString(solvedStringId);
        String dateFormat = "EEE, MMM dd";
        String dateString = DateFormat.format(dateFormat, m_crime.GetDate()).toString();
        String suspect = m_crime.GetSuspect();
        int suspectStringId = null == suspect ? R.string.crime_report_no_suspect : R.string.crime_report_suspect;
        suspect = getString(suspectStringId, suspect);

        String report = getString(R.string.crime_report
                , m_crime.GetTitle()
                , dateString
                , solvedString
                , suspect);

        return report;
    }
}