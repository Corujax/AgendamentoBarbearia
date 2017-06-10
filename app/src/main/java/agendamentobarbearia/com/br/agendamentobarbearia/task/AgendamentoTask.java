package agendamentobarbearia.com.br.agendamentobarbearia.task;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONObject;

import agendamentobarbearia.com.br.agendamentobarbearia.dao.AgendamentoDAO;
import agendamentobarbearia.com.br.agendamentobarbearia.model.Agendamento;
import agendamentobarbearia.com.br.agendamentobarbearia.ws.WebRequest;

/**
 * Created by Marcello on 10/06/2017.
 */

public class AgendamentoTask extends AsyncTask<String, Object, Long> {
    private final Activity activity;
    private final Agendamento agendamento;
    private ProgressDialog progress;

    private static final String ID = "id";

    public AgendamentoTask(Activity activity, Agendamento agendamento){
        this.activity = activity;
        this.agendamento = agendamento;
    }

    @Override
    protected void onPreExecute(){
        progress = ProgressDialog.show(activity, "Aguarde...", "Enviando dados!!!", true);
    }

    @Override
    protected Long doInBackground(String... params){
        try {
            WebRequest request = new WebRequest();
            String jsonResult  = request.save(agendamento);
            JSONObject jsonObject = new JSONObject(jsonResult);
            return jsonObject.getLong(ID);
        } catch (Exception e){
            return 0L;
        }
    }

    @Override
    protected void onPostExecute(Long id){
        if(id == 0){
            Toast.makeText(activity, "Houve um erro ao salvar o agendamento", Toast.LENGTH_LONG).show();
        } else {
            agendamento.setId(id);
            agendamento.setNovo(false);
            AgendamentoDAO dao = new AgendamentoDAO(activity);
            dao.update(agendamento);
            dao.close();
        }
        progress.dismiss();
        activity.finish();
    }
}
