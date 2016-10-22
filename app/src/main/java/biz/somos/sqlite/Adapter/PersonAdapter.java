package biz.somos.sqlite.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.zip.Inflater;

import biz.somos.sqlite.Model.Persona;
import biz.somos.sqlite.R;
import biz.somos.sqlite.database.PersonaDao;

/**
 * Created by sbv23 on 21/10/2016.
 */
public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.PersonViewHolder> implements View.OnClickListener, View.OnLongClickListener{

    public List<Persona> personaList;
    protected View.OnClickListener listener;
    protected View.OnLongClickListener longListener;

    public PersonAdapter(List<Persona> personaList) {
        this.personaList = personaList;
    }

    public void setOnLongClickListener(View.OnLongClickListener longListener){
        this.longListener = longListener;
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.template,parent,false);
        view.setOnClickListener(this);
        view.setOnLongClickListener(this);
        return new PersonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PersonViewHolder holder, int position) {
        holder.txtId.setText(Integer.toString(personaList.get(position).getId()));
        holder.txtFullName.setText(personaList.get(position).getNombre() + " " + personaList.get(position).getApellido());
    }

    @Override
    public int getItemCount() {
        return personaList.size();
    }

    @Override
    public void onClick(View view) {
        if (listener != null){
            listener.onClick(view);
        }
    }

    @Override
    public boolean onLongClick(View view) {
        if (longListener != null){
            longListener.onLongClick(view);
        }
        return false;
    }

    public class PersonViewHolder extends RecyclerView.ViewHolder {

        protected TextView txtId, txtFullName;

        public PersonViewHolder(View itemView) {
            super(itemView);

            txtId = (TextView) itemView.findViewById(R.id.txtId);
            txtFullName = (TextView) itemView.findViewById(R.id.txtFullName);
        }
    }
}
