package agendamentobarbearia.com.br.agendamentobarbearia;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;

import agendamentobarbearia.com.br.agendamentobarbearia.adapter.AgendamentoAdapter;
import agendamentobarbearia.com.br.agendamentobarbearia.dao.AgendamentoDAO;
import agendamentobarbearia.com.br.agendamentobarbearia.model.Agendamento;
import agendamentobarbearia.com.br.agendamentobarbearia.task.DeleteAgendamentoTask;
import agendamentobarbearia.com.br.agendamentobarbearia.task.ListaAgendamentoTask;
import agendamentobarbearia.com.br.agendamentobarbearia.util.Util;

public class MainActivity extends AppCompatActivity {

    private ListView listaAgendamento;
    private AgendamentoAdapter adapter;
    private List<Agendamento> agendamentos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            if(Util.isConnected(this)){
                new ListaAgendamentoTask(this).execute();
            }
        } catch (Exception e){
        }

        listaAgendamento = (ListView) findViewById(R.id.listaAgendamento);
        listaAgendamento.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Agendamento agendamentoSelecionado = adapter.getItem(position);
                Intent cadAgendamento = new Intent(MainActivity.this, AgendamentoActivity.class);
                cadAgendamento.putExtra("agendamentoSelecionado", agendamentoSelecionado);
                startActivity(cadAgendamento);
            }
        });
        Button btnNovoAgendamento = (Button) findViewById(R.id.novoAgendamento);
        btnNovoAgendamento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AgendamentoActivity.class);
                startActivity(intent);
            }
        });
        registerForContextMenu(listaAgendamento);
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregaLista();
    }


    public void carregaLista() {
        AgendamentoDAO dao = new AgendamentoDAO(this);
        agendamentos = dao.list();
        dao.close();

        adapter = new AgendamentoAdapter(this, agendamentos);
        listaAgendamento.setAdapter(adapter);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        final Agendamento agendamentoSelecionado = adapter.getItem(info.position);
        MenuItem deletar = menu.add("Deletar");
        deletar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                confirmaRemocao(agendamentoSelecionado);
                return false;
            }
        });
    }

    private void confirmaRemocao(final Agendamento agendamentoSelecionado) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Remoção");
        dialogBuilder.setMessage(
                String.format("Confirma a remoção do agendamento do cliente %s?",
                        agendamentoSelecionado.getNome()));
        dialogBuilder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                /*
                AgendamentoDAO dao = new AgendamentoDAO(MainActivity.this);
                dao.delete(agendamentoSelecionado.getId());
                dao.close();
                carregaLista();
                */
                dialog.cancel();
                new DeleteAgendamentoTask(MainActivity.this, agendamentoSelecionado).execute();
            }
        });
        dialogBuilder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
            }
        });
        dialogBuilder.create().show();
    }
}
