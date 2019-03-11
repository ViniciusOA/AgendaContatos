package br.senai.sp.agendacontatos;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import br.senai.sp.agendacontatos.dao.ContatoDAO;
import br.senai.sp.agendacontatos.modelo.Contato;

public class CadastroContatoActivity extends AppCompatActivity {

    private CadastroContatoHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_contato);

        helper = new CadastroContatoHelper(CadastroContatoActivity.this);

        Intent intent = getIntent();
        Contato contato = (Contato) intent.getSerializableExtra("contato");


        if (contato != null){
            helper.preecherFormulario(contato);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_cadastro_contatos, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.menu_salvar:

                Contato contato = helper.getContato();

                ContatoDAO dao = new ContatoDAO(this);

                if (contato.getId() == 0){

                    if (helper.validar()){

                        dao.salvar(contato);
                        dao.close();
                        Toast.makeText(this, contato.getNome() + " gravado com sucesso!", Toast.LENGTH_LONG).show();
                        finish();

                    }else {

                        Toast.makeText(CadastroContatoActivity.this, "Preecha os campos", Toast.LENGTH_LONG).show();

                    }
                } else {

                    if (helper.validar()){

                        dao.atualizar(contato);
                        dao.close();
                        Toast.makeText(this, contato.getNome() + " atualizado com sucesso!", Toast.LENGTH_LONG).show();
                        finish();

                    }else {

                        Toast.makeText(CadastroContatoActivity.this, "Preecha os campos", Toast.LENGTH_LONG).show();

                    }

                }

                break;
            case R.id.menu_cancelar:
                finish();
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
