package agendamentobarbearia.com.br.agendamentobarbearia.task;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONArray;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import agendamentobarbearia.com.br.agendamentobarbearia.MainActivity;
import agendamentobarbearia.com.br.agendamentobarbearia.converter.AgendamentoConverter;
import agendamentobarbearia.com.br.agendamentobarbearia.dao.AgendamentoDAO;
import agendamentobarbearia.com.br.agendamentobarbearia.model.Agendamento;
import agendamentobarbearia.com.br.agendamentobarbearia.util.ImageUtil;
import agendamentobarbearia.com.br.agendamentobarbearia.ws.WebRequest;

/**
 * Created by Marcello on 10/06/2017.
 */

public class ListaAgendamentoTask extends AsyncTask<String, Object, Boolean> {
    private final MainActivity activity;
    private ProgressDialog progress;

    public ListaAgendamentoTask(MainActivity activity){
        this.activity = activity;
    }

    @Override
    protected void onPreExecute(){
        progress = ProgressDialog.show(activity, "Aguarde...", "Obtendo dados!!!", true);
    }

    private Bitmap downloadImageBitmap(String url){
        Bitmap bitmap = null;
        try {
            InputStream inputStream = new URL(url).openStream();
            bitmap = BitmapFactory.decodeStream(inputStream);
            inputStream.close();
        } catch (Exception e){
            e.printStackTrace();
        }
        return bitmap;
    }

    @Override
    protected Boolean doInBackground(String... params){
        try {
            WebRequest request = new WebRequest();
            String jsonResult = request.list();
            JSONArray jsonArray = new JSONArray(jsonResult);
            List<Agendamento> agendamentos = new AgendamentoConverter().fromJson(jsonArray);
            if(agendamentos != null && !agendamentos.isEmpty()){
                AgendamentoDAO dao = new AgendamentoDAO(activity);
                FileOutputStream fos;
                Bitmap image;
                for(Agendamento agendamento : agendamentos){
                    if(agendamento.getFotoAntes() != null){
                        agendamento.setFotoAntes(activity.getExternalFilesDir(null) + "/" + agendamento.getFotoAntes());
                    }
                    if(agendamento.getFotoDepois() != null){
                        agendamento.setFotoDepois(activity.getExternalFilesDir(null) + "/" + agendamento.getFotoDepois());
                    }
                    if(dao.findById(agendamento.getId()) == null){
                        agendamento.setImporting(true);
                        if(agendamento.getImageAntes() != null && !agendamento.getImageAntes().equals("")){
                            ImageUtil.saveImage(agendamento.getImageAntes(), agendamento.getFotoAntes());
                        }
                        if(agendamento.getImageDepois() != null && !agendamento.getImageDepois().equals("")){
                            ImageUtil.saveImage(agendamento.getImageDepois(), agendamento.getFotoDepois());
                        }
                        dao.insert(agendamento);
                    } else {
                        dao.close();
                    }
                }
                dao.close();
            }
            return true;
        } catch (Exception e){
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean statusOK){
        if(!statusOK){
            Toast.makeText(activity, "Houve um erro ao obter a lista de agendamentos", Toast.LENGTH_LONG).show();
        } else {
            activity.carregaLista();
        }
        progress.dismiss();
    }
}
