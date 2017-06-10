package agendamentobarbearia.com.br.agendamentobarbearia.converter;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import agendamentobarbearia.com.br.agendamentobarbearia.model.Agendamento;

/**
 * Created by Marcello on 10/06/2017.
 */

public class AgendamentoConverter {
    private ObjectMapper mapper = new ObjectMapper();

    public Agendamento fromJson(JSONObject jsonObject){
        Agendamento obj;
        try {
            obj = mapper.readValue(jsonObject.toString(), Agendamento.class);
            obj.setNovo(false);
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return obj;
    }

    public List<Agendamento> fromJson(JSONArray jsonArray){
        List<Agendamento> list = new ArrayList<Agendamento>();
        for(int i=0; i < jsonArray.length(); i++){
            JSONObject objectJson = null;
            try {
                objectJson = jsonArray.getJSONObject(i);
            } catch (Exception e){
                e.printStackTrace();
                continue;
            }
            Agendamento obj = fromJson(objectJson);
            if(obj != null){
                list.add(obj);
            }
        }
        return list;
    }
}
