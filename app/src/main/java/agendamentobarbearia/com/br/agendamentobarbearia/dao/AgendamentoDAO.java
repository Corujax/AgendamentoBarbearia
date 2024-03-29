package agendamentobarbearia.com.br.agendamentobarbearia.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import agendamentobarbearia.com.br.agendamentobarbearia.model.Agendamento;
import agendamentobarbearia.com.br.agendamentobarbearia.model.Procedimento;

/**
 * Created by Marcello on 25/05/2017.
 */

public class AgendamentoDAO extends SQLiteOpenHelper {

    final static String DATABASE = "AGENDAMENTOS";
    final static int VERSION = 1;
    final static String TABLE = "agendamento";

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmm");
    private Calendar calendar;

    public AgendamentoDAO(Context context) {
        super(context, DATABASE, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String ddlCliente = "create table " + TABLE + " (" +
                "id integer not null primary key autoincrement," +
                "nome text not null, " +
                "telefone text, " +
                "fotoAntes text," +
                "fotoDepois text," +
                "dataHora integer," +
                "procedimento text," +
                "novo integer);";
        db.execSQL(ddlCliente);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private ContentValues getValues(Agendamento agendamento) {
        ContentValues values = new ContentValues();

        if(agendamento.isImporting()){
            values.put("id", agendamento.getId());
        }
        values.put("nome", agendamento.getNome());
        values.put("telefone", agendamento.getTelefone());
        values.put("fotoAntes", agendamento.getFotoAntes());
        values.put("fotoDepois", agendamento.getFotoDepois());
        values.put("procedimento", agendamento.getProcedimento().name());
        values.put("dataHora", dateFormat.format(agendamento.getDataHora().getTime()));
        values.put("novo", agendamento.isNovo() ? 1:0);
        return values;
    }

    public void insert(Agendamento agendamento) {
        getWritableDatabase().insert(TABLE, null, getValues(agendamento));
    }

    public void update(Agendamento agendamento) {
        String[] args = { String.valueOf(agendamento.getId())};
        getWritableDatabase().update(TABLE, getValues(agendamento), "id=?", args);
    }

    public void delete (Long id) {
        String[] args = { id.toString() };
        getWritableDatabase().delete (TABLE, "id=?", args);
    }

    public Agendamento findById(Long id) {
        String[] args = { String.valueOf(id) };
        Cursor c = getReadableDatabase()
                .rawQuery("SELECT * FROM " + TABLE +  " where id = ?", args);
        Agendamento agendamento = null;
        if (c.moveToNext()) {
            agendamento = fill(c);
        }
        c.close();
        return agendamento;
    }

    public List<Agendamento> list() {
        Cursor c = getReadableDatabase()
                .rawQuery("SELECT * FROM " + TABLE +  " order by dataHora desc", null);
        List<Agendamento> list = new ArrayList<>();
        while (c.moveToNext()) {
            list.add(fill(c));
        }
        c.close();
        return list;
    }

    public boolean verificaDisponibilidadeAgendamento(Agendamento agendamento){
        Calendar dataHoraAnterior = Calendar.getInstance();
        dataHoraAnterior.setTime(agendamento.getDataHora().getTime());
        dataHoraAnterior.add(Calendar.MINUTE, -30);

        Calendar dataHoraPosterior = Calendar.getInstance();
        dataHoraPosterior.setTime(agendamento.getDataHora().getTime());
        dataHoraPosterior.add(Calendar.MINUTE, 30);

        String[] args = {
                String.valueOf(agendamento.getId()),
                dateFormat.format(dataHoraAnterior.getTime()),
                dateFormat.format(dataHoraPosterior.getTime())
        };

        Cursor c = getReadableDatabase()
                .rawQuery("SELECT count(*) as qtde FROM " + TABLE +
                " where id <> ? and dataHora > ?  and dataHora < ?", args);
        long qtde = 0;
        if(c.moveToNext()){
            qtde = c.getLong(c.getColumnIndex("qtde"));
        }
        c.close();
        return qtde == 0;
    }

    private Agendamento fill(Cursor c) {
        Agendamento agendamento = new Agendamento();
        agendamento.setId(c.getLong(c.getColumnIndex("id")));
        agendamento.setNome(c.getString(c.getColumnIndex("nome")));
        agendamento.setTelefone(c.getString(c.getColumnIndex("telefone")));
        agendamento.setFotoAntes(c.getString(c.getColumnIndex("fotoAntes")));
        agendamento.setFotoDepois(c.getString(c.getColumnIndex("fotoDepois")));
        calendar = Calendar.getInstance();
        try {
            calendar.setTime(dateFormat.parse(c.getString(c.getColumnIndex("dataHora"))));
        }
        catch (Exception e) {
            calendar.setTime(new Date());
        }
        agendamento.setDataHora(calendar);
        agendamento.setProcedimento(Procedimento.valueOf(c.getString(c.getColumnIndex("procedimento"))));
        agendamento.setNovo(c.getInt(c.getColumnIndex("novo"))==1);
        agendamento.setImporting(false);
        return agendamento;
    }
}
