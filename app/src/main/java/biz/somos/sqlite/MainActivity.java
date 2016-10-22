package biz.somos.sqlite;

        import android.content.DialogInterface;
        import android.support.v7.app.AlertDialog;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.support.v7.widget.DefaultItemAnimator;
        import android.support.v7.widget.LinearLayoutManager;
        import android.support.v7.widget.RecyclerView;
        import android.util.Log;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.Toast;

        import java.util.List;

        import biz.somos.sqlite.Adapter.PersonAdapter;
        import biz.somos.sqlite.Model.Persona;
        import biz.somos.sqlite.database.PersonaDao;

public class MainActivity extends AppCompatActivity implements Button.OnClickListener {

    protected RecyclerView list;
    protected RecyclerView.Adapter adapter;
    protected RecyclerView.LayoutManager layoutManager;
    protected PersonAdapter personAdapter;
    protected List<Persona> personaList;
    protected EditText edtId, edtName, edtLastName, edtEmail;
    protected Button btnSave;
    protected PersonaDao db_dao;
    protected int SaveEdit = 0;
    protected int idPerson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db_dao = new PersonaDao(this);

        getPerson();

        edtId = (EditText) findViewById(R.id.edtId);
        edtName = (EditText) findViewById(R.id.edtName);
        edtLastName = (EditText) findViewById(R.id.edtLastName);
        edtEmail = (EditText) findViewById(R.id.edtEmail);

        btnSave = (Button) findViewById(R.id.btnSave);

        btnSave.setOnClickListener(this);

        list = (RecyclerView) findViewById(R.id.list);

        layoutManager = new LinearLayoutManager(this);
        list.setLayoutManager(layoutManager);

        personAdapter = new PersonAdapter(personaList);

        personAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = list.getChildAdapterPosition(view);
                int idPersona = personaList.get(position).getId();
                Persona persona = db_dao.findById(idPersona);
                if (persona != null){
                    idPerson = persona.getId();
                    edtId.setText(persona.getIdentificacion());
                    edtName.setText(persona.getNombre());
                    edtLastName.setText(persona.getApellido());
                    edtEmail.setText(persona.getEmail());
                    SaveEdit = 1;
                }else{
                    Toast.makeText(getBaseContext(), "No se ha encontrado la información solicitada", Toast.LENGTH_LONG).show();
                }
            }
        });

        personAdapter.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Log.i("LongClickListener", "OK");
                int position = list.getChildAdapterPosition(view);
                idPerson = personaList.get(position).getId();

                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                alertDialog.setTitle("Eliminar");
                alertDialog.setMessage("¿Esta seguro que desea eliminar el registro?");
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (db_dao.deletePersona(idPerson) != 0){
                            cleanForm();
                            getPerson();
                            personAdapter.personaList = personaList;
                            adapter.notifyDataSetChanged();
                            Toast.makeText(getBaseContext(), "Se ha eliminado el registro", Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(getBaseContext(), "No se ha podido eliminar el registro", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                alertDialog.show();


                return false;
            }
        });

        adapter = personAdapter;
        list.setAdapter(adapter);

        list.setItemAnimator(new DefaultItemAnimator());
    }

    protected void getPerson (){
        personaList = db_dao.getAllPersona();
    }

    @Override
    public void onClick(View view) {
        Persona persona = new Persona();
        String identification, nombre, email;
        identification = edtId.getText().toString().trim();
        nombre = edtName.getText().toString().trim();
        email = edtEmail.getText().toString().trim();
        if (identification.matches("") || nombre.matches("") || email.matches("")){
            Toast.makeText(this,"Por favor complete los campo", Toast.LENGTH_LONG).show();
        }else{
            persona.setIdentificacion(identification);
            persona.setNombre(nombre);
            persona.setEmail(email);
            persona.setApellido(edtLastName.getText().toString().trim());

            if (SaveEdit == 0) {
                //guardar nueva persona
                if (db_dao.insertPersona(persona) != 0) {
                    cleanForm();
                    Toast.makeText(this, "Se inserto correctamente la persona", Toast.LENGTH_LONG).show();
                    getPerson();
                    personAdapter.personaList = this.personaList;
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(this, "No se pudo insertar la persona", Toast.LENGTH_LONG).show();
                }
            }else{
                //actualizar persona
                persona.setId(idPerson);
                if (db_dao.updatePersona(persona) != 0){
                    cleanForm();
                    Toast.makeText(this, "Se actualizo correctamente la persona", Toast.LENGTH_LONG).show();
                    getPerson();
                    personAdapter.personaList = this.personaList;
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(this, "No se pudo actualizar la persona", Toast.LENGTH_LONG).show();
                }

            }
            SaveEdit = 0;
        }
    }

    private void cleanForm (){
        edtId.setText("");
        edtName.setText("");
        edtLastName.setText("");
        edtEmail.setText("");
        edtId.requestFocus();
    }
}
