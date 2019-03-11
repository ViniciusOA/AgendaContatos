package br.senai.sp.agendacontatos;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import java.util.List;
import br.senai.sp.agendacontatos.dao.ContatoDAO;
import br.senai.sp.agendacontatos.modelo.Contato;

public class MainActivity extends AppCompatActivity {

    private ListView listaContatos;
    private ImageButton btnNovoContato;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // *** Associa o objeto ListView do java à View ListView do Layout xml
        listaContatos = findViewById(R.id.list_contatos);
        btnNovoContato = findViewById(R.id.btn_novo_contato);

        // *** Ação do botão novo
        btnNovoContato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent cadastroContatos = new Intent(MainActivity.this, CadastroContatoActivity.class);
                startActivity(cadastroContatos);

            }
        });

        // *** DEFINIÇÃO DE UM MENU CONTEXTO PARA A LISTVIEW (lista de filmes)
        registerForContextMenu(listaContatos);

        listaContatos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                Contato contato = (Contato) listaContatos.getItemAtPosition(position);
                Intent cadastro = new Intent(MainActivity.this, CadastroContatoActivity.class);
                cadastro.putExtra("contato", contato);
                startActivity(cadastro);
            }
        });

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_context_lista_contatos, menu);

        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(final MenuItem item) {

        ContextMenu.ContextMenuInfo menuInfo = item.getMenuInfo();
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        final Contato contato = (Contato) listaContatos.getItemAtPosition(info.position);
        final ContatoDAO dao = new ContatoDAO(MainActivity.this);


        new AlertDialog.Builder(this).setTitle("Apagar filme").setMessage("Deseja apagar " + contato.getNome() + " ?").setPositiveButton("sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(MainActivity.this, contato.getNome() + " excluido", Toast.LENGTH_LONG).show();
                dao.excluir(contato);
                dao.close();
                carregarLista();
            }
        }).setNegativeButton("não", null).show();

        return super.onContextItemSelected(item);
    }


    @Override
    protected void onResume() {
        carregarLista();
        super.onResume();
    }

    private void carregarLista(){

        ContatoDAO dao = new ContatoDAO(this);
        List<Contato> contatos = dao.getContatos();
        dao.close();

        ArrayAdapter<Contato> listaContatosAdapter = new ArrayAdapter<Contato>(this, android.R.layout.simple_list_item_1, contatos);

        listaContatos.setAdapter(listaContatosAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

}
