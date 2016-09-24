package spcstudent.android.personaltrainermanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.UUID;

public class FragmentSessionList extends Fragment{
    private RecyclerView mSessionRecyclerView;
    private SessionAdapter mAdapter;
    private UUID customerId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_session_list, container, false);
        setRetainInstance(true);

        customerId = (UUID) getArguments().getSerializable(ApplicationKeys.CUSTOMER_ID);

        mSessionRecyclerView = (RecyclerView) view.findViewById(R.id.sessions_list_fragment);
        mSessionRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return view;
    }

    private void updateUI() {
        SessionList sessionList = SessionList.get(getActivity());
        List<Session> sessions = sessionList.getSessions(customerId);
        mAdapter = new SessionAdapter(sessions);
        mSessionRecyclerView.setAdapter(mAdapter);
    }

    // ViewHolder for customer list recycler view
    private class SessionHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        private TextView mNameTextView;
        private CheckBox mCompletedCheckbox;
        private Session mSession;

        public SessionHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            mNameTextView = (TextView) v.findViewById(R.id.session_name_textview);
            mCompletedCheckbox = (CheckBox) v.findViewById(R.id.session_completed_checkbox);
        }

        public void bindSession(Session s) {
            mSession = s;
            mNameTextView.setText(mSession.getSessionName());
            mCompletedCheckbox.setChecked(mSession.isSessionCompleted());
            mCompletedCheckbox.setClickable(false);
        }

        @Override
        public void onClick(View v) {
            Bundle toEditSession = new Bundle();
            toEditSession.putSerializable(ApplicationKeys.CUSTOMER_ID, customerId);
            toEditSession.putSerializable(ApplicationKeys.SESSION_ID, mSession.getSessionId());

            Intent myIntent = new Intent(getActivity(), ActivityEditSession.class);
            myIntent.putExtras(toEditSession);
            getActivity().startActivity(myIntent);
        }
    }

    // Adapter for customer list recycler view
    private class SessionAdapter extends RecyclerView.Adapter<SessionHolder> {
        private List<Session> mSessions;

        public SessionAdapter(List<Session> sessions) {
            mSessions = sessions;
        }

        @Override
        public SessionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.listitem_session, parent, false);
            return new SessionHolder(view);
        }

        @Override
        public void onBindViewHolder(SessionHolder holder, int position) {
            Session s = mSessions.get(position);
            holder.bindSession(s);
        }

        @Override
        public int getItemCount() {
            return mSessions.size();
        }
    }
}