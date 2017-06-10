package agendamentobarbearia.com.br.agendamentobarbearia.task;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONObject;

import agendamentobarbearia.com.br.agendamentobarbearia.MainActivity;
import agendamentobarbearia.com.br.agendamentobarbearia.dao.AgendamentoDAO;
import agendamentobarbearia.com.br.agendamentobarbearia.model.Agendamento;
import agendamentobarbearia.com.br.agendamentobarbearia.ws.WebRequest;

/**
 * Created by Marcello on 10/06/2017.
 */

public class DeleteAgendamentoTask extends AsyncTask<String, Object, Boolean> {
    private final MainActivity activity;
    private final Agendamento agendamento;
    private ProgressDialog progress;

    private static final String QTDE = "qtde";

    public DeleteAgendamentoTask (MainActivity activity, Agendamento agendamento){
        this.activity = activity;
        this.agendamento = agendamento;
    }

    @Override
    protected void onPreExecute(){
        progress = ProgressDialog.show(activity, "Aguarde...", "Enviando dados!!!", true);
    }

    @Override
    protected Boolean doInBackground(String... params){
        try {
            WebRequest request = new WebRequest();
            String jsonResult = request.delete(agendamento.getId());
            JSONObject jsonObject = new JSONObject(jsonResult);
            return jsonObject.getLong(QTDE) > 0;
        } catch (Exception e){
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean statusOK){
        if(!statusOK){
            Toast.makeText(activity, "Houve um erroo remover o cliente", Toast.LENGTH_LONG).show();
        } else {
            AgendamentoDAO dao = new AgendamentoDAO(activity);
            dao.delete(agendamento.getId());
            dao.close();
        }
        activity.carregaLista();
        progress.dismiss();
    }
}
